package com.dynatrace.threadlock.server;

import com.dynatrace.threadlock.objects.ObjectsHistoryResult;
import com.dynatrace.threadlock.objects.StreamGraphObject;
import com.dynatrace.threadlock.objects.ThreadObject;
import com.dynatrace.threadlock.objects.TreeMapObject;
import com.dynatrace.threadlock.parser.Parser;
import com.dynatrace.threadlock.playground.Playground;
import com.dynatrace.threadlock.utils.Constants;
import com.dynatrace.threadlock.utils.DataAdapter;
import com.dynatrace.threadlock.utils.Utils;
import com.google.gson.Gson;

import javax.rmi.CORBA.Util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.stream.Stream;

import static spark.Spark.*;

public class Main {
    private Gson gson = new Gson();

    //    static String dataObj = "";
    static PreciousResource[] dataObj = new PreciousResource[5];

//    private static class FirstThread extends Thread {
//        String param;
//        String name;
//        int index;
//        public FirstThread(String name, String param, int index) {
//            this.param = param;
//            this.name = name;
//            this.index = index;
//        }
//
//        public void run() {
//            this.setName(name);
//            synchronized (dataObj[index]) {
//                System.out.println("streamgraph thread ...");
//                StreamGraphObject data = null;
//
//                try {
//                    data = getStreamGraphData(this.param);
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                dataObj[index] = Utils.toJson(data);
//            }
//        }
//    }

//    private static class SecondThread extends Thread {
//        String param;
//        String name;
//        int index;
//        public SecondThread(String name, String param, int index) {
//            this.param = param;
//            this.name = name;
//            this.index = index;
//        }
//
//        public void run() {
//            this.setName(name);
//            synchronized (dataObj[index]) {
//                System.out.println("treemap thread ...");
//
//                List<TreeMapObject> data = null;
//                try {
//                    data = getTreeMapData(this.param);
//                    Thread.sleep(3000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                dataObj[index] = Utils.toJson(data);
//            }
//        }
//    }

    public static void main(String[] args) {
        for(int i = 0; i < dataObj.length; i++)
            dataObj[i] = new PreciousResource("");

        staticFileLocation("/public");

        handleRoutes();


        options("/*",
                (request, response) -> {
                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
        });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }

    private static void handleRoutes() {
        get("/hello", (req, res) -> {
            res.type("application/json");
            return "{\"message\":\"Custom 404\"}";
        });

        get("/get-data", (req, res) -> {
            List<String> data = readData();
            res.type("application/json");
            return Utils.toJson(data);
        });

        get("/threads/method/:methodName", (req, res) -> {
            List<String> data = getThreadsMethod(req.params(":methodName"));
            res.type("application/json");
            return Utils.toJson(data);
        });

        get("/threads/time/:timestamp", (req, res) -> {
            List<String> data = getThreadsTime(req.params(":timestamp"));
            res.type("application/json");
            return Utils.toJson(data);
        });

        get("/thread/:name", (req, res) -> {
            List<String> data = getThreadDataName(req.params(":name"));
            res.type("application/json");
            return Utils.toJson(data);
        });

        get("/objects/history/:dumpName", (req, res) -> {
            ObjectsHistoryResult data = lockedObjectsCounts(req.params(":dumpName"));
            res.type("application/json");
            return Utils.toJson(data);
        });

        get("/resources/:dumpName", (req, res) -> {
            ObjectsHistoryResult data = lockedObjectsCounts(req.params(":dumpName"));
            res.type("application/json");
            return Utils.toJson(data);
        });

        get("/resources/treemap/:dumpName", (req, res) -> {
            res.type("application/json");
            int index = new Random().nextInt(dataObj.length);
            new TreeMapHandler("treemap", req.params(":dumpName"), index, dataObj).start();
            synchronized (dataObj[index]) {
            }
            return Utils.toJson(TreeMapHandler.getTreeMapData(req.params(":dumpName")));
        });

        get("/resources/streamgraph/:dumpName", (req, res) -> {
            res.type("application/json");
            int index = new Random().nextInt(dataObj.length);
            new StreamGraphHandler("StreamGraph", req.params(":dumpName"), index, dataObj).start();
            synchronized (dataObj[index]) {
            }
            return Utils.toJson(StreamGraphHandler.getStreamGraphData(req.params(":dumpName")));
        });

        get("/resources/tree/:dumpName", (req, res) -> {
            res.type("application/json");
            int index = new Random().nextInt(dataObj.length);
            new TreeHandler("Tree", req.params(":dumpName"), index, dataObj).start();
            synchronized (dataObj[index]) {
            }
            return Utils.toJson(TreeHandler.prepStacktrace(req.params(":dumpName")));
        });


    }

    public static ObjectsHistoryResult lockedObjectsCounts(String dumpName) throws IOException {
        List<ThreadObject> threads = Utils.getThreadData(dumpName);
        HashSet<Long> times = new HashSet<>();
        HashMap<String, Integer> counts = new HashMap<>();
        ObjectsHistoryResult objectsHistoryResult = new ObjectsHistoryResult();

        for(ThreadObject thread : threads) {
            for(String object : thread.lockedObjects) {
                objectsHistoryResult.inc(object, thread.time);
            }
        }
        return objectsHistoryResult;
    }


    private static List<String> getThreadDataName(String paramsName) throws IOException  {
        System.out.println("handling ... /thread/" + paramsName);
        BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/thread_data.csv"));
        List<String> ret = new ArrayList<>();
        while(reader.ready()) {
            String threadEntry = reader.readLine();
            String[] splits = threadEntry.split(",");
            String threadName = splits[1];
            if(threadName.equals(paramsName)) {
                ret.add(threadEntry);
            }
        }
        System.out.println("returning " + ret.toString());
        return ret;
    }

    private static List<String> getThreadsTime(String paramsTimestamp) throws IOException {
        System.out.println("handling ... /threads/method/" + paramsTimestamp);
        BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/thread_data.csv"));
        List<String> ret = new ArrayList<>();
        while(reader.ready()) {
            String threadEntry = reader.readLine();
            String[] splits = threadEntry.split(",");
            String timestamp = splits[4];
            if(timestamp.equals(paramsTimestamp)) {
                ret.add(threadEntry);
            }
        }
        System.out.println("returning " + ret.toString());
        return ret;
    }

    private static List<String> getThreadsMethod(String paramsMethodName) throws IOException {
        System.out.println("handling ... /threads/method/" + paramsMethodName);
        BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/thread_data.csv"));
        List<String> ret = new ArrayList<>();
        while(reader.ready()) {
            String threadEntry = reader.readLine();
            String[] splits = threadEntry.split(",");
            if(splits.length >= 6) {
                String stacktrace = splits[5];
                String methodName = stacktrace.split("###\\$\\$###/")[0].replace("at ", "");
                if(methodName.startsWith(paramsMethodName)) {
                    ret.add(threadEntry);
                }
            }
        }
        System.out.println("returning " + ret.toString());
        return ret;
    }

    private static List<String> readData() throws IOException {
        System.out.println("handling .... /get-data");
        BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/thread_data.csv"));
        StringBuilder content = new StringBuilder();
        List<String> ret = new ArrayList<>();
        while(reader.ready()) {
            ret.add(reader.readLine());
        }
        return ret;
    }
}