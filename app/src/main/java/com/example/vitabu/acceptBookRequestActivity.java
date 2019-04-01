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

public class acceptBookRequestActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

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

    //This method is run when the activity is just started for the first time. It will populate the
    //recycler view that contains the information about who has requested the book so far.
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

    //This method creates an arraylist of the borrow records that will later populate the recycler view.
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

        //This will actually get the ArrayList from the database output to later populate the recycler view.
        //This part is run when the application gets the information from the database.
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

    //This method will create the recycler view from a given list of elements.
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
                //This used to print out debug information and does nothing anymore. The reason it exists
                //here is since it is required to use the recycler view adapter
            }


            // go to view requester's profile
            @Override
            public void onUsernameClick(int position) {
                BorrowRecord record = records.get(position);
                String requesterName = record.getBorrowerName();
                viewRequesterProfile(requesterName);
            }

            // accept request and go to setMeetingActivity
            @Override
            public void onAcceptButtonClick(int position) {
                acceptBookRequest(position);
            }
        });

        // attach the ItemTouchHelper to the recycler view
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    }


    //This method will find the username of the person that is requesting this book that was selected.
    public void viewRequesterProfile(String requester) {
        Runnable fail = new Runnable() {
            @Override
            public void run() {
            }
        };

        //Upon getting the username, will open up that user's profile page.
        Runnable success = new Runnable() {
            @Override
            public void run() {
                goToUserProfileActivity(databaseWrapper.getFetchUserReturnValue());
            }
        };

        databaseWrapper.fetchUser(success, fail, requester);

    }


    //This method will accept the book request for a specific user.
    public void acceptBookRequest(int position) {
        final BorrowRecord record = mAdapter.getItem(position);
        record.setApproved(true);

        databaseWrapper.acceptBorrowRequest(null, null, record);

        //Creates the notification and will start the process to open the set meeting activity.
        createRequestersList(bookid);
        String message = "Your request has been accepted by " + record.getOwnerName() + ". Tap to dismiss.";
        Notification newNotification = new Notification("Request Accepted", message, "accept", record.getBorrowerName(), record.getRecordid());
        storeNotification(newNotification);
        goToSetMeetingActivity(record);
    }


    //This will delete the request from the database and remove it from the recycler view.
    public void declineBookRequest(int position) {
        databaseWrapper.denyBorrowRequest(null, null, records.get(position));
        //If there will be no more requests after deleting the current one, this will change the book status back
        //to available.
        if (records.size() == 1){
            databaseWrapper.resetBookStatus(records.get(position).getBookid());
        }
        records.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyDataSetChanged();
    }


    //This will open the profile page for a specified user.
    public void goToUserProfileActivity(User owner) {
        Intent intent = new Intent(this, userProfileActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MainActivity.USER_MESSAGE, gson.toJson(owner));
        startActivity(intent);
    }


    //This method will start the set meeting activity.
    public void goToSetMeetingActivity(BorrowRecord record) {
        Intent intent = new Intent(this, setMeetingActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MainActivity.BORROWRECORD_MESSAGE, gson.toJson(record));
        startActivity(intent);
    }


    //This method will store the notification in the database.
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


    //When the user swipes the request, will delete that request.
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        // identify the item swiped
        declineBookRequest(position);
    }

}
