package dk.au.cs.dash.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class DashMap<T, S> {
    protected final Map<T, S> map;

    public DashMap() {
        this(new HashMap<T, S>());
    }

    protected DashMap(Map<T, S> map) {
        this.map = new HashMap<>(map);
    }

    public void put(T variable, S value) {
        map.put(variable, value);
    }

    public S get(T variable) {
        return requireNonNull(map.get(variable));
    }

    public boolean contains(T variable) {
        return map.containsKey(variable);
    }

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<T, S> entry : map.entrySet())
        {
            if(b.length() != 0)
                b.append(", ");
            b.append(entry.getKey());
            b.append("→");
            b.append(entry.getValue());
        }
        return b.toString().trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashMap dashMap = (DashMap) o;
        return map.equals(dashMap.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    public Set<Map.Entry<T,S>> entrySet() {
        return map.entrySet();
    }
}
