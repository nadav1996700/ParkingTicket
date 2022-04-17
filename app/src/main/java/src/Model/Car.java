package src.Model;

public class Car {
    private CarDetails carDetails;
    private PersonalDetails personalDetails;
    private Residential residential;
    private Pticket pticket;

    public Car() {
        //empty public constructor
    }

    public Car(CarDetails carDetails, PersonalDetails personalDetails, Residential residential, Pticket pticket) {
        this.carDetails = carDetails;
        this.personalDetails = personalDetails;
        this.residential = residential;
        this.pticket = pticket;
    }

    public CarDetails getCarDetails() {
        return carDetails;
    }

    public void setCarDetails(CarDetails carDetails) {
        this.carDetails = carDetails;
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
