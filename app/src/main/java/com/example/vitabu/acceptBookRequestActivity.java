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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private Database databaseWrapper = Database.getInstance();

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

    }

    public void createRequestersList(final String bookid) {
        // HI! I pulled your logic into the Database.java file.  The success Runnable will be called
        // When the borrowRecords are retrieved from firebase.  The List Adapter still doesnt show
        // the items, but I tested it, and the proper BorrowRecords are getting back here from the wrapper...
        // -Tristan
        Runnable fail = new Runnable() {
            @Override
            public void run() {
            }
        };

        Runnable success = new Runnable() {
            @Override
            public void run() {
                Log.d("RUNNING", "RUNNING");
                records = databaseWrapper.getFindBorrowRecordsByBookidReturnValue();
                ArrayList<BorrowRecord> recordCopy = new ArrayList<>();
                for(BorrowRecord record: records){
                    if(record.isApproved()) {
                        recordCopy.add(record);
                        Log.d("REMOVING", record.getRecordid());
                    }
                }
                for(BorrowRecord record: recordCopy){
                    records.remove(record);
                }
                buildRecyclerView(); // initialize the recyclerview
            }
        };
        databaseWrapper.findBorrowRecordsByBookid(success, fail, bookid);

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
                declineBookRequest(position);
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    public void displayItemClickMessage(int position) {

    }

    public void viewRequesterProfile(String requester, int position) {
        Runnable fail = new Runnable() {
            @Override
            public void run() {
            }
        };

        Runnable success = new Runnable() {
            @Override
            public void run() {
                goToUserProfileActivity(databaseWrapper.getFetchUserReturnValue());
            }
        };

        databaseWrapper.fetchUser(success, fail, requester);

    }

    public void acceptBookRequest(int position) {
        final BorrowRecord record = mAdapter.getItem(position);
        record.setApproved(true);
        //record.setRecordid(UUID.randomUUID().toString());
//        recordids = new ArrayList<>();


        databaseWrapper.acceptBorrowRequest(null, null, record);

        createRequestersList(bookid);
        String message = "Your request has been accepted by " + record.getOwnerName() + ".";
        Notification newNotification = new Notification("Request Accepted", message, "accept", record.getBorrowerName(), record.getRecordid());
        storeNotification(newNotification);
        goToSetMeetingActivity(record);
    }

    public void declineBookRequest(int position) {
        databaseWrapper.denyBorrowRequest(null, null, records.get(position));
        if (records.size() == 1){
            databaseWrapper.resetBookStatus(records.get(position).getBookid());
        }
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

    //TODO: update recyclerview
    public void goToSetMeetingActivity(BorrowRecord record) {
        Intent intent = new Intent(this, setMeetingActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MainActivity.BORROWRECORD_MESSAGE, gson.toJson(record));
        startActivity(intent);
    }

    private void storeNotification(Notification notif){
        myRef.child("notifications").child(notif.getNotificationid()).setValue(notif)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Review notification", "Successfully wrote notification to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Review notification", "Failed to write notification to database", e);
                    }
                });

    }
}
