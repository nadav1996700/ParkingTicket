package src.Model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DocumentHelper {
    private String id_from_idImage;
    private String expDate_from_idImage;
    private String typeOfLicense_from_drivingLicenseImage;
    private String address_from_drivingLicenseImage;
    private String id_from_drivingLicenseImage;
    private String expDate_from_drivingLicenseImage;
    private String typeOfLicense_from_carLicenseImage;
    private String id_from_carLicenseImage;
    private String expDate_from_carLicenseImage;
    private String carNumber_from_carLicenseImage;

    public DocumentHelper() {
    }

    public boolean isDateExpired(String date) {
        Date expiry = null;
        try {
            expiry = new SimpleDateFormat("dd.MM.yyyy").parse(date);
            Log.d("Date:", "" + expiry);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(expiry).before(new Date());
    }

    public String getId_from_idImage() {
        return id_from_idImage;
    }

    public void setId_from_idImage(String id_from_idImage) {
        this.id_from_idImage = id_from_idImage;
    }

    public String getExpDate_from_idImage() {
        return expDate_from_idImage;
    }

    public void setExpDate_from_idImage(String expDate_from_idImage) {
        this.expDate_from_idImage = expDate_from_idImage;
    }

    public String getTypeOfLicense_from_drivingLicenseImage() {
        return typeOfLicense_from_drivingLicenseImage;
    }

    public void setTypeOfLicense_from_drivingLicenseImage(String typeOfLicense_from_drivingLicenseImage) {
        this.typeOfLicense_from_drivingLicenseImage = typeOfLicense_from_drivingLicenseImage;
    }

    public String getAddress_from_drivingLicenseImage() {
        return address_from_drivingLicenseImage;
    }

    public void setAddress_from_drivingLicenseImage(String address_from_drivingLicenseImage) {
        this.address_from_drivingLicenseImage = address_from_drivingLicenseImage;
    }

    public String getId_from_drivingLicenseImage() {
        return id_from_drivingLicenseImage;
    }

    public void setId_from_drivingLicenseImage(String id_from_drivingLicenseImage) {
        this.id_from_drivingLicenseImage = id_from_drivingLicenseImage;
    }

    public String getExpDate_from_drivingLicenseImage() {
        return expDate_from_drivingLicenseImage;
    }

    public void setExpDate_from_drivingLicenseImage(String expDate_from_drivingLicenseImage) {
        this.expDate_from_drivingLicenseImage = expDate_from_drivingLicenseImage;
    }

    public String getTypeOfLicense_from_carLicenseImage() {
        return typeOfLicense_from_carLicenseImage;
    }

    public void setTypeOfLicense_from_carLicenseImage(String typeOfLicense_from_carLicenseImage) {
        this.typeOfLicense_from_carLicenseImage = typeOfLicense_from_carLicenseImage;
    }

    public String getId_from_carLicenseImage() {
        return id_from_carLicenseImage;
    }

    public void setId_from_carLicenseImage(String id_from_carLicenseImage) {
        this.id_from_carLicenseImage = id_from_carLicenseImage;
    }

    public String getExpDate_from_carLicenseImage() {
        return expDate_from_carLicenseImage;
    }

    public void setExpDate_from_carLicenseImage(String expDate_from_carLicenseImage) {
        this.expDate_from_carLicenseImage = expDate_from_carLicenseImage;
    }

    public String getCarNumber_from_carLicenseImage() {
        return carNumber_from_carLicenseImage;
    }

    public void setCarNumber_from_carLicenseImage(String carNumber_from_carLicenseImage) {
        this.carNumber_from_carLicenseImage = carNumber_from_carLicenseImage;
    }
}
