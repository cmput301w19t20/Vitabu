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
 * This file contains the adapter for the acceptBookRequestRecyclerview that implements the UI and
 * functionality of the recyclerView.
 *
 * Author: Katherine Richards
 * Version: 1.0
 * Outstanding Issues: ---
 */

package com.example.vitabu;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class acceptBookRequestsRecyclerViewAdapter extends RecyclerView.Adapter<acceptBookRequestsRecyclerViewAdapter.ViewHolder> {

    private List<BorrowRecord> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private RecyclerView recyclerView;
    private boolean onCreate;
    private String userName;
    private ArrayList<String> recordids;
    private Context context;

    // data is passed into the constructor
    acceptBookRequestsRecyclerViewAdapter(Context context, List<BorrowRecord> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    public void setRecyclerView(RecyclerView r){
        this.recyclerView = r;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.content_book_requests, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICKED", "CLICKED");
//                int position = recyclerView.getChildLayoutPosition(v);
//                BorrowRecord record = getItem(position);
//                record.setApproved(true);
//                record.setRecordid(UUID.randomUUID().toString());
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference();
//                final String bookid = record.getBookid();
//                onCreate = true;
//                recordids = new ArrayList<>();
//                myRef.child("borrowrecords").orderByChild("ownerName").equalTo(userName).addValueEventListener(
//                        new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot snapshot) {
//                                if(onCreate) {
//                                    onCreate = false;
//                                    Log.d("Count1 ", "" + snapshot.getChildrenCount());
//                                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                                        String bid = (String) postSnapshot.child("bookid").getValue();
//                                        if (bid.equals(bookid)) {
//                                            recordids.add(postSnapshot.getKey());
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError e) {
//                            }
//                        });
//                for(String id: recordids){
//                    myRef.child("borrowrecords").child(id).removeValue();
//                }
//                myRef.child("borrowrecords").child(record.getRecordid()).setValue(record);
//                Intent intent = new Intent(context, setMeetingActivity.class);
//                Gson gson = new Gson();
//                intent.putExtra(MainActivity.BORROWRECORD_MESSAGE, gson.toJson(record));
//                context.startActivity(intent);
            }
        });
        return new ViewHolder(view);
    }

    // get the borrower username from the borrowrecord and set to textview
    @Override
    public void onBindViewHolder(acceptBookRequestsRecyclerViewAdapter.ViewHolder holder, int position) {
        BorrowRecord book = mData.get(position);
        String requester = book.getBorrowerName();
        holder.requester.setText(requester);
    }

    // Returns total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView requester;

        ViewHolder(View itemView) {
            super(itemView);
            requester = itemView.findViewById(R.id.book_requests_requester_username);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    BorrowRecord getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
