package com.example.database.models;

import java.util.HashMap;

public class Node {
    public String ID;
    public HashMap<String, String> map;
    public Node(){
        this.map = new HashMap<String, String>();
    }
}
