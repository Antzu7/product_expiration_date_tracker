package com.example.product_expiration_date_tracker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MyAdapter extends SimpleCursorAdapter {
    Globals globals = Globals.getInstance();
    Context ctx;
    LayoutInflater inflater;
    public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        ctx = context;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        long id = getItemId(position);
        view.setTag(id);

        //TextView row_id = (TextView) view.findViewById(R.id.id);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView interval = (TextView) view.findViewById(R.id.interval);
        LinearLayout row = (LinearLayout) view.findViewById(R.id.row);

        if (!interval.getText().toString().isEmpty() && Integer.parseInt(interval.getText().toString()) <= 0) {
            row.setBackgroundResource(R.drawable.item_background_bad);
        }
        else if (!interval.getText().toString().isEmpty() && Integer.parseInt(interval.getText().toString()) < 3) {
            row.setBackgroundResource(R.drawable.item_background_mid);
        }
        else if (!interval.getText().toString().isEmpty()) {
            row.setBackgroundResource(R.drawable.item_background_good);
        }

        //row_id.setText(String.valueOf(position+1));
        date.setText(globals.getFormatDate(date.getText().toString()));
        interval.setText(interval.getText().toString() + " " + ctx.getString(R.string.days));

        return view;
    }
}
