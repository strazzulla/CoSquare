package lri.prototype.cosquare.app.settings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import lri.prototype.cosquare.app.R;

public class SettingsLocationActivity extends Activity implements View.OnClickListener{

    Button home,work,save;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_location);
        context = getApplicationContext();
        home = (Button) findViewById(R.id.button_at_home);
        work = (Button) findViewById(R.id.button_at_work);
        save = (Button) findViewById(R.id.button_save);
        home.setOnClickListener(this);
        work.setOnClickListener(this);
        save.setOnClickListener(this);
        initContentView();
    }

    private void initContentView() {
        initWorkView();
        initHomeView();
    }

    private boolean initHomeView() {
        if (SettingsUtils.getHome(context) != null) {
            home.setBackgroundResource(R.drawable.btn2);
            home.setTextColor(Color.rgb(196, 255, 196));
            return true;
        }
        return false;
    }
    private boolean initWorkView(){
        if(SettingsUtils.getWork(context)!=null){
            work.setBackgroundResource(R.drawable.btn2);
            work.setTextColor(Color.rgb(196, 255, 196));
            return true;
        }
        return false;
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_at_home:
                SettingsUtils.setHome(getApplicationContext());
                for (int i = 0; i < 4; i++) {
                    try {Thread.sleep(1000);} catch (InterruptedException e) {}
                    if (initHomeView()) {i = 4;}
                }
                if (!initHomeView()) {
                    Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_at_work:
                SettingsUtils.setWork(getApplicationContext());
                for (int i = 0; i < 4; i++) {
                    try {Thread.sleep(1000);} catch (InterruptedException e) {}
                    if (initWorkView()) {i = 4;}
                }
                if (!initWorkView()) {
                    Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_save :
                finish();
                break;
        }
    }

    public void green(View view){
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        view.setBackgroundResource(R.drawable.btn2);
        ((Button)view).setTextColor(Color.rgb(196, 255, 196));
    }

}
