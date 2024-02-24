package com.example.database.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    public String ID;
    public ArrayList<VirtualNode> vNodeList;
    public HashMap<String, String> map;
    public Node(){
        this.map = new HashMap<>();
    }
}
