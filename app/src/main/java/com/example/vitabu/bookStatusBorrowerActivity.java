/*
 * This file was originally intended to hold the books and sort them by their status with a spinner
 * before we switched to using fragments. See bookStatusFragment for completed version.
 *
 * THIS FILE IS DEPRECATED!
 */
package com.example.vitabu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class bookStatusBorrowerActivity extends AppCompatActivity implements bookStatusBorrowerRecyclerViewAdapter.ItemClickListener, AdapterView.OnItemSelectedListener {

    bookStatusBorrowerRecyclerViewAdapter recyclerAdapter;
    String bookid;
    User owner;
    LocalUser curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_status_borrower);

        Spinner spinner = (Spinner) findViewById(R.id.book_status_borrower_spinner);
        Switch onOffSwitch = (Switch) findViewById(R.id.book_status_owner_switch);

        // Get intent.

        // switch is always true on this activity to enforce that user is in correct tab
        onOffSwitch.setChecked(true);

        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                 Log.d(TAG, "onCheckedChanged: " + isChecked);
                Intent intent = new Intent(bookStatusBorrowerActivity.this, bookStatusOwnerActivity.class);
//                IntentJson passing = new IntentJson(curUser);
//                passing.addObject(owner);
//                String message = passing.toJson();
//                intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
//                startActivity(intent);

                Gson gson = new Gson();
                String curUserMessage = gson.toJson(curUser);
                String ownerMessage = gson.toJson(owner);
                intent.putExtra(MainActivity.LOCALUSER_MESSAGE, curUserMessage);
                intent.putExtra(MainActivity.USER_MESSAGE, ownerMessage);
                startActivity(intent);
            }
        });

        ArrayList<Book> books = new ArrayList<>();

        Book book;
        for (int i = 0; i < 10; i++) {
            book = new Book();
            book.setTitle("Title");
            book.setAuthor("Author");
            books.add(book);
        }

        // set up the spinner
        String[] filterTypes = {"requested", "accepted", "borrowed"};
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterTypes);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.book_status_borrower_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new bookStatusBorrowerRecyclerViewAdapter(this, books);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
//        TODO: Opens book info activity
        Toast.makeText(this, "You clicked " + recyclerAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    private void setOwner(User owner){
        this.owner = owner;
    }

    // update recycler when when spinner item is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                //books array with status: available
                break;
            case 1:
                //books array with status: requested
                break;
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
