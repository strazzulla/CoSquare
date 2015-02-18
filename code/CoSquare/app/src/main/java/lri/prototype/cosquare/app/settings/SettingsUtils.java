package lri.prototype.cosquare.app.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by louis on 03/06/2014.
 */
public class SettingsUtils {

    static final String PREFERENCES = "lri.prototype.cosquare.app.PREFERENCES";
    static final String PREF_NAME = "PREF_NAME";
    static final String PREF_LOGIN = "PREF_LOGIN";
    static final String PREF_REGID = "PREF_REGID";
    static final String PREF_BFFID = "PREF_BFFID";
    static final String PREF_HOME_LON = "PREF_HOME_LON";
    static final String PREF_HOME_LAT = "PREF_HOME_LAT";
    static final String PREF_WORK_LON = "PREF_WORK_LON";
    static final String PREF_WORK_LAT = "PREF_WORK_LAT";

    public static boolean settingsCompleted(Context context){
        if(getName(context).isEmpty())return false;
        if(getLogin(context).isEmpty())return false;
        if(getRegid(context).isEmpty())return false;
        if(getBffid(context).isEmpty())return false;
        if(getHome(context)==null)return false;
        if(getWork(context)==null)return false;
        return true;
    }

    //GETTERS
    public static String getName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_NAME,"");
    }
    public static String getLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_LOGIN,"");
    }
    public static String getRegid(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_REGID,"");
    }
    public static String getBffid(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(PREF_BFFID,"");
    }
    public static Location getHome(Context context){
        Location location = new Location("home");
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        location.setLatitude(sharedPreferences.getFloat(PREF_HOME_LAT,0f));
        location.setLongitude(sharedPreferences.getFloat(PREF_HOME_LON,0f));
        if(location.getLongitude()==0f && location.getLatitude()==0f)location = null;
        return location;
    }
    public static Location getWork(Context context){
        Location location = new Location("work");
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        location.setLatitude(sharedPreferences.getFloat(PREF_WORK_LAT,0f));
        location.setLongitude(sharedPreferences.getFloat(PREF_WORK_LON,0f));
        if(location.getLongitude()==0f && location.getLatitude()==0f)location = null;
        return location;
    }

    //SETTERS
    public static void setUserInfo(String name,String login,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_NAME,name);
        editor.putString(PREF_LOGIN,login);
        editor.commit();
    }
    public static void setRegid(String regid,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_REGID,regid);
        editor.commit();
    }
    public static void setBffid(String bffid,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_BFFID,bffid);
        editor.commit();
    }
    public static void setHome(Context c){
        final Context context = c;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.requestSingleUpdate(criteria, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(PREF_HOME_LAT,(float)location.getLatitude());
                editor.putFloat(PREF_HOME_LON,(float)location.getLongitude());
                editor.commit();
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        }, null);
    }
    public static void setWork(Context c){
        final Context context = c;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.requestSingleUpdate(criteria, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(PREF_WORK_LAT,(float)location.getLatitude());
                editor.putFloat(PREF_WORK_LON,(float)location.getLongitude());
                editor.commit();
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        }, null);
    }

}
