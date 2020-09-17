package Model;


public class Appointment {
    private int appointmentID;
    private int customerID;
    private String customerName;
    private int userID;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private String start;
    private String end;

    //Constructor
    public Appointment(){

    }

    //Overloaded constructor
    public Appointment(int appointmentID, int customerID, String customerName, int userID, String title, String description, String location, String contact, String type, String url, String startDateTime, String endDateTime){
        setAppointmentID(appointmentID);
        setCustomerID(customerID);
        setCustomerName(customerName);
        setUserID(userID);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setContact(contact);
        setType(type);
        setUrl(url);
        setStart(startDateTime);
        setEnd(endDateTime);
    }

    //Getters and Setters
    public int getAppointmentID(){
        return this.appointmentID;
    }

    public void setAppointmentID(int appointmentID){
        this.appointmentID=appointmentID;
    }

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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID){
        this.userID=userID;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location){
        this.location=location;
    }

    public String getContact(){
        return this.contact;
    }

    public void setContact(String contact){
        this.contact=contact;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type=type;
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url=url;
    }

    public String getStart(){
        return this.start;
    }

    public void setStart(String start){
        this.start=start;
    }

    public String getEnd(){
        return this.end;
    }

    public void setEnd(String end){
        this.end=end;
    }

}
