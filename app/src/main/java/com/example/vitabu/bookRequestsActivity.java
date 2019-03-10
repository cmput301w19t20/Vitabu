package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

public class bookRequestsActivity extends AppCompatActivity {

    /**
     * TODO: intent for this activity will be passed from an onClick for a book
     * TODO: retrieved under a filtered result of requests(?)
     *
     * TODO: create row layout for each book request
     */
    //Intent will contain JSON of the Book being requested
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_book_requests);

    }
}
