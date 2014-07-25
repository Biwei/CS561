package com.gmail.biweiguo.smartshopper;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItem extends Activity {
	
	protected static DbHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		db = DbHelper.getInstance(this);
		
		Button addButton = (Button)findViewById(R.id.button_add);
	     
        addButton.setOnClickListener(new OnClickListener() {
           
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Intent intent=new Intent();
                addItem(v);
                //setResult(RESULT_OK, intent);
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
		String date = editDate.getText().toString();
		
		newItem.setItemName(name);
		//newItem.setCount(count);
		newItem.setStore(store);
		newItem.setDate(date);
		
        if (name.equalsIgnoreCase("")) {
            Toast.makeText(this, "enter the item name at least!!",
                    Toast.LENGTH_LONG).show();
        } else {
            MainActivity.db.addItem(newItem);
            Log.d("my list", "data added");
        }
        
        //finish();
		
	}
}
