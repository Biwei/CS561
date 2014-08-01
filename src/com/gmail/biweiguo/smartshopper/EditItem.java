package com.gmail.biweiguo.smartshopper;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditItem extends Activity {
	
	protected static DbHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_item);
		Bundle extras = getIntent().getExtras();
	    final int id = extras.getInt("selected");
		
		db = DbHelper.getInstance(this);
		
		Button saveButton = (Button)findViewById(R.id.button_save1);
		Button cancelButton = (Button)findViewById(R.id.button_cancel1);
	     
        saveButton.setOnClickListener(new OnClickListener() {
           
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Intent intent=new Intent();
                editItem(id);
                //setResult(RESULT_OK, intent);
                finish();
               
            }
        });
        
        cancelButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		finish();
        	}
        });
	}
	
	public void editItem(int id) {
		
		Item newItem = new Item();
		Item oldItem = new Item();
		oldItem = db.getItem(id);
		
		EditText editName = (EditText) findViewById(R.id.textedit_item_name1);
		editName.setHint(oldItem.getItemName());
		editName.setHintTextColor(Color.GRAY);
		String name = editName.getText().toString();
		
		//EditText editCount = (EditText) findViewById(R.id.textedit_item_count);
		//int count = Integer.parseInt(editCount.getText().toString());
		
		EditText editStore = (EditText) findViewById(R.id.textedit_store_name1);
		editStore.setHint(oldItem.getStore());
		editStore.setHintTextColor(Color.GRAY);
		String store = editStore.getText().toString();
		
		EditText editDate = (EditText) findViewById(R.id.textedit_deadline1);
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
            Toast.makeText(this, "enter the item name at least!!",
                    Toast.LENGTH_LONG).show();
        } 
        else {
            MainActivity.db.updateItem(newItem);
            Log.d("items", "data updated");
        }
        
        //finish();
	}

	
}
