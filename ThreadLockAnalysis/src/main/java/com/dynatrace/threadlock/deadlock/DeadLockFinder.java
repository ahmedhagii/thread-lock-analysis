package com.dynatrace.threadlock.deadlock;

import com.dynatrace.threadlock.objects.ThreadObject;
import com.dynatrace.threadlock.utils.Utils;

import java.io.IOException;
import java.util.*;

public class DeadLockFinder {

    private HashMap<ThreadObject, List<ThreadObject>> graph;

    public DeadLockFinder(HashMap<ThreadObject, List<ThreadObject>> graph) {
        this.graph = graph;
    }
    public DeadLockFinder(){}


    public GraphNode dfs(ThreadObject root) {
        Stack<ThreadObject> ST = new Stack<>();
        HashMap<ThreadObject, GraphNode> dic = new HashMap<>();
        HashMap<ThreadObject, Boolean> visited = new HashMap<>();
        dic = new HashMap<>();

        ST.add(root);
        visited.put(root, true);
        GraphNode currGraphNode = new GraphNode(null, root);
        dic.put(root, currGraphNode);

        while(!ST.empty()) {
            ThreadObject curr = ST.pop();
            List<ThreadObject> children = graph.getOrDefault(curr, new ArrayList<>());

            for(ThreadObject child : children) {
                GraphNode childGraphNode = new GraphNode(dic.get(curr), child);
                if(visited.getOrDefault(child, false)) {
                    System.out.println("hi");
                    return childGraphNode;
                }
                ST.add(child);
                visited.put(child, true);
                dic.put(child, childGraphNode);
            }
        }
        return null;
    }


    class GraphNode {
        GraphNode parent;
        ThreadObject value;

        public GraphNode(GraphNode parent, ThreadObject value) {
            this.parent = parent;
            this.value = value;
        }
    }


    public void mapResources(List<ThreadObject> threads, HashMap<String, Resource> resourceMapper) {
        for(ThreadObject thread : threads) {
            List<String> lockedObjects = thread.lockedObjects;
            if(!lockedObjects.isEmpty()) {
                for(String lockedObjectName : lockedObjects) {
                    Resource resource = resourceMapper.getOrDefault(lockedObjectName, new Resource(lockedObjectName));
                    resource.holder = thread;
                    resourceMapper.put(lockedObjectName, resource);
                }
            }

            List<String> wantedLocks = thread.wantedLocks;
            if(!wantedLocks.isEmpty()) {
                for(String wantedLockName : wantedLocks) {
                    Resource resource = resourceMapper.getOrDefault(wantedLockName, new Resource(wantedLockName));
                    resource.requesters.add(thread);
//                if(resourceMapper.containsKey(wantedLockName)) {
//                    System.out.println("hi 1");
//                    Resource resource = resourceMapper.get(wantedLockName);
//                    if(graph.containsKey(thread)) {
//                        System.out.println("hi");
//                        graph.get(thread).add(resource.holder);
//                    }else {
//                        System.out.println("hi");
//                        graph.put(thread, Arrays.asList(resource.holder));
//                    }
//                }
                }
            }
        }
    }
    public void buildGraph(List<ThreadObject> threads) {
        HashMap<String, Resource> resourceMapper = new HashMap<>();
        graph = new HashMap<>();
        mapResources(threads, resourceMapper);
        mapResources(threads, resourceMapper);

        for(Resource resource : resourceMapper.values()) {
            if(resource.holder != null && !resource.requesters.isEmpty()) {
                for(ThreadObject blockedThread : resource.requesters) {
                    System.out.println("I'm blocked: " + blockedThread.name + " by " + resource.holder.name);
                    List<ThreadObject> oldNeighbors = graph.getOrDefault(blockedThread, new LinkedList<>());
                    oldNeighbors.add(resource.holder);
                    graph.put(blockedThread, oldNeighbors);
                }
            }
        }
    }

    public List<String> detectCycle() throws IOException {
        List<ThreadObject> threads = Utils.parseData("deadlock_thread_data.csv");
        System.out.println(threads.toString());
        HashMap<Long, List<ThreadObject>> uniquetimestamps = new HashMap<>();

        // for each unique timestamp, get all the threads at that time
        // and build a graph for them, then try to detect a cycle
        for(ThreadObject thread : threads) {
            List old = uniquetimestamps.getOrDefault(thread.time, new LinkedList<>());
            old.add(thread);
            uniquetimestamps.put(thread.time, old);
        }

        for(Long time : uniquetimestamps.keySet()) {
            buildGraph(uniquetimestamps.get(time));
            if(graph.isEmpty()) continue;
            GraphNode cycleHead = dfs(graph.keySet().iterator().next());
            if(cycleHead == null) continue;;
            return extractCycle(cycleHead);
        }
        return new LinkedList<>();
    }


    public List<String> extractCycle(GraphNode root) {
        HashMap<GraphNode, Boolean> visited = new HashMap<>();
        List<String> ret = new LinkedList<>();
        while(root.parent != null) {
            ThreadObject parent = root.parent.value;
            ThreadObject cur = root.value;
            Set<String> s1 = new HashSet<>(parent.wantedLocks);
            System.out.println("parent " + parent.name + " wants " + s1.toString());
            System.out.println("cur " + cur.name + " has " + cur.lockedObjects.toString());
            s1.retainAll(cur.lockedObjects);
            String str = parent.name + " requesting => " + s1.toString() + " <= holding " + cur.name;
            ret.add(0, str);
            visited.put(root, true);
            root = root.parent;
            System.out.println(root.parent);
        }
        return ret;
    }

    public static void main(String[] ar) throws IOException {
        ThreadObject one = new ThreadObject("thread-one");
        one.lockedObjects = Arrays.asList("database");
        one.wantedLocks = Arrays.asList("redis");

        ThreadObject two = new ThreadObject("thread-two");
        two.lockedObjects = Arrays.asList("redis");
        two.wantedLocks = Arrays.asList("elasticsearch", "authenticator");

        ThreadObject three = new ThreadObject("thread-three");
        three.lockedObjects = Arrays.asList("elasticsearch");
        three.wantedLocks = Arrays.asList("database");

        ThreadObject four = new ThreadObject("thread-four");
        four.lockedObjects = Arrays.asList("authenticator");
        four.wantedLocks = Arrays.asList("api-gateway");


        //

//        HashMap<ThreadObject, List<ThreadObject>> graph = new HashMap<>();
//        graph.put(one, new ArrayList<>(Arrays.asList(two)));
//        graph.put(two, new ArrayList<>(Arrays.asList(three)));
//        graph.put(three, new ArrayList<>(Arrays.asList(one)));
//        graph.put(two, new ArrayList<>(Arrays.asList(four)));

        DeadLockFinder dlf = new DeadLockFinder();
        dlf.buildGraph(Arrays.asList(one, two, three, four));
        if(dlf.graph.isEmpty()) System.out.println("nothing to show");
        GraphNode cycleHead = dlf.dfs(dlf.graph.keySet().iterator().next());
        if(cycleHead == null) System.out.println("nothing to show");
        System.out.println(dlf.extractCycle(cycleHead).toString());
//        List<String> ret = dlf.detectCycle();
//        System.out.println(ret.toString());
    }

}
