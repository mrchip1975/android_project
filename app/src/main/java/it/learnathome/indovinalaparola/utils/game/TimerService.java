package it.learnathome.indovinalaparola.utils.game;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class TimerService extends IntentService {

    public static final String ID_TIMER = "it.learathome.TIMER_SERVICE";
    public TimerService() {
        super(ID_TIMER);
    }
    public static boolean running = true;
    @Override
    protected void onHandleIntent(Intent intent) {
        int minutes =0,seconds=0;
        Intent time = new Intent();
        time.setAction(ID_TIMER);
        while(running) {
            for(seconds=0;seconds<60 && running ;seconds++){
                time.putExtra("minutes",minutes);
                time.putExtra("seconds",seconds);
                sendBroadcast(time);
                SystemClock.sleep(1000);
            }
            minutes++;
        }
    }


}