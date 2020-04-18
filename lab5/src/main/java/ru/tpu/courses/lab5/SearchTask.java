package ru.tpu.courses.lab5;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class SearchTask extends Task<List<Repo>> {

    private static OkHttpClient httpClient;
    private static String query="";
    private static boolean waitDelay = false;

    public static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (SearchTask.class) {
                if (httpClient == null) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BASIC);
                    httpClient = new OkHttpClient.Builder()
                            .addInterceptor(loggingInterceptor)
                            .build();
                }
            }
        }
        return httpClient;
    }

    public SearchTask(@Nullable Observer<List<Repo>> observer) {
        super(observer);
    }

    public SearchTask(@Nullable Observer<List<Repo>> observer, String query) {
        super(observer);
        this.query = query;
    }

    public void newSearch(String newQuery){
        waitDelay = (query.length() > 2);
        if(waitDelay)
            resetQueries();

        query = newQuery;
    }

    @Override
    @WorkerThread
    protected List<Repo> executeInBackground() throws Exception {
        if(waitDelay)
            synchronized(this) {
                wait(500);
            }
        String response = search();
        return parseSearch(response);
    }

    private String search() throws Exception {
        Request request = new Request.Builder()
                .url("https://api.github.com/search/repositories?q="+query)
                .build();
        Response response = getHttpClient().newCall(request).execute();
        if (response.code() != 200) {
            throw new Exception("api returned unexpected http code: " + response.code());
        }

        return response.body().string();
    }

    private List<Repo> parseSearch(String response) throws JSONException {
        JSONObject responseJson = new JSONObject(response);
        List<Repo> repos = new ArrayList<>();
        JSONArray items = responseJson.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            JSONObject repoJson = items.getJSONObject(i);
            Repo repo = new Repo();
            repo.fullName = repoJson.getString("full_name");
            repo.description = repoJson.getString("description");
            repo.url = repoJson.getString("html_url");
            repos.add(repo);
        }
        return repos;
    }
}