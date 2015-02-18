package lri.prototype.cosquare.app.location;

import android.content.Context;
import android.location.Location;

import lri.prototype.cosquare.app.settings.SettingsUtils;

/**
 * Created by louis on 15/05/2014.
 */
public class LocationUtils {

    public static int MINUTE = 60*1000;
    public static int TIME_BETWEEN_UPDATES = 20*MINUTE;

    static public int distanceFromHomePercent(Context context, Location location){
        return (int) Math.min(100,((100*distanceFromHome(context, location)) / (2*(distanceFromHomeToWork(context)))));
    }
    static public float distanceFromHomeToWork(Context context){
        return Math.max(1000,distanceFromHome(context,SettingsUtils.getWork(context)));
    }
    static public float distanceFromHome(Context context,Location location){
        if(location!=null&&SettingsUtils.getHome(context)!=null)return location.distanceTo(SettingsUtils.getHome(context));
        return 42;
    }

}
