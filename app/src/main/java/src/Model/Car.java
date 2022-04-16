package src.Model;

public class Car {
    private String carId;
    private String color;
    private String manufacturer;
    private Ownership ownership;
    private PersonalDetails personalDetails;
    private Residential residential;
    private Pticket pticket;

    public Car() {
        //empty public constructor
    }

    public Car(String carId, String color, String manufacturer, Ownership ownership, PersonalDetails personalDetails, Residential residential, Pticket pticket) {
        this.carId = carId;
        this.color = color;
        this.manufacturer = manufacturer;
        this.ownership = ownership;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
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
