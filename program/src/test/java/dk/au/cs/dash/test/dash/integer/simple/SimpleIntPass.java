package dk.au.cs.dash.test.dash.integer.simple;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.integer.simple.pass.*;
import org.junit.Test;

public class SimpleIntPass extends AbstractDashTests {
    @Test
    public void add() {
        assertDashReportsNoError(Add.class);
    }

    @Test
    public void and() {
        assertDashReportsNoError(And.class);
    }

    @Test
    public void assignment() {
        assertDashReportsNoError(Assignment.class);
    }

    @Test
    public void assignmentAdd() {
        assertDashReportsNoError(AssignmentAdd.class);
    }

    @Test
    public void assignmentAnd() {
        assertDashReportsNoError(AssignmentAnd.class);
    }

    @Test
    public void assignmentDivide() {
        assertDashReportsNoError(AssignmentDivide.class);
    }

    @Test
    public void assignmentExclusiveOr() {
        assertDashReportsNoError(AssignmentExclusiveOr.class);
    }

    @Test
    public void assignmentInclusiveOr() {
        assertDashReportsNoError(AssignmentInclusiveOr.class);
    }

    @Test
    public void assignmentModulo() {
        assertDashReportsNoError(AssignmentModulo.class);
    }

    @Test
    public void assignmentMultiply() {
        assertDashReportsNoError(AssignmentMultiply.class);
    }

    @Test
    public void assignmentShiftLeft() {
        assertDashReportsNoError(AssignmentShiftLeft.class);
    }

    @Test
    public void assignmentShiftRight() {
        assertDashReportsNoError(AssignmentShiftRight.class);
    }

    @Test
    public void assignmentShiftRightSignExtend() {
        assertDashReportsNoError(AssignmentShiftRightSignExtend.class);
    }

    @Test
    public void assignmentSubtract() {
        assertDashReportsNoError(AssignmentSubtract.class);
    }

    @Test
    public void divide() {
        assertDashReportsNoError(Divide.class);
    }

    @Test
    public void equal() {
        assertDashReportsNoError(Equal.class);
    }

    @Test
    public void exclusiveOr() {
        assertDashReportsNoError(ExclusiveOr.class);
    }

    @Test
    public void greaterThan() {
        assertDashReportsNoError(GreaterThan.class);
    }

    @Test
    public void greaterThanOrEqual() {
        assertDashReportsNoError(GreaterThanOrEqual.class);
    }

    @Test
    public void identity() {
        assertDashReportsNoError(Identity.class);
    }

    @Test
    public void inclusiveOr() {
        assertDashReportsNoError(InclusiveOr.class);
    }

    @Test
    public void intParam() {
        assertDashReportsNoError(IntParam.class);
    }

    @Test
    public void lessThan() {
        assertDashReportsNoError(LessThan.class);
    }

    @Test
    public void lessThanOrEqual() {
        assertDashReportsNoError(LessThanOrEqual.class);
    }

    @Test
    public void modulo() {
        assertDashReportsNoError(Modulo.class);
    }

    @Test
    public void multiply() {
        assertDashReportsNoError(Multiply.class);
    }

    @Test
    public void negate() {
        assertDashReportsNoError(Negate.class);
    }

    @Test
    public void not() {
        assertDashReportsNoError(Not.class);
    }

    @Test
    public void notEqual() {
        assertDashReportsNoError(NotEqual.class);
    }

    @Test
    public void postFixAdd() {
        assertDashReportsNoError(PostFixAdd.class);
    }

    @Test
    public void postFixDec() {
        assertDashReportsNoError(PostFixDec.class);
    }

    @Test
    public void shiftLeft() {
        assertDashReportsNoError(ShiftLeft.class);
    }

    @Test
    public void shiftRight() {
        assertDashReportsNoError(ShiftRight.class);
    }

    @Test
    public void shiftRightSignExtend() {
        assertDashReportsNoError(ShiftRightSignExtend.class);
    }

    @Test
    public void subtract() {
        assertDashReportsNoError(Subtract.class);
    }

    @Test
    public void unaryAdd() {
        assertDashReportsNoError(UnaryAdd.class);
    }

    @Test
    public void unaryDec() {
        assertDashReportsNoError(UnaryDec.class);
    }

    @Test
    public void bitwiseNegation() {
        assertDashReportsNoError(BitwiseNegation.class);
    }

    @Test
    public void leftHandConstantRightHandLocalPass() {
        assertDashReportsNoError(LeftHandConstantRightHandLocalPass.class);
    }

    @Test
    public void byteSizeInt() {
        assertDashReportsNoError(ByteSizeInt.class);
    }

    @Test
    public void shortSizeInt() {
        assertDashReportsNoError(ShortSizeInt.class);
    }
}
