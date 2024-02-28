package com.example.database;

import com.example.database.hashing.RandomString;
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
//			String value = RandomString.generateRandomString(10);
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

	}
}
