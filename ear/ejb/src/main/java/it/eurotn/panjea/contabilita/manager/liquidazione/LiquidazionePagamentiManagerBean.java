package it.eurotn.panjea.contabilita.manager.liquidazione;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.service.exception.TipoDocumentoNonConformeException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings.ETipoPeriodicita;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.manager.LiquidazioneIvaManagerBean;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.contabilita.manager.interfaces.LiquidazioneIvaManager;
import it.eurotn.panjea.contabilita.manager.liquidazione.interfaces.LiquidazionePagamentiManager;
import it.eurotn.panjea.contabilita.util.RegistroLiquidazioneDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoNonTrovatoException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.rate.manager.interfaces.RateGenerator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.domain.Pagamento.TipoPagamentoDocumento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccontoManager;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti.EStatoAcconto;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.joda.time.DateTime;

@Stateless(name = "Panjea.LiquidazionePagamentiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LiquidazionePagamentiManager")
public class LiquidazionePagamentiManagerBean implements LiquidazionePagamentiManager {

	private static Logger logger = Logger.getLogger(LiquidazioneIvaManagerBean.class.getName());

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private AreaRateManager areaRateManager;

	@EJB
	private RateGenerator rateGenerator;

	@EJB
	private TipiAreaPartitaManager tipiAreaPartitaManager;

	@EJB
	private ContabilitaSettingsManager contabilitaSettingsManager;

	@EJB(mappedName = "Panjea.AreaContabileManager")
	private AreaContabileManager areaContabileManager;

	@EJB
	private AreaAccontoManager areaAccontoManager;

	@EJB
	@IgnoreDependency
	private LiquidazioneIvaManager liquidazioneIvaManager;

	@Override
	public void cancellaPagamentoAccontoLiquidazione(Pagamento pagamento) {
		boolean isPagamentoAcconto = pagamento.getTipoPagamentoDocumento().equals(TipoPagamentoDocumento.ACCONTO);
		AreaRate areaRate = pagamento.getRata().getAreaRate();
		TipologiaPartita tipologiaPartita = areaRate.getCodicePagamento().getTipologiaPartita();
		boolean isAreaRateLiquidazione = tipologiaPartita.equals(TipologiaPartita.LIQUIDAZIONE);

		if (!(isPagamentoAcconto && isAreaRateLiquidazione)) {
			throw new IllegalArgumentException("Il pagamento per il documento non può essere cancellato "
					+ pagamento.getTipoPagamentoDocumento().toString());
		}
		try {
			panjeaDAO.delete(pagamento);
		} catch (DAOException e) {
			logger.error("--> impossibile cancellare il pagamento", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void cancellaPagamentoLiquidazionePrecedente(Documento documento) {
		if (liquidazioneIvaManager.isDocumentoLiquidazione(documento)) {
			AreaContabile areaContabile = areaContabileManager.caricaAreaContabileByDocumento(documento.getId());
			// Carico rata precedente riferimento data reg della liq attuale
			Date dataRegistrazione = areaContabile.getDataRegistrazione();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataRegistrazione);
			int mese = cal.get(Calendar.MONTH) + 1;
			int anno = cal.get(Calendar.YEAR);
			Rata rataPrec = null;
			try {
				rataPrec = caricaRataLiquidazionePrecedente(mese, anno);
			} catch (TipoDocumentoBaseException e) {
				logger.warn("--> tipoDocumento base di Liquidazione non impostato");
			}
			if (rataPrec != null) {
				Set<Pagamento> pagamenti = rataPrec.getPagamenti();
				for (Pagamento pagamento : pagamenti) {
					try {
						// devo cancellare solo il pagamento generato dalla liquidazione quindi quello senza una area
						// collegata
						if (pagamento.getTipoPagamentoDocumento().equals(TipoPagamentoDocumento.LIQUIDAZIONE)) {
							panjeaDAO.delete(pagamento);
						}
					} catch (Exception e) {
						logger.error("--> impossibile cancellare il pagamento", e);
						throw new RuntimeException(e);
					}
				}
			}
		} else {
			logger.debug("--> non è un documento di liquidazione");
		}
	}

	@Override
	public Rata caricaRataLiquidazionePrecedente(int mese, int anno) throws TipoDocumentoBaseException {
		logger.debug(">>>>>>>>> MESE " + mese + " >>>>>>> ANNO" + anno);

		int mesePrec = mese;
		int annoPrec = anno;
		// carico da contabilita' settings il tipo periodicita' scelto
		// dall'utente
		ETipoPeriodicita tipoPeriodicita = contabilitaSettingsManager.caricaContabilitaSettings().getTipoPeriodicita();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, mese - 1);
		cal.set(Calendar.YEAR, anno);
		switch (tipoPeriodicita) {
		case TRIMESTRALE:
			cal.add(Calendar.MONTH, -3);
			mesePrec = cal.get(Calendar.MONTH);
			annoPrec = cal.get(Calendar.YEAR);
			break;
		case ANNUALE:
			annoPrec = annoPrec - 1;
			break;
		default:
			cal.add(Calendar.MONTH, -1);
			mesePrec = cal.get(Calendar.MONTH);
			annoPrec = cal.get(Calendar.YEAR);
			break;
		}

		logger.debug(">>>>>>>>>DOPO MESE " + mesePrec + " >>>>>>>DOPO ANNO" + annoPrec);
		List<RegistroLiquidazioneDTO> registri = liquidazioneIvaManager.caricaRegistriLiquidazione(annoPrec);
		RegistroLiquidazioneDTO registroMese = registri.get(mesePrec);
		Rata rata = null;
		if (registroMese.getAreaContabileDTO().getId() != null) {
			AreaContabile areaContabile = areaContabileManager.caricaAreaContabile(registroMese.getAreaContabileDTO()
					.getId());
			AreaRate areaRate = areaRateManager.caricaAreaRate(areaContabile.getDocumento());
			logger.debug("--> >>>>>>> " + areaRate.getRate());
			if (areaRate.getRate() != null && !areaRate.getRate().isEmpty()) {
				rata = (Rata) areaRate.getRate().toArray()[0];
			}
		}
		return rata;
	}

	@Override
	public BigDecimal caricaTotaleAccontoIvaDicembre(ETipoPeriodicita periodicitaLiquidazione, Date dataInizioPeriodo,
			Date dataFinePeriodo) {

		BigDecimal tot = null;
		boolean ricercaAccontoIva = isPeriodoPerAccontoIva(periodicitaLiquidazione, dataInizioPeriodo);

		if (ricercaAccontoIva) {
			ParametriRicercaAcconti parametri = new ParametriRicercaAcconti();
			parametri.setADataDocumento(dataFinePeriodo);
			parametri.setDaDataDocumento(dataInizioPeriodo);
			parametri.setEffettuaRicerca(true);
			parametri.setStatoAcconto(EStatoAcconto.TUTTI);
			// so che al momento il tipo entita' azienda crea solamente degli
			// acconti iva
			// TODO con il refactoring che utilizzera' la struttura contabile
			// per la generazione bisognera'
			// differenziare gli acconti iva da tutti gli altri acconti che
			// hanno tipo entita' azienda
			parametri.setTipoEntita(TipoEntita.AZIENDA);
			List<AreaAcconto> accontiIva = areaAccontoManager.caricaAreaAcconti(parametri);
			tot = getTotaleAcconti(accontiIva);
		}
		return tot;
	}

	@Override
	public AreaRate creaAreaRatePerDocumentoLiquidazione(AreaContabile areaContabile, CodicePagamento codicePagamento)
			throws CodicePagamentoNonTrovatoException, TipoDocumentoNonConformeException {
		logger.debug("--> Enter creaAreaPartite");
		TipoDocumento tipoDocumento = areaContabile.getDocumento().getTipoDocumento();
		TipoAreaPartita tipoAreaPartita = tipiAreaPartitaManager.caricaTipoAreaPartitaPerTipoDocumento(tipoDocumento);

		// se non ho il tipoAreaPartite devo bloccare l'utente
		if (tipoAreaPartita.isNew()) {
			logger.warn("--> non e' impostato il TipoAreaPartita per il tipo documento di LIQUIDAZIONE");
			throw new TipoDocumentoNonConformeException(tipoDocumento, "tipo area partite non presente");
		}

		AreaRate areaRate = new AreaRate();
		areaRate.setDocumento(areaContabile.getDocumento());
		areaRate.setCodicePagamento(codicePagamento);
		areaRate.setSpeseIncasso(BigDecimal.ZERO);
		areaRate.setTipoAreaPartita(tipoAreaPartita);
		areaRate.getDatiValidazione().valida(((JecPrincipal) context.getCallerPrincipal()).getUserName());
		areaRate = areaRateManager.salvaAreaRate(areaRate);
		areaRate = rateGenerator.generaRate(areaContabile, areaRate);

		ContabilitaSettings contabilitaSettings = contabilitaSettingsManager.caricaContabilitaSettings();
		ETipoPeriodicita tipoPeriodicita = contabilitaSettings.getTipoPeriodicita();
		Date dataRegistrazione = areaContabile.getDataRegistrazione();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataRegistrazione);

		// adatto la data di inizio periodo a seconda del tipo periodicita'
		Date daData = null;
		Date aData = null;

		int mesePrec = cal.get(Calendar.MONTH) + 1;
		int annoPrec = cal.get(Calendar.YEAR);

		// Carico rata precedente riferimento data reg della liq attuale
		Rata rataPrec = null;
		try {
			rataPrec = caricaRataLiquidazionePrecedente(mesePrec, annoPrec);
		} catch (TipoDocumentoBaseException e) {
			throw new TipoDocumentoNonConformeException(areaContabile.getDocumento().getTipoDocumento(), e.getMessage());
		}

		switch (tipoPeriodicita) {
		case TRIMESTRALE:
			// daData.set(cal.get(Calendar.YEAR), Calendar.OCTOBER, 01, 0, 0, 0);
			// aData.set(cal.get(Calendar.YEAR), Calendar.DECEMBER, 31, 0, 0, 0);
			daData = PanjeaEJBUtil.getQuarterStartDate(new DateTime(cal.getTime())).toDate();
			aData = PanjeaEJBUtil.getQuarterEnddate(new DateTime(cal.getTime())).toDate();
			cal.add(Calendar.MONTH, -3);
			mesePrec = cal.get(Calendar.MONTH) + 1;
			annoPrec = cal.get(Calendar.YEAR);
			break;
		case ANNUALE:
			mesePrec = mesePrec - 1;
			annoPrec = annoPrec - 1;
			break;
		default:
			daData = new DateTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 01, 0, 0, 0).toDate();
			aData = new DateTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
					cal.getActualMaximum(Calendar.DAY_OF_MONTH), 0, 0, 0).toDate();

			cal.add(Calendar.MONTH, -1);
			mesePrec = cal.get(Calendar.MONTH) + 1;
			annoPrec = cal.get(Calendar.YEAR);
			break;
		}

		// Se esiste un acconto iva e se rientro nel mese di dicembre/4to
		// trimestre, pago la rata per l'importo dell'acconto
		boolean ricercaAccontoIva = isPeriodoPerAccontoIva(tipoPeriodicita, daData);
		if (ricercaAccontoIva) {
			pagaAccontoIva(areaRate, tipoPeriodicita, daData, aData);
		}

		BigDecimal minimaleIvaPrec = BigDecimal.ZERO;
		try {
			GiornaleIva giornaleIvaPrec = liquidazioneIvaManager.caricaGiornaleIvaDefinitivo(annoPrec, mesePrec);
			if (giornaleIvaPrec != null) {
				minimaleIvaPrec = giornaleIvaPrec.getMinimaleIVA();
			}
		} catch (Exception e1) {
			logger.error("--> errore durante il caricamento del giornale iva precedentre", e1);
			throw new RuntimeException("errore durante il caricamento del giornale iva precedentre", e1);
		}

		if (rataPrec != null && rataPrec.getResiduo().getImportoInValuta().compareTo(BigDecimal.ZERO) != 0
				&& rataPrec.getImporto().getImportoInValuta().compareTo(minimaleIvaPrec) <= 0) {
			Pagamento pagamento = new Pagamento();
			pagamento.setRata(rataPrec);
			Importo importoPag = rataPrec.getResiduoConSegno();
			Importo impForzato = new Importo();
			impForzato.setCodiceValuta(importoPag.getCodiceValuta());

			pagamento.setChiusuraForzataRata(false);
			pagamento.setDataPagamento(dataRegistrazione);
			pagamento.setImporto(importoPag);
			pagamento.setImportoForzato(impForzato);

			try {
				panjeaDAO.save(pagamento);
			} catch (Exception e) {
				logger.error("--> errore in creaAreaRatePerDocumentoLiquidazione", e);
				throw new RuntimeException();
			}
		}
		logger.debug("--> Exit creaAreaPartite");
		return areaRate;
	}

	/**
	 * Data la lista di acconti restituisce la somma del totale di ogni documento.
	 * 
	 * @param accontiIva
	 *            la lista di acconti di cui sommare il totale
	 * @return 0 se la lista risulta vuota o la somma del totale di tutti i documenti presenti
	 */
	private BigDecimal getTotaleAcconti(List<AreaAcconto> accontiIva) {
		BigDecimal tot = BigDecimal.ZERO;
		if (accontiIva != null && accontiIva.size() > 0) {
			for (AreaAcconto areaAcconto : accontiIva) {
				Importo totAcconto = areaAcconto.getDocumento().getTotale();
				tot = tot.add(totAcconto.getImportoInValutaAzienda());
			}
		}
		return tot;
	}

	/**
	 * Verifica se il periodo e' adeguato per la verifica della presenza di un acconto IVA. se la periodicita' e'
	 * mensile devo verificare se il periodo richiesto e' nel mese di dicembre altrimenti nel 4to trimenstre.
	 * 
	 * @param periodicitaLiquidazione
	 *            tipo periodicita' impostato nelle preferenze della contabilita'
	 * @param dataInizioPeriodo
	 *            data inizio periodo richiesta dall'utente
	 * @return true o false
	 */
	private boolean isPeriodoPerAccontoIva(ETipoPeriodicita periodicitaLiquidazione, Date dataInizioPeriodo) {
		boolean ricercaAccontoIva = false;
		Calendar calInizioPeriodo = Calendar.getInstance();
		if (dataInizioPeriodo != null) {
			calInizioPeriodo.setTime(dataInizioPeriodo);
		}
		int monthInizioPeriodo = calInizioPeriodo.get(Calendar.MONTH);
		switch (periodicitaLiquidazione) {
		case MENSILE:
			if (monthInizioPeriodo == Calendar.DECEMBER) {
				ricercaAccontoIva = true;
			}
			break;
		case TRIMESTRALE:
			if (monthInizioPeriodo == Calendar.OCTOBER) {
				ricercaAccontoIva = true;
			}
			break;
		default:
			break;
		}
		return ricercaAccontoIva;
	}

	/**
	 * Paga, se presente, l'importo della rata pari al valore dell'acconto iva.
	 * 
	 * @param areaRate
	 *            l'area rate da cui recupero la rata da pagare
	 * @param periodicitaLiquidazione
	 *            tipo periodicita' della liquidazione
	 * @param dataInizioPeriodo
	 *            data inizio periodo da considerare per la ricerca di acconti
	 * @param dataFinePeriodo
	 *            data fine periodo da considerare per la ricerca di acconti
	 */
	private void pagaAccontoIva(AreaRate areaRate, ETipoPeriodicita periodicitaLiquidazione, Date dataInizioPeriodo,
			Date dataFinePeriodo) {
		Set<Rata> rate = areaRate.getRate();

		if (rate.size() == 0) {
			return;
		}

		ParametriRicercaAcconti parametriRicercaAcconti = new ParametriRicercaAcconti();
		parametriRicercaAcconti.setEffettuaRicerca(true);
		parametriRicercaAcconti.setTipoEntita(TipoEntita.AZIENDA);
		parametriRicercaAcconti.setStatoAcconto(EStatoAcconto.APERTO);
		parametriRicercaAcconti.setDaDataDocumento(dataInizioPeriodo);
		parametriRicercaAcconti.setADataDocumento(dataFinePeriodo);

		Rata rata = rate.iterator().next();

		List<AreaAcconto> accontiIva = areaAccontoManager.caricaAreaAcconti(parametriRicercaAcconti);
		for (AreaAcconto areaAcconto : accontiIva) {
			Pagamento pagamento = new Pagamento();
			pagamento.setAreaAcconto(areaAcconto);
			pagamento.setDataCreazione(new Date());
			pagamento.setDataPagamento(areaAcconto.getDocumento().getDataDocumento());
			pagamento.setRata(rata);

			Importo importoPag = areaAcconto.getDocumento().getTotale();
			Importo impForzato = new Importo();
			impForzato.setCodiceValuta(importoPag.getCodiceValuta());

			pagamento.setChiusuraForzataRata(false);
			pagamento.setImporto(importoPag);
			pagamento.setImportoForzato(impForzato);

			try {
				panjeaDAO.save(pagamento);
			} catch (Exception e) {
				logger.error("--> errore in creaAreaRatePerDocumentoLiquidazione", e);
				throw new RuntimeException(e);
			}
		}
	}
}
