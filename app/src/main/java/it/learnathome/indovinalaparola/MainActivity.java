package it.learnathome.indovinalaparola;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import it.learnathome.indovinalaparola.screen.AboutActivity;
import it.learnathome.indovinalaparola.utils.GameMaster;

public class MainActivity extends AppCompatActivity {
    private static final int ABOUT_INTENT_ID = 1;
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("onCreate","onCreate avviato");
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
    }
    public void checkAttempt(View v) {
        counter++;
        TextView attemptCounterLbl = findViewById(R.id.attemptCounter);
        attemptCounterLbl.setText(String.valueOf(counter));
        EditText myAttemptField = findViewById(R.id.myAttemptField);
        String myAttemptFieldContent = myAttemptField.getText().toString();
        if(GameMaster.youWin(myAttemptFieldContent)) {
            //Toast.makeText(MainActivity.this,getResources().getString(R.string.win_message),Toast.LENGTH_LONG).show();
            buildAlert();
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
        Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
        switch(id) {
            case R.id.creditsMenuItem:
            case R.id.aboutMenuItem:
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
       // builder.setMessage(getResources().getString(R.string.win_message));
       // builder.setTitle(getResources().getString(R.string.app_name));
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
}