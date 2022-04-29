package jrp.mietmanager.benutzeroberflaeche.hauptfenster;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import jrp.mietmanager.logik.Wohnung;

public class TextFeldZelle extends TableCell<Wohnung, String> {

    private TextField textFeld;

    public TextFeldZelle() {
        erstelleTextFeld();
        setGraphic(textFeld);
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            textFeld.selectAll();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(item);
        } else {
            if (isEditing()) {
                textFeld.setText(getString());
            }
        }
    }

    private void erstelleTextFeld() {
        textFeld = new TextField(getString());
        textFeld.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textFeld.setOnAction((e) -> commitEdit(textFeld.getText()));
        textFeld.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                System.out.println("Commiting " + textFeld.getText());
                commitEdit(textFeld.getText());
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem();
    }

}
