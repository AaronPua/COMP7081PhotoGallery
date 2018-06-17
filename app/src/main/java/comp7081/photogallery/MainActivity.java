package comp7081.photogallery;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import comp7081.photogallery.Helpers.BitmapUtility;
import comp7081.photogallery.database.DatabaseHelper;
import comp7081.photogallery.database.models.LocationInfo;
import comp7081.photogallery.database.models.Photo;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;
    public ImageView mImageView;
    String mCurrentPhotoPath;
    String timeStamp;
    String imageFileName;
    String caption;
    String latitude;
    String longitude;
    Uri photoURI;
    DatabaseHelper dbHelper;
    int currentPhotoIndex = 0;
    ArrayList<Photo> photoArrayList;
    Location location;
    double currentLat;
    double currentLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        dbHelper = new DatabaseHelper(getApplicationContext());
        photoArrayList = dbHelper.getAllPhotos();
        if (photoArrayList != null && photoArrayList.size() > 0) {
            currentPhotoIndex = photoArrayList.size() - 1;
            Bitmap lastBitmap = BitmapFactory.decodeFile(photoArrayList.get(currentPhotoIndex).getImage());
            mImageView.setImageBitmap(lastBitmap);
        } else {
            mImageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    public void openSearchActivity(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
    }

    public void openCameraApp(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.d("File Creation", "Failed photoFile.");
            }
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
                        if (!emptyCaptionString) {
                            caption = captionEditText.getText().toString();
                            dbHelper.updateCaption((currentPhotoIndex), caption);
                            Toast.makeText(MainActivity.this, "Updated caption: " + caption, Toast.LENGTH_LONG).show();
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
        if (photoArrayList != null && photoArrayList.size() > 0) {
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
            if (currentPhotoIndex >= photoArrayList.size())
                currentPhotoIndex = photoArrayList.size() - 1;

            Bitmap bitmap = BitmapFactory.decodeFile(photoArrayList.get(currentPhotoIndex).getImage());
            mImageView.setImageBitmap(bitmap);
        } else {
            mImageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    public void rotatePhotoDialog(View view) {
        final EditText rotateEditText = new EditText(MainActivity.this);
        rotateEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Rotate Photo");
        alertDialog.setMessage("Ex: 45 to rotate clockwise, -90 for counterclockwise.");
        alertDialog.setView(rotateEditText, 50, 0, 50, 0);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Rotate",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean emptyRotateString = TextUtils.isEmpty(rotateEditText.getText().toString());
                        if (!emptyRotateString) {
                            String rotationAngle = rotateEditText.getText().toString();
                            //Bitmap rotatedBitmap = BitmapUtility.rotateBitmap();
                            dbHelper.updateFilePath((currentPhotoIndex), mCurrentPhotoPath);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    processCapturedImage(mCurrentPhotoPath);
                }
                break;
            case SEARCH_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String startDate = data.getStringExtra("startDate");
                    String endDate = data.getStringExtra("endDate");
                    String caption = data.getStringExtra("caption");
                    String latitude = data.getStringExtra("latitude");
                    String longitude = data.getStringExtra("longitude");
                    getImagesByFilters(startDate, endDate, caption, latitude, longitude);
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

    private void getImagesByFilters(String startDate, String endDate, String caption, String latitude, String longitude) {
        if(!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
            photoArrayList = dbHelper.getPhotosByDate(startDate, endDate);
            setImageBitmapForFilters(photoArrayList, mImageView);
        }

        if(!TextUtils.isEmpty(caption)) {
            photoArrayList = dbHelper.getPhotosByCaption(caption);
            setImageBitmapForFilters(photoArrayList, mImageView);
        }

        if(!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
            photoArrayList = dbHelper.getPhotosByLatLong(latitude, longitude);
            setImageBitmapForFilters(photoArrayList, mImageView);
        }

    }

    private void setImageBitmapForFilters(ArrayList<Photo> photoArrayList, ImageView mImageView) {
        if(photoArrayList != null && photoArrayList.size() > 0) {
            currentPhotoIndex = photoArrayList.size() - 1;
            Bitmap bitmap = BitmapFactory.decodeFile(photoArrayList.get(currentPhotoIndex).getImage());
            mImageView.setImageBitmap(bitmap);
        }
    }

    private void processCapturedImage(String mCurrentPhotoPath) {
        File imgFile = new File(mCurrentPhotoPath);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            mImageView.setImageBitmap(bitmap);

            try {
                //byte[] byteArray = BitmapUtility.convertBitmapToByteArray(bitmap);

                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {}
                if (lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();
                    latitude = location.convert(currentLat, location.FORMAT_DEGREES);
                    longitude = location.convert(currentLong, location.FORMAT_DEGREES);
                } else if (lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                    location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    latitude = location.convert(currentLat, location.FORMAT_DEGREES);
                    longitude = location.convert(currentLong, location.FORMAT_DEGREES);
                } else {
                    currentLat = 37.422;
                    currentLong = -122.084;
                    latitude = location.convert(currentLat, location.FORMAT_DEGREES);
                    longitude = location.convert(currentLong, location.FORMAT_DEGREES);
                    Toast.makeText(getApplicationContext(), "GPS and Network not enabled. Defaulting " +
                            "to home coordinates.", Toast.LENGTH_SHORT);
                }

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());

                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                addresses = geocoder.getFromLocation(currentLat, currentLong, 1);

                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();

                Photo photo = new Photo(mCurrentPhotoPath, imgFile.getName(), timeStamp);
                dbHelper.addPhotoEntry(photo);

                LocationInfo locationInfo = new LocationInfo(latitude, longitude, address, city, state, country, postalCode);
                dbHelper.addLocationForPhoto(photo, locationInfo);

                photoArrayList = dbHelper.getAllPhotos();
                if(photoArrayList != null && photoArrayList.size() > 0) {
                    currentPhotoIndex = photoArrayList.size() - 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
