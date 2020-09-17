package Model;

public class User {

    private int userID;
    private String username;
    private String password;

    //Constructor
    public User(){}

    //Getters and Setters
    public int getUserID(){
        return this.userID;
    }

    public void setUserID(int userID){
        this.userID=userID;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password=password;
    }


}
