package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jrp.mietmanager.benutzeroberflaeche.Stilklasse;
import jrp.mietmanager.benutzeroberflaeche.grafik.ZaehlerstandDiagramm;
import jrp.mietmanager.logik.Immobilie;
import jrp.mietmanager.logik.Wohnung;

import java.util.logging.Logger;

public class PdfAnsicht extends VBox {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Immobilie immobilie;
    private Wohnung wohnung;

    public PdfAnsicht(Immobilie immobilie, Wohnung wohnung) {
        this.wohnung = wohnung;
        this.immobilie = immobilie;

        VBox platzhalter = new VBox();
        VBox fuerTitel = new VBox();
        Label titel = new Label();

        platzhalter.getStyleClass().add(Stilklasse.PLATZHALTER.toString());
        fuerTitel.getStyleClass().add(Stilklasse.BEHAELTER.toString());
        titel.getStyleClass().add(Stilklasse.TITEL_LABEL.toString());

        fuerTitel.setAlignment(Pos.CENTER);
        titel.textProperty().bind(wohnung.bezeichnungProperty());

        fuerTitel.getChildren().add(titel);
        platzhalter.getChildren().add(fuerTitel);
        getChildren().add(platzhalter);

        getStyleClass().add(Stilklasse.HAUPTBEHAELTER.toString());
        erstelleDiagramm();
    }

    private void erstelleDiagramm() {
        ZaehlerstandDiagramm zaehlerstandDiagramm = new ZaehlerstandDiagramm(wohnung);
        // Pfeildiagramm pfeildiagramm = new Pfeildiagramm(immobilie);

        getChildren().add(zaehlerstandDiagramm);
    }

    // Getter und Setter

    public Wohnung getWohnung() {
        return wohnung;
    }

    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
    }
}
