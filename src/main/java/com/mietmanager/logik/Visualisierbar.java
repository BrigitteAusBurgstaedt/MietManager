package com.mietmanager.logik;

import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public interface Visualisierbar {


    /**
     * Methode zum öffnen des Tab/Reiters, welcher die Klasse visualisiert.
     *
     * @return Gibt den dazugehörigen Tab/Reiter zurück.
     */
    Tab oeffneReiter();

    /**
     * Methode zum öffnen der PDF-Ansicht, welche die Klasse visualisiert.
     *
     * @return Gibt die dazugehörige PDF-Ansicht zurück.
     */
    VBox oeffnePdfAnsicht(Immobilie immobilie);

    /**
     * Wahrheitswert welcher den Zustand des Tab/Reiters widerspiegelt.
     *
     * @return Gibt wahr zurück, wenn der zugehörige Tab/Reiter geöffnet ist. Ansonsten wird falsch zurückgegeben.
     */
    boolean istGeoeffnet();

    /**
     * Wahrheitswert welcher widerspiegelt, ob ein PDF gezeigt werden soll.
     *
     * @return Gibt wahr zurück, wenn eine PDF-Ansicht benötigt wird.
     */
    boolean brauchtPdfAnsicht();
}
