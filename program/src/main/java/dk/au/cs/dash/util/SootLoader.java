package dk.au.cs.dash.util;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;

public class SootLoader {
    private static boolean loaded = false;

    public static SootClass loadClass(String className) {
        Options.v().set_prepend_classpath(true);
        Options.v().setPhaseOption("jb", "use-original-names:true");

        Scene s = Scene.v();
        TimeTracer.start("loadClass.forceResolve");
        SootClass c = s.forceResolve(className, SootClass.BODIES);
        TimeTracer.end("loadClass.forceResolve");
        TimeTracer.start("loadClass.loadNecessaryClasses");
        if(!loaded)
            s.loadNecessaryClasses();
        loaded = true;
        TimeTracer.end("loadClass.loadNecessaryClasses");

        TimeTracer.start("loadClass.retrieveActiveBody");
        for (SootMethod sootMethod : c.getMethods())
            sootMethod.retrieveActiveBody();
        TimeTracer.end("loadClass.retrieveActiveBody");

        return c;
    }
}
