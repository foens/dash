package dk.au.cs.dash;

import dk.au.cs.dash.cfg.Graph;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

public class PreparedMethod {
    public final Graph g;
    public final ArrayList<String> paramAndLocalNames;
    public final ArrayList<Parameter> parameters;

    public PreparedMethod(Graph g, ArrayList<String> paramAndLocalNames, ArrayList<Parameter> parameters) {
        this.g = requireNonNull(g);
        this.paramAndLocalNames = requireNonNull(paramAndLocalNames);
        this.parameters = requireNonNull(parameters);
    }
}
