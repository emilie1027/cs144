package edu.ucla.cs.cs144;

import java.io.*;
import java.util.*;


public class Item {
    
    //private String itemId;
    private String[] itemAttr;
    
    Item() {}
    
    Item(String[] itemAttr) {
        this.itemAttr = itemAttr;
    }
    
    public String getItemID() {
        return this.itemAttr[0];
    }
    
    public String getName() {
        return this.itemAttr[1];
    }
    
    public String getCurrently() {
        return this.itemAttr[2];
    }
    
    public String getBuyPrice() {
        return this.itemAttr[3];
    }
    
    public String getFirstBid() {
        return this.itemAttr[4];
    }
    
    public String getNumberofBids() {
        return this.itemAttr[5];
    }
    
    public String getDescription() {
        return this.itemAttr[10];
    }
    
    /*Item(String itemId, String[] itemAttr) {
        this.itemId = itemId;
        this.itemAttr = itemAttr;
    }
     */
    /*
    public String name;
    public int currently;
    public int first_bid;
    public int number_of_bids;
    public String location;
    public String country;
    public String started;
    public String ends;
    public String description;
     
    
    public Item(String itemId, String name, int currently, int first_bid, int number_of_bids, String location, String country, String started, String ends, String description) {
        this.itemId = itemId;
        this.name = name;
        this.currently = currently;
        this.first_bid = first_bid;
        this.number_of_bids = number_of_bids;
        this.location = location;
        this.country = country;
        this.started = started;
        this.ends = ends;
        this.description = description;
     */
    
}
