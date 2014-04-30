package dk.au.cs.dash.util;

import com.microsoft.z3.Expr;


public class SubstitutionArrays {
    public final Expr[] from;
    public final Expr[] to;

    public SubstitutionArrays() {
        this(0);
    }

    public SubstitutionArrays(int size) {
        this.from = new Expr[size];
        this.to = new Expr[size];
    }
}
