package dk.au.cs.dash.cfg.Op;

import com.microsoft.z3.BitVecExpr;

import java.util.Collections;
import java.util.List;

public class CallOp implements Op {

    public final String className;
    public final String methodName;
    public final List<BitVecExpr> arguments;

    public CallOp(String className, String methodName, List<BitVecExpr> arguments) {

        this.className = className;
        this.methodName = methodName;
        this.arguments = Collections.unmodifiableList(arguments);
    }

    @Override
    public String toString() {
        StringBuilder args = new StringBuilder();
        for (BitVecExpr argument : arguments) {
            if (args.length() != 0)
                args.append(", ");
            args.append(argument.toString());
        }
        return methodName + "(" + args + ")";
    }
}
