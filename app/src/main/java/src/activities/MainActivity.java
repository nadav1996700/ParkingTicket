package src.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.src.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.Objects;

import src.Model.Car;
import src.Model.PersonalDetails;
import src.Model.Pticket;
import src.Model.Residential;
import src.Utils.My_Firebase;
import src.Utils.My_images;
import src.Utils.SendMail;
import src.fragments.CallBack_changeFragmentPersonal;
import src.fragments.CallBack_changeFragmentResidential;
import src.fragments.CallBack_finishDataProcess;
import src.fragments.CallBack_finishWithSuccess;
import src.fragments.FragmentDocuments;
import src.fragments.FragmentPersonalInfo;
import src.fragments.FragmentResidential;
import src.fragments.FragmentSuccess;

public class MainActivity extends AppCompatActivity implements
        CallBack_changeFragmentPersonal,
        CallBack_changeFragmentResidential,
        CallBack_finishDataProcess,
        CallBack_finishWithSuccess {

    private BottomNavigationView bnv;
    private StateProgressBar stateProgressBar;
    private My_images images = My_images.getInstance();
    private final String[] descriptionData = {"אישי", "מגורים", "מסמכים"};
    private final Car car = new Car();
    private final My_Firebase db = My_Firebase.getInstance();
    private Pticket pticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        FragmentPersonalInfo fragmentPersonalInfo = new FragmentPersonalInfo();
        fragmentPersonalInfo.setCallBack(this);
        initFragment(fragmentPersonalInfo);
        bnv.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.page_2:
                    intent = new Intent(MainActivity.this, DataActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.page_3:
                    intent = new Intent(MainActivity.this, StatusActivity.class);
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
        transaction.replace(R.id.main_FRAG_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* initialize variables */
    private void initVariables() {
        bnv = findViewById(R.id.main_NAV_navigation);
        bnv.setSelectedItemId(R.id.page_1);
        stateProgressBar = findViewById(R.id.state_progress_bar);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.enableAnimationToCurrentState(true);
        stateProgressBar.setAnimationDuration(3000);
    }

    // update car object with personal details and move to residential fragment
    @Override
    public void changeFragmentPersonal(PersonalDetails personalDetails) {
        car.setPersonalDetails(personalDetails);
        FragmentResidential fragmentResidential = new FragmentResidential();
        fragmentResidential.setCallBack(this);
        initFragment(fragmentResidential);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
    }

    // update car object with residential details and move to documentDetails fragment
    @Override
    public void changeFragmentResidential(Residential residential) {
        car.setResidential(residential);
        FragmentDocuments fragmentDocuments = new FragmentDocuments();
        fragmentDocuments.setCallBack(this);
        initFragment(fragmentDocuments);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
    }

    @Override
    public void finishDataProcess(String carId) {
        stateProgressBar.setVisibility(View.GONE);
        car.setCarId(carId);
        generatePticketId();
    }

    private void generatePticketId() {
        db.setReference("/lastTicketId");
        db.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pticket = new Pticket((Long) (Objects.requireNonNull(snapshot.getValue())) + 1);
                db.setReference("/lastTicketId");
                db.getReference().setValue(pticket.getTicketId());
                sendParkingTicketByEmail();
                saveParkingTicketOnDB();
                initFragmentSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initFragmentSuccess() {
        FragmentSuccess fragmentSuccess = new FragmentSuccess();
        fragmentSuccess.setCallBack(this);
        initFragment(fragmentSuccess);
    }

    private void saveParkingTicketOnDB() {
        car.setPticket(pticket);
        db.setReference("/" + car.getCarId() + "/");
        db.getReference().setValue(car);
    }

    private void sendParkingTicketByEmail() {
        String email = car.getPersonalDetails().getEmail();
        String subject = "תו חניה";
        String message =
                "תו החניה שלך עבור רכב שמספרו: " + car.getCarId() + " הונפק בהצלחה." +
                        " ותוקפו למשך שלוש שנים, מספר תו החניה: " + pticket.getTicketId();
        SendMail sendMail = new SendMail(email, subject, message);
        sendMail.execute();
    }

    // return one state back or get out of the app
    @Override
    public void onBackPressed() {
        if (stateProgressBar.getCurrentStateNumber() == 1) {
            finish();
        } else {
            stateProgressBar.setCurrentStateNumber(getPrevStateNumber(stateProgressBar.getCurrentStateNumber()));
            super.onBackPressed();
        }
    }

    // return "StateProgressBar.StateNumber" object that represents the previews state number
    private StateProgressBar.StateNumber getPrevStateNumber(int currentStateNumber) {
        switch (currentStateNumber) {
            case 3:
                return StateProgressBar.StateNumber.TWO;
            case 4:
                return StateProgressBar.StateNumber.THREE;
            default:
                return StateProgressBar.StateNumber.ONE;
        }
    }

    /* handle images from gallery */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // id image
            Drawable photo = images.convertDataToDrawable(data);
            if (photo != null) {
                ImageButton imageButton = findViewById(R.id.documents_IMB_id);
                images.setImage(imageButton, photo);
            }
        } else if (requestCode == 2) { // Add_Driving_License_IMAGE
            Drawable photo = images.convertDataToDrawable(data);
            if (photo != null) {
                ImageButton imageButton = findViewById(R.id.documents_IMB_drivingLicense);
                images.setImage(imageButton, photo);
            }
        } else if (requestCode == 3) { // Add_Car_License_IMAGE
            Drawable photo = images.convertDataToDrawable(data);
            if (photo != null) {
                ImageButton imageButton = findViewById(R.id.documents_IMB_carLicense);
                images.setImage(imageButton, photo);
            }
        }
    }

    @Override
    public void returnToDataActivity() {
        startActivity(new Intent(MainActivity.this, DataActivity.class));
        finish();
    }
}