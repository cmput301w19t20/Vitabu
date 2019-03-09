package com.example.vitabu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NotificationsRecyclerViewAdapter extends RecyclerView.Adapter<NotificationsRecyclerViewAdapter.ViewHolder> {
    private List<Notification> mData;
    private LayoutInflater mInflater;
    private NotificationsRecyclerViewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    NotificationsRecyclerViewAdapter(Context context, List<Notification> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.notifications_notification, parent, false);
        return new NotificationsRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the fields in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notification notification = mData.get(position);
        holder.title.setText(notification.getTitle());
        holder.message.setText(notification.getMessage());
        holder.date.setText(notification.getDate().toString());
    }

    // Returns total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, message, date;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notifications_notification_title);
            message = itemView.findViewById(R.id.notifications_notification_message);
            date = itemView.findViewById(R.id.notifications_notification_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Notification getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(NotificationsRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
