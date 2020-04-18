package ru.tpu.courses.lab5;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ReposCache {

    private static ReposCache instance;

    public static ReposCache getInstance() {
        if (instance == null) {
            synchronized (ReposCache.class) {
                if (instance == null) {
                    instance = new ReposCache();
                }
            }
        }
        return instance;
    }

    private Set<Repo> repos = new LinkedHashSet<>();

    private ReposCache() {
    }

    @NonNull
    public List<Repo> getRepos() {
        return new ArrayList<>(repos);
    }

    public void addRepo(@NonNull Repo repo) {
        repos.add(repo);
    }

    public void removeRepo(Repo repo) {
        repos.remove(repo);
    }

    public boolean contains(@NonNull Repo repo) {
        return repos.contains(repo);
    }
}
