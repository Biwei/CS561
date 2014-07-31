package com.gmail.biweiguo.smartshopper;

import java.util.Comparator;

public class Item implements Comparable<Item> {

    private String itemName;
    private int id;
    //private int count;
    private String store;
    private String date;
    private float price;
    
    public Item()
    {
        this.itemName = null;
        this.store = null;
        this.date = null;
        this.price = 0;
    }
    
    public Item(String itemName) {
        super();
        this.itemName = itemName;
    }
    
    public void setDefault() {
    	
    	if(store == null)
    		this.store = "wherever";
    	if(date == null)
    		this.date = "whenever";
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
    
 /*  I don't think count is that necessary 
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
    	this.count = count;
    }
*/
    
    public float getPrice() {
        return price;
    }
    
    public void setPrice(float price) {
    	this.price = price;
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
    
    @Override
    public String toString() {
    	
    	StringBuilder sb = new StringBuilder();
    	String str;

    	str = sb.append(itemName).append(" from ").append(store).append(" by ").append(date).toString();
    	if(store.equals("wherever") && date.equals("whenever"))
    		return itemName;
    	else
    		return str;

    }
    
    public int compareTo(Item compareItem) {
    	 
		String compare = ((Item) compareItem).getStore(); 
 
		//ascending order
		return this.getStore().compareTo(compare);
 
	}
    
	public static Comparator<Item> StoreComparator = new Comparator<Item>() {

		public int compare(Item item1, Item item2) {

			String store1 = item1.getStore().toUpperCase();
			String store2 = item2.getStore().toUpperCase();

			//ascending order
			return store1.compareTo(store2);

		}

	};

 
}

