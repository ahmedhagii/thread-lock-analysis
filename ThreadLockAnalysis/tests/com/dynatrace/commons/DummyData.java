package com.dynatrace.commons;

import java.util.Arrays;
import java.util.List;

/**
 * Created by akram on 1/17/18.
 */
public class DummyData {


    public static final String THREAD_DUMP = "\"Inactive RequestProcessor thread [Was:ThreadsWorker/com.sun.tools.visualvm.application.views.threads.ApplicationThreadsView$MasterViewSupport$3]\" #41 daemon prio=1 os_prio=31 tid=0x00007ffa01667800 nid=0x13ef3 in Object.wait() [0x00007000105c1000]\n" +
            "   java.lang.Thread.State: TIMED_WAITING (on object monitor)\n" +
            "        at java.lang.Object.wait(Native Method)\n" +
            "        at org.openide.util.RequestProcessor$Processor.run(RequestProcessor.java:2002)\n" +
            "        - locked <0x00000007b16ac640> (a java.lang.Object)\n" +
            "\n" +
            "   Locked ownable synchronizers:\n" +
            "        - None\n" +
            "\n" +
            "\"Attach Listener\" #39 daemon prio=9 os_prio=31 tid=0x00007ffa00a06000 nid=0xe13f waiting on condition [0x0000000000000000]\n" +
            "   java.lang.Thread.State: RUNNABLE\n" +
            "\n" +
            "   Locked ownable synchronizers:\n" +
            "        - None\n" +
            "\n" +
            "\"pool-3-thread-1\" #37 prio=5 os_prio=31 tid=0x00007ffa01213800 nid=0x1350b waiting on condition [0x00007000111e5000]\n" +
            "   java.lang.Thread.State: WAITING (parking)\n" +
            "        at sun.misc.Unsafe.park(Native Method)\n" +
            "        - parking to wait for  <0x00000007b0bb10c0> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)\n" +
            "        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)\n" +
            "        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)\n" +
            "        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)\n" +
            "        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)\n" +
            "        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)\n" +
            "        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\n" +
            "        at java.lang.Thread.run(Thread.java:748)\n" +
            "\n" +
            "   Locked ownable synchronizers:\n" +
            "        - None\n";

    public static final String THREAD_1 = "\"Inactive RequestProcessor thread [Was:ThreadsWorker/com.sun.tools.visualvm.application.views.threads.ApplicationThreadsView$MasterViewSupport$3]\" #41 daemon prio=1 os_prio=31 tid=0x00007ffa01667800 nid=0x13ef3 in Object.wait() [0x00007000105c1000]\n" +
            "   java.lang.Thread.State: TIMED_WAITING (on object monitor)\n" +
            "        at java.lang.Object.wait(Native Method)\n" +
            "        at org.openide.util.RequestProcessor$Processor.run(RequestProcessor.java:2002)\n" +
            "        - locked <0x00000007b16ac640> (a java.lang.Object)\n" +
            "\n" +
            "   Locked ownable synchronizers:\n" +
            "        - None";

    public static final String THREAD_2 = "\"Attach Listener\" #39 daemon prio=9 os_prio=31 tid=0x00007ffa00a06000 nid=0xe13f waiting on condition [0x0000000000000000]\n" +
            "   java.lang.Thread.State: RUNNABLE\n" +
            "\n" +
            "   Locked ownable synchronizers:\n" +
            "        - None";

    public static final String THREAD_3 = "\"pool-3-thread-1\" #37 prio=5 os_prio=31 tid=0x00007ffa01213800 nid=0x1350b waiting on condition [0x00007000111e5000]\n" +
            "   java.lang.Thread.State: WAITING (parking)\n" +
            "        at sun.misc.Unsafe.park(Native Method)\n" +
            "        - parking to wait for  <0x00000007b0bb10c0> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)\n" +
            "        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)\n" +
            "        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)\n" +
            "        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)\n" +
            "        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)\n" +
            "        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)\n" +
            "        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)\n" +
            "        at java.lang.Thread.run(Thread.java:748)\n" +
            "\n" +
            "   Locked ownable synchronizers:\n" +
            "        - None";

    public static final String CSV_ENTRY = "0x232382332ff, jenkins_thread, 5, waiting on condition";

    public static final String TIMESTAMP = "1516309321278";

    public static final String THREAD_1_CSV = "0x00007ffa01667800,Inactive RequestProcessor thread [Was:ThreadsWorker/com.sun.tools.visualvm.application.views.threads.ApplicationThreadsView$MasterViewSupport$3],1,in Object.wait() [0x00007000105c1000]," + TIMESTAMP;
    public static final String THREAD_2_CSV = "0x00007ffa00a06000,Attach Listener,9,waiting on condition [0x0000000000000000]," + TIMESTAMP;
    public static final String THREAD_3_CSV = "0x00007ffa01213800,pool-3-thread-1,5,waiting on condition [0x00007000111e5000]," + TIMESTAMP;

    public static final List<String> CSV_DATA = Arrays.asList(new String[]{THREAD_1_CSV, THREAD_2_CSV, THREAD_3_CSV});
}

