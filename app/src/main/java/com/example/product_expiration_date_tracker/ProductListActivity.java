package com.example.product_expiration_date_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ProductListActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();
    private DBManager dbManager;
    private ListView listView;
    private Button addBtn, delBtn;
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
        delBtn = (Button) findViewById(R.id.del_products_btn);

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
                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);

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
                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);
                Intent add_intent = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivity(add_intent);
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UnsafeIntentLaunch")
            @Override
            public void onClick(View v) {
                dbManager.deleteAll();
                recreate();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("UnsafeIntentLaunch")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_maintain:
                if (String.valueOf(item.getTitle()).equals("EN")) {
                    LanguageHelper.setLocale(ProductListActivity.this, "ru");
                }
                else if (String.valueOf(item.getTitle()).equals("RU")) {
                    LanguageHelper.setLocale(ProductListActivity.this, "en");
                }
                else {
                    return true;
                }
                recreate();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}