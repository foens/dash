package dk.au.cs.dash.cfg.Op;

import com.microsoft.z3.BitVecExpr;
import dk.au.cs.dash.util.Z3Printer;

public class ReturnOp implements Op {

    private final BitVecExpr returnVariable;

    public ReturnOp() {
        this(null);
    }

    public ReturnOp(BitVecExpr returnVariable) {
        this.returnVariable = returnVariable;
    }

    public BitVecExpr getReturnVariable() {
        return returnVariable;
    }

    @Override
    public String toString() {
        return returnVariable == null ? "return" : "return " + Z3Printer.toString(returnVariable);
    }
}
