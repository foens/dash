package dk.au.cs.dash.test.dash.inputandexitconstraints.testclasses;

public class PerformOperation {

    public static int test(int op, int a, int b) {
        if(op == 0) {
            return a;
        } else if(op == 1) {
            return b;
        } else if(op == 2) {
            return a + b;
        } else if(op == 3) {
            return a - b;
        } else if(op == 4) {
            return a * b;
        } else if(op == 5) {
            return a / b;
        } else {
            return 0;
        }
    }
}
