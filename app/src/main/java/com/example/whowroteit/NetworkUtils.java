package com.example.whowroteit;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    // declaring the Log message key
    private static final String LOGTAG = NetworkUtils.class.getSimpleName();

    // Base Url for the books api
    private static final String Book_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    // parameter for the search string
    private static final String QUERY_PARAM = "q";
    // parameter that limit the search results
    private static final String MAX_RESULTS = "maxResults";
    // parameter to filter by print type
    private static final String PRINT_TYPE = "printType";

    // this method will return the Book details which it will get the book api
    static String getBookInfo(String queryString) throws IOException {
        HttpURLConnection urlConnection  = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        try {
            // here we know that all uri all same the differnce is in the query which is at last
            // so here we append that and all other things related to a search
            Uri builtURI = Uri.parse(Book_BASE_URL).buildUpon()
                            .appendQueryParameter(QUERY_PARAM,queryString)
                            .appendQueryParameter(MAX_RESULTS,"10")
                            .appendQueryParameter(PRINT_TYPE,"books")
                            .build();

            // now convert this uri to url
            URL requestUrl = new URL(builtURI.toString());
            // open the connection
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // get the input stream
            InputStream inputStream = urlConnection.getInputStream();
            // pass the input stream so to initiale the bufferedReader
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // use the stringBuilder to store the string
            StringBuilder stringBuilder = new StringBuilder();

            // read the inputStream line by line
            String line;
            while((line= reader.readLine()) != null) {
                stringBuilder.append(line);
                // end the line so it will be easy to debug
                stringBuilder.append("\n");
            }

            // if length is 0 means there is nothing return by api
            if(stringBuilder.length() == 0) {
                return null;
            }
            // convert the stringBuilder to string and store in bookJSONSting
            bookJSONString = stringBuilder.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            // disconnect the connection if it connected
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            // close the reader
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        Log.d(LOGTAG,bookJSONString);
        return bookJSONString;
    }
}
