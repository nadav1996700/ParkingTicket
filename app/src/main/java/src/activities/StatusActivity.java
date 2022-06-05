package src.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.src.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

import src.Utils.My_Firebase;

public class StatusActivity extends AppCompatActivity {
    private BottomNavigationView bnv;
    private EditText parkingTicketNumber;
    private EditText carNumber;
    private Button checkButton;

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

        carNumber.setOnClickListener(view -> carNumber.setError(null));

        parkingTicketNumber.setOnClickListener(view -> parkingTicketNumber.setError(null));

        checkButton.setOnClickListener(view -> checkParkingTicketStatus());
    }

    private void checkParkingTicketStatus() {
        carNumber.setError(null);
        parkingTicketNumber.setError(null);
        String _carNumber = carNumber.getText().toString();
        String _parkingTicketNumber = parkingTicketNumber.getText().toString();
        if (_carNumber.length() < 6) {
            carNumber.setError("מספר רכב חייב להיות בן 6 ספרות לפחות");
        } else if (_parkingTicketNumber.isEmpty()) {
            parkingTicketNumber.setError("הקלד מספר תו חניה!");
        } else {
            checkOnDB(_carNumber, _parkingTicketNumber);
        }
    }

    private void checkOnDB(String _carNumber, String _parkingTicketNumber) {
        My_Firebase db = My_Firebase.getInstance();
        db.setReference("/" + _carNumber);
        db.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    showErrorDialog("מספר הרכב שהוקלד לא קיים במערכת");
                } else {
                    long ticketId = (long) snapshot.child("/pticket/ticketId").getValue();
                    if (ticketId != Long.parseLong(_parkingTicketNumber)) {
                        showErrorDialog("מספר תו החניה לא תואם למספר הרכב או שאינו קיים");
                    } else {
                        String expDateStr = Objects.requireNonNull(snapshot.child("/pticket/expirationDate").getValue()).toString();
                        long expDate = Long.parseLong(expDateStr);
                        long millis = System.currentTimeMillis();
                        if (expDate < millis) {
                            // date expired
                            showErrorDialog("תו החניה לא בתוקף!");
                        } else {
                            // parking ticket is valid
                            showSuccessDialog(expDate);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.getMessage());
            }
        });
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("שגיאה בזיהוי הפרטים")
                .setMessage(message)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.close, null)
                .show();
    }

    private void showSuccessDialog(long expDate) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(expDate);
        String date = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
        String message = "תו החניה בתוקף עד לתאריך: " + date;
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("תוצאות הבדיקה")
                .setMessage(message)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.close, null)
                .show();
    }

    /* initialize variables */
    private void initVariables() {
        bnv = findViewById(R.id.main_NAV_navigation);
        bnv.setSelectedItemId(R.id.page_3);
        parkingTicketNumber = findViewById(R.id.status_EDT_parkingTicketNumber);
        carNumber = findViewById(R.id.status_EDT_carNumber);
        checkButton = findViewById(R.id.status_BTN_check);
    }
}