package com.dynatrace.threadlock.utils;

import com.dynatrace.threadlock.objects.ThreadObject;
import com.dynatrace.threadlock.parser.Parser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DataAdapter {

    private static String readJsonFile(String filename) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(Constants.inputDir + filename)));
        return json;
    }
    private static void writeJsonFile(List<ThreadObject> threadData, String filename) throws IOException {
        FileWriter fw = new FileWriter(Constants.inputDir + filename + ".json");
        fw.write(Utils.toJson(threadData));
    }


    public static List<ThreadObject> getThreadObjects(String name) throws IOException {
        String json = readJsonFile(name + ".json");
        ThreadObject[] ret = (ThreadObject[])Utils.toObject(json, ThreadObject[].class);
        return Arrays.asList(ret);
    }

    public static void writeThreadObjects(List<ThreadObject> threadObjects, String fileName) throws IOException {
        FileWriter fw = new FileWriter(Constants.inputDir + fileName);
        fw.write(Utils.toJson(threadObjects));
        fw.flush();
        fw.close();
    }

    public static void writeClusteringData(String fileName, List<String> list) throws IOException {
        FileWriter fw = new FileWriter(Constants.inputDir + fileName);
        for(String x : list)
            fw.write(x + "\n");
        fw.flush();
        fw.close();
    }
}
