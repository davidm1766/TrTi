package sk.listok.zssk.zssklistok.objects;

/**
 * Created by Nexi on 14.03.2017.
 */

public class PersonInfo {

    private String email;
    private String name;
    private String surname;
    private String RegNumber;


    public PersonInfo(String email, String name,String surname, String RegNumber){
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.RegNumber = RegNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRegNumber() {
        return RegNumber;
    }

    public String getSurname() {
        return surname;
    }

}
