package it.learnathome.indovinalaparola.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.nio.channels.AsynchronousChannelGroup;

public class AsyncTimer extends AsyncTask<Void,Integer,String> {
    private TextView labelTimer;
    private int minutes, seconds;

    public AsyncTimer(TextView lbl) {
        this.labelTimer = lbl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        minutes = 0;
        seconds = 0;
    }

    @Override
    protected String doInBackground(Void... voids) {

        timer:
        while (!this.isCancelled()) {
            for (seconds = 0; seconds < 60 && !isCancelled(); seconds++) {
                if (isCancelled())
                    break timer;
                SystemClock.sleep(1000);
                publishProgress(minutes, seconds);
            }
            minutes++;
        }
        return String.format("tempo impiegato %d:%d", minutes, seconds);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        this.labelTimer.setText(String.format("%d:%d", values[0], values[1]));
        //
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

}
