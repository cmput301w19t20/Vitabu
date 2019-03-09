package com.example.vitabu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class NotificationsFragment extends Fragment implements NotificationsRecyclerViewAdapter.ItemClickListener {
    NotificationsRecyclerViewAdapter adapter;
    ArrayList<Notification> notifications;
    private boolean wait = true;

    private void addNotification(Notification n){
        notifications.add(n);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_notifications, container, false);

        // data to populate the RecyclerView with
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        LocalUser user = new LocalUser(firebaseUser);
        final String userName = user.getUserName();
        notifications = new ArrayList<>();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        /*for (int i = 0; i < 5; i++) {
            Notification notification = new Notification("Notif USERNAME" + Integer.toString(i),
                                                        "New message #" + Integer.toString(i),
                                                        "TYPE",
                                                        "owen");
            myRef.child("notifications").child(UUID.randomUUID().toString()).setValue(notification);
        }

        for (int i = 0; i < 5; i++) {
            Notification notification = new Notification("Notif NONE" + Integer.toString(i),
                    "New message #" + Integer.toString(i),
                    "TYPE",
                    "notarealusername");
            myRef.child("notifications").child(UUID.randomUUID().toString()).setValue(notification);
        }*/

        myRef.child("notifications").addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Notification n = postSnapshot.getValue(Notification.class);
                    if(userName == n.getUserName()) {
                        addNotification(n);
                    }
                    //notifications.add(n);
                }
                nextStep(fragmentView);
            }
            @Override
            public void onCancelled(DatabaseError e) {
            }
        });

        return fragmentView;
    }

    private void nextStep(View fragmentView){
        System.out.println(notifications.toString());

        // set up the RecyclerView
        RecyclerView recyclerView = fragmentView.findViewById(R.id.notifications_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new NotificationsRecyclerViewAdapter(this.getActivity(), notifications);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onItemClick(View view, int position) {
//        TODO: Opens book info activity
        Toast.makeText(this.getActivity(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

}
