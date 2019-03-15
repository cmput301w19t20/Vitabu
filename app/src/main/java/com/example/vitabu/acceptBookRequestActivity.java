/*
Vitabu is an Open Source application available under the Apache (Version 2.0) License.

Copyright 2019 Arseniy Kouzmenkov, Owen Randall, Ayooluwa Oladosu, Tristan Carlson, Jacob Paton,
Katherine Richards

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * This file contains the activity that deals with the accepting of a book request.
 *
 * Author: Katherine Richards
 * Version: 1.0
 * Outstanding Issues: ---
 */
package com.example.vitabu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

public class acceptBookRequestActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private acceptBookRequestsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Book book;
    private String bookid;
    private String userName;
    final String logTag = "acceptBookRequest";
    private ArrayList<BorrowRecord> records = new ArrayList<>();
    private ArrayList<String> recordids;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_book_request);

        // receive and deserialize the requested book
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.BOOK_MESSAGE);
        book = gson.fromJson(message, Book.class);
        bookid = book.getBookid();

        // set heading for current book request
        TextView bookTitle = (TextView) findViewById(R.id.book_requests_book_name);
        String bTitle = "pending requests for: " + book.getTitle();
        bookTitle.setText(bTitle); // will get title from intent passed

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        userName = firebaseUser.getDisplayName();

        createRequestersList(bookid); // populate the records list with current borrow records
        buildRecyclerView(); // initialize the recyclerview

    }

    public void createRequestersList(final String bookid) {

        Log.d("PULLING", "FROM DATABASE");
        myRef.child("borrowrecords").orderByChild("ownerName").equalTo(userName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d("Count1 ", "" + snapshot.getChildrenCount());
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            String bid = (String) postSnapshot.child("bookid").getValue();
                            if (bid.equals(bookid)) {
                                records.add(new BorrowRecord(userName, (String) postSnapshot.child("borrowerName").getValue(), bookid));
                            }
                        }
                        Log.d("RECORDS", "" + records.size());
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {
                    }
                });

//        mAdapter.notifyDataSetChanged();
    }

    public void buildRecyclerView() {

        mRecyclerView = findViewById(R.id.book_requests_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new acceptBookRequestsRecyclerViewAdapter(records);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // handle onclick methods for recycler view
        mAdapter.setOnItemClickListener(new acceptBookRequestsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                displayItemClickMessage(position);
            }


            // go to view requester's profile
            @Override
            public void onUsernameClick(int position) {
                BorrowRecord record = records.get(position);
                String requesterName = record.getBorrowerName();
                viewRequesterProfile(requesterName , position);
            }

            // accept request and go to setMeetingActivity
            @Override
            public void onAcceptButtonClick(int position) {
                acceptBookRequest(position);
            }
        });

        // handle swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                // do nothing
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // identify the item swiped
                int position = viewHolder.getAdapterPosition();
                Toast.makeText(acceptBookRequestActivity.this, "Removing item at position: " + position, Toast.LENGTH_SHORT).show();
                declineBookRequest(position);
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    public void displayItemClickMessage(int position) {
        Toast.makeText(acceptBookRequestActivity.this, "You clicked on item: " + mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
    }

    public void viewRequesterProfile(String requester, int position) {
        Toast.makeText(acceptBookRequestActivity.this, "View user " + mAdapter.getItem(position) + "'s profile?", Toast.LENGTH_SHORT).show();
        // Get Owner from database. When done launch goToUserProfileActivity()
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(requester).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        goToUserProfileActivity(dataSnapshot.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Database error", databaseError.toException());
                    }
                }
        );
    }

    public void acceptBookRequest(int position) {
        Toast.makeText(acceptBookRequestActivity.this, "You would like to accept the request at row: " + position, Toast.LENGTH_SHORT).show();
        BorrowRecord record = mAdapter.getItem(position);
        record.setApproved(true);
        record.setRecordid(UUID.randomUUID().toString());
        recordids = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("borrowrecords").orderByChild("ownerName").equalTo(userName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d("Count1 ", "" + snapshot.getChildrenCount());
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            String bid = (String) postSnapshot.child("bookid").getValue();
                            if (bid.equals(bookid)) {
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

        goToSetMeetingActivity(record);
    }

    //TODO: remove the borrow record from the database when user declines request
    public void declineBookRequest(int position) {
        records.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyDataSetChanged();
    }

    public void goToUserProfileActivity(User owner) {
        Intent intent = new Intent(this, userProfileActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MainActivity.USER_MESSAGE, gson.toJson(owner));
        startActivity(intent);
    }

    //TODO: delete all remaining book requests from database, update recyclerview
    public void goToSetMeetingActivity(BorrowRecord record) {
        Intent intent = new Intent(this, setMeetingActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MainActivity.BORROWRECORD_MESSAGE, gson.toJson(record));
        startActivity(intent);
    }

}
