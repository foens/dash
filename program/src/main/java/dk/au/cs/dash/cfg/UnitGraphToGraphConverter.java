package dk.au.cs.dash.cfg;

import com.microsoft.z3.Context;
import dk.au.cs.dash.Dash;
import dk.au.cs.dash.cfg.Op.FakeAssumeTrueOp;
import dk.au.cs.dash.cfg.Op.Op;
import dk.au.cs.dash.cfg.Op.ReturnOp;
import soot.BooleanType;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IntConstant;
import soot.jimple.internal.*;
import soot.toolkits.graph.UnitGraph;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class UnitGraphToGraphConverter {
    private final UnitGraph unitGraph;
    private final UnitToOpConverter toOpConverter;
    private final Edges edges = new Edges();
    private final PatchingChain<Unit> units;
    private final Map<Unit, Region> unitToRegionMap = new HashMap<>();
    private final Set<Unit> nondetIf = new HashSet<>();
    private final Context ctx;
    private final ErrorChooser errorChooser;

    private List<Region> returnRegions = new ArrayList<>();
    private int lastRegionNumber;
    private Region firstRegion = null;

    public UnitGraphToGraphConverter(UnitGraph unitGraph, ErrorChooser errorChooser, UnitToOpConverter toOpConverter, Context ctx) {
        this.toOpConverter = requireNonNull(toOpConverter);
        this.unitGraph = requireNonNull(unitGraph);
        units = unitGraph.getBody().getUnits();
        this.ctx = requireNonNull(ctx);
        this.errorChooser = requireNonNull(errorChooser);
    }

    public Graph convert() {
        createRegionsFromUnits();
        addEdgesBetweenRegions();
        SootMethod method = unitGraph.getBody().getMethod();
        return new Graph(firstRegion, edges, method.getDeclaringClass().getName(), method.getName(), calculateErrorRegions(), returnRegions);
    }

    private Set<Region> calculateErrorRegions() {
        Set<Region> errorRegions = new HashSet<>();
        for (Unit u : units) {
            if (u instanceof JGotoStmt)
                continue;
            Region region = unitToRegionMap.get(u);
            if (region != null && region.isErrorRegion)
                errorRegions.add(region);
        }
        return errorRegions;
    }

    private void createRegionsFromUnits() {
        int regionNumber = 0;
        for (Unit u : units) {
            if (!(u instanceof JGotoStmt) && !(u instanceof JIdentityStmt)) {
                Region newRegion = new Region(errorChooser.isError(u), regionNumber, ctx.mkTrue());
                unitToRegionMap.put(u, newRegion);
                ++regionNumber;

                if(firstRegion == null)
                    firstRegion = newRegion;
            }
        }
        lastRegionNumber = regionNumber;
    }

    private void addEdgesBetweenRegions() {
        for (Unit u : units)
            if (!(u instanceof JGotoStmt) && !(u instanceof JIdentityStmt))
                addEdgeFromUnit(u);
    }

    private void addEdgeFromUnit(Unit u) {
        Region r = unitToRegionMap.get(u);
        if (unitGraph.getTails().contains(u))
            addEdgeForTailUnit(u, r);
        else
            addEdgeForNonTailUnit(u, r);
    }

    private void addEdgeForTailUnit(Unit u, Region r) {
        Region region = new Region(false, lastRegionNumber, ctx.mkTrue());
        returnRegions.add(region);
        edges.add(new Edge(toOpConverter.convert(u, null), r, region));
    }

    private void addEdgeForNonTailUnit(Unit u, Region r) {
        List<Unit> childrenOfU = unitGraph.getSuccsOf(u);

        if(isNondetAssign(u, childrenOfU)) {
            handleNondetAssign(u, childrenOfU.get(0), r);
        } else if(isNondetBranch(u)) {
            handleNondetBranch(u, childrenOfU, r);
        } else {
            for (Unit childOfU : childrenOfU) {
                Region childOfR = findDestinationRegion(childOfU);
                Op convert = toOpConverter.convert(u, childOfU);
                if(convert instanceof ReturnOp)
                    returnRegions.add(childOfR);
                edges.add(new Edge(convert, r, childOfR));
            }
        }
    }

    private void handleNondetBranch(Unit u, List<Unit> childrenOfU, Region r) {

        if(childrenOfU.size() != 2)
            throw new RuntimeException("Must be two targets after branch");

        JIfStmt stm = (JIfStmt) u;
        Unit target, fallThrough;

        if(stm.getTarget() == childrenOfU.get(0)) {
            target = childrenOfU.get(0);
            fallThrough = childrenOfU.get(1);
        } else if(stm.getTarget() == childrenOfU.get(1)) {
            target = childrenOfU.get(1);
            fallThrough = childrenOfU.get(0);
        } else {
            throw new RuntimeException("No target");
        }

        Region regionTarget = findDestinationRegion(target);
        Region regionFallThrough = findDestinationRegion(fallThrough);

        // check is == 0, so the real target (not soot target) is by fallthrough
        edges.add(new Edge(new FakeAssumeTrueOp(toOpConverter.convert(u, target), FakeAssumeTrueOp.BranchType.BRANCH_EDGE_FALSE, ctx), r, regionTarget));
        edges.add(new Edge(new FakeAssumeTrueOp(toOpConverter.convert(u, fallThrough), FakeAssumeTrueOp.BranchType.BRANCH_EDGE_TRUE, ctx), r, regionFallThrough));
    }

    private boolean isNondetBranch(Unit u) {
        return nondetIf.contains(u);
    }

    private void handleNondetAssign(Unit u, Unit child, Region r) {
        Region childOfR = findDestinationRegion(child);
        edges.add(new Edge(new FakeAssumeTrueOp(toOpConverter.convert(u, child), FakeAssumeTrueOp.BranchType.NOT_A_BRANCH_EDGE, ctx), r, childOfR));
        nondetIf.add(child);
    }

    //TODO cleanup
    private boolean isNondetAssign(Unit u, List<Unit> childrenOfU) {
        return u instanceof JAssignStmt
                && ((JAssignStmt) u).getRightOp() instanceof JStaticInvokeExpr
                && ((JStaticInvokeExpr) ((JAssignStmt) u).getRightOp()).getMethod().getName().equals("nondet")
                && ((JStaticInvokeExpr) ((JAssignStmt) u).getRightOp()).getMethod().getDeclaringClass().getName().equals(Dash.class.getName())
                && ((JAssignStmt) u).getLeftOp() instanceof JimpleLocal
                && ((JAssignStmt) u).getLeftOp().getType() instanceof BooleanType
                && childrenOfU.size() == 1
                && childrenOfU.get(0) instanceof JIfStmt
                && ((JIfStmt) childrenOfU.get(0)).getCondition() instanceof JEqExpr
                && ((JEqExpr) ((JIfStmt) childrenOfU.get(0)).getCondition()).getOp1() instanceof JimpleLocal
                && ((JimpleLocal) ((JEqExpr) ((JIfStmt) childrenOfU.get(0)).getCondition()).getOp1()).getName().equals(((JimpleLocal) ((JAssignStmt) u).getLeftOp()).getName())
                && ((JEqExpr) ((JIfStmt) childrenOfU.get(0)).getCondition()).getOp2() instanceof IntConstant
                && ((IntConstant) ((JEqExpr) ((JIfStmt) childrenOfU.get(0)).getCondition()).getOp2()).value == 0
                ;
    }

    private Region findDestinationRegion(Unit u) {
        if (u instanceof JGotoStmt)
            return findDestinationRegion(((JGotoStmt) u).getTarget());
        else
            return unitToRegionMap.get(u);
    }
}
