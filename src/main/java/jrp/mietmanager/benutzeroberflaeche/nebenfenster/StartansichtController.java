package jrp.mietmanager.benutzeroberflaeche.nebenfenster;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartansichtController {

    @FXML
    public void neueImmobilieErstellen(ActionEvent actionEvent) {
        Button taster = (Button) actionEvent.getSource();
        Stage fenster = (Stage) taster.getScene().getWindow();
        fenster.hide();
        new NeueImmobilieFenster();
    }
}
