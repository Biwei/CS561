/*************************************************************
* Copyright (c) 2014 Biwei Guo
* [This program is licensed under the "MIT License"]
* Please see the file COPYING in the source
* distribution of this software for license terms.
**************************************************************/

package com.gmail.biweiguo.smartshopper;

import com.gmail.biweiguo.smartshopper.Item;

import java.util.ArrayList;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	
	private static DbHelper singleInstance;
	
	private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ShoppingDatabase";
    // to-buy table name
    private static final String TABLE_ITEMS = "items";
    //bought table name
    private static final String TABLE_BOUGHT = "bought";
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    //private static final String KEY_COUNT = "count";
    private static final String KEY_STORE = "store";
    private static final String KEY_DATE = "date";
    private static final String KEY_PRICE = "price";
    // to buy table create statement
    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE IF NOT EXISTS " 
    		+ TABLE_ITEMS + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "	//column 0
            + KEY_NAME + " TEXT, "								//column 1
            + KEY_STORE + " TEXT, "								//column 2
            + KEY_DATE + " TEXT)";								//column 3
    
    // bought table create statement
    private static final String CREATE_TABLE_BOUGHT = "CREATE TABLE IF NOT EXISTS " 
    		+ TABLE_BOUGHT + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "	//column 0	
            + KEY_NAME + " TEXT, "								//column 1
            + KEY_STORE + " TEXT, "								//column 2	
            + KEY_DATE + " TEXT, "								//column 3	
            + KEY_PRICE + " DOUBLE)";							//column 4
    
    public static DbHelper getInstance(Context context) {

        // Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        if (singleInstance == null) {
          singleInstance = new DbHelper(context.getApplicationContext());
        }
        return singleInstance;
      }
 
    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	
	@Override
	public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_ITEMS);
        db.execSQL(CREATE_TABLE_BOUGHT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOUGHT);
        // Create tables again
        onCreate(db);
	}

	//Table items related methods:
	public void addItem(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, item.getItemName()); // item name
		//values.put(KEY_COUNT, item.getCount()); // number of item
		values.put(KEY_STORE, item.getStore()); // where to buy
		values.put(KEY_DATE, item.getDateString()); // purchase deadline
		//values.put(KEY_PRICE, item.getPrice()); //purchase price
		// Inserting Row
		db.insert(TABLE_ITEMS, null, values);

		Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
		cursor.moveToFirst();
		int id = cursor.getInt(0);
		item.setId(id);
	    
		db.close(); // Closing database connection
		}
	
	public void removeItem(long id) {
		
		SQLiteDatabase db = this.getWritableDatabase();
        //String string =String.valueOf(id);
        db.execSQL("DELETE FROM " + TABLE_ITEMS + " WHERE id = '" + id + "'");
        Log.d("items ", id + " deleted");
        db.close();
    }
	
	public ArrayList<Item> getAllItems() {
		ArrayList<Item> itemList = new ArrayList<Item>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ITEMS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
		do {
		Item item = new Item();
		item.setId(cursor.getInt(0));
		item.setItemName(cursor.getString(1));
		//item.setCount(cursor.getInt(2));
		item.setStore(cursor.getString(2));
		item.setDateString(cursor.getString(3));
		Date date = Item.parseDate(item.getDateString());
	    item.setDate(date);
		
		itemList.add(item);
		} while (cursor.moveToNext());
		}
		
		return itemList;
		}
	
	public void updateItem(Item item) {
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_NAME, item.getItemName()); // item name
		//values.put(KEY_COUNT, item.getCount()); // number of item
		values.put(KEY_STORE, item.getStore()); // where to buy
		values.put(KEY_DATE, item.getDateString()); // purchase deadline

		db.update(TABLE_ITEMS, values, KEY_ID + " = ?",
		new String[]{String.valueOf(item.getId())});
		}
	
	public Item getItemByName(String name) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    String selectQuery = "SELECT  * FROM " + TABLE_ITEMS + " WHERE "
	            + KEY_NAME + " = '" + name + "'";
	 
	    Log.e("items", "data selected");
	 
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    Item item;
	 
	    if (cursor.moveToFirst())  {
			item = new Item();
		    item.setId(cursor.getInt(0));
		    item.setItemName(cursor.getString(1));
		    item.setStore(cursor.getString(2));
		    item.setDateString(cursor.getString(3));
		    Date date = Item.parseDate(item.getDateString());
		    item.setDate(date);
		    //item.setPrice(curcor.getFloat(cursor.getColumnIndex(KEY_PRICE)));
		    
		    return item;
	    }
	    else
	    	return null;
			
	}


	//purchase table related methods:
	public Item getItem(long id) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    String selectQuery = "SELECT  * FROM " + TABLE_ITEMS + " WHERE "
	            + KEY_ID + " = " + id;
	 
	    Log.e("items", "data selected");
	 
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    Item item;
	 
	    if (cursor != null)
            cursor.moveToFirst();
		item = new Item();
	    item.setId(cursor.getInt(0));
	    item.setItemName(cursor.getString(1));
	    item.setStore(cursor.getString(2));
	    item.setDateString(cursor.getString(3));
	    Date date = Item.parseDate(item.getDateString());
	    item.setDate(date);
	    //item.setPrice(curcor.getFloat(cursor.getColumnIndex(KEY_PRICE)));
	    
	    return item;
		//Didn't close database because the item may need to be added to the other table.
	}
	
	public Item getPurchase(long id) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    String selectQuery = "SELECT  * FROM " + TABLE_BOUGHT + " WHERE "
	            + KEY_ID + " = " + id;
	 
	    Log.e("bought", "data selected");
	 
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    Item item;
	 
	    if (cursor != null)
            cursor.moveToFirst();
		item = new Item();
	    item.setId(cursor.getInt(0));
	    item.setItemName(cursor.getString(1));
	    item.setStore(cursor.getString(2));
	    item.setDateString(cursor.getString(3));
	    Date date = Item.parseDate(item.getDateString());
	    item.setDate(date);
	    item.setPrice(cursor.getDouble(cursor.getColumnIndex(KEY_PRICE)));
	    
	    return item;
		//Didn't close database because the item may need to be added to the other table.
	}
	
	public void updatePurchase(Item item) {
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_NAME, item.getItemName()); // item name
		//values.put(KEY_COUNT, item.getCount()); // number of item
		values.put(KEY_STORE, item.getStore()); // where to buy
		values.put(KEY_DATE, item.getDateString()); // purchase deadline
		values.put(KEY_PRICE, item.getPrice());//purchase price
		db.update(TABLE_BOUGHT, values, KEY_ID + " = ?",
		new String[]{String.valueOf(item.getId())});
		}

	public ArrayList<Item> getAllPurchases() {
		ArrayList<Item> itemList = new ArrayList<Item>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_BOUGHT;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
		do {
		Item item = new Item();
		item.setId(cursor.getInt(0));
		item.setItemName(cursor.getString(1));
		//item.setCount(cursor.getInt(2));
		item.setStore(cursor.getString(2));
	    item.setDateString(cursor.getString(3));
	    Date date = Item.parseDate(item.getDateString());
	    item.setDate(date);
	    item.setPrice(cursor.getDouble(4));
		
		itemList.add(item);
		} while (cursor.moveToNext());
		}
		
		return itemList;
		}
	
	
	public void addPurchase(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();
        Date currentDate = new Date ();
        String currentDateStr = Item.sdf.format(currentDate);

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, item.getItemName()); // item name
		//values.put(KEY_COUNT, item.getCount()); // number of item
		values.put(KEY_STORE, item.getStore()); // where to buy
		values.put(KEY_DATE, currentDateStr); // purchase date
		values.put(KEY_PRICE, 0); //purchase price
		// Inserting Row
		db.insert(TABLE_BOUGHT, null, values);
		db.close(); // Closing database connection
		}

	public void recordPurchase(long id) {
		
	    Item item = getItem(id);
	    removeItem(id);
	    addPurchase(item);      
	}
	
	public void removeBoughtItem(long id) {
		
		SQLiteDatabase db = this.getWritableDatabase();
        //String string = String.valueOf(id);
        db.execSQL("DELETE FROM " + TABLE_BOUGHT + " WHERE id = '" + id + "'");
        Log.d("bought ", id + " deleted");
        db.close();
    }
	
	public void removeAllHistory() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE from "+ TABLE_BOUGHT);
		db.close();
	}
	
}


