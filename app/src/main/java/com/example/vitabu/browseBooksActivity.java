package com.example.vitabu;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class browseBooksActivity extends AppCompatActivity implements BrowseBooksBookRecyclerViewAdapter.ItemClickListener {
    BrowseBooksBookRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.browse_books_bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // data to populate the RecyclerView with
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("Book1", "Author1", 1234,"available"));
        books.add(new Book("Book2", "Author2", 1235,"available"));
        books.add(new Book("Book3", "Author3", 1236,"available"));
        books.add(new Book("Book4", "Author4", 1237,"available"));
        books.add(new Book("Book5", "Author5", 1238,"available"));
        books.add(new Book("Book6", "Author6", 1239,"available"));
        books.add(new Book("Book7", "Author7", 1241,"available"));

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.browse_books_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BrowseBooksBookRecyclerViewAdapter(this, books);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            TODO: Implement nav bar behaviour for tab switching
            switch (item.getItemId()) {
                case R.id.browse_books_bottom_nav_menu_requests:
                    return true;
                case R.id.browse_books_bottom_nav_menu_browse:
                    return true;
                case R.id.browse_books_bottom_nav_menu_add_book:
                    return true;
                case R.id.browse_books_bottom_nav_menu_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onItemClick(View view, int position) {
//        TODO: Opens book info activity
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
