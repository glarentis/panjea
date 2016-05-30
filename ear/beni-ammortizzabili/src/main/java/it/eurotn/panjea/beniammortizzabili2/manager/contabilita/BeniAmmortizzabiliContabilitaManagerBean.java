/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.manager.contabilita;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.beniammortizzabili.exception.AreeContabiliDaSimulazionePresentiException;
import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeniSettings;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.beniammortizzabili2.manager.contabilita.interfaces.BeniAmmortizzabiliContabilitaManager;
import it.eurotn.panjea.beniammortizzabili2.manager.contabilita.interfaces.GeneratoreAreaContabileManager;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.BeniAmmortizzabiliSettingsManager;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.RigheContabiliNonValidiException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.BeniAmmortizzabiliContabilitaManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.BeniAmmortizzabiliContabilitaManager")
public class BeniAmmortizzabiliContabilitaManagerBean implements BeniAmmortizzabiliContabilitaManager {

	private static Logger logger = Logger.getLogger(BeniAmmortizzabiliContabilitaManagerBean.class);

	@EJB(beanName = "BeneGeneratoreAreaContabileManagerBean")
	private GeneratoreAreaContabileManager beneGeneratoreAreaContabileManager;

	@EJB(beanName = "SpecieGeneratoreAreaContabileManagerBean")
	private GeneratoreAreaContabileManager specieGeneratoreAreaContabileManager;

	@EJB(beanName = "SottospecieGeneratoreAreaContabileManagerBean")
	private GeneratoreAreaContabileManager sottospecieGeneratoreAreaContabileManager;

	@EJB
	private BeniAmmortizzabiliSettingsManager beniAmmortizzabiliSettingsManager;

	@EJB
	private TipiAreaContabileManager tipiAreaContabileManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	private AreaContabileCancellaManager areaContabileCancellaManager;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@Override
	public void cancellaAreeContabiliSimulazione(Integer idSimulazione) {

		List<AreaContabile> areeContabili = caricaAreeContabiliSimulazione(idSimulazione);

		for (AreaContabile areaContabile : areeContabili) {
			try {
				areaContabileCancellaManager.cancellaAreaContabile(areaContabile, true, true);
			} catch (Exception e) {
				logger.error("--> errore durante la cancellazione dell'area contabile.", e);
				throw new RuntimeException("errore durante la cancellazione dell'area contabile.", e);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AreaContabile> caricaAreeContabiliSimulazione(Integer idSimulazione) {
		List<AreaContabile> areeContabili = new ArrayList<AreaContabile>();

		// senza verificare se l'area contabile è unica o per politica di calcolo le carico direttamente entrambe, tanto
		// oslo 1 esiste
		// AC simulazione
		StringBuilder sb = new StringBuilder(200);
		sb.append("select ac from AreaContabile ac, Simulazione sim where ac.id = sim.areaContabile.id and sim.id = :paramIdSimulazione");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdSimulazione", idSimulazione);
		try {
			areeContabili.addAll(panjeaDAO.getResultList(query));
		} catch (DAOException e1) {
			logger.error("--> errore durante il caricamento dell'area contabile della simulazione.", e1);
			throw new RuntimeException("errore durante il caricamento dell'area contabile della simulazione.", e1);
		}
		// AC politiche calcolo
		sb = new StringBuilder(200);
		sb.append("select ac from AreaContabile ac, PoliticaCalcolo pc where ac.id = pc.areaContabile.id and pc.simulazione.id = :paramIdSimulazione");
		query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramIdSimulazione", idSimulazione);
		try {
			areeContabili.addAll(panjeaDAO.getResultList(query));
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento delle aree contabili delle politiche di calcolo.", e);
			throw new RuntimeException(
					"errore durante il caricamento delle aree contabili delle politiche di calcolo.", e);
		}

		return areeContabili;
	}

	@Override
	public void confermaAreeContaibliSimulazione(Integer idSimulazione) throws ContabilitaException,
			RigheContabiliNonValidiException {

		List<AreaContabile> areeContabili = caricaAreeContabiliSimulazione(idSimulazione);

		for (AreaContabile areaContabile : areeContabili) {

			if (areaContabile.getStatoAreaContabile() == StatoAreaContabile.PROVVISORIO
					|| areaContabile.getStatoAreaContabile() == StatoAreaContabile.SIMULATO) {
				areaContabile.setStatoAreaContabile(StatoAreaContabile.PROVVISORIO);
				areaContabileManager.validaRigheContabili(areaContabile);
			}
		}
	}

	@Override
	public void creaAreeContabili(Integer idSimulazione) throws AreeContabiliDaSimulazionePresentiException,
			SottocontiBeniNonValidiException {

		Simulazione simulazione;
		try {
			simulazione = panjeaDAO.load(Simulazione.class, idSimulazione);
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore durante il caricamento della simulazione", e);
			throw new RuntimeException("errore durante il caricamento della simulazione", e);
		}

		// se ci sono già aree contabili presenti mi fermo
		if (isAreeContabiliSimulazionePresenti(simulazione.getAnno())) {
			throw new AreeContabiliDaSimulazionePresentiException(simulazione.getAnno());
		}

		// carico il tipo area contabile dai documenti base
		TipoAreaContabile tipoAreaContabile = getTipoAreaContabilePerSimulazione();

		getGeneratoreAreaContabile().creaAreeContabili(idSimulazione, tipoAreaContabile);
	}

	/**
	 * Restituisce il generatore da utilizzare in base alle settings dei beni.
	 *
	 * @return generatore
	 */
	private GeneratoreAreaContabileManager getGeneratoreAreaContabile() {

		BeniSettings beniSettings = beniAmmortizzabiliSettingsManager.caricaBeniSettings();

		GeneratoreAreaContabileManager generatore = null;
		switch (beniSettings.getTipoRaggruppamentoACSimulazione()) {
		case BENE:
			generatore = beneGeneratoreAreaContabileManager;
			break;
		case SPECIE:
			generatore = specieGeneratoreAreaContabileManager;
			break;
		case SOTTOSPECIE:
			generatore = sottospecieGeneratoreAreaContabileManager;
			break;
		default:
			throw new RuntimeException("Tipo raggruppamento AC Simulazione beni non previsto.");
		}
		return generatore;
	}

	/**
	 * Restituisce il tipo area contabile da utilizzare per la generazione dell'area contabile dalla simulazione.
	 *
	 * @return {@link TipoAreaContabile}
	 */
	private TipoAreaContabile getTipoAreaContabilePerSimulazione() {

		TipoAreaContabile tipoAreaContabile = null;

		List<TipoDocumentoBase> tipiDocumentoBase = tipiAreaContabileManager.caricaTipiDocumentoBase();
		for (TipoDocumentoBase tipoDocumentoBase : tipiDocumentoBase) {
			if (tipoDocumentoBase.getTipoOperazione() == TipoOperazioneTipoDocumento.AC_SIMULAZIONE_BENI) {
				tipoAreaContabile = tipoDocumentoBase.getTipoAreaContabile();
				break;
			}
		}

		if (tipoAreaContabile == null) {
			throw new RuntimeException(new TipoDocumentoBaseException(
					new String[] { "Tipo operazione: Aree contabili simulazione beni " }));
		}

		return tipoAreaContabile;
	}

	/**
	 * @param anno
	 *            anno movimento
	 * @return <code>true</code> se esistono aree contabili generate dalle simulazioni nell'anno movimento indicato
	 */
	private boolean isAreeContabiliSimulazionePresenti(Integer anno) {

		StringBuilder sb = new StringBuilder(150);
		sb.append("select  count(ac.id) from AreaContabile ac where ac.annoMovimento = :paramAnno and ac.datiGenerazione.tipoGenerazione = :paramTipoGenerazione");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramAnno", anno);
		query.setParameter("paramTipoGenerazione", TipoGenerazione.SIMULAZIONE_BENI);

		Long areePresenti;
		try {
			areePresenti = (Long) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento delle aree contabili delle simulazioni", e);
			throw new RuntimeException("errore durante il caricamento delle aree contabili delle simulazioni", e);
		}

		return areePresenti > 0;
	}
}
