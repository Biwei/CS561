package com.gmail.biweiguo.smartshopper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gmail.biweiguo.smartshopper.R;
import com.gmail.biweiguo.smartshopper.Item;
import com.gmail.biweiguo.smartshopper.DbHelper;
import com.gmail.biweiguo.smartshopper.AddItem;

import android.support.v7.app.ActionBarActivity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
    protected static DbHelper db;
    ArrayList<Item> list;
    ArrayAdapter<Item> adapter;
    private Spinner sortBy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        /** Reference to the delete button of the layout main.xml */
        Button del = (Button) findViewById(R.id.button_delete);
        Button bought = (Button)findViewById(R.id.button_bought);
        /** Prepare database */
        
        db = DbHelper.getInstance(this);
        list = db.getAllItems();
        
        addChoicesOnSpinner();
        
        /** Defining the ArrayAdapter to set items to ListView */
        adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_multiple_choice, list);
        ListView listItem = (ListView) findViewById(android.R.id.list);
        
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
 
 
        /** Setting the event listener for the delete button */
        del.setOnClickListener(listenerDel);

        /** Defining a click event listener for the button "Bought" */
        OnClickListener listenerBought = new OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Getting the checked items from the listview */
                SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
                int itemCount = getListView().getCount();
                Item selected = new Item ();
 
                for(int i=itemCount-1; i >= 0; i--){
                    if(checkedItemPositions.get(i)){
                    	selected = list.get(i);
                        adapter.remove(selected);
                        db.recordPurchase(selected.getId());
                    }
                }
                checkedItemPositions.clear();
                adapter.notifyDataSetChanged();
                goToBought(v);
            }
        };
 
 
        /** Setting the event listener for the delete button */
        bought.setOnClickListener(listenerBought);
        
        listItem.setAdapter(adapter);
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);

	}

	public void quickAddButtonPressed(View view) {
	    // Do something in response to button
		
		EditText editName = (EditText) findViewById(R.id.text_quick_add);
		String name = editName.getText().toString();
			
		if (name.equalsIgnoreCase("")) {
            Toast.makeText(this, "enter the item name at least!!",
                    Toast.LENGTH_LONG).show();
        } else {
        	Item newItem = new Item(name);
    		newItem.setDefault();
            db.addItem(newItem);
            Log.d("my list", "data added");
            editName.setText("");
            adapter.add(newItem);
    		adapter.notifyDataSetChanged();
        }
		
	}
	
	public void addButtonPressed(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, AddItem.class);
		startActivity(intent);
	}
	
	public void goToBought(View view) {
		
		Intent intent =  new Intent(this, BoughtActivity.class);
		
		
		startActivity(intent);
	}
	
	public void addChoicesOnSpinner() {
		 
		sortBy = (Spinner) findViewById(R.id.spinner_sort);
		List<String> choiceList = new ArrayList<String>();
		choiceList.add("Nothing");
		choiceList.add("Store");
		choiceList.add("Deadline");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, choiceList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sortBy.setAdapter(dataAdapter);
		sortBy.setOnItemSelectedListener(new MyOnItemSelectedListener());
	}


	@Override
    public void onResume(){
    super.onResume();
        adapter.clear();
        list = db.getAllItems();
        adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_multiple_choice, list);
        ListView updatedListTask = (ListView) findViewById(android.R.id.list);
        updatedListTask.setAdapter(adapter);
    }
	
	public void sortByNothing() {
		
		list = db.getAllItems();
		adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_multiple_choice, list);
		ListView updatedListTask = (ListView) findViewById(android.R.id.list);
        updatedListTask.setAdapter(adapter);
		
	}
	
	public void sortByStore() {
		
		Collections.sort(list, Item.StoreComparator);
		adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_multiple_choice, list);
		ListView updatedListTask = (ListView) findViewById(android.R.id.list);
        updatedListTask.setAdapter(adapter);
		
	}
	
	public void sortByDeadline() {
		
	}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {
	    @Override
		    public void onItemSelected(AdapterView parent, View view, int pos, long id) {
	    	
	    		String choice = parent.getItemAtPosition(pos).toString();
	    		switch(choice) {
	    			case "Store":
	    				sortByStore();
	    				break;
	    			case "Deadline":
	    				sortByDeadline();
	    				break;
	    			default:
	    				break;
	    		}
	    				
    			
		        Toast.makeText(parent.getContext(), "Selected choice : " + choice, Toast.LENGTH_SHORT).show();
		    
	    	}
		 
		    @Override
		    public void onNothingSelected(AdapterView parent) {
		 
		    }
		}
}
