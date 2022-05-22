package src.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.src.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import src.fragments.FragmentData;

public class DataActivity extends AppCompatActivity {
    private BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        initVariables();
        initFragment(new FragmentData());
        bnv.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.page_1:
                    intent = new Intent(DataActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.page_3:
                    intent = new Intent(DataActivity.this, StatusActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /* start new fragment */
    private void initFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                );
        transaction.replace(R.id.data_FRAG_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* initialize variables */
    private void initVariables() {
        bnv = findViewById(R.id.main_NAV_navigation);
        bnv.setSelectedItemId(R.id.page_2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}