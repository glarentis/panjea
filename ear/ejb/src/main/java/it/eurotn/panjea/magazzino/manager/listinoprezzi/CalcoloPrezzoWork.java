package it.eurotn.panjea.magazzino.manager.listinoprezzi;

import java.math.BigDecimal;

import commonj.work.Work;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.PrezzoArticoloCalculator;

public class CalcoloPrezzoWork implements Work {

    private PrezzoArticoloCalculator prezzoArticoloCalculator;
    private ParametriCalcoloPrezzi parametriCalcoloPrezzi;
    private ListinoPrezziDTO listinoPrezziDTO;
    private ArticoloManager articoloManager;

    /**
     * @param articoloManager
     *            manager articoli
     * @param prezzoArticoloCalculator
     *            calculatore per il prezzo
     * @param parametriCalcoloPrezzi
     *            parametri di calcolo
     */
    public CalcoloPrezzoWork(final ArticoloManager articoloManager,
            final PrezzoArticoloCalculator prezzoArticoloCalculator,
            final ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        super();
        this.articoloManager = articoloManager;
        this.prezzoArticoloCalculator = prezzoArticoloCalculator;
        this.parametriCalcoloPrezzi = parametriCalcoloPrezzi;
    }

    /**
     *
     * @return risultato del calcolo prezzo
     */
    public ListinoPrezziDTO getRisultato() {
        return listinoPrezziDTO;
    }

    @Override
    public boolean isDaemon() {
        return false;
    }

    @Override
    public void release() {
    }

    @Override
    public void run() {
        this.listinoPrezziDTO = new ListinoPrezziDTO();
        PoliticaPrezzo politicaPrezzo = prezzoArticoloCalculator.calcola(parametriCalcoloPrezzi);
        RisultatoPrezzo<BigDecimal> risultatoPrezzo = politicaPrezzo.getPrezzi().getRisultatoPrezzo(0.0);
        if (risultatoPrezzo != null) {
            listinoPrezziDTO.setNumeroDecimaliPrezzo(risultatoPrezzo.getNumeroDecimali());
            listinoPrezziDTO.setPrezzo(risultatoPrezzo.getValue());
            listinoPrezziDTO.setArticolo(null);
        }

        RisultatoPrezzo<Sconto> risultatoSconto = politicaPrezzo.getSconti().getRisultatoPrezzo(0.0);
        if (risultatoSconto != null) {
            listinoPrezziDTO.setSconto(risultatoSconto.getValue());
        }
        ArticoloLite articolo = articoloManager.caricaArticoloLite(parametriCalcoloPrezzi.getIdArticolo());
        listinoPrezziDTO.setArticolo(articolo);
    }

}
