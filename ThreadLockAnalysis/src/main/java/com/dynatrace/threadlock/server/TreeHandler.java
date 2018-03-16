package com.dynatrace.threadlock.server;

import apple.laf.JRSUIUtils;
import com.dynatrace.threadlock.objects.StreamGraphObject;
import com.dynatrace.threadlock.objects.ThreadObject;
import com.dynatrace.threadlock.objects.TreeMapObject;
import com.dynatrace.threadlock.objects.TreeObject;
import com.dynatrace.threadlock.parser.Parser;
import com.dynatrace.threadlock.parser.ThreadRegex;
import com.dynatrace.threadlock.utils.Constants;
import com.dynatrace.threadlock.utils.DataAdapter;
import com.dynatrace.threadlock.utils.Utils;

import java.io.IOException;
import java.util.*;

public class TreeHandler extends RequestHandler {
    public TreeHandler(String name, String param, int index, PreciousResource[] resourceArray) {
        super(name, param, index, resourceArray);
    }

    @Override
    public void run() {
        this.setName(this.name);
//        System.out.println("tree thread waiting for lock..");
        synchronized (this.resourceArray[index]) {
//            System.out.println("tree thread acquired lock ...");
            TreeObject  data = null;
            try {
                data = getTreeDataHelper(this.param);
                Utils.toJson(data);
                Thread.sleep(new Random().nextInt(1000) + 500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resourceArray[index] = new PreciousResource(Utils.toJson(data));
        }
//        System.out.println("tree thread released lock");
    }

    private TreeObject getTreeDataHelper(String dumpName) throws IOException {
        return prepStacktrace(dumpName);
    }


    // constructing a trie
    public static TreeObject prepStacktrace(String dumpName) throws IOException {
        Set<ThreadObject> threads = new HashSet(Utils.getThreadData(dumpName));

        HashMap<String, Integer> clusterDictionary = new HashMap<>();
        HashMap<String, Integer> clusterAssignment = new HashMap<>();
        int clusterCounter = 0;
        Deque<String> clusterOrder = new LinkedList<>();


        HashMap<String, Set<String>> graph = new HashMap<>();
        Set<String> startingPoints = new HashSet<>();
        TreeObject root = new TreeObject();
        root.setName("root");

        for(ThreadObject thread : threads) {
//            System.out.println("threa");
            List<String> trace = thread.stacktrace;

            for(int i  = trace.size() - 1; i >= 0; i--) {
                String line = trace.get(i);
                String id = "";
                if(line.startsWith("at")) {
                    String[] pkgName = line.split("\\.");
                    id = String.join(".", Arrays.copyOfRange(pkgName, 0, 2)).replace("at", "").trim();
                }else {
                    id = new ThreadRegex().getThreadWantedLock(line);
                }
                if(id == null) continue;
                if(clusterOrder.isEmpty() || !clusterOrder.getLast().equals(id)) clusterOrder.addLast(id);
//                System.out.println("hena");

//                if(!clusterDictionary.containsKey(id)) clusterDictionary.put(id, clusterCounter++);
//                clusterAssignment.put(line, clusterDictionary.get(id));
//                clusterOrder.add(id);
//                if(i == trace.size() - 1) startingPoints.add(id);

            }
            if(thread.status.equals("RUNNABLE")) clusterOrder.addLast("running");
            else if(thread.status.equals("BLOCKED")) clusterOrder.addLast("blocked");
            else clusterOrder.addLast("waiting");

            TreeObject cur = root;
            while(!clusterOrder.isEmpty()) {
                String id = clusterOrder.pollFirst();
                if(cur.map.containsKey(id)) {
                    cur.map.get(id).incValue();
                    cur = cur.map.get(id);
                    continue;
                }
                TreeObject child = new TreeObject();
                child.setName(id);
                cur.map.put(id, child);
                cur = child;
            }



//            for(int i = 0; i < clusterOrder.size();) {
//                String id = clusterOrder.get(i);
////                System.out.println(id);
//                if (!graph.containsKey(id))
//                    graph.put(id, new HashSet<>());
//                while(i < clusterOrder.size() && id.equals(clusterOrder.get(i))) i++;
//                if(i < clusterOrder.size())
//                    graph.get(id).add(clusterOrder.get(i));
//            }

            clusterOrder.clear();
        }
        System.out.println(root);
        dfs(root);
        return root;
    }

    public static void dfs(TreeObject root) {
        if(root.map != null) {
//            System.out.println("helo " + root.map.keySet().toString());
            for(TreeObject child : root.map.values()) {
//                System.out.println(child.toString());
                root.addChild(child);
            }
        }
        root.map = null;
        for(TreeObject ch : root.getChildren())
            dfs(ch);
    }


//    public static void main(String[]ar) throws IOException {
//        TreeObject to = prepStacktrace("easytravel");
//        System.out.println(Utils.toJson(to));
//    }
}
