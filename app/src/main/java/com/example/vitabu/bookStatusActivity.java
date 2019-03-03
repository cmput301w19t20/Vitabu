package com.example.vitabu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class bookStatusActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /**
     * TODO: design row for each book in RecyclerView (CardView?), IMG: Title, Author, book details
     * TODO: notify data set changed for array adapter when filter is selected
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_status);

        // Since we have predetermined the items for the drop down status menu,
        // will use a string array containing the status items -- located in the resource file
        Spinner spinner = (Spinner) findViewById(R.id.book_status_status_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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
