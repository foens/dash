package dk.au.cs.dash;

import com.microsoft.z3.Context;
import dk.au.cs.dash.cfg.ErrorChooser;
import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.cfg.UnitGraphToGraphConverter;
import dk.au.cs.dash.cfg.UnitToOpConverter;
import dk.au.cs.dash.instrumentation.InstrumentationBuilder;
import dk.au.cs.dash.util.Debug;
import dk.au.cs.dash.util.SootLoader;
import dk.au.cs.dash.util.TimeTracer;
import soot.*;
import soot.jimple.internal.AbstractDefinitionStmt;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class ProgramPreparer {
    private final PreparedProgram preparedProgram = new PreparedProgram();
    private final Context context;
    private final UnitToOpConverter toOpConverter;
    private final ErrorChooser errorChooser;

    public ProgramPreparer(Context context) {
        this.context = requireNonNull(context);
        errorChooser = new ErrorChooser();
        toOpConverter = new UnitToOpConverter(errorChooser, context);
    }

    public PreparedProgram prepareProgram(String startingClass) {
        Set<String> preparedClasses = new HashSet<>();
        Set<String> referencedClasses = new HashSet<>();
        referencedClasses.add(startingClass);

        while(referencedClasses.size() > 0) {
            String next = referencedClasses.iterator().next();
            referencedClasses.addAll(prepareClass(next));
            preparedClasses.add(next);
            referencedClasses.removeAll(preparedClasses);
        }
        preparedProgram.setPreparedClasses(preparedClasses);
        return preparedProgram;
    }

    private Set<String> prepareClass(String className) {
        TimeTracer.start("loadClass");
        SootClass c = SootLoader.loadClass(className);
        TimeTracer.end("loadClass");
        TimeTracer.start("InstrumentationBuilder");
        new InstrumentationBuilder(c).instrument();
        TimeTracer.end("InstrumentationBuilder");
        prepareMethods(className, c);
        return findRelatedClasses(c);
    }

    private Set<String> findRelatedClasses(SootClass c) {
        Set<String> referencedClasses = new HashSet<>();
        for (SootMethod method : c.getMethods()) {
            if(!method.isConstructor()) {
                Body activeBody = method.getActiveBody();
                PatchingChain<Unit> units = activeBody.getUnits();
                for (Unit unit : units) {
                    if(unit instanceof AbstractDefinitionStmt && ((AbstractDefinitionStmt) unit).getRightOp() instanceof JStaticInvokeExpr) {
                        SootMethodRef methodRef = ((JStaticInvokeExpr) ((AbstractDefinitionStmt) unit).getRightOp()).getMethodRef();
                        if(!methodRef.declaringClass().getName().equals(Dash.class.getName()))
                            referencedClasses.add(methodRef.declaringClass().getName());
                    }
                }
            }
        }
        return referencedClasses;
    }

    private void prepareMethods(String className, SootClass c) {
        for (SootMethod method : c.getMethods()) {
            if (method.isConstructor())
                continue;
            preparedProgram.put(className, method.getName(), prepareMethod(method));
        }
    }

    private PreparedMethod prepareMethod(SootMethod method) {
        Body body = method.retrieveActiveBody();
        ExceptionalUnitGraph ug = new ExceptionalUnitGraph(body, UnitThrowAnalysis.v(), false);
        Debug.createDotOutput(ug);

        Graph g = new UnitGraphToGraphConverter(ug, errorChooser, toOpConverter, context).convert();
        Debug.createDotOutput(g);

        ArrayList<Parameter> parameters = getParameters(method);
        ArrayList<String> paramAndLocalNames = getParamAndLocalNames(body, parameters);
        return new PreparedMethod(g, paramAndLocalNames, parameters);
    }

    private ArrayList<Parameter> getParameters(SootMethod method) {
        ArrayList<Parameter> parameters = new ArrayList<>();
        for(int i = 0; i<method.getParameterCount(); i++) {
            Local parameterLocal = method.getActiveBody().getParameterLocal(i);
            Parameter parameter = new Parameter(parameterLocal.getType(), parameterLocal.getName());
            parameters.add(parameter);
        }
        return parameters;
    }

    private ArrayList<String> getParamAndLocalNames(Body body, List<Parameter> parameters) {
        ArrayList<String> paramAndLocalNames = new ArrayList<>();
        for (Local local : body.getLocals())
            paramAndLocalNames.add(local.getName());


        for (Parameter parameter : parameters) {
            //paramAndLocalNames.add(parameter.name);
            if(!paramAndLocalNames.contains(parameter.name))
                throw new RuntimeException(paramAndLocalNames + " --- " + parameter.name);
        }
        return paramAndLocalNames;
    }
}
