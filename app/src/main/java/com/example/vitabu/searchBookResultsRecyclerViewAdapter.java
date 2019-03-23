package com.example.vitabu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class searchBookResultsRecyclerViewAdapter extends RecyclerView.Adapter<searchBookResultsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Book> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    searchBookResultsRecyclerViewAdapter(Context context, ArrayList<Book> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.content_search_book_results, parent, false);
        return new searchBookResultsRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the fields in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = mData.get(position);
        String title = book.getTitle();
        String author = book.getAuthor();
        String status = book.getStatus();

        holder.title.setText(title);
        holder.author.setText(author);
        holder.status.setText(status);
    }

    // Returns total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, author, status;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.search_books_book_title);
            author = itemView.findViewById(R.id.search_books_book_author);
            status = itemView.findViewById(R.id.search_books_book_status);
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
