package it.eurotn.panjea.pagamenti.rich.bd;

import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipoRicercaCodicePagamento;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.pagamenti.service.interfaces.AnagraficaPagamentiService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.List;

import org.apache.log4j.Logger;

public class AnagraficaPagamentiBD extends AbstractBaseBD implements IAnagraficaPagamentiBD {

	private static Logger logger = Logger.getLogger(AnagraficaPagamentiBD.class);

	private AnagraficaPagamentiService anagraficaPagamentiService;

	@Override
	public void cancellaCodicePagamento(CodicePagamento codicePagamento) {
		logger.debug("--> Enter cancellaCodicePagamento");
		start("cancellaCodicePagamento");
		try {
			anagraficaPagamentiService.cancellaCodicePagamento(codicePagamento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCodicePagamento");
		}
		logger.debug("--> Exit cancellaCodicePagamento ");
	}

	@Override
	public void cancellaSedePagamento(Integer sedePagamentoId) {
		logger.debug("--> Enter cancellaSedePagamento");
		start("cancellaSedePagamento");
		try {
			anagraficaPagamentiService.cancellaSedePagamento(sedePagamentoId);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaSedePagamento");
		}
		logger.debug("--> Exit cancellaSedePagamento ");
	}

	@Override
	public CodicePagamento caricaCodicePagamento(Integer id) {
		logger.debug("--> Enter caricaCodicePagamento");
		start("caricaCodicePagamento");
		CodicePagamento codicePagamento = null;
		try {
			codicePagamento = anagraficaPagamentiService.caricaCodicePagamento(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCodicePagamento");
		}
		logger.debug("--> Exit caricaCodicePagamento ");

		return codicePagamento;
	}

	@Override
	public CodicePagamento caricaCodicePagamento(String codice) {
		logger.debug("--> Enter caricaCodicePagamento");
		start("caricaCodicePagamento");
		CodicePagamento codicePagamento = null;
		try {
			codicePagamento = anagraficaPagamentiService.caricaCodicePagamento(codice);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCodicePagamento");
		}
		logger.debug("--> Exit caricaCodicePagamento ");
		return codicePagamento;
	}

	@Override
	public List<CodicePagamento> caricaCodiciPagamento(String filtro, TipoRicercaCodicePagamento tipoRicerca,
			boolean includiDisabilitati) {
		logger.debug("--> Enter caricaCodiciPagamento");
		start("caricaCodiciPagamento");
		try {
			return anagraficaPagamentiService.caricaCodiciPagamento(filtro, tipoRicerca, includiDisabilitati);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCodiciPagamento");
		}
		logger.debug("--> Exit caricaCodiciPagamento ");
		return null;
	}

	@Override
	public SedePagamento caricaSedePagamento(Integer sedePagamentoId) throws PagamentiException {
		logger.debug("--> Enter caricaSedePagamento");
		start("caricaSedePagamento");
		SedePagamento sedePagamento = null;
		try {
			sedePagamento = anagraficaPagamentiService.caricaSedePagamento(sedePagamentoId);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSedePagamento");
		}
		logger.debug("--> Exit caricaSedePagamento ");
		return sedePagamento;
	}

	@Override
	public SedePagamento caricaSedePagamentoBySedeEntita(Integer sedeEntitaId) {
		logger.debug("--> Enter caricaSedePagamentoBySedeEntita");
		start("caricaSedePagamentoBySedeEntita");
		SedePagamento sedePagamento = null;
		try {
			sedePagamento = anagraficaPagamentiService.caricaSedePagamentoBySedeEntita(sedeEntitaId);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSedePagamentoBySedeEntita");
		}
		logger.debug("--> Exit caricaSedePagamentoBySedeEntita ");
		return sedePagamento;
	}

	@Override
	public SedePagamento caricaSedePagamentoPrincipaleEntita(Integer entitaId) {
		logger.debug("--> Enter caricaSedePagamentoByEntita");
		start("caricaSedePagamentoByEntita");
		SedePagamento sedePagamento = null;
		try {
			sedePagamento = anagraficaPagamentiService.caricaSedePagamentoPrincipaleEntita(entitaId);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaSedePagamentoByEntita");
		}
		logger.debug("--> Exit caricaSedePagamentoByEntita ");
		return sedePagamento;
	}

	@Override
	public CodicePagamento salvaCodicePagamento(CodicePagamento codicePagamento) {
		logger.debug("--> Enter salvaCodicePagamento");
		start("salvaCodicePagamento");
		try {
			codicePagamento = anagraficaPagamentiService.salvaCodicePagamento(codicePagamento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCodicePagamento");
		}
		logger.debug("--> Exit salvaCodicePagamento ");
		return codicePagamento;
	}

	@Override
	public SedePagamento salvaSedePagamento(SedePagamento sedePagamento) {
		logger.debug("--> Enter salvaSedePagamento");
		start("salvaSedePagamento");
		SedePagamento sedePagamentoSave = null;
		try {
			sedePagamentoSave = anagraficaPagamentiService.salvaSedePagamento(sedePagamento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaSedePagamento");
		}
		logger.debug("--> Exit salvaSedePagamento ");
		return sedePagamentoSave;
	}

	@Override
	public void setAnagraficaPagamentiService(AnagraficaPagamentiService anagraficaPagamentiService) {
		this.anagraficaPagamentiService = anagraficaPagamentiService;
	}

}