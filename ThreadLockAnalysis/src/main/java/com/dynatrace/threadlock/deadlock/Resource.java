package com.dynatrace.threadlock.deadlock;

import java.util.Date;

public class Resource {

    public ThreadObject holder;
    public String name;
    public Date lockedAt;

    public Resource(String name, ThreadObject holder) {
        this.holder = holder;
        this.name = name;
    }

}
