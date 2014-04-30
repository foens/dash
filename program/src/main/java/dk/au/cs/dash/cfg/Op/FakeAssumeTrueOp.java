package dk.au.cs.dash.cfg.Op;

import com.microsoft.z3.Context;

public class FakeAssumeTrueOp extends AssumeOp {

    public enum BranchType {
        BRANCH_EDGE_TRUE,
        BRANCH_EDGE_FALSE,
        NOT_A_BRANCH_EDGE
    }

    private Op realOp;
    private BranchType branchType;

    public FakeAssumeTrueOp(Op realOp, BranchType branchType, Context ctx) {
        super(ctx.mkBool(true));
        this.realOp = realOp;
        this.branchType = branchType;
    }

    public BranchType getBranchType() {
        return branchType;
    }

    public Op getRealOp() {
        return realOp;
    }

    @Override
    public String toString() {
        return super.toString() + " (" + realOp + ")";
    }
}
