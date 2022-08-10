package com.mietmanager.speicherung;

import com.mietmanager.logik.Immobilie;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * IO-Klasse für Immobilien.
 *
 * @author John Robin Pfeifer
 * @see Immobilie
 * @since 1.0.0
 */
public final class Dateiverwalter {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Privater Konstruktor, da es sich um eine Werkzeug-Klasse handelt.
     */
    private Dateiverwalter() {}

    /**
     * Speichert eine Immobilie als .immo.
     *
     * @param datei die Datei für die Immobilie
     * @param immobilie die zu speichernde Immobilie
     */
    public static void speichern(File datei, Immobilie immobilie) {

        try (FileOutputStream fos = new FileOutputStream(datei)) {

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(new ImmobilieMomentaufnahme(immobilie));

        } catch (IOException e) {
            log.log(Level.SEVERE, "Speichern fehlgeschlagen", e);
        }
    }

    /**
     * Liest eine Immobilie aus einer .immo Datei.
     *
     * @param datei die .immo datei
     * @return die Immobilie
     * @see Immobilie
     */
    public static Immobilie lesen(File datei) {

        try (FileInputStream fis = new FileInputStream(datei)) {

            ObjectInputStream ois = new ObjectInputStream(fis);

            ImmobilieMomentaufnahme momentaufnahme = (ImmobilieMomentaufnahme) ois.readObject();

            return momentaufnahme.getImmobilie();

        } catch (IOException | ClassNotFoundException e) {
            log.log(Level.SEVERE, "Lesen fehlgeschlagen", e);
        }

        return null;
    }

}
