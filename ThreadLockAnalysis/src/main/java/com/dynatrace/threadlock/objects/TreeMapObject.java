package com.dynatrace.threadlock.objects;

public class TreeMapObject {

    String id;
    String name;
    String color;

    Integer value;
    String parent;

    public TreeMapObject() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
