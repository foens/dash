package dk.au.cs.dash.cfg;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.Dash;
import dk.au.cs.dash.cfg.Op.*;
import dk.au.cs.dash.util.DashConstants;
import soot.*;
import soot.jimple.*;
import soot.jimple.internal.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class UnitToOpConverter {
    private final ErrorChooser errorChooser;
    private final Context ctx;

    public UnitToOpConverter(ErrorChooser errorChooser, Context ctx) {
        this.errorChooser = requireNonNull(errorChooser);
        this.ctx = requireNonNull(ctx);
    }

    public Op convert(Unit u, Unit childOfU) {
        if (u instanceof AbstractDefinitionStmt)
            return convertAssign((AbstractDefinitionStmt) u);
        else if (u instanceof IfStmt)
            return convertIf((JIfStmt) u, childOfU);
        else if (errorChooser.isError(u))
            return new ErrorOp();
        else if (u instanceof JReturnVoidStmt)
            return new ReturnOp();
        else if (u instanceof JReturnStmt)
            return convertReturn((JReturnStmt) u);
        else if (u instanceof JInvokeStmt)
            return convertInvoke((JInvokeStmt) u);
        throw new RuntimeException(u.toString());
    }

    private Op convertAssign(AbstractDefinitionStmt assign) {
        BitVecExpr lValue = convertLValue(assign.getLeftOp());
        Value rightOp = assign.getRightOp();
        if (rightOp instanceof JStaticInvokeExpr)
            return convertStaticInvokeInsideAssign(lValue, (JStaticInvokeExpr) rightOp);
        return new AssignOp(lValue, convertToExpression(rightOp));
    }

    private BitVecExpr convertLValue(Value v) {
        JimpleLocal local = (JimpleLocal) v;
        Type localType = local.getType();
        if (localType instanceof IntegerType)
            return ctx.mkBVConst(local.getName(), DashConstants.BITS_IN_INT);
        throw new RuntimeException();
    }

    private Op convertStaticInvokeInsideAssign(BitVecExpr lValue, JStaticInvokeExpr staticInvokeExpr) {
        SootMethod method = staticInvokeExpr.getMethod();
        if (isFromMainDashClass(method))
            return handleSpecialMethodsFromDashClass(lValue, method);
        return new AssignWithCallOp(lValue, convertStaticInvokeExpr(staticInvokeExpr));
    }

    private boolean isFromMainDashClass(SootMethod method) {
        return method.getDeclaringClass().getName().equals(Dash.class.getName());
    }

    private Op handleSpecialMethodsFromDashClass(BitVecExpr name, SootMethod method) {
        BitVecExpr value;
        if (method.getName().equals("nondet")) {
            value = ctx.mkBVConst("NON-DET", DashConstants.BITS_IN_INT);
            return new AssignOp(name, value);
        }
        throw new RuntimeException(method.toString());
    }

    private AssumeOp convertIf(JIfStmt statement, Unit afterFrontierStatement) {
        ConditionExpr expr = (ConditionExpr) statement.getCondition();
        BoolExpr predicate = convertCondition(expr);
        predicate = negateConditionIfTakingFalseBranch(statement, afterFrontierStatement, predicate);
        return new AssumeOp((BoolExpr) predicate.simplify());
    }

    private BoolExpr convertCondition(Value v) {
        ConditionExpr expr = (ConditionExpr) v;
        BitVecExpr exp1 = convertToExpression(expr.getOp1());
        BitVecExpr exp2 = convertToExpression(expr.getOp2());

        if (expr instanceof JEqExpr)
            return ctx.mkEq(exp1, exp2);
        else if (expr instanceof JNeExpr)
            return ctx.mkNot(ctx.mkEq(exp1, exp2));
        else if (expr instanceof JGtExpr)
            return ctx.mkBVSGT(exp1, exp2);
        else if (expr instanceof JLtExpr)
            return ctx.mkBVSLT(exp1, exp2);
        else if (expr instanceof JGeExpr)
            return ctx.mkBVSGE(exp1, exp2);
        else if (expr instanceof JLeExpr)
            return ctx.mkBVSLE(exp1, exp2);
        throw new RuntimeException(expr.toString());
    }

    private BoolExpr negateConditionIfTakingFalseBranch(JIfStmt statement, Unit afterFrontierStatement, BoolExpr predicate) {
        boolean isTakingFalseBranch = afterFrontierStatement != statement.getTargetBox().getUnit();
        if (isTakingFalseBranch)
            return ctx.mkNot(predicate);
        return predicate;
    }

    private Op convertReturn(JReturnStmt u) {
        BitVecExpr bitVecExpr = convertToExpression(u.getOp());
        return new ReturnOp(bitVecExpr);
    }

    private Op convertInvoke(JInvokeStmt invokeStm) {
        if (invokeStm.getInvokeExpr() instanceof JStaticInvokeExpr)
            return convertStaticInvokeExpr((JStaticInvokeExpr) invokeStm.getInvokeExpr());
        throw new RuntimeException(invokeStm.toString());
    }

    private CallOp convertStaticInvokeExpr(JStaticInvokeExpr staticInvoke) {
        SootMethod method = staticInvoke.getMethod();
        //noinspection unchecked
        List<Value> args = staticInvoke.getArgs();
        List<BitVecExpr> bitVecArguments = new ArrayList<>();
        for (Value arg : args)
            bitVecArguments.add(convertToExpression(arg));
        return new CallOp(method.getDeclaringClass().getName(), method.getName(), bitVecArguments);
    }

    private BitVecExpr convertToExpression(Value v) {
        if (v instanceof JimpleLocal)
            return convertLocal(v);
        else if (v instanceof IntConstant)
            return convertIntConstant(v);
        else if (v instanceof ThisRef)
            throw new RuntimeException();
        else if (v instanceof BinopExpr)
            return convertArithmeticBinop(v);
        else if (v instanceof JNegExpr)
            return ctx.mkBVNeg(convertToExpression(((JNegExpr) v).getOp()));
        throw new RuntimeException(v.toString());
    }

    private BitVecExpr convertLocal(Value v) {
        JimpleLocal local = (JimpleLocal) v;
        return ctx.mkBVConst(local.getName(), DashConstants.BITS_IN_INT);
    }

    private BitVecExpr convertIntConstant(Value v) {
        int constant = ((IntConstant) v).value;
        return ctx.mkBV(constant, DashConstants.BITS_IN_INT);
    }

    private BitVecExpr convertArithmeticBinop(Value v) {
        BinopExpr binop = (BinopExpr) v;
        BitVecExpr op1 = convertOp(binop.getOp1());
        BitVecExpr op2 = convertOp(binop.getOp2());
        if (binop instanceof JXorExpr)
            return ctx.mkBVXOR(op1, op2);
        else if (binop instanceof JDivExpr)
            return ctx.mkBVSDiv(op1, op2);
        else if (binop instanceof JMulExpr)
            return ctx.mkBVMul(op1, op2);
        else if (binop instanceof JShrExpr)
            return ctx.mkBVASHR(op1, op2);
        else if (binop instanceof JAddExpr)
            return ctx.mkBVAdd(op1, op2);
        else if (binop instanceof JUshrExpr)
            return ctx.mkBVLSHR(op1, op2);
        else if (binop instanceof JAndExpr)
            return ctx.mkBVAND(op1, op2);
        else if (binop instanceof JRemExpr)
            return ctx.mkBVSRem(op1, op2);
        else if (binop instanceof JShlExpr)
            return ctx.mkBVSHL(op1, op2);
        else if (binop instanceof JSubExpr)
            return ctx.mkBVSub(op1, op2);
        else if (binop instanceof JOrExpr)
            return ctx.mkBVOR(op1, op2);
        throw new RuntimeException();
    }

    private BitVecExpr convertOp(Value op) {
        if (op instanceof JimpleLocal)
            return convertLocal(op);
        else if (op instanceof IntConstant)
            return convertIntConstant(op);
        throw new RuntimeException();
    }
}
