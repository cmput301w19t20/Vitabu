package com.example.vitabu;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

public class bookRequestsFragment extends Fragment {

    /**
     * TODO: intent for this activity will be passed from an onClick for a book
     * TODO: retrieved under a filtered result of requests(?)
     *
     * TODO: create row layout for each book request
     */
    //Intent will contain JSON of the Book being requested
    private Book book;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_book_requests, container, false);
        // TODO Rethink the following code, as it will not work.  This fragment recieves it's intent from BrowseBooksActivity, which is not (and should not) be passed a book through intent.
        Intent intent = getActivity().getIntent();
        String message = intent.getStringExtra(MainActivity.BOOK_MESSAGE);
        Gson gson = new Gson();
        book = gson.fromJson(message, Book.class);

        return fragmentView;
    }
}
