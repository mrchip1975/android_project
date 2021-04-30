package it.learnathome.indovinalaparola.screen.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import it.learnathome.indovinalaparola.R;

public class PersonalFragment extends Fragment {
    public PersonalFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout= inflater.inflate(R.layout.fragment_personal, container, false);
        Context ctx = getContext();
        SharedPreferences prefs = ctx.getSharedPreferences("prefs_game",Context.MODE_PRIVATE);
        int avatarId = ctx.getResources().getIdentifier(
                prefs.getString("avatar","avat0"),"drawable",
                ctx.getPackageName());
        ((ImageView)layout.findViewById(R.id.avatarImg)).setImageResource(avatarId);
        return layout;
    }
}