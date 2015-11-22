package com.creative.womensafety.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.creative.womenssafety.R;

import java.util.List;

/**
 * Created by Syed Ikhtiar Ahmed on 11/22/2015.
 */
public class Drawer_list_adapter extends ArrayAdapter<Drawer_item> {
    private Context context;
    private List<Drawer_item> object;
    private String title_name;


    public Drawer_list_adapter(Context context, int resource, List object) {
        super(context, resource, object);
        this.object = object;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View row, ViewGroup parent) {
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.drawer_list_item, parent, false);
        }

        ImageView icon = (ImageView) row.findViewById(R.id.drawer_item_icon);
        icon.setImageResource(object.get(position).getImage());

        TextView text = (TextView) row.findViewById(R.id.drawer_item_text);
        text.setText(object.get(position).getText());

        return row;
    }
}
