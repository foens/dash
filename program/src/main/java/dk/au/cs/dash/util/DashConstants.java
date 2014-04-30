package dk.au.cs.dash.util;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.Context;

public class DashConstants {
    public static final int BITS_IN_BOOL = 1;
    public static final int BITS_IN_BYTE = 8;
    public static final int BITS_IN_CHAR = 16;
    public static final int BITS_IN_Short = 16;
    public static final int BITS_IN_INT = 32;
    public static final int BITS_IN_LONG = 64;
    private static final String EXIT_CONSTRAINT_RESULT_NAME = "@r";

    public static BitVecExpr createResultVariable(Context ctx) {
        return ctx.mkBVConst(EXIT_CONSTRAINT_RESULT_NAME, BITS_IN_INT);
    }
}
