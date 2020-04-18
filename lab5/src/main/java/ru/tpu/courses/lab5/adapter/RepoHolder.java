package ru.tpu.courses.lab5.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tpu.courses.lab5.R;
import ru.tpu.courses.lab5.Repo;

public class RepoHolder extends RecyclerView.ViewHolder{

    private static String TAG = "RepoHolder";
    public final TextView repo;

    public RepoHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab5_item_repo, parent, false));
        repo = itemView.findViewById(R.id.repo);
    }
}
