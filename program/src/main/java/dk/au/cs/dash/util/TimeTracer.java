package dk.au.cs.dash.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TimeTracer {
    private static final Logger logger = LoggerFactory.getLogger(TimeTracer.class);

    private static final HashMap<String, Queue<Timer>> runningTimers = new HashMap<>();

    private static boolean isDisabled() {
        return !logger.isInfoEnabled();
    }

    public static void start(String name) {
        if(isDisabled())
            return;

        Queue<Timer> timers = runningTimers.get(name);
        if(timers == null)
        {
            timers = Collections.asLifoQueue(new ArrayDeque<Timer>());
            runningTimers.put(name, timers);
        }
        timers.add(new Timer());
    }

    public static void end(String name) {
        if(isDisabled())
            return;

        Timer timer = runningTimers.get(name).remove();
        if(timer == null)
            throw new RuntimeException("Ended " + name + " but was never started");
        logger.info("TIMING: " + name + " = " + timer.tick());
    }

    public static void assertStackIsEmpty() {
        try
        {
            for (Map.Entry<String, Queue<Timer>> stringQueueEntry : runningTimers.entrySet())
                if(!stringQueueEntry.getValue().isEmpty())
                    throw new RuntimeException(stringQueueEntry.getKey() + " not correctly ended");
        } finally {
            clearState();
        }
    }

    public static void clearState() {
        runningTimers.clear();
    }
}
