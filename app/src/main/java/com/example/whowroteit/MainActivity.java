package com.example.whowroteit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
        new FetchBook(mbookTitle,mauthorName).execute(queryString);
    }
}