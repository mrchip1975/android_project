package it.learnathome.indovinalaparola.utils.login;

import android.app.IntentService;
import android.content.Intent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SignUpService extends IntentService {

    private final static String SIGN_UP_URL = "https://corsipca.altervista.org/indovina_la_parola/signup.php";
    public final static String SIGN_UP_SERVICE_ID = "it.learnathome.SIGN_UP";
    private final static String PAYLOAD_REQUEST = "username=%s&password=%s";

    public SignUpService() {
        super(SIGN_UP_SERVICE_ID);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent response = new Intent();
        response.setAction(SIGN_UP_SERVICE_ID);
        try {
            URL url = new URL(SIGN_UP_URL);
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();
            try(PrintWriter out = new PrintWriter(connection.getOutputStream());) {
                out.println(String.format(PAYLOAD_REQUEST,intent.getStringExtra("username"),intent.getStringExtra("password")));
                out.flush();
                out.close();
                if(connection.getResponseCode()== HttpURLConnection.HTTP_OK) {
                    Scanner in = new Scanner(connection.getInputStream());
                    response.putExtra("response",in.next());
                } else {
                    response.putExtra("response","Error "+connection.getResponseMessage());
                }
            }
        } catch (IOException e) {
           response.putExtra("response","Error "+e.getMessage());
        } finally {
            sendBroadcast(response);
        }
    }


}