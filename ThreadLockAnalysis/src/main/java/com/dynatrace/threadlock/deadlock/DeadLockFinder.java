package com.dynatrace.threadlock.deadlock;

import java.util.*;

public class DeadLockFinder {

    private HashMap<Resource, List<Resource>> graph;
    private HashMap<Resource, GraphNode> dic;
    private GraphNode root;

    public DeadLockFinder(HashMap<Resource, List<Resource>> graph) {
        this.graph = graph;
    }

    public List<String> detectCycle() {
        if(graph.isEmpty()) return null;
        GraphNode cycleHead = dfs(graph.keySet().iterator().next());
        if(cycleHead == null) return null;
        return extractCycle(cycleHead);
    }

    public GraphNode dfs(Resource root) {
        Stack<Resource> ST = new Stack<Resource>();
        HashMap<Resource, Boolean> visited = new HashMap<>();
        dic = new HashMap<>();

        ST.add(root);
        visited.put(root, true);
        GraphNode currGraphNode = new GraphNode(null, root);
        dic.put(root, currGraphNode);

        while(!ST.empty()) {
            Resource curr = ST.pop();
            List<Resource> children = graph.getOrDefault(curr, new ArrayList<>());

            for(Resource child : children) {
                GraphNode childGraphNode = new GraphNode(dic.get(curr), child);
                if(visited.getOrDefault(child, false)) {
                    return childGraphNode;
                }
                ST.add(child);
                visited.put(child, true);
                dic.put(child, childGraphNode);
            }
        }
        return null;
    }

    public List<String> extractCycle(GraphNode root) {
        HashMap<GraphNode, Boolean> visited = new HashMap<>();
        List<String> ret = new LinkedList<>();
        while(root.parent != null) {
            String str = root.parent.value.holder.name + "\nHolding: " + root.parent.value.name + "\nRequesting: " + root.value.name;
            ret.add(0, str);
            visited.put(root, true);
            root = root.parent;
        }
        return ret;
    }

    class GraphNode {
        GraphNode parent;
        Resource value;

        public GraphNode(GraphNode parent, Resource value) {
            this.parent = parent;
            this.value = value;
        }
    }

    public void buildGraph() {

    }


    public static void main(String[] ar) {
        Resource one = new Resource("database", new ThreadObject("0x938232", "thread-one", null, 1));
        Resource two = new Resource("redis", new ThreadObject("0x938223", "thread-two", null, 1));
        Resource three = new Resource("elasticsearch", new ThreadObject("0x938223", "thread-three", null, 1));
        Resource four = new Resource("authenticator", new ThreadObject("0x938223", "thread-four", null, 1));

        HashMap<Resource, List<Resource>> graph = new HashMap<>();
        graph.put(one, new ArrayList<>(Arrays.asList(two)));
        graph.put(two, new ArrayList<>(Arrays.asList(three, four)));
        graph.put(three, new ArrayList<>(Arrays.asList(one)));
        DeadLockFinder dlf = new DeadLockFinder(graph);

        List<String> ret = dlf.detectCycle();
        System.out.println(ret.toString());
    }

}
