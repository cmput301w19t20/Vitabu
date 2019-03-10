package com.example.vitabu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class BrowseBooksFragment extends Fragment implements BrowseBooksBookRecyclerViewAdapter.ItemClickListener {
    private BrowseBooksBookRecyclerViewAdapter adapter;
    private LocalUser curUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout
        View fragmentView = inflater.inflate(R.layout.fragment_browse_books, container, false);

        // Get current user from Activity
        curUser = ((browseBooksActivity) getActivity()).getCurUser();

        // Generate data to populate the RecyclerView with
        ArrayList<Book> books = new ArrayList<>();
        Book book;
        for (int i = 0; i < 10; i++) {
            book = new Book("Title" + Integer.toString(i), "Author", "1234", "available", "owen", "description", "bookid");
            books.add(book);
        }

        // Set up the RecyclerView
        RecyclerView recyclerView = fragmentView.findViewById(R.id.browse_books_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new BrowseBooksBookRecyclerViewAdapter(this.getActivity(), books);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        return fragmentView;
    }

    @Override
    public void onItemClick(View view, int position) {
//        TODO: Opens book info activity

        Intent intent = new Intent(this.getContext(), bookInfoActivity.class);
        Log.d("fragment launch", adapter.getItem(position).getTitle());
        Gson gson = new Gson();
        String message = gson.toJson(adapter.getItem(position));
        intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
        startActivity(intent);

        Toast.makeText(this.getActivity(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
