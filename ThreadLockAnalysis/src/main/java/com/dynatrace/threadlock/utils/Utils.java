package com.dynatrace.threadlock.utils;

import com.dynatrace.threadlock.deadlock.ThreadObject;
import com.google.gson.Gson;

import java.util.List;

public class Utils {

    static public Gson gson = new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public List<ThreadObject> parseData() {

    }
}
