package com.example.database.models;

import com.example.database.hashing.SHA256;

import java.util.*;

public class Store {
    TreeMap<byte[], VirtualNode> HashRing;
    public ArrayList<Node> nodes;
    int nxtID;
    int numVirtualNodes;
    int numReplica;
    public Store(int numNodes, int numVirtualNodes, int numReplica){
        this.numVirtualNodes = numVirtualNodes;
        this.numReplica = numReplica;

        this.nodes = NodeFactory.get(numNodes, numVirtualNodes, numReplica, this.nxtID);
        this.nxtID += this.nodes.size();

        TreeMap<byte[], VirtualNode> HashRing = new TreeMap<>(new ByteArrayComparator());
        for (Node node : this.nodes ){
            for (VirtualNode vNode : node.vNodeList){
                byte[] hashKey = SHA256.hash(vNode.ID);
                HashRing.put(hashKey, vNode);
            }
        }
        this.HashRing = HashRing;
    }

    // Custom comparator for byte arrays
    static class ByteArrayComparator implements Comparator<byte[]> {
        @Override
        public int compare(byte[] arr1, byte[] arr2) {
            // Compare byte arrays lexicographically
            for (int i = 0; i < Math.min(arr1.length, arr2.length); i++) {
                int byte1 = Byte.toUnsignedInt(arr1[i]);
                int byte2 = Byte.toUnsignedInt(arr2[i]);
                int diff = Integer.compare(byte1, byte2);
                if (diff != 0) {
                    return diff;
                }
            }
            return Integer.compare(arr1.length, arr2.length);
        }
    }

    public VirtualNode nxtNode(String key){
        byte[] hashValue = SHA256.hash(key);
        Map.Entry<byte[], VirtualNode> entry = this.HashRing.ceilingEntry(hashValue);
        if (entry == null) {
            entry = this.HashRing.firstEntry();
        }
        return entry.getValue();
    }

    public Integer get(String key){
        VirtualNode vNode = nxtNode(key);
        return vNode.refNode.get(key);
    }

    public void put(String key, Integer val){
        VirtualNode vNode = nxtNode(key);
        vNode.keys.add(key);
        vNode.refNode.put(key, val);
    }

    public void addNode(int numVirtualNodes, int numReplica){
        ArrayList<Node> nodes = NodeFactory.get(1, numVirtualNodes, numReplica, this.nxtID++);
        this.nodes.add(nodes.get(0));
        // for every new virtual node
        for (VirtualNode newVirtualNode: nodes.get(0).vNodeList){
            byte[] hashValue = SHA256.hash(newVirtualNode.ID);
            // order of following operations is very crucial

            // determine existing next virtual node
            VirtualNode vNxt = nxtNode(newVirtualNode.ID);
            // put this one in the map
            this.HashRing.put(hashValue, newVirtualNode);

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

                int val = vNxt.refNode.getSure(key);

                vNxt.refNode.remove(key);

                // put on this virtual node
                newVirtualNode.keys.add(key);
                newVirtualNode.refNode.put(key, val);
            }

        }
    }

    public void removeNode(String NodeID){
        Node currNode = null;
        for (Node node:this.nodes){
            if (node.ID.equals(NodeID)){
                currNode = node;
                break;
            }
        }

        // for every virtual node to be removed
        for (VirtualNode virtualNode: currNode.vNodeList){
            byte[] hashValue = SHA256.hash(virtualNode.ID);
            // remove this one from the map
            this.HashRing.remove(hashValue);
            // determine existing next virtual node
            VirtualNode nxtNode = this.nxtNode(virtualNode.ID);
            // identify keys to reshard
            HashSet<String> keysToReShard = virtualNode.keys;
            for (String key: keysToReShard) {
                nxtNode.keys.add(key);
                nxtNode.refNode.put(key, currNode.getSure(key));
            }
        }

        // de-link current node but use it later code, automatically garbage collected
        this.nodes.remove(currNode);

    }

//    public void showAll(){
//        for (Node node: this.nodes){
//            System.out.println(node.ID);
//            for (String key: node.keySet()){
//                System.out.print(key + ", ");
//                System.out.println(node.get(key));
//            }
//        }
//    }

    public static void printHashRing(TreeMap<byte[], Node> nodeMap){
        for (Map.Entry<byte[], Node> entry : nodeMap.entrySet()) {
            SHA256.printByteArrayAsBits(entry.getKey());
            System.out.println(entry.getValue().ID);
        }
    }

    public void showActualNodes() {
        int sum = 0;
        System.out.println(this.nodes.size());
        for (Node node: this.nodes){
            System.out.println(node.ID);
            Set<String> ss = new HashSet<>();
            for (Replica replica: node.replicaList) {
                for (String key : replica.data.keySet()) {
                    ss.add(key);
                }
            }
            sum += ss.size();
            System.out.println(ss.size());
            for (Replica replica: node.replicaList){
                System.out.printf("Replica %d\n", replica.ID);
                for (String key: replica.data.keySet()){
                    System.out.printf("%s %s ", key, replica.data.get(key));
                }
                System.out.println();
            }
        }
        System.out.printf("Total number of keys : %d \n", sum);
    }


}
