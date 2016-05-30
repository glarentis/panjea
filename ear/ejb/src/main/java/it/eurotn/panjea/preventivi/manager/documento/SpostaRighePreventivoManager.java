package it.eurotn.panjea.preventivi.manager.documento;

import it.eurotn.panjea.service.interfaces.PanjeaDAO;

public class SpostaRighePreventivoManager extends SpostaRigheDocumentoManager {

	/**
	 * @param panjeaDAO
	 *            panjeaDAO
	 */
	public SpostaRighePreventivoManager(final PanjeaDAO panjeaDAO) {
		super(panjeaDAO, "RigaPreventivo", "areaPreventivo", "prev_righe_preventivo", "areaPreventivo_id");
	}
}
