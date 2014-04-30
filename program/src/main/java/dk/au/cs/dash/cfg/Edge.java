package dk.au.cs.dash.cfg;

import dk.au.cs.dash.cfg.Op.Op;

import static java.util.Objects.requireNonNull;

public class Edge {
    public final Op op;
    public final Region from;
    public final Region to;

    public Edge(Op op, Region from, Region to) {
        this.op = requireNonNull(op);
        this.from = requireNonNull(from);
        this.to = requireNonNull(to);
    }
}
