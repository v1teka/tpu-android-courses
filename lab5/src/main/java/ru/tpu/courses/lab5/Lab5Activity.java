package ru.tpu.courses.lab5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import ru.tpu.courses.lab5.adapter.ReposAdapter;

public class Lab5Activity  extends AppCompatActivity {
    private static final String TAG = "Lab5Activity";
    private static Executor threadExecutor = Executors.newCachedThreadPool();
    private RecyclerView list;
    private final ReposCache reposCache = ReposCache.getInstance();
    private ReposAdapter reposAdapter;
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab5Activity.class);
    }

    private SearchTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.lab5_title, getClass().getSimpleName()));
        setContentView(R.layout.lab5_activity);

        list = findViewById(android.R.id.list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        list.setAdapter(reposAdapter = new ReposAdapter());
        reposAdapter.setRepos(reposCache.getRepos());

        task = new SearchTask(searchObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab5_search, menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        SearchView searchView = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            searchView = (SearchView) myActionMenuItem.getActionView();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                task.newSearch(s);
                if(s.length() > 2){
                    threadExecutor.execute(task);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.unregisterObserver();
    }

    private Observer<List<Repo>> searchObserver = new Observer<List<Repo>>() {
        @Override
        public void onLoading(@NonNull Task<List<Repo>> task) {
            Log.d(TAG, "onLoading");
        }

        @Override
        public void onSuccess(@NonNull Task<List<Repo>> task, @Nullable List<Repo> data) {
            Log.d(TAG, "onSuccess");
            for (Repo repo : data) {
                reposCache.addRepo(repo);
            }
            reposAdapter.setRepos(reposCache.getRepos());
            reposAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError(@NonNull Task<List<Repo>> task, @NonNull Exception e) {
            Log.e(TAG, "onError", e);
        }
    };
}
