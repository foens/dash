package dk.au.cs.dash.cfg.Op;

import com.microsoft.z3.BoolExpr;
import dk.au.cs.dash.util.Z3Printer;

public class AssumeOp implements Op {

    public final BoolExpr assumption;

    public AssumeOp(BoolExpr assumption) {
        this.assumption = assumption;
    }

    @Override
    public String toString() {
        return Z3Printer.toString(assumption);
    }
}
