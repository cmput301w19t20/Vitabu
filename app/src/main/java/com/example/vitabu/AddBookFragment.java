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
 * This file deals with the functionality of adding a book to the database within this fragment.
 * This fragment is a part of the browseBooksActivity activity. It will call the ISBNActivity activity
 * to get the ISBN from the user.
 *
 * Author: Arseniy Kouzmenkov
 *
 * Version: 1.1
 *
 * Outstanding issues: no image addition activity (just button that does nothing).
 *
 */

package com.example.vitabu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * This class deals with the add book fragment and adding a new book for the user.
 *
 * @author Arseniy Kouzmenkov
 * @version 1.0
 * @see browseBooksActivity
 */
public class AddBookFragment extends Fragment {
    Button isbnButton;
    Button addBookButton;
    EditText isbnText;
    Button addImageButton;
    Bitmap imageBitmap;
    boolean imageExists = false;
    FirebaseAuth auth;
    private final String logTag = "AddBookFragment";
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private FirebaseDatabase database = FirebaseDatabase.getInstance(); //The realtime database handle
    private DatabaseReference myRef = database.getReference(); //The reference to the database handle
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("images");

    View root;


    /**
     * This function initializes the view when the user clicks on the Add Book button in the bottom
     * navigation bar.
     *
     * @param inflater this inflates the fragment_add_book.xml file to fill out the screen.
     * @param container this is the container that has all the information about the screen view itself.
     * @param savedInstanceState this keeps the state of the screen when another fragment is being viewed
     * @return the view that the fragment is situated in.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_book, container, false);
        root = rootView;
        auth = FirebaseAuth.getInstance();

        // This part simply creates an onClick listener to deal with the Scan ISBN button
        isbnButton = (Button) rootView.findViewById(R.id.add_book_isbn_scan_button);
        isbnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanISBNClick(rootView);
            }
        });

        //This part creates an onClick listener to deal with the Add Book button
        addBookButton = (Button) rootView.findViewById(R.id.add_book_submit_button);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddBookClick(rootView);
            }
        });

        //This part creates an onClick listener to deal with the Add Image button
        addImageButton = (Button) rootView.findViewById(R.id.add_book_add_image_button);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddImageClick(rootView);
            }
        });

        //This just gets a handle on the isbnText to be used by the onActivityResult method
        isbnText = (EditText) rootView.findViewById(R.id.add_book_isbn_input);

        // hide image preview until user adds a photo
        ImageView imageView = (ImageView) root.findViewById(R.id.add_book_pic);
        imageView.setVisibility(View.GONE);

        return rootView;
    }

    /**
     * This function processes the Scan ISBN button result (placing the scanned ISBN into the ISBN
     * text field) and the add image button result.
     *
     * @param requestCode the number that lets us know which activity returned.
     * @param resultCode the number identifying the fact that the activity finished as intended
     * @param data the intent that contains the data passed back from the previous activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //Gets the handle on the onActivityResult from
        // the class containing the original return result.
        getActivity();
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String text = data.getStringExtra("ISBN_number");
//                Toast.makeText(this.getActivity(), text, Toast.LENGTH_SHORT).show();
                isbnText.setText(text);
            }
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            // get image from picture intent and display preview
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            TextView textView = (TextView) root.findViewById(R.id.add_book_add_image_text);
            textView.setVisibility(View.GONE);
            ImageView imageView = (ImageView) root.findViewById(R.id.add_book_pic);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(imageBitmap);
            imageExists = true;
        }

    }

    /**
     * This function will open the ISBN scanner activity.
     *
     * @param view the view where this fragment resides
     */
    public void onScanISBNClick(View view){
        Intent intent = new Intent(this.getActivity(), ISBNActivity.class);
        startActivityForResult(intent, 1);
    }

    /**
     * This function will check that the necessary fields are filled in.
     *
     * @param view the view where this fragment resides
     */
    public void onAddBookClick(View view){
        EditText title = (EditText) view.findViewById(R.id.add_book_title_input);
        EditText author = (EditText) view.findViewById(R.id.add_book_author_input);
        EditText description = (EditText) view.findViewById(R.id.add_book_description_input);

        if (title.getText().toString().length() < 1){
            Toast.makeText(this.getActivity(), "Please, provide a title.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (author.getText().toString().length() < 1){
            Toast.makeText(this.getActivity(), "Please, provide an author.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isbnText.getText().toString().length() < 1){
            Toast.makeText(this.getActivity(), "Please, provide an ISBN.", Toast.LENGTH_SHORT).show();
            return;
        }

        attemptAddingBook(view);

    }


    /**
     * This function will attempt to add the book into the database. On success will notify the user
     * and clear the screen of the previous input.
     *
     * @param view the view where this fragment resides.
     */
    public void attemptAddingBook(final View view){
        Book bookToAdd = new Book();
        String title = ((EditText) view.findViewById(R.id.add_book_title_input)).getText().toString();
        String author = ((EditText) view.findViewById(R.id.add_book_author_input)).getText().toString();
        String isbn = isbnText.getText().toString();
        String description = ((EditText) view.findViewById(R.id.add_book_description_input)).getText().toString();

        bookToAdd.setTitle(title);
        bookToAdd.setAuthor(author);
        bookToAdd.setISBN(isbn);
        bookToAdd.setStatus("available");

        final String user = auth.getCurrentUser().getDisplayName();
        bookToAdd.setOwnerName(user);

        if (description.length() > 0){
            bookToAdd.setDescription(description);
        }
        Toast.makeText(getContext(), "Book Added.", Toast.LENGTH_LONG).show();

        myRef.child("books").child(bookToAdd.getBookid()).setValue(bookToAdd)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Successfully wrote book to database.");
                        String reset = "";
                        ((EditText) view.findViewById(R.id.add_book_title_input)).setText(reset);
                        ((EditText) view.findViewById(R.id.add_book_author_input)).setText(reset);
                        ((EditText) view.findViewById(R.id.add_book_isbn_input)).setText(reset);
                        ((EditText) view.findViewById(R.id.add_book_description_input)).setText(reset);

                        myRef.child("users").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User tempUser = dataSnapshot.getValue(User.class);
                                updateUsersBooksOwned(tempUser);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write User to database", e);
                    }
                });

        //Uploads an image to the firebase storage service if there is one to upload.
        if(imageExists){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = mStorageRef.child(bookToAdd.getBookid()).putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(logTag, "Failed to write image to cloud storage", exception);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ((ImageView) view.findViewById(R.id.add_book_pic)).setVisibility(View.GONE);
                    ((TextView) view.findViewById(R.id.add_book_add_image_text)).setVisibility((View.VISIBLE));
                }
            });
            imageExists = false;
        }

    }


    /**
     * This function will increment the number of books that user owns in the database.
     *
     * @param user the user for which we need to increment the books owned counter.
     */
    public void updateUsersBooksOwned(User user){
        user.setBooksOwned(user.getBooksOwned() + 1);
        myRef.child("users").child(user.getUserName()).setValue(user).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Successfully updated the User's books owned count.");
                    }
                }
        );
    }

    /**
     * This function will add an image linked to the book from the user.
     *
     * @param view the view where this fragment resides.
     */
    public void onAddImageClick(View view){
        //Toast.makeText(this.getActivity(), "You clicked on Add Image button. This feature is not yet implemented.", Toast.LENGTH_SHORT).show();
        if (this.getActivity() != null) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, 2);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this.getActivity(), "Need camera permission to take photo", Toast.LENGTH_LONG).show();
            }
        }
    }
}
