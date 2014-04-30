package dk.au.cs.dash;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.util.TimeTracer;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(OutputDeleter.class)
public abstract class AbstractDashTests {
    private void assertDash(Class<?> clazz, String methodName, boolean passed) {
        assertDash(clazz, methodName, passed, null, null);
    }

    public void assertDash(Class<?> clazz, String methodName, boolean passed, ConditionGenerator inputConstraints, ConditionGenerator exitConstraints) {
        Context context = new Context();
        try {
            PreparedProgram p = new ProgramPreparer(context).prepareProgram(clazz.getName());
            p.get(clazz.getName(), methodName).g.isMainProcedure = true;
            Dash dash = new Dash(p, context);
            BoolExpr extraInputConstraints = inputConstraints != null ? inputConstraints.create(context) : null;
            BoolExpr extraExitConstraints = exitConstraints != null ? exitConstraints.create(context) : null;
            boolean pass = dash.dashLoop(clazz.getName(), methodName, extraInputConstraints, extraExitConstraints).pass;
            TimeTracer.assertStackIsEmpty();
            assertEquals(passed, pass);
        } finally {
            context.dispose();
            TimeTracer.clearState();
        }
    }

    public interface ConditionGenerator {
        public BoolExpr create(Context ctx);
    }

    private void assertDash(Class<?> clazz, boolean passed) {
        assertDash(clazz, "test", passed);
    }

    protected void assertDashReportsNoError(Class<?> clazz) {
        assertDash(clazz, true);
    }

    protected void assertDashReportsError(Class<?> clazz) {
        assertDash(clazz, false);
    }
}
