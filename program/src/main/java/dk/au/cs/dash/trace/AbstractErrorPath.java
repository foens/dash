package dk.au.cs.dash.trace;

import dk.au.cs.dash.cfg.Region;

import java.util.ArrayList;

public class AbstractErrorPath {
    private final ArrayList<Region> errorTrace;

    public AbstractErrorPath(ArrayList<Region> errorTrace) {
        this.errorTrace = errorTrace;
    }

    public boolean isEmpty() {
        return errorTrace.isEmpty();
    }

    public ArrayList<Region> getErrorTrace() {
        return errorTrace;
    }

    @Override
    public String toString() {
        return "AbstractErrorPath{" +
                "errorTrace=" + errorTrace +
                '}';
    }
}
