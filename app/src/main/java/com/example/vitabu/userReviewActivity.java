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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review);

        // get user object from intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.USER_MESSAGE);
        Gson gson = new Gson();
        user = gson.fromJson(message, User.class);
        getReviewList();

        //Review t = new Review("Owner name", "borrow name", 10, "This is a generic review");
        //reviewList.add(t);

        //create recycler view
        recyclerView = (RecyclerView) findViewById(R.id.user_review_recyclerView);     // capture recycler view
        LayoutManager = new LinearLayoutManager(this);                          // use linear layout and set it
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new CustomAdapter(reviewList);                                        // create adapter
        recyclerView.setAdapter(adapter);                                               // set adapter to view
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        // show no data textView if array list is empty
        emptyText = (TextView) findViewById(R.id.user_review_no_data);
        emptyText.setVisibility(View.VISIBLE);
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
            name.setText(currentReview.getBorrowerName());
            date.setText(currentReview.getDate().toString());
            rating.setText(Integer.toString(currentReview.getRating()));
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
            reviewList.add(review);
            if (emptyText != null) {
                emptyText.setVisibility(View.GONE);
            }
        }
        adapter.notifyDataSetChanged();
    }

}
