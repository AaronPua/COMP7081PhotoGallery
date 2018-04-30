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
    public ImageView mImageView;
    String mCurrentPhotoPath;
    Uri photoURI;
    String imageFileName;
    DatabaseHelper dbHelper;
    int currentPhotoIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);

        dbHelper = new DatabaseHelper(getApplicationContext());
        ArrayList<Bitmap> bitmapArrayList = dbHelper.getBitmapArrayList();
        if(bitmapArrayList != null && bitmapArrayList.size() > 0) {
            currentPhotoIndex = bitmapArrayList.size() - 1;
            Bitmap lastBitmap = bitmapArrayList.get(currentPhotoIndex);
            mImageView.setImageBitmap(lastBitmap);
        } else {
            mImageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    public void openCameraApp(View view) {
        Log.d("open camera photo index, index", Integer.toString(currentPhotoIndex));
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

        ArrayList<Bitmap> bitmapArrayList = dbHelper.getBitmapArrayList();
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

            Bitmap lastBitmap = bitmapArrayList.get(currentPhotoIndex);
            mImageView.setImageBitmap(lastBitmap);
        } else {
            mImageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            File imgFile = new File(mCurrentPhotoPath);
            if(imgFile.exists())
            {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                mImageView.setImageBitmap(bitmap);
                try {
                    byte[] byteArray = BitmapUtility.convertBitmapToByteArray(bitmap);
                    dbHelper.addPhotoEntry(imageFileName, byteArray);
                    ++currentPhotoIndex;
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
