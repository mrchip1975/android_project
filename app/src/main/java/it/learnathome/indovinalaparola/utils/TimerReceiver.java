package it.learnathome.indovinalaparola.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import it.learnathome.indovinalaparola.MainActivity;
import it.learnathome.indovinalaparola.R;

public class TimerReceiver extends BroadcastReceiver {
    private final static String TIMER_TEMPLATE = "%d:%d";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!TimerService.ID_TIMER.equals(intent.getAction())|| !(context instanceof MainActivity))
            return;
        String text = String.format(TIMER_TEMPLATE,intent.getIntExtra("minutes",0),
                intent.getIntExtra("seconds",0));
        TextView timerLabel = ((MainActivity)context).findViewById(R.id.timerLbl);
        timerLabel.setText(text);
    }
}