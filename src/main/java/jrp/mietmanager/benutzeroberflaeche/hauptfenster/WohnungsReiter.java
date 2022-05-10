package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import jrp.mietmanager.logik.Wohnung;
import jrp.mietmanager.logik.Zaehlermodus;
import jrp.mietmanager.logik.Zaehlerstand;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WohnungsReiter extends Tab {
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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

        platzhalter.getStyleClass().add("platzhalter");
        fuerEingabe.getStyleClass().add("platzhalter");
        fuerTaster.getStyleClass().add("platzhalter");
        behaelter.getStyleClass().add("behaelter");

        fuerTaster.setAlignment(Pos.TOP_RIGHT);

        //
        // Steuerelemente
        //
        DatePicker datumsFeld = new DatePicker();
        TextField wertFeld = new TextField();
        Button hinzufuegen = new Button("Hinzufügen");
        Button entfernen = new Button("Entfernen");

        datumsFeld.setPromptText("tt.mm.jjjj");

        // Beim drücken des Hinzufügen-Tasters werden die eingegeben werte als Objekt erstellt
        hinzufuegen.setOnAction(actionEvent -> {
            try {
                datumsFeld.setValue(datumsFeld.getConverter().fromString(datumsFeld.getEditor().getText()));
                wohnung.hinzufuegen(new Zaehlerstand(datumsFeld.getValue(), Double.parseDouble(wertFeld.getText()), Zaehlermodus.HEIZUNG));
            } catch (Exception e) {
                log.log(Level.WARNING, "Eingabe ungueltig.", e);

                Alert info = new Alert(Alert.AlertType.INFORMATION, "Es scheint als wäre etwas bei der Eingabe nicht ganz richtig.");
                info.setHeaderText("Überprüfen sie die Eingabe!");
                info.show();
            }
        });

        // Beim drücken des Entfernen-Tasters wird die Ausgewählte Reihe gelöscht TODO: Ist sie auch als Objekt gelöscht?
        entfernen.setOnAction(actionEvent -> {
            tabelle.getItems().removeAll(
                    tabelle.getSelectionModel().getSelectedItems()
            );
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
        c1.setSortable(false);
        tabelle.getColumns().add(c1);

        TableColumn<Zaehlerstand, Number> c2 = new TableColumn<>("Zählerstand");
        c2.setCellValueFactory(zaehlerstandNumberCellDataFeatures -> zaehlerstandNumberCellDataFeatures.getValue().wertProperty());
        c2.getStyleClass().add("zahlenspalte");
        c2.setSortable(false);
        tabelle.getColumns().add(c2);

        TableColumn<Zaehlerstand, Number> c3 = new TableColumn<>("Differenz");
        c3.setCellValueFactory(zaehlerstandNumberCellDataFeatures -> zaehlerstandNumberCellDataFeatures.getValue().differenzProperty());
        c3.getStyleClass().add("zahlenspalte");
        c3.setSortable(false);
        tabelle.getColumns().add(c3);

    }

}
