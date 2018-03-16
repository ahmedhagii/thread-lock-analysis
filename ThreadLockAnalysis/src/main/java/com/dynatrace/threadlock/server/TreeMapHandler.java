package com.dynatrace.threadlock.server;

import com.dynatrace.threadlock.objects.StreamGraphObject;
import com.dynatrace.threadlock.objects.ThreadObject;
import com.dynatrace.threadlock.objects.TreeMapObject;
import com.dynatrace.threadlock.parser.Parser;
import com.dynatrace.threadlock.utils.Constants;
import com.dynatrace.threadlock.utils.DataAdapter;
import com.dynatrace.threadlock.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TreeMapHandler extends RequestHandler {
    public TreeMapHandler(String name, String param, int index, PreciousResource[] resourceArray) {
        super(name, param, index, resourceArray);
    }

//    public TreeMapHandler(String name, String param, int index, String[] resourceArray) {
//        super(name, param, index, resourceArray);
//    }

    @Override
    public void run() {
        this.setName(this.name);
//        System.out.println("treemap thread waiting for lock..");
        synchronized (this.resourceArray[index]) {
//            System.out.println("treemap thread acquired lock ...");
            List<TreeMapObject>  data = null;
            try {
                data = getTreeMapDataHelper(this.param);
                Utils.toJson(data);
                Thread.sleep(new Random().nextInt(1000) + 500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resourceArray[index] = new PreciousResource(Utils.toJson(data));
        }
//        System.out.println("treemap thread released lock");
    }

    private List<TreeMapObject> getTreeMapDataHelper(String dumpName) throws IOException {
        return getTreeMapData(dumpName);
    }

    public static List<TreeMapObject> getTreeMapData(String dumpName) throws IOException {
        List<ThreadObject> threads;
        try {
            threads = DataAdapter.getThreadObjects(dumpName);
        } catch(Exception e) {
            Parser parser = new Parser();
            List<List> threadList = parser.getListsFromDumpFile(Constants.inputDir + dumpName);
            threads = Utils.threadDumpToObject(threadList);
            DataAdapter.writeThreadObjects(threads, dumpName + ".json");
        }

        HashMap<String, Integer> map = new HashMap<>();
        for(ThreadObject thread : threads) {
            for(String object : thread.lockedObjects) {
                map.put(object, map.getOrDefault(object, 0) + 1);
            }
        }
        List<TreeMapObject> res = new LinkedList<>();
        HashMap<String, Boolean> seen = new HashMap<>();
        int color = 0;

        String[] colors = {"#EC2500", "#ECE100", "#EC9800"};
        for(String key : map.keySet()) {
            String prefix = key.split("\\.")[0] + key.split("\\.")[1];
            if(!seen.getOrDefault(prefix, false)) {
                TreeMapObject tmo = new TreeMapObject();
                tmo.setId(prefix);
                tmo.setName(prefix);
                tmo.setColor(colors[(new Random()).nextInt(3)]);
                seen.put(prefix, true);
                res.add(0, tmo);
            }
            TreeMapObject tmo = new TreeMapObject();
            tmo.setParent(prefix);
            tmo.setName(key);
            tmo.setValue(map.get(key));
            res.add(tmo);
        }
        return res;
    }
}
