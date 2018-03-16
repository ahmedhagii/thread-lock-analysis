package com.dynatrace.threadlock.parser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by akram on 1/17/18.
 */
public class GenerateCSV {

    private ThreadRegex threadRegex = new ThreadRegex();
    private BufferedWriter writer;

    /**
     * Given the thread trace as text, generates a formatted CSV entry for that thread.
     * @param threadData the thread trace.
     * @param dateTime the data and time the trace was captured
     * @return 1 CSV entry representing the thread information
     */
    public String getCSVEntry(String threadData, String dateTime) {
        String result = String.format("%s,%s,%d,%s,%s,", threadRegex.getThreadID(threadData),
                threadRegex.getThreadName(threadData), threadRegex.getThreadPriority(threadData),
                threadRegex.getThreadState(threadData), dateTime) +
                threadRegex.getThreadStacktrace(threadData) +
                "," + threadRegex.getThreadLockedObjects(threadData) +
                "," + threadRegex.getThreadWantedLocks(threadData);
        return result;
    }

    /**
     * Given the whole thread jenkins, generates a formatted CSV line for each thread trace
     * appearing in that jenkins and return a list of them.
     * @param threadDump the thread jenkins
     * @param timestamp the unix timestamp of capturing the jenkins
     * @return A list of CSV lines for evey thread trace in the jenkins.
     */
    public List<String> generateCSVData(String threadDump, String timestamp) {
        List<String> individualThreads = threadRegex.getThreads(threadDump);
        List<String> lines = new ArrayList<>();
        for(String thread : individualThreads) {
            String line = getCSVEntry(thread, timestamp);
            lines.add(line);
        }
        return lines;
    }

    /**
     * This method takes and input directory containing the sampled thread dumps over time, create a CSV line per
     * thread trace in evey file, then write all the lines into a one CSV file representing all the sampled dumps.
     * @param inputDir the directory containing the sampled thread dumps
     * @param prefix filename prefix to match (optional, if not present, will match all files in 'inputDir')
     * @param outputDir where to save the generated CSV file
     * @param outputFileName generated CSV filename
     * @throws IOException
     */
    public void readDumpFilesAndGenerateCSV(String inputDir, String prefix, String outputDir, String outputFileName) throws IOException {
        File dir = new File(inputDir);
        File [] files = dir.listFiles((dir1, name) -> name.startsWith(prefix));
        Arrays.sort(files, (o1, o2) -> o1.getName().compareTo(o2.getName()));

        this.openWriterBuffer(outputDir + outputFileName);
        for (File csvFile : files) {
            System.out.println(csvFile.getName());
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

            List<String> lines = this.generateCSVData(content.toString(), timestamp);
            System.out.println("generator " + lines);
            this.writeData(lines);
        }
        this.closeWriterBuffer();
    }

    /**
     * Open a writer buffer preparing to write data.
     * @param fileName filename to open
     * @throws IOException
     */
    public void openWriterBuffer(String fileName) throws IOException {
         writer = new BufferedWriter(new FileWriter(fileName));
    }

    /**
     * Write the passed data to a file already opened
      * @param content List of lines to write
     * @throws IOException
     */
    public void writeData(List<String> content) throws IOException {
        for(String line : content) {
            writer.write(line);
            writer.write(System.lineSeparator());
        }
    }

    /**
     * Close the writer buffer after writing the data.
     * @throws IOException
     */
    public void closeWriterBuffer() throws IOException {
        writer.flush();
        writer.close();
    }
}
