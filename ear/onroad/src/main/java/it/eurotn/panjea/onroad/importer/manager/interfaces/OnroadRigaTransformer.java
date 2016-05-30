package it.eurotn.panjea.onroad.importer.manager.interfaces;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.onroad.domain.RigaDocumentoOnroad;

import javax.ejb.Local;

@Local
public interface OnroadRigaTransformer {

	RigaMagazzino trasforma(RigaDocumentoOnroad rigaDocumentoOnroad, AreaMagazzino areaMagazzino, AgenteLite agente)
			throws QtaLottiMaggioreException, RimanenzaLottiNonValidaException;
}
