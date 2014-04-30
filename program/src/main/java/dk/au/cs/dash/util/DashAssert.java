package dk.au.cs.dash.util;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.Parameter;
import dk.au.cs.dash.cfg.Region;
import dk.au.cs.dash.instrumentation.Variables;
import dk.au.cs.dash.symbolic.RefinePredicate;
import dk.au.cs.dash.symbolic.SATSolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DashAssert {
    private static final boolean DISABLED = false;

    public static void assertRefinePredMakesMakesADifference(BoolExpr p, Region skMinusOne, Context ctx) {
        if(DISABLED)
            return;
        BoolExpr boolExpr = ctx.mkNot(ctx.mkIff(p, skMinusOne.predicate));
        if(!checkIsSat(boolExpr, ctx)) {
            throw new RuntimeException("refine predicate and sk_pred is equivalent: " + p + " #### " + skMinusOne.predicate);
        }
    }

    private static boolean checkIsSat(BoolExpr boolExpr, Context ctx) {
        TimeTracer.start("refineGraph.checkEqv");
        Variables v = new SATSolver(boolExpr, new ArrayList<Parameter>(), ctx).run();
        TimeTracer.end("refineGraph.checkEqv");
        return v != null;
    }

    public static void assertCtxSolverSimplifyIsGoodEnough(BoolExpr predicate, boolean couldBeUnsatisfiable, BoolExpr result, Context ctx) {
        if(DISABLED)
            return;
        if(couldBeUnsatisfiable && !result.isFalse())
        {
            TimeTracer.start("refineGraph.IsSAT");
            Variables v = new SATSolver(predicate, new ArrayList<Parameter>(), ctx).run();
            TimeTracer.end("refineGraph.IsSAT");
            if(v == null)
                throw new RuntimeException("CTX not good enough");
        }
    }

    public static class CheckUniquenessOfInputToMethod {
        private final Set<InputPair> inputValues = new HashSet<>();

        public void check(Variables variables, ArrayList<Boolean> nonDetChoices) {
            if(DISABLED)
                return;

            InputPair inputPair = new InputPair(variables, nonDetChoices);

            if (inputValues.contains(inputPair))
                throw new RuntimeException("Input values has been tried before=" + inputPair);
            inputValues.add(inputPair);
        }

        private static class InputPair {
            private Variables variables;
            private ArrayList<Boolean> nonDetChoices;

            private InputPair(Variables variables, ArrayList<Boolean> nonDetChoices) {
                this.variables = variables;
                this.nonDetChoices = nonDetChoices;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                InputPair inputPair = (InputPair) o;

                if (nonDetChoices != null ? !nonDetChoices.equals(inputPair.nonDetChoices) : inputPair.nonDetChoices != null)
                    return false;
                if (variables != null ? !variables.equals(inputPair.variables) : inputPair.variables != null)
                    return false;

                return true;
            }

            @Override
            public int hashCode() {
                int result = variables != null ? variables.hashCode() : 0;
                result = 31 * result + (nonDetChoices != null ? nonDetChoices.hashCode() : 0);
                return result;
            }

            @Override
            public String toString() {
                return "InputPair{" +
                        "variables=" + variables +
                        ", nonDetChoices=" + nonDetChoices +
                        '}';
            }
        }
    }

    public static void noConcreteRunsInAfterFrontier(Region afterFrontier) {
        if(DISABLED)
            return;

        if (!afterFrontier.isStateEmpty())
            throw new RuntimeException("AfterFrontier has concrete states");
    }
}
