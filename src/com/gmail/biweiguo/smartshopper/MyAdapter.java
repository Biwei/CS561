package com.gmail.biweiguo.smartshopper;


import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
 
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
 
public class MyAdapter extends ArrayAdapter<Item> {
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<Item> myList;
    private SparseBooleanArray mSelectedItemsIds;
 
    public MyAdapter(Context context, int resourceId,
            List<Item> myList) {
        super(context, resourceId, myList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.myList = myList;
        inflater = LayoutInflater.from(context);
    }

 
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_inner_view, null);
        }

        Item item = getItem(position);
        if (item!= null) {
            // My layout has only one TextView
            TextView itemView = (TextView) view.findViewById(R.id.ItemName);
            if (itemView != null) {
                // do whatever you want with your string and long
                itemView.setText(String.format("%s %d", item.getItemName(), item.getCount()));
            }
         }

        return view;
    }
 
    @Override
    public void remove(Item object) {
        myList.remove(object);
        notifyDataSetChanged();
    }
 
    public List<Item> getMyList() {
        return myList;
    }
 
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }
 
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
 
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }
 
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }
 
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}