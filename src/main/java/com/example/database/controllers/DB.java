package com.example.database.controllers;

import com.example.database.Reconciliation.Standard;
import com.example.database.models.Node;
import com.example.database.models.Store;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.ToString;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class DB {

    @GetMapping(value = "/store", produces = MediaType.APPLICATION_JSON_VALUE)
    public String data(){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String out = gson.toJson(Store.instance);
        return out;
    }

    @GetMapping(value = "/node/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String data(@PathVariable String id){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Node curr = null;
        for (Node node : Store.instance.nodes){
            if (node.ID.equals(id)){
                curr = node;
            }
        }
        return gson.toJson(curr);
    }

    @GetMapping(value = "/store/{key}")
    public Integer getKey(@PathVariable String key){
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        int val = -1;

        val = Store.instance.get(key);
        return val;
    }

    @PutMapping(value = "/store")
    public void putData(@RequestBody KVBody requestBody){
        Store.instance.put(requestBody.key, requestBody.val);
    }

    @PostMapping(value = "/reconcile")
    public void reconcile(){
        for (Node node: Store.instance.nodes){
			System.out.println(node);
			Standard.reconcile(node);
		}
    }
}



