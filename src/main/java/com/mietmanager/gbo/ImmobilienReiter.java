package com.mietmanager.gbo;

import com.mietmanager.logik.Immobilie;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import com.mietmanager.gbo.Stilklasse;
import com.mietmanager.logik.Wohnung;

public class ImmobilienReiter extends Tab {

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


        behaelter.getChildren().add(erstelleTabelle());
        platzhalter.getChildren().add(behaelter);
        setContent(platzhalter);

    }

    // TODO: 02.06.2022 fix
    /*
    private TableView<ZaehlerstandListe> erstelleTabelle2() {
        TableView<ZaehlerstandListe> tabelle = new TableView<>();

        ZaehlerstandListe liste = new ZaehlerstandListe(immobilie);

        tabelle.setItems(immobilie.getZaehlerstaende());
        tabelle.setEditable(true);
        tabelle.setPlaceholder(new Label("Noch keine Zählerstände erfasst."));
        tabelle.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ZaehlerstandListe, LocalDate> c1 = new TableColumn<>("Datum");
        c1.setCellValueFactory(zaehlerstandStringCellDataFeatures -> zaehlerstandStringCellDataFeatures.getValue().datumProperty());
        c1.setSortable(false);
        tabelle.getColumns().add(c1);

        TableColumn<ZaehlerstandListe, Number>[] spalten = new TableColumn[immobilie.getWohnungsanzahl()];

        for (int i = 0; i < spalten.length; i++) {
            spalten[i].textProperty().bind(immobilie.getWohnungen().get(i).bezeichnungProperty());
            int finalI = i;
            spalten[i].setCellValueFactory(zaehlerstandListeNumberCellDataFeatures -> zaehlerstandListeNumberCellDataFeatures.getValue().getWerte().get(finalI));
            spalten[i].getStyleClass().add(Stilklasse.ZAHLENSPALTE.toString());
            spalten[i].setSortable(false);
            tabelle.getColumns().add(spalten[i]);
        }
        
        return tabelle;

    }

     */

    /**
     * Erstellt die Tabelle die alle Wohnungen enthält.
     */
    private TableView<Wohnung> erstelleTabelle() {
        TableView<Wohnung> tabelle = new TableView<>();

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

        tabelle.setPrefHeight(1080); // TODO: 05.05.2022 bessere Lösung (zeige (wenn möglich) nur die gefüllten Zeilen

        return tabelle;
    }
}
