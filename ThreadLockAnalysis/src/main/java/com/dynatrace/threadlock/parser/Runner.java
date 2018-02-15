package com.dynatrace.threadlock.parser;

import com.dynatrace.threadlock.parser.GenerateCSV;

import java.io.IOException;

/**
 * Created by akram on 1/18/18.
 */
public class Runner {

    public static void main(String[]ar) throws IOException {
        GenerateCSV generator = new GenerateCSV();
        // This is the dummy data I sampled from Jenkins

        String inputDir = "./src/main/resources/dump/";
        String outputDir = "./src/main/resources/";
//        generator.readDumpFilesAndGenerateCSV(inputDir, "jstack.jenkins.1516386", outputDir, "thread_data.csv");
        generator.readDumpFilesAndGenerateCSV(inputDir, "jstack.jenkins", outputDir, "thread_data.csv");
    }

}
