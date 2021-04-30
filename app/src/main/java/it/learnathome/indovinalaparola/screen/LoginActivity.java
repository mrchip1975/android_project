package it.learnathome.indovinalaparola.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.zip.Inflater;

import it.learnathome.indovinalaparola.R;
import it.learnathome.indovinalaparola.utils.login.LoginReceiver;
import it.learnathome.indovinalaparola.utils.login.LoginService;
import it.learnathome.indovinalaparola.utils.login.SignInService;
import it.learnathome.indovinalaparola.utils.login.SignUpService;

public class LoginActivity extends AppCompatActivity {
    private LoginReceiver receiver = new LoginReceiver();
    private int avatarCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerReceiver(receiver,new IntentFilter(SignUpService.SIGN_UP_SERVICE_ID));
        registerReceiver(receiver,new IntentFilter(SignInService.SIGN_IN_SERVICE_ID));
        }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("prefs_game",MODE_PRIVATE);
        if(preferences.contains("username")) {
            EditText field = findViewById(R.id.usernameField);
            field.setText(preferences.getString("username",""));
            field = findViewById(R.id.passwordField);
            field.setText(preferences.getString("password",""));
            Log.d("avatar",preferences.getString("avatar","avat0"));
            int idAvatar = getResources().getIdentifier(preferences.getString("avatar","avat0"),"drawable",getPackageName());
            ((ImageView)findViewById(R.id.myAvatar)).setImageResource(idAvatar);
        }
    }


    private Intent buildIntent(String operation) {
        Intent intent = new Intent(LoginActivity.this, LoginService.class);
        EditText field = findViewById(R.id.usernameField);
        intent.putExtra("username",field.getText().toString());
        field = findViewById(R.id.passwordField);
        intent.putExtra("password",field.getText().toString());
        intent.putExtra("operation",operation);
        return intent;
    }
    public void signUp(View view) {
        buildAvatarChooser();

    }
    public void changeAvatar(View view) {
        avatarCounter++;
        avatarCounter%=7;
        int idAvatar = getResources().getIdentifier("avat"+avatarCounter,"drawable",getPackageName());
        ((ImageView)view).setImageResource(idAvatar);
    }
    public void signIn(View view) {
         startService(buildIntent("login"));
        CheckBox savePrefsChk = findViewById(R.id.saveLoginChk);
        if(savePrefsChk.isChecked()) {
            SharedPreferences preferences = getSharedPreferences("prefs_game",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username",((EditText)findViewById(R.id.usernameField)).getText().toString());
            editor.putString("password",((EditText)findViewById(R.id.passwordField)).getText().toString());
            editor.commit();
        }
    }
    private void buildAvatarChooser() {
        View view = LayoutInflater.from(this).inflate(R.layout.avatar_chooser,null);
        view.findViewById(R.id.avatarBtn).setOnClickListener(v->changeAvatar(v));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(android.R.string.ok),((dialog, which) -> {
            getSharedPreferences("prefs_game",MODE_PRIVATE).edit().putString("avatar","avat"+String.valueOf(avatarCounter%7)).commit();
            dialog.dismiss();
            startService(buildIntent("registration"));
        }));
        builder.create().show();
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }
}