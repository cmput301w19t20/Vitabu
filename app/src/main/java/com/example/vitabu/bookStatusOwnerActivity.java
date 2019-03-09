package com.example.vitabu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class bookStatusOwnerActivity extends Activity implements bookStatusOwnerRecyclerViewAdapter.ItemClickListener, AdapterView.OnItemSelectedListener{

    bookStatusOwnerRecyclerViewAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_status_owner);
        Spinner spinner = (Spinner) findViewById(R.id.book_status_owner_spinner);
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
        ArrayList<Book> booksA = new ArrayList<>();

        Book book;
        for (int i = 0; i < 10; i++) {
            book = new Book();
            book.setTitle("Title");
            book.setAuthor("Author");
            booksA.add(book);
        }

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
