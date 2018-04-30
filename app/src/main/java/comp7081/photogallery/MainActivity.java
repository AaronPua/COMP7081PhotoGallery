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
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import comp7081.photogallery.database.BitmapUtility;
import comp7081.photogallery.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    public ImageView mImageView;
    String mCurrentPhotoPath;
    Uri photoURI;
    String imageFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);
        showMostRecentPhoto(mImageView);
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

    public void showMostRecentPhoto(ImageView mImageView) {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        byte[] image = dbHelper.getMostRecentPhoto();
        if(image != null) {
            Bitmap bitmap = BitmapUtility.getImage(image);
            mImageView.setImageBitmap(bitmap);
        } else {
            mImageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(mCurrentPhotoPath);
            if(imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mImageView.setImageBitmap(bitmap);

                byte[] byteArray = BitmapUtility.convertBitmapToByteArray(bitmap);
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                dbHelper.addPhotoEntry(imageFileName, byteArray);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        imageFileName = timeStamp;
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
        startActivity(intent);
    }
}
