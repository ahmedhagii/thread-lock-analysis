package com.dynatrace.abstracts;


import org.junit.jupiter.api.AfterAll;

import java.io.File;

/**
 * Created by akram on 1/18/18.
 */
public class TestClass {

    protected final String dir = "./src/test/resources/";

    @AfterAll
    public static void clean() {
        String DIR_PATH = "./src/test/resources/";
        File dir = new File(DIR_PATH);
        for (File file:dir.listFiles()) {
            file.delete();
        }
    }
}
