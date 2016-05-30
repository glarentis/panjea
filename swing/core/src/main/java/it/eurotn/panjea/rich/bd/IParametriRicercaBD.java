package it.eurotn.panjea.rich.bd;

import java.util.List;

import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.ParametriRicercaDTO;

public interface IParametriRicercaBD {

    /**
     * 
     * @param classeParametro
     *            classe del parametro da caricare
     * @param nome
     *            nome del parametro
     */
    void cancellaParametro(Class<? extends AbstractParametriRicerca> classeParametro, String nome);

    /**
     * 
     * @param classeParametro
     *            classe dei parametri da caricare
     * @return parametri salvati per quel tipo di ricerca
     */
    List<ParametriRicercaDTO> caricaParametri(Class<? extends AbstractParametriRicerca> classeParametro);

    /**
     * @param classeParametro
     *            classe del parametro da caricare
     * @param nome
     *            nome del parametro
     * @return Parametro con nome e classe. Null se non esiste.
     */
    AbstractParametriRicerca caricaParametro(Class<? extends AbstractParametriRicerca> classeParametro, String nome);

    /**
     * 
     * @param parametro
     *            parametro da salvare
     * @return parametro salvato
     */
    AbstractParametriRicerca salvaParametro(AbstractParametriRicerca parametro);
}
