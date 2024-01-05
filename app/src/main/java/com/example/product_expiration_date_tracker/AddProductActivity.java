package com.example.product_expiration_date_tracker;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddProductActivity extends Activity implements OnClickListener {

    Globals globals = Globals.getInstance();
    private Button addBtn;
    private ImageButton dateBtn;
    private EditText nameEditText;
    private EditText dateEditText;

    private DBManager dbManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Добавить");

        setContentView(R.layout.activity_add_record);

        nameEditText = (EditText) findViewById(R.id.name_edittext);
        dateEditText = (EditText) findViewById(R.id.date_edittext);

        addBtn = (Button) findViewById(R.id.add_record);
        dateBtn = (ImageButton) findViewById(R.id.date_btn);

        dbManager = new DBManager(this);
        dbManager.open();
        addBtn.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:
                Boolean nameCheck = true, dateCheck = true;

                String sn = nameEditText.getText().toString().trim();
                if (TextUtils.isEmpty(sn)) {
                    nameEditText.setHint("Введите название");
                    nameEditText.setHintTextColor(Color.RED);
                    nameCheck = false;
                }

                String sd = dateEditText.getText().toString().trim();
                if (TextUtils.isEmpty(sd)) {
                    dateEditText.setHint("Выберите дату истечения срока годности");
                    dateEditText.setHintTextColor(Color.RED);
                    dateCheck = false;
                }

                if (nameCheck && dateCheck) {
                    final String name = nameEditText.getText().toString();
                    final String date = dateEditText.getText().toString();

                    dbManager.insert(name, globals.getFormatDate(date));

                    Intent main = new Intent(AddProductActivity.this, ProductListActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(main);
                }

                break;

            case R.id.date_btn:
                openDatePicker();
                break;
        }
    }

    private void openDatePicker() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(AddProductActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                dateEditText.setText(d + "." + m + "." + year);
            }
        }, year, month, day);

        dpd.show();
    }
}
