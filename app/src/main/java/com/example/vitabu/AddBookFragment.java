/*
 * This file deals with the functionality of adding a book to the database within this fragment.
 * This fragment is a part of the browseBooksActivity activity. It will call the ISBNActivity activity
 * to get the ISBN from the user.
 *
 * Author: Arseniy Kouzmenkov
 *
 * Version: 1.0
 *
 * Outstanding issues: no database object addition, no checking for user input correctness, no check
 * for empty user input, no image addition activity (just button that does nothing).
 *
 */

package com.example.vitabu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        return rootView;
    }

    /**
     * This function processes the Scan ISBN button result (placing the scanned ISBN into the ISBN
     * text field).
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
        Toast.makeText(this.getActivity(), "You clicked on Add Book button.", Toast.LENGTH_SHORT).show();
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
     * This function will attempt to add the book into the database.
     *
     * @param view the view where this fragment resides.
     */
    public void attemptAddingBook(View view){
        //TODO: implement the adding to the database of the book.
        Book bookToAdd = new Book();
        String title = ((EditText) view.findViewById(R.id.add_book_title_input)).getText().toString();
        String author = ((EditText) view.findViewById(R.id.add_book_author_input)).getText().toString();
        String isbn = isbnText.getText().toString();
        String description = ((EditText) view.findViewById(R.id.add_book_description_input)).getText().toString();

        bookToAdd.setTitle(title);
        bookToAdd.setAuthor(author);
//        bookToAdd.setISBN(isbn);
        System.out.println("Incomplete!");
    }

    /**
     * This function will add an image linked to the book from the user.
     *
     * @param view the view where this fragment resides.
     */
    public void onAddImageClick(View view){
        Toast.makeText(this.getActivity(), "You clicked on Add Image button. This feature is not yet implemented.", Toast.LENGTH_SHORT).show();
    }
}
