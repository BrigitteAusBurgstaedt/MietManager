package com.mietmanager.gbo.grafik;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Region;

/**
 * Das Pfeildiagramm stellt die Spanne der Energieverbräuche einer ganzen Wohnung dar.
 *
 * @see Bandtacho
 * @see Pfeil
 * @author John Robin Pfeifer
 * @since 1.0.0
 */
public class Pfeildiagramm extends Region {
    private static final int HOEHEN_FAKTOR = 10;
    private final DoubleProperty min;
    private final DoubleProperty max;
    private final DoubleProperty aktuell;
    private final int monat;

    /**
     * Erstellt das Fertige Pfeildiagramm aus Pfeil und Bandtacho.
     *
     * @param min der Minimale Verbrauch
     * @param max der Maximale Verbrauch
     * @param aktuell der Aktuelle Verbrauch
     * @param monat der Monat, für dem das Diagramm erzeugt werden soll
     */
    public Pfeildiagramm(double min, double max, double aktuell, int monat) {
        this.min = new SimpleDoubleProperty(min);
        this.max = new SimpleDoubleProperty(max);
        this.aktuell = new SimpleDoubleProperty(aktuell);

        // minimale Einheit
        double e = 20;
        setMinHeight(HOEHEN_FAKTOR * e);
        setMinWidth(30 * e);

        this.monat = monat;
        erstellePfeildiagrammMitBandtacho();
    }

    /**
     * Erstellt das Fertige Pfeildiagramm aus Pfeil und Bandtacho.
     */
    private void erstellePfeildiagrammMitBandtacho() {
        Bandtacho bandtacho = new Bandtacho(monat);
        Pfeil pfeil = new Pfeil(min.get(), max.get(), aktuell.get(), bandtacho);

        bandtacho.minHeightProperty().bind(this.heightProperty().divide(HOEHEN_FAKTOR));  // Quatsch mit Soße
        bandtacho.minWidthProperty().bind(this.widthProperty());
        bandtacho.layoutYProperty().bind(this.heightProperty().subtract(bandtacho.heightProperty()));

        pfeil.prefHeightProperty().bind(this.heightProperty().divide(HOEHEN_FAKTOR)); // Selbe
        pfeil.prefWidthProperty().bind(this.widthProperty()
                .multiply(EnergieKlasse.prozent(max, monat))
                .subtract(this.widthProperty().multiply(EnergieKlasse.prozent(min, monat))));
        pfeil.layoutXProperty().bind(this.widthProperty()
                .multiply(EnergieKlasse.prozent(min, monat)));
        pfeil.layoutYProperty().bind(this.heightProperty().subtract(bandtacho.heightProperty().multiply(3)));

        getChildren().addAll(bandtacho, pfeil);
    }

    // TODO: 10.08.2022 Nur Pfeil erstellen

    // Getter und Setter
    public double getMin() {
        return min.get();
    }

    public DoubleProperty minProperty() {
        return min;
    }

    public void setMin(double min) {
        this.min.set(min);
    }

    public double getMax() {
        return max.get();
    }

    public DoubleProperty maxProperty() {
        return max;
    }

    public void setMax(double max) {
        this.max.set(max);
    }

    public double getAktuell() {
        return aktuell.get();
    }

    public DoubleProperty aktuellProperty() {
        return aktuell;
    }

    public void setAktuell(double aktuell) {
        this.aktuell.set(aktuell);
    }




}
