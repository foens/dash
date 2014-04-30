package dk.au.cs.dash.cfg;

import dk.au.cs.dash.Dash;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.internal.JInvokeStmt;

public class ErrorChooser {
    public boolean isError(Unit u) {
        if (u instanceof JInvokeStmt) {
            JInvokeStmt jInvoke = (JInvokeStmt) u;
            SootMethod method = jInvoke.getInvokeExpr().getMethod();
            String className = method.getDeclaringClass().getName();
            String methodName = method.getName();
            return className.equals(Dash.class.getName()) && methodName.equals("error");
        }
        return false;
    }
}
