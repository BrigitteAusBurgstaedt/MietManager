package com.mietmanager.logik;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * Ein Datenmodell einer Wohneinheit. Beinhaltet alle wichtigen Informationen, die das Programm über eine Wohnung wissen
 * muss. Eine Wohnung befindet sich in genau einer {@link Immobilie} und kann mehrere Instanzen vom Typ
 * {@link Zaehlerstand} haben.
 *
 * @since       1.0.0
 * @see         Immobilie
 * @see         Zaehlerstand
 * @author      John Robin Pfeifer
 */
public class Wohnung implements Visualisierbar {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final Immobilie immobilie;

    private final StringProperty bezeichnung;
    private final IntegerProperty mieteranzahl;
    private final DoubleProperty flaeche;
    private final ObservableList<Zaehlerstand> zaehlerstaende = FXCollections.observableArrayList();

    /**
     * Konstruktor der Wohnung.
     *
     * @param bezeichnung   die Bezeichnung der Wohnung.
     * @param mieteranzahl  die Anzahl der Mieter.
     * @param flaeche       die Fläche der Wohnung in Quadratmeter.
     */
    protected Wohnung(Immobilie immobilie, String bezeichnung, int mieteranzahl, double flaeche) {
        this.immobilie = immobilie;                 // Festlegen der dazugehörigen Immobilie
        this.immobilie.getWohnungen().add(this);    // Eintragen in die Wohnungsliste der Immobilie

        this.bezeichnung = new SimpleStringProperty(bezeichnung);
        this.mieteranzahl = new SimpleIntegerProperty(mieteranzahl);
        this.flaeche = new SimpleDoubleProperty(flaeche);
    }

    public Zaehlerstand hinzufuegen(LocalDate datum, double wert, Zaehlermodus zaehlermodus) throws InstantiationException {
        return new Zaehlerstand(this, datum, wert, zaehlermodus);
    }

    public double getZaehlerstandWertProFlaeche(int monat, int jahr) {
        double summe = 0.0;
        int anzahl = 0;
        for (Zaehlerstand z : zaehlerstaende) {
            if (z.getDatum().getMonthValue() == monat && z.getDatum().getYear() == jahr) {
                summe += z.getDifferenzProFlaeche();
                anzahl++;
            }
        }
        return summe / anzahl;
    }

    // Getter und Setter

    public Immobilie getImmobilie() {
        return immobilie;
    }

    @Override
    public String toString() {
        return bezeichnung.get();
    }

    public StringProperty bezeichnungProperty() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung.set(bezeichnung);
    }

    public int getMieteranzahl() {
        return mieteranzahl.get();
    }

    public IntegerProperty mieteranzahlProperty() {
        return mieteranzahl;
    }

    public void setMieteranzahl(int mieteranzahl) {
        this.mieteranzahl.set(mieteranzahl);
    }

    public double getFlaeche() {
        return flaeche.get();
    }

    public DoubleProperty flaecheProperty() {
        return flaeche;
    }

    public void setFlaeche(double flaeche) {
        this.flaeche.set(flaeche);
    }

    public ObservableList<Zaehlerstand> getZaehlerstaende() {
        return zaehlerstaende;
    }

    public Zaehlerstand getZaehlerstand(int monat, int jahr) {

        for (int i = zaehlerstaende.size() - 1; i >= 0; i--) {
            Zaehlerstand aktuell = zaehlerstaende.get(i);
            if(aktuell.getDatum().getYear() == jahr && aktuell.getDatum().getMonthValue() == monat)
                return aktuell;
        }

        return null;
    }
}
