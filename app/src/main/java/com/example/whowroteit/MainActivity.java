package com.example.whowroteit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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

        new FetchBook(mbookTitle,mauthorName).execute(queryString);
        // print loading in one of the textview to interact the user
        mbookTitle.setText(R.string.loading);
    }
}