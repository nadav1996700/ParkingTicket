package src.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.src.R;
import com.google.android.material.button.MaterialButton;

import src.Utils.My_images;

public class FragmentSuccess extends Fragment {
    protected View view;
    private ImageView successImage;
    private MaterialButton finishButton;
    private CallBack_finishWithSuccess callback;

    public FragmentSuccess() {
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
            view = inflater.inflate(R.layout.fragment_success, container, false);
        // bind variables
        bindVariables();
        // load image
        loadCenterImage();
        finishButton.setOnClickListener(view -> callback.returnToDataActivity());
        return view;
    }

    public void setCallBack(CallBack_finishWithSuccess callBack_finishWithSuccess) {
        this.callback = callBack_finishWithSuccess;
    }

    private void loadCenterImage() {
        My_images images = My_images.getInstance();
        images.setImage(successImage, ContextCompat.getDrawable(getContext(), R.drawable.ic_success_foreground));
    }


    private void bindVariables() {
        successImage = view.findViewById(R.id.success_IMV_successImage);
        finishButton = view.findViewById(R.id.success_BTN_back);
    }
}