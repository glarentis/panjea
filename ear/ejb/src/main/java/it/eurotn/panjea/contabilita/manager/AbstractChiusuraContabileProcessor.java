package it.eurotn.panjea.contabilita.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.manager.interfaces.ValutaManager;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipiDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ChiusuraContabileProcessor;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnnualeManager;
import it.eurotn.panjea.contabilita.manager.interfaces.LibroGiornaleManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.DocumentiNonStampatiGiornaleException;
import it.eurotn.panjea.contabilita.service.exception.GiornaliNonValidiException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.RigaContabileDTO;
import it.eurotn.panjea.contabilita.util.SaldoConti;
import it.eurotn.panjea.contabilita.util.SaldoConto;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public abstract class AbstractChiusuraContabileProcessor implements ChiusuraContabileProcessor {
	private static Logger logger = Logger.getLogger(AbstractChiusuraContabileProcessor.class);

	@IgnoreDependency
	@EJB
	protected ContabilitaAnnualeManager contabilitaAnnualeManager;

	@EJB
	protected TipiAreaContabileManager tipiAreaContabileManager;

	@EJB
	protected AreaContabileManager areaContabileManager;

	@EJB
	protected PianoContiManager pianoContiManager;

	@EJB
	protected ValutaManager valutaManager;

	@EJB
	protected LibroGiornaleManager libroGiornaleManager;

	/**
	 * Carica i tipi area contabili per la chiusura conti per il tipo conto restutiuto da {@link #getTipoConto()}. Il
	 * TipoAreaContabile è caricato tramite i {@link TipiDocumentoBase}.
	 *
	 * @return TipoAreaContabile per la chiusura
	 * @throws TipoDocumentoBaseException
	 *             lanciata quando non sono configurati i conti base per le chiusure
	 */
	private TipoAreaContabile caricaTipoAreaContabileChiusura() throws TipoDocumentoBaseException {
		logger.debug("--> Enter caricaTipoAraeContabileChiusura");
		TipoAreaContabile tipoDocumentoBaseChiusura = null;
		TipiDocumentoBase tipiDocumentoBase = null;
		try {
			tipiDocumentoBase = tipiAreaContabileManager.caricaTipiOperazione();
		} catch (ContabilitaException e) {
			logger.error("--> errore nel caricare i tipi documento base", e);
			throw new RuntimeException(e);
		}
		TipoOperazioneTipoDocumento tipoOperazione = TipiDocumentoBase
				.getTipoOperazioneChiusuraPerTipoConto(getTipoConto());
		if (!tipiDocumentoBase.containsKey(tipoOperazione)) {
			logger.warn("--> Tipo documento chiusura per il conto " + getTipoConto() + " non presente");
			throw new TipoDocumentoBaseException(new String[] { "Tipo operazione "
					+ TipiDocumentoBase.getTipoOperazioneChiusuraPerTipoConto(getTipoConto()).name() });
		}
		tipoDocumentoBaseChiusura = tipiDocumentoBase
				.get(TipiDocumentoBase.getTipoOperazioneChiusuraPerTipoConto(getTipoConto()));
		logger.debug("--> Exit caricaTipoAraeContabileChiusura");
		return tipoDocumentoBaseChiusura;
	}

	/**
	 * Chiama alla fine della compilazione del documento di chiusura.
	 *
	 * @param saldoImportoDare
	 *            saldo dare calcolato. Il saldo delle righe di chiusura quindi già girato.
	 * @param saldoImportoAvere
	 *            saldo avere calcolato. Il saldo delle righe di chiusura quindi già girato.
	 * @return lista di {@link RigaContabile} da aggiungere a quelle create
	 * @throws ContiBaseException
	 * @throws ContiBaseException
	 *             lanciata quando mancano i conti base per la chiusura<br.>
	 *
	 */
	protected abstract List<RigaContabile> completaDocumentoChiusura(BigDecimal saldoImportoDare,
			BigDecimal saldoImportoAvere) throws ContiBaseException;

	/**
	 * Crea e salva l'area contabile per la chiusura.
	 *
	 * @param annoEsercizio
	 *            anno di esercizio dellì'area contabile.
	 * @param dataDocumento
	 *            data del documento e data di registrazione dell' area contabile.
	 * @param tipoAreaContabileChiusura
	 *            tipo area contabile da associare al documento di chiusura
	 * @return {@link AreaContabile} in stato {@link StatoAreaContabile#CONFERMATO}
	 */
	private AreaContabile creaSalvaAreaContabileChiusura(Integer annoEsercizio, Date dataDocumento,
			TipoAreaContabile tipoAreaContabileChiusura) {
		AreaContabile areaContabileChiusura = new AreaContabile();
		areaContabileChiusura.setAnnoMovimento(annoEsercizio);
		areaContabileChiusura.getDocumento().getTotale()
				.setCodiceValuta(valutaManager.caricaValutaAziendaCorrente().getCodiceValuta());
		areaContabileChiusura.setDataRegistrazione(dataDocumento);
		areaContabileChiusura.getDocumento().setDataDocumento(dataDocumento);
		areaContabileChiusura.setTipoAreaContabile(tipoAreaContabileChiusura);
		areaContabileChiusura.getDocumento().setTipoDocumento(tipoAreaContabileChiusura.getTipoDocumento());
		areaContabileChiusura.setStatoAreaContabile(StatoAreaContabile.CONFERMATO);
		try {
			areaContabileChiusura = areaContabileManager.salvaAreaContabileNoCheck(areaContabileChiusura);
		} catch (Exception e) {
			logger.error("--> errore nel salvare l'area contabile per il documento di chiusura", e);
			throw new RuntimeException(e);
		}
		return areaContabileChiusura;
	}

	@Override
	public void eseguiChiusura(Integer annoEsercizio, SaldoConti bilancio, Date dataDocumentoChiusura)
			throws ChiusuraEsistenteException, DocumentiNonStampatiGiornaleException, TipoDocumentoBaseException,
			ContiBaseException, GiornaliNonValidiException {
		if (bilancio.values().size() == 0) {
			logger.warn("--> Bilancio senza righe");
			return;
		}

		// Prima creo tutte le righe...se non ne ho non devo creare il documento
		List<RigaContabile> righeDocumento = new ArrayList<RigaContabile>();

		// non posso verificare qui dentro la presenza di documenti e di documenti non a giornale
		// perchè questo metodo viene chiamato per:
		// chiusuraContabileEconomicaProcessor, chiusuraContabilePatrimonialeProcessor, chiusuraContabileOrdineProcessor
		// verificaDocumentiChisuraPresenti(annoEsercizio);
		// verificaStampaGiornaleMovimenti(annoEsercizio);
		TipoAreaContabile tipoAreaContabile = caricaTipoAreaContabileChiusura();

		BigDecimal saldoImportoDare = BigDecimal.ZERO;
		BigDecimal saldoImportoAvere = BigDecimal.ZERO;

		for (SaldoConto saldoConto : bilancio.asList()) {
			if (saldoConto.getTipoConto() != getTipoConto()) {
				continue;
			}
			SottoConto sottoConto = null;
			try {
				sottoConto = pianoContiManager.caricaSottoConto(saldoConto.getSottoContoId());
			} catch (ContabilitaException e) {
				logger.error("--> errore nel caricare il sotto conto con id " + saldoConto.getSottoContoId(), e);
				throw new RuntimeException(e);
			}

			// E' UNA CHIUSURA. Quindi i conti vanno girati.==>Se saldo>0 metto in avere
			if (saldoConto.getSaldo().compareTo(BigDecimal.ZERO) > 0) {
				saldoImportoAvere = saldoImportoAvere.add(saldoConto.getSaldo());
				righeDocumento.add(
						RigaContabile.creaRigaContabile(null, sottoConto, false, saldoConto.getSaldo(), null, false));
			} else if (saldoConto.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
				saldoImportoDare = saldoImportoDare.add(saldoConto.getSaldo().abs());
				righeDocumento.add(RigaContabile.creaRigaContabile(null, sottoConto, true, saldoConto.getSaldo().abs(),
						null, false));
			}
		}

		righeDocumento.addAll(completaDocumentoChiusura(saldoImportoDare, saldoImportoAvere));

		if (righeDocumento.size() > 0) {
			AreaContabile areaContabileChiusura = creaSalvaAreaContabileChiusura(annoEsercizio, dataDocumentoChiusura,
					tipoAreaContabile);
			for (RigaContabile rigaContabileNuova : righeDocumento) {
				rigaContabileNuova.setAreaContabile(areaContabileChiusura);
				areaContabileManager.salvaRigaContabileNoCheck(rigaContabileNuova);
			}
		}
	}

	/**
	 * @return Tipo Conto che deve gestire la classe.
	 */
	protected abstract TipoConto getTipoConto();

	@Override
	public void verificaDocumentiChisuraPresenti(Integer annoEsercizio) throws ChiusuraEsistenteException {
		logger.debug("--> Enter verificaDocumentiChisuraPresenti");
		try {
			TipoAreaContabile tipoAreaContabileChiusura = caricaTipoAreaContabileChiusura();
			List<AreaContabile> areeContabiliAperturaChiusura = contabilitaAnnualeManager
					.caricaAreeContabiliAperturaChiusura(annoEsercizio, getTipoConto());
			// Se il primo è un documento di chiusura nell'anno richiesto errore, altrimenti ok .
			if (areeContabiliAperturaChiusura.isEmpty()) {
				return;
			}
			boolean tipoAreaChiusura = areeContabiliAperturaChiusura.get(0).getTipoAreaContabile()
					.equals(tipoAreaContabileChiusura);
			boolean annoCorrente = areeContabiliAperturaChiusura.get(0).getAnnoMovimento() == annoEsercizio;
			if (tipoAreaChiusura && annoCorrente) {
				throw new ChiusuraEsistenteException(getTipoConto());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit verificaDocumentiChisuraPresenti");
	}

	@Override
	public final void verificaStampaGiornaleMovimenti(Integer annoEsercizio)
			throws DocumentiNonStampatiGiornaleException, GiornaliNonValidiException {

		libroGiornaleManager.verificaStatoGiornali(annoEsercizio);
		List<TipoDocumento> tipiDocumento = null;
		TipiDocumentoBase tipiDocumentoBase = null;
		// Pulisco dalla lista dei tipiDocumento
		// i tipiDocumentoBase per le chiusure
		try {
			tipiDocumento = tipiAreaContabileManager.caricaTipiDocumentiContabili();
			tipiDocumentoBase = tipiAreaContabileManager.caricaTipiOperazione();
		} catch (ContabilitaException e) {
			logger.error("--> errore neo verificare la stampa giornale movimenti ", e);
		}

		TipoAreaContabile tipoAreaContabileChisura = tipiDocumentoBase
				.get(TipiDocumentoBase.getTipoOperazioneChiusuraPerTipoConto(TipoConto.PATRIMONIALE));
		tipiDocumento.remove(tipoAreaContabileChisura.getTipoDocumento());

		tipoAreaContabileChisura = tipiDocumentoBase
				.get(TipiDocumentoBase.getTipoOperazioneChiusuraPerTipoConto(TipoConto.ECONOMICO));
		tipiDocumento.remove(tipoAreaContabileChisura.getTipoDocumento());

		tipoAreaContabileChisura = tipiDocumentoBase
				.get(TipiDocumentoBase.getTipoOperazioneChiusuraPerTipoConto(TipoConto.ORDINE));
		tipiDocumento.remove(tipoAreaContabileChisura.getTipoDocumento());

		ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili;
		parametriRicercaMovimentiContabili = new ParametriRicercaMovimentiContabili();
		parametriRicercaMovimentiContabili.setAnnoCompetenza(annoEsercizio.toString());
		parametriRicercaMovimentiContabili.setEscludiMovimentiStampati(true);
		parametriRicercaMovimentiContabili.setFiltraAbilitatiStampaGiornale(true);
		Set<TipoDocumento> tipiSet = new HashSet<TipoDocumento>(tipiDocumento);
		parametriRicercaMovimentiContabili.setTipiDocumento(tipiSet);
		List<RigaContabileDTO> movimentiNonStampati = areaContabileManager
				.ricercaControlloAreeContabili(parametriRicercaMovimentiContabili);
		if (movimentiNonStampati.size() > 0) {
			logger.error("--> errore, documenti contabili non stampati a giornale nell'anno di competenza ");
			throw new DocumentiNonStampatiGiornaleException(
					"Documenti contabili non stampati a giornale nell'anno di competenza ");
		}
	}

	@Override
	public final void verificaTipoDocumentoBaseChiusura() throws TipoDocumentoBaseException {
		caricaTipoAreaContabileChiusura();
	}
}
