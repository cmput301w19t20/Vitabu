/*
 * This file contains the fragment that has the logic and UI for displaying and dealing with books
 * that have been requested by the user so far and have not yet been borrowed.
 *
 * Author: Jacob Paton
 * Outstanding Issues: Implement the functionality of the fragment.
 */
package com.example.vitabu;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class bookRequestsFragment extends Fragment implements AdapterView.OnItemSelectedListener, bookRequestsRecyclerViewAdapter.ItemClickListener{

    /**
     *  This Fragment is not yet ready to be used. Need to consult with logic team on how to
     *  retrieve all pending requests for a specific book.
     */

    bookRequestsRecyclerViewAdapter recyclerViewAdapter;
    //Intent will contain JSON of the Book being requested
    private Book book;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_book_requests, container, false);
        // TODO Rethink the following code, as it will not work.  This fragment recieves it's intent from BrowseBooksActivity, which is not (and should not) be passed a book through intent.
        Intent intent = getActivity().getIntent();
        String message = intent.getStringExtra(MainActivity.BOOK_MESSAGE);
        Gson gson = new Gson();
//        Book book = gson.fromJson(message, Book.class);

        // data to populate the RecyclerView with
        ArrayList<Book> books = new ArrayList<>();
        Book book;
        for (int i = 0; i < 10; i++) {
            book = new Book();
            book.setTitle("Title");
            book.setAuthor("Author");
            books.add(book);
        }
        // set up the RecyclerView
        RecyclerView recyclerView = fragmentView.findViewById(R.id.requested_books_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerViewAdapter = new bookRequestsRecyclerViewAdapter(this.getActivity(), books);
        recyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        return fragmentView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
