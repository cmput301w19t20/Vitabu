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
 * This file contains the adapter for the RequestedBooksBookRecyclerView. This adapter provides the UI and
 * functionality to the recycler view to display the requested books correctly.
 *
 * Author: Jacob Paton
 * Version: 1.2
 * Outstanding Issues: ---
 */

package com.example.vitabu;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RequestedBooksBookRecyclerViewAdapter extends RecyclerView.Adapter<RequestedBooksBookRecyclerViewAdapter.ViewHolder> {

    private List<Book> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RequestedBooksBookRecyclerViewAdapter(Context context, List<Book> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public RequestedBooksBookRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.requested_books_book, parent, false);
        return new RequestedBooksBookRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the fields in each row
    @Override
    public void onBindViewHolder(final RequestedBooksBookRecyclerViewAdapter.ViewHolder holder, int position) {
        Book book = mData.get(position);

        String title = book.getTitle();
        String author = book.getAuthor();
        String owner = book.getOwnerName();

        holder.title.setText(title);
        holder.author.setText(author);
        holder.owner.setText(owner);

        //This section gets the image of the book if it currently exists from the firebase storage service
        StorageReference mReference = FirebaseStorage.getInstance().getReference().child("images/" + book.getBookid());
        mReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .fit()
                        .centerCrop()
                        .into(holder.image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Do nothing.
            }
        });
    }

    // Returns total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, author, owner;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.requested_fragment_book_title);
            author = itemView.findViewById(R.id.requested_fragment_book_author);
            owner = itemView.findViewById(R.id.requested_fragment_book_owner);
            image = itemView.findViewById(R.id.requested_fragment_book_image);
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
    void setClickListener(RequestedBooksBookRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
