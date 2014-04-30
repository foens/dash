package dk.au.cs.dash.symbolic;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.util.SubstitutionArrays;

import static dk.au.cs.dash.util.DashConstants.createResultVariable;

public class VariableInserter {
    private final SubstitutionArrays sa;

    public VariableInserter(SubstitutionArrays sa) {
        this.sa = sa;
    }

    public VariableInserter(SymbolicMap symbolicMap) {
        this(symbolicMap.getSubstitutionArrays());
    }

    public BitVecExpr insertValueInto(BitVecExpr expr) {
        return (BitVecExpr)expr.substitute(sa.from, sa.to);
    }

    public BoolExpr insertValueInto(BoolExpr expr) {
        return (BoolExpr)expr.substitute(sa.from, sa.to);
    }

    public static VariableInserter createExitConstraintsInserter(BitVecExpr assignToLocalName, Context ctx) {
        SubstitutionArrays sa = new SubstitutionArrays(1);
        sa.from[0]  = assignToLocalName;
        sa.to[0]    = createResultVariable(ctx);
        return new VariableInserter(sa);
    }

    public static VariableInserter createEmptyInserter() {
        return new VariableInserter(new SubstitutionArrays(0));
    }
}
