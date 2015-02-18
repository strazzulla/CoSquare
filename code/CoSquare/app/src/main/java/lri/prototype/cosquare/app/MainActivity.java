package lri.prototype.cosquare.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import lri.prototype.cosquare.app.bff.BandManager;
import lri.prototype.cosquare.app.bff.ColorActivity;
import lri.prototype.cosquare.app.gcm.GcmManager;
import lri.prototype.cosquare.app.location.LocationBroadcastReceiver;
import lri.prototype.cosquare.app.location.LocationUtils;
import lri.prototype.cosquare.app.server.LinkToServerManager;
import lri.prototype.cosquare.app.settings.SettingsActivity;
import lri.prototype.cosquare.app.settings.SettingsLocationActivity;
import lri.prototype.cosquare.app.settings.SettingsUtils;
import lri.prototype.cosquare.app.sqldatabase.ColorTokenReceived;
import lri.prototype.cosquare.app.sqldatabase.DatabaseUtils;


public class MainActivity extends Activity {

    static final String TAG = "MAIN_ACTIVITY";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static GcmManager gcmManager;
    Context context;
    ImageView band_received,band_sent;
    Button colorButton,locationButton,settingsButton;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"onReceive");
            initLayout();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        //Check if settings have been completed
        if(!SettingsUtils.settingsCompleted(context)){
            //TODO change layout.
        }

        //Check if gcm is active
        if(checkPlayServices()){
            gcmManager = new GcmManager(context);
            gcmManager.register();
        }

        //Activation of an AlarmManager for repeating locations updates.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LocationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, LocationUtils.TIME_BETWEEN_UPDATES, LocationUtils.TIME_BETWEEN_UPDATES, pendingIntent);

        band_received = (ImageView) findViewById(R.id.band_received);
        band_sent = (ImageView) findViewById(R.id.band_sent);
        colorButton = (Button) findViewById(R.id.buttonColor);
        locationButton = (Button) findViewById(R.id.buttonLocation);
        settingsButton = (Button) findViewById(R.id.buttonSettings);

        DatabaseUtils.clearAll(context);

    }

    public void onResume(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MAIN");
        registerReceiver(receiver, filter);
        super.onResume();
        initLayout();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    public void initLayout(){

        int color = ColorTokenReceived.getLastColor(context);
        int backgroundDrawable = R.drawable.btn0;
        if(color == getResources().getColor(R.color.palette_0))backgroundDrawable = R.drawable.btn0;
        if(color == getResources().getColor(R.color.palette_1))backgroundDrawable = R.drawable.btn1;
        if(color == getResources().getColor(R.color.palette_2))backgroundDrawable = R.drawable.btn2;
        if(color == getResources().getColor(R.color.palette_3))backgroundDrawable = R.drawable.btn3;
        if(color == getResources().getColor(R.color.palette_4))backgroundDrawable = R.drawable.btn4;
        if(color == getResources().getColor(R.color.palette_5))backgroundDrawable = R.drawable.btn5;
        if(color == getResources().getColor(R.color.palette_6))backgroundDrawable = R.drawable.btn6;
        if(color == getResources().getColor(R.color.palette_7))backgroundDrawable = R.drawable.btn7;
        if(color == getResources().getColor(R.color.palette_8))backgroundDrawable = R.drawable.btn8;
        if(color == getResources().getColor(R.color.palette_9))backgroundDrawable = R.drawable.btn9;

        final int bd = backgroundDrawable;
        runOnUiThread(new Runnable() {
            public void run() {
                colorButton.setBackgroundResource(bd);
                locationButton.setBackgroundResource(bd);
                settingsButton.setBackgroundResource(bd);
                band_received.setImageBitmap(BandManager.getBitmapReceived(context));
                band_sent.setImageBitmap(BandManager.getBitmapSent(context));

            }
        });
    }

    public void buttonLocationClick(View view){
        startActivity(new Intent(this, SettingsLocationActivity.class));
    }

    public void buttonColorClick(View view){
        startActivity(new Intent(this, ColorActivity.class));
    }

    public void buttonSettingsClick(View view){
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void sendLike(View view){
        LinkToServerManager.sendClick(this);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}
