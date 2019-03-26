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
 * This file contains the adapter for the OwnedBooksBookRecyclerView. This adapter provides the UI and
 * functionality to the recycler view to display the owned books correctly.
 *
 * Author: Owen Randall
 * Version: 1.2
 * Outstanding Issues: ---
 */
package com.example.vitabu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class OwnedBooksBookRecyclerViewAdapter extends RecyclerView.Adapter<OwnedBooksBookRecyclerViewAdapter.ViewHolder> {
    private List<Book> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    OwnedBooksBookRecyclerViewAdapter(Context context, List<Book> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.owned_books_book, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the fields in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = mData.get(position);
        String title = book.getTitle();
        String author = book.getAuthor();
        String status = book.getStatus();
        String borrower = book.getBorrower();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userName = firebaseUser.getDisplayName();

        if (!book.getOwnerName().equals(userName) && book.getStatus().equals("borrowed")){
            //Log.d("BOOKKKK", book.getTitle() + " " +  book.getOwnerName() + " " + userName);
            status = "borrowing";
        }

        if (borrower == null){
            borrower = "None.";
        }

        holder.title.setText(title);
        holder.author.setText(author);
        holder.status.setText(status);
        holder.borrower.setText(borrower);

    }

    // Returns total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, author, status, borrower;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.owned_books_book_title);
            author = itemView.findViewById(R.id.owned_books_book_author);
            status = itemView.findViewById(R.id.owned_books_book_status);
            borrower = itemView.findViewById(R.id.owned_books_book_borrower);
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
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
