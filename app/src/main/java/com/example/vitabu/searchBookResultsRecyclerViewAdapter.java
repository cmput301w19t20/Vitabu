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

import java.util.ArrayList;

public class searchBookResultsRecyclerViewAdapter extends RecyclerView.Adapter<searchBookResultsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Book> mData;
    private OnItemClickListener mClickListener;
    private Context context;

    // activity will have to implement these interface methods for an onclick event inside a row
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // set up the click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    // viewholder class declaration
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;
        public TextView status;
        public ImageView image;

        // constructor
        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.search_books_book_title);
            author = itemView.findViewById(R.id.search_books_book_author);
            status = itemView.findViewById(R.id.search_books_book_status);
            image = itemView.findViewById(R.id.search_books_book_image);

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

        }
    }


    // set the data list to the recycler view
    public searchBookResultsRecyclerViewAdapter(ArrayList<Book> data) {
        mData = data;
    }

    // helper method for returning row id of list item clicked
    Book getItem(int id) { return mData.get(id); }

    // getter method to access context of adapter
    Context getContext() {return context;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_search_book_results, parent, false);
        context = parent.getContext();

        return new ViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Book book = mData.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.status.setText(book.getStatus());

        //This section gets the image of the book if it currently exists from the firebase storage service
        StorageReference mReference = FirebaseStorage.getInstance().getReference().child("images/" + book.getBookid());
        mReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher_round)
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

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
