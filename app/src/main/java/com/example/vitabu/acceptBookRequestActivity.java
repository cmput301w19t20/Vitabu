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
    RecyclerView recyclerView = findViewById(R.id.book_requests_list);
    private String userName;
    private ArrayList<BorrowRecord> records;
    private ArrayList<String> recordids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_book_request);


        TextView bookTitle = (TextView) findViewById(R.id.book_requests_book_name);
        bookTitle.setText("Book Title Here"); // will get title from intent passed

        // set up recycler view here

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        userName = firebaseUser.getDisplayName();
        Gson gson = new Gson();
        Intent intent = getIntent();
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