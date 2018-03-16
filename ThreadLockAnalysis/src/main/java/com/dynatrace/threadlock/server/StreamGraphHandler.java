package com.dynatrace.threadlock.server;

import com.dynatrace.threadlock.objects.StreamGraphObject;
import com.dynatrace.threadlock.objects.ThreadObject;
import com.dynatrace.threadlock.parser.Parser;
import com.dynatrace.threadlock.utils.Constants;
import com.dynatrace.threadlock.utils.DataAdapter;
import com.dynatrace.threadlock.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class StreamGraphHandler extends RequestHandler {
    public StreamGraphHandler(String name, String param, int index, PreciousResource[] resourceArray) {
        super(name, param, index, resourceArray);
    }

//    public StreamGraphHandler(String name, String param, int index, String[] resourceArray) {
//        super(name, param, index, resourceArray);
//    }

    @Override
    public void run() {
        this.setName(this.name);
//        System.out.println("streamgraph thread waiting for lock..");
        synchronized (this.resourceArray[index]) {
//            System.out.println("streamgraph thread acquired lock ...");
            StreamGraphObject data = null;
            try {
                data = getStreamGraphData(this.param);
                Thread.sleep(new Random().nextInt(1000) + 500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resourceArray[index] = new PreciousResource(Utils.toJson(data));
        }
//        System.out.println("streamgraph thread released lock");
    }

    private String getStreamGraphDataHelper(String dumpName) throws IOException {
        return Utils.toJson(getStreamGraphData(dumpName));
    }

    public static StreamGraphObject getStreamGraphData(String dumpName) throws IOException {
        List<ThreadObject> threads;
        try {
            threads = DataAdapter.getThreadObjects(dumpName);
        } catch(Exception e) {
            Parser parser = new Parser();
            List<List> threadList = parser.getListsFromDumpFile(Constants.inputDir + dumpName);
            threads = Utils.threadDumpToObject(threadList);
            DataAdapter.writeThreadObjects(threads, dumpName + ".json");
        }


        HashMap<String, HashMap<Long, Integer>> counter = new HashMap<>();
        StreamGraphObject res = new StreamGraphObject();

        for(ThreadObject thread : threads) {
            for(String object : thread.wantedLocks) {
                HashMap<Long, Integer> resourceCounter = counter.getOrDefault(object, new HashMap<>());

                resourceCounter.put(thread.time, resourceCounter.getOrDefault(thread.time, 0) + 1);
                counter.put(object, resourceCounter);
            }
            if(res.getTimesteps().size() == 0)
                res.addTimeStep(thread.time);
            res.addTimeStep(thread.time);
        }

        for(String resource : counter.keySet()) {
            res.addData(resource, counter.get(resource).values());
        }

        return res;
    }
}
