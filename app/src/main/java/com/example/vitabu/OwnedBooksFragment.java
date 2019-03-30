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
 * This file contains the fragment that has the logic and UI of showing the books owned by the user.
 *
 * Author: Jacob Paton
 * Version: 1.2
 * Outstanding Issues: ---
 */

package com.example.vitabu;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class OwnedBooksFragment extends Fragment implements AdapterView.OnItemSelectedListener, OwnedBooksBookRecyclerViewAdapter.ItemClickListener {
    private OwnedBooksBookRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Book> books;
    private ArrayList<String> bookids;
    private String userName;
    private RecyclerView recyclerView;
    private Database database;
    private ValueEventListener firstListener;
    private Query firstQuery;
    private ValueEventListener secondListener;
    private Query secondQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_owned_books, container, false);

        // Since we have predetermined the items for the drop down status menu,
        // will use a string array containing the status items -- located in the resource file
        Spinner spinner = (Spinner) fragmentView.findViewById(R.id.owned_books_status_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.owned_books_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        books = new ArrayList<>();
        bookids = new ArrayList<>();
      
        // set up the RecyclerView
        recyclerView = fragmentView.findViewById(R.id.owned_books_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerViewAdapter = new OwnedBooksBookRecyclerViewAdapter(this.getActivity(), books);
        recyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewAdapter.notifyDataSetChanged();

        database = Database.getInstance();
        userName = database.getCurUserName();

        // pull all books that user owns
        firstQuery = database.getRootReference().child("books").orderByChild("ownerName").equalTo(userName);

        //Create the ValueEventListener
        firstListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                books.clear();
                Log.d("Count1 ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Book b = postSnapshot.getValue(Book.class);
                    books.add(b);
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError e) {
            }
        };


        secondQuery = database.getRootReference().child("borrowrecords").orderByChild("borrowerName").equalTo(userName);
        secondListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("Count2 ", "" + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if((boolean)postSnapshot.child("approved").getValue()) {
                        String id = (String) postSnapshot.child("bookid").getValue();
                        database.getRootReference().child("books").child(id).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Book b = dataSnapshot.getValue(Book.class);
//                                                String bid = b.getBookid();
//                                                ArrayList<Book> copy = new ArrayList<>();
//                                                for (Book book : books) {
//                                                    if (book.getBookid().equals(bid)) {
//                                                        copy.add(book);
//                                                    }
//                                                }
//                                                for (Book book : copy) {
//                                                    books.remove(book);
//                                                }
                                        books.add(b);
                                        recyclerViewAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                }
                        );
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError e) {
            }
        };

        return fragmentView;
    }

    @Override
    public void onStart(){
        super.onStart();
        firstQuery.addValueEventListener(firstListener);
        secondQuery.addValueEventListener(secondListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        firstQuery.removeEventListener(firstListener);
        secondQuery.removeEventListener(secondListener);
    }

    private void newAdapter(){
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            recyclerViewAdapter = new OwnedBooksBookRecyclerViewAdapter(this.getActivity(), books);
            recyclerViewAdapter.setClickListener(this);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        } catch(Exception e){
            Log.d("Adapter error", e.getMessage());
        }
    }

    private void removeBookid(String id){
        bookids.remove(id);
        if(bookids.size() == 0){
            orderBy("available");
        }
    }

    private void addBookid(String id){
        bookids.add(id);
    }

    private void addBook(Book b){
        books.add(b);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                orderBy("available");
                break;
            case 1:
                orderBy("requested");
                break;
            case 2:
                orderBy("accepted");
                break;
            case 3:
                orderBy("borrowed");
                break;
            case 4:
                orderBy("borrowing");
                break;
        }
    }

    private void orderBy(String status){
        ArrayList<Book> temp = new ArrayList<>();
        if(status.equals("borrowing")) {
            for (Book b : books) {
                if (!b.getOwnerName().equals(userName) && b.getStatus().equals("borrowed")) {
                    temp.add(b);
                }
            }
            for (Book b : books) {
                if (b.getOwnerName().equals(userName) || !b.getStatus().equals("borrowed")) {
                    temp.add(b);
                }
            }
        }else{
            for (Book b : books) {
                if (b.getStatus().equals(status)) {
                    temp.add(b);
                }
            }
            for (Book b : books) {
                if (!b.getStatus().equals(status)) {
                    temp.add(b);
                }
            }

        }
        books = temp;
        newAdapter();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing for now...
    }

    @Override
    public void onItemClick(View view, int position) {
        Book book = recyclerViewAdapter.getItem(position);
        Gson gson = new Gson();
        Intent intent = new Intent();
        if(book.getStatus().equals("available")) {
            intent = new Intent(this.getContext(), bookEditActivity.class);
            String message = gson.toJson(book);
            intent.putExtra(MainActivity.BOOK_MESSAGE, message);
        }else if(book.getStatus().equals("requested")){
            intent = new Intent(this.getContext(), acceptBookRequestActivity.class);
            String message = gson.toJson(book);
            intent.putExtra(MainActivity.BOOK_MESSAGE, message);
        }else if(book.getStatus().equals("accepted")) {
            //TODO: change activity to the new tradeoff activity
            intent = new Intent(this.getContext(), acceptBookActivity.class);
            String message = gson.toJson(book);
            intent.putExtra(MainActivity.BOOK_MESSAGE, message);
        }else if(book.getStatus().equals("borrowed")) {
            intent = new Intent(this.getContext(), returnBookActivity.class);
            String message = gson.toJson(book);
            intent.putExtra(MainActivity.BOOK_MESSAGE, message);
        }
        startActivity(intent);
    }
}
