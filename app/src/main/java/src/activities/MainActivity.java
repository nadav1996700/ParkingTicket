package src.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.src.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kofigyan.stateprogressbar.StateProgressBar;

import src.Model.Car;
import src.Model.CarDetails;
import src.Model.PersonalDetails;
import src.Model.Residential;
import src.fragments.CallBack_changeFragmentCarDetails;
import src.fragments.CallBack_changeFragmentPersonal;
import src.fragments.CallBack_changeFragmentResidential;
import src.fragments.CallBack_finishProcess;
import src.fragments.FragmentCar;
import src.fragments.FragmentDocuments;
import src.fragments.FragmentPersonalInfo;
import src.fragments.FragmentResidential;

public class MainActivity extends AppCompatActivity implements
        CallBack_changeFragmentPersonal,
        CallBack_changeFragmentResidential,
        CallBack_changeFragmentCarDetails,
        CallBack_finishProcess {

    private BottomNavigationView bnv;
    private StateProgressBar stateProgressBar;
    private final String[] descriptionData = {"אישי", "מגורים", "רכב", "מסמכים"};
    private final Car car = new Car();

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

    // update car object with car details and move to documents fragment
    @Override
    public void changeFragmentCarDetails(CarDetails carDetails) {
        car.setCarDetails(carDetails);
        FragmentDocuments fragmentDocuments = new FragmentDocuments();
        fragmentDocuments.setCallBack(this);
        initFragment(fragmentDocuments);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
    }

    // update car object with residential details and move to carDetails fragment
    @Override
    public void changeFragmentResidential(Residential residential) {
        car.setResidential(residential);
        FragmentCar fragmentCar = new FragmentCar();
        fragmentCar.setCallBack(this);
        initFragment(fragmentCar);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
    }

    @Override
    public void finishProcess() {

    }

    // return one state back
    @Override
    public void onBackPressed() {
        stateProgressBar.setCurrentStateNumber(getPrevStateNumber(stateProgressBar.getCurrentStateNumber()));
        super.onBackPressed();
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
}