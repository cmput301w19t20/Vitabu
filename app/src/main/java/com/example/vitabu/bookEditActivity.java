package com.example.vitabu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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


/**
 * This class creates the Book Edt activity which lets a book owner view and edit the full information about a
 * provided book.
 *
 * @version 1.0
 * @author Jacob Paton
 * @see browseBooksActivity
 */
public class bookEditActivity extends AppCompatActivity {
    //Create this class with an intent that contains a book object in json in the intent's extra message
    Book book;
    String ownerid;
    User owner;
    LocalUser curUser;
    final String logTag = "bookInfoActivity";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth auth;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

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
        setContentView(R.layout.activity_book_edit);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        auth = FirebaseAuth.getInstance();

        // Get Book, and Current user from intent.
        Intent intent = getIntent();
        String bookMessage = intent.getStringExtra(MainActivity.BOOK_MESSAGE);
        String localUserMessage = intent.getStringExtra(MainActivity.LOCALUSER_MESSAGE);
        Log.d("bookEdit", bookMessage);

        //Deserializes the book, and the current owner.
        Gson gson = new Gson();
        book = gson.fromJson(bookMessage, Book.class);
        curUser = gson.fromJson(localUserMessage, LocalUser.class);
        ownerid = book.getOwnerName();
        Log.d("bookEditTESTTEST", book.getOwnerName());

        // Populate textviews with appropriate data.
        TextView title = (TextView) findViewById(R.id.book_edit_title);
        title.setText(book.getTitle());
        TextView author = (TextView) findViewById(R.id.book_edit_author);
        author.setText(book.getAuthor());
        TextView ISBN = (TextView) findViewById(R.id.book_edit_isbn);
        ISBN.setText(book.getISBN());
        TextView desc = (TextView) findViewById(R.id.book_edit_desc);
        desc.setText(book.getDescription());
    }

    /**
     * This method will open the browser on the Goodreads page for the provided book. The book is
     * found on the Goodreads page by the provided ISBN.
     *
     * @param view the view in which this method is invoked.
     * @author Arseniy Kouzmenkov
     */
    public void onISBNClick(View view){
        // scan the isbn
        Intent intent = new Intent(this, ISBNActivity.class);
        startActivityForResult(intent, 1);
    }


    /**
     * This method simply finishes the current activity.
     *
     */
    public void finishThisActivity(){
        this.finish();
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

    /**
     * This method saves a clicked book and closes the activity.
     *
     * @param view the view which was clicked.
     */
    public void onClickSave(View view) {
//        TODO: Save in database
        attemptSavingBook();
        finish();
    }

    /**
     * This function will attempt to save the book into the database. On success will notify the user.
     */
    public void attemptSavingBook(){
        // Get data from activity
        String title = ((EditText) findViewById(R.id.book_edit_title)).getText().toString();
        String author = ((EditText) findViewById(R.id.book_edit_author)).getText().toString();
        String isbn = ((EditText) findViewById(R.id.book_edit_isbn)).getText().toString();
        String description = ((EditText) findViewById(R.id.book_edit_desc)).getText().toString();

        // Add the data entered to the book.
        book.setTitle(title);
        book.setAuthor(author);
        book.setISBN(isbn);

        // Check if there is a description then add it (if there is one).
        if (description.length() > 0){
            book.setDescription(description);
        }

        System.out.println(book.toString());

        Log.d("Book Edit", "Saving " + book.toString());
        myRef.child("books").child(book.getBookid()).setValue(book)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Book Edit", "Successfully updated book's attributes on database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Book Edit", "Failed to update book in database.");
                    }
                });

//        myRef.child("books").child(bookToAdd.getBookid()).setValue(bookToAdd)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(logTag, "Successfully wrote book to database.");
//
//                        myRef.child("users").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(logTag, "Failed to write User to database", e);
//                    }
//                });
    }

    public void onClickDeleteImage(View view){
        // handle deleting an image
        ImageView imageView = (ImageView) findViewById(R.id.book_edit_picture);
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher);
        imageView.setImageBitmap(bitmap);

    }

    public void onClickAddImage(View view){
        // handle adding a new image
        // start intent and request permissions
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //Gets the handle on the onActivityResult from
        // the class containing the original return result.
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String text = data.getStringExtra("ISBN_number");
                EditText isbnText = findViewById(R.id.book_edit_isbn);
                isbnText.setText(text);
            }
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            // get image from picture intent and display preview
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = (ImageView) findViewById(R.id.book_edit_picture);
            imageView.setImageBitmap(imageBitmap);
        }

    }
}
