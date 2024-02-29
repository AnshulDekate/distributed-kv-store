package com.example.database.models;

import com.google.gson.annotations.Expose;
import lombok.ToString;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

@ToString
public class Node {
    @Expose
    public String ID;
    public Random random;
    @Expose
    public ArrayList<Replica> replicaList;
    public ArrayList<VirtualNode> vNodeList;
    public Node(String ID){
        this.ID = ID;
        this.random = new Random();
        this.vNodeList = new ArrayList<>();
        this.replicaList = new ArrayList<>();
    }

    public Integer get(String key){
        int choice = this.random.nextInt(this.replicaList.size());
        System.out.printf("%s %d ", this.ID, choice);
        return this.replicaList.get(choice).data.getOrDefault(key, -1);
    }

    public void put(String key, Integer val){
        int choice = this.random.nextInt(this.replicaList.size());
        int currVal = 0;
        if (this.replicaList.get(choice).data.containsKey(key)){
            currVal = this.replicaList.get(choice).data.get(key);
        }
        this.replicaList.get(choice).data.put(key, currVal+val);
    }

    public Integer getSure(String key){
        for (Replica replica: this.replicaList){
            if (replica.data.containsKey(key)) {
                return replica.data.get(key);
            }
        }
        return -1;
    }

    public void remove(String key) {
        for (Replica replica: this.replicaList){
            replica.data.remove(key);
        }
    }
}
