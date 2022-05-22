package src.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.src.R;

import src.Utils.My_images;

public class FragmentData extends Fragment {
    protected View view;
    private ImageView imageView1;
    private ImageView imageView2;

    public FragmentData() {
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
            view = inflater.inflate(R.layout.fragment_data, container, false);

        // bind variables
        bindVariables();
        setImages();
        return view;
    }

    private void bindVariables() {
        imageView1 = view.findViewById(R.id.data_IV_img1);
        imageView2 = view.findViewById(R.id.data_IV_img2);
    }

    private void setImages() {
        My_images images = My_images.getInstance();
        images.setImage(imageView1, ContextCompat.getDrawable(getContext(), R.drawable.image1));
        images.setImage(imageView2, ContextCompat.getDrawable(getContext(), R.drawable.image2));
    }
}