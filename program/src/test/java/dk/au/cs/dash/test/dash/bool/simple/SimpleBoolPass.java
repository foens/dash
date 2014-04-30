package dk.au.cs.dash.test.dash.bool.simple;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.bool.simple.pass.*;
import org.junit.Test;

public class SimpleBoolPass extends AbstractDashTests {
    @Test
    public void boolParam() {
        assertDashReportsNoError(BoolParam.class);
    }

    @Test
    public void boolParams() {
        assertDashReportsNoError(BoolParams.class);
    }

    @Test
    public void boolVariable() {
        assertDashReportsNoError(BoolVariable.class);
    }

    @Test
    public void equality() {
        assertDashReportsNoError(Equality.class);
    }

    @Test
    public void exprHalfAdder() {
        assertDashReportsNoError(ExprHalfAdder.class);
    }

    @Test
    public void logicalAnd() {
        assertDashReportsNoError(LogicalAnd.class);
    }

    @Test
    public void logicalNot() {
        assertDashReportsNoError(LogicalNot.class);
    }

    @Test
    public void logicalOr() {
        assertDashReportsNoError(LogicalOr.class);
    }

    @Test
    public void methodReturnFalse() {
        assertDashReportsNoError(MethodReturnFalse.class);
    }

    @Test
    public void methodReturnTrue() {
        assertDashReportsNoError(MethodReturnTrue.class);
    }

    @Test
    public void notEqual() {
        assertDashReportsNoError(NotEqual.class);
    }

    @Test
    public void ternary() {
        assertDashReportsNoError(Ternary.class);
    }
}
