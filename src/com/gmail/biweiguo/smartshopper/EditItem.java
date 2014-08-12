/*************************************************************
* Copyright (c) 2014 Biwei Guo
* [This program is licensed under the "MIT License"]
* Please see the file COPYING in the source
* distribution of this software for license terms.
**************************************************************/

package com.gmail.biweiguo.smartshopper;

import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditItem extends Activity {
	
	protected static DbHelper db;
	EditText editName, editStore, editDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_item);
		Bundle extras = getIntent().getExtras();
	    final int id = extras.getInt("selected");
		
		db = DbHelper.getInstance(this);
		
		Item oldItem = new Item();
		oldItem = db.getItem(id);
		
		Button saveButton = (Button)findViewById(R.id.button_save1);
		Button cancelButton = (Button)findViewById(R.id.button_cancel1);
		
		editName = (EditText) findViewById(R.id.textedit_item_name1);
		editName.setText(oldItem.getItemName());
		editName.setTextColor(Color.GRAY);
		
		editStore = (EditText) findViewById(R.id.textedit_store_name1);
		editStore.setText(oldItem.getStore());
		editStore.setTextColor(Color.GRAY);
		
		editDate = (EditText) findViewById(R.id.textedit_deadline1);
		if(!oldItem.getDateString().equals(Item.getDefaultString())) {
			editDate.setText(oldItem.getDateString());
			editDate.setTextColor(Color.GRAY);
		}
	     
        saveButton.setOnClickListener(new OnClickListener() {
           
            public void onClick(View v) {
            	Item newItem = new Item();
            	String name = editName.getText().toString();			
        		String store = editStore.getText().toString();			
        		String dateString = editDate.getText().toString();
        		Date date = Item.parseDate(dateString);
        		
        		newItem.setItemName(name);
        		//newItem.setCount(count);
        		newItem.setStore(store);
        		newItem.setDateString(dateString);
        		newItem.setDate(date);
        		newItem.setId(id);
        		newItem.setDefault();
        		
        		if (name.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "enter the item name at least!!",
                            Toast.LENGTH_LONG).show();
                } 
                else {
                    db.updateItem(newItem);
                    Log.d("items", "data updated");
                }
                finish();
               
            }
        });
        
        cancelButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		finish();
        	}
        });
	}
	
	
}
