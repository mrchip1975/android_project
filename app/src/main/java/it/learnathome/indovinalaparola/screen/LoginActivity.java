package it.learnathome.indovinalaparola.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import it.learnathome.indovinalaparola.R;
import it.learnathome.indovinalaparola.utils.login.SignUpService;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void signUp(View view) {
        Intent signupIntent = new Intent(LoginActivity.this, SignUpService.class);
        EditText field = findViewById(R.id.usernameField);
        signupIntent.putExtra("username",field.getText().toString());
        field = findViewById(R.id.passwordField);
        signupIntent.putExtra("password",field.getText().toString());
        startService(signupIntent);
    }
}