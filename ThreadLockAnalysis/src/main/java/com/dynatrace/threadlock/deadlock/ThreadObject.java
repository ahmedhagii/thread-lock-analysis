package com.dynatrace.threadlock.deadlock;

import java.util.List;

public class ThreadObject {

    public String id;
    public String name;
    public int priority;
    public List<String> stacktrace;

    public ThreadObject(String id, String name, List<String> stacktrace, int priority) {
        this.id = id;
        this.name = name;
        this.stacktrace = stacktrace;
        this.priority = priority;
    }
}
