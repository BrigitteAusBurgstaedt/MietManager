package jrp.mietmanager.logik;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class ZaehlerstandListe {

    private final ObjectProperty<LocalDate> datum;
    private final ObservableList<ObjectProperty<Wohnung>> wohnungen;

    public ZaehlerstandListe(Immobilie immobilie) {

    }

}
