package com.example.database.models;

import ch.qos.logback.core.joran.sanity.Pair;
import lombok.ToString;

import java.util.*;

//@ToString
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
