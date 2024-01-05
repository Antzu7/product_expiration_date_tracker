package com.example.product_expiration_date_tracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

public class ModifyProductActivity extends Activity implements OnClickListener {

    Globals globals = Globals.getInstance();
    private EditText nameText, dateText;
    private ImageButton dateBtn;
    private Button updateBtn, deleteBtn;

    private long _id;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Редактировать");

        setContentView(R.layout.activity_modify_record);

        dbManager = new DBManager(this);
        dbManager.open();

        nameText = (EditText) findViewById(R.id.name_edittext);
        dateText = (EditText) findViewById(R.id.date_edittext);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);
        dateBtn = (ImageButton) findViewById(R.id.date_btn);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");

        _id = Long.parseLong(id);

        nameText.setText(name);
        dateText.setText(date);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                Boolean nameCheck = true, dateCheck = true;

                String sn = nameText.getText().toString().trim();
                if (TextUtils.isEmpty(sn)) {
                    nameText.setHint("Введите название");
                    nameText.setHintTextColor(Color.RED);
                    nameCheck = false;
                }

                String sd = dateText.getText().toString().trim();
                if (TextUtils.isEmpty(sd)) {
                    dateText.setHint("Выберите дату истечения срока годности");
                    dateText.setHintTextColor(Color.RED);
                    dateCheck = false;
                }

                if (nameCheck && dateCheck) {
                    String name = nameText.getText().toString();
                    String date = dateText.getText().toString();
                    dbManager.update(_id, name, globals.getFormatDate(date));
                    this.returnHome();
                }

                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;

            case R.id.date_btn:
                openDatePicker();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), ProductListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

    private void openDatePicker() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(ModifyProductActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String m = month + 1 + "";
                String d = dayOfMonth + "";
                if (m.length() < 2) {
                    m = "0" + m;
                }
                if (d.length() < 2) {
                    d = "0" + d;
                }
                dateText.setText(d + "." + m + "." + year);
            }
        }, year, month, day);

        dpd.show();
    }
}