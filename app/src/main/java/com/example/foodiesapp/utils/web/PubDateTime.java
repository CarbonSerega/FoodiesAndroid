package com.example.foodiesapp.utils.web;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.foodiesapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class PubDateTime {
    public static String getParsedPubTime(Date date, Context context) {
        long diff = Math.abs(date.getTime() - new Date().getTime());

        long seconds = (int)(diff/ 1000);
        if(seconds < 60) return " " +context.getString(R.string.seconds_ago);

        long minutes = (int)(diff / (60 * 1000));
        if(minutes < 60) return minutes + " " + context.getString(R.string.minutes_ago);

        long hours = (int)(diff / (60 * 60 * 1000));
        if(hours < 24) return hours + " " + context.getString(R.string.hours_ago);

        if(hours / 24 < 5) return (int)(hours / 24) + " " + context.getString(R.string.days_ago);;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter
                = new SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(date);
    }
}
