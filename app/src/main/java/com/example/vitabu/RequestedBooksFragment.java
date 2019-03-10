package com.example.vitabu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class RequestedBooksFragment extends Fragment implements AdapterView.OnItemSelectedListener, RequestedBooksBookRecyclerViewAdapter.ItemClickListener{

    RequestedBooksBookRecyclerViewAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_requested_books, container, false);

        // Since we have predetermined the items for the drop down status menu,
        // will use a string array containing the status items -- located in the resource file
        Spinner spinner = (Spinner) fragmentView.findViewById(R.id.requested_books_status_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.requested_books_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // data to populate the RecyclerView with
        ArrayList<Book> books = new ArrayList<>();
        Book book;
        for (int i = 0; i < 10; i++) {
            book = new Book();
            book.setTitle("Title");
            book.setAuthor("Author");
            books.add(book);
        }

        // set up the RecyclerView
        RecyclerView recyclerView = fragmentView.findViewById(R.id.requested_books_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerViewAdapter = new RequestedBooksBookRecyclerViewAdapter(this.getActivity(), books);
        recyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        return fragmentView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // TODO: filter books according to status selected
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing for now ..
    }

    @Override
    public void onItemClick(View view, int position) {
//        TODO: Opens book info activity
        //on item click user will be prompted to view book owner profile
//        Intent intent = new Intent(this.getActivity(), bookRequestsActivity.class);
//        startActivity(intent);
        Toast.makeText(this.getActivity(), "You clicked " + recyclerViewAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
