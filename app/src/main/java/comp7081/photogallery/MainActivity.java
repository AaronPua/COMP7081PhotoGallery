package comp7081.photogallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
    String mCurrentPhotoPath;
    String timeStamp;
    String imageFileName;
    Uri photoURI;
    DatabaseHelper dbHelper;
    int currentPhotoIndex = 0;
    ArrayList<Bitmap> bitmapArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);

        dbHelper = new DatabaseHelper(getApplicationContext());
        bitmapArrayList = dbHelper.getAllPhotos();
        if(bitmapArrayList != null && bitmapArrayList.size() > 0) {
            currentPhotoIndex = bitmapArrayList.size() - 1;
            Bitmap lastBitmap = bitmapArrayList.get(currentPhotoIndex);
            mImageView.setImageBitmap(lastBitmap);
        } else {
            mImageView.setImageResource(R.drawable.ic_launcher_foreground);
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
        } else {
            mImageView.setImageResource(R.drawable.ic_launcher_foreground);
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
                            dbHelper.addPhotoEntry(imgFile.getName(), timeStamp, byteArray);

                            bitmapArrayList = dbHelper.getAllPhotos();
                            if(bitmapArrayList != null && bitmapArrayList.size() > 0) {
                                currentPhotoIndex = bitmapArrayList.size() - 1;
                            }
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

                    bitmapArrayList = dbHelper.getPhotosByDate(startDate, endDate);
                    if(bitmapArrayList != null && bitmapArrayList.size() > 0) {
                        currentPhotoIndex = bitmapArrayList.size() - 1;
                        Bitmap bitmap = bitmapArrayList.get(currentPhotoIndex);
                        mImageView.setImageBitmap(bitmap);
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
