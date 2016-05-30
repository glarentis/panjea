package it.eurotn.panjea.ordini.manager.documento.evasione.interfaces;

import java.util.Date;

import javax.ejb.Local;

import it.eurotn.panjea.ordini.manager.documento.evasione.DatiDistintaCaricoVerifica;

@Local
public interface DistintaCaricoVerificaManager {
    /**
     *
     * @param dataInizioTrasporto
     *            data inizio trasp.
     * @return dati per la verifica degli ordini in produzione
     */
    DatiDistintaCaricoVerifica caricaDatiVerifica(Date dataInizioTrasporto);
}
