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
 * This file contains the fragment that has the logic and UI of showing the notifications to the user.
 *
 * Author: Jacob Paton
 * Version: 1.2
 * Outstanding Issues: Implement the requests listing and accepting.
 */

package com.example.vitabu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class NotificationsFragment extends Fragment implements NotificationsRecyclerViewAdapter.ItemClickListener {
    NotificationsRecyclerViewAdapter adapter;
    ArrayList<Notification> notifications;
    private boolean wait = true;
    private TextView emptyText;
    private boolean onCreate;

    private void addNotification(Notification n){
        notifications.add(n);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_notifications, container, false);
        onCreate = true;
        // data to populate the RecyclerView with
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        final String userName = firebaseUser.getDisplayName();
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

        myRef.child("notifications").orderByChild("userName").equalTo(userName).addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(onCreate) {
                    Log.d("Count ", "" + snapshot.getChildrenCount());
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Notification n = postSnapshot.getValue(Notification.class);
                        if (!n.isSeen()) {
                            addNotification(n);
                        }
                        if (emptyText != null) {
                            emptyText.setVisibility(View.GONE);
                        }
                    }
                    onCreate = false;
                    nextStep(fragmentView);
                }
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
        adapter.notifyDataSetChanged();

        // show no data textView if array list is empty
        emptyText = (TextView) fragmentView.findViewById(R.id.notification_no_data);
        if (notifications.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Notification curNotification = notifications.get(position);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        if (curNotification.getType().equals("request")) {
            myRef.child("borrowrecords").child(curNotification.getBorrowRecordId()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            getBook(dataSnapshot.getValue(BorrowRecord.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    }
            );
        }
        if (curNotification.getType().equals("acccept")){
            myRef.child("borrowrecords").child(curNotification.getBorrowRecordId()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            startViewMeetingLocationActivity(dataSnapshot.getValue(BorrowRecord.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    }
            );
        }
        if (curNotification.getType().equals("review")){
            myRef.child("borrowrecords").child(curNotification.getBorrowRecordId()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            startReviewActivity(dataSnapshot.getValue(BorrowRecord.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    }
            );
        }


    }

    public  void startViewMeetingLocationActivity(BorrowRecord borrowRecord){
        // TODO Launch viewMeetingLocationActivity.
//        Intent intent = new Intent(this, viewMeetingLocationActivity.class);
//        Gson gson = new Gson();
//        intent.putExtra(MainActivity.BOOK_MESSAGE, gson.toJson(book));
//        startActivity(intent);
    }

    public void getBook(BorrowRecord borrowRecord){
//        Book book = MainActivity.getBookFromDatabase(borrowRecord.getBookid());
        // Get Book fom database.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("books").child(borrowRecord.getBookid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        startAcceptBookRequestActivity(dataSnapshot.getValue(Book.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                }
        );

    }

    public void startAcceptBookRequestActivity(Book book){
        // TODO launch acceptBookRequestActivity.
        Intent intent = new Intent(this.getContext(), acceptBookRequestActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MainActivity.BOOK_MESSAGE, gson.toJson(book));
        startActivity(intent);
    }

    public void startReviewActivity(BorrowRecord rec) {
        Intent intent = new Intent(getActivity(), WriteReviewActivity.class);
        Gson gson = new Gson();
        String message = gson.toJson(rec);
        intent.putExtra(MainActivity.BORROWRECORD_MESSAGE, message);
        startActivity(intent);
    }

}
