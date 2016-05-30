package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
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
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoContabilitaManager;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAnticipoFattureContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaPagamentiContabilitaManager;
import it.eurotn.panjea.tesoreria.service.exception.ContoCassaAssenteException;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@Stateless(name = "Panjea.AreaPagamentiContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaPagamentiContabilitaManager")
public class AreaPagamentiContabilitaManagerBean extends AbstractAreaTesoreriaContabilitaManagerBean implements
		AreaPagamentiContabilitaManager {

	private static Logger logger = Logger.getLogger(AreaPagamentiContabilitaManagerBean.class);

	@EJB
	private PianoContiManager pianoContiManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	private ContabilitaSettingsManager contabilitaSettingsManager;

	@EJB
	private TipiAreaPartitaManager tipiAreaPartitaManager;

	@EJB
	private RitenutaAccontoContabilitaManager ritenutaAccontoContabilitaManager;

	@EJB
	private AreaAnticipoFattureContabilitaManager areaAnticipoFattureContabilitaManager;

	/**
	 * crea e restituisce un {@link AreaContabile} per {@link AreaPagamenti} passata come argomento.
	 * 
	 * @param areaContabile
	 *            area contabile
	 * @param areaPagamenti
	 *            area pagamenti
	 * @param tipoAreaContabile
	 *            tipo area contabile
	 * @return area contabile
	 */
	private AreaContabile aggiornaAreaContabile(AreaContabile areaContabile, AreaPagamenti areaPagamenti,
			TipoAreaContabile tipoAreaContabile) {
		logger.debug("--> Enter creaAreaContabile");
		areaContabile.setDocumento(areaPagamenti.getDocumento());
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
	public AreaPagamenti creaAreaContabilePagamentoDiretto(AreaPagamenti areaPagamenti,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) throws PagamentiException {
		logger.debug("--> Enter creaAreaContabilePagamentoDiretto");

		// Recupero il tipoAreaContabile per il documento di pagamento
		TipoAreaContabile tipoAreaContabile = getTipoAreaContabileByTipoDocumento(areaPagamenti.getDocumento()
				.getTipoDocumento());

		// non faccio niente se non esiste il tipo area contabile per il tipo documento
		if (tipoAreaContabile == null) {
			return areaPagamenti;
		}

		BigDecimal percentualeAnticipo = BigDecimal.ZERO;
		BigDecimal percentualeIvaAnticipo = BigDecimal.ZERO;
		SottoConto sottoContoAnticipiFatture = null;

		// Recupero il sotto conto cassa
		SottoConto sottoContoCassa = null;
		boolean raggruppamentoContoCassa = false;
		if (isTipoDocumentoBaseAcconto(areaPagamenti.getTipoAreaPartita())) {
			try {
				if (areaPagamenti.getTipoAreaPartita().getTipoPartita() == TipoPartita.ATTIVA) {
					sottoContoCassa = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.ACCONTO_CLIENTE);
				} else {
					sottoContoCassa = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.ACCONTO_FORNITORE);
				}
			} catch (Exception e) {
				logger.error(
						"-->errore durante il caricamento del tipo conto base ACCONTO_CLIENTE o ACCONTO_FORNITORE", e);
				throw new RuntimeException(
						"errore durante il caricamento del tipo conto base ACCONTO_CLIENTE o ACCONTO_FORNITORE", e);
			}
			raggruppamentoContoCassa = true;
		} else {
			// determina il conto cassa da utilizzare: conto cassa tipo area
			// contabile/conto rapporto bancario
			switch (areaPagamenti.getTipoAreaPartita().getTipoDocumento().getTipoEntita()) {
			case AZIENDA:
				sottoContoCassa = tipoAreaContabile.getContoCassa();
				if ((sottoContoCassa == null)) {
					logger.error("--> errore in creaAreaContabilePagamentoDiretto: impossibile determinare il sottoconto cassa ");
					throw new ContoCassaAssenteException(tipoAreaContabile.getTipoDocumento());
				}
				break;
			case BANCA:
				try {
					RapportoBancarioAzienda rapportoBancarioAzienda = areaPagamenti.getDocumento()
							.getRapportoBancarioAzienda();

					sottoContoCassa = pianoContiManager.caricaContoPerRapportoBancario(areaPagamenti.getDocumento()
							.getRapportoBancarioAzienda());

					if (parametriCreazioneAreaChiusure.isAnticipoFattura()) {
						percentualeAnticipo = rapportoBancarioAzienda.getPercAnticippoFatture();
						percentualeIvaAnticipo = rapportoBancarioAzienda.getPercIvaAnticipoFatture();

						sottoContoAnticipiFatture = areaAnticipoFattureContabilitaManager
								.caricaSottoContoAnticipiFatture(rapportoBancarioAzienda);
					}
				} catch (ContoRapportoBancarioAssenteException e1) {
					logger.error(
							"--> errore ContoRapportoBancarioAssenteException in creaAreaContabilePagamentoDiretto", e1);
					throw new RuntimeException(e1);
				}
				break;
			default:
				logger.error("--> errore TipoEntita non valida per tipo documento in creaAreaContabilePagamentoDiretto");
				throw new IllegalArgumentException("TipoEntita non valida per tipo documento incasso pagamento ("
						+ areaPagamenti.getTipoAreaPartita().getTipoDocumento().getTipoEntita() + ")");
			}
		}

		// Creo l'area contabile
		AreaContabile areaContabile = new AreaContabile();
		areaContabile.setNote(parametriCreazioneAreaChiusure.getNoteContabili());
		areaContabile = aggiornaAreaContabile(areaContabile, areaPagamenti, tipoAreaContabile);
		if (!raggruppamentoContoCassa) {
			raggruppamentoContoCassa = areaContabile.getTipoAreaContabile().isRaggruppamentoContoCassa();
		}

		// nel caso di entita' del documento di origine
		// (pagamento.getRata().getAreaRate().getDocumento()) null e quindi
		// entita' azienda, non genero nessuna delle righe contabili e quindi
		// esco dal metodo.
		// soluzione provvisoria per rimborsi e compensazioni bug 1641, da
		// rivedere completamente.
		Pagamento p = areaPagamenti.getPagamenti().iterator().next();
		if (p.getRata().getAreaRate().getDocumento().getEntita() == null) {
			// aggiorna il documento di area partite
			areaPagamenti.setDocumento(areaContabile.getDocumento());
			logger.debug("--> Exit creaAreaContabilePagamentoDiretto");
			return areaPagamenti;
		}

		// carica la lista di Pagamenti per l'area pagamenti corrente per la
		// generazione delle RigheContabili
		BigDecimal totaleConto = BigDecimal.ZERO;
		BigDecimal totaleAbbuoniPagamentiAttivi = BigDecimal.ZERO;
		BigDecimal totaleAbbuoniPagamentiPassivi = BigDecimal.ZERO;
		BigDecimal totaleAbbuoniIncassiAttivi = BigDecimal.ZERO;
		BigDecimal totaleAbbuoniIncassiPassivi = BigDecimal.ZERO;
		BigDecimal totaleScontoFinanziario = BigDecimal.ZERO;
		BigDecimal perditeSuCambi = BigDecimal.ZERO;
		BigDecimal utileSuCambi = BigDecimal.ZERO;
		BigDecimal totaleAnticipato = BigDecimal.ZERO;

		List<RigaContabile> righeContabiliEntita = new ArrayList<RigaContabile>();

		boolean scritturaContabileInDare = false;
		TipoPartita tipoPartitaCorrente;
		Set<Pagamento> pagamentiPerRaggruppamentoContoCassa = new HashSet<Pagamento>();
		for (Pagamento pagamento : areaPagamenti.getPagamenti()) {
			if (parametriCreazioneAreaChiusure.isCompensazioneRate()
					&& TipoPartita.PASSIVA.equals(pagamento.getRata().getAreaRate().getTipoAreaPartita()
							.getTipoPartita())) {
				pagamento.setImporto(pagamento.getImporto().negate());
				pagamento.setImportoForzato(pagamento.getImportoForzato().negate());
			}

			// scrittura in dare se la rata partita corrente è passiva , in avere se attiva.
			// viceversa per la scrittura in conto cassa
			tipoPartitaCorrente = pagamento.getRata().getAreaRate().getTipoAreaPartita().getTipoPartita();
			scritturaContabileInDare = TipoPartita.PASSIVA.equals(pagamento.getRata().getAreaRate()
					.getTipoAreaPartita().getTipoPartita());
			// recupera il sottoconto dell'entita
			EntitaLite entita = pagamento.getRata().getAreaRate().getDocumento().getEntita();

			SottoConto sottoContoEntita;
			try {
				sottoContoEntita = pianoContiManager.caricaSottoContoPerEntita(entita);
			} catch (ContabilitaException e) {
				logger.error("--> errore ContabilitaException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			}

			BigDecimal importoPagamento = pagamento.getImporto().getImportoInValuta()
					.add(pagamento.getImportoForzato().getImportoInValuta());
			// L'importoPagamento per l'entità risulta essere l'importo in valuta ma con il tasso della rata
			Importo importoPagamentoInValutaRata = new Importo(pagamento.getImporto().getCodiceValuta(), pagamento
					.getRata().getImporto().getTassoDiCambio());
			importoPagamentoInValutaRata.setImportoInValuta(importoPagamento);
			importoPagamentoInValutaRata.calcolaImportoValutaAzienda(RigaIva.SCALE_FISCALE);

			BigDecimal importoDiffCambio = importoPagamentoInValutaRata.getImportoInValutaAzienda().subtract(
					pagamento.getImporto().getImportoInValutaAzienda()
							.add(pagamento.getImportoForzato().getImportoInValutaAzienda()));

			switch (tipoPartitaCorrente) {
			case ATTIVA:
				if (importoDiffCambio.compareTo(BigDecimal.ZERO) > 0) {
					perditeSuCambi = perditeSuCambi.add(importoDiffCambio);
				} else if (importoDiffCambio.compareTo(BigDecimal.ZERO) < 0) {
					utileSuCambi = utileSuCambi.add(importoDiffCambio.abs());
				}
				break;
			case PASSIVA:
				if (importoDiffCambio.compareTo(BigDecimal.ZERO) > 0) {
					utileSuCambi = utileSuCambi.add(importoDiffCambio);
				} else if (importoDiffCambio.compareTo(BigDecimal.ZERO) < 0) {
					perditeSuCambi = perditeSuCambi.add(importoDiffCambio.abs());
				}
				break;
			default:
				break;
			}

			if (pagamento.getChiusuraForzataRata()) {
				// calcolo del residuo rata
				BigDecimal importoForzato = pagamento.getImportoForzato().getImportoInValutaAzienda();
				Importo importoForzatoInValutaRata = pagamento.getImportoForzato().clone();
				importoForzatoInValutaRata.setTassoDiCambio(pagamento.getRata().getImporto().getTassoDiCambio());
				importoForzatoInValutaRata.calcolaImportoValutaAzienda(RigaIva.SCALE_FISCALE);

				// gestione residuo rata
				if (TipoPartita.PASSIVA.equals(tipoPartitaCorrente)) {
					// se tipo partita passiva (pagamento) se il residuo rata è negativo deve rientrare come abbuono
					// attivo se positivo come abbuono passivo
					if ((importoForzato.compareTo(BigDecimal.ZERO) > 0)) {
						totaleAbbuoniPagamentiAttivi = totaleAbbuoniPagamentiAttivi.add(importoForzato);
					} else if (importoForzato.compareTo(BigDecimal.ZERO) < 0) {
						totaleAbbuoniPagamentiPassivi = totaleAbbuoniPagamentiPassivi.add(importoForzato.abs());
					}
				} else if (TipoPartita.ATTIVA.equals(tipoPartitaCorrente)) {
					if (importoForzato.compareTo(BigDecimal.ZERO) != 0 && pagamento.isScontoFinanziario()) {
						// calcolo a quanto ammonta lo sconto finanziario in valuta azienda alla data della rata
						Importo scontoFinanziarioRata = pagamento.getImportoForzato().clone();
						scontoFinanziarioRata.setTassoDiCambio(pagamento.getRata().getImporto().getTassoDiCambio());
						scontoFinanziarioRata.calcolaImportoValutaAzienda(RigaIva.SCALE_FISCALE);

						// aggiungo il l'importo dello sconto nella data di pagamento
						totaleScontoFinanziario = totaleScontoFinanziario.add(importoForzato);

						// se c'è differenza tra i 2 importi la giro sulle perdite o utili su cambi
						BigDecimal diffSconto = scontoFinanziarioRata.getImportoInValutaAzienda().subtract(
								totaleScontoFinanziario);
						if (diffSconto.compareTo(BigDecimal.ZERO) > 0) {
							utileSuCambi = utileSuCambi.add(diffSconto);
						} else if (diffSconto.compareTo(BigDecimal.ZERO) < 0) {
							perditeSuCambi = perditeSuCambi.add(diffSconto);
						}
					} else if (importoForzato.compareTo(BigDecimal.ZERO) > 0) {
						// abbuono passivo
						totaleAbbuoniIncassiPassivi = totaleAbbuoniIncassiPassivi.add(importoForzato);
					} else if (importoForzato.compareTo(BigDecimal.ZERO) < 0) {
						// abbuono attivo
						totaleAbbuoniIncassiAttivi = totaleAbbuoniIncassiAttivi.add(importoForzato.abs());
					}
				}
				importoPagamento = importoPagamento.add(importoForzato);
			}

			Set<Pagamento> pagamenti = new HashSet<Pagamento>();
			pagamenti.add(pagamento);
			RigaContabile rigaEntita = creaRigaContabile(areaContabile, sottoContoEntita,
					importoPagamentoInValutaRata.getImportoInValutaAzienda(), scritturaContabileInDare, pagamenti);

			righeContabiliEntita.add(rigaEntita);

			BigDecimal importoInValutaAzienda = pagamento.getImporto().getImportoInValutaAzienda();

			BigDecimal importoAnticipato = BigDecimal.ZERO;
			if (parametriCreazioneAreaChiusure.isAnticipoFattura()) {
				importoAnticipato = areaAnticipoFattureContabilitaManager.calcolaImportoAnticipato(pagamento,
						percentualeAnticipo, percentualeIvaAnticipo);
				totaleAnticipato = totaleAnticipato.add(importoAnticipato);
			}

			if (!raggruppamentoContoCassa) {
				Set<Pagamento> pags = new HashSet<Pagamento>();
				pags.add(pagamento);
				// eseguo la scrittura del conto cassa per il singolo pagamento
				creaRigaContabile(areaContabile, sottoContoCassa, importoInValutaAzienda, !scritturaContabileInDare,
						pags);
			} else {
				// totalizza importo di pagamento
				if (parametriCreazioneAreaChiusure.isCompensazioneRate()) {
					if (TipoPartita.PASSIVA.equals(pagamento.getRata().getAreaRate().getTipoAreaPartita()
							.getTipoPartita())) {
						totaleConto = totaleConto.add(importoInValutaAzienda.negate());
					} else {
						totaleConto = totaleConto.add(importoInValutaAzienda);
					}
					scritturaContabileInDare = false;
				} else {
					totaleConto = totaleConto.add(importoInValutaAzienda);
				}
				pagamentiPerRaggruppamentoContoCassa.add(pagamento);
			}
		}

		if (parametriCreazioneAreaChiusure.isAnticipoFattura()) {
			creaRigaContabile(areaContabile, sottoContoAnticipiFatture, totaleAnticipato, !scritturaContabileInDare,
					areaPagamenti.getPagamenti());
			creaRigaContabile(areaContabile, sottoContoCassa, totaleAnticipato, scritturaContabileInDare,
					areaPagamenti.getPagamenti());
		}

		if (totaleScontoFinanziario.compareTo(BigDecimal.ZERO) != 0) {
			logger.debug("--> creazione riga contabile per abbuoni attivi pagamento in avere ");
			SottoConto sottoContoScontoFinanziario;
			try {
				sottoContoScontoFinanziario = pianoContiManager
						.caricaContoPerTipoContoBase(ETipoContoBase.SCONTO_FINANZIARIO);
			} catch (ContabilitaException e) {
				logger.error("--> errore ContabilitaException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			} catch (ContiBaseException e) {
				logger.error("--> errore ContiBaseException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			}
			creaRigaContabile(areaContabile, sottoContoScontoFinanziario, totaleScontoFinanziario, true, null);
		}

		if (totaleAbbuoniPagamentiAttivi.compareTo(BigDecimal.ZERO) != 0) {
			logger.debug("--> creazione riga contabile per abbuoni attivi pagamento in avere ");
			SottoConto sottoContoAbbuoniAttivi;
			try {
				sottoContoAbbuoniAttivi = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.ABBUONI_ATTIVI);
			} catch (ContabilitaException e) {
				logger.error("--> errore ContabilitaException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			} catch (ContiBaseException e) {
				logger.error("--> errore ContiBaseException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			}
			creaRigaContabile(areaContabile, sottoContoAbbuoniAttivi, totaleAbbuoniPagamentiAttivi, false, null);
		}
		if (totaleAbbuoniPagamentiPassivi.compareTo(BigDecimal.ZERO) != 0) {
			logger.debug("--> creazione riga contabile per abbuoni passivi pagamento in dare ");
			SottoConto sottoContoAbbuoniPassivi;
			try {
				sottoContoAbbuoniPassivi = pianoContiManager
						.caricaContoPerTipoContoBase(ETipoContoBase.ABBUONI_PASSIVI);
			} catch (ContabilitaException e) {
				logger.error("--> errore ContabilitaException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			} catch (ContiBaseException e) {
				logger.error("--> errore ContiBaseException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			}
			creaRigaContabile(areaContabile, sottoContoAbbuoniPassivi, totaleAbbuoniPagamentiPassivi, true, null);
		}

		if (totaleAbbuoniIncassiAttivi.compareTo(BigDecimal.ZERO) != 0) {
			logger.debug("--> creazione riga contabile per abbuoni attivi incasso in avere ");
			SottoConto sottoContoAbbuoniAttivi;
			try {
				sottoContoAbbuoniAttivi = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.ABBUONI_ATTIVI);
			} catch (ContabilitaException e) {
				logger.error("--> errore ContabilitaException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			} catch (ContiBaseException e) {
				logger.error("--> errore ContiBaseException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			}
			creaRigaContabile(areaContabile, sottoContoAbbuoniAttivi, totaleAbbuoniIncassiAttivi, false, null);
		}

		if (totaleAbbuoniIncassiPassivi.compareTo(BigDecimal.ZERO) != 0) {
			logger.debug("--> creazione riga contabile per abbuoni passivi incasso in dare ");
			SottoConto sottoContoAbbuoniPassivi;
			try {
				sottoContoAbbuoniPassivi = pianoContiManager
						.caricaContoPerTipoContoBase(ETipoContoBase.ABBUONI_PASSIVI);
			} catch (ContabilitaException e) {
				logger.error("--> errore ContabilitaException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			} catch (ContiBaseException e) {
				logger.error("--> errore ContiBaseException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			}
			creaRigaContabile(areaContabile, sottoContoAbbuoniPassivi, totaleAbbuoniIncassiPassivi, true, null);
		}

		if (perditeSuCambi.compareTo(BigDecimal.ZERO) != 0) {
			SottoConto sottoContoPerditeSuCambi;
			try {
				sottoContoPerditeSuCambi = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.PERDITE_CAMBI);
			} catch (ContabilitaException e) {
				logger.error("--> errore ContabilitaException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			} catch (ContiBaseException e) {
				logger.error("--> errore ContiBaseException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			}
			creaRigaContabile(areaContabile, sottoContoPerditeSuCambi, perditeSuCambi, true, null);
		}

		if (utileSuCambi.compareTo(BigDecimal.ZERO) != 0) {
			SottoConto sottoContoUtileSuCambi;
			try {
				sottoContoUtileSuCambi = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.UTILE_CAMBI);
			} catch (ContabilitaException e) {
				logger.error("--> errore ContabilitaException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			} catch (ContiBaseException e) {
				logger.error("--> errore ContiBaseException in creaAreaContabilePagamentoDiretto", e);
				throw new RuntimeException(e);
			}
			creaRigaContabile(areaContabile, sottoContoUtileSuCambi, utileSuCambi, false, null);
		}

		if (raggruppamentoContoCassa) {
			creaRigaContabile(areaContabile, sottoContoCassa, totaleConto, !scritturaContabileInDare,
					pagamentiPerRaggruppamentoContoCassa);
		}

		// spese incasso
		creaRigheContabiliSpeseIncasso(areaContabile, areaPagamenti.getSpeseIncasso());

		// ritenuta acconto
		ritenutaAccontoContabilitaManager.creaRigheContabiliPerErarioDaPagare(areaContabile,
				areaPagamenti.getPagamenti());
		ritenutaAccontoContabilitaManager.creaRigheContabiliPerErarioPagato(areaContabile,
				areaPagamenti.getPagamenti(), righeContabiliEntita);

		// aggiorna il documento di area partite
		areaPagamenti.setDocumento(areaContabile.getDocumento());

		if (areaContabile.getTipoAreaContabile().getStatoAreaContabileGenerata() != StatoAreaContabile.PROVVISORIO) {
			boolean validaAreaContabile = true;
			for (RigaContabile rigaContabile : areaContabileManager.caricaRigheContabili(areaContabile.getId())) {
				if (!rigaContabile.isValid()) {
					validaAreaContabile = false;
					break;
				}
			}
			if (validaAreaContabile) {
				areaContabile.setStatoAreaContabile(areaContabile.getTipoAreaContabile()
						.getStatoAreaContabileGenerata());
				areaContabile.setValidRigheContabili(true);
				areaContabile.setValidDataRigheContabili(Calendar.getInstance().getTime());
				areaContabile.setValidUtenteRigheContabili(getPrincipal().getUserName());
			} else {
				areaContabile.setStatoAreaContabile(StatoAreaContabile.PROVVISORIO);
			}

			try {
				areaContabile = areaContabileManager.salvaAreaContabile(areaContabile, false);
			} catch (Exception e) {
				logger.error("--> errore durante il salvataggio dell'area contabile.", e);
				throw new RuntimeException("errore durante il salvataggio dell'area contabile.", e);
			}
		}

		logger.debug("--> Exit creaAreaContabilePagamentoDiretto");
		return areaPagamenti;
	}

	@Override
	public AreaPagamenti creaAreaContabilePagamentoDistinta(AreaPagamenti areaPagamenti,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) throws PagamentiException {
		logger.debug("--> Enter creaAreaContabilePagamentoDistinta");

		ContabilitaSettings contabilitaSetting = contabilitaSettingsManager.caricaContabilitaSettings();

		TipoAreaContabile tipoAreaContabile = getTipoAreaContabileByTipoDocumento(areaPagamenti.getDocumento()
				.getTipoDocumento());

		// non faccio niente se non esiste il tipo area contabile per il tipo documento
		if (tipoAreaContabile == null) {
			return areaPagamenti;
		}

		AreaContabile areaContabile = new AreaContabile();
		areaContabile.setNote(parametriCreazioneAreaChiusure.getNoteContabili());
		areaContabile = aggiornaAreaContabile(areaContabile, areaPagamenti, tipoAreaContabile);

		SottoConto contoEffetti = null;
		if (areaContabile.getDocumento().getRapportoBancarioAzienda() != null) {
			logger.debug("--> documento incasso per distinta: carico i conti base");
			try {
				if (contabilitaSetting.isUsaContoEffettiAttivi()) {
					contoEffetti = pianoContiManager.caricaContoPerTipoContoBase(ETipoContoBase.EFFETTI_ATTIVI);
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
		}

		// carica la lista di Pagamenti per l'area partita corrente per la
		// generazione delle RigheContabili
		BigDecimal totaleConto = BigDecimal.ZERO;
		boolean rigaContabileInDare = false;
		for (Pagamento pagamento : areaPagamenti.getPagamenti()) {
			// scrittura in dare se la rata partita corrente è passiva , in
			// avere se attiva
			rigaContabileInDare = TipoPartita.PASSIVA.equals(pagamento.getRata().getAreaRate().getTipoAreaPartita()
					.getTipoPartita());
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
					rigaContabileInDare, null);
			// totalizza importo di pagamento
			totaleConto = totaleConto.add(pagamento.getImporto().getImportoInValutaAzienda());

			// spese incasso
			creaRigheContabiliSpeseIncasso(areaContabile, areaPagamenti.getSpeseIncasso());

		}

		creaRigaContabile(areaContabile, contoEffetti, totaleConto, !rigaContabileInDare, null);

		if (areaContabile.getTipoAreaContabile().getStatoAreaContabileGenerata() != StatoAreaContabile.PROVVISORIO) {
			boolean validaAreaContabile = true;
			for (RigaContabile rigaContabile : areaContabileManager.caricaRigheContabili(areaContabile.getId())) {
				if (!rigaContabile.isValid()) {
					validaAreaContabile = false;
					break;
				}
			}
			if (validaAreaContabile) {
				areaContabile.setStatoAreaContabile(areaContabile.getTipoAreaContabile()
						.getStatoAreaContabileGenerata());
				areaContabile.setValidRigheContabili(true);
				areaContabile.setValidDataRigheContabili(Calendar.getInstance().getTime());
				areaContabile.setValidUtenteRigheContabili(getPrincipal().getUserName());
			} else {
				areaContabile.setStatoAreaContabile(StatoAreaContabile.PROVVISORIO);
			}

			try {
				areaContabile = areaContabileManager.salvaAreaContabile(areaContabile, false);
			} catch (Exception e) {
				logger.error("--> errore durante il salvataggio dell'area contabile.", e);
				throw new RuntimeException("errore durante il salvataggio dell'area contabile.", e);
			}
		}

		// aggiorna il documento di area partite
		areaPagamenti.setDocumento(areaContabile.getDocumento());

		logger.debug("--> Exit creaAreaContabilePagamentoDistinta");
		return areaPagamenti;
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
	 *            i pagamenti associati alla riga contabile generata
	 * @return riga contrabile creata
	 */
	private RigaContabile creaRigaContabile(AreaContabile areaContabile, SottoConto sottoConto, BigDecimal importo,
			boolean rigaContabileInDare, Set<Pagamento> pagamenti) {
		logger.debug("--> Enter creaRigaContabile");

		if (BigDecimal.ZERO.compareTo(importo) > 0) {
			importo = importo.negate();
			rigaContabileInDare = !rigaContabileInDare;
		}

		RigaContabile rigaContabile = RigaContabile.creaRigaContabile(areaContabile, sottoConto, rigaContabileInDare,
				importo, null, false);
		rigaContabile.setPagamenti(pagamenti);

		logger.debug("--> Exit creaRigaContabile");
		return areaContabileManager.salvaRigaContabileNoCheck(rigaContabile);
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
			creaRigaContabile(areaContabile, contoSpese, speseIncasso, true, null);

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
			creaRigaContabile(areaContabile, contoSpese, speseIncasso, false, null);
		}
	}

	/**
	 * Verifica se il tipo area partita è definito tra i tipi documento base come
	 * TipoOperazione.UTILIZZO_ACCONTO_CLIENTE.
	 * 
	 * @param tipoAreaPartita
	 *            il tipo area da verificare
	 * @return true o false
	 */
	private boolean isTipoDocumentoBaseAcconto(TipoAreaPartita tipoAreaPartita) {
		TipoDocumentoBasePartite tipoDocumentoBaseAccontoCliente = null;
		TipoDocumentoBasePartite tipoDocumentoBaseAccontoFornitore = null;
		try {
			tipoDocumentoBaseAccontoCliente = tipiAreaPartitaManager
					.caricaTipoDocumentoBase(TipoOperazione.UTILIZZO_ACCONTO_CLIENTE);
			if (tipoAreaPartita.equals(tipoDocumentoBaseAccontoCliente.getTipoAreaPartita())) {
				return true;
			}
		} catch (TipoDocumentoBaseException e2) {
			if (logger.isDebugEnabled()) {
				logger.debug("--> Tipo documento base per UTILIZZO_ACCONTO_CLIENTE non trovato.");
			}
		}

		try {
			tipoDocumentoBaseAccontoFornitore = tipiAreaPartitaManager
					.caricaTipoDocumentoBase(TipoOperazione.UTILIZZO_ACCONTO_FORNITORE);
			if (tipoAreaPartita.equals(tipoDocumentoBaseAccontoFornitore.getTipoAreaPartita())) {
				return true;
			}
		} catch (TipoDocumentoBaseException e2) {
			if (logger.isDebugEnabled()) {
				logger.debug("--> Tipo documento base per UTILIZZO_ACCONTO_FORNITORE non trovato.");
			}
		}

		return false;
	}
}
