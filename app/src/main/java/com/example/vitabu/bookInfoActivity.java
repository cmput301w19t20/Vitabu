/*
 * This file handles the activity of viewing a book from browse books. The user is then presented with
 * the option of viewing the Owner's profile, viewing a Goodreads page for the book and requesting to
 * borrow this book.
 *
 * Author: Jacob Paton
 * Version: 1.2
 * Outstanding Issues: ---
 */

package com.example.vitabu;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

/**
 * This class creates the Book Info activity which lets the users view the full information about a
 * provided book.
 *
 * @version 1.2
 * @author Jacob Paton
 * @see browseBooksActivity
 */
public class bookInfoActivity extends AppCompatActivity {

    //Create this class with an intent that contains a book object in json in the intent's extra message
    Book book;
    String ownerid;
    User owner;
    LocalUser curUser;
    final String logTag = "bookInfoActivity";
    FirebaseDatabase database;
    DatabaseReference myRef;

    /**
     * This method is run when the screen is created. It will get the provided book from the invoking
     * activity and will populate the screen with the provided book's information.
     *
     * @author Jacob Paton
     * @param savedInstanceState keeps track of the state that the screen was in when it was left.
     */
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

        //Deserializes the book, and the current owner.
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

    /**
     * This method will be invoked when the user clicks on the Request Book button. It writes the
     * book request to the database and exits the activity.
     *
     * @param view the view in which this method is invoked.
     */
    public void onClickRequestBook(View view) {
        //Finds the owner's profile in the database and writes the book request to the database.
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

    /**
     * This method will open the browser on the Goodreads page for the provided book. The book is
     * found on the Goodreads page by the provided ISBN.
     *
     * @param view the view in which this method is invoked.
     * @author Arseniy Kouzmenkov
     */
    public void onGoodreadsClick(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.goodreads.com/book/isbn/" + book.getISBN()));
        startActivity(intent);
    }


    /**
     * This method simply finishes the current activity.
     *
     */
    public void finishThisActivity(){
        this.finish();
    }

    /**
     * This method creates a notification for the owner and a borrow request record and pushes both
     * of them to into the database.
     *
     * @param owner the owner of the book being requested.
     */
    private void writeRequestToDatabase(User owner){
        this.owner = owner;
        // Get database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        // Construct BorrowRecord for this transaction.
        BorrowRecord newRecord = new BorrowRecord(owner.getUserName(), curUser.getUserName(), book.getBookid());
        // Construct notification for owner
        Notification newNotification = new Notification("Book Request for '" + book.getTitle() + "'",
                "A user has requested your book. Click here to view.",
                "request", ownerid, newRecord.getRecordid());

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

        book.setStatus("requested");
        myRef.child("books").child(book.getBookid()).setValue(book)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Successfully updated book status in database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to update Book status in database", e);
                    }
                });
    }

    /**
     * This method gets the owner account from the database and attempts to go to that user's profile.
     *
     * @param view the view from which this method gets invoked.
     */
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

    /**
     * This method will start the userProfileActivity and will pass the owner's data to that activity.
     *
     * @param owner the user information for the owner of the book.
     */
    public void goToUserProfileActivity(User owner) {
        this.owner = owner;
        Intent intent = new Intent(this, userProfileActivity.class);
        Gson gson = new Gson();
        intent.putExtra(MainActivity.USER_MESSAGE, gson.toJson(owner));
        startActivity(intent);
    }
}
