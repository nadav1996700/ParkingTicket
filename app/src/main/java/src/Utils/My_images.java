package src.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class My_images {
    private static My_images instance;
    private Context context;

    private My_images(Context context) {
        this.context = context;
    }

    public static My_images getInstance() {
        return instance;
    }

    public static My_images initHelper(Context context) {
        if (instance == null)
            instance = new My_images(context);
        return instance;
    }

    /* download image from firebase reference into placeholder */
    public void downloadImageUrl(String ref, final ImageView imageView) {
        My_Firebase.getInstance().setStorage_reference(ref);
        StorageReference reference = My_Firebase.getInstance().getStorage_reference();
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                setImageByUri(uri, imageView);
            }
        });
    }

    /* upload image from imageView to firebase storage */
    public void uploadImage(ImageView imageView, String ref) {
        // set storage reference
        My_Firebase.getInstance().setStorage_reference(ref);
        StorageReference reference = My_Firebase.getInstance().getStorage_reference();
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        reference.putBytes(data);
    }

    private void setImageByUri(Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    /* set images using glide library (by drawable) */
    public void setImage(Drawable photo, ImageView imageView) {
        //ImageView imageView = activity.findViewById(download_placeholder);
        Glide.with(context)
                .load(photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    /* convert intent data (from gallery) into drawable */
    public Drawable convertDataToDrawable(Intent data) {
        try {
            final Uri imageUri = data.getData();
            final InputStream imageStream;
            imageStream = context.getContentResolver().openInputStream(Objects.requireNonNull(imageUri));
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            return new BitmapDrawable(context.getResources(), selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
