package com.dynatrace.threadlock.deadlock;

import com.dynatrace.threadlock.objects.ThreadObject;

import java.util.*;

public class Resource {

    public ThreadObject holder;
    public String name;
    public Date lockedAt;

    public Set<ThreadObject> requesters;

    public Resource(String name) {
        this.name = name;
        requesters = new HashSet<>();
    }

}
