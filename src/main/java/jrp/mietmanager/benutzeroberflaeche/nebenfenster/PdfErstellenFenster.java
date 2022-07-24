package jrp.mietmanager.benutzeroberflaeche.nebenfenster;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jrp.mietmanager.logik.Immobilie;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PdfErstellenFenster extends Stage {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public PdfErstellenFenster(Immobilie immobilie) {
        oeffneFenster(immobilie);
    }

    private void oeffneFenster(Immobilie immobilie) {
        FXMLLoader pdfErstellenAnsichtLader = new FXMLLoader(getClass().getResource("pdfErstellenAnsicht.fxml"));

        Scene pdfErstellenAnsicht = null;
        try {
            pdfErstellenAnsicht = new Scene(pdfErstellenAnsichtLader.load());
            ((PdfErstellenAnsichtController) pdfErstellenAnsichtLader.getController()).uebergeben(immobilie, this);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Die \"PDF erstellen Ansicht\" konnte nicht geladen werden.", e);
        }


        getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("iconIMsmall.png")).toString()));
        setTitle("ImmoMa - PDF erstellen");
        setScene(pdfErstellenAnsicht);
        show();
    }
}
