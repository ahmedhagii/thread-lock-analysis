package com.dynatrace.threadlock.utils;

import com.dynatrace.threadlock.objects.ThreadObject;
import com.dynatrace.threadlock.parser.Parser;
import com.dynatrace.threadlock.parser.ThreadRegex;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Utils {

    static public Gson gson = new Gson();


    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Object toObject(String json, Class c) {
        return gson.fromJson(json, c);
    }

    public static List<ThreadObject> parseData(String fileName) throws IOException {
        if(fileName.isEmpty()) fileName = "thread_data.csv";
        BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/" + fileName));
        StringBuilder content = new StringBuilder();
        List<ThreadObject> ret = new ArrayList<>();
        while(reader.ready()) {
            String[] splits = reader.readLine().split(",");
            String id = splits[0];
            String name = splits[1];
            int priority = Integer.parseInt(splits[2]);
            String status = splits[3];
            long timestamp = Long.parseLong(splits[4]);

            List<String> stacktrace = Arrays.asList(splits[5].split("###\\$\\$###"));
            List<String> lockedObjects = Arrays.asList(splits[6].split("###\\$\\$###"));
            List<String> wantedLocks = Arrays.asList(splits[7].split("###\\$\\$###"));

            ThreadObject thread = new ThreadObject(id, name, priority, status, timestamp, stacktrace, lockedObjects, wantedLocks);
            ret.add(thread);
        }
        return ret;
    }

    public static List<ThreadObject> threadDumpToObject(List<List> threadDumps) {
        ThreadRegex threadRegex = new ThreadRegex();
        List<ThreadObject> result = new LinkedList<>();
        for(List<String> singleFile : threadDumps) {
            Long timestamp = Long.parseLong(singleFile.get(0));
            for(int i = 1; i < singleFile.size(); i++) {
                String threadData = singleFile.get(i);
                ThreadObject threadObject = new ThreadObject();
                threadObject.time = timestamp;
                threadObject.id = threadRegex.getThreadID(threadData);
                threadObject.name = threadRegex.getThreadName(threadData);
                threadObject.priority = threadRegex.getThreadPriority(threadData);
                threadObject.status = threadRegex.getThreadState(threadData);
                threadObject.stacktrace = threadRegex.getThreadStacktraceList(threadData);
                threadObject.lockedObjects = threadRegex.getThreadLockedObjectsList(threadData);
                threadObject.wantedLocks = threadRegex.getThreadWantedLocksList(threadData);
                result.add(threadObject);
            }
        }
        return result;
    }

    public static List<ThreadObject> getThreadData(String dumpName) throws IOException {
        List<ThreadObject> threads;
        try {
            threads = DataAdapter.getThreadObjects(dumpName);
        } catch(Exception e) {
            Parser parser = new Parser();
            List<List> threadList = parser.getListsFromDumpFile(Constants.inputDir + dumpName);
            threads = Utils.threadDumpToObject(threadList);
            DataAdapter.writeThreadObjects(threads, dumpName + ".json");
        }
        return threads;
    }


//    public static <T extends Collection> T[] subarray(T[] arr, int start, int end) {
//        if(start > end) throw new IllegalArgumentException("start have to be <= end");
//        T[] t = new T[end - start + 1];
//        for(int i = 0; start < end; i++, start++)
//            t[i] = arr[start];
//        return t;
//    }
}
