package jrp.mietmanager.berechnung;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public class Pfeildiagramm extends Region {

    private final DoubleProperty min, max, aktuell;
    private final int e = 20; // minimale Einheit

    public Pfeildiagramm() {
        min = new SimpleDoubleProperty(0.0);
        max = new SimpleDoubleProperty(1.0);
        aktuell = new SimpleDoubleProperty(0.5);

        erstellePfeil();
    }

    public Pfeildiagramm(int monat) {
        this();

        erstelleBandtacho(monat);
    }

    private void erstelleBandtacho(int monat) {

        //
        // Container
        //
        Pane bandtacho = new Pane();

        bandtacho.setMinHeight(e);
        bandtacho.setMinWidth(30 * e); // TODO: 09.05.2022
        bandtacho.setStyle("-fx-background-color: red;");


        //
        // Grafische Elemente
        //
        Label[] eKlassenSymbole = new Label[9];
        Separator[] separators = new Separator[8];

        for (int i = 0; i < eKlassenSymbole.length; i++) {
            eKlassenSymbole[i] = new Label();
        }

        eKlassenSymbole[0].setText("A+");
        eKlassenSymbole[1].setText("A");
        eKlassenSymbole[2].setText("B");
        eKlassenSymbole[3].setText("C");
        eKlassenSymbole[4].setText("D");
        eKlassenSymbole[5].setText("E");
        eKlassenSymbole[6].setText("F");
        eKlassenSymbole[7].setText("G");
        eKlassenSymbole[8].setText("H");

        for (int i = 0; i < separators.length; i++) {
            separators[i] = new Separator();
        }

        //
        // Elemente zu Container
        //
        bandtacho.getChildren().addAll(separators);
        bandtacho.getChildren().addAll(eKlassenSymbole);
        getChildren().add(bandtacho);
    }

    private void erstellePfeil() {

        //
        // Container
        //
        Pane pfeil = new Pane();
        Pane bandtacho = new Pane();

        pfeil.setMinHeight(e);
        pfeil.setMinWidth(10 * e); // TODO: 09.05.2022

        //
        // Grafische Elemente
        //
        Rectangle spitze = new Rectangle();
        Rectangle schaft = new Rectangle();
        Label pfeilbeschriftung = new Label("IHR SPARPOTENZIAL");

        // Hilfsvariablen
        DoubleBinding b = pfeil.heightProperty().multiply(Math.sqrt(2)).divide(2);  // b für Breite der Spitze
        DoubleBinding z = pfeil.heightProperty().divide(2);                         // z für Zentrum Spitze bis Ecke Spitze
        DoubleProperty farbstoppSpitze = new SimpleDoubleProperty();
        DoubleProperty farbstoppSchaft = new SimpleDoubleProperty();

        spitze.heightProperty().bind(b);
        spitze.widthProperty().bind(b);
        spitze.setRotate(45);
        spitze.layoutXProperty().bind(pfeil.heightProperty().subtract(b).divide(2));
        spitze.layoutYProperty().bind(pfeil.heightProperty().subtract(b).divide(2));

        schaft.heightProperty().bind(pfeil.heightProperty());
        schaft.widthProperty().bind(pfeil.widthProperty().subtract(z));
        schaft.layoutXProperty().bind(z);

        pfeilbeschriftung.layoutXProperty().bind(z);
        pfeilbeschriftung.layoutYProperty().bind(z.subtract(pfeilbeschriftung.heightProperty().divide(2)));
        pfeilbeschriftung.setStyle("-fx-font-weight: bold;");

        farbstoppSpitze.bind(schaft.widthProperty().add(z).divide(z).multiply(100));
        farbstoppSchaft.bind(schaft.widthProperty().add(z).divide(schaft.widthProperty()).multiply(100));
        farbstoppSchaft.addListener((observableValue, alteZahl, neueZahl) -> {
            spitze.setStyle("-fx-fill: linear-gradient(to top right, green " + farbstoppSpitze.get() + "%, red);");
            schaft.setStyle("-fx-fill: linear-gradient(to right, green, red " + farbstoppSchaft.get() + "%);");
        });

        //
        // Elemente zu Container
        //
        pfeil.getChildren().addAll(spitze, schaft, pfeilbeschriftung);
        getChildren().add(pfeil);
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
