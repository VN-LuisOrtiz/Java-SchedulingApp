package Model;

public class Customer {

    private int customerID;
    private String customerName;
    private int addressID;
    private String address;
    private String address2;
    private String postalCode;
    private String phone;
    private int cityID;
    private String city;
    private int countryID;
    private String country;


    //Constructor
    public Customer(){
    }
    //Overloaded Constructor
    public Customer(int customerID, String customerName, int addressID, String address, String address2, String postalCode, String phone, int cityID, String city, int countryID, String country){
        setCustomerID(customerID);
        setCustomerName(customerName);
        setAddressID(addressID);
        setAddress(address);
        setAddress2(address2);
        setPostalCode(postalCode);
        setPhone(phone);
        setCityID(cityID);
        setCity(city);
        setCountryID(countryID);
        setCountry(country);
    }


    //Getters and Setters
    public int getCustomerID(){
        return this.customerID;
    }

    public void setCustomerID(int customerID){
        this.customerID=customerID;
    }

    public String getCustomerName(){
        return this.customerName;
    }

    public void setCustomerName(String customerName){
        this.customerName=customerName;
    }

    public int getAddressID(){
        return this.addressID;
    }

    public void setAddressID(int addressID){
        this.addressID=addressID;
    }


    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this.address=address;
    }

    public String getAddress2(){
        return this.address2;
    }

    public void setAddress2(String address2){
        this.address2=address2;
    }

    public String getPostalCode(){
        return this.postalCode;
    }

    public void setPostalCode(String postalCode){
        this.postalCode=postalCode;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setPhone(String phone){
        this.phone=phone;
    }

    public int getCityID(){
        return this.cityID;
    }

    public void setCityID(int cityID){
        this.cityID=cityID;
    }

    public String getCity(){
        return this.city;
    }

    public void setCity(String city){
        this.city=city;
    }

    public int getCountryID(){
        return this.countryID;
    }

    public void setCountryID(int countryID){
        this.countryID=countryID;
    }

    public String getCountry(){
        return this.country;
    }

    public void setCountry(String country){
        this.country=country;
    }
}
