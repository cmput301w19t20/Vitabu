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
 * This file contains the fragment that has the logic and UI for displaying and dealing with browsing
 * all the books that have not yet been accepted or borrowed.
 *
 * Author: Jacob Paton
 * Version: 1.5
 * Outstanding Issues: ---
 */
package com.example.vitabu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


import java.util.ArrayList;

public class BrowseBooksFragment extends Fragment implements BrowseBooksBookRecyclerViewAdapter.ItemClickListener {
    private BrowseBooksBookRecyclerViewAdapter adapter;
    private ArrayList<Book> books;
    private LocalUser curUser;
    FirebaseAuth auth;
    String logTag = "Browse Books Fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();

        // Inflate layout
        View fragmentView = inflater.inflate(R.layout.fragment_browse_books, container, false);

        // Get current user from Activity
        curUser = ((browseBooksActivity) getActivity()).getCurUser();

        // Generate data to populate the RecyclerView with
        books = new ArrayList<>();
//        Book book;
//        ArrayList<Book> books = new ArrayList<>();
//        Book book;
//        for (int i = 0; i < 10; i++) {
//            book = new Book("Title" + Integer.toString(i), "Author", "1234", "available", "owen", "description", "bookid");
//            books.add(book);
//        }

        // Set up the RecyclerView
        RecyclerView recyclerView = fragmentView.findViewById(R.id.browse_books_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new BrowseBooksBookRecyclerViewAdapter(this.getActivity(), books);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        String userName = auth.getCurrentUser().getDisplayName();
        // Get data from the database.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("books");
        // myRef.orderByChild("ownerName").equalTo(userName).addValueEventListener
        myRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        updateBooks(dataSnapshot);
                        Log.d(logTag, "Read Books from database.");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Uhh Failed to get books from database...", databaseError.toException());
                    }
        });


        return fragmentView;
    }

    public void updateBooks(DataSnapshot snapshot){
        Book curBook;
        int curBookIndex;
        books.clear();
        Log.d(logTag, "Got Books snapshot!");
        for (DataSnapshot subSnapshot: snapshot.getChildren()){
            curBook = subSnapshot.getValue(Book.class);
            if (curBook.getOwnerName().equals(curUser.getUserName()) || (! curBook.getStatus().equals("available") && ! curBook.getStatus().equals("requested"))){
                continue;
            }
            curBookIndex = books.indexOf(curBook);
            // Check if book is already in arraylist.  If it is update it, else add it.
            if ( curBookIndex == -1 ){
                books.add(curBook);
            }
            else{
                books.add(curBookIndex, curBook);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
//        TODO: Opens book info activity

        Intent intent = new Intent(this.getContext(), bookInfoActivity.class);
        Log.d("fragment launch", adapter.getItem(position).getTitle());
        Gson gson = new Gson();
        String bookMessage = gson.toJson(adapter.getItem(position));
        intent.putExtra(MainActivity.BOOK_MESSAGE, bookMessage);
        String LocalUserMessage = gson.toJson(curUser);
        intent.putExtra(MainActivity.LOCALUSER_MESSAGE, LocalUserMessage);
        startActivity(intent);

        Toast.makeText(this.getActivity(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
