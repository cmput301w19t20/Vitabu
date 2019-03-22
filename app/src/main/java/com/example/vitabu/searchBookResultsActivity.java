package com.example.vitabu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class searchBookResultsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private acceptBookRequestsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Book> bookResults = new ArrayList(); // container for search results

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book_results);

        // unpack search parameters through intent here to set to TextViews

//        mRecyclerView = findViewById(R.id.book_requests_list);
//        mLayoutManager = new LinearLayoutManager(this);
//        mAdapter = new searchBookResultsRecyclerViewAdapter(bookResults);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // handle onclick methods for recycler view





    }




}
