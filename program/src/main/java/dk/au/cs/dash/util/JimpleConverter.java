package dk.au.cs.dash.util;

import soot.Printer;
import soot.SootClass;
import soot.SourceLocator;
import soot.jimple.JasminClass;
import soot.options.Options;
import soot.util.JasminOutputStream;

import java.io.*;

public class JimpleConverter {
    public static void toClassFile(SootClass clazz) {
        String fileName = SourceLocator.v().getFileNameFor(clazz, Options.output_format_class);
        createDirsForFile(new File(fileName));
        try (PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(new JasminOutputStream(new FileOutputStream(fileName)), "UTF-8"))) {
            JasminClass jasminClass = new soot.jimple.JasminClass(clazz);
            jasminClass.print(writerOut);
            writerOut.flush();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void toJimpleFile(SootClass clazz) {
        String fileName = SourceLocator.v().getFileNameFor(clazz, Options.output_format_jimple);
        createDirsForFile(new File(fileName));
        try (PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"))) {
            Printer.v().printTo(clazz, writerOut);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createDirsForFile(File f) {
        File parentFile = f.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs())
            throw new RuntimeException("Could not create directories " + parentFile);
    }
}
