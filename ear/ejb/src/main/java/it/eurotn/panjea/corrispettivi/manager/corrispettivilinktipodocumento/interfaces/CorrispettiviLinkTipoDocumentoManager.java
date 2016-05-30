package it.eurotn.panjea.corrispettivi.manager.corrispettivilinktipodocumento.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.corrispettivi.domain.CorrispettivoLinkTipoDocumento;
import it.eurotn.panjea.manager.interfaces.CrudManager;

@Local
public interface CorrispettiviLinkTipoDocumentoManager extends CrudManager<CorrispettivoLinkTipoDocumento> {

    /**
     * Carica tutti i tipi documento configurati per essere usati nei corrispettivi.
     *
     * @return tipi documento
     */
    List<TipoDocumento> caricaTipiDocumentoCorrispettivi();
}