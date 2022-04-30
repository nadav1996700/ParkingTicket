package src.Model;

public class Car {
    private String carId;
    private PersonalDetails personalDetails;
    private Residential residential;
    private Pticket pticket;

    public Car() {
        //empty public constructor
    }

    public Car(String carId, PersonalDetails personalDetails, Residential residential, Pticket pticket) {
        this.carId = carId;
        this.personalDetails = personalDetails;
        this.residential = residential;
        this.pticket = pticket;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public Residential getResidential() {
        return residential;
    }

    public void setResidential(Residential residential) {
        this.residential = residential;
    }

    public Pticket getPticket() {
        return pticket;
    }

    public void setPticket(Pticket pticket) {
        this.pticket = pticket;
    }
}
