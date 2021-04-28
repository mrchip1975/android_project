package it.learnathome.indovinalaparola.utils.game;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import it.learnathome.indovinalaparola.R;
import it.learnathome.indovinalaparola.data.Record;
import it.learnathome.indovinalaparola.data.RemoteRecord;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DownloadRecordService extends IntentService {
     public static final String DOWNLOAD_RECORD_SERVICE_ID = "it.learnathome.DOWNLOAD_ID";
     private static final String URL_SCRIPT = "https://corsipca.altervista.org/indovina_la_parola/download_record.php";
     public DownloadRecordService() {
        super(DOWNLOAD_RECORD_SERVICE_ID);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
         Intent response = new Intent();
         response.setAction(DOWNLOAD_RECORD_SERVICE_ID);
         try{
             URL url = new URL(URL_SCRIPT);
             HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
             connection.connect();
             try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                 String row;
                  while((row=reader.readLine())!=null) {
                     response.putExtra("response",row);
                     sendBroadcast(response);
                 }
             }
         } catch(IOException ex) {
            response.putExtra("response","Error "+ex.getMessage());
             sendBroadcast(response);
         }
      }


}