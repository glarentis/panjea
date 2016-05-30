package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaCancellaManager;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaEffettiContabilitaManager;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 *
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaEffettiContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaEffettiContabilitaManager")
public class AreaEffettiContabilitaManagerBean extends AbstractAreaTesoreriaContabilitaManagerBean implements
AreaEffettiContabilitaManager {

	private static Logger logger = Logger.getLogger(AreaEffettiContabilitaManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	private AreaContabileCancellaManager areaContabileCancellaManager;

	@EJB
	private PianoContiManager pianoContiManager;

	@EJB
	private AreaIvaCancellaManager areaIvaCancellaManager;

	@EJB
	private AreaIvaManager areaIvaManager;

	@EJB
	private AreaRateManager areaRateManager;

	@EJB
	private ContabilitaSettingsManager contabilitaSettingsManager;

	/**
	 * crea e restituisce un {@link AreaContabile} per {@link AreaEffetti} passata come argomento.
	 *
	 * @param areaContabile
	 *            area contabile
	 * @param areaEffetti
	 *            area effetti
	 * @param tipoAreaContabile
	 *            tipo area contabile
	 * @return area contabile
	 */
	private AreaContabile aggiornaAreaContabile(AreaContabile areaContabile, AreaEffetti areaEffetti,
			TipoAreaContabile tipoAreaContabile) {
		logger.debug("--> Enter creaAreaContabile");
		areaContabile.setDocumento(areaEffetti.getDocumento());
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
	public void creaAreaContabileEffetti(AreaEffetti areaEffetti,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) {
		TipoAreaContabile tipoAreaContabile = null;

		// Controllo se ho un area contabile. In tal caso la cancello e recupero i dati che devo tenere non posso usare
		// il cancella area contabile con il parametro cancella aree collegate altrimenti mi verrebbe cancellata anche
		// l'area effetti
		ContabilitaSettings contabilitaSetting = contabilitaSettingsManager.caricaContabilitaSettings();
		AreaContabile areaContabileOld = areaContabileManager.caricaAreaContabileByDocumento(areaEffetti.getDocumento()
				.getId());
		AreaContabile areaContabile = new AreaContabile();
		if (parametriCreazioneAreaChiusure != null) {
			areaContabile.setNote(parametriCreazioneAreaChiusure.getNoteContabili());
		} else {
			areaContabile.setNote(areaContabileOld.getNote());
		}
		try {
			if (areaContabileOld != null) {
				AreaIva areaIva = areaIvaManager.caricaAreaIvaByDocumento(areaContabileOld.getDocumento());
				if (areaIva != null && areaIva.getId() != null) {
					areaIvaCancellaManager.cancellaAreaIva(areaIva);
				}
				areaRateManager.cancellaAreaRate(areaContabileOld.getDocumento());
				areaContabileCancellaManager.cancellaAreaContabile(areaContabileOld, false);
				areaContabile.setCodice(areaContabileOld.getCodice());
				tipoAreaContabile = areaContabileOld.getTipoAreaContabile();
			} else {
				tipoAreaContabile = getTipoAreaContabileByTipoDocumento(areaEffetti.getDocumento().getTipoDocumento());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// non faccio niente se non esiste il tipo area contabile per il tipo documento
		if (tipoAreaContabile == null) {
			return;
		}

		areaContabile = aggiornaAreaContabile(areaContabile, areaEffetti, tipoAreaContabile);

		SottoConto contoEffetti = null;
		if (areaContabile.getDocumento().getRapportoBancarioAzienda() != null) {
			try {
				if (contabilitaSetting.isUsaContoEffettiAttivi()) {
					contoEffetti = pianoContiManager.caricaContoEffettiAttiviPerRapportoBancario(areaContabile
							.getDocumento().getRapportoBancarioAzienda());
					if (contoEffetti == null) {
						contoEffetti = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.EFFETTI_ATTIVI);
					}
				} else {
					contoEffetti = pianoContiManager.caricaContoPerRapportoBancario(areaContabile.getDocumento()
							.getRapportoBancarioAzienda());
				}
			} catch (ContabilitaException e) {
				logger.error("--> errore ContabilitaException in creaAreaContabilePagamentoDistinta", e);
				throw new RuntimeException(e);
			} catch (ContiBaseException e) {
				logger.error("--> errore ContiBaseException in creaAreaContabilePagamentoDistinta", e);
				throw new RuntimeException(e);
			} catch (ContoRapportoBancarioAssenteException e) {
				logger.error("--> errore contoRapportoBancarioAssente", e);
				throw new RuntimeException(e);
			}

			// carica la lista di Pagamenti per l'area partita corrente per la
			// generazione delle RigheContabili
			BigDecimal totaleConto = BigDecimal.ZERO;
			boolean rigaContabileInDare = false;

			// se non aggiorno areaEffetti mi ritrovo gli effetti null
			panjeaDAO.getEntityManager().refresh(areaEffetti);
			for (Effetto effetto : areaEffetti.getEffetti()) {

				// se non aggiorno effetto mi ritrovo i pagamenti vuoti
				panjeaDAO.getEntityManager().refresh(effetto);

				for (Pagamento pagamento : effetto.getPagamenti()) {
					// scrittura in dare se la rata partita corrente è passiva , in
					// avere se attiva
					rigaContabileInDare = TipoPartita.PASSIVA.equals(pagamento.getRata().getAreaRate()
							.getTipoAreaPartita().getTipoPartita());
					// recupera il sottoconto dell'entita
					EntitaLite entita = pagamento.getRata().getAreaRate().getDocumento().getEntita();
					SottoConto sottoConto;
					try {
						sottoConto = pianoContiManager.caricaSottoContoPerEntita(entita);
					} catch (ContabilitaException e) {
						logger.error("--> errore ContabilitaException in creaAreaContabilePagamentoDistinta", e);
						throw new RuntimeException(e);
					}

					creaRigaContabile(areaContabile, sottoConto, pagamento.getImporto().getImportoInValutaAzienda(),
							rigaContabileInDare);
					// totalizza importo di pagamento
					totaleConto = totaleConto.add(pagamento.getImporto().getImportoInValutaAzienda());
				}
			}

			creaRigaContabile(areaContabile, contoEffetti, totaleConto, !rigaContabileInDare);

			// spese incasso
			creaRigheContabiliSpeseIncasso(areaContabile, areaEffetti.getSpeseIncasso());
		}
	}

	/**
	 * crea una riga contabile.
	 *
	 * @param areaContabile
	 *            area contabile
	 * @param sottoConto
	 *            sottoconto della riga
	 * @param importo
	 *            importo delal riga
	 * @param rigaContabileInDare
	 *            <code>true</code> se il conto della riga deve essere in dare
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
	 * Crea le righe contabili per le spese di incasso.
	 *
	 * @param areaContabile
	 *            area contabile di riferimento
	 * @param speseIncasso
	 *            spese di incasso
	 */
	private void creaRigheContabiliSpeseIncasso(AreaContabile areaContabile, BigDecimal speseIncasso) {

		if (speseIncasso != null && speseIncasso.compareTo(BigDecimal.ZERO) != 0) {

			// Dare = conto base spese di incasso x l'importo delle spese
			SottoConto contoSpese = null;
			try {
				contoSpese = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.SPESE_INCASSO);
			} catch (Exception e) {
				logger.error("--> errore durante il caricamento del conto base per spese di incasso", e);
				throw new RuntimeException("errore durante il caricamento del conto base per spese di incasso", e);
			}
			creaRigaContabile(areaContabile, contoSpese, speseIncasso, true);

			// Avere = conto del rapporto bancario o conto cassa del TAC
			contoSpese = null;
			if (areaContabile.getTipoAreaContabile().getTipoDocumento().getTipoEntita() == TipoEntita.BANCA) {
				try {
					contoSpese = pianoContiManager.caricaContoPerRapportoBancario(areaContabile.getDocumento()
							.getRapportoBancarioAzienda());
				} catch (ContoRapportoBancarioAssenteException e) {
					logger.error("--> Non esiste nessun conto per il rapporto bancario.", e);
					throw new RuntimeException("Non esiste nessun conto per il rapporto bancario.", e);
				}
			} else {
				contoSpese = areaContabile.getTipoAreaContabile().getContoCassa();
			}
			creaRigaContabile(areaContabile, contoSpese, speseIncasso, false);
		}
	}

}
