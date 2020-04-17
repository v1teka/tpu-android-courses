package ru.tpu.courses.lab5;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Lab5Activity  extends AppCompatActivity {
    private static final String TAG = "Lab5Activity";
    private static Executor threadExecutor = Executors.newCachedThreadPool();

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab5Activity.class);
    }

    private SearchTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.lab5_title, getClass().getSimpleName()));
        setContentView(R.layout.lab5_activity);
        //Handler::postDelayed(threadExecutor, 500);
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
                if(task != null)
                    task.unregisterObserver();
                if(s.length() > 2){
                    task = new SearchTask(searchObserver, s);
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
                Log.d(TAG, repo.toString());
            }
        }

        @Override
        public void onError(@NonNull Task<List<Repo>> task, @NonNull Exception e) {
            Log.e(TAG, "onError", e);
        }
    };
}
