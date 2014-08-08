package com.gmail.biweiguo.smartshopper;

import java.util.Date;

import com.gmail.biweiguo.smartshopper.Item;
import com.gmail.biweiguo.smartshopper.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItem extends Activity {
	
	protected static DbHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_item);

		db = DbHelper.getInstance(this);
		
		Button addButton = (Button)findViewById(R.id.button_add);
		Button cancelButton = (Button)findViewById(R.id.button_cancel);
	     
        addButton.setOnClickListener(new OnClickListener() {
           
            public void onClick(View v) {
                addItem(v);
                finish();            
            }
        });
        
        cancelButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {      		
        		finish();
        	}
        });
	}
	
	public void addItem(View v) {
		
		Item newItem = new Item();
		
		EditText editName = (EditText) findViewById(R.id.textedit_item_name);
		String name = editName.getText().toString();
		
		//EditText editCount = (EditText) findViewById(R.id.textedit_item_count);
		//int count = Integer.parseInt(editCount.getText().toString());
		
		EditText editStore = (EditText) findViewById(R.id.textedit_store_name);
		String store = editStore.getText().toString();
		
		EditText editDate = (EditText) findViewById(R.id.textedit_deadline);
		String dateString = editDate.getText().toString();
		Date date = Item.parseDate(dateString);
		
		newItem.setItemName(name);
		//newItem.setCount(count);
		newItem.setStore(store);
		newItem.setDateString(dateString);
		newItem.setDate(date);
		newItem.setDefault();
		
        if (name.equalsIgnoreCase("")) {
            Toast.makeText(this, "enter the item name at least!!",
                    Toast.LENGTH_LONG).show();
        } 
        else if (db.getItemByName(name) != null) {
        	Toast.makeText(this, "item already exists!" , Toast.LENGTH_LONG).show();
        }
        else {
            MainActivity.db.addItem(newItem);
            Log.d("items", "data added");
        }
        
        //finish();
		
	}
}
