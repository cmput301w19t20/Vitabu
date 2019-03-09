package com.example.vitabu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class NotificationsFragment extends Fragment implements NotificationsRecyclerViewAdapter.ItemClickListener {
    NotificationsRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_notifications, container, false);

        // data to populate the RecyclerView with
        ArrayList<Notification> notifications = new ArrayList<>();
        Notification notification;
        for (int i = 0; i < 10; i++) {
            notification = new Notification();
            notification.setTitle("Notif" + Integer.toString(i));
            notification.setMessage("This is the message for the notification #" + Integer.toString(i));
            notification.setDate(new Date());
            notifications.add(notification);
        }

        System.out.println(notifications.toString());

        // set up the RecyclerView
        RecyclerView recyclerView = fragmentView.findViewById(R.id.notifications_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new NotificationsRecyclerViewAdapter(this.getActivity(), notifications);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        return fragmentView;
    }

    @Override
    public void onItemClick(View view, int position) {
//        TODO: Opens book info activity
        Toast.makeText(this.getActivity(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

}
