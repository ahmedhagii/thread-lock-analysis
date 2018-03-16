package com.dynatrace.threadlock.playground;

import com.dynatrace.threadlock.objects.ThreadObject;
import com.dynatrace.threadlock.parser.GenerateCSV;
import com.dynatrace.threadlock.parser.Parser;
import com.dynatrace.threadlock.parser.ThreadRegex;
import com.dynatrace.threadlock.utils.Constants;
import com.dynatrace.threadlock.utils.DataAdapter;
import com.dynatrace.threadlock.utils.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;


public class Playground {


    public static void generateCSV() throws IOException {
//        GenerateCSV generator = new GenerateCSV();
//        String inputDir = "./src/main/resources/";
//        String outputDir = "./src/main/resources/";
////        String prefix = "jstack.jenkins";
//        String prefix = "easytravel_dump";
//        generator.readDumpFilesAndGenerateCSV(inputDir, prefix, outputDir, "real_dump.csv");
    }


    public static void runScenario(String dumpName) throws IOException {
        Parser parser = new Parser();
        List<List> threadList = parser.getListsFromDumpFile(Constants.inputDir + dumpName);
        List<ThreadObject> threads = Utils.threadDumpToObject(threadList);

        DataAdapter.writeThreadObjects(threads, dumpName + ".json");
    }

    public static void generateCSVForClustering(String dumpName) throws IOException {
        Parser parser = new Parser();
        List<List> threadList = parser.getListsFromDumpFile(Constants.inputDir + dumpName);
        List<ThreadObject> threads = Utils.threadDumpToObject(threadList);

        String[] colNames = new String[]{"ID", "Name", "Locked", "Class1", "Class2", "Class3",
                "Class4", "Class5", "Class6", "Class7", "Class8", "Class9", "Class10"};
        System.out.println(Arrays.toString(colNames).replaceAll("\\]|\\[|\\s", ""));
        List<String> res = new LinkedList<>();
        res.add(Arrays.toString(colNames).replaceAll("\\]|\\[|\\s", ""));
        for(ThreadObject thread : threads) {
            StringBuilder str = new StringBuilder();
            str.append(thread.id);
            str.append("," + thread.name);

            if(!thread.lockedObjects.isEmpty())
                str.append("," + thread.lockedObjects.get(0));
            else
                str.append("," + "NA");

            String[] trace = thread.stacktrace.toArray(new String[0]);
            int lockedPos = -1;
            for(int i = 0; i < trace.length; i++) {
                if(trace[i].contains("locked")) {
                    lockedPos = i;
                    break;
                }
            }

//            if(lockedPos == -1) continue;

//            int before = 0;
//            for(int i = lockedPos+1 ; i < trace.length && before < 2; i++) {
//                if(trace[i].contains("-")) continue;
//                ThreadRegex regex = new ThreadRegex();
//                Matcher ret = regex.matchAndReturn(trace[i], "\\((.+)\\.java");
////                System.out.println(trace[i] +  ret.find());
//                if(ret.find())
//                    str.append("," + ret.group(1));
//                else
//                    continue;
//                before++;
//            }
//            for(int i = before; i < 2; i++)
//                str.append("," + "NA");
//
//            int after = 0;
//            for(int i = lockedPos-1 ; i >= 0 && after < 2; i--) {
//                if(trace[i].contains("-")) continue;
//                ThreadRegex regex = new ThreadRegex();
//                Matcher ret = regex.matchAndReturn(trace[i], "\\((.+)\\.java");
////                System.out.println(trace[i] +  ret.find());
//                if(ret.find())
//                    str.append("," + ret.group(1));
//                else
//                    continue;
//                after++;
//            }
//            for(int i = after; i < 2; i++)
//                str.append("," + "NA");
            int counter = 0;
            for(String line : trace) {
                if(counter == 10) break;
                if(line.contains("-")) continue;
                ThreadRegex regex = new ThreadRegex();
                Matcher ret = regex.matchAndReturn(line, "\\((.+)\\.java");
                if(ret.find()) {
                    str.append("," + ret.group(1));
                    counter++;
                }
            }
            for(; counter < 10; counter++) str.append("," + "NA");
//            System.out.println(str.toString());
            res.add(str.toString());
        }
        DataAdapter.writeClusteringData(dumpName + ".csv", res);
    }

    public static void main(String[]ar) throws IOException {
//        runScenario("self_dump");
//        generateCSVForClustering("self_dump");
        generateCSVForClustering("elasticsearch");
    }
}
