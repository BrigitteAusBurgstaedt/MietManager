package jrp.mietmanager.logik;

import javafx.scene.control.Tab;

public interface Visualisierbar {


    /**
     * Methode zum öffnen des Tab/Reiters, welcher die Klasse visualisiert.
     *
     * @return Gibt den dazugehörigen Tab/Reiter zurück.
     */
    Tab oeffneReiter();

    /**
     * Wahrheitswert welcher den Zustand des Tab/Reiters widerspiegelt.
     *
     * @return Gibt wahr zurück, wenn der zugehörige Tab/Reiter geöffnet ist. Ansonsten wird falsch zurückgegeben.
     */
    boolean istGeoeffnet();
}
