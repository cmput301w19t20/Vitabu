package com.example.vitabu;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class browseBooksActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment requests;
    private Fragment browseBooks;
    private Fragment addBook;
    private Fragment notifications;
    private Fragment ownedBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);

        fragmentManager = getSupportFragmentManager();

        // Initialize fragments
        requests = new bookRequestsFragment();
        browseBooks = new BrowseBooksFragment();
        addBook = new AddBookFragment();
        notifications = new NotificationsFragment();
        ownedBooks = new Fragment();//OwnedBooksFragment();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.browse_books_bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.browse_books_bottom_nav_menu_requests:
                    fragment = requests;
                    break;
                case R.id.browse_books_bottom_nav_menu_browse:
                    fragment = browseBooks;
                    break;
                case R.id.browse_books_bottom_nav_menu_add_book:
                    fragment = addBook;
                    break;
                case R.id.browse_books_bottom_nav_menu_notifications:
                    fragment = notifications;
                    break;
                case R.id.browse_books_bottom_nav_menu_owned_books:
                    fragment = ownedBooks;
                    break;
                default:
                    fragment = browseBooks;
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.browse_books_frame, fragment).commit();
            return true;
        }
    };
}
