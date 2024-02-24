package com.example.database.models;

import com.example.database.hashing.SHA256;

import java.util.*;

public class Store {
    TreeMap<byte[], VirtualNode> nodes;
    HashSet<Node> actualNodes;
    public Store(int numNodes, int numVirtualNodes){
        this.nodes = NodeFactory.get(numNodes, numVirtualNodes);
//        System.out.println(this.nodes.size());
        this.actualNodes = new HashSet<Node>();
        for (VirtualNode node: this.nodes.values()){
            this.actualNodes.add(node.refNode);
        }

    }

    public Node getNode(String key){
        byte[] hashValue = SHA256.hash(key);
        Map.Entry<byte[], VirtualNode> entry = this.nodes.ceilingEntry(hashValue);
        if (entry == null) {
            entry = this.nodes.firstEntry();
        }
        return entry.getValue().refNode;
    }

    public String get(String key){
        Node node = getNode(key);
        return node.map.get(key);
    }

    public void put(String key, String val){
        Node node = getNode(key);
        node.map.put(key, val);
    }

    public void showAll(){
        for (Node node: this.actualNodes){
            System.out.println(node.ID);
            for (String key: node.map.keySet()){
                System.out.print(key + ", ");
                System.out.println(node.map.get(key));
            }
        }
    }

    public void showCnt() {
        for (Node node: this.actualNodes){
            System.out.println(node.ID);
            System.out.println(node.map.size());
        }
    }

}
