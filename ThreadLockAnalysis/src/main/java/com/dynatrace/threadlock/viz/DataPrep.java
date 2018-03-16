package com.dynatrace.threadlock.viz;

import com.dynatrace.threadlock.objects.ThreadObject;
import com.dynatrace.threadlock.parser.Parser;
import com.dynatrace.threadlock.parser.ThreadRegex;
import com.dynatrace.threadlock.utils.Utils;

import java.io.IOException;
import java.util.*;

public class DataPrep {


    public void prepStacktrace(String dumpName) throws IOException {
        Set<ThreadObject> threads = new HashSet(Utils.getThreadData(dumpName));

        HashMap<String, Integer> clusterDictionary = new HashMap<>();
        HashMap<String, Integer> clusterAssignment = new HashMap<>();
        int clusterCounter = 0;
        List<String> clusterOrder = new LinkedList<>();


        System.out.println(threads.size());

        HashMap<String, Set<String>> graph = new HashMap<>();

        for(ThreadObject thread : threads) {
            List<String> trace = thread.stacktrace;
            for(String line : trace) {
                if(line.startsWith("at")) {
                    String[] pkgName = line.split("\\.");
                    String id = String.join(".", Arrays.copyOfRange(pkgName, 0, 3)).replace("at", "").trim();
                    if(!clusterDictionary.containsKey(id)) clusterDictionary.put(id, clusterCounter++);
                    clusterAssignment.put(line, clusterDictionary.get(id));
                    clusterOrder.add(id);
                } else {
                    String id = new ThreadRegex().getThreadWantedLock(line);
                    if(id == null) continue;
                    if(!clusterDictionary.containsKey(id)) clusterDictionary.put(id, clusterCounter++);
                    clusterAssignment.put(line, clusterDictionary.get(id));
                    clusterOrder.add(id);
                }
            }
            for(int i = 0; i < clusterOrder.size() - 1; i++) {
                String id = clusterOrder.get(i);
//            System.out.println(id);
                if (!graph.containsKey(id))
                    graph.put(id, new HashSet<>());
                graph.get(id).add(clusterOrder.get(i + 1));
            }
            clusterOrder.clear();
        }



        System.out.println("kdjf");
        System.out.println(graph.toString());

    }

    public static void main(String[]ar) throws IOException {
        new DataPrep().prepStacktrace("elasticsearch");
    }
}
