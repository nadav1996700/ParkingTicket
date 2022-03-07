package src.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.src.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import src.Utils.Common_utils;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bnv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        bnv.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.page_2:
                    intent = new Intent(MainActivity.this, DataActivity.class);
                    startActivity(intent);
                    break;
                case R.id.page_3:
                    intent = new Intent(MainActivity.this, StatusActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /* start new fragment */
    private void initFragment(Fragment fragment) {
        if (Common_utils.getInstance().checkInternetConnection(this)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_FRAG_fragment, fragment);
            transaction.commit();
        }
    }

    /* initialize variables */
    private void initVariables() {
        bnv = findViewById(R.id.main_NAV_navigation);
        bnv.setSelectedItemId(R.id.page_1);
    }
}