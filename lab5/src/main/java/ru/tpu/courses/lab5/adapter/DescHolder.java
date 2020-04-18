package ru.tpu.courses.lab5.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.courses.lab5.R;

public class DescHolder extends RecyclerView.ViewHolder{

    private static String TAG = "DescHolder";
    public final TextView desc;

    public DescHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab5_item_desc, parent, false));
        desc = itemView.findViewById(R.id.desc);
    }
}
