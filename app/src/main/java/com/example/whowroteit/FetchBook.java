package com.example.whowroteit;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class FetchBook extends AsyncTask<String,Void,String> {

    private WeakReference<TextView> mtitleText;
    private WeakReference<TextView> mauthorName;

    public FetchBook(TextView titleText, TextView authorText) {
        mauthorName = new WeakReference<>(authorText);
        mtitleText = new WeakReference<>(titleText);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            return NetworkUtils.getBookInfo(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            // declaring the JSONObject which is use to read JSON string
            // here we have the string but we didn't remove any syntax or other things which
            // change it's JSON property
            JSONObject jsonObject = new JSONObject(s);
            // items is the array in JSON string which have all data related to books
            JSONArray items = jsonObject.getJSONArray("items");

            int i=0;                   // to iterate the items array
            String title = null;       // store title of the book first find in the array
            String author = null;      // store author name of the first book find in the array

            // iterate till not get the first book and store it's needed value
            while(i<items.length() && (title == null && author == null)) {
                // get the current item information
                // understand it like it is the ith index value and we are searching for books so each index  having a book data
                JSONObject book = items.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");        // book is also like and object and having volumeInfo

                try {
                    // get title name and author name from volumeInfo
                    title = volumeInfo.getString("title");
                    author = volumeInfo.getString("authors");
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                i++;                                           // next item
            }

            // pass this title and author name to textviews
            if(title != null && author != null) {
                mtitleText.get().setText(title);                 // set the title of first book find in items array
                mauthorName.get().setText(author);
            }
            else mtitleText.get().setText(R.string.noResult);
        }
        catch (JSONException e) {
            e.printStackTrace();
            // not receive a proper json object then pass no result string
            mtitleText.get().setText(R.string.noResult);
        }
    }
}
