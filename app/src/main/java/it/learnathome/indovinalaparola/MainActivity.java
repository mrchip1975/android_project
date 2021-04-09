package it.learnathome.indovinalaparola;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
    }
    public void checkAttempt(View v) {
        counter++;
        TextView attemptCounterLbl = findViewById(R.id.attemptCounter);
        attemptCounterLbl.setText(String.valueOf(counter));
        EditText myAttemptField = findViewById(R.id.myAttemptField);
        String myAttemptFieldContent = myAttemptField.getText().toString();
        String checkedResult = GameMaster.checkAttempt(myAttemptFieldContent);
        TextView yourProgressLbl = findViewById(R.id.yourProgressText);
        yourProgressLbl.setText(checkedResult);
        myAttemptField.setText("");
        TextView shuffledTextLbl = findViewById(R.id.shuffledText);
        shuffledTextLbl.setText(GameMaster.updateShuffledText(shuffledTextLbl.getText().toString()));
    }
}