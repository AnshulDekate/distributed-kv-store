package com.example.database;

import com.example.database.Reconciliation.Standard;
import com.example.database.hashing.RandomString;
import com.example.database.models.Node;
import com.example.database.models.Store;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DatabaseApplication {

	public static void main(String[] args) {
//		SpringApplication.run(DatabaseApplication.class, args);

		int numVirtualNodes = 20;
		int numReplica = 3;
		Store c = new Store(5, numVirtualNodes, numReplica);
		String s = "key-";
		for (int i=0; i<200; i++){
			String key = s + Integer.toString(i);
			Integer value = i;
			c.put(key, value);
		}
		c.addNode(numVirtualNodes, numReplica);
		c.addNode(numVirtualNodes, numReplica);
		c.addNode(numVirtualNodes, numReplica);
		c.removeNode("Node-4");
		c.removeNode("Node-7");
		c.addNode(numVirtualNodes, numReplica);
		c.removeNode("Node-5");
		c.showActualNodes();

		// this is adding value not replacing it, so result should be 56 after reconciliation
		c.put("key-50", 6);

		for (int i=0; i<10; i++){
			System.out.println(c.get("key-50"));
		}

		for (Node node: c.nodes){
			Standard.reconcile(node);
		}

		c.showActualNodes();
		for (int i=0; i<10; i++){
			System.out.println(c.get("key-50"));
		}
	}
}
