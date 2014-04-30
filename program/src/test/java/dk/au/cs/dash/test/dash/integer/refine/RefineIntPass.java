package dk.au.cs.dash.test.dash.integer.refine;

import dk.au.cs.dash.AbstractDashTests;
import dk.au.cs.dash.test.dash.integer.refine.pass.*;
import org.junit.Ignore;
import org.junit.Test;

public class RefineIntPass extends AbstractDashTests {
    @Test
    public void lessThanPass() {
        assertDashReportsNoError(AssignmentShiftRightSignExtendPass.class);
    }

    @Test
    public void andPass() {
        assertDashReportsNoError(AndPass.class);
    }

    @Test
    @Ignore("Does not seem to complete: Cannot figure out loop invariants")
    public void forAddPass() {
        assertDashReportsNoError(ForAddPass.class);
    }

    @Test
    public void passWithForLoop() {
        assertDashReportsNoError(PassWithWhileLoop.class);
    }

    @Test
    public void forWithIfPass() {
        assertDashReportsNoError(ForWithIfPass.class);
    }

    @Test
    public void mathPass() {
        assertDashReportsNoError(MathPass.class);
    }

    @Test
    public void moreAndPass() {
        assertDashReportsNoError(MoreAndPass.class);
    }

    @Test
    @Ignore("Does not seem to complete: Cannot figure out loop invariants")
    public void moduloCheckPass() {
        assertDashReportsNoError(ModuloCheckPass.class);
    }

    @Test
    public void modulePass() {
        assertDashReportsNoError(ModuloPass.class);
    }

    @Test
    public void negatePass() {
        assertDashReportsNoError(NegatePass.class);
    }

    @Test
    public void subtractPass() {
        assertDashReportsNoError(SubtractPass.class);
    }

    @Test
    @Ignore("Does not seem to complete: Cannot figure out loop invariants")
    public void primePass() {
        assertDashReportsNoError(PrimePass.class);
    }

    @Test
    public void multiplyNumberEqualToPrimePass() {
        assertDashReportsNoError(MultiplyNumberEqualToPrimePass.class);
    }

    @Test
    public void stupidSimplePass() {
        assertDashReportsNoError(StupidSimplePass.class);
    }

    @Test
    public void boolAsLocalVarButForcedToTakeNonBoolValuePass() {
        assertDashReportsNoError(BoolAsLocalVarButForcedToTakeNonBoolValuePass.class);
    }
}