package com.gmail.biweiguo.smartshopper;

public class Item {

    private String itemName;
    private int id;
    private int count;
    private String store;
    private String date;
    
    public Item()
    {
        this.itemName=null;
        this.store = null;
        this.date = null;
    }
    
    public Item(String itemName) {
        super();
        this.itemName = itemName;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
    	this.count = count;
    }

    public String getStore() {
    	return store;   	
    }
    
    public void setStore (String store) {
    	this.store = store;
    }
    
    public String getDate() {
    	return date;
    }
    
    public void setDate (String date) {
    	this.date = date;
    }
    
    public void setDefault() {
    	this.count = -1;
    	this.store = "wherever";
    	this.date = "whenever";  	
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	String str;
    	if(count == -1) {
    		str = sb.append(itemName).append(" from ").append(store).append(" by ").append(date).toString();
    	}
    	else {
    		str = sb.append(count).append(" ").append(itemName).append(" from ").append(store).append(" by ").append(date).toString();
    	}
    	return str;
    }
}

