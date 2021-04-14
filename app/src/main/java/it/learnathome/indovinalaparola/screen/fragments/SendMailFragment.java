package it.learnathome.indovinalaparola.screen.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import it.learnathome.indovinalaparola.R;

import static android.app.Activity.RESULT_OK;
import static it.learnathome.indovinalaparola.R.color.header_textcolour;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SendMailFragment extends Fragment {
    private static final int SENDER_ID_ACTIVITY = 2;

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
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"help.me.alessandro@gmail.com"});
        field = v.findViewById(R.id.subjectField);
        intent.putExtra(Intent.EXTRA_SUBJECT, field.getText().toString());
        field = v.findViewById(R.id.contentField);
        intent.putExtra(Intent.EXTRA_TEXT,field.getText());
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent,SENDER_ID_ACTIVITY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            if (requestCode==SENDER_ID_ACTIVITY) {
                Toast toast = Toast.makeText(getContext(),getString(R.string.send_mail_successfull),Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.getView().setBackgroundColor(getResources().getColor(header_textcolour,getActivity().getTheme()));
                TextView testo = toast.getView().findViewById(android.R.id.message);
                testo.setTextColor(getResources().getColor(android.R.color.white,getActivity().getTheme()));
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getContext(),getString(R.string.send_mail_error),Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }
}
