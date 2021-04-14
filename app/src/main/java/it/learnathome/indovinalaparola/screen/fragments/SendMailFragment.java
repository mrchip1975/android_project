package it.learnathome.indovinalaparola.screen.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import it.learnathome.indovinalaparola.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SendMailFragment extends Fragment {
 public SendMailFragment() {
        // Required empty public constructor
    }
  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_send_mail, container, false);
        layout.findViewById(R.id.send).setOnClickListener(v -> sendMail(layout));
        return layout;
    }
    private void sendMail(View v) {
        EditText field;
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        field = v.findViewById(R.id.senderField);
        intent.putExtra(Intent.EXTRA_EMAIL, field.getText());
        field = v.findViewById(R.id.subjectField);
        intent.putExtra(Intent.EXTRA_SUBJECT, field.getText());
        field = v.findViewById(R.id.contentField);
        intent.putExtra(Intent.EXTRA_TEXT,field.getText());
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
 }
