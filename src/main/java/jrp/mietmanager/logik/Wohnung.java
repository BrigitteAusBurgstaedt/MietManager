package jrp.mietmanager.logik;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jrp.mietmanager.benutzeroberflaeche.hauptfenster.WohnungsReiter;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Wohnung implements Visualisierbar {
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final StringProperty bezeichnung;
    private final IntegerProperty mieteranzahl;
    private final DoubleProperty flaeche;
    private final ObservableList<Zaehlerstand> zaehlerstaende = FXCollections.observableArrayList();

    private boolean istGeoffnet;

    /**
     * Konstruktor der Wohnung.
     *
     * @param bezeichnung Die Bezeichnung der Wohnung.
     * @param mieteranzahl Die Anzahl der Mieter.
     * @param flaeche Die Fläche der Wohnung in Quadratmeter.
     */
    public Wohnung(String bezeichnung, int mieteranzahl, double flaeche) {
        this.bezeichnung = new SimpleStringProperty(bezeichnung);
        this.mieteranzahl = new SimpleIntegerProperty(mieteranzahl);
        this.flaeche = new SimpleDoubleProperty(flaeche);

        this.istGeoffnet = false;

        // Differenzenbildung für die Zählerstände
        zaehlerstaende.addListener((ListChangeListener<Zaehlerstand>) veraenderung -> {

            // Falls mehrere Veränderungen vorgenommen wurden
            while (veraenderung.next()) {

                if (veraenderung.wasUpdated())
                    Collections.sort(zaehlerstaende);

                if (veraenderung.wasUpdated() || veraenderung.wasRemoved() || veraenderung.wasAdded()) {
                    log.info("Der ListChangeListener ist angesprungen");

                    // Differenzenbildung
                    for (int i = veraenderung.getFrom(); veraenderung.getTo() != zaehlerstaende.size() ? i <= veraenderung.getTo() : i < veraenderung.getTo(); i++) {
                        if (i == 0)
                            zaehlerstaende.get(i).setDifferenz(0); // Der erste Zählerstand erhält die Differenz 0
                        else
                            zaehlerstaende.get(i).setDifferenz(
                                    zaehlerstaende.get(i).getWert() - zaehlerstaende.get(i - 1).getWert()
                            );
                    }

                }
            }
        });
    }

    /**
     * Methode zum Hinzufügen eines Zählerstandes.
     *
     * @param zaehlerstand Zählerstand welcher hinzugefügt werden soll.
     */
    public void hinzufuegen(Zaehlerstand zaehlerstand) {
        int index = Collections.binarySearch(zaehlerstaende, zaehlerstand);
        if (index < 0) index = ~index;

        this.zaehlerstaende.add(index, zaehlerstand);
    }

    @Override
    public WohnungsReiter oeffneReiter() {
        WohnungsReiter wr = new WohnungsReiter(this);
        wr.setOnClosed(event -> istGeoffnet = false);
        istGeoffnet = true;
        return wr;
    }

    @Override
    public boolean istGeoeffnet() {
        return istGeoffnet;
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
}
