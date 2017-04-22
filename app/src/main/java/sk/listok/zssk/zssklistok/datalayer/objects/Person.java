package sk.listok.zssk.zssklistok.datalayer.objects;

/**
 * Created by Nexi on 11.04.2017.
 */

public class Person {

    private int id;
    private String email;
    private String name;
    private String surname;
    private String regNumber;
    private String idCard;

    // ID, name, surname, email, regnumber, idcard
    public Person(int id, String name, String surname, String email, String regNumber, String idCard){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.regNumber = regNumber;
        this.idCard = idCard;
    }

    public Person(String name, String surname, String email, String regNumber, String idCard){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.regNumber = regNumber;
        this.idCard = idCard;
    }

    public Person(String name, String surname, String email, String regNumber){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.regNumber = regNumber;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString() {
        return surname + " " + name + " (" +email+ ")";
    }
}
