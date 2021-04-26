package it.learnathome.indovinalaparola.utils.game;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SaveRecordReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(SaveRecordService.SAVE_SERVICE_ID.equals(intent.getAction())){
            Toast.makeText(context,intent.getStringExtra("response"),Toast.LENGTH_LONG).show();
        }
    }
}