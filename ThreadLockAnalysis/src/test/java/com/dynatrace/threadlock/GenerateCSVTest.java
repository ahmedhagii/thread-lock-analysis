package com.dynatrace.threadlock;

import com.dynatrace.abstracts.TestClass;
import com.dynatrace.commons.DummyData;
import com.dynatrace.threadlock.parser.GenerateCSV;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Created by akram on 1/17/18.
 */
class GenerateCSVTest extends TestClass {

    @Test
    void shouldWriteDataToFile() throws IOException{
        GenerateCSV generateCSV = new GenerateCSV();

        generateCSV.openWriterBuffer(this.dir + "test_file.csv");
        generateCSV.writeData(DummyData.CSV_DATA);
        generateCSV.closeWriterBuffer();

        BufferedReader reader = new BufferedReader(new FileReader(this.dir + "test_file.csv"));
        List<String> returned = new ArrayList<>();
        while(reader.ready()) returned.add(reader.readLine());
        List<String> expected = DummyData.CSV_DATA;

        assertEquals(expected, returned);
    }

    @Test
    void shouldGenerateOneCSVEntryGivenAThread() {
        GenerateCSV generateCSV = new GenerateCSV();

        String returned = generateCSV.getCSVEntry(DummyData.THREAD_3, DummyData.TIMESTAMP);
        String expected = DummyData.THREAD_3_CSV;

        assertEquals(expected, returned);
    }

    @Test
    void shouldReadDumpDirAndGenerateCSV() throws IOException {
        GenerateCSV generateCSV = new GenerateCSV();

        // Write some dummy thread jenkins files
        BufferedWriter writer = new BufferedWriter(new FileWriter(String.format(this.dir + "dump1.%s", DummyData.TIMESTAMP)));
        writer.write(DummyData.THREAD_DUMP);
        writer.flush();
        writer.close();

        writer = new BufferedWriter(new FileWriter(String.format(this.dir + "dump2.%s", DummyData.TIMESTAMP)));
        writer.write(DummyData.THREAD_DUMP);
        writer.flush();
        writer.close();

        String inputDir = this.dir;
        String outputDir = this.dir;
        generateCSV.readDumpFilesAndGenerateCSV(inputDir, "jenkins", outputDir, "jenkins.csv");

        // read the data written to 'thread_data.csv' and match it to the ones in DummyData class
        BufferedReader reader = new BufferedReader(new FileReader(this.dir + "thread_data.csv"));
        List<String> returned = new ArrayList<>();
        while(reader.ready()) returned.add(reader.readLine());

        List<String> expected = new ArrayList<>(DummyData.CSV_DATA);
        expected.addAll(DummyData.CSV_DATA);

        assertEquals(expected, returned);
    }

    @Test
    void shouldGenerateCSVDataFromThreadDump() throws IOException {
        GenerateCSV generateCSV = new GenerateCSV();

        List<String> returned = generateCSV.generateCSVData(DummyData.THREAD_DUMP, DummyData.TIMESTAMP);
        List<String> expected = DummyData.CSV_DATA;

        assertEquals(expected, returned);
    }

}