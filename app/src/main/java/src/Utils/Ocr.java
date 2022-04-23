package src.Utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;

public class Ocr {
    private static Ocr instance;
    private Bitmap bitmap;
    private FirebaseFunctions mFunctions;
    private String result;

    private Ocr() {
    }

    public static Ocr getInstance() {
        return instance;
    }

    public static Ocr initHelper() {
        if (instance == null)
            instance = new Ocr();
        return instance;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    // Convert bitmap to base64 encoded string
    private String convertBitmapToBase64() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }


    public synchronized String getTextFromImage() {
        mFunctions = FirebaseFunctions.getInstance();
        String base64encoded = convertBitmapToBase64();

        // Create json request to cloud vision
        JsonObject request = new JsonObject();
        // Add image to request
        JsonObject image = new JsonObject();
        image.add("content", new JsonPrimitive(base64encoded));
        request.add("image", image);
        //Add features to the request
        JsonObject feature = new JsonObject();
        feature.add("type", new JsonPrimitive("TEXT_DETECTION"));
        // Alternatively, for DOCUMENT_TEXT_DETECTION:
        //feature.add("type", new JsonPrimitive("DOCUMENT_TEXT_DETECTION"));
        JsonArray features = new JsonArray();
        features.add(feature);
        request.add("features", features);

        // provide language hints to assist with language detection
        JsonObject imageContext = new JsonObject();
        JsonArray languageHints = new JsonArray();
        languageHints.add("iw");
        imageContext.add("languageHints", languageHints);
        request.add("imageContext", imageContext);

        annotateImage(request.toString())
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // Task failed with an exception
                        Log.d("ERROR", "error on ocr process");
                        result = "error";
                    } else {
                        // Task completed successfully
                        JsonObject annotation = task.getResult().getAsJsonArray().get(0).
                                getAsJsonObject().get("fullTextAnnotation").getAsJsonObject();
                        result = annotation.get("text").getAsString();
                    }
                });

        // wait for result
        while(result == null) {}
        return result;
    }

    private Task<JsonElement> annotateImage(String requestJson) {
        return mFunctions
                .getHttpsCallable("annotateImage")
                .call(requestJson)
                .continueWith(task -> {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.
                    return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
                });
    }
}
