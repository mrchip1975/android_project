package it.learnathome.indovinalaparola.screen.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.learnathome.indovinalaparola.R;
import it.learnathome.indovinalaparola.data.Record;
import it.learnathome.indovinalaparola.data.RecordManager;
import it.learnathome.indovinalaparola.screen.adapters.RecordAdapter;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {
    private int recordType;
  public RecordFragment() {
        // Required empty public constructor
    }
    public RecordFragment setRecordType(int id) {
      this.recordType = id;

      return this;
    }
   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout= inflater.inflate(R.layout.fragment_record, container, false);
       TextView header = layout.findViewById(R.id.headerRecordLabel);
       header.setText(this.recordType==R.id.personalBest?getContext().getResources().getString(R.string.personal_best_menu_item):"Ciccio ciccio");
        showRecords(layout.findViewById(R.id.recordListView));
        return layout;
    }
    private void showRecords(ListView list) {
        List<Record>  recordsList = new RecordManager(getContext()).selectAll();
        /*List<String> info = new ArrayList<>();
        for (Record record:recordsList) {
            info.add(record.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.my_record_textview,info);*/
        list.setAdapter(new RecordAdapter(getContext(), recordsList));
    }
}