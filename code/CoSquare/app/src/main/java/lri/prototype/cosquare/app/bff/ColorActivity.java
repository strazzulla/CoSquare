package lri.prototype.cosquare.app.bff;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import lri.prototype.cosquare.app.R;
import lri.prototype.cosquare.app.server.LinkToServerManager;

public class ColorActivity extends Activity {

    static final String TAG = "COLOR_ACTIVITY";
    Button send;
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        send = (Button) findViewById(R.id.button_send);
        color = getResources().getColor(R.color.palette_0);
        initLayout();
    }

    public void onClick(View view){
        color = ((ColorDrawable)view.getBackground()).getColor();
        Log.d(TAG, "Color : " + color);
        initLayout();
    }

    public void initLayout(){
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
        send.setBackgroundResource(backgroundDrawable);
    }

    public void send(View view){
        Log.d(TAG, "send");
        LinkToServerManager.sendColorToken(getApplicationContext(), color);
        finish();
    }

}
