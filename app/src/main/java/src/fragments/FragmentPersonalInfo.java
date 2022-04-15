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

import src.Utils.DatePickerFragment;

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
        pickDate.setOnClickListener(v -> showDatePickerDialog(v));
        return view;
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
}