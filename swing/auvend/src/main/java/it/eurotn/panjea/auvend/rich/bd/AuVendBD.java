/**
 * 
 */
package it.eurotn.panjea.auvend.rich.bd;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.auvend.domain.CodiceIvaAuVend;
import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;
import it.eurotn.panjea.auvend.domain.StatisticaImportazione;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend;
import it.eurotn.panjea.auvend.domain.TipoDocumentoBaseAuVend.TipoOperazione;
import it.eurotn.panjea.auvend.exception.AuVendException;
import it.eurotn.panjea.auvend.service.interfaces.AuVendService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Implementazione del Business Delegate per plugin AuVend.
 * 
 * @author adriano
 * @version 1.0, 30/dic/2008
 * 
 */
public class AuVendBD extends AbstractBaseBD implements IAuVendBD {

	private AuVendService auVendService;

	private static Logger logger = Logger.getLogger(AuVendBD.class);

	@Override
	public void cancellaCodiceIvaAuVend(Integer id) {
		logger.debug("--> Enter cancellaCodiceIvaAuVend");
		start("cancellaCodiceIvaAuVend");
		try {
			auVendService.cancellaCodiceIvaAuVend(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaCodiceIvaAuVend");
		}
		logger.debug("--> Exit cancellaCodiceIvaAuVend ");
	}

	@Override
	public void cancellaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend) {
		logger.debug("--> Enter cancellaLetturaFlussoAuVend");
		start("cancellaLetturaFlussoAuVend");
		try {
			auVendService.cancellaLetturaFlussoAuVend(letturaFlussoAuVend);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaLetturaFlussoAuVend");
		}
		logger.debug("--> Exit cancellaLetturaFlussoAuVend ");

	}

	@Override
	public void cancellaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend) {
		logger.debug("--> Enter cancellaTipoDocumentoBaseAuVend");
		start("cancellaTipoDocumentoBaseAuVend");
		try {
			auVendService.cancellaTipoDocumentoBaseAuVend(tipoDocumentoBaseAuVend);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaTipoDocumentoBaseAuVend");
		}
		logger.debug("--> Exit cancellaTipoDocumentoBaseAuVend ");
	}

	@Override
	public List<Deposito> caricaCaricatori() {
		logger.debug("--> Enter caricaCaricatori");
		start("caricaCaricatori");
		List<Deposito> caricatori = null;
		try {
			caricatori = auVendService.caricaCaricatori();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCaricatori");
		}
		logger.debug("--> Exit caricaCaricatori ");
		return caricatori;
	}

	@Override
	public List<String> caricaCausaliNonAssociateAuvend() {
		logger.debug("--> Enter caricaCausaliNonAssociateAuvend");
		start("caricaCausaliNonAssociateAuvend");
		List<String> result = null;
		try {
			result = auVendService.caricaCausaliNonAssociateAuvend();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCausaliNonAssociateAuvend");
		}
		logger.debug("--> Exit caricaCausaliNonAssociateAuvend ");
		return result;
	}

	@Override
	public CodiceIvaAuVend caricaCodiceIvaAuVend(Integer id) {
		logger.debug("--> Enter caricaCodiceIvaAuVend");
		start("caricaCodiceIvaAuVend");
		CodiceIvaAuVend codiceIvaAuVend = null;
		try {
			codiceIvaAuVend = auVendService.caricaCodiceIvaAuVend(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCodiceIvaAuVend");
		}
		logger.debug("--> Exit caricaCodiceIvaAuVend ");
		return codiceIvaAuVend;
	}

	@Override
	public List<CodiceIvaAuVend> caricaCodiciIvaAuVend() {
		logger.debug("--> Enter caricaCodiciIvaAuVend");
		start("caricaCodiciIvaAuVend");
		List<CodiceIvaAuVend> list = new ArrayList<CodiceIvaAuVend>();
		try {
			list = auVendService.caricaCodiciIvaAuVend();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaCodiciIvaAuVend");
		}
		logger.debug("--> Exit caricaCodiciIvaAuVend ");
		return list;
	}

	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend)
			throws AuVendException {
		logger.debug("--> Enter caricaLetturaFlussoAuVend");
		start("caricaLetturaFlussoAuVend");
		LetturaFlussoAuVend letturaFlussoAuVendLoad = null;
		try {
			letturaFlussoAuVendLoad = auVendService.caricaLetturaFlussoAuVend(letturaFlussoAuVend);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLetturaFlussoAuVend");
		}
		logger.debug("--> Exit caricaLetturaFlussoAuVend ");
		return letturaFlussoAuVendLoad;
	}

	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoFatturazioneRifornimenti() {
		logger.debug("--> Enter caricaLetturaFlussoFatturazioneRifornimenti");
		start("caricaLetturaFlussoMovimenti");
		LetturaFlussoAuVend result = null;
		try {
			result = auVendService.caricaLetturaFlussoFatturazioneRifornimenti();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLetturaFlussoFatturazioneRifornimenti");
		}
		logger.debug("--> Exit caricaLetturaFlussoFatturazioneRifornimenti ");
		return result;
	}

	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoMovimenti() {
		logger.debug("--> Enter caricaLetturaFlussoMovimenti");
		start("caricaLetturaFlussoMovimenti");
		LetturaFlussoAuVend result = null;
		try {
			result = auVendService.caricaLetturaFlussoMovimenti();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLetturaFlussoMovimenti");
		}
		logger.debug("--> Exit caricaLetturaFlussoMovimenti ");
		return result;
	}

	@Override
	public LetturaFlussoAuVend caricaLetturaFlussoRiparazioniContoTerzi() {
		logger.debug("--> Enter caricaLetturaFlussoRiparazioniContoTerzi");
		start("caricaLetturaFlussoRiparazioniContoTerzi");
		LetturaFlussoAuVend result = null;
		try {
			result = auVendService.caricaLetturaFlussoRiparazioniContoTerzi();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLetturaFlussoRiparazioniContoTerzi");
		}
		logger.debug("--> Exit caricaLetturaFlussoRiparazioniContoTerzi ");
		return result;
	}

	@Override
	public List<LetturaFlussoAuVend> caricaLettureFlussoAuVend() {
		logger.debug("--> Enter caricaLettureFlussoAuVend");
		start("caricaLettureFlussoAuVend");
		List<LetturaFlussoAuVend> letture = new ArrayList<LetturaFlussoAuVend>();
		try {
			letture = auVendService.caricaLettureFlussoAuVend();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLettureFlussoAuVend");
		}
		logger.debug("--> Exit caricaLettureFlussoAuVend ");
		return letture;
	}

	@Override
	public Map<Deposito, LetturaFlussoAuVend> caricaLettureFlussoCarichi() {
		logger.debug("--> Enter caricaLettureFlussoCarichi");
		start("caricaLettureFlussoCarichi");
		Map<Deposito, LetturaFlussoAuVend> map = new HashMap<Deposito, LetturaFlussoAuVend>();
		try {
			map = auVendService.caricaLettureFlussoCarichi();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLettureFlussoCarichi");
		}
		logger.debug("--> Exit caricaLettureFlussoCarichi ");
		return map;
	}

	@Override
	public Map<Deposito, LetturaFlussoAuVend> caricaLettureFlussoFatture() {
		logger.debug("--> Enter caricaLettureFlussoFatture");
		start("caricaLettureFlussoFatture");
		Map<Deposito, LetturaFlussoAuVend> map = new HashMap<Deposito, LetturaFlussoAuVend>();
		try {
			map = auVendService.caricaLettureFlussoFatture();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaLettureFlussoFatture");
		}
		logger.debug("--> Exit caricaLettureFlussoFatture ");
		return map;
	}

	@Override
	public List<TipoDocumentoBaseAuVend> caricaTipiDocumentoBaseAuVend() {
		logger.debug("--> Enter caricaTipiDocumentoBaseAuVend");
		start("caricaTipiDocumentoBaseAuVend");
		List<TipoDocumentoBaseAuVend> tipi = new ArrayList<TipoDocumentoBaseAuVend>();
		try {
			tipi = auVendService.caricaTipiDocumentoBaseAuVend();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiDocumentoBaseAuVend");
		}
		logger.debug("--> Exit caricaTipiDocumentoBaseAuVend ");
		return tipi;
	}

	@Override
	public List<TipoDocumentoBaseAuVend> caricaTipiDocumentoBaseAuVendPerTipoOperazione(TipoOperazione tipoOperazione) {
		logger.debug("--> Enter caricaTipiDocumentoBaseAuVendPerTipoOperazione");
		start("caricaTipiDocumentoBaseAuVendPerTipoOperazione");
		List<TipoDocumentoBaseAuVend> tipiDocumentoBaseAuVend = new ArrayList<TipoDocumentoBaseAuVend>();
		try {
			tipiDocumentoBaseAuVend = auVendService.caricaTipiDocumentoBaseAuVendPerTipoOperazione(tipoOperazione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiDocumentoBaseAuVendPerTipoOperazione");
		}
		logger.debug("--> Exit caricaTipiDocumentoBaseAuVendPerTipoOperazione ");
		return tipiDocumentoBaseAuVend;
	}

	@Override
	public TipoDocumentoBaseAuVend caricaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend)
			throws AuVendException {
		logger.debug("--> Enter caricaTipoDocumentoBaseAuVend");
		start("caricaTipoDocumentoBaseAuVend");
		TipoDocumentoBaseAuVend tipoDocumentoBaseAuVendLoad = null;
		try {
			tipoDocumentoBaseAuVendLoad = auVendService.caricaTipoDocumentoBaseAuVend(tipoDocumentoBaseAuVend);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipoDocumentoBaseAuVend");
		}
		logger.debug("--> Exit caricaTipoDocumentoBaseAuVend ");
		return tipoDocumentoBaseAuVendLoad;
	}

	@Override
	public TipoDocumentoBaseAuVend caricaTipoDocumentoBaseAuVendPerTipoOperazione(TipoOperazione tipoOperazione) {
		logger.debug("--> Enter caricaTipoDocumentoBaseAuVendPerTipoOperazione");
		start("caricaTipoDocumentoBaseAuVendPerTipoOperazione");
		TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend = null;
		try {
			tipoDocumentoBaseAuVend = auVendService.caricaTipoDocumentoBaseAuVendPerTipoOperazione(tipoOperazione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipoDocumentoBaseAuVendPerTipoOperazione");
		}
		logger.debug("--> Exit caricaTipoDocumentoBaseAuVendPerTipoOperazione ");
		return tipoDocumentoBaseAuVend;
	}

	@Override
	public List<Integer> chiudiFatture(String deposito, Date dataFine) {
		logger.debug("--> Enter chiudiFatture");
		List<Integer> result = null;
		try {
			result = auVendService.chiudiFatture(deposito, dataFine);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		}
		logger.debug("--> Exit chiudiFatture ");
		return result;
	}

	@Override
	public boolean importaCarichiERifornimenti(Date dataInizio, Date dataFine) {
		logger.debug("--> Enter importaCarichi");
		// non inserisco lo start perchè mi inserirebbe il glassPane nel frame
		// e mi blocca la possibilità di interagire con la gui. La progressBar è già inserita nella pagina
		try {
			return auVendService.importaCarichiERifornimenti(dataInizio, dataFine);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
			return false;
		} finally {
			logger.debug("--> Exit importaCarichi");
		}
	}

	@Override
	public boolean importaFatturazioneRifornimenti(Date dataInizio, Date dataFine) {
		logger.debug("--> Enter importaFatturazioneRifornimenti");
		// non inserisco lo start perchè mi inserirebbe il glassPane nel frame
		// e mi blocca la possibilità di interagire con la gui. La progressBar è già inserita nella pagina
		boolean result = false;
		try {
			result = auVendService.importaFatturazioneRifornimenti(dataInizio, dataFine);
		} catch (AuVendException e) {
			e.printStackTrace();
		}
		logger.debug("--> Exit importaFatturazioneRifornimenti ");
		return result;
	}

	@Override
	public boolean importaFatture(String deposito, Date dataFine) {
		logger.debug("--> Enter importaFatture");
		// non inserisco lo start perchè mi inserirebbe il glassPane nel frame
		// e mi blocca la possibilità di interagire con la gui. La progressBar è già inserita nella pagina
		boolean depositoImportato = false;
		try {
			depositoImportato = auVendService.importaFatture(deposito, dataFine);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		}
		logger.debug("--> Exit importaFatture ");
		return depositoImportato;
	}

	@Override
	public boolean importaMovimenti(Date dataInizio, Date dataFine) {
		logger.debug("--> Enter importamovimenti");
		// non inserisco lo start perchè mi inserirebbe il glassPane nel frame
		// e mi blocca la possibilità di interagire con la gui. La progressBar è già inserita nella pagina
		boolean result = false;
		result = auVendService.importaMovimenti(dataInizio, dataFine);
		logger.debug("--> Exit importamovimenti ");
		return result;
	}

	@Override
	public boolean importaRiparazioniContoTerzi(Date dataInizio, Date dataFine) {
		logger.debug("--> Enter importaRiparazioniContoTerzi");
		// non inserisco lo start perchè mi inserirebbe il glassPane nel frame
		// e mi blocca la possibilità di interagire con la gui. La progressBar è già inserita nella pagina
		boolean result = false;
		try {
			result = auVendService.importaRiparazioniContoTerzi(dataInizio, dataFine);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("--> Exit importaRiparazioniContoTerzi ");
		return result;
	}

	@Override
	public CodiceIvaAuVend salvaCodiceIvaAuVend(CodiceIvaAuVend codiceIvaAuVend) {
		logger.debug("--> Enter salvaCodiceIvaAuVend");
		start("salvaCodiceIvaAuVend");
		CodiceIvaAuVend codiceIvaAuVendSave = null;
		try {
			codiceIvaAuVendSave = auVendService.salvaCodiceIvaAuVend(codiceIvaAuVend);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaCodiceIvaAuVend");
		}
		logger.debug("--> Exit salvaCodiceIvaAuVend ");
		return codiceIvaAuVendSave;
	}

	@Override
	public LetturaFlussoAuVend salvaLetturaFlussoAuVend(LetturaFlussoAuVend letturaFlussoAuVend) {
		logger.debug("--> Enter salvaLetturaFlussoAuVend");
		start("salvaLetturaFlussoAuVend");
		LetturaFlussoAuVend letturaFlussoAuVendSave = null;
		try {
			letturaFlussoAuVendSave = auVendService.salvaLetturaFlussoAuVend(letturaFlussoAuVend);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaLetturaFlussoAuVend");
		}
		logger.debug("--> Exit salvaLetturaFlussoAuVend ");
		return letturaFlussoAuVendSave;
	}

	@Override
	public TipoDocumentoBaseAuVend salvaTipoDocumentoBaseAuVend(TipoDocumentoBaseAuVend tipoDocumentoBaseAuVend) {
		logger.debug("--> Enter salvaTipoDocumentoBaseAuVend");
		start("salvaTipoDocumentoBaseAuVend");
		TipoDocumentoBaseAuVend tipoDocumentoBaseAuVendSave = null;
		try {
			tipoDocumentoBaseAuVendSave = auVendService.salvaTipoDocumentoBaseAuVend(tipoDocumentoBaseAuVend);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaTipoDocumentoBaseAuVend");
		}
		logger.debug("--> Exit salvaTipoDocumentoBaseAuVend ");
		return tipoDocumentoBaseAuVendSave;
	}

	/**
	 * @param auVendService
	 *            The auVendService to set.
	 */
	public void setAuVendService(AuVendService auVendService) {
		this.auVendService = auVendService;
	}

	@Override
	public StatisticaImportazione verificaCarichi(Date dataInizio, Date dataFine) {
		logger.debug("--> Enter verificaCarichi");
		start("verificaCarichi");
		StatisticaImportazione statistica;
		try {
			statistica = auVendService.verificaCarichi(dataInizio, dataFine);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
			statistica = new StatisticaImportazione();
		} finally {
			end("verificaCarichi");
		}
		logger.debug("--> Exit verificaCarichi ");
		return statistica;
	}

	@Override
	public StatisticaImportazione verificaFatturazioneRifornimenti(Date dataInizio, Date dataFine) {
		logger.debug("--> Enter verificaFatturazioneRifornimenti");
		start("verificaFatturazioneRifornimenti");
		StatisticaImportazione result = null;
		try {
			result = auVendService.verificaFatturazioneRifornimenti(dataInizio, dataFine);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("verificaFatturazioneRifornimenti");
		}
		logger.debug("--> Exit verificaFatturazioneRifornimenti ");
		return result;
	}

	@Override
	public Map<String, StatisticaImportazione> verificaFatture(List<String> depositi, Date dataFine) {
		logger.debug("--> Enter verificaFatture");
		start("verificaFatture");
		Map<String, StatisticaImportazione> map = new HashMap<String, StatisticaImportazione>();
		try {
			map = auVendService.verificaFatture(depositi, dataFine);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("verificaFatture");
		}
		logger.debug("--> Exit verificaFatture ");
		return map;
	}

	@Override
	public Map<String, StatisticaImportazione> verificaMovimenti(Date dataInizio, Date dataFine) {
		logger.debug("--> Enter verificaMovimenti");
		start("verificaMovimenti");
		Map<String, StatisticaImportazione> result = null;
		try {
			result = auVendService.verificaMovimenti(dataInizio, dataFine);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("verificaMovimenti");
		}
		logger.debug("--> Exit verificaMovimenti ");
		return result;
	}

	@Override
	public StatisticaImportazione verificaRiparazioniContoTerzi(Date dataInizio, Date dataFine) {
		logger.debug("--> Enter verificaRiparazioniContoTerzi");
		start("verificaRiparazioniContoTerzi");
		StatisticaImportazione result = null;
		try {
			result = auVendService.verificaRiparazioniContoTerzi(dataInizio, dataFine);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("verificaRiparazioniContoTerzi");
		}
		logger.debug("--> Exit verificaRiparazioniContoTerzi ");
		return result;
	}

}
