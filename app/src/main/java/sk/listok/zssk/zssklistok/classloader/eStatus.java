package sk.listok.zssk.zssklistok.classloader;

/**
 * Status o úspešnosti sťahovania .dex
 */
public enum eStatus {
    /**
     * Podarilo sa stiahnúť bajtkód z API.
     */
    OK,

    /**
     * Nepodarilo sa stiahnúť bajtkód z API.
     */
    FAILED
}
