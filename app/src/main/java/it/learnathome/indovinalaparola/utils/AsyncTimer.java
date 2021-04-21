package it.learnathome.indovinalaparola.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.nio.channels.AsynchronousChannelGroup;

public class AsyncTimer extends AsyncTask<Void,Integer,String> {
    private TextView labelTimer;
    private int minutes,seconds;
    private Bridge saver;

    public AsyncTimer(TextView lbl, Context ctx) {
        this.labelTimer = lbl;
        if(ctx instanceof  Bridge)
            this.saver = (Bridge)ctx;
        else throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        minutes =0;
        seconds =0;
    }

    @Override
    protected String doInBackground(Void... voids) {

        timer: while(!this.isCancelled()) {
            for (; seconds < 60 && !isCancelled(); seconds++) {
                publishProgress(minutes, seconds);
                if(isCancelled())
                    break timer;
                SystemClock.sleep(1000);
            }
            Log.d("asyncTimer",String.valueOf(this.isCancelled()));
            minutes++;
        }
        return String.format("tempo impiegato %d:%d",minutes,seconds);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        this.labelTimer.setText(String.format("%d:%d",values[0],values[1]));
       //
    }

    @Override
    protected void onPostExecute(String aString) {
        saver.saveRecord(minutes,seconds);
      //Log.d("timer_async",aString);
    }
    public interface Bridge {
        void saveRecord(int minutes,int seconds);
    }
}
