package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import jrp.mietmanager.logik.Wohnung;
import jrp.mietmanager.logik.Zaehlerstand;

public class PdfAnsicht extends VBox {

    public PdfAnsicht(Wohnung wohnung) {
        getStyleClass().add("hauptbehaelter");
        erstelleDiagramm(wohnung);
    }

    private void erstelleDiagramm(Wohnung wohnung) {

        //
        // Diagrammelemente
        //
        NumberAxis xAchse = new NumberAxis("Monat", 0, 13, 1); // X-Achse mit Datum
        NumberAxis yAchse = new NumberAxis(); // Y-Achse mit Zählerwerten
        LineChart<Number, Number> diagramm = new LineChart<>(xAchse, yAchse);
        ObservableList<XYChart.Data<Number, Number>> daten = FXCollections.observableArrayList();
        XYChart.Series<Number, Number> serie = new XYChart.Series<>(daten);

        // Füllen der Datenreihe Todo: Unnötig?
        for (Zaehlerstand z : wohnung.getZaehlerstaende()) {
            daten.add(z.getDatenpunkt());
            System.out.println("Datum des Zählerstandes: " + z.getDatenpunkt().XValueProperty().toString());
        }

        // TODO: 04.05.2022 Durchleuchten  
        wohnung.getZaehlerstaende().addListener((ListChangeListener<Zaehlerstand>) veraenderung -> {
            while (veraenderung.next()) {

                if (veraenderung.wasAdded()) {
                    for (Zaehlerstand z : veraenderung.getAddedSubList()) {
                        daten.add(z.getDatenpunkt());
                    }
                } else {
                    for (int i = veraenderung.getFrom(); veraenderung.getTo() != wohnung.getZaehlerstaende().size() ? i <= veraenderung.getTo() : i < veraenderung.getTo(); i++) {
                        if (i < wohnung.getZaehlerstaende().size())
                            daten.remove(i);
                        daten.add(i, wohnung.getZaehlerstaende().get(i).getDatenpunkt());
                    }
                }
            }
        });

        // Beschriftung der Achse
        xAchse.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number number) {

                float tag = number.floatValue() % 1;

                return switch (Float.toString(tag)) {
                    case "0.0" -> "Jan";
                    case "1.0" -> "Feb";
                    case "2.0" -> "Mär";
                    case "3.0" -> "Apr";
                    case "4.0" -> "Mai";
                    case "5.0" -> "Jun";
                    case "6.0" -> "Jul";
                    case "7.0" -> "Aug";
                    case "8.0" -> "Sep";
                    case "9.0" -> "Okt";
                    case "10.0" -> "Nov";
                    case "11.0" -> "Dez";
                    default -> null;
                };
            }

            @Override
            public Number fromString(String s) {
                return null;
            }
        });

        // Serie hinzufügen
        diagramm.getData().add(serie);

        getChildren().add(diagramm);
    }
}
