package src.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.src.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DataActivity extends AppCompatActivity {
    private BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        initVariables();
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