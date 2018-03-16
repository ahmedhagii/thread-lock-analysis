package com.dynatrace.threadlock.objects;

import java.util.HashMap;
import java.util.HashSet;

public class ObjectsHistoryResult {

    HashMap<Long, HashMap<String, Integer>> counts;

    public ObjectsHistoryResult() {
        counts = new HashMap<>();
    }

    public ObjectsHistoryResult(HashMap counts) {
        this.counts = counts;
    }

    public void inc(String obj, Long time) {
        HashMap<String, Integer> atThatTime = counts.getOrDefault(time, new HashMap<>());
        atThatTime.put(obj, atThatTime.getOrDefault(obj, 0) + 1);
        counts.put(time, atThatTime);
    }
}
