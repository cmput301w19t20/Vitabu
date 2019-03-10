package com.example.vitabu;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    final String logTag = "bookInfoActivity";
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up view.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // Get Book, and Current user from intent.
        Intent intent = getIntent();
        String bookMessage = intent.getStringExtra(MainActivity.BOOK_MESSAGE);
        String localUserMessage = intent.getStringExtra(MainActivity.LOCALUSER_MESSAGE);
        Log.d("bookInfo", bookMessage);

        Gson gson = new Gson();
        book = gson.fromJson(bookMessage, Book.class);
        curUser = gson.fromJson(localUserMessage, LocalUser.class);
        ownerid = book.getOwnerName();
        Log.d("bookInfoTESTTEST", book.getOwnerName());

        // Populate textviews with appropriate data.
        TextView title = (TextView) findViewById(R.id.book_info_title);
        title.setText(book.getTitle());
        TextView author = (TextView) findViewById(R.id.book_info_author);
        author.setText(book.getAuthor());
        TextView ISBN = (TextView) findViewById(R.id.book_info_isbn);
        ISBN.setText(book.getISBN());
        TextView desc = (TextView) findViewById(R.id.book_info_desc);
        desc.setText(book.getDescription());
    }

    public void onClickRequestBook(View view) {

        myRef.child("users").child(ownerid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        writeRequestToDatabase(dataSnapshot.getValue(User.class));
                        finishThisActivity();
                        Toast.makeText(bookInfoActivity.this, "You have requested '" + book.getTitle() + "'", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Cancelled");
                    }
                }
        );
    }

    public void onGoodreadsClick(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.goodreads.com/book/isbn/" + book.getISBN()));
        startActivity(intent);
    }


    public void finishThisActivity(){
        this.finish();
    }

    private void writeRequestToDatabase(User owner){
        this.owner = owner;
        // Get database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        // Construct notification for owner
        Notification newNotification = new Notification("Book Request for '" + book.getTitle() + "'",
                                                "A user has requested your book. Click here to view.",
                                                "request", ownerid);
        Log.d(logTag, "Notification ID = " + newNotification.getNotificationid());
        // Write notification to database.
        myRef.child("notifications").child(newNotification.getNotificationid()).setValue(newNotification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Successfully wrote notification to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write notification to database", e);
                    }
                });

        // Construct BorrowRecord for this transaction.
        BorrowRecord newRecord = new BorrowRecord(owner.getUserName(), curUser.getUserName(), book.getBookid());
        myRef.child("borrowrecords").child(newRecord.getRecordid()).setValue(newRecord)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Successfully wrote Borrow record to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write Borrow record to database", e);
                    }
                });
    }

    public void onClickViewOwner(View view){
        // Get Owner from database. When done launch goToUserProfileActivity()
        myRef.child("users").child(ownerid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        goToUserProfileActivity(dataSnapshot.getValue(User.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Database error", databaseError.toException());
                    }
                }
        );
    }

    public void goToUserProfileActivity(User owner) {
        this.owner = owner;
        Intent intent = new Intent(this, userProfileActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MainActivity.USER_MESSAGE, gson.toJson(owner));
        startActivity(intent);
    }
}
