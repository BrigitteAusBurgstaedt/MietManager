package com.mietmanager.gbo;

import com.mietmanager.gbo.grafik.Pfeildiagramm;
import com.mietmanager.gbo.grafik.ZaehlerstandDiagramm;
import com.mietmanager.logik.Wohnung;
import com.mietmanager.logik.Zaehlermodus;
import com.mietmanager.logik.Zaehlerstand;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller für wohnungsansicht.fxml.
 *
 * @since       1.0.3
 * @author      John Robin Pfeifer
 */
public class WohnungsansichtController implements Controller {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // Modell
    private Wohnung wohnung;

    // Ansicht
    @FXML private TableView<Zaehlerstand> tabelle;
    @FXML private DatePicker datumsFeld;
    @FXML private TextField wertFeld;
    @FXML private TextField monat;
    @FXML private TextField jahr;
    @FXML private VBox bandtachobehaelter;
    @FXML private VBox zaehlerstandDiagrammBehaelter;
    @FXML private Label titel;
    @FXML private Label flaeche;

    /**
     * @param daten die Daten, welche übergeben werden sollen
     */
    @Override
    public void uebergeben(Object... daten) {
        this.wohnung = (Wohnung) daten[0];
        einrichten();
    }

    private void einrichten() {
        titel.textProperty().bind(wohnung.bezeichnungProperty());
        Bindings.bindBidirectional(flaeche.textProperty(), wohnung.flaecheProperty(), new NumberStringConverter());

        zaehlerstandDiagrammBehaelter.getChildren().add(new ZaehlerstandDiagramm(wohnung));

        erstelleTabelle();
    }

    // Beim drücken des Hinzufügen-Tasters werden die eingegeben werte als Objekt erstellt
    public void hinzufuegen() {
        try {
            datumsFeld.setValue(datumsFeld.getConverter().fromString(datumsFeld.getEditor().getText()));
            wohnung.hinzufuegen(datumsFeld.getValue(), Double.parseDouble(wertFeld.getText()), Zaehlermodus.HEIZUNG);
        } catch (Exception e) {
            log.log(Level.WARNING, "Eingabe ungueltig.", e);

            Alert info = new Alert(Alert.AlertType.INFORMATION, "Es scheint als wäre etwas bei der Eingabe nicht ganz richtig.");
            info.setHeaderText("Überprüfen sie die Eingabe!");
            info.show();
        }
    }

    // Beim drücken des Entfernen-Tasters wird die Ausgewählte Reihe gelöscht
    public void entfernen() {
        // Liste aus den ausgewählten Zählerstände in der Tabelle
        ObservableList<Zaehlerstand> zuEntfernenedeZaehlerstaende = tabelle.getSelectionModel().getSelectedItems();
        if (zuEntfernenedeZaehlerstaende == null) return;

        if (wohnung.getZaehlerstaende().size() == 1){ // ich weiß nicht, warum das notwendig ist aber es ist notwendig
            wohnung.getZaehlerstaende().get(0).entfernen();
            return;
        }

        for (Zaehlerstand z : zuEntfernenedeZaehlerstaende)     // Zählerstände von der Wohnung entfernen
            z.entfernen();
    }

    /**
     * Erstellt die Tabelle die alle Zählerstände enthält.
     */
    private void erstelleTabelle() {
        tabelle.setItems(wohnung.getZaehlerstaende());
        tabelle.setPlaceholder(new Label("Es gibt noch keine Zählerstände."));
        tabelle.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Zaehlerstand, LocalDate> c1 = new TableColumn<>("Datum");
        c1.setCellValueFactory(zaehlerstandStringCellDataFeatures -> zaehlerstandStringCellDataFeatures.getValue().datumProperty());
        tabelle.getColumns().add(c1);

        TableColumn<Zaehlerstand, Number> c2 = new TableColumn<>("Zählerstand");
        c2.setCellValueFactory(zaehlerstandNumberCellDataFeatures -> zaehlerstandNumberCellDataFeatures.getValue().wertProperty());
        c2.getStyleClass().add("zahlenspalte");
        tabelle.getColumns().add(c2);

        TableColumn<Zaehlerstand, Number> c3 = new TableColumn<>("Differenz");
        c3.setCellValueFactory(zaehlerstandNumberCellDataFeatures -> zaehlerstandNumberCellDataFeatures.getValue().differenzProperty());
        c3.getStyleClass().add("zahlenspalte");
        tabelle.getColumns().add(c3);
    }

    public void erzeugeDiagramm() {
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
            bandtachobehaelter.getChildren().add(pfeildiagramm);
        }
    }
}