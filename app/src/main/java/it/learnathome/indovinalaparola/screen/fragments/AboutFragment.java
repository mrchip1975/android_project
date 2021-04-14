package it.learnathome.indovinalaparola.screen.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.learnathome.indovinalaparola.R;


public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_about, container, false);
        //TextView textView = layout.findViewById(R.id.gpl2Url);
        //textView.setMovementMethod(LinkMovementMethod.getInstance());
        return layout;
    }
}