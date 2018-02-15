package com.dynatrace.threadlock.server;

import com.dynatrace.threadlock.utils.Utils;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class Main {
    private Gson gson = new Gson();

    public static void main(String[] args) {
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
            if(splits.length == 6) {
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