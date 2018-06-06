package com.debajyotibasak.udacitypopularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        return nf != null && nf.isConnected();
    }

    public static String convertDate(String date, String df1, String df2) {
        SimpleDateFormat df = new SimpleDateFormat(df1, Locale.US);
        Date newDate = null;
        try {
            newDate = df.parse(date);
            df = new SimpleDateFormat(df2, Locale.US);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df.format(newDate);
    }
}
