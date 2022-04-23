package src.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import com.example.src.R;
import com.google.android.material.button.MaterialButton;

import src.Utils.My_SP;
import src.Utils.My_images;
import src.Utils.Ocr;

public class FragmentDocuments extends Fragment {
    protected View view;
    private MaterialButton btnFinish;
    private ImageButton id;
    private ImageButton drivingLicense;
    private ImageButton carLicense;
    private RadioButton terms;
    private Ocr ocr = Ocr.getInstance();
    My_SP sp = My_SP.getInstance();
    My_images images = My_images.getInstance();
    private CallBack_finishProcess callBack_finishProcess;

    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    //private String mParam1;
    //private String mParam2;

    public FragmentDocuments() {
        // Required empty public constructor
    }

    public static FragmentDocuments newInstance(String param1, String param2) {
        FragmentDocuments fragment = new FragmentDocuments();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_documents, container, false);
        // bind variables
        bindVariables();
        // load data from sharedPreferences
        loadData();
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                BitmapDrawable drawable = (BitmapDrawable) id.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ocr.setBitmap(bitmap);
                String text = ocr.getTextFromImage();
                Log.d("MYTEXT", "" + text);
            }
        });
        return view;
    }

    public void setCallBack(CallBack_finishProcess callBack_finishProcess) {
        this.callBack_finishProcess = callBack_finishProcess;
    }

    private void bindVariables() {
        id = view.findViewById(R.id.documents_IMB_id);
        drivingLicense = view.findViewById(R.id.documents_IMB_drivingLicense);
        carLicense = view.findViewById(R.id.documents_IMB_carLicense);
        terms = view.findViewById(R.id.documents_RB_terms);
        btnFinish = view.findViewById(R.id.documents_BTN_finish);
        setListeners();
    }

    private void setListeners() {
        terms.setOnClickListener(view -> {
            terms.setChecked(!terms.isChecked());
        });

        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery(1); // add_Id_IMAGE
            }
        });
        drivingLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery(2); // add_Driving_License_IMAGE
            }
        });
        carLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery(3); // add_Car_License_IMAGE
            }
        });
    }

    private void getImageFromGallery(int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        requireActivity().startActivityForResult(intent, code);
    }

    private void loadData() {
        //My_Firebase.getInstance().setReference("/2356534/carDetails/carId");
        //My_Firebase.getInstance().getReference().setValue("1234444");
        // load initial images
        //final String basis_path = "gs://parking-department-rh.appspot.com/parkingTicketApp/";
        //images.downloadImageUrl(basis_path + "add_id_picture_parkingticket.png", id);
        //images.downloadImageUrl(basis_path + "drivingLicense.png", drivingLicense);
        //images.downloadImageUrl(basis_path + "carLicense.jpg", carLicense);
    }

    private void saveData() {

    }

    private boolean validateData() {
        return true;
    }

    @Override
    public void onPause() {
        saveData();
        super.onPause();
    }

    @Override
    public void onResume() {
        loadData();
        super.onResume();
    }
}