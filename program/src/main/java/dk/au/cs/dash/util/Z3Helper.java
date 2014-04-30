package dk.au.cs.dash.util;

import com.microsoft.z3.*;

import java.util.ArrayList;

public class Z3Helper {
    public static BoolExpr optimizeWithCtxSolverSimplify(BoolExpr boolExpr, Context ctx) {
        Tactic tactic = ctx.mkTactic("ctx-solver-simplify");
        Goal g = ctx.mkGoal(true, true, false);
        g.add(boolExpr);
        ApplyResult apply = tactic.apply(g);

        if (apply.getNumSubgoals() != 1)
            throw new RuntimeException(apply.toString());

        if (apply.getSubgoals()[0].getFormulas().length == 0)
            return ctx.mkTrue();
        else if (apply.getSubgoals()[0].getFormulas().length == 1)
            return apply.getSubgoals()[0].getFormulas()[0];
        else
            return ctx.mkAnd(apply.getSubgoals()[0].getFormulas());
    }



    public static SubstitutionArrays createLocalsSubstitutionArray(ArrayList<String> localNames, String prefix, Context ctx) {
        SubstitutionArrays result = new SubstitutionArrays(localNames.size());

        for(int i = 0; i < localNames.size(); ++i) {
            String name = localNames.get(i);
            result.from[i] = ctx.mkBVConst(name, DashConstants.BITS_IN_INT);
            result.to[i] = ctx.mkBVConst(prefix + name, DashConstants.BITS_IN_INT);
        }

        return result;
    }
}
