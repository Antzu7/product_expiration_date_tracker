package com.example.product_expiration_date_tracker;

import android.util.Log;

public class Globals{
    private static Globals instance;

    // Restrict the constructor from being instantiated
    private Globals(){}

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }

    public String getFormatDate(String date) {

        String[] split = date.split("\\.");

        if (split.length > 1) {
            return split[2] + "-" + split[1] + "-" + split[0];
        }
        else {
            split = date.split("-");
            return split[2] + "." + split[1] + "." + split[0];
        }
    }
}