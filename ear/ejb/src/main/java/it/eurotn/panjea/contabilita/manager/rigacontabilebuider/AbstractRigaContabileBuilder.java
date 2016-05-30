package it.eurotn.panjea.contabilita.manager.rigacontabilebuider;

import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.FormuleManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractRigaContabileBuilder implements IRigaContabileBuilder {

	protected PianoContiManager pianoContiManager;
	protected FormuleManager formuleManager;
	protected AreaContabileManager areaContabileManager;
	protected AziendeManager aziendeManager;

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
	public AbstractRigaContabileBuilder(final PianoContiManager pianoContiManager, final FormuleManager formuleManager,
			final AreaContabileManager areaContabileManager, final AziendeManager aziendeManager) {
		super();
		this.pianoContiManager = pianoContiManager;
		this.formuleManager = formuleManager;
		this.areaContabileManager = areaContabileManager;
		this.aziendeManager = aziendeManager;
	}

	@Override
	public List<RigaContabile> creaRigheContabili(StrutturaContabile strutturaContabile, AreaContabile areaContabile,
			Map<String, BigDecimal> mapSC, long ordinamentoRiga, List<ControPartita> controPartite,
			boolean rigaAutomatica) throws FormulaException, ContabilitaException, ContoEntitaAssenteException,
			ContoRapportoBancarioAssenteException {

		List<RigaContabile> righeResult = new ArrayList<RigaContabile>();

		boolean isDare = false;
		SottoConto sottoConto = null;
		if (strutturaContabile.getAvere() != null && strutturaContabile.getAvere().length() > 0) {
			sottoConto = getSottoConto(areaContabile, strutturaContabile.getAvere());
			isDare = false;
		}
		if (strutturaContabile.getDare() != null && strutturaContabile.getDare().length() > 0) {
			sottoConto = getSottoConto(areaContabile, strutturaContabile.getDare());
			isDare = true;
		}

		// se l'importo della riga è negativo o se il tipo documento è uno
		// storno devo girare il dare in avere e
		// viceversa. Viene invertito solo se una delle 2 condizioni è vera, per
		// questo uso una xor.
		BigDecimal importoRiga = formuleManager.calcola(strutturaContabile.getFormula(), mapSC, 2);
		boolean importoNegativo = (BigDecimal.ZERO.compareTo(importoRiga) > 0);
		boolean storno = areaContabile.getDocumento().getTipoDocumento().isNotaCreditoEnable();
		if (importoNegativo ^ storno) {
			isDare = !isDare;
		}

		RigaContabile rigaContabile = RigaContabile.creaRigaContabile(areaContabile, sottoConto, isDare,
				importoRiga.abs(), null, ordinamentoRiga + 1, rigaAutomatica);
		// se la riga contabile ha importo 0 non la salvo
		if (rigaContabile.getImporto() != null && rigaContabile.getImporto().compareTo(BigDecimal.ZERO) != 0) {
			righeResult.add(areaContabileManager.salvaRigaContabile(rigaContabile));
		}

		return righeResult;
	}

	/**
	 * Restituisce il sottoconto da utilizzare per la creazione della riga contabile.
	 *
	 * @param areaContabile
	 *            area contabile
	 * @param codiceTipologiaConto
	 *            codice della tipologia del conto definito
	 * @return {@link SottoConto}
	 * @throws ContabilitaException
	 *             eccezione generale
	 * @throws ContoEntitaAssenteException
	 *             sollevata se per l'entità non esiste un sottoconto
	 * @throws ContoRapportoBancarioAssenteException
	 *             ContoRapportoBancarioAssenteException
	 */
	public abstract SottoConto getSottoConto(AreaContabile areaContabile, String codiceTipologiaConto)
			throws ContabilitaException, ContoEntitaAssenteException, ContoRapportoBancarioAssenteException;

}
