package it.learnathome.indovinalaparola;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;
import java.util.StringJoiner;

import it.learnathome.indovinalaparola.data.Record;
import it.learnathome.indovinalaparola.data.RecordManager;
import it.learnathome.indovinalaparola.screen.AboutActivity;
import it.learnathome.indovinalaparola.utils.GameMaster;

public class MainActivity extends AppCompatActivity {
    private static final int ABOUT_INTENT_ID = 1;
    private int counter = 0;
    private GameTimer timer;
    private LocalTime time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.header_textcolour,getTheme())));
    }
    public void startGame(View v) {
        TextView shuffledTextLbl = findViewById(R.id.shuffledText);
        shuffledTextLbl.setText(GameMaster.startGame(MainActivity.this));
        TextView yourProgressLbl = findViewById(R.id.yourProgressText);
        yourProgressLbl.setText(GameMaster.buildCryptedWord());
        TextView attemptCounterLbl = findViewById(R.id.attemptCounter);
        attemptCounterLbl.setText("0");
        this.counter = 0;
        EditText myAttemptField = findViewById(R.id.myAttemptField);
        myAttemptField.setText("");
        timer = new GameTimer(findViewById(R.id.timerLbl));
        timer.start();
    }
    public void checkAttempt(View v) {
        counter++;
        TextView attemptCounterLbl = findViewById(R.id.attemptCounter);
        attemptCounterLbl.setText(String.valueOf(counter));
        EditText myAttemptField = findViewById(R.id.myAttemptField);
        String myAttemptFieldContent = myAttemptField.getText().toString();
        if(GameMaster.youWin(myAttemptFieldContent)) {
            //Toast.makeText(MainActivity.this,getResources().getString(R.string.win_message),Toast.LENGTH_LONG).show();

            buildSaveAlert();
            return;
        }

        String checkedResult = GameMaster.checkAttempt(myAttemptFieldContent);
        TextView yourProgressLbl = findViewById(R.id.yourProgressText);
        yourProgressLbl.setText(checkedResult);
        myAttemptField.setText("");
        TextView shuffledTextLbl = findViewById(R.id.shuffledText);
        shuffledTextLbl.setText(GameMaster.updateShuffledText(shuffledTextLbl.getText().toString()));
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
        Log.d("personalBest",id==R.id.personalBest?"record":"nulla");
        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
        switch(id) {
            case R.id.recordMenu:
                return true;
            case R.id.creditsMenuItem:
            case R.id.aboutMenuItem:
            case R.id.personalBest:
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
        builder.setNegativeButton(getResources().getString(android.R.string.no),(dialog,which)->finish());
        builder.create().show();
    }
    private void buildSaveAlert() {
        timer.interrupt();
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
            Toast.makeText(MainActivity.this,getString(R.string.record_saved),Toast.LENGTH_LONG).show();
            dialog.dismiss();
            buildAlert();
        });
        builder.create().show();
    }
    private class GameTimer extends Thread{
        private TextView timerLabel;
        public GameTimer(TextView view) {
            this.timerLabel = view;
        }
        @Override
        public void run() {
            int minutes=0, seconds=0;
            while(!isInterrupted()) {
                 for(;seconds<60 && !isInterrupted();seconds++){
                    String timer = String.format("%d:%d",minutes,seconds);
                    runOnUiThread(()->timerLabel.setText(timer));
                    //timerLabel.post(()->timerLabel.setText(timer));
                    //timerLabel.postDelayed(()->timerLabel.setText(timer),1000);
                    SystemClock.sleep(1000);
                }
                minutes++;
                seconds=0;
            }
            time = LocalTime.of(minutes/60,minutes%60,seconds);
        }
    }
}
