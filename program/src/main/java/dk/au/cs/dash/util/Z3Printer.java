package dk.au.cs.dash.util;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BitVecNum;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Expr;

public class Z3Printer {

    private StringBuilder out = new StringBuilder();
    private StringBuilder indention = new StringBuilder();
    private static final String ONE_INDENTATION = "   ";


    public static String toString(Expr expr) {
        Z3Printer z3Printer = new Z3Printer();
        z3Printer.convertExpr(expr);
        return z3Printer.out.toString();
    }

    private void convertExpr(Expr expr) {
        if(expr.isBool()) {
            convertBoolExpr((BoolExpr) expr);
        } else if(expr.isBV()) {
            convertBitVecExpr((BitVecExpr) expr);
        } else {
            throw new RuntimeException(expr.toString());
        }
    }

    private void convertBitVecExpr(BitVecExpr bitVecExpr) {
        if(bitVecExpr.isBVNumeral()) {
            out.append((int)((BitVecNum)bitVecExpr).getLong());
        } else if(bitVecExpr.isConst()) {
            out.append(bitVecExpr.toString().replaceAll("\\|", ""));
        } else if(bitVecExpr.isBVUMinus()) {    // bvneg   -<something>
            Expr[] args = bitVecExpr.getArgs();
            out.append("-");
            convertExpr(args[0]);
        } else {
            String symbol = null;

            if(bitVecExpr.isBVAdd()) {
                symbol = " + ";
            } else if(bitVecExpr.isBVSub()) {
                symbol = " - ";
            } else if(bitVecExpr.isBVMul()) {
                symbol = " * ";
            } else if(bitVecExpr.isBVSDiv()) {
                symbol = " / ";
            } else if(bitVecExpr.isBVSRem() || bitVecExpr.getFuncDecl().getName().toString().equals("bvsrem_i")) {
                symbol = " % ";
            } else if(bitVecExpr.isBVAND()) {
                symbol = " & ";
            } else if(bitVecExpr.isBVOR()) {
                symbol = " | ";
            } else if(bitVecExpr.isBVXOR()) {
                symbol = " ^ ";
            } else if(bitVecExpr.isBVShiftLeft()) {
                symbol = " << ";
            } else if(bitVecExpr.isBVShiftRightArithmetic()) {
                symbol = " >> ";
            } else if(bitVecExpr.isBVShiftRightLogical()) {
                symbol = " >>> ";
            }


            if (symbol != null) {
                Expr[] args = bitVecExpr.getArgs();
                out.append("(");
                convertExpr(args[0]);
                out.append(symbol);
                convertExpr(args[1]);
                out.append(")");
            } else {
                out.append("FAIL=" + bitVecExpr.toString());
            }
        }
    }

    private void convertBoolExpr(BoolExpr boolExpr) {
        Expr[] args = boolExpr.getArgs();
        if (boolExpr.isFalse()) {
            out.append("false");
        } else if (boolExpr.isTrue()) {
            out.append("true");
        } else if (boolExpr.isAnd()) {
            convertExprArray("and", args);
        } else if (boolExpr.isOr()) {
            convertExprArray("or", args);
        } else if (boolExpr.isNot()) {
            assertArgsLength(args, 1);
            Expr internalExpr = args[0];
            if(!internalExpr.isBool())
                throw new RuntimeException(internalExpr.toString());
            Expr[] internalArgs = internalExpr.getArgs();
            if (internalExpr.isEq()) {
                convertExpr(internalArgs[0]);
                out.append(" ≠ ");
                convertExpr(internalArgs[1]);
            } else if (internalExpr.isBVSLE()) { // !(a≤b)  ==>  a>b
                assertArgsLength(internalArgs, 2);
                outputGreaterThan(internalArgs[0], internalArgs[1]);
            } else if (internalExpr.isBVSLT()) { // !(a<b)  ==>  a≥b
                assertArgsLength(internalArgs, 2);
                outputGreaterThanEqual(internalArgs[0], internalArgs[1]);
            } else if (internalExpr.isBVSGT()) { // !(a>b)  ==>  a≤b
                assertArgsLength(internalArgs, 2);
                outputLessThanEqual(internalArgs[0], internalArgs[1]);
            } else if (internalExpr.isBVSGE()) { // !(a≥b)  ==>  a<b
                assertArgsLength(internalArgs, 2);
                outputLessThan(internalArgs[0], internalArgs[1]);
            } else {
                out.append("!(");
                convertExpr(internalExpr);
                out.append(")");
            }
        } else {
            assertArgsLength(args, 2);
            Expr first = args[0];
            Expr second = args[1];

            if (boolExpr.isEq()) {
                convertExpr(first);
                out.append(" = ");
                convertExpr(second);
            } else if (boolExpr.isBVSLE()) {
                outputLessThanEqual(first, second);
            } else if (boolExpr.isBVSLT()) {
                outputLessThan(first, second);
            } else if (boolExpr.isBVSGE()) {
                outputGreaterThanEqual(first, second);
            } else if (boolExpr.isBVSGT()) {
                outputGreaterThan(first, second);
            } else {
                throw new RuntimeException(boolExpr.toString());
            }
        }
    }

    private void assertArgsLength(Expr[] args, int assertedLength) {
        if (args.length != assertedLength)
            throw new RuntimeException("Argument list was not size " + assertedLength + ", it was " + args.length);
    }

    private void outputGreaterThan(Expr first, Expr second) {
        if (!first.isConst() && second.isConst()) {
            outputLessThan(second, first);
        } else {
            convertExpr(first);
            out.append(" > ");
            convertExpr(second);
        }
    }

    private void outputGreaterThanEqual(Expr first, Expr second) {
        if (!first.isConst() && second.isConst()) {
            outputLessThanEqual(second, first);
        } else {
            convertExpr(first);
            out.append(" ≥ ");
            convertExpr(second);
        }

    }

    private void outputLessThan(Expr first, Expr second) {
        if (!first.isConst() && second.isConst()) {
            outputGreaterThan(second, first);
        } else {
            convertExpr(first);
            out.append(" < ");
            convertExpr(second);
        }
    }

    private void outputLessThanEqual(Expr first, Expr second) {
        if (!first.isConst() && second.isConst()) {
            outputGreaterThanEqual(second, first);
        } else {
            convertExpr(first);
            out.append(" ≤ ");
            convertExpr(second);
        }
    }

    private void convertExprArray(String functionName, Expr[] args) {
        out.append('(');
        out.append(functionName);
        indention.append(ONE_INDENTATION);
        for (Expr arg : args) {
            out.append("\\l");
            out.append(indention);
            convertExpr(arg);
        }
        indention.setLength(indention.length() - ONE_INDENTATION.length());
        out.append(')');
    }

}
