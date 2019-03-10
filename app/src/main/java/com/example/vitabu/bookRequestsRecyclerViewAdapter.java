/*
 * This file contains the adapter for the bookRequestsRecyclerView to implement the functionality
 * behind the recyclerview.
 *
 * Author: Jacob Paton
 * Outstanding Issues: ---
 */
package com.example.vitabu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class bookRequestsRecyclerViewAdapter extends RecyclerView.Adapter<bookRequestsRecyclerViewAdapter.ViewHolder> {

    /**
     * This adapter is not yet ready for use. Need to consult with logic team to decide on
     * object that will be passed as data to the recycler.
     */
    private List<Book> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    bookRequestsRecyclerViewAdapter(Context context, List<Book> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.requested_books_book, parent, false);
        return new bookRequestsRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the fields in each row
    @Override
    public void onBindViewHolder(bookRequestsRecyclerViewAdapter.ViewHolder holder, int position) {
        Book book = mData.get(position);
        String requester = book.getOwnerName();
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
    Book getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(bookRequestsRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
