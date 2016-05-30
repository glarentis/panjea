package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.RapportoBancarioRegolamentazioneAssenteException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.domain.AreaInsoluti;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaInsolutoContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaInsolutoManager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "AreaInsolutoContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaInsolutoContabilitaManager")
public class AreaInsolutoContabilitaManagerBean extends AbstractAreaTesoreriaContabilitaManagerBean implements
		AreaInsolutoContabilitaManager {

	private static Logger logger = Logger.getLogger(AreaInsolutoContabilitaManagerBean.class);

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	private PianoContiManager pianoContiManager;

	/**
	 * @uml.property name="areaInsolutoManager"
	 * @uml.associationEnd
	 */
	@EJB
	@IgnoreDependency
	protected AreaInsolutoManager areaInsolutoManager;

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaDAO panjeaDAO;

	/**
	 * crea e restituisce un {@link AreaContabile} per {@link AreaDistintaBancaria} passata come argomento.
	 * 
	 * @param areaContabile
	 *            area contabile
	 * @param areaInsoluti
	 *            area insoluti
	 * @param tipoAreaContabile
	 *            tipo area contabile
	 * @return area contabile
	 */
	private AreaContabile aggiornaAreaContabile(AreaContabile areaContabile, AreaInsoluti areaInsoluti,
			TipoAreaContabile tipoAreaContabile) {
		logger.debug("--> Enter creaAreaContabile");
		areaContabile.setDocumento(areaInsoluti.getDocumento());
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
	public void creaAreaContabileInsoluto(AreaInsoluti areaInsoluti, Set<Effetto> effetti) {

		TipoAreaContabile tipoAreaContabile = getTipoAreaContabileByTipoDocumento(areaInsoluti.getDocumento()
				.getTipoDocumento());

		// non faccio niente se non esiste il tipo area contabile per il tipo documento
		if (tipoAreaContabile == null) {
			return;
		}

		// carico il sottoconto delle spese insoluto
		SottoConto sottoContoSpeseInsoluto = getSottoContoSpeseInsoluto();

		// carica conto del rapporto bancario di regolamentazione
		SottoConto sottoContoRegolamentazione = getSottoContoRegolamentazione(areaInsoluti.getDocumento()
				.getRapportoBancarioAzienda().getRapportoBancarioRegolamentazione());

		AreaContabile areaContabile = new AreaContabile();
		areaContabile = aggiornaAreaContabile(areaContabile, areaInsoluti, tipoAreaContabile);

		// creare la prima riga sul cliente?
		for (Effetto effetto : effetti) {
			BigDecimal totaleEffetto = effetto.getImporto().getImportoInValutaAzienda();
			SottoConto sottoContoEntita = getSottoContoEntita(effetto.getPagamenti().iterator().next().getRata()
					.getAreaRate().getDocumento().getEntita());
			creaRigaContabile(areaContabile, sottoContoEntita, totaleEffetto, true, effetto.getPagamenti());
		}

		// riga spese aggiunta solo se le spese sono diverse da 0
		BigDecimal totaleSpese = areaInsoluti.getSpeseIncasso();
		if (totaleSpese.compareTo(BigDecimal.ZERO) != 0) {
			creaRigaContabile(areaContabile, sottoContoSpeseInsoluto, totaleSpese, true, null);
		}

		// riga conto regolamentazione
		BigDecimal totaleAreaContabile = areaInsoluti.getDocumento().getTotale().getImportoInValutaAzienda();
		creaRigaContabile(areaContabile, sottoContoRegolamentazione, totaleAreaContabile, false, null);
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
	 * @param pagamenti
	 *            i pagamenti associati alla riga contabile
	 */
	private void creaRigaContabile(AreaContabile areaContabile, SottoConto sottoConto, BigDecimal importo,
			boolean rigaContabileInDare, Set<Pagamento> pagamenti) {
		logger.debug("--> Enter creaRigaContabile");

		if (BigDecimal.ZERO.compareTo(importo) > 0) {
			importo = importo.negate();
			rigaContabileInDare = !rigaContabileInDare;
		}

		RigaContabile rigaContabile = RigaContabile.creaRigaContabile(areaContabile, sottoConto, rigaContabileInDare,
				importo, null, false);
		rigaContabile.setPagamenti(pagamenti);

		areaContabileManager.salvaRigaContabileNoCheck(rigaContabile);
		logger.debug("--> Exit creaRigaContabile");
	}

	/**
	 * @param entita
	 *            entità
	 * @return sotto conto per l'entità
	 */
	private SottoConto getSottoContoEntita(EntitaLite entita) {
		SottoConto sottoContoEntita = null;
		try {
			sottoContoEntita = pianoContiManager.caricaSottoContoPerEntita(entita);
		} catch (Exception e) {
			logger.error("-->errore durante il caricamento del sottoconto per l'entita con id " + entita.getId(), e);
			throw new RuntimeException("errore durante il caricamento del sottoconto per l'entita con id "
					+ entita.getId(), e);
		}

		return sottoContoEntita;
	}

	/**
	 * Restituisce il sottoconto di un rapporto bancario di regolamentazione.
	 * 
	 * @param rapportoBancario
	 *            rapporto bancario
	 * @return sottoconto
	 */
	private SottoConto getSottoContoRegolamentazione(RapportoBancarioAzienda rapportoBancario) {
		if (rapportoBancario == null) {
			logger.error("--> Errore in creaAreaContabileAccredito: rapportoBancario di regolamentazione assente ");
			context.setRollbackOnly();
			RapportoBancarioRegolamentazioneAssenteException e = new RapportoBancarioRegolamentazioneAssenteException();
			throw new RuntimeException(
					"Errore in creaAreaContabileAccredito: rapportoBancario di regolamentazione assente ", e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> trovato rapporto bancario di regolamentazione " + rapportoBancario);
		}
		SottoConto sottoContoRegolamentazione;
		try {
			sottoContoRegolamentazione = pianoContiManager.caricaContoPerRapportoBancario(rapportoBancario);
		} catch (Exception e1) {
			logger.error("--> Errore durante il caricamento del conto del rapporto bancario.", e1);
			throw new RuntimeException("Errore durante il caricamento del conto del rapporto bancario.", e1);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> trovato conto regolamentazione " + sottoContoRegolamentazione);
		}
		return sottoContoRegolamentazione;
	}

	/**
	 * Restituisce il sottoconto delle spese insoluto definito nei conti base.
	 * 
	 * @return sottoconto
	 */
	private SottoConto getSottoContoSpeseInsoluto() {

		SottoConto sottoContoSpeseInsoluto = null;
		try {
			sottoContoSpeseInsoluto = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.SPESE_INSOLUTO);
		} catch (ContiBaseException e2) {
			context.setRollbackOnly();
			logger.error("--> Nessun conto spese insoluto definito.", e2);
			throw new RuntimeException("Nessun conto spese insoluto definito.", e2);
		} catch (Exception e2) {
			context.setRollbackOnly();
			logger.error("--> errore durante il caricamento del conto spese insoluto.", e2);
			throw new RuntimeException("errore durante il caricamento del conto spese insoluto.", e2);
		}

		return sottoContoSpeseInsoluto;
	}

}
