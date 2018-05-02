package comp7081.photogallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import comp7081.photogallery.database.DatabaseHelper;

public class SearchActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText startDateField;
    EditText endDateField;
    EditText captionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dbHelper = new DatabaseHelper(getApplicationContext());
        startDateField = (EditText) findViewById(R.id.startDate);
        endDateField = (EditText) findViewById(R.id.endDate);
        captionField = (EditText) findViewById(R.id.caption);
    }

    public void onFilterSubmit(View view) {

        Intent matchingDatesIntent = new Intent();

        String startDate = startDateField.getText().toString();
        String endDate = endDateField.getText().toString();
        String caption = captionField.getText().toString();

        if(!TextUtils.isEmpty(startDate))
            matchingDatesIntent.putExtra("startDate", startDate);

        if(!TextUtils.isEmpty(endDate))
            matchingDatesIntent.putExtra("endDate", endDate);

        if(!TextUtils.isEmpty(caption))
            matchingDatesIntent.putExtra("caption", caption);

        setResult(RESULT_OK, matchingDatesIntent);
        finish();
    }

    public void cancelButton(View view) {
        finish();
    }
}
