package comp7081.photogallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import comp7081.photogallery.database.BitmapUtility;
import comp7081.photogallery.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;
    public ImageView mImageView;
    public TextView captionTextView;
    String mCurrentPhotoPath;
    String timeStamp;
    String imageFileName;
    String caption;
    String location;
    Uri photoURI;
    DatabaseHelper dbHelper;
    int currentPhotoIndex = 0;
    ArrayList<Bitmap> bitmapArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);
        captionTextView = (TextView) findViewById(R.id.captionTextView);

        dbHelper = new DatabaseHelper(getApplicationContext());
        bitmapArrayList = dbHelper.getAllPhotos();
        if(bitmapArrayList != null && bitmapArrayList.size() > 0) {
            currentPhotoIndex = bitmapArrayList.size() - 1;
            Bitmap lastBitmap = bitmapArrayList.get(currentPhotoIndex);
            mImageView.setImageBitmap(lastBitmap);
            
            String caption = dbHelper.getCaption(currentPhotoIndex);
            if(!TextUtils.isEmpty(caption))
                captionTextView.setText("Caption: " + caption);
            else
                captionTextView.setText("Caption: ");
        } else {
            mImageView.setImageResource(R.drawable.ic_launcher_foreground);
            captionTextView.setText("");
        }
    }

    public void openCameraApp(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.d("File Creation", "Failed photoFile.");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "comp7081.photogallery.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void captionAlertDialog(View view) {
        final EditText captionEditText = new EditText(MainActivity.this);

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Caption");
        alertDialog.setMessage("Insert a caption for this photo.");
        alertDialog.setView(captionEditText, 50, 0, 50, 0);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean emptyCaptionString = TextUtils.isEmpty(captionEditText.getText().toString());
                        if(!emptyCaptionString) {
                            caption = captionEditText.getText().toString();
                            dbHelper.updateCaption((currentPhotoIndex), caption);
                            Toast.makeText(MainActivity.this, "Updated caption: " + caption, Toast.LENGTH_LONG).show();
                            captionTextView.setText("Caption: " + caption);
                        }
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void previousOrNextPhoto(View view) {

        if(bitmapArrayList != null && bitmapArrayList.size() > 0)
        {
            switch (view.getId()) {
                case R.id.previousPhotoButton:
                    --currentPhotoIndex;
                    break;
                case R.id.nextPhotoButton:
                    ++currentPhotoIndex;
                    break;
                default:
                    break;
            }

            if (currentPhotoIndex < 0)
                currentPhotoIndex = 0;
            if (currentPhotoIndex >= bitmapArrayList.size())
                currentPhotoIndex = bitmapArrayList.size() - 1;

            Bitmap bitmap = bitmapArrayList.get(currentPhotoIndex);
            mImageView.setImageBitmap(bitmap);
            caption = dbHelper.getCaption(currentPhotoIndex);
            if(!TextUtils.isEmpty(caption)) {
                captionTextView.setText("Caption: " + caption);
            }
            else {
                captionTextView.setText("Caption: ");
            }
        } else {
            mImageView.setImageResource(R.drawable.ic_launcher_foreground);
            captionTextView.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    File imgFile = new File(mCurrentPhotoPath);
                    if(imgFile.exists())
                    {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        mImageView.setImageBitmap(bitmap);
                        try {
                            byte[] byteArray = BitmapUtility.convertBitmapToByteArray(bitmap);
                            dbHelper.addPhotoEntry(byteArray, imgFile.getName(), timeStamp, caption, location);

                            bitmapArrayList = dbHelper.getAllPhotos();
                            if(bitmapArrayList != null && bitmapArrayList.size() > 0) {
                                currentPhotoIndex = bitmapArrayList.size() - 1;
                            }
                            captionTextView.setText("Caption:");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case SEARCH_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String startDate = data.getStringExtra("startDate");
                    String endDate = data.getStringExtra("endDate");
                    String caption = data.getStringExtra("caption");

                    if(!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
                        bitmapArrayList = dbHelper.getPhotosByDate(startDate, endDate);
                        if(bitmapArrayList != null && bitmapArrayList.size() > 0) {
                            currentPhotoIndex = bitmapArrayList.size() - 1;
                            Bitmap bitmap = bitmapArrayList.get(currentPhotoIndex);
                            mImageView.setImageBitmap(bitmap);
                        }
                    }

                    if(!TextUtils.isEmpty(caption)) {
                        bitmapArrayList = dbHelper.getPhotosByCaption(caption);
                        if(bitmapArrayList != null && bitmapArrayList.size() > 0) {
                            currentPhotoIndex = bitmapArrayList.size() - 1;
                            Bitmap bitmap = bitmapArrayList.get(currentPhotoIndex);
                            mImageView.setImageBitmap(bitmap);
                            captionTextView.setText("Caption: " + caption);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        imageFileName = "JPG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void openSearchActivity(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
    }
}
