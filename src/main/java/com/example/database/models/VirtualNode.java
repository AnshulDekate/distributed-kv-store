package com.example.database.models;

import java.util.HashSet;

public class VirtualNode {
    public String ID;
    public Node refNode;

    public HashSet<String> keys;

    public VirtualNode(String ID){
        this.ID = ID;
        this.keys = new HashSet<>();
    }
}
