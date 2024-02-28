package com.example.database.models;

import com.example.database.hashing.SHA256;

import java.util.*;

public class NodeFactory {
    public static ArrayList<Node> get(int numNodes, int numVirtualNodes, int numReplica, int nxt) {
        ArrayList<Node> nodeSet = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            String nodeID = String.format("Node-%d", nxt+i);
            Node node = new Node(nodeID);
            for (int j=0; j<numVirtualNodes; j++){
                String vNodeID = String.format("VirtualNode-%d%d", nxt+i, j);
                VirtualNode vNode = new VirtualNode(vNodeID);
                vNode.refNode = node;
                node.vNodeList.add(vNode);
            }
            for (int k=0; k<numReplica; k++){
                Replica replica = new Replica(k);
                node.replicaList.add(replica);
            }
            nodeSet.add(node);
        }
        return nodeSet;
    }
}
