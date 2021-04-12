package it.learnathome.indovinalaparola;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import it.learnathome.indovinalaparola.utils.GameMaster;

public class MainActivity extends AppCompatActivity {
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startGame(View v) {
        TextView shuffledTextLbl = findViewById(R.id.shuffledText);
        shuffledTextLbl.setText(GameMaster.startGame(MainActivity.this));
        TextView yourProgressLbl = findViewById(R.id.yourProgressText);
        yourProgressLbl.setText(GameMaster.buildCryptedWord());
        TextView attemptCounterLbl = findViewById(R.id.attemptCounter);
        attemptCounterLbl.setText("0");
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
    private void buildAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getResources().getString(R.string.win_message));
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(android.R.string.yes), (dialog, which) -> {
            dialog.dismiss();
            startGame(null);});
        builder.setNegativeButton(getResources().getString(android.R.string.no),(dialog,which)->finish());
        builder.create().show();
    }
}