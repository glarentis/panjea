package it.eurotn.panjea.agenti.manager.interfaces;

import java.util.Set;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;

public interface SediAgenteManager {

    /**
     * Carica la lista di sedi entità associate all'agente, considera il flag di ereditarietà dei dati commerciali.
     * 
     * @param agente
     *            agente
     * @return lista di sedi entità associate all'agente
     */
    Set<SedeEntita> caricaSediAssociate(AgenteLite agente);
}
