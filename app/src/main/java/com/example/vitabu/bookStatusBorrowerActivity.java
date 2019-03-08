package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class bookStatusBorrowerActivity extends AppCompatActivity implements bookStatusBorrowerRecyclerViewAdapter.ItemClickListener, AdapterView.OnItemSelectedListener {

    bookStatusBorrowerRecyclerViewAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_status_borrower);

        Spinner spinner = (Spinner) findViewById(R.id.book_status_borrower_spinner);
        Switch onOffSwitch = (Switch) findViewById(R.id.book_status_owner_switch);

        // switch is always true on this activity to enforce that user is in correct tab
        onOffSwitch.setChecked(true);

        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                 Log.i(TAG, "onCheckedChanged: " + isChecked);
                Intent intent = new Intent(bookStatusBorrowerActivity.this, bookStatusOwnerActivity.class);
                startActivity(intent);
            }
        });

        // populate array of books to display
        ArrayList<Book> booksA = new ArrayList<>();

        // would need to instantiate User objects to test this
        Book book;
        for (int i = 0; i < 10; i++) {
            book = new Book();
            book.setTitle("Title");
            book.setAuthor("Author");
//            book.setOwner(); // get user
            booksA.add(book);
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
        recyclerAdapter = new bookStatusBorrowerRecyclerViewAdapter(this, booksA);
        recyclerView.setAdapter(recyclerAdapter);
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
