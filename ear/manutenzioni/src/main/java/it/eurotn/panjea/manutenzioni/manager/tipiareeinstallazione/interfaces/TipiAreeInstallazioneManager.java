package it.eurotn.panjea.manutenzioni.manager.tipiareeinstallazione.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.manutenzioni.domain.documento.TipoAreaInstallazione;

@Local
public interface TipiAreeInstallazioneManager extends CrudManager<TipoAreaInstallazione> {
    /**
     *
     * @param idTipoDocumento
     *            id del tipo documento
     * @return tipoAreaInstallazione per il tipo documento. Se non esiste viene creata una nuova
     *         istanza
     */
    TipoAreaInstallazione caricaByTipoDocumento(int idTipoDocumento);
}