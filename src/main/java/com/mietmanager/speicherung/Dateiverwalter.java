package com.mietmanager.speicherung;

import com.mietmanager.logik.Immobilie;

import java.io.*;

public final class Dateiverwalter {


    /**
     * Privater Konstruktor, da es sich um eine Werkzeug-Klasse handelt.
     */
    private Dateiverwalter() {}

    public static void speichern(File datei, Immobilie immobilie) {

        try (FileOutputStream fos = new FileOutputStream(datei)) {

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(new ImmobilieMomentaufnahme(immobilie));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Immobilie lesen(File datei) {

        try (FileInputStream fis = new FileInputStream(datei)) {

            ObjectInputStream ois = new ObjectInputStream(fis);

            ImmobilieMomentaufnahme momentaufnahme = (ImmobilieMomentaufnahme) ois.readObject();

            return momentaufnahme.getImmobilie();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
