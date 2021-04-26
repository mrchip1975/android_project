package it.learnathome.indovinalaparola.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import it.learnathome.indovinalaparola.R;
import it.learnathome.indovinalaparola.utils.login.LoginReceiver;
import it.learnathome.indovinalaparola.utils.login.LoginService;
import it.learnathome.indovinalaparola.utils.login.SignInService;
import it.learnathome.indovinalaparola.utils.login.SignUpService;

public class LoginActivity extends AppCompatActivity {
    private LoginReceiver receiver = new LoginReceiver();
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
        startService(buildIntent("registration"));
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

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }
}