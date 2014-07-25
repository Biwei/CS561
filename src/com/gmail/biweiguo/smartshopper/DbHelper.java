package com.gmail.biweiguo.smartshopper;

import com.gmail.biweiguo.smartshopper.Item;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	
	private static DbHelper singleInstance;
	
	private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "ShoppingDatabase";
    // tasks table name
    private static final String TABLE_ITEMS = "items";
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    //private static final String KEY_COUNT = "count";
    private static final String KEY_STORE = "store";
    private static final String KEY_DATE = "deadline";
    
    public static DbHelper getInstance(Context context) {

        // Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
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
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT, "
                + KEY_STORE + " TEXT, "
                + KEY_DATE + " TEXT)";
        db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        // Create tables again
        onCreate(db);
	}

	public void addItem(Item item) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, item.getItemName()); // item name
		//values.put(KEY_COUNT, item.getCount()); // number of item
		values.put(KEY_STORE, item.getStore()); // where to buy
		values.put(KEY_DATE, item.getDate()); // purchase deadline

		// Inserting Row
		db.insert(TABLE_ITEMS, null, values);
		db.close(); // Closing database connection
		}
	
	public void removeItem(long id) {
		
		SQLiteDatabase db = this.getWritableDatabase();
        String string =String.valueOf(id);
        db.execSQL("DELETE FROM " + TABLE_ITEMS + " WHERE id = '" + string + "'");
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
		item.setDate(cursor.getString(3));
		// Adding contact to list
		itemList.add(item);
		} while (cursor.moveToNext());
		}
		// return task list
		return itemList;
		}
	
	public void updateTask(Item item) {
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_NAME, item.getItemName()); // item name
		//values.put(KEY_COUNT, item.getCount()); // number of item
		values.put(KEY_STORE, item.getStore()); // where to buy
		values.put(KEY_DATE, item.getDate()); // purchase deadline

		db.update(TABLE_ITEMS, values, KEY_ID + " = ?",
		new String[]{String.valueOf(item.getId())});
		}
}
