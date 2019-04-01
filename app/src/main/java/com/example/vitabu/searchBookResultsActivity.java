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
 * This file handles the logic and UI of displaying the search results on the screen.
 *
 * Author: Tristan Carlson
 * Version: 1.0
 * Outstanding Issues: ---
 */

package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class searchBookResultsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private searchBookResultsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Book> bookResults; // container for search results
    private String logTag = "SearchBookResultsActivity";
    private String author;
    private String title;
    private String isbn;
    private String kwords;
    private LocalUser curUser;

    //This method is used when the activity starts up for the first time. It will display the search
    //results in the recycler view.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book_results);

        // Get search results, LocalUser, and search terms from intent.
        Intent intent = getIntent();
        String bookResultsMessage = intent.getStringExtra(MainActivity.BOOKLIST_MESSAGE);
        Gson gson = new Gson();
        // Used https://stackoverflow.com/questions/5374546/passing-arraylist-through-intent
        // To find how to retrieve arraylist of arbitrary type from intent.
        Type type = new TypeToken<ArrayList<Book>>(){}.getType();
        bookResults = gson.fromJson(bookResultsMessage, type);
        String curUserMessage = intent.getStringExtra(MainActivity.USER_MESSAGE);
        curUser = gson.fromJson(curUserMessage, LocalUser.class);
        author = intent.getStringExtra(searchBooksActivity.AUTHOR_SEARCH_MESSAGE);
        title = intent.getStringExtra(searchBooksActivity.TITLE_SEARCH_MESSAGE);
        isbn = intent.getStringExtra(searchBooksActivity.ISBN_SEARCH_MESSAGE);
        kwords = intent.getStringExtra(searchBooksActivity.KWORDS_SEARCH_MESSAGE);

        for(Book book : bookResults){
            Log.d(logTag, "Pulled book '" + book.getTitle() + "' From intent!");
        }

        // set up textviews
        TextView inputTitle = (TextView) findViewById(R.id.search_results_title);
        TextView inputAuthor = (TextView) findViewById(R.id.search_results_author);
        TextView inputISBN = (TextView) findViewById(R.id.search_results_isbn);
        TextView inputKeywords = (TextView) findViewById(R.id.search_results_keywords);

        // populate fields with data passed through intent from searchBooksActivity
        inputTitle.setText(title);
        inputAuthor.setText(author);
        inputISBN.setText(isbn);
        inputKeywords.setText(kwords);

        buildRecyclerView();
    }


    //This method builds the actual recycler view that will display the resulting books.
    public void buildRecyclerView() {
        // set up the recycler view
        mRecyclerView = findViewById(R.id.search_results_list);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new searchBookResultsRecyclerViewAdapter(bookResults);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // on item click, go to book info
        mAdapter.setOnItemClickListener(new searchBookResultsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(getApplicationContext(), bookInfoActivity.class);
                Log.d("launch", mAdapter.getItem(position).getTitle());
                Gson gson = new Gson();
                String bookMessage = gson.toJson(mAdapter.getItem(position));
                intent.putExtra(MainActivity.BOOK_MESSAGE, bookMessage);
                String LocalUserMessage = gson.toJson(curUser);
                intent.putExtra(MainActivity.LOCALUSER_MESSAGE, LocalUserMessage);
                startActivity(intent);
                }
        });
    }


}
