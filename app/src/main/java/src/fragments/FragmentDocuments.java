package src.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.src.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionCloudDocumentRecognizerOptions;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import src.Model.DocumentHelper;
import src.Utils.My_images;

public class FragmentDocuments extends Fragment {
    protected View view;
    private MaterialButton btnFinish;
    private ImageButton id;
    private ImageButton drivingLicense;
    private ImageButton carLicense;
    private CheckBox terms;
    private boolean errorFlag = false;
    private DocumentHelper documentHelper = new DocumentHelper();
    My_images images = My_images.getInstance();
    private CallBack_finishProcess callBack_finishProcess;

    public FragmentDocuments() {
        // Required empty public constructor
    }

    public static FragmentDocuments newInstance() {
        return new FragmentDocuments();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_documents, container, false);
        // bind variables
        bindVariables();
        btnFinish.setOnClickListener(v -> {
            if (!terms.isChecked()) {
                Toast.makeText(getContext(), "יש לאשר את תנאי השימוש", Toast.LENGTH_LONG).show();
            } else if (errorFlag) {
                Toast.makeText(getContext(), "טעינת התמונות נכשלה", Toast.LENGTH_LONG).show();
            } else {
                // recognize text from images and validate the data
                recognizeTextAndValidateData();
            }
        });
        return view;
    }

    private void recognizeTextAndValidateData() {
        // detect text from id image
        BitmapDrawable drawable = (BitmapDrawable) id.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        detectTextFromImage(bitmap, "id");
        // detect text from driving license image
        drawable = (BitmapDrawable) drivingLicense.getDrawable();
        bitmap = drawable.getBitmap();
        detectTextFromImage(bitmap, "driving");
        // detect text from car license image
        drawable = (BitmapDrawable) carLicense.getDrawable();
        bitmap = drawable.getBitmap();
        detectTextFromImage(bitmap, "car");
    }

    private void validateData() {
        final String desiredCity = "ראש העין"; // the city that the resident need to live in

        // end loading

    }

    public void setCallBack(CallBack_finishProcess callBack_finishProcess) {
        this.callBack_finishProcess = callBack_finishProcess;
    }

    private void bindVariables() {
        id = view.findViewById(R.id.documents_IMB_id);
        drivingLicense = view.findViewById(R.id.documents_IMB_drivingLicense);
        carLicense = view.findViewById(R.id.documents_IMB_carLicense);
        terms = view.findViewById(R.id.documents_CB_terms);
        btnFinish = view.findViewById(R.id.documents_BTN_finish);
        setListeners();
        setInitialImages();
    }

    private void setInitialImages() {
        images.setImage(id, ContextCompat.getDrawable(getContext(), R.drawable.add_id_picture_parkingticket));
        images.setImage(drivingLicense, ContextCompat.getDrawable(getContext(), R.drawable.driving_license));
        images.setImage(carLicense, ContextCompat.getDrawable(getContext(), R.drawable.car_license));
    }

    private void setListeners() {
        id.setOnClickListener(view -> {
            getImageFromGallery(1); // add_Id_IMAGE
        });
        drivingLicense.setOnClickListener(view -> {
            getImageFromGallery(2); // add_Driving_License_IMAGE
        });
        carLicense.setOnClickListener(view -> {
            getImageFromGallery(3); // add_Car_License_IMAGE
        });
    }

    private void getImageFromGallery(int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        requireActivity().startActivityForResult(intent, code);
    }

    // key - which image to detect text from
    private void detectTextFromImage(Bitmap bitmap, String key) {
        FirebaseVisionImage image = null;
        try {
            image = FirebaseVisionImage.fromBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FirebaseVisionCloudDocumentRecognizerOptions options =
                new FirebaseVisionCloudDocumentRecognizerOptions.Builder()
                        .setLanguageHints(Arrays.asList("iw", "en"))
                        .build();
        FirebaseVisionDocumentTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudDocumentTextRecognizer(options);

        detector.processImage(Objects.requireNonNull(image))
                .addOnSuccessListener(result -> {
                    // Task completed successfully
                    switch (key) {
                        case "id":
                            extractTextFromIdTextResult(result);
                            break;
                        case "car":
                            extractTextFromCarLicenseTextResult(result);
                            validateData();
                            break;
                        case "driving":
                            extractTextFromDrivingTextResult(result);
                            break;
                        default:
                            break;
                    }
                })
                .addOnFailureListener(e -> {
                    // Task failed with an exception
                    Log.d("ERROR", "failed to detect text!!!");
                    e.printStackTrace();
                    errorFlag = true;
                });
    }

    private void extractTextFromIdTextResult(FirebaseVisionDocumentText result) {
        List<FirebaseVisionDocumentText.Block> blockList = result.getBlocks();
        try {
            documentHelper.setId_from_idImage(blockList.get(5).getText());
            documentHelper.setExpDate_from_idImage(blockList.get(8).getText());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            errorFlag = true;
        }

    }

    private void extractTextFromDrivingTextResult(FirebaseVisionDocumentText result) {
        List<FirebaseVisionDocumentText.Block> blockList = result.getBlocks();
        try {
            documentHelper.setTypeOfLicense_from_drivingLicenseImage(blockList.get(0).getText());
            documentHelper.setAddress_from_drivingLicenseImage(blockList.get(5).getParagraphs().get(1).getText());
            documentHelper.setExpDate_from_drivingLicenseImage(blockList.get(6).getParagraphs().get(0).getText());
            documentHelper.setId_from_drivingLicenseImage(blockList.get(7).getParagraphs().get(0).getWords().get(1).getText());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            errorFlag = true;
        }

    }

    private void extractTextFromCarLicenseTextResult(FirebaseVisionDocumentText result) {
        List<FirebaseVisionDocumentText.Block> blockList = result.getBlocks();
        try {
            documentHelper.setCarNumber_from_carLicenseImage(blockList.get(3).getText());
            documentHelper.setId_from_carLicenseImage(blockList.get(7).getParagraphs().get(2).getWords().get(2).getText());
            documentHelper.setExpDate_from_carLicenseImage(blockList.get(14).getText());
            documentHelper.setTypeOfLicense_from_carLicenseImage(blockList.get(15).getParagraphs().get(3).getWords().get(3).getText());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            errorFlag = true;
        }

    }
}