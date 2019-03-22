package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

public class returnBookActivity extends AppCompatActivity {

    String returnedISBN; // scanned book isbn
    Book book;
    String userName;

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
                onScanISBNClick(v);
                // method call to handle database implementation of book return
                completeBookReturnTransaction();
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
        Toast.makeText(returnBookActivity.this, "Book return for ISBN: " + returnedISBN, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        getActivity();
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String text = data.getStringExtra("ISBN_number");
//                Toast.makeText(this.getActivity(), text, Toast.LENGTH_SHORT).show();
                returnedISBN = text;
            }
        }
    }

    public void completeBookReturnTransaction() {
        if(returnedISBN == book.getISBN()){
            Database database = Database.getInstance();
            Runnable onSuccess = new Runnable() {
                @Override
                public void run() {
                    deleteBorrowRecord();
                }
            };
            database.queryDatabase(onSuccess, database.getRootReference().child("borrowrecords"),
                                    "bookid", book.getBookid());
        }else{
            Toast.makeText(returnBookActivity.this, "Wrong ISBN please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteBorrowRecord(){
        Database database = Database.getInstance();
        DatabaseReference ref = database.getRootReference();
        BorrowRecord b = new BorrowRecord();
        for(DataSnapshot snap : database.getQueryResult()){
            b = snap.getValue(BorrowRecord.class);
        }
        ref.child("borrowrecords").child(b.getRecordid()).removeValue();
        ref.child("books").child(book.getBookid()).child("borrower").setValue("");
        ref.child("books").child(book.getBookid()).child("status").setValue("available");
        Intent intent = new Intent(this, browseBooksActivity.class);
        startActivity(intent);
    }
}
