package com.example.vitabu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class acceptBookRequestActivity extends AppCompatActivity implements bookRequestsRecyclerViewAdapter.ItemClickListener {

    acceptBookRequestsRecyclerViewAdapter recyclerViewAdapter;
    ArrayList<BorrowRecord> records;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_book_request);
        // when intent is passed,

        TextView bookTitle = (TextView) findViewById(R.id.book_requests_book_name);
        bookTitle.setText("Book Title Here"); // will get title from intent passed

        // set up the recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.book_requests_list);
        acceptBookRequestsRecyclerViewAdapter recyclerAdapter = new acceptBookRequestsRecyclerViewAdapter(this, records);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(acceptBookRequestActivity.this, "You clicked " + recyclerViewAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        // if user presses accept, go to setMeetingActivity

    }
}
