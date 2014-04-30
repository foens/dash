package dk.au.cs.dash.type;

import soot.BooleanType;
import soot.IntType;

public enum DashType {
    bool,
    integer;

    public Class<?> toJavaType() {
        switch (this) {
            case bool:
                return Boolean.TYPE;
            case integer:
                return Integer.TYPE;
            default:
                throw new RuntimeException("Unknown type: " + this);
        }
    }

    public static DashType from(soot.Type t) {
        if (t instanceof BooleanType)
            return bool;
        if (t instanceof IntType)
            return integer;
        throw new RuntimeException("Unknown type: " + t.getClass().getName());
    }
}
