package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

/**
 *
 * Aggiunge il testo della query per filtrare la sorgente degli articoli.
 *
 * @author giangi
 * @version 1.0, 11/nov/2010
 *
 */
public abstract class AbstractFiltroSorgente {

    /**
     * 
     * @return join da aggiungere alla query per il filtro
     */
    public abstract String getJoin();

    /**
     * 
     * @return filtro da aggiungere alla query.
     */
    public abstract String getWhere();
}
