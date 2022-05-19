package src.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.src.R;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.Date;
import java.util.regex.Pattern;

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
    private CallBack_changeFragmentPersonal callBack_changeFragmentPersonal;

    public FragmentPersonalInfo() {
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
            view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        // bind variables
        bindVariables();
        // load data from sharedPreferences
        loadData();
        pickDate.setOnClickListener(this::showDatePickerDialog);
        btnContinue.setOnClickListener(v -> {
            if (validateData()) {
                saveData();
                callBack_changeFragmentPersonal.changeFragmentPersonal(pd);
            }
        });
        return view;
    }

    public void setCallBack(CallBack_changeFragmentPersonal callBack_changeFragmentPersonal) {
        this.callBack_changeFragmentPersonal = callBack_changeFragmentPersonal;
    }

    // save data to shared preferences
    private void saveData() {
        pd = new PersonalDetails();
        pd.setId(id.getText().toString());
        pd.setFirstName(firstName.getText().toString());
        pd.setLastName(lastName.getText().toString());
        pd.setDateOfBirth(birthday.getText().toString());
        pd.setEmail(email.getText().toString());
        pd.setPhone(phone.getText().toString());
        sp.saveObject(pd, "PersonalData");
    }

    private boolean validateData() {
        if (!Pattern.matches("[0-9]{9}", id.getText().toString())) {
            id.setError("תעודת זהות חייבת להיות מורכבת מ-9 ספרות בלבד!");
            return false;
        } else if (!Pattern.matches("^(.+)@(.+)$", email.getText().toString())) {
            email.setError("אימייל לא תיקני!");
            return false;
        } else if (!Pattern.matches("[0-9]{9,10}", phone.getText().toString())) {
            phone.setError("טלפון מורכב מתשע או עשר ספרות בלבד!");
            return false;
        }
        return true;
    }

    // load data from shared preferences
    private void loadData() {
        // convert json data to PersonalDetails object
        Gson gson = new Gson();
        String json = sp.loadData("PersonalData");
        if (json != null) {
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