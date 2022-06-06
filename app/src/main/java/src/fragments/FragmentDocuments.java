package src.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
import java.util.Objects;
import java.util.regex.Pattern;

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
    private My_images images = My_images.getInstance();
    private CallBack_finishDataProcess callBack_finishDataProcess;
    private ProgressDialog progressDialog;

    public FragmentDocuments() {
        // Required empty public constructor
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
            errorFlag = false;
            if (!terms.isChecked()) {
                Toast.makeText(getContext(), "יש לאשר את תנאי השימוש", Toast.LENGTH_LONG).show();
            } else {
                progressDialog = ProgressDialog.show(getContext(), "מעבד נתונים", "בבקשה המתן...", false, false);
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
        boolean test1 = false, test2 = false, test3 = false, test4 = false;
        // the city that the resident need to live in
        final String desiredCity = "ראש העין";
        try {
            // check that the id's equals in all images
            test1 = documentHelper.getId_from_idImage().equals(documentHelper.getId_from_carLicenseImage()) &&
                    documentHelper.getId_from_carLicenseImage().equals(documentHelper.getId_from_drivingLicenseImage());
            // check that the type of license equals between car license and driving license
            test2 = documentHelper.getTypeOfLicense_from_carLicenseImage().trim().equals(documentHelper.getTypeOfLicense_from_drivingLicenseImage().trim());
            // check that the id image, car license, and driving license is valid (by expiration date)
            test3 = !documentHelper.isDateExpired(documentHelper.getExpDate_from_carLicenseImage())
                    && !documentHelper.isDateExpired(documentHelper.getExpDate_from_drivingLicenseImage())
                    && !documentHelper.isDateExpired(documentHelper.getExpDate_from_idImage());
            // check that the city in the driving license is equal to the desired city
            test4 = documentHelper.getAddress_from_drivingLicenseImage().contains(desiredCity);
            Log.d("test1", "" + test1);
            Log.d("test2", "" + test2);
            Log.d("test3", "" + test3);
            Log.d("test4", "" + test4);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("ודא שהתעודות בתוקף ושהפרטים תואמים בין התעודות");
        } finally {
            errorFlag = false;
        }

        if (test1 && test2 && test3 && test4) {
            callBack_finishDataProcess.finishDataProcess(documentHelper.getCarNumber_from_carLicenseImage());
        } else {
            showErrorDialog("ודא שהתעודות בתוקף ושהפרטים תואמים בין התעודות");
        }
        // end loading
        progressDialog.dismiss();
    }

    private void showErrorDialog(String messege) {
        new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom)
                .setTitle("שגיאה בזיהוי הפרטים")
                .setMessage(messege)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.try_again, null)
                .show();
    }

    public void setCallBack(CallBack_finishDataProcess callBack_finishProcess) {
        this.callBack_finishDataProcess = callBack_finishProcess;
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
                        .setLanguageHints(Arrays.asList("iw", "en", "ar"))
                        .build();
        FirebaseVisionDocumentTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudDocumentTextRecognizer(options);

        detector.processImage(Objects.requireNonNull(image))
                .addOnSuccessListener(result -> {
                    // Task completed successfully
                    switch (key) {
                        case "id":
                            if (!errorFlag && !extractTextFromIdTextResult(result)) {
                                progressDialog.dismiss();
                                showErrorDialog("תעודת הזהות לא צולמה כראוי או שאינה בתוקף");
                                errorFlag = true;
                            }
                            break;
                        case "car":
                            if (!errorFlag && !extractTextFromCarLicenseTextResult(result)) {
                                progressDialog.dismiss();
                                showErrorDialog("רישיון הרכב לא צולם כראוי או שאינו בתוקף");
                                errorFlag = true;
                            } else if (!errorFlag) {
                                validateData();
                            }
                            break;
                        case "driving":
                            if (!errorFlag && !extractTextFromDrivingTextResult(result)) {
                                progressDialog.dismiss();
                                showErrorDialog("רישיון הנהיגה לא צולם כראוי או שאינו בתוקף");
                                errorFlag = true;
                            }
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

    private boolean extractTextFromIdTextResult(FirebaseVisionDocumentText result) {
        boolean foundId = false;
        String expDate = "1.1.1930";
        try {
            for (FirebaseVisionDocumentText.Block block : result.getBlocks()) {
                for (FirebaseVisionDocumentText.Paragraph paragraph : block.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    // search for id
                    if (!foundId && checkIdConditions(paragraphText)) {
                        documentHelper.setId_from_idImage(paragraphText.trim().replace(" ", ""));
                        foundId = true;
                        continue;
                    }
                    // search for the latest date as expDate
                    if (paragraphText.contains(".") && paragraphText.length() > 6 && AfterDate(paragraphText, expDate)) {
                        expDate = paragraphText.trim();
                    }
                }
            }
            //Log.d("expDateIdImage: ", expDate);
            documentHelper.setExpDate_from_idImage(expDate);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // check if "paragraphText" date is later than expDate
    private boolean AfterDate(String paragraphText, String expDate) {
        String[] paraTextDate = paragraphText.split("\\.");
        String[] expDateDate = expDate.split("\\.");
        int paraTextYear = Integer.parseInt(paraTextDate[2].trim());
        int expDateYear = Integer.parseInt(expDateDate[2].trim());
        // if years are equal
        if (paraTextYear == expDateYear) {
            // check by month
            // remove leading zero from month
            if (paraTextDate[1].charAt(0) == '0') paraTextDate[1] = paraTextDate[1].substring(1);
            int paragraphText_month = Integer.parseInt(paraTextDate[1]);
            // remove leading zero from month
            if (expDateDate[1].charAt(0) == '0') expDateDate[1] = expDateDate[1].substring(1);
            int expDateMonth = Integer.parseInt(expDateDate[1]);
            return expDateMonth < paragraphText_month;
        } else {
            // check by year
            return expDateYear < paraTextYear;
        }
    }

    // check that the id number is valid
    private boolean checkIdConditions(String paragraphText) {
        String text = paragraphText.trim().replace(" ", "");
        boolean length = text.length() == 9;
        boolean firstChar = text.charAt(0) == '0' || text.charAt(0) == '2' || text.charAt(0) == '3';
        return length && firstChar;
    }

    private boolean extractTextFromDrivingTextResult(FirebaseVisionDocumentText result) {
        boolean foundTypeOfLicense = false, foundAddress = false, foundExpDate = false, foundId = false;
        try {
            for (FirebaseVisionDocumentText.Block block : result.getBlocks()) {
                for (FirebaseVisionDocumentText.Paragraph paragraph : block.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    if (!foundTypeOfLicense && paragraphText.contains("9.")) {
                        documentHelper.setTypeOfLicense_from_drivingLicenseImage(paragraphText.split("\\.")[1].trim());
                        //Log.d("typeDrivingImage: ", documentHelper.getTypeOfLicense_from_drivingLicenseImage());
                        foundTypeOfLicense = true;
                        continue;
                    }
                    if (!foundAddress && paragraphText.contains(".8")) {
                        documentHelper.setAddress_from_drivingLicenseImage(paragraphText.split("\\.")[0]);
                        //Log.d("addressDrivingImage: ", documentHelper.getAddress_from_drivingLicenseImage());
                        foundAddress = true;
                        continue;
                    }
                    if (!foundExpDate && paragraphText.contains("4b.")) {
                        documentHelper.setExpDate_from_drivingLicenseImage(paragraphText.substring(3, 14).trim());
                        //Log.d("expDatedrivingImage: ", documentHelper.getExpDate_from_drivingLicenseImage());
                        foundExpDate = true;
                    }
                    if (!foundId && paragraphText.contains("ID")) {
                        documentHelper.setId_from_drivingLicenseImage(paragraphText.trim().split("D")[1].trim());
                        //Log.d("idDrivingImage: ", documentHelper.getId_from_drivingLicenseImage());
                        foundId = true;
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean extractTextFromCarLicenseTextResult(FirebaseVisionDocumentText result) {
        boolean foundCarNumber = false, foundId = false, foundType = false;
        String expDate = "1.1.1930";
        try {
            for (FirebaseVisionDocumentText.Block block : result.getBlocks()) {
                for (FirebaseVisionDocumentText.Paragraph paragraph : block.getParagraphs()) {
                    String paragraphText = paragraph.getText();
                    // id
                    if (!foundId && paragraphText.contains("-") && !paragraphText.contains("דרגת")) {
                        for (FirebaseVisionDocumentText.Word word : paragraph.getWords()) {
                            if (word.getText().length() == 10 && word.getText().contains("-")) {
                                documentHelper.setId_from_carLicenseImage(word.getText().replace("-", "").trim());
                                //Log.d("IdFromcarLicense: ", documentHelper.getId_from_carLicenseImage());
                                foundId = true;
                            }
                        }
                    }
                    // car number
                    if (!foundCarNumber && paragraph.getText().trim().length() == 8 && !paragraph.getText().contains("/")) {
                        try {
                            int carNumber = Integer.parseInt(paragraph.getText().trim());
                            documentHelper.setCarNumber_from_carLicenseImage(String.valueOf(carNumber));
                            Log.d("carNumber: ", documentHelper.getCarNumber_from_carLicenseImage());
                            foundCarNumber = true;
                        } catch (Exception ignored) {

                        }
                    }
                    // expiration date
                    if (paragraphText.contains("/")) {
                        for (FirebaseVisionDocumentText.Word word : paragraph.getWords()) {
                            if (Pattern.matches("^\\d{2}/\\d{2}/\\d{4}$", word.getText().replace(".", ""))
                                    && AfterDate(word.getText().replace(".", "").replace("/", ".").trim(), expDate)) {
                                expDate = word.getText().replace(".", "").replace("/", ".").trim();
                                documentHelper.setExpDate_from_carLicenseImage(expDate);
                                //Log.d("expDateCarLicense: ", documentHelper.getExpDate_from_carLicenseImage());
                            }
                        }
                    }

                    // type of license
                    if (!foundType && paragraphText.contains("רשיון")) {
                        String[] words = paragraphText.split(" ");
                        for (String word : words) {
                            if (word.length() == 1 || word.length() == 2) {
                                documentHelper.setTypeOfLicense_from_carLicenseImage(word);
                                //Log.d("typeFromCarLicense: ", documentHelper.getTypeOfLicense_from_carLicenseImage());
                                foundType = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}