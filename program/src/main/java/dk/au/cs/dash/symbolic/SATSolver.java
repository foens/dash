package dk.au.cs.dash.symbolic;

import com.microsoft.z3.*;
import dk.au.cs.dash.Parameter;
import dk.au.cs.dash.instrumentation.Variables;
import dk.au.cs.dash.util.DashConstants;
import dk.au.cs.dash.util.TimeTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.BooleanType;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class SATSolver {
    private static final Logger logger = LoggerFactory.getLogger(SATSolver.class);
    private final BoolExpr pathConstraint;
    private final List<Parameter> parameters;
    private Context context;
    private Solver solver;

    public SATSolver(BoolExpr pathConstraint, List<Parameter> parameters, Context ctx) {
        this.parameters = requireNonNull(parameters);
        this.pathConstraint = requireNonNull(pathConstraint);
        this.context = requireNonNull(ctx);
    }

    public Variables run() {
        TimeTracer.start("SATSolver");
        if(logger.isTraceEnabled()) {
            logger.trace("Ø=" + pathConstraint);
        }
        solver = context.mkSolver();
        TimeTracer.start("SATSolver.addPredicates");
        solver.add(pathConstraint);
        addBoolPredLimits();
        TimeTracer.end("SATSolver.addPredicates");
        TimeTracer.start("SATSolver.IsSAT");
        Status status = solver.check();
        TimeTracer.end("SATSolver.IsSAT");
        Variables result;
        switch (status) {
            case SATISFIABLE:
                result = createSatisfiableResult(solver.getModel());
                break;
            case UNSATISFIABLE:
                result = createUnsatisfiableResult();
                break;
            default:
                throw new RuntimeException(status.toString());
        }
        TimeTracer.end("SATSolver");
        return result;
    }

    private void addBoolPredLimits() {
        // TODO Add type information for all variables (not just parameters)
        for (Parameter parameter : parameters) {
            if (parameter.type instanceof BooleanType) {
                solver.add(context.mkOr(new BoolExpr[]{
                        context.mkEq(context.mkBV(0, DashConstants.BITS_IN_INT), context.mkBVConst(ExecuteSymbolic.getInitialSymbolicVariableName(parameter.name), DashConstants.BITS_IN_INT)),
                        context.mkEq(context.mkBV(1, DashConstants.BITS_IN_INT), context.mkBVConst(ExecuteSymbolic.getInitialSymbolicVariableName(parameter.name), DashConstants.BITS_IN_INT))
                }));
            }
        }
    }

    private Variables createUnsatisfiableResult() {
        if(logger.isTraceEnabled())
            logger.trace(solver.toString());
        return null;
    }

    private Variables createSatisfiableResult(Model model) {
        if (logger.isTraceEnabled()) {
            logger.trace(solver.toString());
            logger.trace(model.toString());
        }
        TimeTracer.start("SATSolver.createSatisfiableResult");
        Variables variables = new Variables();
        for (FuncDecl constDecl : model.getConstDecls()) {
            String name = constDecl.getName().toString();
            BitVecNum bitVecNum = (BitVecNum) model.getConstInterp(constDecl);
            int value = (int) bitVecNum.getLong();
            variables.put(name, value);
        }
        for (Parameter parameter : parameters) {
            String parameterName = ExecuteSymbolic.getInitialSymbolicVariableName(parameter.name);
            if (!variables.contains(parameterName))
                variables.put(parameterName, 0);
        }
        TimeTracer.end("SATSolver.createSatisfiableResult");
        return variables;
    }
}
