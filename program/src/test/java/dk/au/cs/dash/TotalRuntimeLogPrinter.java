package dk.au.cs.dash;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TotalRuntimeLogPrinter {
    public static void main(String[] args) throws IOException {
        try(InputStream log = new FileInputStream("log.txt"))
        {
            String logContents = convertStreamToString(log);
            HashMap<String, Statistics> totalTimes = calculateTotalRunningTimesFromLog(logContents);
            int maxLength = 35;
            String leftAlignFormat = "| %-" + maxLength +"s | %7d | %7d | %7d | %7d | %7d |%n";

            System.out.format("+-------------------------------------+---------+---------+---------+---------+---------+%n");
            System.out.printf("| Procedure                           | Time ms |   Avg   |   Min   |   Max   | #calls  |%n");
            System.out.format("+-------------------------------------+---------+---------+---------+---------+---------+%n");

            ArrayList<Map.Entry<String, Statistics>> entries = new ArrayList<>(totalTimes.entrySet());
            Collections.sort(entries, new EntryComparator());
            Statistics total = new Statistics();
            for (Map.Entry<String, Statistics> totalRunTime : entries) {
                String name = totalRunTime.getKey();
                Statistics stats = totalRunTime.getValue();
                if (name.contains(".")) {
                    continue;
                }
                System.out.format(leftAlignFormat, ellipsize(name, maxLength), stats.totalRunTime, stats.totalRunTime / stats.count, stats.min, stats.max, stats.count);
                total.totalRunTime += stats.totalRunTime;
                total.min = Math.min(total.min, stats.min);
                total.max = Math.max(total.max, stats.max);
                total.count += stats.count;
                for (Map.Entry<String, Statistics> subItems : entries) {
                    if(subItems.getKey().startsWith(name + ".")) {
                        Statistics subStats = subItems.getValue();
                        System.out.format(leftAlignFormat, ellipsize("  " + subItems.getKey().substring(name.length() + 1), maxLength), subStats.totalRunTime, subStats.totalRunTime / subStats.count, subStats.min, subStats.max, subStats.count);
                    }
                }
            }
            System.out.format("+-------------------------------------+---------+---------+---------+---------+---------+%n");
            System.out.format(leftAlignFormat, "Total", total.totalRunTime, total.totalRunTime / total.count, total.min, total.max, total.count);
            System.out.format("+-------------------------------------+---------+---------+---------+---------+---------+%n");
        }
    }

    private static String ellipsize(String input, int maxLength) {
        if (input == null || input.length() < maxLength)
            return input;
        return input.substring(0, maxLength-3) + "...";
    }

    public static HashMap<String, Statistics> calculateTotalRunningTimesFromLog(String log)
    {
        HashMap<String, Statistics> totalRuntimes = new HashMap<>();
        Pattern p = Pattern.compile(".*?TIMING: (.*?) = (\\d+)");
        for (String line : log.split("\n")) {
            line = line.trim();
            Matcher matcher = p.matcher(line);
            if(matcher.matches())
            {
                String name = matcher.group(1);
                long runTime = Long.parseLong(matcher.group(2));
                Statistics stats = totalRuntimes.get(name);
                if(stats == null)
                {
                    stats = new Statistics();
                    totalRuntimes.put(name, stats);
                }
                stats.totalRunTime += runTime;
                stats.min = Math.min(stats.min, runTime);
                stats.max = Math.max(stats.max, runTime);
                stats.count++;
            }
        }
        return totalRuntimes;
    }

    static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private static class Statistics
    {
        public long totalRunTime;
        public long count;
        public long min = Long.MAX_VALUE;
        public long max;
    }

    private static class EntryComparator implements Comparator<Map.Entry<String, Statistics>> {
        @Override
        public int compare(Map.Entry<String, Statistics> o1, Map.Entry<String, Statistics> o2) {
            Statistics o2Stats = o2.getValue();
            Statistics o1Stats = o1.getValue();
            return Long.compare(o2Stats.totalRunTime, o1Stats.totalRunTime);
        }
    }
}
