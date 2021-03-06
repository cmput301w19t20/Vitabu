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
 * This file contains the user reviews activity which displays all the reviews pertaining to the user.
 *
 * Author: Ayooluwa Oladosu
 * Version: 1.2
 * Outstanding Issues: ---
 */

package com.example.vitabu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class userReviewActivity extends AppCompatActivity {

    // recycler view variables
    private ArrayList<Review> reviewList = new ArrayList<Review>();
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private RecyclerView.LayoutManager LayoutManager;
    private TextView emptyText;
    User user;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review);

        // get user object from intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.USER_MESSAGE);
        Gson gson = new Gson();
        user = gson.fromJson(message, User.class);
        type = intent.getStringExtra(MainActivity.REVIEW_TYPE);
        getReviewList();

        //create recycler view
        recyclerView = (RecyclerView) findViewById(R.id.user_review_recyclerView);     // capture recycler view
        LayoutManager = new LinearLayoutManager(this);                          // use linear layout and set it
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new CustomAdapter(reviewList);                                        // create adapter
        recyclerView.setAdapter(adapter);                                               // set adapter to view
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // show no data textView if array list is empty
        emptyText = (TextView) findViewById(R.id.user_review_no_data);
        if (reviewList.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    // class that creates the array adapter to display reviews
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        private final ArrayList<Review> reviews;       // list with reviews data

        public CustomAdapter(ArrayList<Review> listData){
            // initialize data
            reviews = listData;
        }

        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            // creates new views for layout manager and bind the click listeners
            View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
            return new CustomAdapter.ViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder viewHolder, int position){
            // fills the view with the correct content

            Review currentReview = reviews.get(position);

            // get hold of textviews
            TextView name = viewHolder.nameDisplay;
            TextView date = viewHolder.dateDisplay;
            TextView rating = viewHolder.ratingDisplay;
            TextView review = viewHolder.reviewDisplay;
            RelativeLayout list = viewHolder.list;

            // set text in textviews
            name.setText(currentReview.getReviewFrom());
            date.setText(currentReview.getDate().toString());
            rating.setText(Float.toString(currentReview.getRating()));
            review.setText(currentReview.getBody());

        }

        @Override
        public int getItemCount(){
            // get the number of items in the list
            return reviews.size();
        }

        public void addReview(Review review){
            // add a measurement to the list
            reviews.add(review);
        }

        public void deleteReview(int position){
            // remove a measurement from the list
            reviews.remove(position);
        }

        public Review getReview(int position){
            // get a measurement from the list
            return reviews.get(position);
        }
        public class ViewHolder extends RecyclerView.ViewHolder{
            // store views for adapter
            public TextView nameDisplay;
            public TextView dateDisplay;
            public TextView ratingDisplay;
            public TextView reviewDisplay;
            public RelativeLayout list;

            public ViewHolder(View layout){
                super(layout);
                nameDisplay = (TextView) layout.findViewById(R.id.review_item_borrower_name);
                dateDisplay = (TextView) layout.findViewById(R.id.review_item_date);
                ratingDisplay = (TextView) layout.findViewById(R.id.review_item_rating);
                reviewDisplay = (TextView) layout.findViewById(R.id.review_item_review);
                list = (RelativeLayout) layout.findViewById(R.id.review_item_layout);
            }
        }
    }

    private void getReviewList() {
        // query database to get reviews

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("reviews");

        // listener for reviews
        myRef.orderByChild("reviewTo").equalTo(user.getUserName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateReviews(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateReviews(DataSnapshot dataSnapshot){

        for (DataSnapshot subSnapshot: dataSnapshot.getChildren()){
            Review review = subSnapshot.getValue(Review.class);
            String userName = user.getUserName();

            if (type.equals("owner") && userName.equals(review.getOwnerName())) {
                // get only owner reviews
                reviewList.add(review);
                if (emptyText != null) {
                    emptyText.setVisibility(View.GONE);
                }
            }

            if (type.equals("borrower") && userName.equals(review.getBorrowerName())) {
                // get only borrower reviews
                reviewList.add(review);
                if (emptyText != null) {
                    emptyText.setVisibility(View.GONE);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}
