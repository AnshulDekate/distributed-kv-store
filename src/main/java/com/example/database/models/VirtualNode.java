package com.example.database.models;

import com.google.gson.annotations.Expose;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashSet;
@ToString(exclude = "refNode")
public class VirtualNode {
    @Expose
    public String ID;

    public Node refNode;
    @Expose
    public HashSet<String> keys;

    public VirtualNode(String ID){
        this.ID = ID;
        this.keys = new HashSet<>();
    }
}
