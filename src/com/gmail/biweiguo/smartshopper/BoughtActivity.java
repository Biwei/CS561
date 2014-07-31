package com.gmail.biweiguo.smartshopper;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class BoughtActivity extends ListActivity  {
	
    ArrayList<Item> list;
    ArrayAdapter<Item> adapter;
    protected static DbHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bought);
		db = DbHelper.getInstance(this);
        list = db.getAllPurchases();
        adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_multiple_choice, list);
        ListView listItem = (ListView) findViewById(android.R.id.list);
        listItem.setAdapter(adapter);
        
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

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bought, menu);
		return true;
	}
	*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void backButtonPressed(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}