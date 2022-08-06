package com.example.whowroteit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
* The connection between the AsyncTaskLoader and the Activity that calls it is implemented with
* the LoaderManager.LoaderCallbacks interface. These loader callbacks are a set of
* methods in the activity that are called by the LoaderManager when the loader is being created,
* when the data has finished loading, and when the loader is reset
* */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    // declaring the needed objects to access the ui elements
    private EditText mbookInput;
    private TextView mauthorName;
    private TextView mbookTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialising the objects
        mbookInput = findViewById(R.id.bookInput);
        mauthorName = findViewById(R.id.authorText);
        mbookTitle = findViewById(R.id.titleText);
    }

    public void searchBook(View view) {
        // getting the book title
        String queryString = mbookInput.getText().toString();
        // client side api exist in each application context
        // able to handle input methods
        InputMethodManager inputManager = (InputMethodManager)
                                                getSystemService(Context.INPUT_METHOD_SERVICE);
        // there is only input method is enable at a time
        // if enable then hide it till the user not enable it.. enabling by click on edittext
        if (inputManager != null ) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                                                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // manages the nectwork connection
        ConnectivityManager connMgr = (ConnectivityManager)
                                            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        // if context have the service of Connectivity then this will true
        if (connMgr != null) {
            // getting the networkInfo
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // only search when there is connection to the network and queryString is not null
        if(networkInfo != null && networkInfo.isConnected() && queryString.length() != 0) {
            // create the bundle to pass in Loader
            Bundle queryBundle  = new Bundle();
            queryBundle.putString("queryString",queryString);
            // this initiate the LoaderManager every activity have one LoaderManager
            /* The restartLoader() method takes three arguments:
               A loader id, which is useful if you implement more than one loader in your activity.
               An arguments Bundle for any data that the loader needs.
               The instance of LoaderCallbacks that you implemented in your activity.
               If you want the loader to deliver the results to the MainActivity, specify this as the third argument.*/
            getSupportLoaderManager().restartLoader(0,queryBundle,this);

            mbookTitle.setText(R.string.loading);
            mauthorName.setText("");
        } else {
            if(queryString.length() == 0) {
                mbookTitle.setText(R.string.no_search_term);
                mauthorName.setText("");
            } else {
                mbookTitle.setText(R.string.no_network_connection);
                mauthorName.setText("");
            }
        }
    }

    //it is called when we instantiate the loader.
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = null;
        if(args != null) {
            queryString = args.getString("queryString");
        }
        return new BookLoader(this,queryString);
    }

    // called when the loader's task is finished and we update the ui element
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            // declaring the JSONObject which is use to read JSON string
            // here we have the string but we didn't remove any syntax or other things which
            // change it's JSON property
            JSONObject jsonObject = new JSONObject(data);
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
                mbookTitle.setText(title);                 // set the title of first book find in items array
                mauthorName.setText(author);
            } else {
                mbookTitle.setText(R.string.noResult);
                mauthorName.setText("");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            // not receive a proper json object then pass no result string
            mbookTitle.setText(R.string.noResult);
            mauthorName.setText("");
        }
    }

    // cleans up any remaining resources.
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}