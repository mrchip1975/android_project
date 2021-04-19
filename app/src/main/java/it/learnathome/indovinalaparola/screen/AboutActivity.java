package it.learnathome.indovinalaparola.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import it.learnathome.indovinalaparola.MainActivity;
import it.learnathome.indovinalaparola.R;
import it.learnathome.indovinalaparola.screen.fragments.AboutFragment;
import it.learnathome.indovinalaparola.screen.fragments.CreditsFragment;
import it.learnathome.indovinalaparola.screen.fragments.RecordFragment;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.about_header_colour,getTheme())));
        int id = getIntent().getIntExtra("id",R.id.aboutMenuItem);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.contentArea,id==R.id.aboutMenuItem?new AboutFragment():
                id==R.id.personalBest?new RecordFragment().setRecordType(id):new CreditsFragment());
        transaction.commit();
    }
    public void closeClick(View v) {
        Intent result = new Intent();
        result.putExtra("messaggio","hello");
        setResult(RESULT_OK,result);
        finish();
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
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.contentArea,id==R.id.aboutMenuItem?new AboutFragment():
                id==R.id.personalBest?new RecordFragment():new CreditsFragment());
        transaction.commit();

        return super.onOptionsItemSelected(item);
    }
}