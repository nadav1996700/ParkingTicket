package src.Model;

public class Residential {
    private String city = "";
    private String street = "";
    private String houseNumber;
    private String apartmentNumber;
    private String postalCode;
    private ResidentialState residentialState = ResidentialState.PERMANENTTENANT;

    public Residential() {
        // empty constructor
    }

    public Residential(String city, String street, String houseNumber, String apartmentNumber, String postalCode, ResidentialState residentialState) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
        this.postalCode = postalCode;
        this.residentialState = residentialState;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public ResidentialState getResidentialState() {
        return residentialState;
    }

    public void setResidentialState(ResidentialState residentialState) {
        this.residentialState = residentialState;
    }
}
