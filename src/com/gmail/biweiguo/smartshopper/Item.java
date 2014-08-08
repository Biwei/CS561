package com.gmail.biweiguo.smartshopper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import android.util.Log;

public class Item implements Comparable<Item> {

	private static final String defaultString = "01/01/2114"; //a remote day
	private static boolean cartMode = true; 
	private static boolean boughtMode = true; //true for showing details
    private String itemName;
    private int id;
    //private int count;
    private String store;
    private String dateString;
    private Date date;
    private float price;
    
    static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private static final Date defaultDate = parseDate(defaultString);
    
    
    public static Date parseDate (String str) {
    	Date date = null;
    	try {
    		date = sdf.parse(str);
    	}catch (ParseException e) {
    		e.printStackTrace();
    		Log.d("Item", "Can't format date!");
    	}
    	return date;
    }
    
    public Item()
    {
        this.itemName = "";
        this.store = "";
        this.date = defaultDate;
        this.dateString = defaultString;
        this.price = -1;		//for an item that's not bought yet
    }
    
    public Item(String itemName) {
        super();
        this.itemName = itemName;
        this.store = "";
        this.date = defaultDate;
        this.dateString = defaultString;
        this.price = -1;
    }
    
    public void setDefault() {
    	
    	if(store.equals(""))
    		this.store = "wherever";
    	if(dateString.equals("")) {
    		setDateString(defaultString);
    		setDate(defaultDate);
    	}
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
    
    public Date getDate() {
    	return date;
    }
    
    public void setDate (Date date) {
    	this.date = date;
    }
    
    public String getDateString() {
    	return dateString;
    }
    
    public void setDateString(String str) {
    	this.dateString = str;
    }
    
    public static void setCartMode(boolean mode){
    	
    	cartMode = mode;
    }
    
    public static void setBoughtMode(boolean mode) {
    	
    	boughtMode = mode;
    }
   
    @Override
    public String toString() {
    	
    	StringBuilder sb = new StringBuilder();
    	String str = new String();
    	
    	//for items on shopping list
    	if(price == -1) {
    		if(cartMode) {
	    		if(dateString.equals(defaultString)) 
	    			str = sb.append(itemName).append(" from ").append(store).append(" by ").append("whenever").toString();
	    		else
	    			str = sb.append(itemName).append(" from ").append(store).append(" by ").append(dateString).toString();
	    	}
	    	else
	    		str = itemName;		
    	}
    	//for items on bought list, without price information
    	else if(price == 0) {
	    	if(boughtMode) {
	    		if(store.equals("wherever")) {
	    			str = sb.append(itemName).append(" on ").append(dateString).toString();
	    		}
	    		else
	    			str = sb.append(itemName).append(" from ").append(store).append(" on ").append(dateString).toString();
	    	}
	    	else
	    		str = itemName;
    	}
    	//for items on bought list, with price information
    	else {
    		if(boughtMode) {
	    		if(store.equals("wherever")) {
	    			str = sb.append(itemName).append(" on ").append(dateString).append(" price: $").append(price).toString();
	    		}
	    		else
	    			str = sb.append(itemName).append(" from ").append(store).append(" on ").append(dateString).
	    					append(" price: $").append(price).toString();
	    	}
	    	else
	    		str = itemName;
    	}
    	
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
	
	public static Comparator<Item> DateComparator = new Comparator<Item>() {

		public int compare(Item item1, Item item2) {

			Date date1 = item1.getDate();
			Date date2 = item2.getDate();

			//descending order
			return date1.compareTo(date2);

		}

	};

 
}

