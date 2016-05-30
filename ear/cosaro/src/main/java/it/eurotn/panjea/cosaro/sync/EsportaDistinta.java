package it.eurotn.panjea.cosaro.sync;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;

@Local
public interface EsportaDistinta {
    /**
     * Esporta i file per le bilance
     *
     * @param distinteSelezionate
     *            distinte da esportare
     * @throws Exception
     *             ecc generica
     */
    void esporta(List<DistintaCarico> distinteSelezionate) throws Exception;
}
