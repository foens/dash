package dk.au.cs.dash.symbolic;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.util.SubstitutionArrays;
import dk.au.cs.dash.util.Z3Helper;

import java.util.ArrayList;

public class VariableRenamer {

    private final SubstitutionArrays substitutionArray;

    public VariableRenamer(SubstitutionArrays substitutionArray) {
        this.substitutionArray = substitutionArray;
    }

    public VariableRenamer(String prefix, ArrayList<String> variables, Context ctx) {
        this(Z3Helper.createLocalsSubstitutionArray(variables, prefix, ctx));
    }

    public BitVecExpr rename(BitVecExpr expr) {
        return (BitVecExpr)expr.substitute(substitutionArray.from, substitutionArray.to);
    }

    public BoolExpr rename(BoolExpr expr) {
        return (BoolExpr)expr.substitute(substitutionArray.from, substitutionArray.to);
    }

    public BoolExpr reverseRename(BoolExpr expr) {
        return (BoolExpr)expr.substitute(substitutionArray.to, substitutionArray.from);
    }

    public BitVecExpr reverseRename(BitVecExpr expr) {
        return (BitVecExpr)expr.substitute(substitutionArray.to, substitutionArray.from);
    }

    public static VariableRenamer createNopRenamer() {
        return new VariableRenamer(new SubstitutionArrays());
    }
}
