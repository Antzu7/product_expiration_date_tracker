package com.example.product_expiration_date_tracker;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    private Context this_context;
    private DBManager dbManager;
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this_context = context;
    }
    @NonNull
    @Override
    public Result doWork() {
        Integer i = 1;
        dbManager = new DBManager(this_context);
        dbManager.open();
        Cursor cursor = dbManager.getExpired();
        try {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("name"));
                displayNotification("Срок годности продукта истек!", "У продукта " + productName + " истек срок годности", i);
                i++;
            }
        }
        finally {
            cursor.close();
        }

        return Result.success();
    }

    private void displayNotification(String title, String task, Integer id) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("EDT", "EDT", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "EDT")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(id, notification.build());
    }
}
