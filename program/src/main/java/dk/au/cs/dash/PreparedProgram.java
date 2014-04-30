package dk.au.cs.dash;

import dk.au.cs.dash.cfg.Graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class PreparedProgram {
    private final Map<String, PreparedMethod> methods = new HashMap<>();
    private Set<String> preparedClasses;

    public void put(String className, String methodName, PreparedMethod m) {
        methods.put(getKey(className, methodName), m);
    }

    public PreparedMethod get(String className, String methodName) {
        return requireNonNull(methods.get(getKey(className, methodName)));
    }

    public static String getKey(String className, String methodName) {
        return className + "." + methodName;
    }

    public void setPreparedClasses(Set<String> preparedClasses) {
        this.preparedClasses = Collections.unmodifiableSet(preparedClasses);
    }

    public Set<String> getPreparedClasses() {
        return preparedClasses;
    }


    public java.util.Collection<PreparedMethod> getGraphs() {
        return methods.values();
    }
}
