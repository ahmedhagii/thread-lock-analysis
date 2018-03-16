package com.dynatrace.threadlock.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by akram on 1/17/18.
 */
public class ThreadRegex {

    /**
     * Matches a string to the passed pattern and returns the Matcher Object.
     * @param str the string to match against
     * @param pattern the pattern to match
     * @return the Matcher object
     */
    public Matcher matchAndReturn(String str, String pattern) {
        Pattern regex = Pattern.compile(pattern, Pattern.MULTILINE);
        return regex.matcher(str);
    }

    /**
     * Matches individual thread from a thread jenkins and returns a list
     * of Strings representing every single thread.
     * @param threadDump the thread jenkins
     * @return List of threads captured in the jenkins
     */
    public List<String> getThreads(String threadDump) {
        Matcher regexMatcher = matchAndReturn(threadDump, "(\"[^\"]+\"[^\"]+)\n*");
        List<String> ret = new ArrayList<>();
        while(regexMatcher.find()) {
            ret.add(regexMatcher.group().trim());
        }
        return ret;
    }

    /**
     * Given a thread trace, extracts and returns the thread name.
     * @param thread the thread trace
     * @return Thread name
     */
    public String getThreadName(String thread) {
        Matcher regexMatcher = matchAndReturn(thread, "\"([^\"]+)\"");
        if(regexMatcher.find()) return regexMatcher.group(1).replace(",", "##");
        return "None";
    }


    /**
     * Given a thread trace, extracts and returns the thread ID.
     * @param thread the thread trace
     * @return Thread ID
     */
    public String getThreadID(String thread) {
        Matcher regexMatcher = matchAndReturn(thread, "tid=([^\\s]+)");
        if(regexMatcher.find()) return regexMatcher.group(1);
        return "None";
    }

    /**
     * Given a thread trace, extracts and returns the thread state
     * ['waiting on condition', 'runnable', 'in Object.wait()'....]
     * @param thread the thread trace
     * @return Thread state
     */
    public String getThreadState(String thread) {
        Matcher regexMatcher = matchAndReturn(thread, "Thread.State:\\s([^\\s]+)\\s");
        if(regexMatcher.find()) return regexMatcher.group(1);
        return "None";
    }

    /**
     * Given a thread trace, extracts and returns the thread priority.
     * @param thread the thread trace
     * @return Thread priority
     */
    public int getThreadPriority(String thread) {
        Matcher regexMatcher = matchAndReturn(thread, "\\sprio=([^\\s]+)");
        if(regexMatcher.find()) return Integer.parseInt(regexMatcher.group(1));
        return -1;
    }

    /**
     * Extracts the date and time of capturing the thread jenkins.
     * @param threadDump the thread jenkins
     * @return The time that jenkins was captured
     */
    public String getTime(String threadDump) {
        Matcher regexMatcher = matchAndReturn(threadDump, "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");
        if(regexMatcher.find())regexMatcher.group(1);
        return "None";
    }

    public String getThreadStacktrace(String threadDump) {
//        Matcher regexMatcher = matchAndReturn(threadDump, "java\\.lang\\.Thread\\.State:[^\n]+(.+)^$");
        String[] lines = threadDump.split("\n");
        if(lines.length - 3 < 2) return "None";
//        lines = Arrays.copyOfRange(lines, 2, Math.min(lines.length - 3, lines.length));
        lines = Arrays.copyOfRange(lines, 2, lines.length);
        for(int i = 0; i < lines.length; i++)
            lines[i] = lines[i].trim();
        return String.join("###$$###", lines);
    }

    public List<String> getThreadStacktraceList(String threadDump) {
//        Matcher regexMatcher = matchAndReturn(threadDump, "java\\.lang\\.Thread\\.State:[^\n]+(.+)^$");
        String[] lines = threadDump.split("\n");
        if(lines.length < 3) return new LinkedList<>();
//        lines = Arrays.copyOfRange(lines, 2, Math.min(lines.length - 3, lines.length));
        lines = Arrays.copyOfRange(lines, 2, lines.length);
        for(int i = 0; i < lines.length; i++)
            lines[i] = lines[i].trim();
        return Arrays.asList(lines);
    }

    public String getThreadLockedObjects(String threadDump) {
        Matcher regexMatcher = matchAndReturn(threadDump, "locked.*\\(a (.+)\\)");
        List<String> lockedObjects = new ArrayList<String>();
        while(regexMatcher.find()) {
            lockedObjects.add(regexMatcher.group(1).trim());
        }
        if(lockedObjects.size() == 0)
            return "None";
        return String.join("###$$###", lockedObjects);
    }

    public List<String> getThreadLockedObjectsList(String threadDump) {
        Matcher regexMatcher = matchAndReturn(threadDump, "locked.*\\(a (.+)\\)");
        List<String> lockedObjects = new ArrayList<String>();
        while(regexMatcher.find()) {
            lockedObjects.add(regexMatcher.group(1).trim());
        }
        return lockedObjects;
    }

    public String getThreadWantedLocks(String threadDump) {
        Matcher regexMatcher = matchAndReturn(threadDump, "waiting to lock ([^\\s]+)");
        List<String> wantedLocks = new ArrayList<>();
        while(regexMatcher.find()) {
            wantedLocks.add(regexMatcher.group(1).trim());
        }
        if(wantedLocks.size() == 0)
            return "None";
        return String.join("###$$###", wantedLocks);
    }

    public List<String> getThreadWantedLocksList(String threadDump) {
        Matcher regexMatcher = matchAndReturn(threadDump, "waiting to lock.*\\(a ([^\\s]+)\\)|parking to wait for.*\\(a ([^\\s]+)\\)|locked.*\\(a ([^\\s]+)\\)");
        List<String> wantedLocks = new ArrayList<>();
        while(regexMatcher.find()) {
            for(int i = 1; i < 20; i++)
                if(regexMatcher.group(i) != null) {
                    wantedLocks.add(regexMatcher.group(i));
                    break;
                }
        }

//        Matcher regexMatcher = matchAndReturn(threadDump, "parking to wait for.*\\(a ([^\\s]+)\\)");
//        List<String> wantedLocks = new ArrayList<>();
//        while(regexMatcher.find()) {
//            wantedLocks.add(regexMatcher.group(1).trim());
//        }
        return wantedLocks;
    }

    public String getThreadWantedLock(String threadDump) {
        Matcher regexMatcher = matchAndReturn(threadDump, "waiting to lock.*\\(a ([^\\s]+)\\)|parking to wait for.*\\(a ([^\\s]+)\\)|locked.*\\(a ([^\\s]+)\\)");
        if(regexMatcher.find()) {
            for(int i = 1; i < 20; i++)
                if(regexMatcher.group(i) != null)
                    return regexMatcher.group(i);
//            if(regexMatcher.group(1) != null) {
//                System.out.println(regexMatcher.group(1));
//                return regexMatcher.group(1).trim();
//            }
//            return regexMatcher.group(2).trim();
        }
        return null;
    }
}
