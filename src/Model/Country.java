package Model;

public class Country {
    private int countryID;
    private String country;

    //Default Constructor
    public Country(){}

    //Getters and Setters
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
