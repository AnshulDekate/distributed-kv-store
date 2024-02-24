package com.example.database.models;

import com.example.database.hashing.SHA256;

import java.util.*;

public class Store {
    TreeMap<byte[], VirtualNode> vNodes;
    HashSet<Node> nodes;
    int seed;
    int numVirtualNodes;
    public Store(int numNodes, int numVirtualNodes){
        this.seed = 0;
        this.numVirtualNodes = numVirtualNodes;
        this.vNodes = NodeFactory.get(numNodes, numVirtualNodes, this.seed);
        this.seed = numNodes-1;
//        System.out.println(this.nodes.size());
        this.nodes = new HashSet<Node>();
        for (VirtualNode virtualNode: this.vNodes.values()){
            this.nodes.add(virtualNode.refNode);
        }
    }

    public VirtualNode nxtNode(String key){
        byte[] hashValue = SHA256.hash(key);
        Map.Entry<byte[], VirtualNode> entry = this.vNodes.ceilingEntry(hashValue);
        if (entry == null) {
            entry = this.vNodes.firstEntry();
        }
        return entry.getValue();
    }

    public String get(String key){
        VirtualNode vNode = nxtNode(key);
        return vNode.refNode.map.get(key);
    }

    public void put(String key, String val){
        VirtualNode vNode = nxtNode(key);
        vNode.keys.add(key);
        vNode.refNode.map.put(key, val);
    }

    public void addNode(){
        TreeMap<byte[], VirtualNode> VirtualNodes = NodeFactory.get(1, numVirtualNodes, ++this.seed);

        // for every new virtual node
        for (Map.Entry<byte[], VirtualNode> entry: VirtualNodes.entrySet()){
            byte[] hashValue = entry.getKey();
            VirtualNode newVirtualNode = entry.getValue();

            // order of following operations is very crucial

            // determine existing next virtual node
            VirtualNode vNxt = nxtNode(newVirtualNode.ID);
            // put this one in the map
            this.vNodes.put(hashValue, newVirtualNode);
            this.nodes.add(newVirtualNode.refNode);

            // identify keys to reshard
            ArrayList<String> keysToReshard = new ArrayList<>();
            for (String key: vNxt.keys){
                VirtualNode vCurr = nxtNode(key);
                if (vCurr == newVirtualNode){
                    keysToReshard.add(key);
                }
            }

            for (String key: keysToReshard){
                // delete from nxt virtual node
                vNxt.keys.remove(key);
                String val = vNxt.refNode.map.get(key);
                vNxt.refNode.map.remove(key);

                // put on this virtual node
                newVirtualNode.keys.add(key);
                newVirtualNode.refNode.map.put(key, val);
            }

        }
    }

    public void removeNode(String NodeID){
        Node currNode = new Node();
        for (Node node:this.nodes){
            if (node.ID.equals(NodeID)){
                currNode = node;
                break;
            }
        }

        // de-link current node but use it later code, automatically garbage collected
        this.nodes.remove(currNode);

        // for every virtual node to be removed
        for (VirtualNode virtualNode: currNode.vNodeList){
            byte[] hashValue = SHA256.hash(virtualNode.ID);
            // remove this one from the map
            this.vNodes.remove(hashValue);
            // determine existing next virtual node
            VirtualNode nxtNode = this.nxtNode(virtualNode.ID);
            // identify keys to reshard
            HashSet<String> keysToReShard = virtualNode.keys;
            for (String key: keysToReShard) {
                nxtNode.keys.add(key);
                nxtNode.refNode.map.put(key, currNode.map.get(key));
            }
        }

    }

    public void showAll(){
        for (Node node: this.nodes){
            System.out.println(node.ID);
            for (String key: node.map.keySet()){
                System.out.print(key + ", ");
                System.out.println(node.map.get(key));
            }
        }
    }



    public void showActualNodes() {
        int sum = 0;
        for (Node node: this.nodes){
            System.out.println(node.ID);
            System.out.println(node.map.size());
            sum += node.map.size();
//            for (VirtualNode vn: node.vNodeList) {
//                System.out.printf("%s ", vn.ID);
//            }
//            System.out.println();
//            for (String key: node.map.keySet()){
//                System.out.printf(" %s ", key);
//            }
//            System.out.println();
        }
        System.out.printf("Total number of keys : %d \n", sum);
    }

}
