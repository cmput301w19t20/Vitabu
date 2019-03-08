package com.example.vitabu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.UUID;

public class bookInfoActivity extends AppCompatActivity {

    //Create this class with an intent that contains a book object in json in the intent's extra message
    Intent intent;
    Book book;
    User owner;
    User curUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Gson gson = new Gson();
        IntentJson passed = gson.fromJson(message, IntentJson.class);
        curUser = passed.getUser();
        book = (Book) passed.getObject(0);
        owner = book.getOwner();
        TextView title = (TextView) findViewById(R.id.book_info_title);
        title.setText(book.getTitle());
        TextView author = (TextView) findViewById(R.id.book_info_author);
        author.setText(book.getAuthor());
        TextView ISBN = (TextView) findViewById(R.id.book_info_isbn);
        ISBN.setText(book.getISBN());
    }

    public void onClickRequestBook(View view){
        //TODO: Untested, I suspect very strongly that I'm pushing the wrong things to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();;
        final String logTag = "bookInfoActivity";

        Notification request = new Notification("Book Request for '" + book.getTitle() + "'",
                                                "A user has requested your book. Click here to view.",
                                                "request", owner);
        owner.addNotiication(request);
        myRef.child("notifications").child(UUID.randomUUID().toString()).setValue(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Suscsesfully wrote user to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write User to database", e);
                    }
                });

        BorrowRecord bookRequest = new BorrowRecord(owner, curUser, book);
        bookRequest.setApproved(false);
        myRef.child("transactions").child(UUID.randomUUID().toString()).setValue(bookRequest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Suscsesfully wrote user to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write User to database", e);
                    }
                });
    }

    public void onClickViewOwner(View view){
        Intent intent = new Intent(this, userProfileActivity.class);
        IntentJson passing = new IntentJson(curUser);
        passing.addObject(owner);
        String message = passing.toJson();
        intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
