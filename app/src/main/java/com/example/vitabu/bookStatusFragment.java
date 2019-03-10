package com.example.vitabu;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class bookStatusFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    /**
     * TODO: design row for each book in RecyclerView (CardView?), IMG: Title, Author, book details
     * TODO: notify data set changed for array adapter when filter is selected
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_book_status, container, false);

        // Since we have predetermined the items for the drop down status menu,
        // will use a string array containing the status items -- located in the resource file
        Spinner spinner = (Spinner) fragmentView.findViewById(R.id.book_status_status_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.owned_books_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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
}
