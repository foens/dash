package dk.au.cs.dash;

import com.microsoft.z3.BoolExpr;
import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.instrumentation.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DashResult {
    private static final Logger logger = LoggerFactory.getLogger(DashResult.class);

    public final boolean pass;
    public final Variables input;
    public final ArrayList<Boolean> nondetChoices;
    public final Graph g;

    public DashResult(Variables input, ArrayList<Boolean> nondetChoices) {
        logger.info("ERROR WAS FOUND: Input variables=" + input);
        this.pass = false;
        g = null;
        this.input = input;
        this.nondetChoices = nondetChoices;
    }

    public DashResult(Graph g) {
        this.pass = true;
        this.g = g;
        input = null;
        this.nondetChoices = null;
    }
}
