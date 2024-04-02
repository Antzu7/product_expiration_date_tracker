package com.example.product_expiration_date_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ProductListActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();
    private DBManager dbManager;
    private ListView listView;
    private Button addBtn;
    private MyAdapter adapter;
    final String[] from = new String[] { DBHelper._ID, DBHelper.NAME, DBHelper.EXPIRATION_DATE, "interval" };
    final int[] to = new int[] { R.id.id, R.id.name, R.id.date, R.id.interval };
    public static final String TAG_MY_WORK = "mywork";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        addBtn = (Button) findViewById(R.id.add_product_btn);

        adapter = new MyAdapter(this, R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        PeriodicWorkRequest.Builder checkExpirationAndNotify =
                new PeriodicWorkRequest.Builder(MyWorker.class, 1, TimeUnit.MINUTES);
        PeriodicWorkRequest request = checkExpirationAndNotify.build();
        WorkManager.getInstance().enqueueUniquePeriodicWork(TAG_MY_WORK, ExistingPeriodicWorkPolicy.KEEP, request);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView nameTextView = (TextView) view.findViewById(R.id.name);
                TextView dateTextView = (TextView) view.findViewById(R.id.date);

                String id = idTextView.getText().toString();
                String name = nameTextView.getText().toString();
                String date = dateTextView.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), ModifyProductActivity.class);
                modify_intent.putExtra("name", name);
                modify_intent.putExtra("date", date);
                modify_intent.putExtra("id", id);

                startActivity(modify_intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent add_intent = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivity(add_intent);
            }
        });
    }
}