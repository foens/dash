package dk.au.cs.dash.cfg.Op;


import com.microsoft.z3.BitVecExpr;
import dk.au.cs.dash.util.Z3Printer;

public class AssignOp implements Op {

    public final BitVecExpr name;
    public final BitVecExpr rvalue;

    public AssignOp(BitVecExpr name, BitVecExpr rvalue) {
        this.name = name;
        this.rvalue = rvalue;
    }

    @Override
    public String toString() {
        return Z3Printer.toString(name) + " := " + Z3Printer.toString(rvalue);
    }
}
