package lri.prototype.cosquare.app.server;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import lri.prototype.cosquare.app.settings.SettingsUtils;

/**
 * Created by louis on 04/06/2014.
 */
public class LinkToServerManager {

    static final String TAG = "LINK_SERVER";
    static final String SERVER_URL = "https://www.lri.fr/~faucon/gcmserver.php?todo=";




    public static void sendAll(Context c){
        //TODO
    }

    public static void sendRegistrationInfo(final String name,final String login,Context c){

        final boolean[] sent = new boolean[1];

        final Context context = c;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(SERVER_URL+"regid");
                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("regid", SettingsUtils.getRegid(context)));
                    nameValuePairs.add(new BasicNameValuePair("name", name));
                    nameValuePairs.add(new BasicNameValuePair("login", login));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    msg = response.toString();
                    sent[0]=true;

                } catch (Exception e) {
                    msg = e.toString();
                    sent[0]=false;
                }
                 return msg;
            }
            @Override
            protected void onPostExecute(String msg) {
                Log.d(TAG,msg);
            }
        }.execute(null, null, null);

        for(int i = 0;i<4;i++){
            try {Thread.sleep(1000);} catch (InterruptedException e) {}
            if(sent[0]){i=4;}
        }
        if(!sent[0]){
            Toast.makeText(c, "No connection", Toast.LENGTH_SHORT).show();
        }

    }

    public static void sendLocationToken(Context c, final int distance){
        final Context context = c;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(SERVER_URL + "locationtoken");

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    Time time = new Time();
                    time.setToNow();
                    nameValuePairs.add(new BasicNameValuePair("to", SettingsUtils.getBffid(context)));
                    nameValuePairs.add(new BasicNameValuePair("from", SettingsUtils.getRegid(context)));
                    nameValuePairs.add(new BasicNameValuePair("time", ""+time.toMillis(false)));
                    nameValuePairs.add(new BasicNameValuePair("message", ""+ distance));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    msg = response.toString();

                } catch (Exception e) {
                    msg = e.toString();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(TAG,msg);
            }
        }.execute(null, null, null);

    }

    public static void sendColorToken(Context c, final int color){

        final boolean[] sent = new boolean[1];

        Log.d(TAG,"sendColorToken");
        final Context context = c;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(SERVER_URL +"colortoken");

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    Time time = new Time();
                    time.setToNow();
                    Log.d(TAG,SettingsUtils.getRegid(context));
                    nameValuePairs.add(new BasicNameValuePair("to", SettingsUtils.getBffid(context)));
                    nameValuePairs.add(new BasicNameValuePair("from", SettingsUtils.getRegid(context)));
                    nameValuePairs.add(new BasicNameValuePair("time", ""+time.toMillis(false)));
                    nameValuePairs.add(new BasicNameValuePair("message", ""+ color));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    msg = response.toString();
                    sent[0]=true;

                } catch (Exception e) {
                    msg = e.toString();
                    sent[0]=false;
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(TAG,msg);
            }
        }.execute(null, null, null);

        for(int i = 0;i<4;i++){
            try {Thread.sleep(1000);} catch (InterruptedException e) {}
            if(sent[0]){
                i=4;
            }
        }
        if(!sent[0]){
            Toast.makeText(c, "No connection", Toast.LENGTH_SHORT).show();
        }
    }

    public static void sendClick(Context c){final boolean[] sent = new boolean[1];

        Log.d(TAG,"sendClick");
        final Context context = c;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.lri.fr/~faucon/gcmserver.php?todo=click");

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    Time time = new Time();
                    time.setToNow();
                    nameValuePairs.add(new BasicNameValuePair("from", SettingsUtils.getRegid(context)));
                    nameValuePairs.add(new BasicNameValuePair("time", ""+time.toMillis(false)));
                    nameValuePairs.add(new BasicNameValuePair("message", "coucou"));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);
                    msg = response.toString();
                    sent[0]=true;

                } catch (Exception e) {
                    msg = e.toString();
                    sent[0]=false;
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(TAG,msg);
            }
        }.execute(null, null, null);

        for(int i = 0;i<4;i++){
            try {Thread.sleep(1000);} catch (InterruptedException e) {}
            if(sent[0]){
                i=4;
            }
        }
        if(!sent[0]){
            Toast.makeText(c, "No connection", Toast.LENGTH_SHORT).show();
        }
    }

}
