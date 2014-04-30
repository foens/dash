package dk.au.cs.dash.instrumentation;

import com.microsoft.z3.Context;
import dk.au.cs.dash.util.DashConstants;
import dk.au.cs.dash.util.DashMap;
import dk.au.cs.dash.util.SubstitutionArrays;

import java.util.Map;

public class Variables extends DashMap<String, Integer> {
    public Variables() {
        super();
    }

    public Variables(Map<String, Integer> map) {
        super(map);
    }

    public Variables createCopy() {
        return new Variables(map);
    }
//
//    public SymbolicMap convertToSymbolicMap() {
//        SymbolicMap m = new SymbolicMap();
//        for (Map.Entry<String, Integer> entry : map.entrySet())
//            m.put(new NameBitVecExpression(entry.getKey()), new ConstantBitVecExpression(entry.getValue()));
//        return m;
//    }

    public SubstitutionArrays getSubstitutionArrays(Context ctx) {
        SubstitutionArrays sa = new SubstitutionArrays(map.size());

        int i = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sa.from[i] = ctx.mkBVConst(entry.getKey(), DashConstants.BITS_IN_INT);
            sa.to[i] = ctx.mkBV(entry.getValue(), DashConstants.BITS_IN_INT);
            ++i;
        }
        return sa;
    }
}
