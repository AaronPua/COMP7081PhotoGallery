package comp7081.photogallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import comp7081.photogallery.database.DatabaseHelper;

public class SearchActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dbHelper = new DatabaseHelper(getApplicationContext());
    }

    public void onFilterSubmit(View view) {
        EditText startDateField = (EditText) findViewById(R.id.startDate);
        EditText endDateField = (EditText) findViewById(R.id.endDate);

        String startDate = startDateField.getText().toString();
        String endDate = endDateField.getText().toString();

        Intent matchingDatesIntent = new Intent();
        matchingDatesIntent.putExtra("startDate", startDate);
        matchingDatesIntent.putExtra("endDate", endDate);
        setResult(RESULT_OK, matchingDatesIntent);
        finish();
    }

    public void cancelButton(View view) {
        finish();
    }
}
