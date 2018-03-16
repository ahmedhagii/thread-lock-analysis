package com.dynatrace.threadlock.parser;

import com.dynatrace.threadlock.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Parser {


    public List<List> getListsFromDumpFile(String inputDir) throws IOException {
        File dir = new File(inputDir);
        File [] files = dir.listFiles();
        Arrays.sort(files, (o1, o2) -> o1.getName().compareTo(o2.getName()));

        List<List> ret = new LinkedList<>();
        for (File csvFile : files) {
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            StringBuilder content = new StringBuilder();
            while(reader.ready()) {
                content.append(reader.readLine());
                if(reader.ready())
                    content.append(System.lineSeparator());
            }

            String timestamp = "1";
            String[] fileNameSplits = csvFile.getName().split("\\.");
            if(fileNameSplits.length > 1) timestamp = fileNameSplits[fileNameSplits.length - 1];

            ThreadRegex threadRegex = new ThreadRegex();
            List<String> individualThreads = threadRegex.getThreads(content.toString());
            individualThreads.add(0, timestamp);
            ret.add(individualThreads);
        }
        return ret;
    }

    public static void main(String[]ar) throws IOException {
        Parser parser = new Parser();
        parser.getListsFromDumpFile(Constants.inputDir + "self_dump");
    }

}
