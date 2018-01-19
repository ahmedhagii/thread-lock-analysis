package com.dynatrace.threadlock;

import java.io.IOException;

/**
 * Created by akram on 1/18/18.
 */
public class Runner {

    public static void main(String[]ar) throws IOException {
        GenerateCSV generator = new GenerateCSV();
        // This is the dummy data I sampled from Jenkins
        String inputDir = "./resources/dump/";
        String outputDir = "./resources/";
        generator.readDumpFilesAndGenerateCSV(inputDir, "jstack.jenkins.1516386", outputDir, "thread_data_day2.csv");
    }

}
