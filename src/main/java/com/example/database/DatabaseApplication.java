package com.example.database;

import com.example.database.hashing.SHA256;
import com.example.database.hashing.RandomString;
import com.example.database.models.Node;
import com.example.database.models.NodeFactory;
import com.example.database.models.Store;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

@SpringBootApplication
public class DatabaseApplication {

	public static void main(String[] args) {
//		SpringApplication.run(DatabaseApplication.class, args);

		Store c = new Store(5, 20);
		String s = "key-";
		for (int i=0; i<200; i++){
			String key = s + Integer.toString(i);
			String value = RandomString.generateRandomString(10);
			c.put(key, value);
		}

		c.showCnt();
	}

}
