package it.eurotn.panjea.intra.manager.esportazione;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.IntraSettings;
import it.eurotn.panjea.intra.domain.RigaSezione1Intra;
import it.eurotn.panjea.intra.domain.RigaSezione2Intra;
import it.eurotn.panjea.intra.domain.RigaSezione3Intra;
import it.eurotn.panjea.intra.domain.RigaSezione4Intra;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.domain.TipoPeriodo;
import it.eurotn.panjea.intra.domain.TotaliDichiarazione;
import it.eurotn.panjea.intra.domain.dichiarazione.FileDichiarazione;
import it.eurotn.panjea.intra.manager.esportazione.interfaces.FileDichiarazioneManager;
import it.eurotn.panjea.intra.manager.interfaces.DichiarazioneIntraManager;
import it.eurotn.panjea.intra.manager.interfaces.IntraSettingsManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.FileDichiarazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FileDichiarazioneManager")
public class FileDichiarazioneManagerBean implements FileDichiarazioneManager {

	private static Logger logger = Logger.getLogger(FileDichiarazioneManagerBean.class);
	private static final String DIR_TEMPLATE = "dirTemplate";
	private static final String FILE_NAME_TEMPLATE = "INTRA.xml";

	@EJB
	private DichiarazioneIntraManager dichiarazioneIntraManager;

	@EJB
	private PreferenceService preferenceService;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private IntraSettingsManager intraSettingsManager;

	@Override
	public FileDichiarazione aggiornaNomeFileDichiarazione(int idFileDichiarazione, String nome) {
		FileDichiarazione fileDichiarazione = null;
		try {
			fileDichiarazione = panjeaDAO.load(FileDichiarazione.class, idFileDichiarazione);
			fileDichiarazione.setNome(nome);
			fileDichiarazione = panjeaDAO.save(fileDichiarazione);
			fileDichiarazione.getDichiarazioni().size();
		} catch (ObjectNotFoundException e) {
			logger.error("-->errore nel cambiare il nome al filedichiarazione", e);
			throw new RuntimeException(e);
		} catch (DAOException e) {
			logger.error("-->errore nel salvare il file della dichiarazione", e);
			throw new RuntimeException();
		}
		return fileDichiarazione;
	}

	@Override
	public void cancellaFileDichiarazioni(FileDichiarazione fileDichiarazione) {
		try {
			fileDichiarazione = panjeaDAO.load(FileDichiarazione.class, fileDichiarazione.getId());
			Set<DichiarazioneIntra> dic = fileDichiarazione.getDichiarazioni();
			if (dic != null) {
				for (DichiarazioneIntra dichiarazioneIntra : dic) {
					dichiarazioneIntra.setFileDichiarazione(null);
				}
			}
			panjeaDAO.delete(fileDichiarazione);
		} catch (DAOException e) {
			logger.error("-->errore nel cancellare il fileDic con id " + fileDichiarazione.getId(), e);
			throw new RuntimeException("-->errore nel cancellare il fileDic con id " + fileDichiarazione.getId(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileDichiarazione> caricaFileDichiarazioni() {
		Query query = panjeaDAO.prepareNamedQuery("FileDichiarazione.caricaAll");
		List<FileDichiarazione> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore nel caricare i file delle dichiarazioni", e);
			throw new RuntimeException("-->errore nel caricare i file delle dichiarazioni", e);
		}
		return result;
	}

	/**
	 * Trasforma una rigaSezioneIntra di dominio in una classe di utilità per l'esportazione.
	 * 
	 * @param rigaSezioneIntra
	 *            riga di dominio
	 * @return riga per l'esportazione
	 */
	private RigaSezioneIntra createInstanceRigaSezionePerEsportazione(RigaSezioneIntra rigaSezioneIntra) {
		if (rigaSezioneIntra.getDichiarazione().getTipoDichiarazione() == TipoDichiarazione.VENDITE) {
			if (rigaSezioneIntra instanceof RigaSezione1Intra) {
				return new RigaSezione1IntraVendite((RigaSezione1Intra) rigaSezioneIntra);
			}
			if (rigaSezioneIntra instanceof RigaSezione2Intra) {
				return new RigaSezione2IntraVendite((RigaSezione2Intra) rigaSezioneIntra);
			}
			if (rigaSezioneIntra instanceof RigaSezione3Intra) {
				return new RigaSezione3IntraVendite((RigaSezione3Intra) rigaSezioneIntra);
			}
			if (rigaSezioneIntra instanceof RigaSezione4Intra) {
				return new RigaSezione4IntraVendite((RigaSezione4Intra) rigaSezioneIntra);
			}
		} else {
			return rigaSezioneIntra;
		}
		throw new IllegalArgumentException("tipo non previsto");
	}

	@Override
	public FileDichiarazione generaFileEsportazione(List<Integer> idDichiarazioni, boolean salvaRisultati)
			throws PreferenceNotFoundException {

		FileDichiarazione fileDichiarazione = new FileDichiarazione(); //
		// Salvo subito il file della dichiarazione
		// altrimenti quando calcolo i totali (dichiarazioneIntraManager.calcolaTotaliDichiarazione(idDichiarazione))
		// viene fattu un flush della sessione, la dichiarazione è dirty perchè gli ho settato il file, il container
		// genera un'update
		// ma fallisce perchè filedichiaraziohe è transiente.
		try {
			fileDichiarazione = panjeaDAO.save(fileDichiarazione);
		} catch (DAOException e) {
			logger.error("-->errore nel salvare il file della dichiarazione", e);
			throw new RuntimeException();
		}
		File fileTmp = null;
		try {
			fileTmp = File.createTempFile("intra", "cee.txt");
		} catch (IOException e) {
			logger.error("-->errore nel creare il file temporaneo", e);
			throw new RuntimeException(e);
		}

		IntraSettings settings = intraSettingsManager.caricaIntraSettings();
		BeanWriter out = getWriter(fileTmp, settings.getTipoPeriodo());
		// Carico le dichiarazioni da esportare
		// List<DichiarazioneIntra> dichiarazioni = new ArrayList<DichiarazioneIntra>();
		Intestazione intestazione = getIntestazione(idDichiarazioni, settings);
		out.write(intestazione);
		for (Integer idDichiarazione : idDichiarazioni) {
			DichiarazioneIntra dichiarazione = dichiarazioneIntraManager.caricaDichiarazioneIntra(idDichiarazione);
			TotaliDichiarazione totali = dichiarazioneIntraManager.calcolaTotaliDichiarazione(idDichiarazione);
			if (salvaRisultati) {
				fileDichiarazione.addDichiarazione(dichiarazione);
			}
			dichiarazione.setTotali(totali);
			out.write(dichiarazione);
			scriviRighe(out,
					dichiarazioneIntraManager.caricaRigheSezioniDichiarazione(dichiarazione, RigaSezione1Intra.class));
			scriviRighe(out,
					dichiarazioneIntraManager.caricaRigheSezioniDichiarazione(dichiarazione, RigaSezione2Intra.class));
			scriviRighe(out,
					dichiarazioneIntraManager.caricaRigheSezioniDichiarazione(dichiarazione, RigaSezione3Intra.class));
			scriviRighe(out,
					dichiarazioneIntraManager.caricaRigheSezioniDichiarazione(dichiarazione, RigaSezione4Intra.class));
		}
		out.flush();
		out.close();
		byte[] byteStream = null;
		try {
			byteStream = IOUtils.toByteArray(new FileInputStream(fileTmp));
		} catch (Exception e) {
			logger.error("-->errore nel trasformare il file temporaneo in byte[]", e);
			throw new RuntimeException("-->errore nel trasformare il file temporaneo in byte[]", e);
		}
		fileDichiarazione.setContent(byteStream);
		fileDichiarazione.setNome(intestazione.getNomeFlusso());
		if (salvaRisultati) {
			try {
				fileDichiarazione = panjeaDAO.save(fileDichiarazione);
			} catch (DAOException e) {
				logger.error("-->errore nel salvare il file della dichiarazione", e);
				throw new RuntimeException();
			}
		}
		fileTmp.delete();
		return fileDichiarazione;
	}

	/**
	 * 
	 * @param idDichiarazioni
	 *            lista di dichiarazioni da esportare
	 * @param settings
	 *            settings per l'intra;
	 * @return record di intestazione
	 */
	private Intestazione getIntestazione(List<Integer> idDichiarazioni, IntraSettings settings) {
		Intestazione intestazione = new Intestazione();
		intestazione.setCodiceUA(settings.getCodiceUA());
		intestazione.setProgressivoGiorno(1);
		intestazione.setSezioneDoganale(settings.getSezioneDoganale());
		intestazione.setCodiceFiscaleSpedizionere(settings.getCodiceFiscaleSpedizionere());
		intestazione.setProgessivoSede(settings.getProgressivoSede());

		// calcolo il numero record presenti nel flusso
		int numRecord = 1;
		for (Integer idDichiarazione : idDichiarazioni) {
			DichiarazioneIntra dichiarazione = dichiarazioneIntraManager.caricaDichiarazioneIntra(idDichiarazione);
			numRecord++;
			List<RigaSezione1Intra> righe1 = dichiarazioneIntraManager.caricaRigheSezioniDichiarazione(dichiarazione,
					RigaSezione1Intra.class);
			numRecord += righe1.size();
			List<RigaSezione2Intra> righe2 = dichiarazioneIntraManager.caricaRigheSezioniDichiarazione(dichiarazione,
					RigaSezione2Intra.class);
			numRecord += righe2.size();
			List<RigaSezione3Intra> righe3 = dichiarazioneIntraManager.caricaRigheSezioniDichiarazione(dichiarazione,
					RigaSezione3Intra.class);
			numRecord += righe3.size();
			List<RigaSezione4Intra> righe4 = dichiarazioneIntraManager.caricaRigheSezioniDichiarazione(dichiarazione,
					RigaSezione4Intra.class);
			numRecord += righe4.size();
		}
		intestazione.setNumeroRecord(numRecord);
		return intestazione;
	}

	/**
	 * @return factoryStream per export.
	 * @param fileTmp
	 *            file temporaneo dove salvare i dati.
	 * @param tipoPeriodo
	 *            della dichiarazione
	 * @throws PreferenceNotFoundException
	 *             chiave della cartella contenente il template non trovata
	 */
	protected BeanWriter getWriter(File fileTmp, TipoPeriodo tipoPeriodo) throws PreferenceNotFoundException {
		// Recupero la cartella dove sono i file di template
		Preference dirTemplatePreference = null;
		StreamFactory factory = null;
		dirTemplatePreference = preferenceService.caricaPreference(DIR_TEMPLATE);
		String templateFile = FilenameUtils.concat(dirTemplatePreference.getValore(), FILE_NAME_TEMPLATE);
		factory = StreamFactory.newInstance();
		factory.load(templateFile);
		BeanWriter out;
		String writerName = "intraMensile";
		if (tipoPeriodo == TipoPeriodo.T) {
			writerName = "intraTrimestre";
		}
		try {
			out = factory.createWriter(writerName, fileTmp);
		} catch (Exception e) {
			logger.error("-->errore nel creare il writer per l'intra", e);
			throw new RuntimeException(e);
		}
		return out;
	}

	/**
	 * 
	 * @param out
	 *            stream dove scrivere l'output
	 * @param caricaRigheSezioniDichiarazione
	 *            righe da scrivere nel file
	 */
	private void scriviRighe(BeanWriter out, List<? extends RigaSezioneIntra> caricaRigheSezioniDichiarazione) {
		int nRiga = 1;
		for (RigaSezioneIntra rigaSezioneIntra : caricaRigheSezioniDichiarazione) {
			// Se ho una dichiarazione di vendita devo trasformare le righe in rigaSezioneIntraVendite
			// solamente per l'esportazione in modo che nel file template di esportazione di BeanIO possa identificare
			// correttamente
			// il <record> corretto
			RigaSezioneIntra rigaPerEsportazione = createInstanceRigaSezionePerEsportazione(rigaSezioneIntra);
			rigaPerEsportazione.setProgressivo(nRiga);
			out.write(rigaPerEsportazione);
			nRiga++;
		}
	}

	@Override
	public void spedisciFileEsportazione(FileDichiarazione fileDichiarazione) {

	}

	@Override
	public void spedisciFileEsportazione(List<Integer> dichiarazioni) throws PreferenceNotFoundException {
		FileDichiarazione fileDichiarazione = generaFileEsportazione(dichiarazioni, true);
	}

}
