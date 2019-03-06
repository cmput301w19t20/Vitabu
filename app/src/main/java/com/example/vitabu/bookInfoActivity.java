package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

public class bookInfoActivity extends AppCompatActivity {

    //Create this class with an intent that contains a book object in json in the intent's extra message
    Intent intent;
    Book book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Gson gson = new Gson();
        book = gson.fromJson(message, Book.class);
        TextView title = (TextView) findViewById(R.id.book_info_title);
        title.setText(book.getTitle());
        TextView author = (TextView) findViewById(R.id.book_info_author);
        author.setText(book.getAuthor());
        TextView ISBN = (TextView) findViewById(R.id.book_info_isbn);
        ISBN.setText(book.getISBN());
    }

    public void onClickRequestBook(View view){
//        TODO: Implement book request
        Intent intent = new Intent(this, bookRequestsActivity.class);
        Gson gson = new Gson();
        String message = gson.toJson(book);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void onClickViewOwner(View view){
        Intent intent = new Intent(this, userProfileActivity.class);
        Gson gson = new Gson();
        String message = gson.toJson(book.getOwner());
        intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
