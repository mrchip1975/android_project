package it.learnathome.indovinalaparola;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.StringJoiner;

import it.learnathome.indovinalaparola.data.Record;
import it.learnathome.indovinalaparola.data.RecordManager;
import it.learnathome.indovinalaparola.screen.AboutActivity;
import it.learnathome.indovinalaparola.utils.AsyncTimer;
import it.learnathome.indovinalaparola.utils.GameMaster;
import it.learnathome.indovinalaparola.utils.game.SaveRecordReceiver;
import it.learnathome.indovinalaparola.utils.game.SaveRecordService;
import it.learnathome.indovinalaparola.utils.game.TimerReceiver;
import it.learnathome.indovinalaparola.utils.game.TimerService;

public class MainActivity extends AppCompatActivity {
    private static final int ABOUT_INTENT_ID = 1;
    private static final int MONEY_PRICE = 20;
    private static final String COLOUR_TEMPLATE = "#%s%s00";
    private int red = 0;
    private int green = 255;
    private int counter = 0;
    private ArrayList<View> lettersView = new ArrayList<>();
    private static String time = "";
    private TimerReceiver receiver = new TimerReceiver();
    private SaveRecordReceiver sReceiver = new SaveRecordReceiver();
    private Intent timer;
    private StringBuilder shuffledText;
    private Animation animMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.header_textcolour, getTheme())));
        animMove = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(TimerService.ID_TIMER));
        registerReceiver(sReceiver, new IntentFilter(SaveRecordService.SAVE_SERVICE_ID));
        SharedPreferences preferences = getSharedPreferences("prefs_game", MODE_PRIVATE);
        int money = preferences.getInt("gold", 100);
        TextView piggyBankLbl = findViewById(R.id.piggyBankLbl);
        piggyBankLbl.setText(String.valueOf(money));
        ((TextView) findViewById(R.id.attemptCounter)).setTextColor(Color.parseColor(String.format(COLOUR_TEMPLATE,
                Integer.toHexString(red).length() < 2 ? "0" + Integer.toHexString(red) : Integer.toHexString(red),
                Integer.toHexString(green).length() < 1 ? "0" + Integer.toHexString(green) : Integer.toHexString(green))));

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
        unregisterReceiver(sReceiver);
    }

    public void startGame(View v) {
        LinearLayout shuffledTextLayout = findViewById(R.id.shuffledText);
        shuffledText = new StringBuilder(GameMaster.startGame(MainActivity.this));
        populateLayout(shuffledTextLayout, shuffledText.toString());
        TextView yourProgressLbl = findViewById(R.id.yourProgressText);
        yourProgressLbl.setText(GameMaster.buildCryptedWord());
        TextView attemptCounterLbl = findViewById(R.id.attemptCounter);
        attemptCounterLbl.setText("0");
        this.counter = 0;
        TextView timerLbl = findViewById(R.id.timerLbl);
        timerLbl.setText("0:0");
        TimerService.running = true;
        EditText myAttemptField = findViewById(R.id.myAttemptField);
        myAttemptField.setText("");
        timer = new Intent(MainActivity.this, TimerService.class);
        startService(timer);

    }

    private void populateLayout(LinearLayout shuffledTextLayout, String shuffledText) {
        lettersView = new ArrayList<>();
        shuffledTextLayout.removeAllViews();
        final DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = LayoutInflater.from(this);

        for(char ch:shuffledText.toCharArray()) {
            TextView cell = (TextView)inflater.inflate(R.layout.my_text_cell_layout,null);

            cell.setPadding(8,0,8,0);
            cell.setAutoSizeTextTypeUniformWithConfiguration(
                   24, 48, 3, TypedValue.COMPLEX_UNIT_DIP);
            cell.setLayoutParams(lparams);
            cell.setText(String.valueOf(ch));
            shuffledTextLayout.addView(cell);
            lettersView.add(cell);
        }
    }

    public void checkAttempt(View v) {
        counter++;
        red=red+12<=255?red+12:255;
        green=green-12>=0?green-12:0;
        TextView attemptCounterLbl = findViewById(R.id.attemptCounter);
        attemptCounterLbl.setTextColor(Color.parseColor(String.format(COLOUR_TEMPLATE,
                Integer.toHexString(red).length()<2?"0"+Integer.toHexString(red):Integer.toHexString(red),
                Integer.toHexString(green).length()<2?"0"+Integer.toHexString(green):Integer.toHexString(green))));
        attemptCounterLbl.setText(String.valueOf(counter));
        EditText myAttemptField = findViewById(R.id.myAttemptField);
        String myAttemptFieldContent = myAttemptField.getText().toString();
        if(GameMaster.youWin(myAttemptFieldContent)) {
            ((TextView)findViewById(R.id.yourProgressText)).setText(myAttemptFieldContent);
            TimerService.running = false;
            buildSaveAlert();
            return;
        }

        String checkedResult = GameMaster.checkAttempt(myAttemptFieldContent);
        TextView yourProgressLbl = findViewById(R.id.yourProgressText);
        yourProgressLbl.setText(checkedResult);
        myAttemptField.setText("");
        LinearLayout shuffledTextLayout = findViewById(R.id.shuffledText);
        int position = GameMaster.updateShuffledText(shuffledText.toString());
        if(position>=0) {

            shuffledText.deleteCharAt(position);
            TextView item = (TextView)shuffledTextLayout.getChildAt(position);
            item.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark,getTheme()));
            item.setTextColor(getResources().getColor(android.R.color.white,getTheme()));
            item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,64);
            item.startAnimation(animMove);
            shuffledTextLayout.removeViewAt(position);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
        switch(id) {
            case R.id.recordMenu:
                return true;
            case R.id.creditsMenuItem:
            case R.id.aboutMenuItem:
            case R.id.personalBest:
            case R.id.worldBest:
                    aboutIntent.putExtra("id",id);
                 break;

        }
        startActivityForResult(aboutIntent,ABOUT_INTENT_ID);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ABOUT_INTENT_ID: String messaggio = data.getStringExtra("messaggio");
                                      Log.d("messaggio",messaggio+" avviato");

            }
        }
    }

    private void buildAlert() {
       //
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
       builder.setTitle(getResources().getString(R.string.app_name));
        LayoutInflater inflater = getLayoutInflater();
        View screenWin = inflater.inflate(R.layout.win_layout,null);
        TextView winText = screenWin.findViewById(R.id.winText);
        winText.setText(getResources().getString(R.string.win_message));
        builder.setView(screenWin);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(android.R.string.yes), (dialog, which) -> {
            dialog.dismiss();
            startGame(null);});
        builder.setNegativeButton(getResources().getString(android.R.string.no),(dialog,which)->dialog.dismiss());
        builder.create().show();
    }
    private void buildSaveAlert() {
        time =  ((TextView)findViewById(R.id.timerLbl)).getText().toString();
        int moneyForYou = (int)(MONEY_PRICE*(1-(0.1*(counter-1))));
        if(moneyForYou>0) updateMoneyBank(moneyForYou);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View screenSave = inflater.inflate(R.layout.save_record_layout,null);
        View.OnClickListener l = v -> {
            TextView source = (TextView)v;
            char ch = source.getText().charAt(0);
            ch++;
            source.setText(ch<='Z'?String.valueOf(ch):"A");
        };
        screenSave.findViewById(R.id.firstLetter).setOnClickListener(l);
        screenSave.findViewById(R.id.secondLetter).setOnClickListener(l);
        screenSave.findViewById(R.id.thirdLetter).setOnClickListener(l);
        builder.setView(screenSave);
        builder.setNegativeButton(getString(R.string.no_save),(dialog,which)->{dialog.dismiss(); buildAlert();});
        builder.setPositiveButton(getString(R.string.yes_save),(dialog,which)->{
            StringJoiner buffer = new StringJoiner("");
            TextView letter = screenSave.findViewById(R.id.firstLetter);
            buffer.add(letter.getText().toString());
            letter = screenSave.findViewById(R.id.secondLetter);
            buffer.add(letter.getText().toString());
            letter = screenSave.findViewById(R.id.thirdLetter);
            buffer.add(letter.getText().toString());
            Record r = new Record();
            r.setName(buffer.toString())
             .setAttempts(counter)
             .setWord(GameMaster.getSecretWord())
             .setTime(time);
            new RecordManager(MainActivity.this).insert(r);
            CheckBox saveRanking = screenSave.findViewById(R.id.saveWorldChk);
            if (saveRanking.isChecked()) {
                Intent rankingIntent = new Intent(MainActivity.this, SaveRecordService.class);
                rankingIntent.putExtra("record",r);
                startService(rankingIntent);
            }
            Toast.makeText(MainActivity.this,getString(R.string.record_saved),Toast.LENGTH_LONG).show();
            dialog.dismiss();
            buildAlert();
        });
        builder.create().show();
    }
    private void updateMoneyBank(int money) {
        SharedPreferences prefs = getSharedPreferences("prefs_game", MODE_PRIVATE);
        int oldMoney = prefs.getInt("gold",100);
        oldMoney+=money;
        prefs.edit().putInt("gold",oldMoney).commit();
        ((TextView)findViewById(R.id.piggyBankLbl)).setText(String.valueOf(oldMoney));
    }



}
