package com.gmail.biweiguo.smartshopper;

import java.util.ArrayList;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
    protected static DbHelper db;
    ArrayList<Item> list;
    ArrayAdapter<Item> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        /** Reference to the delete button of the layout main.xml */
        Button del = (Button) findViewById(R.id.button_delete);
        
        db = DbHelper.getInstance(this);
        list = db.getAllItems();
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
                Item selected = new Item ();
 
                for(int i=itemCount-1; i >= 0; i--){
                    if(checkedItemPositions.get(i)){
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
		
		Item newItem = new Item(name);
		newItem.setDefault();
		
		if (name.equalsIgnoreCase("")) {
            Toast.makeText(this, "enter the item name at least!!",
                    Toast.LENGTH_LONG).show();
        } else {
            db.addItem(newItem);
            Log.d("my list", "data added");
        }
		editName.setText("");
        adapter.add(newItem);
		adapter.notifyDataSetChanged();
	}
	
	public void addButtonPressed(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, AddItem.class);
		startActivity(intent);
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
}
