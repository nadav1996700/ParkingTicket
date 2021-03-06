package src.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.src.R;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.regex.Pattern;

import src.Model.Residential;
import src.Model.ResidentialState;
import src.Utils.My_SP;

public class FragmentResidential extends Fragment {
    protected View view;
    private EditText city;
    private EditText street;
    private EditText houseNumber;
    private EditText apartmentNumber;
    private EditText postalCode;
    private RadioGroup rg;
    private RadioButton permanent;
    private RadioButton rent;
    private MaterialButton btnContinue;
    private Residential residential = new Residential();
    private My_SP sp = My_SP.getInstance();
    private CallBack_changeFragmentResidential callBack_changeFragmentResidential;

    public FragmentResidential() {
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
            view = inflater.inflate(R.layout.fragment_residential, container, false);
        // bind variables
        bindVariables();
        // load data from sharedPreferences
        loadData();
        btnContinue.setOnClickListener(v -> {
            if (validateData()) {
                saveData();
                callBack_changeFragmentResidential.changeFragmentResidential(residential);
            }
        });
        return view;
    }

    public void setCallBack(CallBack_changeFragmentResidential callBack_changeFragmentResidential) {
        this.callBack_changeFragmentResidential = callBack_changeFragmentResidential;
    }

    private void bindVariables() {
        city = view.findViewById(R.id.residential_EDT_city);
        street = view.findViewById(R.id.residential_EDT_street);
        houseNumber = view.findViewById(R.id.residential_EDT_houseNumber);
        apartmentNumber = view.findViewById(R.id.residential_EDT_apartmentNumber);
        postalCode = view.findViewById(R.id.residential_EDT_postalCode);
        rg = view.findViewById(R.id.residential_RG);
        permanent = view.findViewById(R.id.residential_RB_permanent);
        rent = view.findViewById(R.id.residential_RB_rent);
        btnContinue = view.findViewById(R.id.residential_BTN_continue);
        setRadioGroupListener();
    }

    public void setRadioGroupListener() {
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if (permanent.isChecked()) {
                residential.setResidentialState(ResidentialState.PERMANENTTENANT);
            } else {
                residential.setResidentialState(ResidentialState.TENANTINRENT);
            }
        });
    }

    private void loadData() {
        // convert json data to Residential object
        Gson gson = new Gson();
        String json = sp.loadData("ResidentialData");
        if (json != null) {
            residential = gson.fromJson(json, Residential.class);
            // fill the form with the data
            city.setText(residential.getCity());
            street.setText(residential.getStreet());
            houseNumber.setText(residential.getHouseNumber());
            apartmentNumber.setText(residential.getApartmentNumber());
            postalCode.setText(residential.getPostalCode());
            if (residential.getResidentialState() == ResidentialState.PERMANENTTENANT) {
                permanent.setChecked(true);
                rent.setChecked(false);
            } else {
                permanent.setChecked(false);
                rent.setChecked(true);
            }
        }

    }

    private void saveData() {
        residential.setCity(city.getText().toString());
        residential.setApartmentNumber(apartmentNumber.getText().toString());
        residential.setHouseNumber(houseNumber.getText().toString());
        residential.setPostalCode(postalCode.getText().toString());
        residential.setStreet(street.getText().toString());
        sp.saveObject(residential, "ResidentialData");
    }

    private boolean validateData() {
        if (!Pattern.matches("[0-9]{1,3}", houseNumber.getText().toString())) {
            houseNumber.setError("???????? ?????? ???????? ??????????!");
            return false;
        } else if (!Pattern.matches("[0-9]{1,2}", apartmentNumber.getText().toString())) {
            apartmentNumber.setError("???????? ???????? ???????? ??????????!");
            return false;
        } else if (!Pattern.matches("[0-9]{7}", postalCode.getText().toString())) {
            postalCode.setError("?????????? ???????? ??????????!");
            return false;
        }
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