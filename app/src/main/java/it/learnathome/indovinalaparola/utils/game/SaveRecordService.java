package it.learnathome.indovinalaparola.utils.game;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import it.learnathome.indovinalaparola.data.Record;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SaveRecordService extends IntentService {

    public final static String SAVE_SERVICE_ID = "it.learnathome.SAVE_ID";
    private final static String SAVE_SCRIPT_URL = "https://corsipca.altervista.org/indovina_la_parola/save_record.php";
    private final static String SAVE_PAYLOAD = "player=%s&word=%s&elapsed_time=%s&attempts=%d";

    public SaveRecordService() {
        super(SAVE_SERVICE_ID);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Record r = (Record) intent.getSerializableExtra("record");
        Log.d("Record",r.getName());
        Intent response = new Intent();
        response.setAction(SAVE_SERVICE_ID);
        try {
            URL url = new URL(SAVE_SCRIPT_URL);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();
            try(PrintWriter out = new PrintWriter(connection.getOutputStream())) {
                out.println(String.format(SAVE_PAYLOAD,r.getName(),r.getWord(),r.getTime(),r.getAttempt()));
                out.flush();
                if(connection.getResponseCode()== HttpURLConnection.HTTP_OK) {
                    try(Scanner in = new Scanner(connection.getInputStream())) {
                        response.putExtra("response",in.next());
                    }
                } else {
                    response.putExtra("response","Error "+connection.getResponseMessage());
                }
            }
        } catch(IOException ex) {
            response.putExtra("response","Error "+ex.getMessage());
        } finally {
            sendBroadcast(response);
        }
    }


}