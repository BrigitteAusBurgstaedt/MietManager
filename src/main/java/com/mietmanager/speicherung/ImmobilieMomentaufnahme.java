package com.mietmanager.speicherung;

import com.mietmanager.logik.Immobilie;
import com.mietmanager.logik.Zaehlerstand;
import com.mietmanager.logik.Wohnung;
import com.mietmanager.logik.Zaehlermodus;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class ImmobilieMomentaufnahme implements Serializable {
    private final String bezeichnung;
    private final WohnungMomentaufnahme[] wohnungen;

    public ImmobilieMomentaufnahme(Immobilie immobilie) {
        List<Wohnung> hilfWohnungen = immobilie.getWohnungen();

        bezeichnung = immobilie.toString();
        wohnungen = new WohnungMomentaufnahme[hilfWohnungen.size()];

        for (int i = 0; i < hilfWohnungen.size(); i++) {
            wohnungen[i] = new WohnungMomentaufnahme(hilfWohnungen.get(i));
        }

    }

    public Immobilie getImmobilie() {
        Immobilie immobilie = new Immobilie(bezeichnung);
        for (WohnungMomentaufnahme wohnungMomentaufnahme : wohnungen)
            wohnungMomentaufnahme.setupWohnung(immobilie);
        return immobilie;
    }

}

class WohnungMomentaufnahme implements Serializable {
    private final String bezeichnung;
    private final int mieteranzahl;
    private final double flaeche;
    private final ZaehlerstandMomentaufnahme[] zaehlerstaende;

    WohnungMomentaufnahme(Wohnung wohnung) {
        List<Zaehlerstand> hilfZaehlerstaende = wohnung.getZaehlerstaende();

        bezeichnung = wohnung.toString();
        mieteranzahl = wohnung.getMieteranzahl();
        flaeche = wohnung.getFlaeche();

        zaehlerstaende = new ZaehlerstandMomentaufnahme[hilfZaehlerstaende.size()];

        for (int i = 0; i < hilfZaehlerstaende.size(); i++) {
            zaehlerstaende[i] = new ZaehlerstandMomentaufnahme(hilfZaehlerstaende.get(i));
        }
    }

    void setupWohnung(Immobilie immobilie) {
        Wohnung wohnung = immobilie.hinzufuegen(bezeichnung, mieteranzahl, flaeche);
        for (ZaehlerstandMomentaufnahme zaehlerstandMomentaufnahme : zaehlerstaende)
            zaehlerstandMomentaufnahme.setupZaehlerstaende(wohnung);
    }

}

class ZaehlerstandMomentaufnahme implements Serializable {
    private final LocalDate datum;
    private final double wert;
    private final Zaehlermodus modus;

    ZaehlerstandMomentaufnahme(Zaehlerstand zaehlerstand) {
        datum = zaehlerstand.getDatum();
        wert = zaehlerstand.getWert();
        modus = zaehlerstand.getModus();
    }

    void setupZaehlerstaende(Wohnung wohnung) {
        try {
            wohnung.hinzufuegen(datum, wert, modus);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
