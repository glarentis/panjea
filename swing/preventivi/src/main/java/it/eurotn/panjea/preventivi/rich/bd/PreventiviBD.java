package it.eurotn.panjea.preventivi.rich.bd;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.service.exception.ClientePotenzialePresenteException;
import it.eurotn.panjea.preventivi.service.interfaces.PreventiviDocumentoService;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.preventivi.util.AreaPreventivoRicerca;
import it.eurotn.panjea.preventivi.util.RigaEvasione;
import it.eurotn.panjea.preventivi.util.RigaMovimentazione;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaAreaPreventivo;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class PreventiviBD extends AbstractBaseBD implements IPreventiviBD {

	private static Logger logger = Logger.getLogger(PreventiviBD.class);
	public static final String BEAN_ID = "preventiviBD";
	private PreventiviDocumentoService preventiviService;

	@Override
	public void aggiungiVariazione(Integer idAreaPreventivo, BigDecimal variazione, BigDecimal percProvvigione,
			RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
			TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
			RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
			TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy) {
		logger.debug("--> Enter aggiungiVariazione");
		start("aggiungiVariazione");

		try {
			preventiviService.aggiungiVariazione(idAreaPreventivo, variazione, percProvvigione,
					variazioneScontoStrategy, tipoVariazioneScontoStrategy, variazioneProvvigioneStrategy,
					tipoVariazioneProvvigioneStrategy);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("aggiungiVariazione");
		}
		logger.debug("--> Exit aggiungiVariazione ");
	}

	@Override
	public AreaPreventivo cambiaStatoSeApplicabile(AreaPreventivo areaPreventivo, StatoAreaPreventivo statoDaApplicare)
			throws ClientePotenzialePresenteException {
		logger.debug("--> Enter cambiaStatoSeApplicabile");
		start("cambiaStatoSeApplicabile");
		AreaPreventivo result = null;
		try {
			result = preventiviService.cambiaStatoSePossibile(areaPreventivo, statoDaApplicare);
		} catch (ClientePotenzialePresenteException e1) {
			throw e1;
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cambiaStatoSeApplicabile");
		}
		logger.debug("--> Exit cambiaStatoSeApplicabile ");
		return result;
	}

	@Override
	public void cancellaArea(AreaPreventivo areaDocumento) {
		cancellaAreaPreventivo(areaDocumento);
	}

	@Override
	public void cancellaAreaPreventivo(AreaPreventivo areaPreventivo) {
		logger.debug("--> Enter cancellaAreaPreventivo");
		start("cancellaAreaPreventivo");

		try {
			preventiviService.cancellaAreaPreventivo(areaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaAreaPreventivo");
		}
		logger.debug("--> Exit cancellaAreaPreventivo ");
	}

	@Override
	public void cancellaAree(List<AreaPreventivo> areeDocumento) {
		cancellaAreePreventivo(areeDocumento);
	}

	@Override
	public void cancellaAreePreventivo(List<AreaPreventivo> areaPreventivo) {
		logger.debug("--> Enter cancellaAreePreventivo");
		start("cancellaAreePreventivo");

		try {
			preventiviService.cancellaAreePreventivo(areaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaAreePreventivo");
		}
		logger.debug("--> Exit cancellaAreePreventivo ");
	}

	@Override
	public AreaPreventivo cancellaRigaPreventivo(RigaPreventivo riga) {
		logger.debug("--> Enter cancellaRigaPreventivo");
		start("cancellaRigaPreventivo");
		AreaPreventivo result = null;
		try {
			result = preventiviService.cancellaRigaPreventivo(riga);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaRigaPreventivo");
		}
		logger.debug("--> Exit cancellaRigaPreventivo ");
		return result;
	}

	@Override
	public void cancellaTipoAreaPreventivo(TipoAreaPreventivo tipoAreaPreventivo) {
		logger.debug("--> Enter cancellaTipoAreaPreventivo");
		start("cancellaTipoAreaPreventivo");
		try {
			preventiviService.cancellaTipoAreaPreventivo(tipoAreaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaTipoAreaPreventivo");
		}
		logger.debug("--> Exit cancellaTipoAreaPreventivo ");
	}

	@Override
	public AreaPreventivoFullDTO caricaAreaFullDTO(AreaPreventivo areaDocumento) {
		return caricaAreaPreventivoFullDTO(areaDocumento);
	}

	@Override
	public AreaPreventivo caricaAreaPreventivo(AreaPreventivo areaPreventivo) {
		logger.debug("--> Enter caricaAreaPreventivo");
		start("caricaAreaPreventivo");
		AreaPreventivo result = null;
		try {
			result = preventiviService.caricaAreaPreventivo(areaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAreaPreventivo");
		}
		logger.debug("--> Exit caricaAreaPreventivo ");
		return result;
	}

	@Override
	public AreaPreventivoFullDTO caricaAreaPreventivoControlloFullDTO(Map<Object, Object> paramenters) {
		logger.debug("--> Enter caricaAreaPreventivoControlloFullDTO");
		start("caricaAreaPreventivoControlloFullDTO");
		AreaPreventivoFullDTO result = null;
		try {
			result = preventiviService.caricaAreaPreventivoControlloFullDTO(paramenters);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAreaPreventivoControlloFullDTO");
		}
		logger.debug("--> Exit caricaAreaPreventivoControlloFullDTO ");
		return result;
	}

	@Override
	public AreaPreventivoFullDTO caricaAreaPreventivoFullDTO(AreaPreventivo areaPreventivo) {
		logger.debug("--> Enter caricaAreaPreventivoFullDTO");
		start("caricaAreaPreventivoFullDTO");
		AreaPreventivoFullDTO result = null;
		try {
			result = preventiviService.caricaAreaPreventivoFullDTO(areaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAreaPreventivoFullDTO");
		}
		logger.debug("--> Exit caricaAreaPreventivoFullDTO ");
		return result;
	}

	@Override
	public AreaPreventivoFullDTO caricaAreaPreventivoFullDTOStampa(Map<Object, Object> paramenters) {
		logger.debug("--> Enter caricaAreaPreventivoFullDTOStampa");
		start("caricaAreaPreventivoFullDTOStampa");
		AreaPreventivoFullDTO result = null;
		try {
			result = preventiviService.caricaAreaPreventivoFullDTOStampa(paramenters);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAreaPreventivoFullDTOStampa");
		}
		logger.debug("--> Exit caricaAreaPreventivoFullDTOStampa ");
		return result;
	}

	@Override
	public List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
			int page, int sizeOfPage) {
		logger.debug("--> Enter caricaMovimentazione");
		start("caricaMovimentazione");

		List<RigaMovimentazione> righe = new ArrayList<RigaMovimentazione>();
		try {
			righe = preventiviService.caricaMovimentazione(parametriRicercaMovimentazione, page, sizeOfPage);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaMovimentazione");
		}
		logger.debug("--> Exit caricaMovimentazione ");
		return righe;
	}

	@Override
	public RigaPreventivo caricaRigaPreventivo(RigaPreventivo rigaPreventivo) {
		logger.debug("--> Enter caricaRigaPreventivo");
		start("caricaRigaPreventivo");
		RigaPreventivo result = null;
		try {
			result = preventiviService.caricaRigaPreventivo(rigaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRigaPreventivo");
		}
		logger.debug("--> Exit caricaRigaPreventivo ");
		return result;
	}

	@Override
	public List<RigaDestinazione> caricaRigheCollegate(RigaPreventivo rigaPreventivo) {
		logger.debug("--> Enter caricaRigheCollegate");
		start("caricaRigheCollegate");
		List<RigaDestinazione> righe = null;
		try {
			righe = preventiviService.caricaRigheCollegate(rigaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRigheCollegate");
		}
		logger.debug("--> Exit caricaRigheCollegate ");
		return righe;
	}

	@Override
	public List<RigaEvasione> caricaRigheEvasione(Integer idAreaPreventivo) {
		logger.debug("--> Enter caricaRigheEvasione");
		start("caricaRigheEvasione");

		List<RigaEvasione> righeEvansione = null;
		try {
			righeEvansione = preventiviService.caricaRigheEvasione(idAreaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRigheEvasione");
		}
		logger.debug("--> Exit caricaRigheEvasione ");
		return righeEvansione;
	}

	@Override
	public List<RigaPreventivo> caricaRighePreventivo(AreaPreventivo areaPreventivo) {
		logger.debug("--> Enter caricaRighePreventivo");
		start("caricaRighePreventivo");
		List<RigaPreventivo> result = null;
		try {
			result = preventiviService.caricaRighePreventivo(areaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRighePreventivo");
		}
		logger.debug("--> Exit caricaRighePreventivo ");
		return result;
	}

	@Override
	public List<RigaPreventivoDTO> caricaRighePreventivoDTO(AreaPreventivo areaPreventivo) {
		logger.debug("--> Enter caricaRighePreventivoDTO");
		start("caricaRighePreventivoDTO");
		List<RigaPreventivoDTO> result = null;
		try {
			result = preventiviService.caricaRighePreventivoDTO(areaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRighePreventivoDTO");
		}
		logger.debug("--> Exit caricaRighePreventivoDTO ");
		return result;
	}

	@Override
	public List<TipoAreaPreventivo> caricaTipiAreaPreventivo(String fieldSearch, String valueSearch,
			boolean loadTipiDocumentoDisabilitati) {
		logger.debug("--> Enter caricaTipiAreaPreventivo");
		start("caricaTipiAreaPreventivo");
		List<TipoAreaPreventivo> result = null;
		try {
			result = preventiviService
					.caricaTipiAreaPreventivo(fieldSearch, valueSearch, loadTipiDocumentoDisabilitati);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiAreaPreventivo");
		}
		logger.debug("--> Exit caricaTipiAreaPreventivo ");
		return result;
	}

	@Override
	public List<TipoDocumento> caricaTipiDocumentiPreventivo() {
		logger.debug("--> Enter caricaTipiDocumentiPreventivo");
		start("caricaTipiDocumentiPreventivo");
		List<TipoDocumento> result = null;
		try {
			result = preventiviService.caricaTipiDocumentiPreventivo();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiDocumentiPreventivo");
		}
		logger.debug("--> Exit caricaTipiDocumentiPreventivo ");
		return result;
	}

	@Override
	public TipoAreaPreventivo caricaTipoAreaPreventivo(Integer id) {
		logger.debug("--> Enter caricaTipoAreaPreventivo");
		start("caricaTipoAreaPreventivo");
		TipoAreaPreventivo result = null;
		try {
			result = preventiviService.caricaTipoAreaPreventivo(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipoAreaPreventivo");
		}
		logger.debug("--> Exit caricaTipoAreaPreventivo ");
		return result;
	}

	@Override
	public TipoAreaPreventivo caricaTipoAreaPreventivoPerTipoDocumento(Integer idTipoDocumento) {
		logger.debug("--> Enter caricaTipoAreaPreventivoPerTipoDocumento");
		start("caricaTipoAreaPreventivoPerTipoDocumento");
		TipoAreaPreventivo result = null;
		try {
			result = preventiviService.caricaTipoAreaPreventivoPerTipoDocumento(idTipoDocumento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipoAreaPreventivoPerTipoDocumento");
		}
		logger.debug("--> Exit caricaTipoAreaPreventivoPerTipoDocumento ");
		return result;
	}

	@Override
	public void collegaTestata(Set<Integer> righePreventivoDaCambiare) {
		logger.debug("--> Enter collegaTestata");
		start("collegaTestata");
		try {
			preventiviService.collegaTestata(righePreventivoDaCambiare);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("collegaTestata");
		}
		logger.debug("--> Exit collegaTestata ");
	}

	@Override
	public Object copiaArea(Integer idArea) {
		return copiaPreventivo(idArea);
	}

	@Override
	public AreaPreventivoFullDTO copiaPreventivo(Integer idAreaPreventivo) {
		logger.debug("--> Enter copiaPreventivo");
		start("copiaPreventivo");
		AreaPreventivoFullDTO result = null;
		try {
			result = preventiviService.copiaPreventivo(idAreaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("copiaPreventivo");
		}
		logger.debug("--> Exit copiaPreventivo ");
		return result;
	}

	@Override
	public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
		logger.debug("--> Enter creaRigaArticolo");
		start("creaRigaArticolo");
		RigaArticolo result = null;
		try {
			result = preventiviService.creaRigaArticolo(parametriCreazioneRigaArticolo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("creaRigaArticolo");
		}
		logger.debug("--> Exit creaRigaArticolo ");
		return result;
	}

	@Override
	public boolean creaRigaNoteAutomatica(AreaPreventivo areaDocumento, String note) {
		logger.debug("--> Enter creaRigaNoteAutomatica");
		start("creaRigaNoteAutomatica");
		boolean result = false;
		try {
			result = preventiviService.creaRigaNoteAutomatica(areaDocumento, note);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("creaRigaNoteAutomatica");
		}
		logger.debug("--> Exit creaRigaNoteAutomatica ");
		return result;
	}

	@Override
	public void evadiPreventivi(List<RigaEvasione> rigaEvasione, TipoAreaOrdine tipoAreaOrdine, DepositoLite deposito,
			Date dataOrdine, AgenteLite agente, Date dataConsegnaOrdine) {
		logger.debug("--> Enter evadiPreventivi");
		start("evadiPreventivi");
		try {
			preventiviService.evadiPreventivi(rigaEvasione, tipoAreaOrdine, deposito, dataOrdine, agente,
					dataConsegnaOrdine);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("evadiPreventivi");
		}
		logger.debug("--> Exit evadiPreventivi ");
	}

	@Override
	public void inserisciRaggruppamentoArticoli(Integer idAreaPreventivo, ProvenienzaPrezzo provenienzaPrezzo,
			Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
			Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
			Integer idZonaGeografica, boolean noteSuDestinazione, String codiceValuta, String codiceLingua,
			Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
			BigDecimal percentualeScontoCommerciale) {
		logger.debug("--> Enter inserisciRaggruppamentoArticoli");
		start("inserisciRaggruppamentoArticoli");
		try {
			preventiviService.inserisciRaggruppamentoArticoli(idAreaPreventivo, provenienzaPrezzo,
					idRaggruppamentoArticoli, data, idSedeEntita, idListinoAlternativo, idListino, importo,
					codiceIvaAlternativo, idTipoMezzo, idZonaGeografica, noteSuDestinazione, codiceValuta,
					codiceLingua, idAgente, tipologiaCodiceIvaAlternativo, percentualeScontoCommerciale);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("inserisciRaggruppamentoArticoli");
		}
		logger.debug("--> Exit inserisciRaggruppamentoArticoli ");
	}

	@Override
	public AreaPreventivoFullDTO ricalcolaPrezziPreventivo(Integer idAreaPreventivo) {
		logger.debug("--> Enter ricalcolaPrezziPreventivo");
		start("ricalcolaPrezziPreventivo");
		AreaPreventivoFullDTO result = null;
		try {
			result = preventiviService.ricalcolaPrezziPreventivo(idAreaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("ricalcolaPrezziPreventivo");
		}
		logger.debug("--> Exit ricalcolaPrezziPreventivo ");
		return result;
	}

	@Override
	public List<AreaPreventivoRicerca> ricercaAreePreventivo(
			ParametriRicercaAreaPreventivo parametriRicercaAreaPreventivo) {
		logger.debug("--> Enter ricercaAreePreventivo");
		start("ricercaAreePreventivo");
		List<AreaPreventivoRicerca> result = null;
		try {
			result = preventiviService.ricercaAreePreventivo(parametriRicercaAreaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("ricercaAreePreventivo");
		}
		logger.debug("--> Exit ricercaAreePreventivo ");
		return result;
	}

	@Override
	public AreaPreventivoFullDTO salvaAreaDocumento(AreaPreventivo areaDocumento, AreaRate areaRate) {
		return salvaAreaPreventivo(areaDocumento, areaRate);
	}

	@Override
	public AreaPreventivoFullDTO salvaAreaPreventivo(AreaPreventivo areaPreventivo, AreaRate areaRate) {
		logger.debug("--> Enter salvaAreaPreventivo");
		start("salvaAreaPreventivo");
		AreaPreventivoFullDTO result = null;
		try {
			result = preventiviService.salvaAreaPreventivo(areaPreventivo, areaRate);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaAreaPreventivo");
		}
		logger.debug("--> Exit salvaAreaPreventivo ");
		return result;
	}

	@Override
	public RigaPreventivo salvaRigaPreventivo(RigaPreventivo riga, boolean checkRiga) {
		logger.debug("--> Enter salvaRigaPreventivo");
		start("salvaRigaPreventivo");
		RigaPreventivo result = null;
		try {
			result = preventiviService.salvaRigaPreventivo(riga, checkRiga);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaRigaPreventivo");
		}
		logger.debug("--> Exit salvaRigaPreventivo ");
		return result;
	}

	@Override
	public TipoAreaPreventivo salvaTipoAreaPreventivo(TipoAreaPreventivo tipoAreaPreventivo) {
		logger.debug("--> Enter salvaTipoAreaPreventivo");
		start("salvaTipoAreaPreventivo");
		TipoAreaPreventivo result = null;
		try {
			result = preventiviService.salvaTipoAreaPreventivo(tipoAreaPreventivo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaTipoAreaPreventivo");
		}
		logger.debug("--> Exit salvaTipoAreaPreventivo ");
		return result;
	}

	/**
	 * @param preventiviService
	 *            the preventiviService to set
	 */
	public void setPreventiviService(PreventiviDocumentoService preventiviService) {
		this.preventiviService = preventiviService;
	}

	@Override
	public void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest) {
		logger.debug("--> Enter spostaRighe");
		start("spostaRighe");
		try {
			preventiviService.spostaRighe(righeDaSpostare, idDest);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("spostaRighe");
		}
		logger.debug("--> Exit spostaRighe ");
	}

	@Override
	public AreaPreventivo totalizzaDocumento(AreaPreventivo areaPreventivo, AreaRate areaRate) {
		logger.debug("--> Enter totalizzaDocumento");
		start("totalizzaDocumento");
		AreaPreventivo result = null;
		try {
			result = preventiviService.totalizzaDocumento(areaPreventivo, areaRate);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("totalizzaDocumento");
		}
		logger.debug("--> Exit totalizzaDocumento ");
		return result;
	}

	@Override
	public AreaPreventivoFullDTO validaRighePreventivo(AreaPreventivo areaPreventivo, AreaRate areaRate,
			boolean cambioStato) {
		logger.debug("--> Enter validaRighePreventivo");
		start("validaRighePreventivo");
		AreaPreventivoFullDTO result = null;
		try {
			result = preventiviService.validaRighePreventivo(areaPreventivo, areaRate, cambioStato);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("validaRighePreventivo");
		}
		logger.debug("--> Exit validaRighePreventivo ");
		return result;
	}

}
