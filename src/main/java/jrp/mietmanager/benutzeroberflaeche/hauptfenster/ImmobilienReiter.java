package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import jrp.mietmanager.logik.Immobilie;
import jrp.mietmanager.logik.Wohnung;

public class ImmobilienReiter extends Tab {
    private final TableView<Wohnung> tabelle = new TableView<>();
    private final Immobilie immobilie;

    public ImmobilienReiter(Immobilie immobilie) {
        this.immobilie = immobilie;
        erzeugeReiter();
    }

    private void erzeugeReiter() {
        textProperty().bind(immobilie.bezeichnungProperty());

        VBox platzhalter = new VBox();
        VBox behaelter = new VBox();

        platzhalter.getStyleClass().add("platzhalter");
        behaelter.getStyleClass().add("behaelter");

        erstelleTabelle();

        behaelter.getChildren().add(tabelle);
        platzhalter.getChildren().add(behaelter);
        setContent(platzhalter);

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
        c3.getStyleClass().add("zahlenspalte");
        tabelle.getColumns().add(c3);

        tabelle.setPrefHeight(1080); // TODO: 05.05.2022 bessere Lösung (zeige (wenn möglich) nur die gefüllten Zeilen
    }
}
