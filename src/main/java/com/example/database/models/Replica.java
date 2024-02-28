package com.example.database.models;

import ch.qos.logback.core.joran.sanity.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Replica {
    public int ID;
    public HashMap<String, Integer> data;
    public HashMap<String, VersionVector> versionVectors;
    public Replica(Integer ID){
        this.ID = ID;
        this.data = new HashMap<>();
        this.versionVectors =  new HashMap<>();
    }
}
