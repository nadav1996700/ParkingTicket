package src.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.src.R;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import src.Model.PersonalDetails;
import src.Utils.DatePickerFragment;
import src.Utils.My_SP;

public class FragmentPersonalInfo extends Fragment {
    protected View view;
    private EditText firstName;
    private EditText lastName;
    private EditText birthday;
    private EditText id;
    private EditText email;
    private EditText phone;
    private ImageButton pickDate;
    private MaterialButton btnContinue;
    private PersonalDetails pd;
    private My_SP sp = My_SP.getInstance();

    public FragmentPersonalInfo() {
        // Required empty public constructor
    }

    public static FragmentPersonalInfo newInstance(String param1, String param2) {
        FragmentPersonalInfo fragment = new FragmentPersonalInfo();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        // bind variables
        bindVariables();
        // load data from sharedPreferences
        loadData();
        pickDate.setOnClickListener(v -> showDatePickerDialog(v));
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

            }
        });
        return view;
    }

    // save data to shared preferences
    private void saveData() {
        if(validateData()) {
            pd = new PersonalDetails();
            pd.setId(id.getText().toString());
            pd.setFirstName(firstName.getText().toString());
            pd.setLastName(lastName.getText().toString());
            pd.setDateOfBirth(birthday.getText().toString());
            pd.setEmail(email.getText().toString());
            pd.setPhone(phone.getText().toString());
            sp.saveObject(pd,"PersonalData");
        }
    }

    private boolean validateData() {
        return true;
    }

    // load data from shared preferences
    private void loadData() {
        // convert json data to PersonalDetails object
        Gson gson = new Gson();
        String json = sp.loadData("PersonalData");
        if(json != null) {
            pd = gson.fromJson(json, PersonalDetails.class);
            // fill the form with the data
            firstName.setText(pd.getFirstName());
            lastName.setText(pd.getLastName());
            id.setText(pd.getId());
            email.setText(pd.getEmail());
            phone.setText(pd.getPhone());
            birthday.setText(pd.getDateOfBirth());
        }
    }

    public void showDatePickerDialog(View v) {
        try {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.setEditText(birthday);
            newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
        } catch (NullPointerException ex) {
            Log.d("ERROR", "ERROR ON FRAGMENT MANAGER");
        }
    }

    private void bindVariables() {
        firstName = view.findViewById(R.id.personalInfo_EDT_firstName);
        lastName = view.findViewById(R.id.personalInfo_EDT_lastName);
        birthday = view.findViewById(R.id.personalInfo_EDT_pickDate);
        id = view.findViewById(R.id.personalInfo_EDT_id);
        email = view.findViewById(R.id.personalInfo_EDT_email);
        phone = view.findViewById(R.id.personalInfo_EDT_phone);
        btnContinue = view.findViewById(R.id.personalInfo_BTN_continue);
        pickDate = view.findViewById(R.id.personalInfo_BTN_pickDate);
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