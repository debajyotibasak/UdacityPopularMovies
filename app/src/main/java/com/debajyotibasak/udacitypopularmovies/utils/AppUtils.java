package com.debajyotibasak.udacitypopularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.debajyotibasak.udacitypopularmovies.MoviesApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppUtils {

    public static boolean isNetworkAvailable() {
        ConnectivityManager cn = (ConnectivityManager) MoviesApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public static void setSnackBar(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
        View view1 = snackbar.getView();
        TextView txv = view1.findViewById(android.support.design.R.id.snackbar_text);
        txv.setGravity(Gravity.CENTER_HORIZONTAL);
    }
}
