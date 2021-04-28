package it.learnathome.indovinalaparola.screen.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import it.learnathome.indovinalaparola.utils.game.DownloadRecordService;
import it.learnathome.indovinalaparola.utils.game.SaveRecordReceiver;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment  {
    private int recordType;
    public static List<Record>  recordsList= new ArrayList();
    public static RecordAdapter adapter;
    private SaveRecordReceiver recordReceiver = new SaveRecordReceiver();
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
       header.setText(this.recordType==R.id.personalBest?getContext().getResources().getString(R.string.personal_best_menu_item):
               getContext().getResources().getString(R.string.world_best_menu_item));
        this.adapter = new RecordAdapter(getContext(),recordsList);
        showRecords(layout.findViewById(R.id.recordListView));
       getContext().registerReceiver(recordReceiver, new IntentFilter(DownloadRecordService.DOWNLOAD_RECORD_SERVICE_ID));
        return layout;
    }
    private void showRecords(ListView list) {
        list.setAdapter(adapter);
        if(R.id.personalBest==recordType) {
            recordsList.addAll(new RecordManager(getContext()).selectAll());
            adapter.notifyDataSetChanged();
        }else {
            Log.d("dws","start");
            Intent downloaderService = new Intent(getContext(), DownloadRecordService.class);
            getContext().startService(downloaderService);
            Log.d("dws","start");
        }
        /*List<String> info = new ArrayList<>();
        for (Record record:recordsList) {
            info.add(record.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.my_record_textview,info);*/


    }


}