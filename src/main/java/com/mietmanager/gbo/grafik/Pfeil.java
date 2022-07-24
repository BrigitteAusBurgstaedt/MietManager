package com.mietmanager.gbo.grafik;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

class Pfeil extends Pane {
    private static final int MIN_BREITE = 100;
    private static final int MIN_HOEHE = 10;
    private static final double SPITZEN_FAKTOR = 0.2;

    private final boolean mitBandtacho;
    private final DoubleProperty optischeBreite = new SimpleDoubleProperty();    // Breite inklusive Spitze
    private final DoubleProperty optischesMin = new SimpleDoubleProperty();      // Wert, welcher mit der Spitze assoziiert werden kann
    private DoubleProperty min, max, aktuell;

    private Bandtacho bandtacho;
    private Polygon pfeilform;

    public Pfeil(double min, double max , double aktuell) {
        mitBandtacho = false;

        this.min = new SimpleDoubleProperty(min);
        this.max = new SimpleDoubleProperty(max);
        this.aktuell = new SimpleDoubleProperty(aktuell);

        erstellePfeil();
    }

    public Pfeil(double min, double max, double aktuell, Bandtacho bandtacho) {
        mitBandtacho = true;

        this.min = new SimpleDoubleProperty(min);
        this.max = new SimpleDoubleProperty(max);
        this.aktuell = new SimpleDoubleProperty(aktuell);
        this.bandtacho = bandtacho;

        erstellePfeil();
    }

    private void erstellePfeil() {
        setMinWidth(MIN_BREITE);
        setMinHeight(MIN_HOEHE);
        setWidth(MIN_BREITE);
        setHeight(MIN_HOEHE);

        pfeilform = new Polygon();

        // Automatische Anpassung der Pfeilgröße
        generierePunkte();
        widthProperty().addListener((observableValue, alteBreite, neueBreite) ->
            generierePunkte()
        );
        heightProperty().addListener((observableValue, alteHoehe, neueHoehe) ->
            generierePunkte()
        );

        // Setzen der optischen Breite
        optischeBreite.bind(widthProperty().add(widthProperty().multiply(SPITZEN_FAKTOR)));

        getChildren().add(pfeilform);

        beschriften();
        stylen();


    }

    private void beschriften() {
        Label pfeilbeschriftung = new Label("IHR SPARPOTENZIAL");
        Label nachbarn = new Label("Ihre sparsamsten Nachbarn: " + min.get() + " kWh/m²"); // TODO: 12.05.2022 Zahlenformatierung und Fallabfrage aktuell == min
        Label selbst = new Label("Ihre Wohnung: " + aktuell.get() + " kWh/m²");
        Label haus = new Label("<-- IHR HAUS -->"); // TODO: 12.05.2022 Pfeile als Linien 

        Separator minSep = new Separator(Orientation.VERTICAL);
        Separator maxSep = new Separator(Orientation.VERTICAL);
        Separator aktuellSep = new Separator(Orientation.VERTICAL);

        pfeilbeschriftung.layoutYProperty().bind(heightProperty()
                .divide(2)
                .subtract(pfeilbeschriftung.heightProperty().divide(2))); // Ausrichtung mittig im Pfeil
        pfeilbeschriftung.setLayoutX(10);
        pfeilbeschriftung.setStyle("-fx-font-weight: bold;");

        minSep.prefHeightProperty().bind(heightProperty().multiply(3));
        minSep.layoutYProperty().bind(heightProperty().negate());

        maxSep.prefHeightProperty().bind(heightProperty().multiply(2));
        maxSep.layoutXProperty().bind(widthProperty());

        aktuellSep.prefHeightProperty().bind(heightProperty().multiply(2));
        aktuellSep.layoutXProperty().bind(widthProperty()
                .multiply(aktuell.subtract(min).divide(max.subtract(min))));
        aktuellSep.layoutYProperty().bind(heightProperty().negate());

        haus.layoutXProperty().bind(widthProperty().divide(2).subtract(haus.widthProperty().divide(2)));
        haus.layoutYProperty().bind(heightProperty().multiply(1.5).subtract(haus.heightProperty().divide(2)));

        // Rotation für die Labels
        Rotate r = new Rotate(-30, 0, 15);

        nachbarn.layoutYProperty().bind(heightProperty().add(nachbarn.heightProperty()).negate());
        nachbarn.getTransforms().add(r);

        selbst.layoutXProperty().bind(aktuellSep.layoutXProperty());
        selbst.layoutYProperty().bind(nachbarn.layoutYProperty());
        selbst.getTransforms().add(r);

        getChildren().addAll(minSep, maxSep, aktuellSep, pfeilbeschriftung, haus, nachbarn, selbst);
    }

    private void stylen() {
        if (mitBandtacho) {
            optischesMin.bind(min.subtract(max.subtract(min).multiply(SPITZEN_FAKTOR)));

            StringBuilder stilPfeilform = new StringBuilder("-fx-fill: linear-gradient(to right, ");

            // Stil String setzen
            stilPfeilform.append(EnergieKlasse.OA1.farbeMitProzent(optischesMin.doubleValue(), max.doubleValue(), bandtacho.getMonat()))
                    .append(", ");

            for (EnergieKlasse e : EnergieKlasse.values()) {
                if (!e.equals(EnergieKlasse.OA1)) {
                    stilPfeilform.append(e.farbeMitProzent(optischesMin.doubleValue(), max.doubleValue(), bandtacho.getMonat()));

                    if (e.equals(EnergieKlasse.H100))
                        stilPfeilform.append(");");
                    else
                        stilPfeilform.append(", ");
                }
            }
            pfeilform.setStyle(stilPfeilform.toString());

        } else {
            pfeilform.setStyle("-fx-fill: linear-gradient(to right, #0047ba, #ff6b00);");
        }
    }

    private void generierePunkte() {
        pfeilform.getPoints().clear();
        pfeilform.getPoints().addAll(
                0.0, 0.0,
                -(getWidth() * SPITZEN_FAKTOR), getHeight() / 2,
                0.0, getHeight(),
                getWidth(), getHeight(),
                getWidth(), 0.0
        );
    }
}
