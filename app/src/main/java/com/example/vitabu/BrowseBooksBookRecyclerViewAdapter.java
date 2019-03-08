package com.example.vitabu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

// Used https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example

public class BrowseBooksBookRecyclerViewAdapter extends RecyclerView.Adapter<BrowseBooksBookRecyclerViewAdapter.ViewHolder> {

        private List<Book> mData;
        private LayoutInflater mInflater;
        private ItemClickListener mClickListener;

        // data is passed into the constructor
        BrowseBooksBookRecyclerViewAdapter(Context context, List<Book> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.browse_books_book, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the fields in each row
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Book book = mData.get(position);
            String title = book.getTitle();
            String author = book.getAuthor();
//            Once Book has comments attribute added, get comments from there
            String comments = "Comments go here";//book.getComments();
            holder.title.setText(title);
            holder.author.setText(author);
            holder.comments.setText(comments);
        }

        // Returns total number of rows
        @Override
        public int getItemCount() {
            return mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title, author, comments;

            ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.browse_books_book_title);
                author = itemView.findViewById(R.id.browse_books_book_author);
                comments = itemView.findViewById(R.id.browse_books_book_comments);
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