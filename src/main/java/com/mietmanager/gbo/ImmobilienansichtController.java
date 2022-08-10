package com.mietmanager.gbo;

import com.mietmanager.logik.Immobilie;
import com.mietmanager.logik.Wohnung;
import com.mietmanager.logik.Zaehlermodus;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller für immobilienansicht.fxml.
 *
 *  @since       1.0.3
 *  @author      John Robin Pfeifer
 */
public class ImmobilienansichtController implements Controller {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    // Modell
    private Immobilie immobilie;

    // Ansicht
    @FXML private TableView<Wohnung> tabelle;
    @FXML private VBox gesamteingabe;
    @FXML private Label titel;
    @FXML private Label wohnungsanzahl;

    @Override
    public void uebergeben(Object... daten) {
        this.immobilie = (Immobilie) daten[0];
        einrichten();
    }

    private void einrichten() {
        titel.textProperty().bind(immobilie.bezeichnungProperty());
        wohnungsanzahl.setText(String.valueOf(immobilie.getWohnungen().size()));

        erstelleTabelle();
        erstelleGesamteingabe();
    }

    private void erstelleGesamteingabe() {
        GridPane raster = new GridPane();
        raster.setVgap(10);

        Label datum = new Label("Datum");
        Label[] kopf = new Label[immobilie.getWohnungen().size()];
        DatePicker datumsFeld = new DatePicker();
        datumsFeld.setPromptText("JJJJ-MM-TT");
        TextField[] eingabe = new TextField[immobilie.getWohnungen().size()];

        raster.add(datum, 0,0);
        raster.add(datumsFeld, 0, 1);

        for (int i = 0; i < immobilie.getWohnungen().size(); i++) {
            kopf[i] = new Label();
            kopf[i].setMaxWidth(Double.MAX_VALUE);
            kopf[i].setAlignment(Pos.TOP_CENTER);
            kopf[i].textProperty().bind(immobilie.getWohnungen().get(i).bezeichnungProperty());

            eingabe[i] = new TextField();

            raster.add(kopf[i], i + 1, 0);
            raster.add(eingabe[i], i + 1, 1);
        }

        Button hinzufuegen = new Button("Hinzufügen");
        hinzufuegen.setOnAction(event -> {
            try {
                datumsFeld.setValue(datumsFeld.getConverter().fromString(datumsFeld.getEditor().getText()));

                for (int i = 0; i < immobilie.getWohnungen().size(); i++) {
                    immobilie.getWohnungen().get(i).hinzufuegen(datumsFeld.getValue(), Double.parseDouble(eingabe[i].getText()), Zaehlermodus.HEIZUNG);
                }

            } catch (Exception e) {
                log.log(Level.WARNING, "Eingabe ungueltig.", e);

                Alert info = new Alert(Alert.AlertType.INFORMATION, "Es scheint als wäre etwas bei der Eingabe nicht ganz richtig.");
                info.setHeaderText("Überprüfen sie die Eingabe!");
                info.show();
            }

            datumsFeld.setValue(null);
            for (int i = 0; i < immobilie.getWohnungen().size(); i++) {
                eingabe[i].setText("");
            }

        });

        gesamteingabe.getChildren().add(raster);
        gesamteingabe.getChildren().add(hinzufuegen);
    }

    /**
     * Erstellt die Tabelle die alle Wohnungen enthält.
     */
    private void erstelleTabelle() {
        tabelle.setItems(immobilie.getWohnungen()); // Tabelle soll die Wohnungen der enthalten
        tabelle.setEditable(true); // Die Wohnungen sollen bearbeitbar sein
        tabelle.setPlaceholder(new Label("Noch keine Wohnungen erstellt."));
        tabelle.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        TableColumn<Wohnung, String> c1 = new TableColumn<>("Bezeichnung");
        c1.setCellValueFactory(wohnungStringCellDataFeatures -> wohnungStringCellDataFeatures.getValue().bezeichnungProperty());
        c1.setCellFactory(TextFieldTableCell.forTableColumn());
        tabelle.getColumns().add(c1);

        /*
        Wird zum jetzigen Zeitpunkt des Programmes noch nicht benötigt. Die Mieteranzahl kann aber später für die Kostenberechnung wichtig sein

        TableColumn<Wohnung, Number> c2 = new TableColumn<>("Mieteranzahl");
        c2.setCellValueFactory(wohnungIntegerCellDataFeatures -> wohnungIntegerCellDataFeatures.getValue().mieteranzahlProperty());
        c2.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        c2.getStyleClass().add("zahlenspalte");
        tabelle.getColumns().add(c2);
         */

        TableColumn<Wohnung, Number> c3 = new TableColumn<>("Wohnungsnutzfläche in m²");
        c3.setCellValueFactory(wohnungNumberTableColumn -> wohnungNumberTableColumn.getValue().flaecheProperty());
        c3.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        c3.getStyleClass().add(Stilklasse.ZAHLENSPALTE);
        tabelle.getColumns().add(c3);
    }
}
