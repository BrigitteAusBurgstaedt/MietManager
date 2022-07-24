package com.mietmanager.gbo.grafik;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Pane;

class Bandtacho extends Pane {
    private final int monat;

    public Bandtacho(int monat) {
        this.monat = monat;

        erstelleBandtacho();
    }

    private void erstelleBandtacho() {
        Label[] eKlassenSymbole = new Label[9];
        Separator[] separators = new Separator[8];
        StringBuilder stil = new StringBuilder("-fx-background-color: linear-gradient(to right, ");

        stil.append(EnergieKlasse.values()[9].farbeMitProzent(monat)).append(", ");

        for (int i = 0; i < eKlassenSymbole.length; i++) {
            // Hilfsvariable
            double prozentVorhergehender = i == 0 ? 0.0 : EnergieKlasse.values()[i-1].prozent(monat);

            eKlassenSymbole[i] = new Label();
            eKlassenSymbole[i].setText(EnergieKlasse.values()[i].toString());
            eKlassenSymbole[i].layoutXProperty().bind(widthProperty().multiply(
                    ((EnergieKlasse.values()[i].prozent(monat) - prozentVorhergehender) / 2) + prozentVorhergehender
            ).subtract(eKlassenSymbole[i].widthProperty().divide(2)));
            eKlassenSymbole[i].layoutYProperty().bind(heightProperty().divide(2).subtract(eKlassenSymbole[i].heightProperty().divide(2)));

            stil.append(EnergieKlasse.values()[i].farbeMitProzent(monat));

            if (i != 8) {
                separators[i] = new Separator(Orientation.VERTICAL);
                separators[i].minHeightProperty().bind(heightProperty());
                separators[i].layoutXProperty().bind(widthProperty().multiply(EnergieKlasse.values()[i].prozent(monat)));

                stil.append(", ");
            }
        }

        stil.append(");");
        setStyle(stil.toString());

        getChildren().addAll(separators);
        getChildren().addAll(eKlassenSymbole);
    }


    public int getMonat() {
        return monat;
    }
}
