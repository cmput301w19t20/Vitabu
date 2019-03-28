package com.example.vitabu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class returnBookActivity extends AppCompatActivity {

    String returnedISBN; // scanned book isbn
    Book book;
    String userName;
    BorrowRecord record = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);

        //Deserialize the book
        Intent intent = getIntent();
        String bookMessage = intent.getStringExtra(MainActivity.BOOK_MESSAGE);
        Gson gson = new Gson();
        book = gson.fromJson(bookMessage, Book.class);
        String bookOwner = book.getOwnerName();

        // Populate text fields with appropriate data
        TextView title = (TextView) findViewById(R.id.return_book_title_input);
        title.setText(book.getTitle());
        TextView author = (TextView) findViewById(R.id.return_book_author_input);
        author.setText(book.getAuthor());
        TextView ISBN = (TextView) findViewById(R.id.return_book_isbn_input);
        ISBN.setText(book.getISBN());
        Button returnBookButton = (Button) findViewById(R.id.return_book_isbn_scan_button);
        // when return book button is clicked, user is sent to ISBNActivity to scan ISBN
        returnBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(returnBookActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                onScanISBNClick(v);
            }
        });

        Database database = Database.getInstance();
        userName = database.getCurUserName();

        if(bookOwner == userName){
            TextView header = (TextView) findViewById(R.id.return_book_header);
            header.setText("Accept return");
            returnBookButton.setText("Accept return");
        }else{
            TextView header = (TextView) findViewById(R.id.return_book_header);
            header.setText("Return book");
            returnBookButton.setText("Return book");
        }
    }

    // send to ISBNScan Activity
    public void onScanISBNClick(View view){
        Intent intent = new Intent(this, ISBNActivity.class);
        startActivityForResult(intent, 1);
        //Toast.makeText(returnBookActivity.this, "Book return for ISBN: " + returnedISBN, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("BACK", "FROM ACTIVITY");
//        getActivity();
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String text = data.getStringExtra("ISBN_number");
//                Toast.makeText(this.getActivity(), text, Toast.LENGTH_SHORT).show();
                returnedISBN = text;
                completeBookReturnTransaction();
            }
        }
    }

    public void completeBookReturnTransaction() {
        if(returnedISBN.equals(book.getISBN())){
            getBorrowRecord(book.getBookid());
            while(record == null);
            String message;
            if(userName.equals(book.getOwnerName())) {
                Toast.makeText(returnBookActivity.this, "Success Owner", Toast.LENGTH_SHORT).show();
                Database database = Database.getInstance();
                Runnable onSuccess = new Runnable() {
                    @Override
                    public void run() {
                        returnFromActivity();
                    }
                };
                database.returnBook(onSuccess, null, book);
                message = "Write a review of " + record.getBorrowerName()+ ".";
            }else{
                Toast.makeText(returnBookActivity.this, "Success Borrower", Toast.LENGTH_SHORT).show();
                message = "Write a review of " + record.getOwnerName()+ ".";
            }

            // create review notification
            Notification newNotification = new Notification("Write Review", message, "review", userName, record.getRecordid());
            storeReview(newNotification);

        }else{
            Toast.makeText(returnBookActivity.this, "Wrong ISBN please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public void returnFromActivity(){
        this.finish();
    }

    private void getBorrowRecord(String bookid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("borrowrecords").orderByChild("bookid").equalTo(bookid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d("Count2 ", "" + snapshot.getChildrenCount());
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            record = postSnapshot.getValue(BorrowRecord.class);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {
                    }
                });
    }

    private void storeReview(Notification notif){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("notifications").child(notif.getNotificationid()).setValue(notif)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Review notification", "Successfully wrote notification to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Review notification", "Failed to write notification to database", e);
                    }
                });

    }
}
