package dk.au.cs.dash.test.dash.integer.controlflow;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.integer.controlflow.fail.*;
import org.junit.Test;

public class IntControlFlowFail extends AbstractDashTests {
    @Test
    public void addFail() {
        assertDashReportsError(AddFail.class);
    }

    @Test
    public void andFail() {
        assertDashReportsError(AndFail.class);
    }

    @Test
    public void assignmentAddFail() {
        assertDashReportsError(AssignmentAddFail.class);
    }

    @Test
    public void assignmentDivideFail() {
        assertDashReportsError(AssignmentDivideFail.class);
    }

    @Test
    public void assignmentExclusiveOrFail() {
        assertDashReportsError(AssignmentExclusiveOrFail.class);
    }

    @Test
    public void assignmentInclusiveOrFail() {
        assertDashReportsError(AssignmentInclusiveOrFail.class);
    }

    @Test
    public void assignmentModuloFail() {
        assertDashReportsError(AssignmentModuloFail.class);
    }

    @Test
    public void assignmentMultiplyFail() {
        assertDashReportsError(AssignmentMultiplyFail.class);
    }

    @Test
    public void assignmentShiftLeftFail() {
        assertDashReportsError(AssignmentShiftLeftFail.class);
    }

    @Test
    public void assignmentShiftRightFail() {
        assertDashReportsError(AssignmentShiftRightFail.class);
    }

    @Test
    public void assignmentShiftRightSignExtendFail() {
        assertDashReportsError(AssignmentShiftRightSignExtendFail.class);
    }

    @Test
    public void assignmentSubtractFail() {
        assertDashReportsError(AssignmentSubtractFail.class);
    }

    @Test
    public void divideFail() {
        assertDashReportsError(DivideFail.class);
    }

    @Test
    public void exclusiveOrFail() {
        assertDashReportsError(ExclusiveOrFail.class);
    }

    @Test
    public void identityFail() {
        assertDashReportsError(IdentityFail.class);
    }

    @Test
    public void inclusiveOrFail() {
        assertDashReportsError(InclusiveOrFail.class);
    }

    @Test
    public void intParamFail() {
        assertDashReportsError(IntParamFail.class);
    }

    @Test
    public void intParamFailWithEqual() {
        assertDashReportsError(IntParamFailWithEqual.class);
    }

    @Test
    public void intParamFailWithNotEqual() {
        assertDashReportsError(IntParamFailWithNotEqual.class);
    }

    @Test
    public void moduloFail() {
        assertDashReportsError(ModuloFail.class);
    }

    @Test
    public void multiplyFail() {
        assertDashReportsError(MultiplyFail.class);
    }

    @Test
    public void negateFail() {
        assertDashReportsError(NegateFail.class);
    }

    @Test
    public void postFixDecFail() {
        assertDashReportsError(PostFixDecFail.class);
    }

    @Test
    public void shiftLeftFail() {
        assertDashReportsError(ShiftLeftFail.class);
    }

    @Test
    public void shiftRightFail() {
        assertDashReportsError(ShiftRightFail.class);
    }

    @Test
    public void subtractFail() {
        assertDashReportsError(SubtractFail.class);
    }

    @Test
    public void unaryAddFail() {
        assertDashReportsError(UnaryAddFail.class);
    }

    @Test
    public void unaryDecFail() {
        assertDashReportsError(UnaryDecFail.class);
    }

    @Test
    public void postFixAddFail() {
        assertDashReportsError(PostFixAddFail.class);
    }

    @Test
    public void assignmentAndFail() {
        assertDashReportsError(AssignmentAndFail.class);
    }

    @Test
    public void shiftRightSignExtendFail() {
        assertDashReportsError(ShiftRightSignExtendFail.class);
    }

    @Test
    public void notFail() {
        assertDashReportsError(NotFail.class);
    }

    @Test
    public void forFail10() {
        assertDashReportsError(ForFail10.class);
    }

    @Test
    public void forFail1() {
        assertDashReportsError(ForFail1.class);
    }

    @Test
    public void bitwiseNegationFail() {
        assertDashReportsError(BitwiseNegationFail.class);
    }
}