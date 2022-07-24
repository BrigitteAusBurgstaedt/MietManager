package jrp.mietmanager;

import javafx.application.Application;
import javafx.stage.Stage;
import jrp.mietmanager.benutzeroberflaeche.nebenfenster.Startfenster;

import java.io.IOException;
import java.util.logging.*;

// Alternativer Name: ImmoMa (für Immobilien Manager)

/**
 * Startpunkt der Anwendung.
 *
 * @since       1.0
 * @see         Startfenster
 * @author      John Robin Pfeifer
 */
public class MietManager extends Application {
    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Öffnet das {@link Startfenster}.
     *
     * @param hauptfenster  Das von der Anwendung erzeugte Hauptfenster. (ignoriert)
     * @see                 Startfenster
     */
    @Override
    public void start(Stage hauptfenster) {
        new Startfenster();
    }

    /**
     * Konfiguriert zunächst den Logger und startet dann die Anwendung.
     *
     * @param args  Von der Konsole übergebene Parameter. (ignoriert)
     * @see         #konfiguriereLogger()
     */
    public static void main(String[] args) {
        konfiguriereLogger();
        launch();
    }

    /**
     * Konfiguration des Loggers. Es wird ein File Handler und ein Console Handler eingerichtet,
     * sowie die Warnstufen eingestellt. Das Logbuch wird als "log.txt" gespeichert.
     *
     * @see Logger
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
