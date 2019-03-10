package com.example.vitabu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

public class bookStatusOwnerActivity extends Activity implements bookStatusOwnerRecyclerViewAdapter.ItemClickListener, AdapterView.OnItemSelectedListener{

    private bookStatusOwnerRecyclerViewAdapter recyclerAdapter;
    private ArrayList<Book> booksA;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_status_owner);
        spinner = (Spinner) findViewById(R.id.book_status_owner_spinner);
        Switch onOffSwitch = (Switch) findViewById(R.id.book_status_to_borrow_switch);

        // switch is always false on this activity to enforce that user is in correct tab
        onOffSwitch.setChecked(false);

        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Log.d(TAG, "onCheckedChanged: " + isChecked);

                Intent intent = new Intent(bookStatusOwnerActivity.this, bookStatusBorrowerActivity.class);
                startActivity(intent);
            }
        });

        // populate array of books to display
        booksA = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userName = firebaseUser.getDisplayName();

        Book book;
        for (int i = 0; i < 10; i++) {
            book = new Book();
            book.setTitle("Title" + Integer.toString(i));
            book.setAuthor("Author");
            myRef.child("books").child(UUID.randomUUID().toString()).setValue(book);
        }

        myRef.child("books").orderByChild("ownerName").equalTo(userName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d("Count ", "" + snapshot.getChildrenCount());
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Book b = postSnapshot.getValue(Book.class);
                            addBook(b);
                        }
                        nextStep();
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {
                    }
                });
    }

    private void addBook(Book b){
        booksA.add(b);
    }

    private void nextStep(){
        // set up the spinner
        String[] filterTypes = {"available", "requested", "accepted", "borrowed"};
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterTypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.book_status_owner_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new bookStatusOwnerRecyclerViewAdapter(this, booksA);
        recyclerAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
//        TODO: Opens book info activity
        Toast.makeText(this, "You clicked " + recyclerAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    // update recycler when when spinner item is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO: filter books according to status selected
        switch (position) {
            case 0:
                //books array with status: available
            case 1:
                //books array with status: requested
            case 2:
                //books array with status: accepted
                break;
            case 3:
                //books array with status: borrowed
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

}
