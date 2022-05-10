package jrp.mietmanager;

import javafx.application.Application;
import javafx.stage.Stage;
import jrp.mietmanager.benutzeroberflaeche.nebenfenster.Startfenster;

import java.io.IOException;
import java.util.logging.*;

// Alternativer Name: ImmoMa (für Immobilien Manager)
public class MietManager extends Application {
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void start(Stage hauptfenster) {
        new Startfenster();
    }

    public static void main(String[] args) {
        konfiguriereLogger();
        launch();
    }

    /**
     * Konfiguration des Loggers. Es wird ein File Handler und ein Console Handler eingerichtet,
     * sowie die Warnstufen eingestellt. Das Logbuch wird als "log.txt" gespeichert.
     */
    private static void konfiguriereLogger() {

        log.setUseParentHandlers(false);    // Der Parent Handler soll nicht genutzt werden, damit ich den Handler selbst definieren kann
        log.setLevel(Level.ALL);            // Jede Warnstufe soll im Log erscheinen

        // Handler einreichen
        ConsoleHandler konsolenSteuerung = new ConsoleHandler();
        konsolenSteuerung.setLevel(Level.ALL);  // Ausgabe von allen Nachrichten auf der Konsole
        log.addHandler(konsolenSteuerung);

        try {
            FileHandler logbuchPfleger = new FileHandler("log.txt", true);  // Das Logbuch heißt "log.txt" und die Logbucheinträge werden der Datei angehängt
            logbuchPfleger.setLevel(Level.ALL);                                           // Ausgabe von allen Nachrichten auf der Konsole
            logbuchPfleger.setFormatter(new SimpleFormatter());                           // Formatierung der Logbucheinträge
            log.addHandler(logbuchPfleger);
        } catch (IOException e) {
            log.log(Level.SEVERE, "FileHandler konnte das Logbuch nicht laden.", e);
        }

    }

}
