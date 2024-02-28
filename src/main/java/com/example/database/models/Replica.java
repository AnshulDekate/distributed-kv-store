package com.example.database.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Replica {
    public int ID;
    public HashMap<String, Integer> data;

    public HashMap<String, HashMap<Integer, Integer>> versionVector;

    public Replica(Integer ID){
        this.ID = ID;
        this.data = new HashMap<>();
        this.versionVector =  new HashMap<>();
    }
}
