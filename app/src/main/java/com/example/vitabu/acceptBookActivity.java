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
 * This file contains the activity that has the logic and UI of finalizing an accept book borrow
 * transaction between a book owner and borrower.
 *
 * Author: Katherine Richards
 * Version: 1.0
 * Outstanding Issues: ---
 */

package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

public class acceptBookActivity extends AppCompatActivity {

    String bookISBN; // scanned book isbn
    Book book;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_book);

        //Deserialize the book
        Intent intent = getIntent();
        String bookMessage = intent.getStringExtra(MainActivity.BOOK_MESSAGE);
        Gson gson = new Gson();
        book = gson.fromJson(bookMessage, Book.class);
        String bookOwner = book.getOwnerName();

        // Populate text fields with appropriate data
        TextView title = (TextView) findViewById(R.id.accept_book_title_input);
        title.setText(book.getTitle());
        TextView author = (TextView) findViewById(R.id.accept_book_author_input);
        author.setText(book.getAuthor());
        TextView ISBN = (TextView) findViewById(R.id.accept_book_isbn_input);
        ISBN.setText(book.getISBN());
        Button acceptBookButton = (Button) findViewById(R.id.accept_book_isbn_scan_button);
        // when return book button is clicked, user is sent to ISBNActivity to scan ISBN
        acceptBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanISBNClick(v);
            }
        });

        Database database = Database.getInstance();
        userName = database.getCurUserName();

        if(bookOwner == userName){
            TextView header = (TextView) findViewById(R.id.accept_book_header);
            header.setText("Accept book");
            acceptBookButton.setText("Accept book");
        }else{
            TextView header = (TextView) findViewById(R.id.accept_book_header);
            header.setText("Lend Book");
            acceptBookButton.setText("Accept book");
        }
    }

    // send to ISBNScan Activity
    public void onScanISBNClick(View view){
        Intent intent = new Intent(this, ISBNActivity.class);
        startActivityForResult(intent, 1);
        //Toast.makeText(this, "Accept book transaction for ISBN: " + bookISBN, Toast.LENGTH_SHORT).show();

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
                bookISBN = text;
                if(bookISBN.equals(book.getISBN())){
                    if(book.getBorrower().equals(userName)){
                        completeBookBorrowTransaction();
                    }else{
                    }
                }else{
                    Toast.makeText(this, "Incorrect ISBN try again", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void completeBookBorrowTransaction() {
        Database d = Database.getInstance();
        d.getRootReference().child("books").child(book.getBookid()).child("status").setValue("borrowed");
        this.finish();
    }

}
