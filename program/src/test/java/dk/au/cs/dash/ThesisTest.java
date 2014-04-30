package dk.au.cs.dash;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThesisTest {
    public static void main(String[] args) throws IOException {
        String thesis = readFile("C:\\Users\\foens\\Desktop\\dash\\thesis\\thesis.tex", Charset.forName("iso-8859-1"));
        Set<String> classNamesUsed = classnameTest(thesis);
        checkForClassnameStringsNotUsedInsideClassname(thesis, classNamesUsed);
        checkForStupidSentences(thesis);
        checkForTodo(thesis);
    }

    private static Set<String> classnameTest(String thesis) throws IOException {
        Pattern p = Pattern.compile("\\\\classname\\{(.*?)\\}", Pattern.CASE_INSENSITIVE);
        String[] lines = thesis.split("\n");
        Map<String, Set<Integer>> matches = new TreeMap<>();
        for (int i = 0; i < lines.length; i++) {
            Matcher matcher = p.matcher(lines[i]);
            if(matcher.find())
            {
                String match = matcher.group(1);
                Set<Integer> linesMatched = matches.get(match);
                if(linesMatched == null)
                {
                    linesMatched = new TreeSet<>();
                    matches.put(match, linesMatched);
                }
                linesMatched.add(i);
            }
        }
        System.out.println("Classnames used:");
        for (Map.Entry<String, Set<Integer>> match : matches.entrySet())
            System.out.println(String.format(" %-50s  lines %s", match.getKey(), match.getValue()));

        return matches.keySet();
    }

    private static void checkForClassnameStringsNotUsedInsideClassname(String thesis, Set<String> classNamesUsed) {
        String[] lines = thesis.split("\n");
        Set<String> ignored = new HashSet<>();
        ignored.add("DASH");
        List<Pattern> patterns = new ArrayList<>();
        for (String classname : classNamesUsed) {
            if(ignored.contains(classname))
                continue;
            patterns.add(Pattern.compile(" (" + Pattern.quote(classname) +")[., \\(]"));
        }

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            for (Pattern p : patterns) {
                Matcher matcher = p.matcher(line);
                if(matcher.find())
                {
                    String match = matcher.group(1);
                    System.out.println(" Found use of " + match + " without \\classname in line " + i + ": " + line);
                }
            }
        }
    }

    private static void checkForStupidSentences(String thesis) {
        Pattern p = Pattern.compile("means|this is true|this is done|\\?|i\\.e\\.", Pattern.CASE_INSENSITIVE);
        String[] lines = thesis.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Matcher matcher = p.matcher(line);
            if (matcher.find())
                System.out.println("PROBLEM IN LINE " + i + " with match \"" + matcher.group() + "\": " + line);
        }
    }

    private static void checkForTodo(String thesis) {
        Pattern p = Pattern.compile("\\\\todo\\{(.*?)\\}", Pattern.CASE_INSENSITIVE);
        String[] lines = thesis.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if(p.matcher(line).find())
                System.out.println("Todo found in line " + i + ": " + line);
        }
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return encoding.decode(ByteBuffer.wrap(encoded)).toString();
    }
}
