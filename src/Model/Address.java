package Model;

public class Address {
    private int addressID;
    private String address;
    private String address2;
    private int cityID;
    private String postalCode;
    private String phone;

    //Constructor
    public Address(){}

    //Overloaded constructor
    public Address(int addressID, String address, String address2, int cityID, String postalCode, String phone){
        setAddressID(addressID);
        setAddress(address);
        setAddress2(address2);
        setCityID(cityID);
        setPostalCode(postalCode);
        setPhone(phone);
    }

    //Getters and Setters
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

    public int getCityID(){
        return this.cityID;
    }

    public void setCityID(int cityID){
        this.cityID=cityID;
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



}
