package jrp.mietmanager.logik;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import jrp.mietmanager.benutzeroberflaeche.hauptfenster.PdfAnsicht;
import jrp.mietmanager.benutzeroberflaeche.hauptfenster.WohnungsReiter;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Wohnung implements Visualisierbar {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final StringProperty bezeichnung;
    private final IntegerProperty mieteranzahl;
    private final DoubleProperty flaeche;
    private final ObservableList<Zaehlerstand> zaehlerstaende = FXCollections.observableArrayList();

    private PdfAnsicht pdfAnsicht;
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
            log.info("Im List Change Listener für Differenzen");
            int anzahlVeraenderungen = 1; // Für Log


            while (veraenderung.next()) { // Falls mehrere Veränderungen vorgenommen wurden
                log.log(Level.INFO,"Veränderung Nr. {0}", anzahlVeraenderungen);

                if (veraenderung.wasPermutated()) {
                    log.severe("Die Liste wurde durcheinander gebracht");
                    continue;
                }

                if (veraenderung.wasAdded()) {
                    log.info("Es wurde etwas hinzugefügt oder ersetzt");
                    bildeDifferenzen(veraenderung.getFrom(), veraenderung.getTo());

                } else if (veraenderung.wasRemoved()) {
                    log.info("Es wurde etwas Gelöscht");
                    if (veraenderung.getRemovedSize() == 1) { // Es wurde nur ein Element gelöscht
                        bildeDifferenzen(veraenderung.getFrom(), veraenderung.getFrom() + 1);
                    } else {
                        bildeDifferenzen(0, zaehlerstaende.size()); // Wenn mehr als ein Element gelöscht wurde, müssen alle Differenzen neu gebildet werden
                    }
                }

                if (veraenderung.wasUpdated()) log.warning("Es wurde etwas aktualisiert"); // Sollte nicht vorkommen

                anzahlVeraenderungen++;
            }
        });
    }

    private void bildeDifferenzen(int von, int bis) {
        if (zaehlerstaende.isEmpty()) // Wenn Liste leer ist, dann müssen auch keine Differenzen gebildet werden
            return;

        int anzahlDifferenzenbildung = 0; // Für Log

        if (bis != zaehlerstaende.size()) // Wenn innerhalb der Liste etwas verändert wurde muss auch von bis+1 eine Differenz neu gebildet werden
            bis++;

        for (int i = von; i < bis; i++) {
            anzahlDifferenzenbildung++;

            if (i == 0) {
                zaehlerstaende.get(i).setDifferenz(0); // Der erste Zählerstand erhält die Differenz 0
                continue;
            }

            zaehlerstaende.get(i).setDifferenz(zaehlerstaende.get(i).getWert() - zaehlerstaende.get(i - 1).getWert()); // Differenz = Neuer Wert - Alter Wert
        }

        log.log(Level.INFO, "Es wurden {0} Differenzen gebildet", anzahlDifferenzenbildung);
    }

    /**
     * Methode zum Hinzufügen eines Zählerstandes.
     *
     * @throws IllegalStateException Wenn der Wert des Zählerstandes geringer ist als der vorhergehende
     * @param zaehlerstand Zählerstand welcher hinzugefügt werden soll
     */
    public void hinzufuegen(Zaehlerstand zaehlerstand) throws IllegalStateException {
        int index = Collections.binarySearch(zaehlerstaende, zaehlerstand);
        if (index < 0) index = ~index;

        if (index > 0 && zaehlerstaende.get(index - 1).getWert() > zaehlerstand.getWert())
            throw new IllegalStateException("Der Wert des Zählerstandes darf nicht geringer sein als der vorhergehende!");
        else
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
    public VBox oeffnePdfAnsicht(Immobilie immobilie) {
        if (pdfAnsicht == null)
            pdfAnsicht = new PdfAnsicht(immobilie, this);
        return pdfAnsicht;
    }

    @Override
    public boolean istGeoeffnet() {
        return istGeoffnet;
    }

    /**
     * Wahrheitswert welcher widerspiegelt, ob ein PDF gezeigt werden soll.
     *
     * @return Gibt wahr zurück, wenn eine PDF-Ansicht benötigt wird.
     */
    @Override
    public boolean brauchtPdfAnsicht() {
        return true;
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
