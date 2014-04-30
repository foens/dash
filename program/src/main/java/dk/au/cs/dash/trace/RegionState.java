package dk.au.cs.dash.trace;

import dk.au.cs.dash.cfg.Region;
import dk.au.cs.dash.instrumentation.State;

public class RegionState {
    public final Region region;
    public final State state;

    public RegionState(Region region, State state) {
        this.region = region;
        this.state = state;
    }

    public RegionState(Region region) {
        this.region = region;
        this.state = null;
    }

    @Override
    public String toString() {
        return "RegionState{" +
                "region=" + region +
                ", state=" + state +
                '}';
    }
}
