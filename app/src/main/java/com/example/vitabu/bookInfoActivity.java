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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.UUID;

public class bookInfoActivity extends AppCompatActivity {

    //Create this class with an intent that contains a book object in json in the intent's extra message
    Book book;
    String ownerid;
    User owner;
    LocalUser curUser;
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
        //ownerid = book.getOwnerid();
        TextView title = (TextView) findViewById(R.id.book_info_title);
        title.setText(book.getTitle());
        TextView author = (TextView) findViewById(R.id.book_info_author);
        author.setText(book.getAuthor());
        TextView ISBN = (TextView) findViewById(R.id.book_info_isbn);
        ISBN.setText(book.getISBN());
    }

    private void setOwner(User owner){
        this.owner = owner;
    }

    public void onClickRequestBook(View view){
        //TODO: Untested, I suspect very strongly that I'm pushing the wrong things to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final String logTag = "bookInfoActivity";
        myRef.child("users").child(ownerid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        setOwner(dataSnapshot.getValue(User.class));
                        Log.d(logTag, "Read owner");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Cancelled");
                    }
                }
        );

        Notification request = new Notification("Book Request for '" + book.getTitle() + "'",
                                                "A user has requested your book. Click here to view.",
                                                "request", ownerid);

        myRef.child("notifications").child(UUID.randomUUID().toString()).setValue(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Successfully wrote user to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write User to database", e);
                    }
                });

        BorrowRecord bookRequest = new BorrowRecord(ownerid, curUser.getUserid(), book.getBookid());
        bookRequest.setApproved(false);
        String id = UUID.randomUUID().toString();
        bookRequest.setRecordid(id);
        myRef.child("transactions").child(id).setValue(bookRequest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Successfully wrote user to database.");
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
