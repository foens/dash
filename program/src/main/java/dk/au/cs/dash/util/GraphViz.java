package dk.au.cs.dash.util;

import java.io.*;
import java.util.Scanner;

public class GraphViz {
    private static final boolean enableEPS = false;

    private static final File GRAPH_DIRECTORY = new File("./graphs");
    private static final String EPS = "eps";
    private static final String PNG = "png";
    private static final String DOT_BINARY = "./dot/dot";

    public static void toGraph(String dot, String directoryName, String name) {
        try {
            File dir = createSubdirectory(GRAPH_DIRECTORY, directoryName);
            File dotDir = createSubdirectory(dir, "dot");
            File dotFile = new File(dotDir, name + ".dot");
            writeDotToFile(dot, dotFile);
            convertDotToGraph(dotFile, dir, name, PNG);
            if (enableEPS)
                convertDotToGraph(dotFile, createSubdirectory(dir, EPS), name, EPS);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File createSubdirectory(File parent, String subdirectory) {
        File dir = new File(parent, subdirectory);
        if (!dir.exists() && !dir.mkdirs())
            throw new RuntimeException(dir.toString());
        return dir;
    }

    private static void writeDotToFile(String dot, File dotFile) throws IOException {
        try(OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(dotFile), "UTF-8")) {
            out.write(dot);
        }
    }

    private static void convertDotToGraph(File dotFile, File directory, String filename, String fileType) throws IOException, InterruptedException {
        File outFile = new File(directory, filename + "." + fileType);
        String[] args = {DOT_BINARY, "-T" + fileType, dotFile.getAbsolutePath(), "-o", outFile.getAbsolutePath()};
        Process p = Runtime.getRuntime().exec(args);
        p.waitFor();

        String errorOutput = convertStreamToString(p.getErrorStream());
        String normalOutput = convertStreamToString(p.getInputStream());
        if (p.exitValue() != 0 || !errorOutput.isEmpty() || !normalOutput.isEmpty()) {
            throw new RuntimeException(
                    "Dot failed with exit code: " + p.exitValue() + "\n" +
                            "ErrorOutput: " + errorOutput + "\n" +
                            "Output: " + normalOutput);
        }
    }

    private static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
