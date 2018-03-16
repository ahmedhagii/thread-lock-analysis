package com.dynatrace.threadlock.objects;

import apple.laf.JRSUIUtils;

import java.util.*;

public class TreeObject {
    String name;
    Integer value;
    Set<TreeObject> children;
    public HashMap<String, TreeObject> map = new HashMap<>();
//    public HashMap<String, Integer> map = new HashMap<>();

    public TreeObject() {
        children = new HashSet<>();
    }

    public TreeObject(String name) {
        this.name = name;
        children = new HashSet<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setChildren(Set<TreeObject> children) {
        this.children = children;
    }

    public void addChild(TreeObject child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return name + ":" + children.toString();
    }

    public Set<TreeObject> getChildren() {
        return children;
    }

    public void incValue() {
        if(value == null) value = 0;
        value++;
    }
}
