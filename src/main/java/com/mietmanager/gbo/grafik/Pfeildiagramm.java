package com.mietmanager.gbo.grafik;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Region;

public class Pfeildiagramm extends Region {

    private final DoubleProperty min, max, aktuell;
    private static final int HOEHEN_FAKTOR = 10;

    private int monat;

    public Pfeildiagramm(double min, double max, double aktuell) {
        this.min = new SimpleDoubleProperty(min);
        this.max = new SimpleDoubleProperty(max);
        this.aktuell = new SimpleDoubleProperty(aktuell);

        // minimale Einheit
        double e = 20;
        setMinHeight(HOEHEN_FAKTOR * e);
        setMinWidth(30 * e);
    }

    public Pfeildiagramm(double min, double max, double aktuell, int monat) {
        this(min, max, aktuell);

        this.monat = monat;
        erstelleBandtacho();
    }


    private void erstelleBandtacho() {
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

    private void erstellePfeil() {

    }

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
