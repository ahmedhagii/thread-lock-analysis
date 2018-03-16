package com.dynatrace.threadlock.objects;

import java.util.*;

public class StreamGraphObject {

    class Series {
        String name;
        Collection<Integer> data;

        public Series(String name, Collection<Integer> data) {
            this.name = name;
            this.data = data;
        }
    }

    Set<Long> timesteps;
    List<Series> series;

    public StreamGraphObject() {
        timesteps = new HashSet<>();
        series = new LinkedList<>();
    }

    public void addData(String name, Collection<Integer> data) {
//        List<Integer> d = (List<Integer>) data;
        List<Integer> dataList = new LinkedList<>();
        dataList.add(0);
        for(Iterator<Integer> it = data.iterator(); it.hasNext();) dataList.add(it.next());
        series.add(new Series(name, dataList));
    }
    public void addTimeStep(Long timestep) {
        timesteps.add(timestep);
    }

    public Set<Long> getTimesteps() {
        return timesteps;
    }
}
