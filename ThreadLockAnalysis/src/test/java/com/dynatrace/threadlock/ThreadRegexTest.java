package com.dynatrace.threadlock;

import com.dynatrace.abstracts.TestClass;
import com.dynatrace.commons.DummyData;
import com.dynatrace.threadlock.parser.ThreadRegex;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Created by akram on 1/17/18.
 */
class ThreadRegexTest extends TestClass {

    @Test
    void shouldSeparateThreadsFromThreadDump() {
        ThreadRegex threadRegex = new ThreadRegex();
        String threadDump = DummyData.THREAD_DUMP;
        List<String> expected = Arrays.asList(new String[]{DummyData.THREAD_1, DummyData.THREAD_2, DummyData.THREAD_3});
        List<String> returned = threadRegex.getThreads(threadDump);
        assertEquals(expected, returned);

    }

    @Test
    void shouldGetThreadName() {
        ThreadRegex threadThreadRegex = new ThreadRegex();
        String expected = "pool-3-thread-1";
        String returned = threadThreadRegex.getThreadName(DummyData.THREAD_3);
        assertEquals(expected, returned);
    }

    @Test
    void shouldGetThreadID() {
        ThreadRegex threadRegex = new ThreadRegex();
        String expected = "0x00007ffa01213800";
        String returned = threadRegex.getThreadID(DummyData.THREAD_3);
        assertEquals(expected, returned);
    }

    @Test
    void shouldGetThreadState() {
        ThreadRegex threadRegex = new ThreadRegex();
        String expected = "WAITING";
        String returned = threadRegex.getThreadState(DummyData.THREAD_3);
        assertEquals(expected, returned);
    }

    @Test
    void shouldGetThreadPriority() {
        ThreadRegex threadRegex = new ThreadRegex();
        int expected = 5;
        int returned = threadRegex.getThreadPriority(DummyData.THREAD_3);
        assertEquals(expected, returned);
    }

    @Test
    void shouldGetThreadStacktrace() {
        ThreadRegex threadRegex = new ThreadRegex();
        String expected = DummyData.THREAD_3_STACK_TRACE;
        String returned = threadRegex.getThreadStacktrace(DummyData.THREAD_3);
        assertEquals(expected, returned);
    }

}