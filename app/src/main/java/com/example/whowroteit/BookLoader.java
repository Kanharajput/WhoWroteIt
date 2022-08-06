package com.example.whowroteit;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;

/*
* AsyncTaskLoader loads data in the background and reassociates background tasks with the Activity,
* even after a configuration change. With an AsyncTaskLoader, if you rotate the device while the task is running,
* the results are still displayed correctly in the Activity.
*/
public class BookLoader extends AsyncTaskLoader<String> {

    private String queryString;

    public BookLoader(@NonNull Context context,String query) {
        super(context);
        queryString = query;        // query passed by the user
    }

    // system call this method when start the loader
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();                   // this will start the loadInBackground method
    }

    @Nullable
    @Override
    public String loadInBackground() {
        try {
            return NetworkUtils.getBookInfo(queryString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
