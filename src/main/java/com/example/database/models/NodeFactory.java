package com.example.database.models;

import com.example.database.hashing.SHA256;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class NodeFactory {
    public static TreeMap<byte[], VirtualNode> get(int numNodes, int numVirtualNodes, int seed) {
        TreeMap<byte[], VirtualNode> nodeMap = new TreeMap<>(new ByteArrayComparator());
        for (int i = 0; i < numNodes; i++) {
            Node node = new Node();
            node.ID = String.format("Node-%d", seed+i);
            ArrayList<VirtualNode> vNodeList = new ArrayList<>();
            for (int j=0; j<numVirtualNodes; j++){
                VirtualNode vNode = new VirtualNode();
                vNode.ID = String.format("VirtualNode-%d%d%d", seed, i, j);
                vNode.refNode = node;
                vNodeList.add(vNode);
                byte[] hashValue = SHA256.hash(vNode.ID);
                nodeMap.put(hashValue, vNode);
            }
            node.vNodeList = vNodeList;
        }
//        printNodeMap(nodeMap);
        return nodeMap;
    }

    public static void printNodeMap(TreeMap<byte[], Node> nodeMap){
        for (Map.Entry<byte[], Node> entry : nodeMap.entrySet()) {
            SHA256.printByteArrayAsBits(entry.getKey());
            System.out.println(entry.getValue().ID);
        }
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
}
