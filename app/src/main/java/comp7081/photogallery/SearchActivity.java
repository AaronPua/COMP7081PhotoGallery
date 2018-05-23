package comp7081.photogallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import comp7081.photogallery.database.DatabaseHelper;

public class SearchActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText startDateField;
    EditText endDateField;
    EditText captionField;
    EditText latitudeField;
    EditText longitudeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        dbHelper = new DatabaseHelper(getApplicationContext());
        startDateField = (EditText) findViewById(R.id.startDate);
        endDateField = (EditText) findViewById(R.id.endDate);
        captionField = (EditText) findViewById(R.id.caption);
        latitudeField = (EditText) findViewById(R.id.latitudeEditText);
        longitudeField = (EditText) findViewById(R.id.longitudeEditText);
    }

    public void onFilterSubmit(View view) {

        Intent matchingDatesIntent = new Intent();

        String startDate = startDateField.getText().toString();
        String endDate = endDateField.getText().toString();
        String caption = captionField.getText().toString();
        String latitude = latitudeField.getText().toString();
        String longitude = longitudeField.getText().toString();

        if(!TextUtils.isEmpty(startDate))
            matchingDatesIntent.putExtra("startDate", startDate);

        if(!TextUtils.isEmpty(endDate))
            matchingDatesIntent.putExtra("endDate", endDate);

        if(!TextUtils.isEmpty(caption))
            matchingDatesIntent.putExtra("caption", caption);

        if(!TextUtils.isEmpty(latitude))
            matchingDatesIntent.putExtra("latitude", latitude);

        if(!TextUtils.isEmpty(longitude))
            matchingDatesIntent.putExtra("longitude", longitude);

        setResult(RESULT_OK, matchingDatesIntent);
        finish();
    }

    public void cancelButton(View view) {
        finish();
    }
}
