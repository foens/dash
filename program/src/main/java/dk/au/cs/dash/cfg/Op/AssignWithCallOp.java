package dk.au.cs.dash.cfg.Op;

import com.microsoft.z3.BitVecExpr;

public class AssignWithCallOp implements Op {
    public final BitVecExpr localName;
    public final CallOp callOp;

    public AssignWithCallOp(BitVecExpr localName, CallOp callOp) {
        this.localName = localName;
        this.callOp = callOp;
    }

    @Override
    public String toString() {
        return localName + " := " + callOp;
    }
}
