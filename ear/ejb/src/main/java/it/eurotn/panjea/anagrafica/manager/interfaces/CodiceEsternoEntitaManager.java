package it.eurotn.panjea.anagrafica.manager.interfaces;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

@Remote
public interface CodiceEsternoEntitaManager {
    /**
     *
     * @return lista di entità con codice esterno da importare.
     */
    List<EntitaLite> caricaEntitaConCodiceEsternoDaConfermare();

    /**
     *
     * @return numero di entita con codice esterno da modificare.
     */
    long caricaNumeroEntitaConCodiceEsternoDaConfermare();

    /**
     *
     * @param codEsterno
     *            codice da cercare
     * @return true se l'enttia con quel codice esterno esiste
     */
    boolean checkEntitaConCodiceEsternoPresente(String codEsterno);

}
