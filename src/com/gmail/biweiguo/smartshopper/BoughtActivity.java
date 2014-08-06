package com.gmail.biweiguo.smartshopper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gmail.biweiguo.smartshopper.MainActivity.MyOnItemSelectedListener;

import android.support.v7.app.ActionBarActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;

public class BoughtActivity extends ListActivity  {
	
    ArrayList<Item> list;
    ArrayAdapter<Item> adapter;
    protected static DbHelper db;
    private Spinner sortBy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bought);
		db = DbHelper.getInstance(this);
        list = db.getAllPurchases();
        adapter = new ArrayAdapter<Item>(this, R.layout.my_listview, list);
        ListView listItem = (ListView) findViewById(android.R.id.list);
        listItem.setAdapter(adapter);
        
        addChoicesOnSpinner();
        
        Button del = (Button) findViewById(R.id.button_delete_bought);
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
                        db.removeBoughtItem(selected.getId());
                    }
                }
                checkedItemPositions.clear();
                adapter.notifyDataSetChanged();
            }
        };
        del.setOnClickListener(listenerDel);
        
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bought, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.go_to_shop:
	        	backButtonPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }

	}
	
	public void editPriceButtonPressed (View view) {
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
        	Intent intent = new Intent(this, EditPrice.class);
        	intent.putExtra("selected", idSelected);
        	startActivity(intent);
        }
	}
	
	
	public void backButtonPressed() {
	    // Do something in response to button
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
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
	
	public void hideDetails() {
		Item.hideDetails();
		onResume();
	}
	
	public void showDetails() {
		Item.showDetails();
		onResume();
	}
	
	public void noSort(){
		onResume();
	}
	
	public void addChoicesOnSpinner() {
		 
		sortBy = (Spinner) findViewById(R.id.spinner_sort1);
		List<String> choiceList = new ArrayList<String>();
		choiceList.add("None");
		choiceList.add("Store");
		choiceList.add("Date");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, choiceList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sortBy.setAdapter(dataAdapter);
		sortBy.setOnItemSelectedListener(new MyOnItemSelectedListener());
	}
	
	public void onToggleClicked(View view) {
	    // Is the button now checked?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if(on)
	    	showDetails();
	    else
	    	hideDetails();
	}
	
	@Override
    public void onResume(){
    super.onResume();
        adapter.clear();
        list = db.getAllPurchases();
        adapter = new ArrayAdapter<Item>(this, R.layout.my_listview, list);
        ListView updatedListTask = (ListView) findViewById(android.R.id.list);
        updatedListTask.setAdapter(adapter);
    }
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {
	    @Override
		    public void onItemSelected(AdapterView parent, View view, int pos, long id) {
	    	
	    		String choice = parent.getItemAtPosition(pos).toString();
	    		switch(choice) {
	    			case "Store":
	    				sortByStore();
	    				break;
	    			case "Date":
	    				sortByDate();
	    				break;
	    			default:
	    				noSort();
	    				break;
	    		}
	    				
    			
		        Toast.makeText(parent.getContext(), "Selected choice : " + choice, Toast.LENGTH_SHORT).show();
		    
	    	}
		 
		    @Override
		    public void onNothingSelected(AdapterView parent) {
		 
		    }
		}
}
