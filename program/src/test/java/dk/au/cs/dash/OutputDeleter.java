package dk.au.cs.dash;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.io.File;

public final class OutputDeleter extends BlockJUnit4ClassRunner {
    private static boolean initialized = false;

    public OutputDeleter(Class<?> clazz) throws InitializationError {
        super(clazz);

        synchronized (OutputDeleter.class) {
            if (initialized)
                return;

            deleteOutput();
            initialized = true;
        }
    }

    public static void deleteOutput() {
        deleteDirectory(new File("sootOutput"));
        deleteDirectory(new File("graphs"));
    }

    private static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory())
                        deleteDirectory(file);
                    else if (!file.delete())
                        throw new RuntimeException(file.toString());
                }
            }
        }
        return (directory.delete());
    }
}