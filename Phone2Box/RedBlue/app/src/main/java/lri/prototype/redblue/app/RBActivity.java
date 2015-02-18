package lri.prototype.redblue.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RBActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    static public final String DEVICE_ID = "51ff6d065067545758270187";
    static public final String ACCESS_TOKEN = "4f96fb59e0f1e601e49d86354372752292152d1f";
    static String color;
    Button send;
    SeekBar seekbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rb);
        send = (Button) findViewById(R.id.button_send);
        send.setBackgroundColor(Color.GRAY);
        seekbar = (SeekBar) findViewById(R.id.seekBarSparkcore);
        seekbar.setMax(255);
        seekbar.setOnSeekBarChangeListener(this);
    }

    public void onClick(View view){
        int i = ((ColorDrawable)view.getBackground()).getColor();
        Log.d("COLOR",""+i);
        switch(i){
            case -65281: color = "magenta"; break;
            case -16776961: color = "blue"; break;
            case -16711681: color = "cyan"; break;
            case -16711936: color = "green"; break;
            case -65536: color = "red"; break;
            case -256: color = "yellow"; break;
            case -16777216: color = "off"; break;
            case -30686: color = "orange"; break;
        }
        Log.d("COLOR", color);
        send.setBackgroundColor(i);
        send.setText(color);
    }

    public void send(View view){
        send.setBackgroundColor(Color.GRAY);
        new SendColor().execute();
    }
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }
    public void onStartTrackingTouch(SeekBar seekBar) {}
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d("SEEKBAR",String.valueOf(seekbar.getProgress()));
        new SendDistance().execute();
    }

    private class SendColor extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://api.spark.io/v1/devices/" + DEVICE_ID + "/color");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("access_token", ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("args", color));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(httppost);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return "message";
        }
        protected void onPostExecute(String result) {
            Log.d("REDBLUE",result);
        }
    }

    private class SendDistance extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://api.spark.io/v1/devices/" + DEVICE_ID + "/distance");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("access_token", ACCESS_TOKEN));
                nameValuePairs.add(new BasicNameValuePair("args", String.valueOf(seekbar.getProgress())));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(httppost);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return "message";
        }
        protected void onPostExecute(String result) {
            Log.d("REDBLUE",result);
        }
    }

}
