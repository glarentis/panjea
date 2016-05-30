package it.eurotn.panjea.contabilita.manager.spesometro.builder;

import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.aggregato.SpesometroAggregatoBuilder;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.analitico.SpesometroAnaliticoBuilder;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente.TipologiaDati;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author fattazzo
 *
 */
public class SpesometroRecordBuilderFactory {

    /**
     * Restituisce il builder corretto in base alla tipologia dati dei parametri di creazione.
     *
     * @param panjeaDAO
     *            panjeaDAO
     * @param azienda
     *            azienda
     * @param parametri
     *            parametri di creazione
     * @return builder
     */
    public SpesometroRecordBuilder getBuilder(PanjeaDAO panjeaDAO, AziendaAnagraficaDTO azienda,
            ParametriCreazioneComPolivalente parametri) {

        if (parametri.getTipologiaDati() == TipologiaDati.AGGREGATI) {
            return new SpesometroAggregatoBuilder(panjeaDAO, azienda, parametri);
        }

        return new SpesometroAnaliticoBuilder(panjeaDAO, azienda, parametri);
    }
}
