package it.learnathome.indovinalaparola.utils.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import it.learnathome.indovinalaparola.R;

public class LoginReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String response;
      switch (intent.getAction()) {
          case SignUpService.SIGN_UP_SERVICE_ID:
                                                response = intent.getStringExtra("response") ;
                                                if(response.startsWith("Error")) {
                                                    Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(context,context.getResources().getString(R.string.welcome_message),Toast.LENGTH_LONG).show();
                                                    SharedPreferences preferences = context.getSharedPreferences("prefs_game",Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putInt("gold",100);
                                                    editor.commit();
                                                }
                                                break;
      }
    }
}