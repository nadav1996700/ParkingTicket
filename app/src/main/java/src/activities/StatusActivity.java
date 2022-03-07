package src.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.src.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatusActivity extends AppCompatActivity {
    private BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        initVariables();
        bnv.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.page_1:
                    intent = new Intent(StatusActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.page_2:
                    intent = new Intent(StatusActivity.this, DataActivity.class);
                    startActivity(intent);
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
        bnv.setSelectedItemId(R.id.page_3);
    }
}