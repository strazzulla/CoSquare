package lri.prototype.cosquare.app.settings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import lri.prototype.cosquare.app.R;
import lri.prototype.cosquare.app.server.LinkToServerManager;

public class SettingsActivity extends Activity implements View.OnClickListener {

    static final String TAG = "SETTINGS_ACTIVITY";

    EditText editTextName,editTextLogin;
    String name,login;
    Button buttonSave;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = getApplicationContext();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(this);

        initContentView();
    }

    private boolean initContentView(){
        boolean b = false;
        if(!SettingsUtils.getName(context).isEmpty()){
            editTextName.setBackgroundResource(R.drawable.btn2);
            editTextName.setHintTextColor(Color.rgb(196,255,196));
            editTextName.setText(SettingsUtils.getName(context));
            b=true;
        }
        if(!SettingsUtils.getLogin(context).isEmpty()){
            editTextLogin.setBackgroundResource(R.drawable.btn2);
            editTextLogin.setHintTextColor(Color.rgb(196,255,196));
            editTextLogin.setText(SettingsUtils.getLogin(context));
            b=true;
        }
        return b;
    }


    public void onClick(View view) {

        name = editTextName.getText().toString();
        login = editTextLogin.getText().toString();

        if(name.equals("")|| login.equals("")) {
            //TODO show message
            Log.d(TAG,"show message");
        } else {
            LinkToServerManager.sendRegistrationInfo(name, login, getApplicationContext());
            for (int i = 0; i < 4; i++) {
                try {Thread.sleep(1000);} catch (InterruptedException e) {}
                if (initContentView()) {i = 4;}
            }
            if (!initContentView()) {
                Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(SettingsUtils.settingsCompleted(getApplicationContext())){
            //TODO maybe
        }

        //TODO show explanation message


        finish();

    }


}
