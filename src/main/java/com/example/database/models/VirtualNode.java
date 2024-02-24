package com.example.database.models;

import java.util.HashSet;

public class VirtualNode {
    public String ID;
    public Node refNode;

    public HashSet<String> keys;

    public VirtualNode(){
        this.keys = new HashSet<>();
    }
}
