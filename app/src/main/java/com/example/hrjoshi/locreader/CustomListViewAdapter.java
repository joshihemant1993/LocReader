package com.example.hrjoshi.locreader;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by iGuest on 10/31/2016.
 */

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {
    //public class CustomListViewAdapter extends BaseAdapter{

    Context context;
    //private ArrayList<RowItem> rowList;

    public CustomListViewAdapter(Context context, int resourceId, ArrayList<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class viewHolder {
        ImageView imageView;
        TextView title;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        RowItem rowItem = getItem(position);
        Log.v("DEBUG", rowItem.toString());

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_landmark, parent,false);
            holder = new viewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.list_item_landmark_text);
            holder.imageView = (ImageView) convertView.findViewById(R.id.list_item_landmark_image);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        holder.title.setText(rowItem.getTitle());
        holder.imageView.setImageBitmap(rowItem.getImage());

        return convertView;
    }
}

