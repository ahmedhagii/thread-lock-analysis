package com.dynatrace.threadlock.objects;

import com.dynatrace.threadlock.utils.Utils;

import java.util.List;


public class ThreadObject {

    public String id;
    public String name;
    public int priority;
    public long time;
    public String status;
    public List<String> stacktrace;
    public List<String> lockedObjects;
    public List<String> wantedLocks;

    public ThreadObject(){}

    public ThreadObject(String id, String name, int priority, String status, long time,
                        List<String> stacktrace, List<String> lockedObjects, List<String> wantedLocks) {
        this.id = id;
        this.name = name;
        this.stacktrace = stacktrace;
        this.priority = priority;
        this.status = status;
        this.lockedObjects = lockedObjects;
        this.time = time;
        this.wantedLocks = wantedLocks;
    }

    public ThreadObject(String name) {
        this.name = name;
    }

    public String toString() {
        return Utils.toJson(this);
    }
}
