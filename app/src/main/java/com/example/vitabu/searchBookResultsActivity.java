package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class searchBookResultsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private searchBookResultsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Book> bookResults = new ArrayList(); // container for search results
    private LocalUser curUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book_results);

        //TODO: receive intent from searchBooksActivity

        //TODO: set up local user

        // set up textviews
        TextView inputTitle = (TextView) findViewById(R.id.search_results_title);
        TextView inputAuthor = (TextView) findViewById(R.id.search_results_author);
        TextView inputISBN = (TextView) findViewById(R.id.search_results_isbn);
        TextView inputKeywords = (TextView) findViewById(R.id.search_results_keywords);

        // populate fields with data passed through intent from searchBooksActivity
        // currently hardcoded for testing
        inputTitle.setText("test book name");
        inputAuthor.setText("test author");
        inputISBN.setText("0123456789");
        inputKeywords.setText("keywords input will be listed here");

        populateSearchResults();
        buildRecyclerView();


    }

    // populate list with search results from database
    public void populateSearchResults() {

    }

    public void buildRecyclerView() {
        // set up the recycler view
        mRecyclerView = findViewById(R.id.book_requests_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new searchBookResultsRecyclerViewAdapter(bookResults);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // on item click, go to book info
        mAdapter.setOnItemClickListener(new searchBookResultsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(searchBookResultsActivity.this, "You clicked " + mAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), bookInfoActivity.class);
                Log.d("launch", mAdapter.getItem(position).getTitle());
                Gson gson = new Gson();
                String bookMessage = gson.toJson(mAdapter.getItem(position));
                intent.putExtra(MainActivity.BOOK_MESSAGE, bookMessage);
                String LocalUserMessage = gson.toJson(curUser);
                intent.putExtra(MainActivity.LOCALUSER_MESSAGE, LocalUserMessage);
                startActivity(intent);
                }
        });
    }


}
