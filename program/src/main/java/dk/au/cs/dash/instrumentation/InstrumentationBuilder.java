package dk.au.cs.dash.instrumentation;

import dk.au.cs.dash.Dash;
import dk.au.cs.dash.cfg.Op.AssumeOp;
import dk.au.cs.dash.cfg.Op.ErrorOp;
import dk.au.cs.dash.cfg.Op.ReturnOp;
import dk.au.cs.dash.util.Debug;
import dk.au.cs.dash.util.JimpleConverter;
import soot.*;
import soot.jimple.*;
import soot.jimple.internal.*;

import java.util.ArrayList;
import java.util.List;

public class InstrumentationBuilder {
    private static final String DASH_PREFIX = "_d";
    private static final String INSTRUMENTED_SUFFIX = DASH_PREFIX + "I";
    public static final String HELPER_FIELD_NAME = DASH_PREFIX + "H";
    private static final String HELPER_LOCAL_NAME = "helper";

    private final SootClass toInstrument;
    private final SootClass instrumented;
    private Local helper;

    public static String getInstrumentedName(String className) {
        return className + INSTRUMENTED_SUFFIX;
    }

    public InstrumentationBuilder(SootClass toInstrument) {
        this.toInstrument = toInstrument;
        instrumented = new SootClass(getInstrumentedName(toInstrument.getName()), toInstrument.getModifiers());
    }

    public void instrument() {
        Debug.toJimpleFile(toInstrument);
        Scene.v().forceResolve(InstrumentationHelper.class.getName(), SootClass.SIGNATURES);
        Scene.v().addClass(instrumented);
        instrumented.addField(new SootField(HELPER_FIELD_NAME, Scene.v().getRefType(InstrumentationHelper.class.getName()), Modifier.PUBLIC | Modifier.STATIC));
        instrumented.setSuperclass(toInstrument.getSuperclass());

        for (SootMethod sootMethod : toInstrument.getMethods())
        {
            SootMethod method = createMethod(sootMethod);
            if(!sootMethod.isConstructor())
                instrumentMethod(method);
            instrumented.addMethod(method);
        }

        Debug.toJimpleFile(instrumented);
        JimpleConverter.toClassFile(instrumented);
    }

    private SootMethod createMethod(SootMethod method) {
        SootMethod instrumentedMethod = new SootMethod(method.getName(), method.getParameterTypes(), method.getReturnType(), method.getModifiers(), method.getExceptions());
        JimpleBody body = (JimpleBody) method.getActiveBody().clone();
        instrumentedMethod.setActiveBody(body);
        return instrumentedMethod;
    }

    private void instrumentMethod(SootMethod methodByName) {
        Body body = methodByName.getActiveBody();
        PatchingChain<Unit> units = body.getUnits();
        Unit current = units.getFirst();
        helper = Jimple.v().newLocal(HELPER_LOCAL_NAME, Scene.v().getRefType(InstrumentationHelper.class.getName()));
        body.getLocals().add(helper);

        ArrayList<JimpleLocal> identityLocals = new ArrayList<>();

        while (current != null && current instanceof JIdentityStmt) {
            AbstractDefinitionStmt def = (AbstractDefinitionStmt) current;
            JimpleLocal local = (JimpleLocal) def.getLeftOpBox().getValue();
            identityLocals.add(local);
            current = units.getSuccOf(current);
        }
        current = loadHelperFieldToLocalVariable(current, units);
        for (JimpleLocal local : identityLocals) {
            current = addAssignInstrumentation(current, units, local, "loadParam");
        }
        current = addCallToInstrumentationHelper(current, units, "loadParamFinished");

        current = units.getSuccOf(current);
        while (current != null) {
            Unit next = units.getSuccOf(current);
            instrumentStatement(current, units);
            current = next;
        }
    }

    private void instrumentStatement(Unit current, PatchingChain<Unit> units) {
        if (current instanceof AbstractDefinitionStmt) {
            AbstractDefinitionStmt def = (AbstractDefinitionStmt) current;
            JimpleLocal local = (JimpleLocal) def.getLeftOpBox().getValue();
            Value rightOp = def.getRightOp();

            if(!(rightOp instanceof JStaticInvokeExpr)) {
                addAssignInstrumentation(current, units, local, "assign");
            } else {
                transformAndInstrumentAssignWithCall(local, (JStaticInvokeExpr) rightOp, current, units, local);
                patchUpCallsToCurrentInstructionToHitPredecessorInstrumentedInstruction(current, units);
            }
        } else if (current instanceof JIfStmt) {
            addTickInstrumentation(current, units, AssumeOp.class.getSimpleName());
            patchUpCallsToCurrentInstructionToHitPredecessorInstrumentedInstruction(current, units);
        } else if (current instanceof JReturnStmt || current instanceof JReturnVoidStmt) {
            addTickInstrumentation(current, units, ReturnOp.class.getSimpleName());
            patchUpCallsToCurrentInstructionToHitPredecessorInstrumentedInstruction(current, units);
        } else if (current instanceof JInvokeStmt) {
            JInvokeStmt inv = (JInvokeStmt) current;
            InvokeExpr invokeExpr = inv.getInvokeExpr();
            if(invokeExpr instanceof JStaticInvokeExpr)
            {
                JStaticInvokeExpr staticInvokeExpr = (JStaticInvokeExpr) invokeExpr;
                SootMethodRef m = staticInvokeExpr.getMethodRef();
                if(m.declaringClass().getName().equals(Dash.class.getName())) {
                    if(m.name().equals("error")) {
                        addTickInstrumentationAfter(current, units, ErrorOp.class.getSimpleName());
                    } else {
                        throw new RuntimeException(m.toString());
                    }
                } else
                {
                    throw new RuntimeException();
                }
            } else
            {
                throw new RuntimeException(current.toString());
            }

            //patchUpCallsToCurrentInstructionToHitPredecessorInstrumentedInstruction(current, units);
        } else if (current instanceof JGotoStmt) {

        } else {
            throw new RuntimeException("Unknown instruction: " + current);
        }
    }

    private void transformAndInstrumentAssignWithCall(JimpleLocal jimpleLocal, JStaticInvokeExpr staticInvoke, Unit current, PatchingChain<Unit> units, JimpleLocal local) {
        SootMethodRef m = staticInvoke.getMethodRef();
        if(m.declaringClass().getName().equals(Dash.class.getName())) {
            if(m.name().equals("nondet")) {
                SootClass helperClass = Scene.v().loadClass(InstrumentationHelper.class.getName(), SootClass.SIGNATURES);
                AssignStmt assignNondet = Jimple.v().newAssignStmt(jimpleLocal, Jimple.v().newVirtualInvokeExpr(helper, helperClass.getMethodByName("nondet").makeRef()));
                units.insertAfter(assignNondet, current);
                addAssignInstrumentation(assignNondet, units, local, "assign");
                units.remove(current);
            } else {
                throw new RuntimeException(m.toString());
            }
        } else {

            SootClass classRef = new SootClass(getInstrumentedName(m.declaringClass().getName()));
            //noinspection unchecked
            staticInvoke.setMethodRef(Scene.v().makeMethodRef(classRef, m.name(), m.parameterTypes(), m.returnType(), m.isStatic()));
            addCallToInstrumentationHelper(units.getPredOf(current), units, "assignWithCall");
            addAssignInstrumentation(current, units, local, "assignWithCallEnd");
        }
    }

    private void addTickInstrumentation(Unit current, PatchingChain<Unit> units, String opName) {
        SootClass helperClass = Scene.v().loadClass(InstrumentationHelper.class.getName(), SootClass.SIGNATURES);
        List<Type> parameterTypes = new ArrayList<>();
        parameterTypes.add(RefType.v(String.class.getName()));

        SootMethodRef methodRef = helperClass.getMethod("tick", parameterTypes).makeRef();
        VirtualInvokeExpr invoke = Jimple.v().newVirtualInvokeExpr(helper, methodRef, StringConstant.v(opName));

        InvokeStmt inserted = Jimple.v().newInvokeStmt(invoke);
        units.insertAfter(inserted, units.getPredOf(current));
    }

    private void addTickInstrumentationAfter(Unit current, PatchingChain<Unit> units, String opName) {
        SootClass helperClass = Scene.v().loadClass(InstrumentationHelper.class.getName(), SootClass.SIGNATURES);
        List<Type> parameterTypes = new ArrayList<>();
        parameterTypes.add(RefType.v(String.class.getName()));

        SootMethodRef methodRef = helperClass.getMethod("tick", parameterTypes).makeRef();
        VirtualInvokeExpr invoke = Jimple.v().newVirtualInvokeExpr(helper, methodRef, StringConstant.v(opName));

        InvokeStmt inserted = Jimple.v().newInvokeStmt(invoke);
        units.insertAfter(inserted, current);
    }

    private void patchUpCallsToCurrentInstructionToHitPredecessorInstrumentedInstruction(Unit current, PatchingChain<Unit> units) {
        while (!current.getBoxesPointingToThis().isEmpty()) {
            UnitBox unitBox = current.getBoxesPointingToThis().get(0);
            unitBox.setUnit(units.getPredOf(current));
        }
    }

    private InvokeStmt addCallToInstrumentationHelper(Unit toInsertAfter, PatchingChain<Unit> units, String methodName) {
        SootClass helperClass = Scene.v().loadClass(InstrumentationHelper.class.getName(), SootClass.SIGNATURES);
        InvokeStmt inserted = Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(helper, helperClass.getMethodByName(methodName).makeRef()));
        units.insertAfter(inserted, toInsertAfter);
        return inserted;
    }

    private InvokeStmt addAssignInstrumentation(Unit current, PatchingChain<Unit> units, JimpleLocal local, String helpClassMethodName) {
        SootClass helperClass = Scene.v().loadClass(InstrumentationHelper.class.getName(), SootClass.SIGNATURES);
        List<Type> parameterTypes = new ArrayList<>();
        parameterTypes.add(RefType.v(String.class.getName()));
        if (local.getType() instanceof RefType)
            parameterTypes.add(RefType.v(Object.class.getName()));
        else
            parameterTypes.add(local.getType());
        InvokeStmt assign = Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(helper, helperClass.getMethod(helpClassMethodName, parameterTypes).makeRef(), StringConstant.v(local.getName()), local));
        units.insertAfter(assign, current);
        return assign;
    }

    private AssignStmt loadHelperFieldToLocalVariable(Unit current, PatchingChain<Unit> units) {
        AssignStmt toInsert = Jimple.v().newAssignStmt(helper, Jimple.v().newStaticFieldRef(instrumented.getFieldByName(HELPER_FIELD_NAME).makeRef()));
        units.insertBefore(toInsert, current);
        return toInsert;
    }
}
