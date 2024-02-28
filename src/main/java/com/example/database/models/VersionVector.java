package com.example.database.models;

import ch.qos.logback.core.joran.sanity.Pair;

import java.util.*;

public class VersionVector {
    public HashMap<Replica, Integer> pairs;
    public VersionVector(){
        pairs = new HashMap<>();
    }

    public Integer resolve(){
        int ret = 0;
        for (int val : this.pairs.values()){
            ret += val;
        }
        return ret;
    }
}
