package src.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.src.R;

import src.Utils.Common_utils;
import src.fragments.Fragment_data;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment(new Fragment_data());
    }

    /* start new fragment */
    private void initFragment(Fragment fragment) {
        if (Common_utils.getInstance().checkInternetConnection(this)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_FRAG_fragment, fragment);
            transaction.commit();
        }
    }
}