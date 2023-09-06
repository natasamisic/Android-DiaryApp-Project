package com.example.diaryapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.util.List;

public class EntryAdapter extends ArrayAdapter<Entry> {

    private List<Entry> entries;

    public EntryAdapter(Context context, int resource, List<Entry> entries) {
        super(context, resource);
        this.entries = entries;
    }

    public Entry getEntry(int position){
        Entry e = null;
        try{
            e = entries.get(position);
        }catch (IndexOutOfBoundsException i){
            Log.d("Entry adapter: ", i.getMessage());
        }
        return e;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);
        TextView title, text, time;
        title = convertView.findViewById(R.id.titleoutput);
        text = convertView.findViewById(R.id.textoutput);
        time = convertView.findViewById(R.id.timeoutput);

        final Entry entry = getEntry(position);
        title.setText(entry.getTitle());
        text.setText(entry.getText());
        String formatedTime = DateFormat.getDateTimeInstance().format(entry.getCreatedTime());
        time.setText(formatedTime);
        return convertView;
    }
}


