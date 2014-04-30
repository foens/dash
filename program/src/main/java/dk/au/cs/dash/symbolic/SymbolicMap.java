package dk.au.cs.dash.symbolic;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import dk.au.cs.dash.util.DashMap;
import dk.au.cs.dash.util.SubstitutionArrays;

import java.util.Map;

public class SymbolicMap extends DashMap<BitVecExpr, BitVecExpr> {
    public SymbolicMap() {
        super();
    }

    public SymbolicMap(Map<BitVecExpr, BitVecExpr> map) {
        super(map);
    }

    public SymbolicMap createCopy() {
        return new SymbolicMap(map);
    }

    public BoolExpr createPredicates(Context ctx) {
        BoolExpr[] boolExpr = new BoolExpr[map.size()];

        int i = 0;
        for (Map.Entry<BitVecExpr, BitVecExpr> entry : map.entrySet()) {
            boolExpr[i] = ctx.mkEq(entry.getKey(), entry.getValue());
            ++i;
        }
        return ctx.mkAnd(boolExpr);
    }

    public SubstitutionArrays getSubstitutionArrays() {
        SubstitutionArrays sa = new SubstitutionArrays(map.size());

        int i = 0;
        for (Map.Entry<BitVecExpr, BitVecExpr> entry : map.entrySet()) {
            sa.from[i] = entry.getKey();
            sa.to[i] = entry.getValue();
            ++i;
        }
        return sa;
    }
}
