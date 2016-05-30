package it.eurotn.panjea.vending.manager.evadts.importazioni.interfaces;

import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.annotations.Loader;

import it.eurotn.panjea.vending.domain.EvaDtsImportFolder.EvaDtsFieldIDContent;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.manager.evadts.exception.ImportazioneEvaDtsException;
import it.eurotn.panjea.vending.manager.evadts.importazioni.ImportazioniQueryType;

@Loader
public interface ImportazioniEvaDtsRifornimentoManager {

    AreaRifornimento getAreaRifornimento(RilevazioneEvaDts rilevazioneEvaDts, EvaDtsFieldIDContent fieldIDContent,
            Map<ImportazioniQueryType, Query> queries, List<AreaRifornimento> rifornimentiGiaAssegnati)
                    throws ImportazioneEvaDtsException;

}
