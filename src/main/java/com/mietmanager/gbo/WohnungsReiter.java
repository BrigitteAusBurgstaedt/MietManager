package com.mietmanager.gbo;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.mietmanager.gbo.Stilklasse;
import com.mietmanager.logik.Wohnung;
import com.mietmanager.logik.Zaehlermodus;
import com.mietmanager.logik.Zaehlerstand;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WohnungsReiter extends Tab {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final TableView<Zaehlerstand> tabelle = new TableView<>();
    private final Wohnung wohnung;

    /**
     * Konstruktor des Wohnungsreiters welcher die Methode erzeugeReiter aufruft.
     *
     * @param wohnung Übernimmt die Wohnung, welche vom Tab/Reiter widergespiegelt werden soll.
     */
    public WohnungsReiter(Wohnung wohnung) {
        this.wohnung = wohnung;
        erzeugeReiter();
    }

    /**
     * Erzeugt den Tab/Reiter welcher der Tab Pane hinzugefügt werden soll.
     */
    private void erzeugeReiter() {
        textProperty().bind(wohnung.bezeichnungProperty());

        //
        // Behälter
        //
        VBox platzhalter = new VBox();
        VBox behaelter = new VBox();
        HBox fuerEingabe = new HBox();
        HBox fuerTaster = new HBox(5);

        platzhalter.getStyleClass().add(Stilklasse.PLATZHALTER);
        fuerEingabe.getStyleClass().add(Stilklasse.PLATZHALTER);
        fuerTaster.getStyleClass().add(Stilklasse.PLATZHALTER);
        behaelter.getStyleClass().add(Stilklasse.BEHAELTER);

        fuerTaster.setAlignment(Pos.TOP_RIGHT);

        //
        // Steuerelemente
        //
        DatePicker datumsFeld = new DatePicker();
        TextField wertFeld = new TextField();
        Button hinzufuegen = new Button("Hinzufügen");
        Button entfernen = new Button("Entfernen");

        datumsFeld.setPromptText("TT.MM.JJJJ");

        // Beim drücken des Hinzufügen-Tasters werden die eingegeben werte als Objekt erstellt
        hinzufuegen.setOnAction(actionEvent -> {
            try {
                datumsFeld.setValue(datumsFeld.getConverter().fromString(datumsFeld.getEditor().getText()));
                wohnung.hinzufuegen(datumsFeld.getValue(), Double.parseDouble(wertFeld.getText()), Zaehlermodus.HEIZUNG);
            } catch (Exception e) {
                log.log(Level.WARNING, "Eingabe ungueltig.", e);

                Alert info = new Alert(Alert.AlertType.INFORMATION, "Es scheint als wäre etwas bei der Eingabe nicht ganz richtig.");
                info.setHeaderText("Überprüfen sie die Eingabe!");
                info.show();
            }
        });

        // Beim drücken des Entfernen-Tasters wird die Ausgewählte Reihe gelöscht
        entfernen.setOnAction(actionEvent -> {
            // Liste aus den ausgewählten Zählerstände in der Tabelle
            ObservableList<Zaehlerstand> zuEntfernenedeZaehlerstaende = tabelle.getSelectionModel().getSelectedItems();
            if (zuEntfernenedeZaehlerstaende == null) return;

            if (wohnung.getZaehlerstaende().size() == 1){ // ich weiß nicht, warum das notwendig ist aber es ist notwendig
                wohnung.getZaehlerstaende().get(0).entfernen();
                return;
            }

            for (Zaehlerstand z : zuEntfernenedeZaehlerstaende)     // Zählerstände von der Wohnung entfernen
                z.entfernen();
        });

        erstelleTabelle();

        //
        // Elemente in Container packen
        //
        fuerEingabe.getChildren().addAll(datumsFeld, wertFeld);
        fuerTaster.getChildren().addAll(hinzufuegen, entfernen);
        behaelter.getChildren().addAll(tabelle, fuerEingabe, fuerTaster);
        platzhalter.getChildren().add(behaelter);
        setContent(platzhalter);
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

}
