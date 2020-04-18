package ru.tpu.courses.lab5.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.tpu.courses.lab5.Repo;

public class ReposAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_REPO = 0;
    public static final int TYPE_DESCRIPTION = 1;

    private List<Repo> repos = new ArrayList<>();

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_REPO:
                return new RepoHolder(parent);
            case TYPE_DESCRIPTION:
                return new DescHolder(parent);
        }
        throw new IllegalArgumentException("unknown viewType = " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Repo repo = repos.get(position/2);
        switch (getItemViewType(position)) {
            case TYPE_REPO:
                RepoHolder repoHolder = (RepoHolder) holder;
                repoHolder.repo.setText(
                        repo.fullName
                );
                break;
            case TYPE_DESCRIPTION:
                DescHolder descHolder = (DescHolder) holder;
                descHolder.desc.setText(
                        repo.description
                );
                break;
        }
    }

    @Override
    public int getItemCount() {
        return repos.size() * 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_REPO : TYPE_DESCRIPTION;
    }

    public void setRepos(List<Repo> reposList) {
        this.repos = reposList;
    }
}


