package lri.prototype.cosquare.app.sqldatabase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by louis on 30/06/2014.
 */
public class DatabaseUtils {

    public static void addColorTokenReceived(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        new ColorTokenReceived(context).add(extras.getString("time"),Integer.parseInt(extras.getString("message")));
    }

    public static void addColorTokenSent(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        new ColorTokenSent(context).add(extras.getString("time"),Integer.parseInt(extras.getString("message")));
    }

    public static void addLocationTokenReceived(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        new LocationTokenReceived(context).add(extras.getString("time"),Integer.parseInt(extras.getString("message")));
    }

    public static void addLocationTokenSent(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        new LocationTokenSent(context).add(extras.getString("time"),Integer.parseInt(extras.getString("message")));
    }

    public static void suppressPendingMessage(Context context, Intent intent) {
        //TODO
    }

    public static void clearAll(Context context){/*
        new LocationTokenSent(context).clearDatabase();
        new LocationTokenReceived(context).clearDatabase();
        new ColorTokenSent(context).clearDatabase();
        new ColorTokenReceived(context).clearDatabase();*/
    }



}
