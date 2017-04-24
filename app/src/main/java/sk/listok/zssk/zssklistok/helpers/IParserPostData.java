package sk.listok.zssk.zssklistok.helpers;

/**
 * Tento interface sluzi zobrazenie dostupných  funkcii, ktore obsahuje parser.
 */
public interface IParserPostData {

    /**
     * Vytvorí reťazec s POST dátami pre nájdenie vlaku.
     */
    String postFindTrains(String sTownFrom, String sTownTo, String sTime, String sDate);

    /**
     * Vytvorí reťazec s POST dátami pre vybratie vlaku.
     */
    String postSelectTrain(String html, String idJourney);

    /**
     * Vytvorí reťazec s POST dátami pre vybratý typ lístka.
     */
    String postTicketType(String html, int selectedItemIndex);

    /**
     * Vytvorí reťazec s POST dátami pre informácie o zákazníkovi.
     */
    String postPassengerInfo(String html, String mail, String name, String surname, String regNumber);

    /**
     * Vytvorí reťazec s POST dátami pre dokončenie platby.
     */
    String postFinishPayment(String html);

    /**
     * Vytvorí reťazec s POST dátami pre stiahnutie lístka
     */
    String postDownloadTicket(String html);

    /**
     * Vráti prazdny reťazec v prípade že nie je žiadna chyba a
     * môže sa pokračovať. Inak vráti reťazec obsahujúci chybu,
     * že už nie je možné kúpiť ďalší lístok.
     */
    String checkNoMoreTickets(String html);

    /**
     * Vyberie z html cenu lístka.
     */
    String ticketPrice(String html);

    /**
     * Vyberie z html detail lístka - odkiaľ
     * kam ide vlak, prestupy...
     */
    String ticketDetails(String html);

    /**
     * Skontroluje či používateľ zadal správne osobné údaje.
     * Ak sa vyskytne chyba tak sa vráti v stringu. Ak nie je
     * chyba tak sa vráti prázdny reťazec.
     */
    String checkUserInfo(String html);


}
