package sk.listok.zssk.zssklistok.datalayer.objects;

/**
 *  Informácie o osobných údajoch.
 */
public class Person {

    /**
     * Identifikátor v databáze.
     */
    private int id;
    /**
     * Email cestujúceho.
     */
    private String email;
    /**
     * Prvé meno cestujúceho.
     */
    private String name;
    /**
     * Priezvisko cestujúceho
     */
    private String surname;
    /**
     * Číslo na kartičke na bezplatnú prepravu.
     */
    private String regNumber;
    /**
     * Číslo občianskeho preukazu.
     */
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
