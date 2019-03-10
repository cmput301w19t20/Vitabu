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
import java.util.UUID;

public class OwnedBooksFragment extends Fragment implements AdapterView.OnItemSelectedListener, OwnedBooksBookRecyclerViewAdapter.ItemClickListener {
    OwnedBooksBookRecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Book> books;
    private ArrayList<String> bookids;
    private View fragmentView;
    private String userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_owned_books, container, false);

        // Since we have predetermined the items for the drop down status menu,
        // will use a string array containing the status items -- located in the resource file
        Spinner spinner = (Spinner) fragmentView.findViewById(R.id.owned_books_status_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.owned_books_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // data to populate the RecyclerView with
        books = new ArrayList<>();
        bookids = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        userName = firebaseUser.getDisplayName();

        Book book;
        for (int i = 0; i < 10; i++) {
            book = new Book("Title" + Integer.toString(i), "Author", "1234", "available", "owen", "description");
            book.setBookid(UUID.randomUUID().toString());
            books.add(book);
        }

        myRef.child("books").orderByChild("ownerName").equalTo(userName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d("Count ", "" + snapshot.getChildrenCount());
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Book b = postSnapshot.getValue(Book.class);
                            addBook(b);
                        }
                        nextStep1();
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {
                    }
                });

        return fragmentView;
    }

    private void nextStep1(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("transactions").orderByChild("borrowerName").equalTo(userName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d("Count ", "" + snapshot.getChildrenCount());
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            addBookid((String)postSnapshot.child("bookid").getValue());
                        }
                        nextStep2();
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {
                    }
                });
    }

    private void nextStep2(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        for(String id: bookids) {
            try{
                myRef.child("books").child(id).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Book b = dataSnapshot.getValue(Book.class);
                            addBook(b);
                            removeBookid(b.getBookid());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }
                );
            }catch(Exception e){
                Log.d("OWNED_BOOKS_FRAGMENT", "Missing bookid " + e.getMessage());
            }
        }
    }

    private void removeBookid(String id){
        bookids.remove(id);
        if(bookids.size() == 0){
            nextStep3();
        }
    }

    private void addBookid(String id){
        bookids.add(id);
    }

    private void nextStep3(){
        // set up the RecyclerView
        RecyclerView recyclerView = fragmentView.findViewById(R.id.owned_books_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerViewAdapter = new OwnedBooksBookRecyclerViewAdapter(this.getActivity(), books);
        recyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void addBook(Book b){
        books.add(b);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO: filter books according to status selected
        switch (position) {
            case 0:
                orderBy("available");
            case 1:
                orderBy("requested");
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
                if (b.getOwnerName().equals(userName)) {
                    temp.add(b);
                }
            }
            for (Book b : books) {
                if (!b.getOwnerName().equals(userName)) {
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
        nextStep3();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing for now ..
    }

    @Override
    public void onItemClick(View view, int position) {
//        TODO: Opens book info activity
        Toast.makeText(this.getActivity(), "You clicked " + recyclerViewAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this.getContext(), bookInfoActivity.class);
        Log.d("fragment launch", recyclerViewAdapter.getItem(position).getTitle());
        Gson gson = new Gson();
        String message = gson.toJson(recyclerViewAdapter.getItem(position));
        intent.putExtra(MainActivity.BOOK_MESSAGE, message);
        startActivity(intent);

    }
}
