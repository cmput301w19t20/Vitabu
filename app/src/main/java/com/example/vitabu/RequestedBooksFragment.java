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
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RequestedBooksFragment extends Fragment implements AdapterView.OnItemSelectedListener, RequestedBooksBookRecyclerViewAdapter.ItemClickListener{

    RequestedBooksBookRecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Book> books;
    ArrayList<String> bookIds = new ArrayList<>();
    String logTag = "RequestedBooksFragment";
    FirebaseAuth auth;
    FirebaseUser firebaseuser;
    boolean accepted_filter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DataSnapshot curSnapshot;
    boolean initialDataUpdateNeeded = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        firebaseuser = auth.getCurrentUser();
        View fragmentView = inflater.inflate(R.layout.fragment_requested_books, container, false);

        // Since we have predetermined the items for the drop down status menu,
        // will use a string array containing the status items -- located in the resource file
        Spinner spinner = (Spinner) fragmentView.findViewById(R.id.requested_books_status_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.requested_books_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // data to populate the RecyclerView with
        books = new ArrayList<>();
        // set up the RecyclerView
        final RecyclerView recyclerView = fragmentView.findViewById(R.id.requested_books_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerViewAdapter = new RequestedBooksBookRecyclerViewAdapter(this.getActivity(), books);
        recyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        //Setup Listener for Pulling books from the database.
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        // myRef.orderByChild("ownerName").equalTo(userName).addValueEventListener
        myRef.child("borrowrecords").orderByChild("borrowerName").equalTo(firebaseuser.getDisplayName()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(logTag, "Read Borrow records from database.");
                        curSnapshot = dataSnapshot;
                        if (initialDataUpdateNeeded){
                            updateBorrowRecords();
                            initialDataUpdateNeeded = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Uhh Failed to get borrow records from database...", databaseError.toException());
                    }
                });



        return fragmentView;
    }



    public void updateBorrowRecords(){
        bookIds.clear();
        books.clear();
        recyclerViewAdapter.notifyDataSetChanged();
        if (curSnapshot == null){
            // Data not yet ready.
            return;
        }
        // Get relevant borrow records.
        for (DataSnapshot subSnapshot : curSnapshot.getChildren()) {
            BorrowRecord curBorrowRecord = subSnapshot.getValue(BorrowRecord.class);
            Log.d(logTag, "Borrow record: " + curBorrowRecord.getRecordid() + " Status: " + curBorrowRecord.isApproved());
            if (curBorrowRecord.isApproved() == accepted_filter){
                bookIds.add(curBorrowRecord.getBookid());
            }

        }

        // get relevant books from database, and append them
        for (String bookid: bookIds){
            myRef.child("books").child(bookid).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                addBookToList(dataSnapshot.getValue(Book.class));
                            }
                            else{
                                Log.d(logTag, "Uhh Tried to fetch book from database that does not exist...");
                            }
                            recyclerViewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(logTag, "Uhh Failed to get books from database...", databaseError.toException());
                        }
                    });
        }

    }

    private void addBookToList(Book book){
        books.add(book);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(logTag, "In ONItemSelected: Position = " + position + " ID = " + id);
        if (id == 1){
            accepted_filter = true;
        }
        if (id == 0) {
            accepted_filter = false;
        }
        updateBorrowRecords();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing for now ..
    }

    @Override
    public void onItemClick(View view, int position) {
        // Get Book that was clicked.
        final Book clickedBook = books.get(position);
        LocalUser curUser = ((browseBooksActivity) getActivity()).getCurUser();

        // Call book info activity.
        Intent intent = new Intent(this.getContext(), bookInfoActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MainActivity.LOCALUSER_MESSAGE, gson.toJson(curUser));
        intent.putExtra((MainActivity.BOOK_MESSAGE), gson.toJson(clickedBook));
        startActivity(intent);

    }
}
