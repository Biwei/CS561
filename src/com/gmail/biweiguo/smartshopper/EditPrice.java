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

public class EditPrice extends Activity {
	
	protected static DbHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_price);
		Bundle extras = getIntent().getExtras();
	    final int id = extras.getInt("selected");
		
		db = DbHelper.getInstance(this);
		
		Button saveButton = (Button)findViewById(R.id.button_save2);
		Button cancelButton = (Button)findViewById(R.id.button_cancel2);
	     
        saveButton.setOnClickListener(new OnClickListener() {
           
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Intent intent=new Intent();
                editPrice(id);
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
	
	public void editPrice(int id) {
		
		Item newItem = new Item();
		newItem = db.getPurchase(id);
		
		EditText editPrice = (EditText) findViewById(R.id.textedit_item_price);
		float price = Float.valueOf(editPrice.getText().toString());
		
		newItem.setPrice(price);
		MainActivity.db.updatePurchase(newItem);
        Log.d("bought", "data updated");
        
        //finish();
	}

	
}
