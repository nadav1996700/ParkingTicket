package src.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import com.example.src.R;
import com.google.android.material.button.MaterialButton;

import src.Model.Residential;
import src.Utils.My_SP;

public class FragmentDocuments extends Fragment {
    protected View view;
    private MaterialButton btnFinish;
    private My_SP sp = My_SP.getInstance();
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
            view = inflater.inflate(R.layout.fragment_residential, container, false);
        // bind variables
        bindVariables();
        // load data from sharedPreferences
        loadData();
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

            }
        });
        return view;
    }

    public void setCallBack(CallBack_finishProcess callBack_finishProcess) {
        this.callBack_finishProcess = callBack_finishProcess;
    }

    private void bindVariables() {

    }

    private void loadData() {

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