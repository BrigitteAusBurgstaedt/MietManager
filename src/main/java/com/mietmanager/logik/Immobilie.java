package com.mietmanager.logik;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.logging.Logger;

/**
 * Ein Datenmodell einer Immobilie. Beinhaltet alle wichtigen Informationen, die das Programm über eine Immobilie wissen
 * muss. Eine Immobilie kann mehrere Instanzen vom Typ {@link Wohnung} haben.
 *
 * @since       1.0.0
 * @see         Wohnung
 * @author      John Robin Pfeifer
 */
public class Immobilie implements Visualisierbar {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final StringProperty bezeichnung;
    private final ObservableList<Wohnung> wohnungen;

    public Immobilie(String bezeichnung) {
        this.bezeichnung = new SimpleStringProperty(bezeichnung);
        this.wohnungen = FXCollections.observableArrayList();
    }

    /**
     * Konstruktor für die erst Erstellung des Objektes.
     * @param bezeichnung name der Immobilie
     * @param wohnungsanzahl anzahl der Wohnungen in der Immobilie
     */
    public Immobilie(String bezeichnung, int wohnungsanzahl) {
        this(bezeichnung);
        erstelleWohnungen(wohnungsanzahl);
    }

    public double[] bestimmeMinAvgMax(int monat, int jahr) {
        int anzahlVorhandener = 0;
        double summeDifferenzProFlaeche = 0.0;
        double min = Double.MAX_VALUE;
        double max = 0.0;
        for (Wohnung w : wohnungen) {
            if (w.getFlaeche() <= 0.0) return new double[0];                    // Mindestens eine Wohnung hat noch keine gesetzte Fläche, das Verfahren war nicht erfolgreich
            boolean enthalten = false;
            for (Zaehlerstand z : w.getZaehlerstaende()) {
                if ((z.getDatum().getMonthValue() == monat) && (z.getDatum().getYear() == jahr)) {
                    double akt = z.getDifferenzProFlaeche();
                    min = Math.min(akt, min);
                    max = Math.max(akt, max);

                    summeDifferenzProFlaeche += akt;
                    anzahlVorhandener++;
                    enthalten = true;
                }
            }
            if (!enthalten) return new double[0];                               // Mindestens eine Wohnung hat keinen passenden Zählerstand, das Verfahren war nicht erfolgreich
        }

        return new double[]{min, summeDifferenzProFlaeche / anzahlVorhandener, max};                                    // Es gibt zu jeder Wohnung mindestens einen passenden Wert und min, max, avg konnten bestimmt werden
    }

    /**
     * Erstellt die im Konstruktor angegebene Anzahl an Wohnungen mit standardisierten Werten.
     */
    private void erstelleWohnungen(int wohnungsanzahl) {
        for (int i = 0; i < wohnungsanzahl; i++) {
            new Wohnung(this, "Wohnung " + (i+1), 1, 0);
        }
    }

    public Wohnung hinzufuegen(String bezeichnung, int mieteranzahl, double flaeche) {
        return new Wohnung(this, bezeichnung, mieteranzahl, flaeche);
    }

    // Getter und Setter

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

    public ObservableList<Wohnung> getWohnungen() {
        return wohnungen;
    }

}
