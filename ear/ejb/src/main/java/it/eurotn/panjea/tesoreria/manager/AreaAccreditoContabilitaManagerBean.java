package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.service.exception.RapportoBancarioRegolamentazioneAssenteException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.tesoreria.domain.AreaAccredito;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccreditoContabilitaManager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.AreaAccreditoContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAccreditoContabilitaManager")
public class AreaAccreditoContabilitaManagerBean extends AbstractAreaTesoreriaContabilitaManagerBean implements
		AreaAccreditoContabilitaManager {

	private static Logger logger = Logger.getLogger(AreaAccreditoContabilitaManagerBean.class);

	@EJB
	private PianoContiManager pianoContiManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	private ContabilitaSettingsManager contabilitaSettingsManager;

	/**
	 * crea e restituisce un {@link AreaContabile} per {@link AreaDistintaBancaria} passata come argomento.
	 *
	 * @param areaContabile
	 *            area contabile
	 * @param areaAccredito
	 *            area accredito
	 * @param tipoAreaContabile
	 *            tipo area contabile
	 * @return area contabile
	 */
	private AreaContabile aggiornaAreaContabile(AreaContabile areaContabile, AreaAccredito areaAccredito,
			TipoAreaContabile tipoAreaContabile) {
		logger.debug("--> Enter creaAreaContabile");
		areaContabile.setDocumento(areaAccredito.getDocumento());
		Calendar calendarDataDocumento = Calendar.getInstance();
		calendarDataDocumento.setTime(areaContabile.getDocumento().getDataDocumento());

		// HACK data registrazione = data documento
		areaContabile.setDataRegistrazione(calendarDataDocumento.getTime());
		// HACK anno movimento da data documento
		areaContabile.setAnnoMovimento(calendarDataDocumento.get(Calendar.YEAR));

		areaContabile.setTipoAreaContabile(tipoAreaContabile);
		// inizializzazione dello stato di areaContabile dal valore presente in
		// tipoAreaContabile
		areaContabile.setStatoAreaContabile(tipoAreaContabile.getStatoAreaContabileGenerata());

		areaContabile.setValidRigheContabili(false);
		if (areaContabile.getStatoAreaContabile() == StatoAreaContabile.CONFERMATO) {
			areaContabile.setValidRigheContabili(true);
			areaContabile.setValidDataRigheContabili(Calendar.getInstance().getTime());
			areaContabile.setValidUtenteRigheContabili(getPrincipal().getUserName());
		}
		try {
			areaContabile = areaContabileManager.salvaAreaContabile(areaContabile, true);
		} catch (ContabilitaException e) {
			logger.error("--> errore ContabilitaException in creaAreaContabile", e);
			throw new RuntimeException(e);
		} catch (AreaContabileDuplicateException e) {
			logger.error("--> errore AreaContabileDuplicateException in creaAreaContabile", e);
			throw new RuntimeException(e);
		} catch (DocumentoDuplicateException e) {
			logger.error("--> errore DocumentoDuplicateException in creaAreaContabile", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit creaAreaContabile");
		return areaContabile;

	}

	@Override
	public void creaAreaContabileAccredito(AreaAccredito areaAccredito, List<Effetto> effetti,
			boolean scritturaPosticipata) {

		TipoAreaContabile tipoAreaContabile = getTipoAreaContabileByTipoDocumento(areaAccredito.getDocumento()
				.getTipoDocumento());

		// non faccio niente se non esiste il tipo area contabile per il tipo documento
		if (tipoAreaContabile == null) {
			return;
		}

		AreaContabile areaContabile = new AreaContabile();
		areaContabile = aggiornaAreaContabile(areaContabile, areaAccredito, tipoAreaContabile);

		BigDecimal totaleConto = areaContabile.getDocumento().getTotale().getImportoInValutaAzienda();

		if (!scritturaPosticipata) {
			// creazione riga contabile per effetti attivi
			creaRigaContabile(areaContabile, getSottoContoEffettiAttivi(areaAccredito), totaleConto, false);
		} else {

			// creazione scrittura posticipata
			for (Effetto effetto : effetti) {
				try {
					SottoConto sottoContoEntita = pianoContiManager.caricaSottoContoPerEntita(effetto.getEntita());

					creaRigaContabile(areaContabile, sottoContoEntita,
							effetto.getImporto().getImportoInValutaAzienda(), false);
				} catch (ContabilitaException e) {
					logger.error("--> errore durante il caricamento del sottoconto dell'entita"
							+ effetto.getEntita().getId(), e);
					throw new RuntimeException("errore durante il caricamento del sottoconto dell'entita"
							+ effetto.getEntita().getId(), e);
				}
			}
		}

		// creazione riga contabile per conto "salvo buon fine"
		creaRigaContabile(areaContabile, getSottoContoRegolamentazione(areaAccredito), totaleConto, true);

		// aggiorna il documento di area partite
		areaAccredito.setDocumento(areaContabile.getDocumento());
	}

	/**
	 * crea una riga contabile.
	 *
	 * @param areaContabile
	 *            area contabile da associare alla riga
	 * @param sottoConto
	 *            sotto conto della riga
	 * @param importo
	 *            importo
	 * @param rigaContabileInDare
	 *            <code>true</code> se il conto è in dare, <code>false</code> altrimenti
	 */
	private void creaRigaContabile(AreaContabile areaContabile, SottoConto sottoConto, BigDecimal importo,
			boolean rigaContabileInDare) {
		logger.debug("--> Enter creaRigaContabile");

		if (BigDecimal.ZERO.compareTo(importo) > 0) {
			importo = importo.negate();
			rigaContabileInDare = !rigaContabileInDare;
		}

		RigaContabile rigaContabile = RigaContabile.creaRigaContabile(areaContabile, sottoConto, rigaContabileInDare,
				importo, null, false);

		areaContabileManager.salvaRigaContabileNoCheck(rigaContabile);
		logger.debug("--> Exit creaRigaContabile");
	}

	/**
	 * Restituisce il sottoconto effetti attivi.
	 *
	 * @param areaAccredito
	 *            area accredito di riferimento
	 * @return sottoconto trovato
	 */
	private SottoConto getSottoContoEffettiAttivi(AreaAccredito areaAccredito) {

		SottoConto sottoContoEffettiAttivi;
		ContabilitaSettings contabilitaSetting = contabilitaSettingsManager.caricaContabilitaSettings();
		try {
			if (contabilitaSetting.isUsaContoEffettiAttivi()) {
				sottoContoEffettiAttivi = pianoContiManager.caricaContoEffettiAttiviPerRapportoBancario(areaAccredito
						.getDocumento().getRapportoBancarioAzienda());
				if (sottoContoEffettiAttivi == null) {
					sottoContoEffettiAttivi = pianoContiManager
							.caricaContoPerTipoContoBase(ETipoContoBase.EFFETTI_ATTIVI);
				}
			} else {
				sottoContoEffettiAttivi = pianoContiManager.caricaContoPerRapportoBancario(areaAccredito.getDocumento()
						.getRapportoBancarioAzienda());
			}
		} catch (Exception e) {
			logger.error("--> errore ContabilitaException in confermaAreaContabileDistintaBancaria", e);
			throw new RuntimeException(e);
		}
		return sottoContoEffettiAttivi;
	}

	/**
	 * Restituisce il sottoconto del rapporto bancario di regolamentazione.
	 *
	 * @param areaAccredito
	 *            area arradito
	 * @return sottoconto trovato
	 */
	private SottoConto getSottoContoRegolamentazione(AreaAccredito areaAccredito) {

		// carica conto del rapporto bancario di regolamentazione
		RapportoBancarioAzienda rapportoBancarioRegolamentazione = areaAccredito.getDocumento()
				.getRapportoBancarioAzienda().getRapportoBancarioRegolamentazione();
		if (rapportoBancarioRegolamentazione == null) {
			logger.error("--> Errore in creaAreaContabileAccredito: rapportoBancario di regolamentazione assente ");
			context.setRollbackOnly();
			RapportoBancarioRegolamentazioneAssenteException e = new RapportoBancarioRegolamentazioneAssenteException();
			throw new RuntimeException(
					"Errore in creaAreaContabileAccredito: rapportoBancario di regolamentazione assente ", e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> trovato rapporto bancario di regolamentazione " + rapportoBancarioRegolamentazione);
		}

		SottoConto sottoContoRegolamentazione;
		try {
			sottoContoRegolamentazione = pianoContiManager
					.caricaContoPerRapportoBancario(rapportoBancarioRegolamentazione);
		} catch (Exception e1) {
			logger.error("--> Errore durante il caricamento del conto del rapporto bancario.", e1);
			throw new RuntimeException("Errore durante il caricamento del conto del rapporto bancario.", e1);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> trovato conto regolamentazione " + sottoContoRegolamentazione);
		}

		return sottoContoRegolamentazione;
	}

	@Override
	public boolean isDopoIncasso(Effetto effetto) {
		// Controllo se l'area effetti degli effetti ha un'area contabile. Se non c'è devo chiudere ogni cliente perchè
		// non è stato fatto, altrimenti uso il conto effetti attivi
		TipoAreaContabile tipoAreaContabileEffetti = getTipoAreaContabileByTipoDocumento(effetto.getAreaEffetti()
				.getTipoAreaPartita().getTipoDocumento());
		return tipoAreaContabileEffetti == null;
	}

}
