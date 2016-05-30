/**
 *
 */
package it.eurotn.panjea.intra.rich.bd;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.IntraSettings;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.panjea.intra.domain.TotaliDichiarazione;
import it.eurotn.panjea.intra.domain.dichiarazione.FileDichiarazione;
import it.eurotn.panjea.intra.manager.ParametriRicercaAreaIntra;
import it.eurotn.panjea.intra.service.interfaces.IntraService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author leonardo
 */
public class IntraBD extends AbstractBaseBD implements IIntraBD {

	public static final String BEAN_ID = "intraBD";

	private static Logger logger = Logger.getLogger(IntraBD.class);

	private IntraService intraService;

	@Override
	public FileDichiarazione aggiornaNomeFileDichiarazione(int idFileDichiarazione, String nome) {
		logger.debug("--> Enter aggiornaNomeFileDichiarazione");
		start("aggiornaNomeFileDichiarazione");
		FileDichiarazione result = null;
		try {
			result = intraService.aggiornaNomeFileDichiarazione(idFileDichiarazione, nome);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("aggiornaNomeFileDichiarazione");
		}
		logger.debug("--> Exit aggiornaNomeFileDichiarazione ");
		return result;
	}

	@Override
	public String associaNomenclatura(byte[] file) {
		logger.debug("--> Enter associaNomenclatura");
		start("associaNomenclatura");
		String result = "";
		try {
			result = intraService.associaNomenclatura(file);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("associaNomenclatura");
		}
		logger.debug("--> Exit associaNomenclatura ");
		return result;
	}

	@Override
	public TotaliDichiarazione calcolaTotaliDichiarazione(DichiarazioneIntra dichiarazioneIntra) {
		logger.debug("--> Enter calcolaTotaliDichiarazione");
		start("calcolaTotaliDichiarazione");
		TotaliDichiarazione result = null;
		try {
			result = intraService.calcolaTotaliDichiarazione(dichiarazioneIntra.getId());
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("calcolaTotaliDichiarazione");
		}
		logger.debug("--> Exit calcolaTotaliDichiarazione ");
		return result;
	}

	@Override
	public void cancellaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		logger.debug("--> Enter cancellaDichiarazioneIntra");
		start("cancellaDichiarazioneIntra");
		try {
			intraService.cancellaDichiarazioneIntra(dichiarazioneIntra);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaDichiarazioneIntra");
		}
		logger.debug("--> Exit cancellaDichiarazioneIntra ");
	}

	@Override
	public void cancellaFileDichiarazioni(int id) {
		logger.debug("--> Enter cancellaFileDichiarazioni");
		start("cancellaFileDichiarazioni");
		try {
			intraService.cancellaFileDichiarazioni(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaFileDichiarazioni");
		}
		logger.debug("--> Exit cancellaFileDichiarazioni ");
	}

	@Override
	public void cancellaRigaSezioneDichiarazione(RigaSezioneIntra rigaSezioneIntra) {
		logger.debug("--> Enter cancellaRigaSezioneDichiarazione");
		start("cancellaRigaSezioneDichiarazione");
		try {
			intraService.cancellaRigaSezioneDichiarazione(rigaSezioneIntra);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaRigaSezioneDichiarazione");
		}
		logger.debug("--> Exit cancellaRigaSezioneDichiarazione ");

	}

	@Override
	public void cancellaServizio(Servizio servizio) {
		logger.debug("--> Enter cancellaServizio");
		start("cancellaServizio");
		try {
			intraService.cancellaServizio(servizio);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaServizio");
		}
		logger.debug("--> Exit cancellaServizio ");

	}

	@Override
	public AreaIntra caricaAreaIntraByDocumento(Documento documento) {
		logger.debug("--> Enter caricaAreaIntraByDocumento");
		start("caricaAreaIntraByDocumento");
		AreaIntra result = null;
		try {
			result = intraService.caricaAreaIntraByDocumento(documento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAreaIntraByDocumento");
		}
		logger.debug("--> Exit caricaAreaIntraByDocumento ");
		return result;
	}

	@Override
	public DichiarazioneIntra caricaDichiarazioneIntra(int id) {
		logger.debug("--> Enter caricaDichiarazioneIntra");
		start("caricaDichiarazioneIntra");
		DichiarazioneIntra result = null;
		try {
			result = intraService.caricaDichiarazioneIntra(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaDichiarazioneIntra");
		}
		logger.debug("--> Exit caricaDichiarazioneIntra ");
		return result;
	}

	@Override
	public List<DichiarazioneIntra> caricaDichiarazioniIntra() {
		logger.debug("--> Enter caricaDichiarazioniIntra");
		start("caricaDichiarazioniIntra");
		List<DichiarazioneIntra> result = null;
		try {
			result = intraService.caricaDichiarazioniIntra();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaDichiarazioniIntra");
		}
		logger.debug("--> Exit caricaDichiarazioniIntra ");
		return result;
	}

	@Override
	public List<DichiarazioneIntra> caricaDichiarazioniIntraDaPresentare() {
		logger.debug("--> Enter caricaDichiarazioniIntraDaPresentare");
		start("caricaDichiarazioniIntraDaPresentare");
		List<DichiarazioneIntra> result = null;
		try {
			result = intraService.caricaDichiarazioniIntraDaPresentare();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaDichiarazioniIntraDaPresentare");
		}
		logger.debug("--> Exit caricaDichiarazioniIntraDaPresentare ");
		return result;
	}

	@Override
	public List<Documento> caricaDocumentiSenzaIntra(ParametriRicercaAreaIntra parametri) {
		logger.debug("--> Enter caricaDocumentiSenzaIntra");
		start("caricaDocumentiSenzaIntra");
		List<Documento> result = null;
		try {
			result = intraService.caricaDocumentiSenzaIntra(parametri);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaDocumentiSenzaIntra");
		}
		logger.debug("--> Exit caricaDocumentiSenzaIntra ");
		return result;
	}

	@Override
	public List<FileDichiarazione> caricaFileDichiarazioni() {
		logger.debug("--> Enter caricaFileDichiarazioni");
		start("caricaFileDichiarazioni");
		List<FileDichiarazione> result = null;
		try {
			result = intraService.caricaFileDichiarazioni();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaFileDichiarazioni");
		}
		logger.debug("--> Exit caricaFileDichiarazioni ");
		return result;
	}

	@Override
	public IntraSettings caricaIntraSettings() {
		logger.debug("--> Enter caricaIntraSettings");
		start("caricaIntraSettings");
		IntraSettings result = null;
		try {
			result = intraService.caricaIntraSettings();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaIntraSettings");
		}
		logger.debug("--> Exit caricaIntraSettings");
		return result;
	}

	@Override
	public <T extends RigaSezioneIntra> List<T> caricaRigheSezioniDichiarazione(DichiarazioneIntra dichiarazioneIntra,
			Class<T> classeSezione) {
		logger.debug("--> Enter caricaRigheSezioniDichiarazione");
		start("caricaRigheSezioniDichiarazione");
		List<T> result = null;
		try {
			result = intraService.caricaRigheSezioniDichiarazione(dichiarazioneIntra, classeSezione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaRigheSezioniDichiarazione");
		}
		logger.debug("--> Exit caricaRigheSezioniDichiarazione ");
		return result;
	}

	@Override
	public List<?> caricaServizi(Class<?> classServizio, String fieldSearch, String valueSearch) {
		logger.debug("--> Enter caricaServizi");
		start("caricaServizi");
		List<?> result = null;
		try {
			result = intraService.caricaServizi(classServizio, fieldSearch, valueSearch);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaServizi");
		}
		logger.debug("--> Exit caricaServizi");
		return result;
	}

	@Override
	public Servizio caricaServizio(Servizio servizio) {
		logger.debug("--> Enter caricaServizio");
		start("caricaServizio");
		Servizio result = null;
		try {
			result = intraService.caricaServizio(servizio);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaServizio");
		}
		logger.debug("--> Exit caricaServizio");
		return result;
	}

	@Override
	public DichiarazioneIntra compilaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		logger.debug("--> Enter generaDichiarazioneIntra");
		start("generaDichiarazioneIntra");
		DichiarazioneIntra result = null;
		try {
			result = intraService.compilaDichiarazioneIntra(dichiarazioneIntra);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("generaDichiarazioneIntra");
		}
		logger.debug("--> Exit generaDichiarazioneIntra ");
		return result;
	}

	@Override
	public DichiarazioneIntra creaDichiarazioneIntra(TipoDichiarazione tipodDichiarazione) {
		logger.debug("--> Enter creaDichiarazioneIntra");
		start("creaDichiarazioneIntra");
		DichiarazioneIntra result = null;
		try {
			result = intraService.creaDichiarazioneIntra(tipodDichiarazione);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("creaDichiarazioneIntra");
		}
		logger.debug("--> Exit creaDichiarazioneIntra ");
		return result;
	}

	@Override
	public void generaAreeIntra(List<Integer> documenti) {
		logger.debug("--> Enter generaAreeIntra");
		start("generaAreeIntra");
		try {
			intraService.generaAreeIntra(documenti);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("generaAreeIntra");
		}
		logger.debug("--> Exit generaAreeIntra ");

	}

	@Override
	public FileDichiarazione generaFileEsportazione(List<Integer> dichiarazioni, boolean salvaRisultati) {
		logger.debug("--> Enter generaFileEsportazione");
		start("generaFileEsportazione");
		FileDichiarazione result = null;
		try {
			result = intraService.generaFileEsportazione(dichiarazioni, salvaRisultati);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("generaFileEsportazione");
		}
		logger.debug("--> Exit generaFileEsportazione ");
		return result;
	}

	/**
	 * @return Returns the intraService.
	 */
	public IntraService getIntraService() {
		return intraService;
	}

	@Override
	public void importaNomenclatura(byte[] file) {
		logger.debug("--> Enter importaNomenclatura");
		start("importaNomenclatura");
		try {
			intraService.importaNomenclatura(file);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("importaNomenclatura");
		}
		logger.debug("--> Exit importaNomenclatura ");

	}

	@Override
	public void importaServizi(byte[] file) {
		logger.debug("--> Enter importaServizi");
		start("importaServizi");
		try {
			intraService.importaServizi(file);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("importaServizi");
		}
		logger.debug("--> Exit importaServizi ");

	}

	@Override
	public List<AreaContabile> ricercaAreeContabiliConIntra(ParametriRicercaAreaIntra parametri) {
		logger.debug("--> Enter ricercaAreeContabiliConIntra");
		start("ricercaAreeContabiliConIntra");
		List<AreaContabile> result = null;
		try {
			result = intraService.ricercaAreeContabiliConIntra(parametri);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("ricercaAreeContabiliConIntra");
		}
		logger.debug("--> Exit ricercaAreeContabiliConIntra ");
		return result;
	}

	@Override
	public AreaIntra salvaAreaIntra(AreaIntra areaIntra) {
		logger.debug("--> Enter salvaAreaIntra");
		start("salvaAreaIntra");
		try {
			areaIntra = intraService.salvaAreaIntra(areaIntra);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaAreaIntra");
		}
		logger.debug("--> Exit salvaAreaIntra ");
		return areaIntra;
	}

	@Override
	public DichiarazioneIntra salvaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		return intraService.salvaDichiarazioneIntra(dichiarazioneIntra);
	}

	@Override
	public IntraSettings salvaIntraSettings(IntraSettings intraSettingsToSave) {
		logger.debug("--> Enter salvaIntraSettings");
		start("salvaIntraSettings");
		IntraSettings result = null;
		try {
			result = intraService.salvaIntraSettings(intraSettingsToSave);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaIntraSettings");
		}
		logger.debug("--> Exit salvaIntraSettings");
		return result;
	}

	@Override
	public RigaSezioneIntra salvaRigaSezioneDichiarazione(RigaSezioneIntra riga) {
		logger.debug("--> Enter salvaRigaSezioneDichiarazione");
		start("salvaRigaSezioneDichiarazione");
		RigaSezioneIntra result = null;
		try {
			result = intraService.salvaRigaSezioneDichiarazione(riga);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaRigaSezioneDichiarazione");
		}
		logger.debug("--> Exit salvaRigaSezioneDichiarazione ");
		return result;
	}

	@Override
	public Servizio salvaServizio(Servizio nomenclatura) {
		logger.debug("--> Enter salvaServizio");
		start("salvaServizio");
		Servizio result = null;
		try {
			result = intraService.salvaServizio(nomenclatura);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaServizio");
		}
		logger.debug("--> Exit salvaServizio");
		return result;
	}

	/**
	 * @param intraService
	 *            The intraService to set.
	 */
	public void setIntraService(IntraService intraService) {
		this.intraService = intraService;
	}

	@Override
	public void spedisciFileEsportazione(FileDichiarazione fileDichiarazione) {
	}

	@Override
	public void spedisciFileEsportazione(List<Integer> dichiarazioni) {
		logger.debug("--> Enter spedisciFileEsportazione");
		start("spedisciFileEsportazione");
		try {
			intraService.spedisciFileEsportazione(dichiarazioni);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("spedisciFileEsportazione");
		}
		logger.debug("--> Exit spedisciFileEsportazione ");
	}

}
