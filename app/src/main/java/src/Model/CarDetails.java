package src.Model;

public class CarDetails {
    private String carId;
    private String color;
    private String manufacturer;
    private Ownership ownership;

    public CarDetails() {
        //empty public constructor
    }

    public CarDetails(String carId, String color, String manufacturer, Ownership ownership) {
        this.carId = carId;
        this.color = color;
        this.manufacturer = manufacturer;
        this.ownership = ownership;
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
}
