package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.UUID;

public class acceptBookRequestActivity extends AppCompatActivity implements bookRequestsRecyclerViewAdapter.ItemClickListener {

    acceptBookRequestsRecyclerViewAdapter recyclerViewAdapter;
    private String userName;
    private ArrayList<BorrowRecord> records;
    private ArrayList<String> recordids;
    String message;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_book_request);

        records = new ArrayList<>();

        

        //receive the book
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.BOOK_MESSAGE);
        Gson gson = new Gson();
        book = gson.fromJson(message, Book.class);

        // set heading for current book being looked at
        TextView bookTitle = (TextView) findViewById(R.id.book_requests_book_name);
        String bTitle = "pending requests for: " + book.getTitle();
        bookTitle.setText(bTitle); // will get title from intent passed

        // set up the recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.book_requests_list);
        acceptBookRequestsRecyclerViewAdapter recyclerAdapter = new acceptBookRequestsRecyclerViewAdapter(this, records);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        userName = firebaseUser.getDisplayName();
        String bookMessage = intent.getStringExtra(MainActivity.BOOK_MESSAGE);
        Book book = gson.fromJson(bookMessage, Book.class);
        final String bookid = book.getBookid();
        myRef.child("borrowrecords").orderByChild("ownerName").equalTo(userName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        records = new ArrayList<>();
                        Log.d("Count1 ", "" + snapshot.getChildrenCount());
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            String bid = (String)postSnapshot.child("bookid").getValue();
                            if(bid.equals(bookid)){
                                records.add(new BorrowRecord(userName, (String)postSnapshot.child("borrowerName").getValue(), bookid));
                            }
                        }
                        recyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(acceptBookRequestActivity.this, "You clicked " + recyclerViewAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        BorrowRecord record = recyclerViewAdapter.getItem(position);
        record.setApproved(true);
        record.setRecordid(UUID.randomUUID().toString());
        recordids = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final String bookid = record.getBookid();
        myRef.child("borrowrecords").orderByChild("ownerName").equalTo(userName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d("Count1 ", "" + snapshot.getChildrenCount());
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            String bid = (String)postSnapshot.child("bookid").getValue();
                            if(bid.equals(bookid)){
                                recordids.add(postSnapshot.getKey());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {
                    }
                });
        for(String id: recordids){
            myRef.child("borrowrecords").child(id).removeValue();
        }
        myRef.child("borrowrecords").child(record.getRecordid()).setValue(record);
        Intent intent = new Intent(this, setMeetingActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MainActivity.BORROWRECORD_MESSAGE, gson.toJson(record));
        startActivity(intent);
    }
}
