package lri.prototype.cosquare.app.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import lri.prototype.cosquare.app.settings.SettingsUtils;

/**
 * Created by louis on 04/06/2014.
 */
public class GcmManager {

    static final String TAG = "GCM_MANAGER";

    String SENDER_ID = "403235248512";
    GoogleCloudMessaging gcm;
    String regid;
    Context context;

    public GcmManager(Context context){
        this.context = context;
    }

    public void register() {
        gcm = GoogleCloudMessaging.getInstance(context);
        regid = SettingsUtils.getRegid(context);
        if (regid.isEmpty()) {
            registerInBackground();
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    SettingsUtils.setRegid(regid,context);

                } catch (IOException ex) {
                    msg = "Error : " + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //TODO
                Log.d(TAG,msg);
            }
        }.execute(null, null, null);
    }



}
