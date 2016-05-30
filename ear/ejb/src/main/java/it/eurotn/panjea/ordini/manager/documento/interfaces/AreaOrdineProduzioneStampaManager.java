package it.eurotn.panjea.ordini.manager.documento.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.AreaProduzioneFullDTOStampa;

@Local
public interface AreaOrdineProduzioneStampaManager {

    /**
     * metodo incaricato di eseguire il caricamento di {@link AreaProduzioneFullDTOStampa} .
     *
     * @param righeOrdine
     * @return area ordine produzione full DTO caricata
     */
    AreaProduzioneFullDTOStampa caricaAreaOrdineProduzioneDTOStampa(AreaOrdineFullDTO areaOrdineFullDTO,
            List<RigaOrdine> righeOrdine);
}
