package com.example.hrjoshi.locreader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by iGuest on 10/31/2016.
 */

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {
    Context context;

    public CustomListViewAdapter(Context context, int resourceId, List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class viewHolder {
        ImageView imageView;
        //TextView title;
        TextView txtDesc;

        public View getView(int position, View convertView, ViewGroup parent) {
            viewHolder holder = null;
            RowItem rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.list_item_landmark,null);
                holder = new viewHolder();
                holder.txtDesc = (TextView) convertView.findViewById(R.id.list_item_landmark_text);
            //    holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
                holder.imageView = (ImageView) convertView.findViewById(R.id.list_item_landmark_image);
                convertView.setTag(holder);
            }else{
                holder = (viewHolder)convertView.getTag();
            }
            holder.txtDesc.setText(rowItem.getDesc());
            holder.imageView.setImageResource(rowItem.getImageId());

            return convertView;
        }
    }
}
