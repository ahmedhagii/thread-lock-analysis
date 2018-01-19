package com.dynatrace.threadlock;

import java.util.ArrayList;
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
    private Matcher matchAndReturn(String str, String pattern) {
        Pattern regex = Pattern.compile(pattern, Pattern.MULTILINE);
        return regex.matcher(str);
    }

    /**
     * Matches individual thread from a thread dump and returns a list
     * of Strings representing every single thread.
     * @param threadDump the thread dump
     * @return List of threads captured in the dump
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
        if(regexMatcher.find()) return regexMatcher.group(1);
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
        Matcher regexMatcher = matchAndReturn(thread, "nid=[^\\s]+\\s([^\\n]+)");
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
     * Extracts the date and time of capturing the thread dump.
     * @param threadDump the thread dump
     * @return The time that dump was captured
     */
    public String getTime(String threadDump) {
        Matcher regexMatcher = matchAndReturn(threadDump, "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");
        if(regexMatcher.find())regexMatcher.group(1);
        return "None";
    }
}
