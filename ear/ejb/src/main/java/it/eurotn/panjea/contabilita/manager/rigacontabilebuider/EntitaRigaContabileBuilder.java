package it.eurotn.panjea.contabilita.manager.rigacontabilebuider;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.FormuleManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;

import org.apache.log4j.Logger;

public class EntitaRigaContabileBuilder extends AbstractRigaContabileBuilder {

	private static Logger logger = Logger.getLogger(EntitaRigaContabileBuilder.class);

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
	public EntitaRigaContabileBuilder(final PianoContiManager pianoContiManager, final FormuleManager formuleManager,
			final AreaContabileManager areaContabileManager, final AziendeManager aziendeManager) {
		super(pianoContiManager, formuleManager, areaContabileManager, aziendeManager);
	}

	/**
	 * In base all'entita' contenuta nel documento dell'area contabile carica il sottoconto.
	 * 
	 * @param areaContabile
	 *            area contabile di cui verificare l'entita
	 * @return SottoConto
	 * @throws ContabilitaException
	 *             exception generico di contabilita
	 * @throws ContoRapportoBancarioAssenteException
	 *             il conto per il rapporto bancario
	 * @throws ContoEntitaAssenteException
	 *             conto entita assente
	 */
	public SottoConto caricaSottoContoperEntita(AreaContabile areaContabile) throws ContabilitaException,
			ContoRapportoBancarioAssenteException, ContoEntitaAssenteException {
		logger.debug("--> Enter caricaSottoContoperEntita");
		SottoConto sottoConto = null;

		RapportoBancarioAzienda rapportoBancarioAzienda = areaContabile.getDocumento().getRapportoBancarioAzienda();
		EntitaLite entitaLite = areaContabile.getDocumento().getEntita();

		TipoEntita tipoEntita = areaContabile.getDocumento().getTipoDocumento().getTipoEntita();
		if (tipoEntita.equals(TipoEntita.CLIENTE)) {
			if (entitaLite != null && entitaLite instanceof ClienteLite) {
				sottoConto = pianoContiManager
						.caricaSottoContoPerEntita(SottotipoConto.CLIENTE, entitaLite.getCodice());
				if (sottoConto.isNew()) {
					logger.error("-->errore, non è stato trovato il conto dell'entità " + entitaLite.getId());
					throw new ContoEntitaAssenteException(entitaLite);
				}
			}
		} else if (tipoEntita.equals(TipoEntita.FORNITORE)) {
			if (entitaLite != null && entitaLite instanceof FornitoreLite) {
				sottoConto = pianoContiManager.caricaSottoContoPerEntita(SottotipoConto.FORNITORE,
						entitaLite.getCodice());
				if (sottoConto.isNew()) {
					logger.error("-->errore, non è stato trovato il conto dell'entità " + entitaLite.getId());
					throw new ContoEntitaAssenteException(entitaLite);
				}
			}
		} else if (tipoEntita.equals(TipoEntita.VETTORE)) {
			// non faccio nulla ?
			logger.debug("Tipo entita VETTORE, caricaSottoContoperEntita non faccio nulla");
		} else if (tipoEntita.equals(TipoEntita.AZIENDA)) {
			// non faccio nulla
			logger.debug("Tipo entita AZIENDA, caricaSottoContoperEntita non faccio nulla");
		} else if (tipoEntita.equals(TipoEntita.BANCA)) {
			// devo caricare il conto del rapporto bancario
			rapportoBancarioAzienda = aziendeManager.caricaRapportoBancario(rapportoBancarioAzienda.getId(), true);
			sottoConto = rapportoBancarioAzienda.getSottoConto();
			if (sottoConto == null) {
				logger.warn("--> Manca il conto per l'entita' rapporto bancario scelta del documento");
				throw new ContoRapportoBancarioAssenteException();
			}
		}
		logger.debug("--> Exit caricaSottoContoperEntita");
		return sottoConto;
	}

	@Override
	public SottoConto getSottoConto(AreaContabile areaContabile, String codiceTipologiaConto)
			throws ContabilitaException, ContoEntitaAssenteException, ContoRapportoBancarioAssenteException {
		return caricaSottoContoperEntita(areaContabile);
	}

}
