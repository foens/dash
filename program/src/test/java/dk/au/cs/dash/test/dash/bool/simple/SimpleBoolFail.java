package dk.au.cs.dash.test.dash.bool.simple;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.bool.simple.fail.*;
import org.junit.Test;

public class SimpleBoolFail extends AbstractDashTests {
    @Test
    public void boolParamFail() {
        assertDashReportsError(BoolParamFail.class);
    }

    @Test
    public void boolParamsFail() {
        assertDashReportsError(BoolParamsFail.class);
    }

    @Test
    public void boolVariableFail() {
        assertDashReportsError(BoolVariableFail.class);
    }

    @Test
    public void equalityFail() {
        assertDashReportsError(EqualityFail.class);
    }

    @Test
    public void exprHalfAdderFail() {
        assertDashReportsError(ExprHalfAdderFail.class);
    }

    @Test
    public void logicalAndFail() {
        assertDashReportsError(LogicalAndFail.class);
    }

    @Test
    public void logicalNotFail() {
        assertDashReportsError(LogicalNotFail.class);
    }

    @Test
    public void logicalOrFail() {
        assertDashReportsError(LogicalOrFail.class);
    }

    @Test
    public void methodReturnFalseFail() {
        assertDashReportsError(MethodReturnFalseFail.class);
    }

    @Test
    public void methodReturnTrueFail() {
        assertDashReportsError(MethodReturnTrueFail.class);
    }

    @Test
    public void notEqualFail() {
        assertDashReportsError(NotEqualFail.class);
    }

    @Test
    public void ternaryFail() {
        assertDashReportsError(TernaryFail.class);
    }
}

