package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import jrp.mietmanager.benutzeroberflaeche.Stilklasse;
import jrp.mietmanager.benutzeroberflaeche.grafik.Pfeildiagramm;
import jrp.mietmanager.benutzeroberflaeche.grafik.ZaehlerstandDiagramm;
import jrp.mietmanager.logik.Wohnung;

import java.util.logging.Logger;

public class PdfAnsicht extends VBox {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Wohnung wohnung;

    public PdfAnsicht(Wohnung wohnung) {
        this.wohnung = wohnung;

        VBox platzhalter = new VBox();
        VBox fuerTitel = new VBox();
        Label titel = new Label();

        platzhalter.getStyleClass().add(Stilklasse.PLATZHALTER);
        fuerTitel.getStyleClass().add(Stilklasse.BEHAELTER);
        titel.getStyleClass().add(Stilklasse.TITEL_LABEL);

        fuerTitel.setAlignment(Pos.CENTER);
        titel.textProperty().bind(wohnung.bezeichnungProperty());

        fuerTitel.getChildren().add(titel);
        platzhalter.getChildren().add(fuerTitel);
        getChildren().add(platzhalter);

        getStyleClass().add(Stilklasse.HAUPTBEHAELTER);
        erstelleDiagramm();
    }

    private void erstelleDiagramm() {
        ZaehlerstandDiagramm zaehlerstandDiagramm = new ZaehlerstandDiagramm(wohnung);
        TextField monat = new TextField("Monat");
        TextField jahr = new TextField("Jahr");
        Button erzeugeDiagramm = new Button("Bandtacho erstellen");

        erzeugeDiagramm.setOnAction(event -> {
            int m = Integer.parseInt(monat.getText());
            int j = Integer.parseInt(jahr.getText());
            double[] minAvgMax = wohnung.getImmobilie().bestimmeMinAvgMax(m, j);
            if (minAvgMax.length == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Vorgang nicht erfolgreich! Eingaben überprüfen.");
                alert.show();
            } else {
                Pfeildiagramm pfeildiagramm = new Pfeildiagramm(
                        minAvgMax[0],
                        minAvgMax[2],
                        wohnung.getZaehlerstandWertProFlaeche(m, j),
                        m
                );
                getChildren().add(pfeildiagramm);
            }
        });

        getChildren().addAll(zaehlerstandDiagramm, monat, jahr, erzeugeDiagramm);
    }

    // Getter und Setter

    public Wohnung getWohnung() {
        return wohnung;
    }

    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
    }
}
