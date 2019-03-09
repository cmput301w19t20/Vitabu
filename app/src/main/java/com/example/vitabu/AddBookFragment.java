package com.example.vitabu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AddBookFragment extends Fragment {
    Button isbnButton;
    Button addBookButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_book, container, false);

        isbnButton = (Button) rootView.findViewById(R.id.add_book_isbn_scan_button);
        isbnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(rootView.getContext(), "You clicked on Scan ISBN button.", Toast.LENGTH_SHORT).show();
                onScanISBNClick(rootView);
            }
        });

        addBookButton = (Button) rootView.findViewById(R.id.add_book_submit_button);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(rootView.getContext(), "You clicked on Add Book button.", Toast.LENGTH_SHORT).show();
                onAddBookClick(rootView);
            }
        });

        return rootView;
    }



    public void onScanISBNClick(View view){
        Toast.makeText(this.getActivity(), "You clicked on Scan ISBN button.", Toast.LENGTH_SHORT).show();
    }

    public void onAddBookClick(View view){
        Toast.makeText(this.getActivity(), "You clicked on Add Book button.", Toast.LENGTH_SHORT).show();
    }
}
