package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import jrp.mietmanager.logik.Wohnung;
import jrp.mietmanager.logik.Zaehlerstand;

public class PdfAnsicht extends VBox {
    private Wohnung wohnung;

    public PdfAnsicht(Wohnung wohnung) {
        this.wohnung = wohnung;

        VBox platzhalter = new VBox();
        VBox fuerTitel = new VBox();
        Label titel = new Label();

        platzhalter.getStyleClass().add("platzhalter");
        fuerTitel.getStyleClass().add("behaelter");
        titel.getStyleClass().add("titel-label");

        fuerTitel.setAlignment(Pos.CENTER);
        titel.textProperty().bind(wohnung.bezeichnungProperty());

        fuerTitel.getChildren().add(titel);
        platzhalter.getChildren().add(fuerTitel);
        getChildren().add(platzhalter);

        getStyleClass().add("hauptbehaelter");
        erstelleDiagramm();
    }

    private void erstelleDiagramm() {

        //
        // Diagrammelemente
        //
        NumberAxis xAchse = new NumberAxis("Monat", 2022.0, 2022.366, 0.0305); // X-Achse mit Datum
        NumberAxis yAchse = new NumberAxis(); // Y-Achse mit Zählerwerten
        LineChart<Number, Number> diagramm = new LineChart<>(xAchse, yAchse);
        ObservableList<XYChart.Data<Number, Number>> daten = FXCollections.observableArrayList();
        XYChart.Series<Number, Number> serie = new XYChart.Series<>(daten);

        // Füllen der Datenreihe (nötig, wenn zwischen Wohnungen gewechselt wird)
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

                double tag = number.doubleValue() % 1;

                return switch (Double.toString(tag)) {
                    case "2022.0" -> "Jan";
                    case "2022.0305" -> "Feb";
                    case "2022.061" -> "Mär";
                    case "2022.0915" -> "Apr";
                    case "2022.122" -> "Mai";
                    case "2022.1525" -> "Jun";
                    case "2022.183" -> "Jul";
                    case "2022.2135" -> "Aug";
                    case "2022.244" -> "Sep";
                    case "2022.2745" -> "Okt";
                    case "2022.305" -> "Nov";
                    case "2022.3355" -> "Dez";
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

    // Getter und Setter

    public Wohnung getWohnung() {
        return wohnung;
    }

    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
    }
}
