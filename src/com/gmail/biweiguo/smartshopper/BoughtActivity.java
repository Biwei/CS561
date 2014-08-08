package com.gmail.biweiguo.smartshopper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;

public class BoughtActivity extends CommonActivity  {
	
	ToggleButton toggle1;
	private Button clear;

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
        
        toggle1 = (ToggleButton)findViewById(R.id.show_details1);
        boolean mode = getPreferences("etatToggle1",this);
	    toggle1.setChecked(mode);
	    Item.setBoughtMode(mode);
	    setPreferences("etatToggle1", toggle1.isChecked(), this);
        
        Button del = (Button) findViewById(R.id.button_delete_bought);
        clear = (Button)findViewById(R.id.button_clear);
        
        del.setOnClickListener(listenerDel);
        clear.setOnLongClickListener(listenerClear);
	}

    OnLongClickListener listenerClear = new OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
        // TODO Auto-generated method stub
        db.removeAllHistory();
        adapter.clear();
        list = db.getAllPurchases();
        adapter.notifyDataSetChanged();
        Toast.makeText(BoughtActivity.this,"Button long click", Toast.LENGTH_SHORT).show();
        return true;
        
        }
    };

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bought, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onStop(){
	    super.onStop();
	    setPreferences("etatToggle1", toggle1.isChecked(), this);
	}
	
	@Override
	public void onPause(){
	    super.onPause();
	    setPreferences("etatToggle1", toggle1.isChecked(), this);
	}
	
	@Override
	public void onRestart(){
	    super.onRestart();
	    boolean mode = getPreferences("etatToggle1",this);
	    toggle1.setChecked(mode);
	    Item.setBoughtMode(mode);
	}
	
	@Override
	public void onStart(){
	    super.onStart();
	    boolean mode = getPreferences("etatToggle1",this);
	    toggle1.setChecked(mode);
	    Item.setBoughtMode(mode);
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
	
	public void onToggleClicked(View view) {
	    // Is the button now checked?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    Item.setBoughtMode(on);
	    
	    adapter.notifyDataSetChanged();
	}
	
	
	public void backButtonPressed() {
	    // Do something in response to button
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
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
	
	@Override
    public void onResume(){
		super.onResume();
		boolean mode = getPreferences("etatToggle1",this);
	    toggle1.setChecked(mode);
	    Item.setBoughtMode(mode);
        adapter.clear();
        list = db.getAllPurchases();
        adapter = new ArrayAdapter<Item>(this, R.layout.my_listview, list);
        ListView updatedListTask = (ListView) findViewById(android.R.id.list);
        updatedListTask.setAdapter(adapter);
    }
	

}
