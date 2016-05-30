package it.eurotn.panjea.magazzino.manager.manutenzionelistino.querybuilder;

import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;

public class FileEsternoArticoloFiltro extends AbstractFiltroSorgente {
    private ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino;

    /**
     *
     * @param parametriRicercaManutenzioneListino
     *            parametri ricerca man listino
     */
    public FileEsternoArticoloFiltro(final ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino) {
        this.parametriRicercaManutenzioneListino = parametriRicercaManutenzioneListino;
    }

    @Override
    public String getJoin() {
        return " inner join " + parametriRicercaManutenzioneListino.getTableNameFileEsterno()
                + " artEst on artEst.COD_ART=articoli.codice ";
    }

    @Override
    public String getWhere() {
        return "";
    }

}
