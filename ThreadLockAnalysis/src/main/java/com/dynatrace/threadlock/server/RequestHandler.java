package com.dynatrace.threadlock.server;

public abstract class RequestHandler extends Thread {
    String param;
    String name;
    int index;
    PreciousResource[] resourceArray;

    public RequestHandler(String name, String param, int index, PreciousResource[] resourceArray) {
        this.param = param;
        this.name = name;
        this.index = index;
        this.resourceArray = resourceArray;
    }

    public abstract void run();



//    public abstract void run();
//        this.setName(name);
//        synchronized (dataObj[index]) {
//            System.out.println("treemap thread ...");
//            List<TreeMapObject> data = null;
//            try {
//                data = getTreeMapData(this.param);
//                Thread.sleep(1000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            dataObj[index] = Utils.toJson(data);
//        }
//    }

}
