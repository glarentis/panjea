package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
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
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.tesoreria.domain.AreaDistintaBancaria;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaDistintaBancariaContabilitaManager;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.AreaDistintaBancariaContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaDistintaBancariaContabilitaManager")
public class AreaDistintaBancariaContabilitaManagerBean extends AbstractAreaTesoreriaContabilitaManagerBean implements
		AreaDistintaBancariaContabilitaManager {

	private static Logger logger = Logger.getLogger(AreaDistintaBancariaContabilitaManagerBean.class);

	@EJB
	private PianoContiManager pianoContiManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	/**
	 * crea e restituisce un {@link AreaContabile} per {@link AreaDistintaBancaria} passata come argomento.
	 * 
	 * @param areaContabile
	 *            area contabile
	 * @param areaDistintaBancaria
	 *            area distinta bancaria
	 * @param tipoAreaContabile
	 *            tipo area contabile
	 * @return area contabile
	 */
	private AreaContabile aggiornaAreaContabile(AreaContabile areaContabile, AreaDistintaBancaria areaDistintaBancaria,
			TipoAreaContabile tipoAreaContabile) {
		logger.debug("--> Enter creaAreaContabile");
		areaContabile.setDocumento(areaDistintaBancaria.getDocumento());
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
	public void creaAreaContabileDistintaBancaria(AreaDistintaBancaria areaDistintaBancaria)
			throws ContoRapportoBancarioAssenteException {
		logger.debug("--> Enter confermaAreaContabileDistintaBancaria");
		if (!(TipoAreaPartita.TipoOperazione.GESTIONE_DISTINTA.equals(areaDistintaBancaria.getTipoAreaPartita()
				.getTipoOperazione()))) {
			logger.debug("--> Exit confermaAreaContabileDistintaBancaria : tipo operazione diversa da GESTIONE_DISTINTA ");
			return;
		}

		BigDecimal totaleSpese = areaDistintaBancaria.getSpeseIncasso();

		if (totaleSpese.equals(BigDecimal.ZERO)) {
			return;
		}
		TipoAreaContabile tipoAreaContabile = getTipoAreaContabileByTipoDocumento(areaDistintaBancaria.getDocumento()
				.getTipoDocumento());

		// non faccio niente se non esiste il tipo area contabile per il tipo documento
		if (tipoAreaContabile == null) {
			return;
		}

		// carica conto effetti attivi
		SottoConto sottoContoSpesePresentazioneEffetti;
		try {
			sottoContoSpesePresentazioneEffetti = pianoContiManager
					.caricaContoPerTipoContoBase(ETipoContoBase.SPESE_PRESENTAZIONE_EFFETTI);
		} catch (Exception e) {
			logger.error("--> errore ContabilitaException in confermaAreaContabileDistintaBancaria", e);
			throw new RuntimeException(e);
		}

		// Rapporto regolamentazione
		SottoConto sottoContoSpeseRapportoBancario = null;

		if (areaDistintaBancaria.getDocumento().getRapportoBancarioAzienda().getRapportoBancarioRegolamentazione() != null) {
			sottoContoSpeseRapportoBancario = pianoContiManager.caricaContoPerRapportoBancario(areaDistintaBancaria
					.getDocumento().getRapportoBancarioAzienda().getRapportoBancarioRegolamentazione());
		}

		if (sottoContoSpeseRapportoBancario == null) {
			// Rapporto ordinario
			sottoContoSpeseRapportoBancario = pianoContiManager.caricaContoPerRapportoBancario(areaDistintaBancaria
					.getDocumento().getRapportoBancarioAzienda());
		}

		// carica AreaEffetti collegata
		if (areaDistintaBancaria.getAreaEffettiCollegata() == null) {
			logger.error("--> errore in confermaAreaContabileDistintaBancaria: impossibile caricara l'area effetti collegata ");
			throw new RuntimeException("Area effetti collegata assente");
		}

		AreaContabile areaContabile = new AreaContabile();
		areaContabile = aggiornaAreaContabile(areaContabile, areaDistintaBancaria, tipoAreaContabile);

		// creazione riga contabile per effetti attivi
		creaRigaContabile(areaContabile, sottoContoSpesePresentazioneEffetti, totaleSpese, true);
		// creazione riga contabile per conto "salvo buon fine"
		creaRigaContabile(areaContabile, sottoContoSpeseRapportoBancario, totaleSpese, false);
		// aggiorna il documento di area partite
		areaDistintaBancaria.setDocumento(areaContabile.getDocumento());
		logger.debug("--> Exit confermaAreaContabileDistintaBancaria");
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
}
