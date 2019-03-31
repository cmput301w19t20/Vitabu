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
import android.widget.RelativeLayout;
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

    private ArrayList<BorrowRecord> mData;
    private OnItemClickListener mClickListener;
    private Context context;

    // activity will have to implement these interface methods for an onclick event inside a row
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onUsernameClick(int position);
        void onAcceptButtonClick(int position);
    }

    // set up the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    // viewholder class declaration
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView requester;
        public Button acceptButton;
        public RelativeLayout viewBackground, viewForeground;

        // constructor
        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            requester = itemView.findViewById(R.id.book_requests_requester_username);
            acceptButton = itemView.findViewById(R.id.book_request_accept_button);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            requester.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onUsernameClick(position);
                        }
                    }
                }
            });

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAcceptButtonClick(position);
                        }
                    }
                }
            });
        }
    }

    // set the data list to the recycler view
    public acceptBookRequestsRecyclerViewAdapter(ArrayList<BorrowRecord> data) {
        mData = data;
    }

    // helper method for returning row id of list item clicked
    BorrowRecord getItem(int id) { return mData.get(id); }

    // getter method to access context of adapter
    Context getContext() {return context;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_book_requests, parent, false);
        context = parent.getContext();

        return new ViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BorrowRecord record = mData.get(position);
        holder.requester.setText(record.getBorrowerName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



}