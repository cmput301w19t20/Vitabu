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

import com.google.gson.Gson;

import java.util.List;

public class acceptBookRequestsRecyclerViewAdapter extends RecyclerView.Adapter<acceptBookRequestsRecyclerViewAdapter.ViewHolder> {

    /**
     * This adapter is not yet ready for use. Need to consult with logic team to decide on
     * object that will be passed as data to the recycler.
     */

    private List<BorrowRecord> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    acceptBookRequestsRecyclerViewAdapter(Context context, List<BorrowRecord> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.content_book_requests, parent, false);
        return new acceptBookRequestsRecyclerViewAdapter.ViewHolder(view);
    }

    // get the borrower username from the borrowrecord and set to textview
    @Override
    public void onBindViewHolder(acceptBookRequestsRecyclerViewAdapter.ViewHolder holder, int position) {
        BorrowRecord book = mData.get(position);
        String requester = book.getBorrowerName();
        holder.requester.setText(requester);
        holder.accept.setText("accept");
        holder.decline.setText("decline");
    }

    // Returns total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView requester;
        Button accept, decline;

        ViewHolder(View itemView) {
            super(itemView);
            requester = itemView.findViewById(R.id.book_requests_requester_username);
            itemView.setOnClickListener(this);
            // accept button
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete request from database
                }
            });
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
    void setClickListener(acceptBookRequestsRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
