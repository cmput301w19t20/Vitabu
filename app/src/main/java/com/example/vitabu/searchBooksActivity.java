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
 * This file is responsible for implementing searching for the books. This is not a requirement that
 * we are fulfilling in Part 4, therefore it is not implemented yet.
 *
 * Author: Jacob Paton
 * Version: 1.0
 * Outstanding Issues: It is not implemented at all yet, we should get on that.
 */

package com.example.vitabu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class searchBooksActivity extends AppCompatActivity {
    Database database = Database.getInstance();
    private ArrayList<Book> searchResults;
    private String logTag = "Search Books Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);
    }


    public void search (View v){
        Log.d(logTag, "In search");
        // Get Info from search fields.
        String author = ((EditText) findViewById(R.id.search_books_author_edittext)).getText().toString();
        String title = ((EditText) findViewById(R.id.search_books_title_edittext)).getText().toString();
        String isbn = ((EditText) findViewById(R.id.search_books_isbn_edittext)).getText().toString();
        String kwords = ((EditText) findViewById(R.id.search_books_keywords_edittext)).getText().toString();

        Runnable success = new Runnable() {
            @Override
            public void run() {
                searchResults = database.getSearchBooksReturnValue();
                showSearchResults();
            }
        };

        Runnable fail = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Search failed.", Toast.LENGTH_LONG).show();
            }
        };
        // Get search results.
        database.searchBooks(success, fail, author, title, isbn, kwords);
    }

    private void showSearchResults(){
        Log.d(logTag, "In search results.");
        for(Book book : searchResults ){
            Log.d(logTag, book.getBookid());
        }
    }
}
