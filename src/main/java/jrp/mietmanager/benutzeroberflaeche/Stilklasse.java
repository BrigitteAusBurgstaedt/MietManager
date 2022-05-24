package jrp.mietmanager.benutzeroberflaeche;

public enum Stilklasse {
    PLATZHALTER("platzhalter"),
    BEHAELTER("behaelter"),
    TITEL_LABEL("titel-label"),
    HAUPTBEHAELTER("hauptbehaelter");

    private final String stilklasseCSS;

    Stilklasse(String stilklasseCSS) {
        this.stilklasseCSS = stilklasseCSS;
    }

    @Override
    public String toString() {
        return stilklasseCSS;
    }
}
