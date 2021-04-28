package it.learnathome.indovinalaparola.utils.game;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import it.learnathome.indovinalaparola.data.Record;
import it.learnathome.indovinalaparola.screen.fragments.RecordFragment;

public class SaveRecordReceiver extends BroadcastReceiver {
    private Gson parser = new Gson();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(SaveRecordService.SAVE_SERVICE_ID.equals(intent.getAction())){
            Toast.makeText(context,intent.getStringExtra("response"),Toast.LENGTH_LONG).show();
        } else if(DownloadRecordService.DOWNLOAD_RECORD_SERVICE_ID.equals(intent.getAction())){
            if(intent.getStringExtra("response").startsWith("Error ")){
                Toast.makeText(context,intent.getStringExtra("reponse"),Toast.LENGTH_LONG).show();
            } else {
                     Record  record = parser.fromJson(intent.getStringExtra("response"), Record.class);
                     RecordFragment.recordsList.add(record);
                     RecordFragment.adapter.notifyDataSetChanged();
                //updater.addRecord((Record)intent.getSerializableExtra("response"));
            }
        }
    }
}