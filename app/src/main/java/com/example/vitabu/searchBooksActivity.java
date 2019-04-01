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
 * This file is responsible for implementing searching for the books.
 *
 * Author: Jacob Paton
 * Version: 1.1
 * Outstanding Issues: ---
 */

package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class searchBooksActivity extends AppCompatActivity {
    Database database = Database.getInstance();
    private ArrayList<Book> searchResults;
    private String logTag = "Search Books Activity";
    public static final String AUTHOR_SEARCH_MESSAGE = "author";
    public static final String TITLE_SEARCH_MESSAGE = "title";
    public static final String ISBN_SEARCH_MESSAGE = "isbn";
    public static final String KWORDS_SEARCH_MESSAGE = "kwords";
    private String author;
    private String title;
    private String isbn;
    private String kwords;
    private LocalUser curUser;


    //This method is called when the activity is first called. It will enable the user to search for
    //specific books.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);
        // Get the current user from intent.  This activity does not use it directly but it does
        // Pass it to other activities.
        Intent intent = getIntent();
        Gson gson = new Gson();
        String message = intent.getStringExtra(MainActivity.USER_MESSAGE);
        curUser = gson.fromJson(message, LocalUser.class);

    }

    //This method will search for books based on the provided input parameters.
    public void search (View v){
        Log.d(logTag, "In search");
        // Get Info from search fields.
        author = ((EditText) findViewById(R.id.search_books_author_edittext)).getText().toString();
        title = ((EditText) findViewById(R.id.search_books_title_edittext)).getText().toString();
        isbn = ((EditText) findViewById(R.id.search_books_isbn_edittext)).getText().toString();
        kwords = ((EditText) findViewById(R.id.search_books_keywords_edittext)).getText().toString();

        Runnable success = new Runnable() {
            @Override
            public void run() {
                searchResults = database.getSearchBooksReturnValue();
                if (searchResults.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Search returned no results.", Toast.LENGTH_LONG).show();
                    return;
                }
                showSearchResults();
            }
        };

        Runnable fail = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Search returned no results.", Toast.LENGTH_LONG).show();
            }
        };
        // Get search results.
        database.searchBooks(success, fail, author, title, isbn, kwords);
    }


    //This method will show the results of the search query.
    private void showSearchResults(){
        Log.d(logTag, "In search results.");
        for(Book book : searchResults ){
            Log.d(logTag, book.getBookid());
        }

        // Put necessary data into intent and start searchBooksResultsActivity.
        Intent intent = new Intent(this, searchBookResultsActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MainActivity.BOOKLIST_MESSAGE, gson.toJson(searchResults));
        intent.putExtra(MainActivity.USER_MESSAGE, gson.toJson(curUser));
        intent.putExtra(AUTHOR_SEARCH_MESSAGE, author);
        intent.putExtra(TITLE_SEARCH_MESSAGE, title);
        intent.putExtra(ISBN_SEARCH_MESSAGE, isbn);
        intent.putExtra(KWORDS_SEARCH_MESSAGE, kwords);

        startActivity(intent);
    }
}
