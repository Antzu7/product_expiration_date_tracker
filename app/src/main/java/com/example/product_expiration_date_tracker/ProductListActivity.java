package com.example.product_expiration_date_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
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

public class ProductListActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();
    private DBManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    final String[] from = new String[] { DBHelper._ID, DBHelper.NAME, DBHelper.EXPIRATION_DATE };
    final int[] to = new int[] { R.id.id, R.id.name, R.id.date };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

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
                modify_intent.putExtra("date", globals.getFormatDate(date));
                modify_intent.putExtra("id", id);

                startActivity(modify_intent);
            }

//        add_product_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Cursor cursor = database.rawQuery("insert into products (\"productName\", \"productDescription\", \"productPrice\", \"categoryId\") values ('Add', 'adaada', 111, 1);", null);
//                cursor.moveToFirst();
//
//                cursor.close();
//            }
//        });
//
//        clothesCategoryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ArrayList<HashMap<String, String>> products = new ArrayList<>();
//                HashMap<String, String> product;
//
//                Cursor cursor = database.rawQuery("SELECT * FROM products WHERE categoryId = 2", null);
//                cursor.moveToFirst();
//                while (!cursor.isAfterLast()) {
//                    product = new HashMap<>();
//                    product.put("productName", cursor.getString(1));
//                    product.put("productInfo", "price: " + cursor.getString(3) + "\ndescription: " + cursor.getString((2)));
//                    products.add(product);
//                    cursor.moveToNext();
//                }
//                cursor.close();
//
//                SimpleAdapter adapter = new SimpleAdapter(
//                        getApplicationContext(),
//                        products,
//                        android.R.layout.simple_list_item_2,
//                        new String[]{"productName", "productInfo"},
//                        new int[]{android.R.id.text1, android.R.id.text2}
//                );
//
//                listView.setAdapter(adapter);
//            }
//        });
//
//        toysCategoryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ArrayList<HashMap<String, String>> products = new ArrayList<>();
//                HashMap<String, String> product;
//
//                Cursor cursor = database.rawQuery("SELECT * FROM products WHERE categoryId = 3", null);
//                cursor.moveToFirst();
//                while (!cursor.isAfterLast()) {
//                    product = new HashMap<>();
//                    product.put("productName", cursor.getString(1));
//                    product.put("productInfo", "price: " + cursor.getString(3) + "\ndescription: " + cursor.getString((2)));
//                    products.add(product);
//                    cursor.moveToNext();
//                }
//                cursor.close();
//
//                SimpleAdapter adapter = new SimpleAdapter(
//                        getApplicationContext(),
//                        products,
//                        android.R.layout.simple_list_item_2,
//                        new String[]{"productName", "productInfo"},
//                        new int[]{android.R.id.text1, android.R.id.text2}
//                );
//
//                listView.setAdapter(adapter);
//            }
        });
    }
}