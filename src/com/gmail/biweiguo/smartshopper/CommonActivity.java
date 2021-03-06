/*************************************************************
* Copyright (c) 2014 Biwei Guo
* [This program is licensed under the "MIT License"]
* Please see the file COPYING in the source
* distribution of this software for license terms.
**************************************************************/

package com.gmail.biweiguo.smartshopper;

import java.util.ArrayList;
import java.util.Collections;
import com.gmail.biweiguo.smartshopper.R;
import com.gmail.biweiguo.smartshopper.Item;
import com.gmail.biweiguo.smartshopper.DbHelper;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CommonActivity extends ListActivity {
	
    protected static DbHelper db;
    ArrayList<Item> list;
    ArrayAdapter<Item> adapter;
    Spinner sortBy;
    ToggleButton toggle;
    SharedPreferences preferences;
	
	public void editButtonPressed (View view) {
		SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
        int itemCount = getListView().getCount();
        int numberSelected = 0;
        int idSelected = 0;
        Item selected = new Item ();

        for(int i=itemCount-1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
            	numberSelected++;
            }
        }
        //
        if(numberSelected == 0) {
        	Toast.makeText(this, "You have to select an item for editing!", Toast.LENGTH_LONG).show();
        }
        else if(numberSelected > 1) {
        	Toast.makeText(this, "You can only edit one item at a time!", Toast.LENGTH_LONG).show();
        }
        else {
        	for(int i=itemCount-1; i >= 0; i--){
                if(checkedItemPositions.get(i)){
                	selected = list.get(i);
                	idSelected = selected.getId();
                }
            }
        	Intent intent = new Intent(this, EditItem.class);
        	intent.putExtra("selected", idSelected);
        	startActivity(intent);
        }
	}

	/** Defining a click event listener for the button "Delete" */
    OnClickListener listenerDel = new OnClickListener() {
        @Override
        public void onClick(View v) {
            /** Getting the checked items from the listview */
            SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
            int itemCount = getListView().getCount();
            

            for(int i=itemCount-1; i >= 0; i--){
                if(checkedItemPositions.get(i)){
                	Item selected = new Item ();
                	selected = list.get(i);
                    adapter.remove(selected);
                    db.removeItem(selected.getId());
                }
            }
            checkedItemPositions.clear();
            adapter.notifyDataSetChanged();
        }
    };

	public void sortByNothing() {
		
		adapter = new ArrayAdapter<Item>(this, R.layout.my_listview, list);
		ListView updatedListTask = (ListView) findViewById(android.R.id.list);
        updatedListTask.setAdapter(adapter);
		
	}
	
	public void sortByStore() {
		
		Collections.sort(list, Item.StoreComparator);
		adapter = new ArrayAdapter<Item>(this, R.layout.my_listview, list);
		ListView updatedListTask = (ListView) findViewById(android.R.id.list);
        updatedListTask.setAdapter(adapter);
		
	}
	
	public void sortByDate() {
		
		Collections.sort(list, Item.DateComparator);
		adapter = new ArrayAdapter<Item>(this, R.layout.my_listview, list);
		ListView updatedListTask = (ListView) findViewById(android.R.id.list);
        updatedListTask.setAdapter(adapter);		
	}
	
	public void sortByPrice() {
		
		Collections.sort(list, Item.PriceComparator);
		adapter = new ArrayAdapter<Item>(this, R.layout.my_listview, list);
		ListView updatedListTask = (ListView) findViewById(android.R.id.list);
        updatedListTask.setAdapter(adapter);		
	}
	
    public static void setPreferences(String key, Boolean value, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static Boolean getPreferences(String key, Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, true);
    }
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {
	    @Override
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	
	    		String choice = parent.getItemAtPosition(pos).toString();
	    		switch(choice) {
	    			case "Store":
	    				sortByStore();
	    				break;
	    			case "Deadline":
	    				sortByDate();
	    				break;
	    			case "Date":
	    				sortByDate();
	    				break;
	    			case "Price":
	    				sortByPrice();
	    				break;
	    			case "None":
	    				onResume();
	    				break;
	    			default:
	    				//do nothing
	    				break;
	    		}
	    				
    			
		        Toast.makeText(parent.getContext(), "Selected choice : " + choice, Toast.LENGTH_SHORT).show();
		    
	    	}
		 
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
		 
		    }
		}
}
