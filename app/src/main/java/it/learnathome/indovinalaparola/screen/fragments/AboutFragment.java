package it.learnathome.indovinalaparola.screen.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        layout.findViewById(R.id.sendMail).setOnClickListener(source->{
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.contentArea,new SendMailFragment());
            transaction.commit();
        });
        return layout;
    }
}