package com.mietmanager.gbo;

/**
 * Controller Interface.
 *
 * @since 1.0.1
 * @see FXMLHelfer
 * @author John Robin Pfeifer
 */
public interface Controller {

    /**
     * Vorlage für eine Übergabefunktion. Die Daten werden von dem {@link FXMLHelfer} an den Controller
     * weitergegeben.
     *
     * @param daten die Daten, welche übergeben werden sollen
     */
    void uebergeben(Object... daten);
}
