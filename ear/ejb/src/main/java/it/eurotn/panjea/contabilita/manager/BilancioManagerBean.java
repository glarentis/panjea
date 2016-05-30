package it.eurotn.panjea.contabilita.manager;

import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.BilancioManager;
import it.eurotn.panjea.contabilita.manager.interfaces.SaldoManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancioConfronto;
import it.eurotn.panjea.contabilita.util.SaldoConti;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.contabilita.util.SaldoContoConfronto;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.exception.PeriodoException;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.BilancioManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.BilancioManager")
public class BilancioManagerBean implements BilancioManager {

	private static Logger logger = Logger.getLogger(BilancioManagerBean.class);

	@Resource
	private SessionContext context;

	@EJB
	private SaldoManager saldoManager;

	@EJB
	private AziendeManager aziendeManager;

	@Override
	public SaldoConti caricaBilancio(ParametriRicercaBilancio parametriRicercaBilancio)
			throws TipoDocumentoBaseException, ContabilitaException {
		logger.debug("--> Enter caricaBilancio");

		// Carico l'aziendaLite per trovare la data di inizio Esercizio
		AziendaLite aziendaLite;
		try {
			aziendaLite = aziendeManager.caricaAzienda(getAzienda());
		} catch (AnagraficaServiceException e) {
			logger.error("-->Errore nel caricare l'azienda corrente", e);
			throw new ContabilitaException(e);
		}
		List<StatoAreaContabile> stati = new ArrayList<AreaContabile.StatoAreaContabile>();
		stati.addAll(parametriRicercaBilancio.getStatiAreaContabile());
		// carico i saldi del periodo richiesto
		List<SaldoConto> sottocontiDTOPeriodo = saldoManager.calcoloSaldi(null, parametriRicercaBilancio
				.getCentroCosto(), parametriRicercaBilancio.getDataRegistrazione().getDataIniziale(),
				parametriRicercaBilancio.getDataRegistrazione().getDataFinale(), parametriRicercaBilancio
						.getAnnoCompetenza(), aziendaLite, stati, false, parametriRicercaBilancio
						.getStampaCentriCosto());

		Date dataInizioEsercizio = aziendaLite.getDataInizioEsercizio(parametriRicercaBilancio.getAnnoCompetenza());

		SaldoConti saldoConti = new SaldoConti();
		saldoConti.sommaSaldi(sottocontiDTOPeriodo);

		// se la data inizio è null voglio il bilancio per l'anno intero.
		boolean aggiungiSaldoIniziale = parametriRicercaBilancio.getDataRegistrazione().getDataIniziale() == null;
		// se la data non è null allora verifico se ho richiesto il saldo da
		// inizio anno contabile. Anche in questo
		// caso devo calcolare il saldo di inizio anno
		if (!aggiungiSaldoIniziale) {
			aggiungiSaldoIniziale = dataInizioEsercizio.compareTo(parametriRicercaBilancio.getDataRegistrazione()
					.getDataIniziale()) == 0;
		}
		// aggiungo il saldo iniziale solo se non e' attiva la ricerca per centro di costo
		if (aggiungiSaldoIniziale) {
			aggiungiSaldoIniziale = aggiungiSaldoIniziale && parametriRicercaBilancio.getCentroCosto() == null;
		}

		if (aggiungiSaldoIniziale) {
			SaldoConti saldoInizialePartimoniale = saldoManager.calcolaSaldiInizioAnno(TipoConto.PATRIMONIALE, null,
					parametriRicercaBilancio.getCentroCosto(), parametriRicercaBilancio.getAnnoCompetenza(),
					aziendaLite);
			saldoConti.sommaSaldi(saldoInizialePartimoniale);

			SaldoConti saldoInizialeEconomico = saldoManager.calcolaSaldiInizioAnno(TipoConto.ECONOMICO, null,
					parametriRicercaBilancio.getCentroCosto(), parametriRicercaBilancio.getAnnoCompetenza(),
					aziendaLite);
			saldoConti.sommaSaldi(saldoInizialeEconomico);

			SaldoConti saldoInizialeOrdine = saldoManager.calcolaSaldiInizioAnno(TipoConto.ORDINE, null,
					parametriRicercaBilancio.getCentroCosto(), parametriRicercaBilancio.getAnnoCompetenza(),
					aziendaLite);
			saldoConti.sommaSaldi(saldoInizialeOrdine);
		}

		// Se devo raggruppo per entita
		if (!parametriRicercaBilancio.getStampaClienti() || !parametriRicercaBilancio.getStampaFornitori()) {
			saldoConti = raggruppaSottoContiEntita(saldoConti, parametriRicercaBilancio.getStampaClienti(),
					parametriRicercaBilancio.getStampaFornitori());
		}

		if (!parametriRicercaBilancio.isVisualizzaSaldiCFAzero() || !parametriRicercaBilancio.getStampaConti()) {
			// rimuovo i saldi a 0 se non devo visualizzarli
			saldoConti = rimuoviSaldi(saldoConti, !parametriRicercaBilancio.isVisualizzaSaldiCFAzero(),
					!parametriRicercaBilancio.getStampaConti(), !parametriRicercaBilancio.getStampaClienti(),
					!parametriRicercaBilancio.getStampaFornitori());
		}

		return saldoConti;
	}

	@Override
	public SaldoConti caricaBilancioAnnuale(Integer annoEsercizio) throws TipoDocumentoBaseException,
			ContabilitaException {
		ParametriRicercaBilancio parametriRicercaBilancio = new ParametriRicercaBilancio();
		parametriRicercaBilancio.getDataRegistrazione().setDataFinale(null);
		parametriRicercaBilancio.getDataRegistrazione().setDataIniziale(null);
		parametriRicercaBilancio.setAnnoCompetenza(annoEsercizio);
		parametriRicercaBilancio.setStampaClienti(true);
		parametriRicercaBilancio.setStampaFornitori(true);
		parametriRicercaBilancio.setVisualizzaSaldiCFAzero(false);
		List<StatoAreaContabile> statiAreaContabile = new ArrayList<StatoAreaContabile>();
		statiAreaContabile.add(StatoAreaContabile.VERIFICATO);
		statiAreaContabile.add(StatoAreaContabile.CONFERMATO);
		return caricaBilancio(parametriRicercaBilancio);
	}

	@Override
	public List<SaldoContoConfronto> caricaBilancioConfronto(
			ParametriRicercaBilancioConfronto parametriRicercaBilancioConfronto) throws TipoDocumentoBaseException,
			ContabilitaException {

		// controllo che le date di inizio e fine siano settate per entrambi i periodi
		Periodo periodo1 = parametriRicercaBilancioConfronto.getDataRegistrazione();
		Periodo periodo2 = parametriRicercaBilancioConfronto.getDataRegistrazione2();
		if (periodo1.getDataIniziale() == null || periodo1.getDataFinale() == null
				|| periodo2.getDataIniziale() == null || periodo2.getDataFinale() == null) {
			logger.error("--> errore. Date iniziali o finali non corrette. ");
			throw new PeriodoException("Date iniziali o finali non specificate per i periodi.");
		}

		List<SaldoContoConfronto> bilancioConfronto = new ArrayList<SaldoContoConfronto>();

		SaldoConti bilancio1 = caricaBilancio(parametriRicercaBilancioConfronto.getParametriRicercaBilancio1());
		SaldoConti bilancio2 = caricaBilancio(parametriRicercaBilancioConfronto.getParametriRicercaBilancio2());

		Map<String, SaldoContoConfronto> bilancioAConfrontoMap = new HashMap<String, SaldoContoConfronto>();

		// ciclo sul primo bilancio e creo le righe a confronto
		for (SaldoConto saldoConto : bilancio1.values()) {
			SaldoContoConfronto saldoContoConfronto = new SaldoContoConfronto(saldoConto, true);
			String keyConto = saldoConto.getSottoContoCodiceCompleto();
			bilancioAConfrontoMap.put(keyConto, saldoContoConfronto);
		}

		// ciclo sui conti del saldo2. Se esistono in saldo1 aggiungo i valori,
		// altrimenti li aggiungo
		for (SaldoConto saldoConto2 : bilancio2.values()) {
			String keyConto2 = saldoConto2.getSottoContoCodiceCompleto();

			SaldoContoConfronto saldoContoInBilancio = null;
			if (bilancioAConfrontoMap.containsKey(keyConto2)) {
				saldoContoInBilancio = bilancioAConfrontoMap.get(keyConto2);
				saldoContoInBilancio.aggiungiImportoAvere2(saldoConto2.getImportoAvere());
				saldoContoInBilancio.aggiungiImportoDare2(saldoConto2.getImportoDare());
				saldoContoInBilancio.aggiungiImporti2CentriCosto(saldoConto2.getCentriCosto());
			} else {
				saldoContoInBilancio = new SaldoContoConfronto(saldoConto2, false);
			}
			bilancioAConfrontoMap.put(keyConto2, saldoContoInBilancio);
		}

		Collection<SaldoContoConfronto> righeBilancioAContronto = bilancioAConfrontoMap.values();
		bilancioConfronto.addAll(righeBilancioAContronto);
		Collections.sort(bilancioConfronto);

		return bilancioConfronto;
	}

	/**
	 * 
	 * @return codice dell' azienda.
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	/**
	 * raggruppa i sottoconti dell'entità.
	 * 
	 * @param saldoConti
	 *            saldo i conti
	 * @param stampaClienti
	 *            indica se stampare i clienti
	 * @param stampaFornitori
	 *            indica se stampare i fornitori
	 * @return sottoconti raggruppati
	 */
	private SaldoConti raggruppaSottoContiEntita(SaldoConti saldoConti, Boolean stampaClienti, Boolean stampaFornitori) {
		List<SaldoConto> saldoContiList = new ArrayList<SaldoConto>();
		saldoContiList.addAll(saldoConti.values());

		for (SaldoConto saldoConto : saldoContiList) {
			if ((saldoConto.getSottoTipoConto() == SottotipoConto.CLIENTE && !stampaClienti)
					|| (saldoConto.getSottoTipoConto() == SottotipoConto.FORNITORE && !stampaFornitori)) {
				// cancello il conto, i suoli valori vanno aggiunti al conto
				// base
				saldoConti.remove(saldoConto);

				// modifico il codice del conto con quello del conto "padre"
				// cioè x.x.000000
				saldoConto.setSottoContoCodice(SottoConto.DEFAULT_CODICE);
				// Cerco nella lista dei conti il conto 000000
				SaldoConto saldoContoEntita = saldoConti.get(saldoConto.getSottoContoCodiceCompleto());
				if (saldoContoEntita == null) {
					// Non ho ancora un conto cliente. Lo aggiungo.
					saldoConti.put(saldoConto);
				} else {
					saldoContoEntita.aggiungiImportoAvere(saldoConto.getImportoAvere());
					saldoContoEntita.aggiungiImportoDare(saldoConto.getImportoDare());
					saldoConti.put(saldoContoEntita);
				}
			}
		}
		return saldoConti;
	}

	/**
	 * Rimuove i conti non richiesti.
	 * 
	 * @param saldoConti
	 *            saldoConti
	 * @param rimuoviSaldiAZero
	 *            rimuoviSaldiAZero
	 * @param rimuoviContiGenerici
	 *            rimuoviContiGenerici
	 * @param rimuoviClienti
	 *            rimuoviClienti
	 * @param rimuoviFornitori
	 *            rimuoviFornitori
	 * @return saldoConti
	 */
	private SaldoConti rimuoviSaldi(SaldoConti saldoConti, boolean rimuoviSaldiAZero, boolean rimuoviContiGenerici,
			boolean rimuoviClienti, boolean rimuoviFornitori) {
		List<SaldoConto> saldoContiList = new ArrayList<SaldoConto>();
		saldoContiList.addAll(saldoConti.values());

		for (SaldoConto saldoConto : saldoContiList) {
			if ((saldoConto.getSottoTipoConto() == SottotipoConto.CLIENTE || saldoConto.getSottoTipoConto() == SottotipoConto.FORNITORE)
					&& rimuoviSaldiAZero && BigDecimal.ZERO.compareTo(saldoConto.getSaldo()) == 0) {
				saldoConti.remove(saldoConto);
			}

			if ((rimuoviContiGenerici && saldoConto.getSottoTipoConto() != SottotipoConto.CLIENTE && saldoConto
					.getSottoTipoConto() != SottotipoConto.FORNITORE)
					|| (rimuoviClienti && saldoConto.getSottoTipoConto() == SottotipoConto.CLIENTE && rimuoviContiGenerici)
					|| (rimuoviFornitori && saldoConto.getSottoTipoConto() == SottotipoConto.FORNITORE && rimuoviContiGenerici)) {
				saldoConti.remove(saldoConto);
			}
		}

		return saldoConti;
	}
}
