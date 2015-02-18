package lri.prototype.cosquare.app.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import lri.prototype.cosquare.app.server.LinkToServerManager;

/**
 * Created by louis on 11/06/2014.
 */
public class LocationBroadcastReceiver extends BroadcastReceiver {

    final static String TAG = "LOCATION_RECEIVER";
    /**
     * @param c The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context c, Intent intent) {
        Log.d(TAG,"onReceive : " + intent.toString());
        final Context context = c;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        locationManager.requestSingleUpdate(criteria, new LocationListener() {
            public void onLocationChanged(Location location) {
                int distance = LocationUtils.distanceFromHomePercent(context,location);
                LinkToServerManager.sendLocationToken(context, distance);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        }, null);




    }
}
