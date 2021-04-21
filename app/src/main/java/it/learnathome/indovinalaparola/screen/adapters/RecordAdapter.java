package it.learnathome.indovinalaparola.screen.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.learnathome.indovinalaparola.R;
import it.learnathome.indovinalaparola.data.Record;

public class RecordAdapter extends BaseAdapter {
    private Context ctx;
    private List<Record> records;
    private LayoutInflater inflater;
    public RecordAdapter(Context ctx,List<Record> records) {
        this.ctx = ctx;
        this.records = records;
        this.inflater = (LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return this.records.size();
    }

    @Override
    public Record getItem(int position) {
        return this.records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = this.inflater.inflate(position<=2? R.layout.best_position_layout:R.layout.my_record_textview,null);
        switch(position) {
            case 0:
            case 1:
            case 2:  customizeBestRecordLayout(layout,position);
                     break;
            default: TextView text = layout.findViewById(R.id.baseRecordText);
                     text.setText(this.records.get(position).toString());
        }
        return layout;
    }
    private void customizeBestRecordLayout(View layout,int position) {
        if(position==1 || position==2) {
            ImageView recordPatch = layout.findViewById(R.id.prizeImg);
            recordPatch.setImageDrawable(this.ctx.getResources().getDrawable(position==1?R.drawable.ic_second:R.drawable.ic_third,this.ctx.getTheme()));
        }
        Record record = getItem(position);
        TextView label = layout.findViewById(R.id.playerNameText);
        label.setText(record.getName());
        label = layout.findViewById(R.id.wordText);
        label.setText(record.getWord());
        label = layout.findViewById(R.id.recordDateText);
        label.setText(record.getDate().toString());
        label = layout.findViewById(R.id.attemptText);
        if(position==1)
        label.setTextColor(this.ctx.getResources().getColor(R.color.second_prize_colour,this.ctx.getTheme()));
        else if(position==2)
                label.setTextColor(this.ctx.getResources().getColor(R.color.third_prize_colour,this.ctx.getTheme()));
        label.setText(String.valueOf(record.getAttempt()));
        label = layout.findViewById(R.id.timerText);
        label.setText(record.getTime());

    }
}
