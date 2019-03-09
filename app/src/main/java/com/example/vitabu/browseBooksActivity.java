package com.example.vitabu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class browseBooksActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment requests;
    private Fragment browseBooks;
    private Fragment addBook;
    private Fragment notifications;
    private Fragment ownedBooks;
    private LocalUser curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);
        Intent intent = getIntent();
        String message = intent.getStringExtra("IntentJson");
        Gson gson = new Gson();

        IntentJson passed = gson.fromJson(message, IntentJson.class);
        curUser = passed.getUser();

        // Get fragment manager (for switching fragments)
        fragmentManager = getSupportFragmentManager();

        // Initialize fragments
        requests = new bookStatusFragment();
        browseBooks = new BrowseBooksFragment();
        addBook = new AddBookFragment();
        notifications = new NotificationsFragment();
        ownedBooks = new OwnedBooksFragment();

        fragmentManager.beginTransaction().replace(R.id.browse_books_frame, browseBooks).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.browse_books_bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.browse_books_bottom_nav_menu_browse);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browse_books_appbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.browse_books_appbar_search:
                Intent intent = new Intent(this, searchBooksActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //This function sends the onActivityResult(...) call to the fragment where that
    //other activity was called so that it can be processed and dealt with correctly.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    public LocalUser getCurUser() {
        return curUser;
    }
}
