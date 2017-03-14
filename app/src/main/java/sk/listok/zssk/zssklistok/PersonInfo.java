package sk.listok.zssk.zssklistok;

/**
 * Created by Nexi on 14.03.2017.
 */

public class PersonInfo {

    private String email;
    private String name;
    private String surname;
    private String IDcard;
    private String RegNumber;


    public PersonInfo(String email, String name,String surname, String IDcard, String RegNumber){
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.IDcard = IDcard;
        this.RegNumber = RegNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getIDcard() {
        return IDcard;
    }

    public String getRegNumber() {
        return RegNumber;
    }

    public String getSurname() {
        return surname;
    }

}
