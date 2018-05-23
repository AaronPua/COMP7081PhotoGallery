package comp7081.photogallery.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapUtility {

    // Convert from bitmap to byte array
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        stream.close();
        return stream.toByteArray();
    }

    // Convert from byte array to bitmap
    public static Bitmap getImageFromBitmap(byte[] image) {
        if(image != null) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        return null;
    }
}
