package it.eurotn.panjea.contabilita.manager.rigacontabilebuider;

import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.FormuleManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;

public class ContoBaseRigaContabileBuilder extends AbstractRigaContabileBuilder {

	/**
	 * Costruttore.
	 * 
	 * @param pianoContiManager
	 *            {@link PianoContiManager}
	 * @param formuleManager
	 *            {@link FormuleManager}
	 * @param areaContabileManager
	 *            {@link AreaContabileManager}
	 * @param aziendeManager
	 *            {@link AziendeManager}
	 */
	public ContoBaseRigaContabileBuilder(final PianoContiManager pianoContiManager,
			final FormuleManager formuleManager, final AreaContabileManager areaContabileManager,
			final AziendeManager aziendeManager) {
		super(pianoContiManager, formuleManager, areaContabileManager, aziendeManager);
	}

	@Override
	public SottoConto getSottoConto(AreaContabile areaContabile, String codiceTipologiaConto)
			throws ContabilitaException {
		return pianoContiManager.caricaContoBase(codiceTipologiaConto).getSottoConto();
	}

}
