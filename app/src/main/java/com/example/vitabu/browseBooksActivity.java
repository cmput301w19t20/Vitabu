/*
 * This file contains the activity that the user reaches after successfully authenticating. It also
 * initializes all the fragments for the screens that are located in this activity. This activity also
 * has a bottom navigation bar that lets the user move between the different fragments.
 *
 * Author: Jacob Paton
 * Version: 1.2
 * Outstanding Issues: ---
 */
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * This class contains all the fragments and the bottom navigation bar that creates the UI. The bottom
 * navigation bar lets the user go between the different fragments in the UI.
 *
 * @author Jacob Paton
 * @version 1.2
 * @see AddBookFragment
 * @see bookRequestsFragment
 * @see bookStatusFragment
 * @see BrowseBooksFragment
 * @see OwnedBooksFragment
 * @see NotificationsFragment
 */
public class browseBooksActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment requests;
    private Fragment browseBooks;
    private Fragment addBook;
    private Fragment notifications;
    private Fragment ownedBooks;
    private LocalUser curUser;
    private String message;

    /**
     * This method is called when this activity gets called initially. It gets the information about
     * the user that is currently using the app from the authentication activity. This method also
     * initializes all the fragments used in the activity.
     *
     * @param savedInstanceState keeps track of the state that the screen was in when it was left.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);

        Intent intent = getIntent();
        message = intent.getStringExtra("LocalUser");
        Gson gson = new Gson();
        curUser = gson.fromJson(message, LocalUser.class);
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
//        Gson gson = new Gson();
//        IntentJson passed = (IntentJson) gson.fromJson(message, IntentJson.class);
//        curUser = (LocalUser) passed.getUser();
//>>>>>>> c604f0d0db2e55b7e0521a3669ce754fefe12931

        // Get fragment manager (for switching fragments)
        fragmentManager = getSupportFragmentManager();

        // Initialize fragments
        requests = new RequestedBooksFragment();
        browseBooks = new BrowseBooksFragment();
        addBook = new AddBookFragment();
        notifications = new NotificationsFragment();
        ownedBooks = new OwnedBooksFragment();

        fragmentManager.beginTransaction().replace(R.id.browse_books_frame, browseBooks).commit();

        //Initializes the Bottom Navigation Bar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.browse_books_bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.browse_books_bottom_nav_menu_browse);
    }

    /**
     * This method creates the menu (an app bar) in the top right of the screen.
     *
     * @param menu the menu that needs to be created.
     * @return true if the options menu was created successfully and false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browse_books_appbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method will go to the activity that is selected from the menu (app bar).
     *
     * @param item the selected item in the app bar
     * @return returns true if an item was clicked successfully and false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.browse_books_appbar_search:
                Intent searchBooksIntent = new Intent(this, searchBooksActivity.class);
                searchBooksIntent.putExtra(MainActivity.USER_MESSAGE, message);
                startActivity(searchBooksIntent);
                break;
            case R.id.browse_books_appbar_profile:
                Intent editProfileIntent = new Intent(this, editProfileActivity.class);
                editProfileIntent.putExtra(MainActivity.USER_MESSAGE, message);
                startActivity(editProfileIntent);
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //This function sends the onActivityResult(...) call to the fragment where that
    //other activity was called so that it can be processed and dealt with correctly.

    /**
     * This function sends the onActivityResult(...) call to the fragment from which that activity
     * was called so that the activity result can be processed and dealt with correctly.
     *
     * @param requestCode the request code with which the activity that just returned was called.
     * @param resultCode the result code of the activity (either a RESULT_OK or something else)
     * @param data the intent that contains the data that was passed back successfully.
     * @see AddBookFragment
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * This listener enables the navigation between fragments using the bottom navigation bar.
     */
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
