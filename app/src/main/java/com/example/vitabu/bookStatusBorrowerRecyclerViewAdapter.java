package com.example.vitabu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class bookStatusBorrowerRecyclerViewAdapter extends RecyclerView.Adapter<bookStatusBorrowerRecyclerViewAdapter.ViewHolder> {

    private List<Book> mData;
    private LayoutInflater mInflater;
    private bookStatusBorrowerRecyclerViewAdapter.ItemClickListener mClickListener;



    // data is passed into the constructor
    bookStatusBorrowerRecyclerViewAdapter(Context context, List<Book> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the layout from xml when needed
    @Override
    public bookStatusBorrowerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.content_book_status_borrower, parent, false);
        return new bookStatusBorrowerRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the fields in each row
    @Override
    public void onBindViewHolder(bookStatusBorrowerRecyclerViewAdapter.ViewHolder holder, int position) {
        Book book = mData.get(position);
        String title = book.getTitle();
        String author = book.getAuthor();

        String ownerName = book.getOwnerName();

 master
        String comments = "Comments go here"; // access comment from Book class when available
        holder.title.setText(title);
        holder.author.setText(author);
        holder.comments.setText(comments);

        holder.owner.setText(ownerName);

master
    }

    // Returns total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    Book getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(bookStatusBorrowerRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, author, comments, owner;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_status_borrower_book_title);
            author = itemView.findViewById(R.id.book_status_borrower_book_author);
            comments = itemView.findViewById(R.id.book_status_borrower_book_comment);
            owner = itemView.findViewById(R.id.book_status_borrower_book_owner);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
