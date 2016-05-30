package it.eurotn.panjea.contabilita.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.TipoDocumentoNonConformeException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva.IndicatoreVolumeAffari;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings.ETipoPeriodicita;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.ProRataSetting;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.manager.corrispettivo.interfaces.RegistroIvaTipologiaCorrispettivoManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnagraficaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.contabilita.manager.interfaces.LiquidazioneIvaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.RegistroIvaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.StrutturaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.manager.ivasospesa.interfaces.IvaSospesaManager;
import it.eurotn.panjea.contabilita.manager.liquidazione.interfaces.LiquidazionePagamentiManager;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.contabilita.util.LiquidazioneIvaDTO;
import it.eurotn.panjea.contabilita.util.RegistroLiquidazioneDTO;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.pagamenti.manager.interfaces.CodicePagamentoManager;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoNonTrovatoException;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.LiquidazioneIvaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LiquidazioneIvaManager")
public class LiquidazioneIvaManagerBean implements LiquidazioneIvaManager {

	private static Logger logger = Logger.getLogger(LiquidazioneIvaManagerBean.class.getName());

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private RegistroIvaManager registroIvaManager;

	@EJB(mappedName = "Panjea.AreaContabileManager")
	private AreaContabileManager areaContabileManager;

	@EJB
	@IgnoreDependency
	private StrutturaContabileManager strutturaContabileManager;

	@EJB
	private ContabilitaAnagraficaManager contabilitaAnagraficaManager;

	@EJB
	private RegistroIvaTipologiaCorrispettivoManager registroIvaTipologiaCorrispettivoManager;

	@EJB
	private CodicePagamentoManager codicePagamentoManager;

	@EJB
	private TipiAreaContabileManager tipiAreaContabileManager;

	@EJB
	private ContabilitaSettingsManager contabilitaSettingsManager;

	@EJB
	private IvaSospesaManager ivaSospesaManager;

	@EJB
	private LiquidazionePagamentiManager liquidazionePagamentiManager;

	/**
	 * Aggiunge alla liquidazioneIva i risultati relativi alla liquidazione annuale.
	 * 
	 * @param liquidazioneIvaDTO
	 *            la liquidazione a cui aggiungere i risultati
	 * @param dataInizio
	 *            dataInizio
	 * @param dataFine
	 *            dataFine
	 * @return LiquidazioneIvaDTO
	 */
	private LiquidazioneIvaDTO aggiungiRisultatiLiquidazioneAnnuale(LiquidazioneIvaDTO liquidazioneIvaDTO,
			Date dataInizio, Date dataFine) {

		List<Object[]> pagamentiLiquidazione = caricaTotaliPagamentiLiquidazione(dataInizio, dataFine);
		for (Object[] obj : pagamentiLiquidazione) {
			TipoDocumento td = new TipoDocumento();
			td.setId((Integer) obj[0]);
			td.setCodice((String) obj[1]);
			td.setDescrizione((String) obj[2]);

			BigDecimal totale = (BigDecimal) obj[3];

			liquidazioneIvaDTO.addToTotaliPagamento(td, totale);
		}

		TotaliCodiceIvaDTO volumeAffariTotale = registroIvaManager.caricaTotaliCodiceIvaByVolumeAffari(dataInizio,
				dataFine, IndicatoreVolumeAffari.SI, null, false);
		TotaliCodiceIvaDTO beniStrumentaliTotale = registroIvaManager.caricaTotaliCodiceIvaByVolumeAffari(dataInizio,
				dataFine, IndicatoreVolumeAffari.VENDITA_BENE_STRUMENTALE, null, false);
		TotaliCodiceIvaDTO volumeAffariAziende = registroIvaManager.caricaTotaliCodiceIvaByVolumeAffari(dataInizio,
				dataFine, IndicatoreVolumeAffari.SI, Boolean.TRUE, true);
		TotaliCodiceIvaDTO volumeAffariPrivati = registroIvaManager.caricaTotaliCodiceIvaByVolumeAffari(dataInizio,
				dataFine, IndicatoreVolumeAffari.SI, Boolean.FALSE, true);

		List<TotaliCodiceIvaDTO> righeVolumeAffariPrivati = registroIvaManager.caricaRigheIvaVolumeAffariPrivati(
				dataInizio, dataFine);

		liquidazioneIvaDTO.setVolumeAffariTotale(volumeAffariTotale);
		liquidazioneIvaDTO.setVolumeAffariAziende(volumeAffariAziende);
		liquidazioneIvaDTO.setVolumeAffariPrivati(volumeAffariPrivati);
		liquidazioneIvaDTO.setBeniStrumentaliTotale(beniStrumentaliTotale);

		liquidazioneIvaDTO.setRigheVolumeAffariPrivati(righeVolumeAffariPrivati);

		return liquidazioneIvaDTO;
	}

	@Override
	public LiquidazioneIvaDTO calcolaLiquidazione(Date dataRegistrazione) {
		// carico da contabilita' settings il tipo periodicita' scelto
		// dall'utente
		ETipoPeriodicita tipoPeriodicita = contabilitaSettingsManager.caricaContabilitaSettings().getTipoPeriodicita();

		// istanzio un calendar per impostare a seconda del tipo periodicita' la
		// data di inizio periodo
		// per il calcolo della liquidazione iva
		Calendar calendarInizioPeriodo = Calendar.getInstance();
		calendarInizioPeriodo.setTime(dataRegistrazione);

		// se periodicita' annuale devo impostare il giorno 1 e mese gennaio
		// come data inizio periodo
		// mantenendo l'anno dell'area contabile
		if (tipoPeriodicita.equals(ETipoPeriodicita.ANNUALE)) {
			calendarInizioPeriodo.set(Calendar.DAY_OF_MONTH, 1);
			calendarInizioPeriodo.set(Calendar.MONTH, Calendar.JANUARY);
		} else if (tipoPeriodicita.equals(ETipoPeriodicita.TRIMESTRALE)) {
			// se e' trimestrale ritorno indietro di 2 mesi rispetto al corrente
			// e
			// imposto il giorno a 1
			// NB. il documento di liquidazione deve avere la data di
			// registrazione
			// = ultimo giorno del trimenstre.
			// Per questo faccio un -2 senza controllare altro
			calendarInizioPeriodo.set(Calendar.DAY_OF_MONTH, 1);
			calendarInizioPeriodo.add(Calendar.MONTH, -2);
		} else if (tipoPeriodicita.equals(ETipoPeriodicita.MENSILE)) {
			// se mensile setto solo il giorno a 1
			calendarInizioPeriodo.set(Calendar.DAY_OF_MONTH, 1);
		}
		// calcolo la liquidazione iva per il periodo
		return calcolaLiquidazione(calendarInizioPeriodo.getTime(), dataRegistrazione);
	}

	@Override
	public LiquidazioneIvaDTO calcolaLiquidazione(Date dataInizioPeriodo, Date dataFinePeriodo) {
		logger.debug("--> Enter caricaLiquidazioneIva");

		ContabilitaSettings contabilitaSettings = contabilitaSettingsManager.caricaContabilitaSettings();

		List<RegistroIva> registriIva;
		try {
			registriIva = contabilitaAnagraficaManager.caricaRegistriIva("nome", null);
		} catch (ContabilitaException e1) {
			logger.error("--> errore nel caricare i registri iva per il calcolo della liquidazione nel periodo "
					+ dataInizioPeriodo + ", " + dataFinePeriodo);
			throw new RuntimeException(
					"errore nel caricare i registri iva per il calcolo della liquidazione nel periodo "
							+ dataInizioPeriodo + ", " + dataFinePeriodo);
		}
		LiquidazioneIvaDTO liquidazioneIvaDTO = new LiquidazioneIvaDTO();
		liquidazioneIvaDTO.setDataInizio(dataInizioPeriodo);
		liquidazioneIvaDTO.setDataFine(dataFinePeriodo);

		// cerco la rata sulla liquidazione iva precedente (Riferimento mese
		// dataInizioPeriodo)
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataInizioPeriodo);
		int mese = cal.get(Calendar.MONTH) + 1;
		int anno = cal.get(Calendar.YEAR);
		int mesePrec = 0;
		int annoPrec = 0;

		ETipoPeriodicita periodicitaLiquidazione = contabilitaSettings.getTipoPeriodicita();

		switch (periodicitaLiquidazione) {
		case TRIMESTRALE:
			cal.add(Calendar.MONTH, -3);
			mesePrec = cal.get(Calendar.MONTH) + 1;
			annoPrec = cal.get(Calendar.YEAR);
			break;
		default:
			cal.add(Calendar.MONTH, -1);
			mesePrec = cal.get(Calendar.MONTH) + 1;
			annoPrec = cal.get(Calendar.YEAR);
			break;
		}

		Rata rataRisultatoPrecedente = null;
		try {
			rataRisultatoPrecedente = liquidazionePagamentiManager.caricaRataLiquidazionePrecedente(mese, anno);
		} catch (TipoDocumentoBaseException e) {
			logger.trace("manca il tipo documento base per liquidazioneRataPrecedente.");
		}

		for (RegistroIva registroIva : registriIva) {

			// se il tipo registro e' RIEPILOGATIVO non devo prenderlo in
			// considerazione
			if (registroIva.getTipoRegistro().compareTo(TipoRegistro.RIEPILOGATIVO) == 0) {
				continue;
			}

			BigDecimal totaleRegistro = BigDecimal.ZERO;
			BigDecimal totaleRegistroIntra = BigDecimal.ZERO;
			BigDecimal totaleSplitPaymentRegistro = BigDecimal.ZERO;

			List<TotaliCodiceIvaDTO> righeCodiciIvaNormali = null;

			List<TotaliCodiceIvaDTO> righeCodiciIvaVentialzione = null;
			List<TotaliCodiceIvaDTO> righeCodiciIvaIntra = null;

			switch (registroIva.getTipoRegistro()) {
			case CORRISPETTIVO:
				LiquidazioneIvaDTO liquidazioneIvaDTOCorr = registroIvaTipologiaCorrispettivoManager.caricaTotali(
						registroIva, dataInizioPeriodo, dataFinePeriodo);

				// non ho righe di tipo corrispettivo quindi non aggiungo nulla
				if (liquidazioneIvaDTOCorr == null) {
					break;
				}

				righeCodiciIvaNormali = liquidazioneIvaDTOCorr.getTotaliRegistri().get(registroIva);
				righeCodiciIvaVentialzione = liquidazioneIvaDTOCorr.getVentilazioniIva().get(registroIva);

				// calcolo i totali per il registro acquisti
				for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : righeCodiciIvaNormali) {
					if (totaliCodiceIvaDTO.isConsideraPerLiquidazione()) {
						totaleRegistro = totaleRegistro.add(totaliCodiceIvaDTO.getImposta());
					}
					if (totaliCodiceIvaDTO.isSplitPayment()) {
						totaleSplitPaymentRegistro = totaleSplitPaymentRegistro.add(totaliCodiceIvaDTO.getImposta());
					}
				}
				break;
			case ACQUISTO:
				righeCodiciIvaNormali = registroIvaManager.caricaTotaliCodiciIvaByRegistro(dataInizioPeriodo,
						dataFinePeriodo, registroIva, null, null);
				righeCodiciIvaIntra = registroIvaManager.caricaTotaliCodiciIvaByRegistro(dataInizioPeriodo,
						dataFinePeriodo, registroIva, null, GestioneIva.INTRA);
				// calcolo i totali per il registro acquisti
				for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : righeCodiciIvaNormali) {
					if (totaliCodiceIvaDTO.isConsideraPerLiquidazione()) {
						totaleRegistro = totaleRegistro.add(totaliCodiceIvaDTO.getImpostaDetraibile());
					}
					if (totaliCodiceIvaDTO.isSplitPayment()) {
						totaleSplitPaymentRegistro = totaleSplitPaymentRegistro.add(totaliCodiceIvaDTO
								.getImpostaDetraibile());
					}
				}
				for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : righeCodiciIvaIntra) {
					if (totaliCodiceIvaDTO.isConsideraPerLiquidazione()) {
						totaleRegistroIntra = totaleRegistroIntra.add(totaliCodiceIvaDTO.getImpostaDetraibile());
					}
				}
				break;
			case VENDITA:
				righeCodiciIvaNormali = registroIvaManager.caricaTotaliCodiciIvaByRegistro(dataInizioPeriodo,
						dataFinePeriodo, registroIva, null, null);
				righeCodiciIvaIntra = registroIvaManager.caricaTotaliCodiciIvaByRegistro(dataInizioPeriodo,
						dataFinePeriodo, registroIva, null, GestioneIva.INTRA);
				// calcolo i totali per il registro vendita
				for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : righeCodiciIvaNormali) {
					if (totaliCodiceIvaDTO.isConsideraPerLiquidazione()) {
						totaleRegistro = totaleRegistro.add(totaliCodiceIvaDTO.getImposta());
					}
					if (totaliCodiceIvaDTO.isSplitPayment()) {
						totaleSplitPaymentRegistro = totaleSplitPaymentRegistro.add(totaliCodiceIvaDTO.getImposta());
					}
				}
				for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : righeCodiciIvaIntra) {
					if (totaliCodiceIvaDTO.isConsideraPerLiquidazione()) {
						totaleRegistroIntra = totaleRegistroIntra.add(totaliCodiceIvaDTO.getImposta());
					}
				}
				break;
			case RIEPILOGATIVO:
				break;
			default:
				throw new UnsupportedOperationException("Tipologia registro non gestita nella liquidazione: "
						+ registroIva.getTipoRegistro());

			}

			liquidazioneIvaDTO.setRataPrecedente(rataRisultatoPrecedente);
			// aggiungo i valori della ventilazione se ci sono
			if (righeCodiciIvaVentialzione != null) {
				liquidazioneIvaDTO.addToTotaliVentilazioni(registroIva, righeCodiciIvaVentialzione);
			}

			// aggiungo i valori del registro
			liquidazioneIvaDTO.addToTotaliRegistri(registroIva, righeCodiciIvaNormali);

			// aggiungo i totali del registro
			liquidazioneIvaDTO.addToTotali(registroIva, totaleRegistro);

			// aggiungo i totali split Payment del registro
			liquidazioneIvaDTO.addToTotaliSplitPayment(registroIva, totaleSplitPaymentRegistro);

			if (registroIva.getTipoRegistro() == TipoRegistro.ACQUISTO) {
				creaRegistroProRata(registroIva, totaleRegistro, liquidazioneIvaDTO, dataInizioPeriodo);
			}

			// aggiungo i valori del registro intra
			liquidazioneIvaDTO.addToTotaliRegistriIntra(registroIva, righeCodiciIvaIntra);
			// aggiungo i totali del registro intra
			liquidazioneIvaDTO.addToTotaliIntra(registroIva, totaleRegistroIntra);
		}

		// aggiunge alla liquidazione i totali codice iva sospesa (iva sospesa pagata)
		liquidazioneIvaDTO = ivaSospesaManager.aggiungiTotaliCodiceIvaSospesa(liquidazioneIvaDTO, dataInizioPeriodo,
				dataFinePeriodo);

		BigDecimal totaleAccontoIvaDicembre = liquidazionePagamentiManager.caricaTotaleAccontoIvaDicembre(
				periodicitaLiquidazione, dataInizioPeriodo, dataFinePeriodo);
		liquidazioneIvaDTO.setTotaleAccontoIvaDicembre(totaleAccontoIvaDicembre);

		// carica i dati per il mese percendete per il minimale iva
		GiornaleIva giornaleIvaRif = caricaParametriGiornaleIvaDefinitivo(contabilitaSettings, annoPrec, mesePrec);
		liquidazioneIvaDTO.setMinimaleIVA(giornaleIvaRif.getMinimaleIVA());
		// carico i dati del mese attuale per la percentuale trimestrale
		giornaleIvaRif = caricaParametriGiornaleIvaDefinitivo(contabilitaSettings, anno, mese);
		liquidazioneIvaDTO.setPercTrimestraleValore(giornaleIvaRif.getPercTrimestraleValore());

		if (liquidazioneIvaDTO.isAnnuale()) {
			liquidazioneIvaDTO = aggiungiRisultatiLiquidazioneAnnuale(liquidazioneIvaDTO, dataInizioPeriodo,
					dataFinePeriodo);
		}

		logger.debug("--> Exit caricaLiquidazioneIva");
		return liquidazioneIvaDTO;
	}

	@Override
	public GiornaleIva caricaGiornaleIvaDefinitivo(int anno, int mese) throws ContabilitaException {
		logger.debug("--> Enter caricaGiornaleIvaDefinitivo");

		Query query = panjeaDAO.prepareNamedQuery("GiornaleIva.caricaDefinitivoByAnnoEMese");
		query.setParameter("paramAnno", anno);
		query.setParameter("paramMese", mese);
		query.setParameter("paramCodiceAzienda", getAzienda());

		GiornaleIva giornale = null;
		try {
			giornale = (GiornaleIva) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> Nessun giornale iva trovato con anno = " + anno + " e mese = " + mese);
			return null;
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento del giornale iva.", e);
			throw new ContabilitaException("Errore durante il caricamento del giornale iva.", e);
		}

		logger.debug("--> Exit caricaGiornaleIvaDefinitivo");
		return giornale;
	}

	/**
	 * Metodo che carica se esite il giornale per il mese specificato altrimenti viengono caricati i valori definiti
	 * dalle preferenze della contabilita'.
	 * 
	 * @param contabilitaSettings
	 *            contabilitaSettings
	 * @param anno
	 *            anno
	 * @param mese
	 *            mese
	 * @return {@link GiornaleIva}
	 */
	private GiornaleIva caricaParametriGiornaleIvaDefinitivo(ContabilitaSettings contabilitaSettings, int anno, int mese) {
		logger.debug("--> Enter caricaMinimaleIva");

		GiornaleIva giornaleIvaResult = new GiornaleIva();

		List<GiornaleIva> giornaliIva = registroIvaManager.caricaGiornaliIva(anno, mese);
		for (GiornaleIva giornaleIva : giornaliIva) {
			if (giornaleIva.getRegistroIva().getTipoRegistro().equals(TipoRegistro.RIEPILOGATIVO)) {
				giornaleIvaResult = giornaleIva;
			}
		}

		if (giornaleIvaResult.getMinimaleIVA() == null) {
			giornaleIvaResult.setMinimaleIVA(contabilitaSettings.getMinimaleIVA());
		}

		if (giornaleIvaResult.getPercTrimestraleValore() == null) {
			giornaleIvaResult.setPercTrimestraleValore(BigDecimal.ZERO);
			if (contabilitaSettings.getTipoPeriodicita() == ETipoPeriodicita.TRIMESTRALE) {
				giornaleIvaResult.setPercTrimestraleValore(contabilitaSettings.getPercTrimestrale());
			}
		}

		return giornaleIvaResult;
	}

	@Override
	public List<RegistroLiquidazioneDTO> caricaRegistriLiquidazione(Integer anno) throws TipoDocumentoBaseException {
		logger.debug("--> Enter caricaRegistroLiquidazione");

		ContabilitaSettings contabilitaSettings = contabilitaSettingsManager.caricaContabilitaSettings();

		List<RegistroLiquidazioneDTO> registri = new ArrayList<RegistroLiquidazioneDTO>();
		// aggiungo 12 righe vuote sia per i mesi o per i trimestri...in caso ne
		// uso 4
		for (int i = 0; i < 12; i++) {
			registri.add(new RegistroLiquidazioneDTO());
		}

		// Carico le aree contabili e le inserisco nel DTO
		List<AreaContabileDTO> areeContabiliDTO = areaContabileManager.ricercaAreaContabilePerLiquidazione(anno);
		for (AreaContabileDTO areaContabileDTO : areeContabiliDTO) {
			// Trovo il mese del documento e lo inserisco nella posizione
			// corretta dell'array
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(areaContabileDTO.getDataDocumento());
			switch (contabilitaSettings.getTipoPeriodicita()) {
			case TRIMESTRALE:
				calendar.add(Calendar.MONTH, -2);
				break;
			case ANNUALE:
				calendar.add(Calendar.MONTH, -12);
				break;
			default:
				break;
			}
			registri.get(calendar.get(Calendar.MONTH)).setAreaContabileDTO(areaContabileDTO);
		}

		// Carico i registri Iva per l'anno
		// e li inserisco nella lista
		List<GiornaleIva> giornaliIva = registroIvaManager.caricaGiornaliIvaRiepilogativi(anno);
		for (GiornaleIva giornaleIva : giornaliIva) {
			int meseJava = giornaleIva.getMese() - 1;
			if (meseJava >= 0) {
				registri.get(meseJava).setGiornaleIva(giornaleIva);
			} else {
				// se ho un mese negativo ho il registro annuale, il mese per il registro annuale è -1
				RegistroLiquidazioneDTO registroAnnualeDTO = new RegistroLiquidazioneDTO();
				registroAnnualeDTO.setGiornaleIva(giornaleIva);
				registri.add(registroAnnualeDTO);
			}
		}
		logger.debug("--> Exit caricaRegistroLiquidazione");
		return registri;
	}

	/**
	 * Carica il totale dei pagamenti, raggruppati per tipo documento, dei documenti incasso pagamento associati alle
	 * rate dei documenti di liquidazione.
	 * 
	 * @param dataInizio
	 *            data inizio periodo
	 * @param dataFine
	 *            data fine periodo
	 * @return List<Object[]> 0:id tipo doc; 1:cod tipo doc; 2:desc tipo doc; 3:totale
	 */
	@SuppressWarnings("unchecked")
	private List<Object[]> caricaTotaliPagamentiLiquidazione(Date dataInizio, Date dataFine) {
		StringBuffer hqlAreeTesoreria = new StringBuffer();
		// via area pagamenti
		hqlAreeTesoreria
				.append("select distinct td.id,td.codice,td.descrizione,sum(p.importo.importoInValutaAzienda) ");
		hqlAreeTesoreria.append("from AreaTesoreria a ");
		hqlAreeTesoreria.append("inner join a.documento d ");
		hqlAreeTesoreria.append("inner join d.tipoDocumento td ");
		hqlAreeTesoreria.append("inner join a.pagamenti p ");
		hqlAreeTesoreria.append("inner join p.rata r ");
		hqlAreeTesoreria.append("where r.tipoPagamento=5 ");
		hqlAreeTesoreria.append("and d.codiceAzienda=:paramCodiceAzienda ");
		hqlAreeTesoreria.append("and d.dataDocumento>=:daData ");
		hqlAreeTesoreria.append("and d.dataDocumento<=:aData ");
		hqlAreeTesoreria.append("group by td.codice ");

		Query query = panjeaDAO.prepareQuery(hqlAreeTesoreria.toString());
		query.setParameter("daData", dataInizio);
		query.setParameter("aData", dataFine);
		query.setParameter("paramCodiceAzienda", getAzienda());

		List<Object[]> pagamentiConTotale;
		try {
			pagamentiConTotale = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("Errore nella ricerca dei pagamenti per la liquidazione annuale", e);
			throw new RuntimeException("Errore nella ricerca dei pagamenti per la liquidazione annuale", e);
		}
		return pagamentiConTotale;
	}

	/**
	 * Calcola ed inserisce un registro fittizzio per il prorata.
	 * 
	 * @param registroIva
	 *            registro acquisto per calcolare il prorata
	 * @param totaleRegistro
	 *            importo totale del registro
	 * @param liquidazioneIvaDTO
	 *            dto da aggiornare
	 * @param dataInizioPeriodo
	 *            data inizio periodo liquidazione
	 */
	private void creaRegistroProRata(RegistroIva registroIva, BigDecimal totaleRegistro,
			LiquidazioneIvaDTO liquidazioneIvaDTO, Date dataInizioPeriodo) {
		Calendar annoCalendar = Calendar.getInstance();
		annoCalendar.setTime(dataInizioPeriodo);
		int anno = annoCalendar.get(Calendar.YEAR);
		ProRataSetting proRataSetting = contabilitaSettingsManager.caricaContabilitaSettings().getProRataSetting(anno,
				registroIva);
		if (proRataSetting != null && !proRataSetting.getPercentuale().equals(BigDecimal.ZERO)) {
			RegistroIva registroIvaProRata = new RegistroIva();
			BigDecimal importoProRata = totaleRegistro.multiply(proRataSetting.getPercentuale())
					.divide(Importo.HUNDRED).negate();
			registroIvaProRata.setDescrizione("Pro rata " + proRataSetting.getPercentuale() + "% per registro "
					+ registroIva.getDescrizione() + " ");
			registroIva.setDescrizioneRegistroProRata(registroIvaProRata.getDescrizione());
			registroIvaProRata.setNome("nome" + registroIva.getNome());
			registroIvaProRata.setTipoRegistro(TipoRegistro.ACQUISTO);
			liquidazioneIvaDTO.addToTotali(registroIvaProRata, importoProRata);
			liquidazioneIvaDTO.setImportoProRata(liquidazioneIvaDTO.getImportoProRata().add(importoProRata.abs()));
		}
	}

	/**
	 * 
	 * @return codice dell'azienda per l'utente loggato
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	public boolean isDocumentoLiquidazione(Documento documento) {
		boolean isLiquidazione = false;
		TipoDocumento tipoDoc = documento.getTipoDocumento();
		try {
			TipoAreaContabile tipoAreaLiquidazione = null;
			try {
				tipoAreaLiquidazione = tipiAreaContabileManager
						.caricaTipoAreaContabilePerTipoOperazione(TipoOperazioneTipoDocumento.LIQUIDAZIONE_IVA);
			} catch (TipoDocumentoBaseException e) {
				logger.warn("--> tipoDocumento base di Liquidazione non impostato");
			}
			if (tipoAreaLiquidazione != null) {
				TipoDocumento tipoDocLiquidazione = tipoAreaLiquidazione.getTipoDocumento();
				if (tipoDoc.equals(tipoDocLiquidazione)) {
					isLiquidazione = true;
				}
			}
		} catch (Exception e) {
			logger.error("--> errore nel caricare i tipi documento base per trovare il tipo liquidazione", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> trovato documento di liquidazione " + isLiquidazione);
		return isLiquidazione;
	}

	@Override
	public AreaContabileFullDTO salvaDocumentoLiquidazione(AreaContabile areaContabile)
			throws AreaContabileDuplicateException, DocumentoDuplicateException, FormulaException,
			CodicePagamentoNonTrovatoException, ContoRapportoBancarioAssenteException,
			TipoDocumentoNonConformeException {
		logger.debug("--> Enter salvaDocumentoLiquidazione");
		AreaContabile areaContabileSalvata = null;
		AreaContabileFullDTO areaContabileFullDTO = null;
		try {
			// verifico il codice pagamento se non e' impostato evito di salvare
			// areacontabile e righe,
			// e' cosi' piu' veloce e posso confermare l'area contabile
			CodicePagamento codicePagamento = null;
			try {
				codicePagamento = codicePagamentoManager.caricaCodicePagamento(TipologiaPartita.LIQUIDAZIONE);
			} catch (CodicePagamentoNonTrovatoException e) {
				logger.warn("--> codicePagamento per LIQUIDAZIONE non trovato");
				throw e;
			}

			// salva l'area contabile
			areaContabileSalvata = areaContabileManager.salvaAreaContabile(areaContabile, true);
			// carica le contropartite
			List<ControPartita> contropartite = strutturaContabileManager
					.caricaControPartiteConImporto(areaContabileSalvata);
			// genera le righe contabili
			List<RigaContabile> righeContabili = strutturaContabileManager.creaRigheContabili(areaContabileSalvata,
					contropartite);

			// crea area partite per F24 tributo IVA
			AreaRate areaRate = liquidazionePagamentiManager.creaAreaRatePerDocumentoLiquidazione(areaContabile,
					codicePagamento);

			String user = ((JecPrincipal) context.getCallerPrincipal()).getUserName();
			areaContabileSalvata.setValidRigheContabili(true);
			areaContabileSalvata.setValidDataRigheContabili(Calendar.getInstance().getTime());
			areaContabileSalvata.setValidUtenteRigheContabili(user);
			areaContabileSalvata.setStatoAreaContabile(StatoAreaContabile.CONFERMATO);
			areaContabileSalvata = areaContabileManager.salvaAreaContabileNoCheck(areaContabileSalvata);

			// faccio una nuova areaContabileFullDTO da restituire
			areaContabileFullDTO = new AreaContabileFullDTO();
			areaContabileFullDTO.setAreaContabile(areaContabileSalvata);
			areaContabileFullDTO.setRigheContabili(righeContabili);
			areaContabileFullDTO.setAreaRate(areaRate);

		} catch (TipoDocumentoNonConformeException e) {
			logger.warn("--> TipoDocumentoNonConformeException");
			context.setRollbackOnly();
			throw e;
		} catch (CodicePagamentoNonTrovatoException e) {
			logger.warn("--> CodicePagamentoNonTrovatoException");
			context.setRollbackOnly();
			throw e;
		} catch (AreaContabileDuplicateException e) {
			logger.warn("--> AreaContabileDuplicateException");
			context.setRollbackOnly();
			throw e;
		} catch (DocumentoDuplicateException e) {
			logger.warn("--> DocumentoDuplicateException");
			context.setRollbackOnly();
			throw e;
		} catch (ContoRapportoBancarioAssenteException e) {
			logger.warn("--> ContoRapportoBancarioAssenteException");
			context.setRollbackOnly();
			throw e;
		} catch (FormulaException e) {
			logger.error("--> FormulaException");
			context.setRollbackOnly();
			throw e;
		} catch (Exception e) {
			logger.error("--> Errore nella generazione del documento di liquidazione", e);
			throw new RuntimeException("--> Errore nella generazione del documento di liquidazione", e);
		}
		logger.debug("--> Exit salvaDocumentoLiquidazione");
		return areaContabileFullDTO;
	}
}
