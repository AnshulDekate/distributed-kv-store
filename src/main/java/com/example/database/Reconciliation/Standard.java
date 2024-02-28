package com.example.database.Reconciliation;
import com.example.database.hashing.SHA256;
import com.example.database.models.*;
import java.util.HashMap;
import java.util.Map;

public class Standard {
    public static void reconcile(Node node) {

        // fill version vectors
        for (Replica replica : node.replicaList) {
            for (String key : replica.data.keySet()) {
                if (replica.versionVectors.containsKey(key)){
                    VersionVector v = replica.versionVectors.get(key);
                    v.pairs.put(replica, replica.data.get(key));
                }
                else {
                    VersionVector v = new VersionVector();
                    v.pairs.put(replica, replica.data.get(key));
                    replica.versionVectors.put(key, v);
                }
            }
        }

        HashMap<String, VersionVector> finalVectors = new HashMap<>();
        for (Replica replica : node.replicaList){
            for (String key : replica.data.keySet()){
                VersionVector v = replica.versionVectors.get(key);
                if (finalVectors.containsKey(key)){
                    // conflict resolution
                    VersionVector to = finalVectors.get(key);
                    for (Map.Entry<Replica, Integer> entry : v.pairs.entrySet()) {
                        Replica r = entry.getKey();
                        int val = entry.getValue();
                        if (to.pairs.containsKey(r)){
                            int newVal = Math.max(to.pairs.get(r), val);
                            to.pairs.put(r, newVal);
                        }
                        else{
                            to.pairs.put(r, val);
                        }
                    }
                }
                else {
                    finalVectors.put(key, v);
                }
            }
        }

//        System.out.printf("size of final %d", finalVectors.size());
//        System.out.println();

//        for (String key: finalVectors.keySet()){
//            System.out.println(key);
//            for (int val: finalVectors.get(key).pairs.values()){
//                System.out.printf("%d ", val);
//            }
//            System.out.println();
//        }
//        System.out.println();

        // put these final vectors on each replica, add the keys which are not present
        for (Replica replica: node.replicaList){
            replica.versionVectors = finalVectors;
            for (String key: finalVectors.keySet()){
                replica.data.put(key, finalVectors.get(key).resolve());
            }
        }
    }
}
