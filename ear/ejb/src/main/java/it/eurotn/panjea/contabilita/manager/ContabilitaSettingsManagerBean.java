package it.eurotn.panjea.contabilita.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.contabilita.domain.CodiceIvaCorrispettivo;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings.ETipoPeriodicita;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Manager per la gestione del <code>ContabilitaSettings</code> roles: visualizzaContabilitaSettings,
 * modificaContabilitaSettings, visualizzaCorrispettivi, modificaCorrispettivi, cancellaCorrispettivi.
 * 
 * @author Leonardo
 */
@Stateless(name = "Panjea.ContabilitaSettingsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ContabilitaSettingsManager")
public class ContabilitaSettingsManagerBean implements ContabilitaSettingsManager {

	private static Logger logger = Logger.getLogger(ContabilitaSettingsManagerBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext context;

	@Override
	public void cancellaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo) {
		logger.debug("--> Enter cancellaCodiceIvaCorrispettivo");

		try {
			panjeaDAO.delete(codiceIvaCorrispettivo);
		} catch (Exception ex) {
			logger.error("--> Errore durante la cancellazione del codice iva corrispettivo.", ex);
			throw new RuntimeException("Errore durante la cancellazione del codice iva corrispettivo.", ex);
		}

		logger.debug("--> Exit cancellaCodiceIvaCorrispettivo");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CodiceIvaCorrispettivo> caricaCodiciIvaCorrispettivo(TipoDocumento tipoDocumento) {
		logger.debug("--> Enter caricaCodiciIvaCorrispettivo");

		List<CodiceIvaCorrispettivo> listcCodiceIvaCorrispettivo = new ArrayList<CodiceIvaCorrispettivo>();

		try {
			Query query = panjeaDAO.prepareNamedQuery("CodiceIvaCorrispettivo.caricaCodiciByTipoDocumento");
			query.setParameter("paramCodiceAzienda", getAzienda());
			query.setParameter("paramIdTipoDocumento", tipoDocumento.getId());

			listcCodiceIvaCorrispettivo = panjeaDAO.getResultList(query);
		} catch (DAOException ex) {
			logger.error("--> Errore durante il caricamento dei codici iva corrispettivo per il tipoDocumento "
					+ tipoDocumento.getId(), ex);
			throw new RuntimeException(
					"Errore durante il caricamento dei codici iva corrispettivo per il tipoDocumento "
							+ tipoDocumento.getId());
		}

		logger.debug("--> Exit caricaCodiciIvaCorrispettivo");
		return listcCodiceIvaCorrispettivo;
	}

	@Override
	public ContabilitaSettings caricaContabilitaSettings() {
		logger.debug("--> Enter caricaContabilitaSettings");

		Query query = panjeaDAO.prepareNamedQuery("ContabilitaSettings.caricaAll");
		query.setParameter("codiceAzienda", getAzienda());

		ContabilitaSettings contabilitaSettings = null;

		// carico il settings, se non lo trovo ne creo uno di default
		try {
			contabilitaSettings = (ContabilitaSettings) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> ContabilitaSettings non trovato, ne creo uno nuovo");
			// se non trovo i settings impostati scelgo io di default i
			// parametri obbligatori
			// usati nell'applicazione
			contabilitaSettings = new ContabilitaSettings();
			contabilitaSettings.setCodiceAzienda(getAzienda());
			contabilitaSettings.setCodiceIvaPerVentilazione(null);
			contabilitaSettings.setTipoPeriodicita(ETipoPeriodicita.MENSILE);
			contabilitaSettings = salvaContabilitaSettings(contabilitaSettings);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento del contabilita settings", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit caricaContabilitaSettings");
		return contabilitaSettings;
	}

	/**
	 * Recupera il codice azienda dell'utente autenticato nel context.
	 * 
	 * @return String codice azienda
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	public CodiceIvaCorrispettivo salvaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo) {
		logger.debug("--> Enter salvaCodiceIvaCorrispettivo");

		CodiceIvaCorrispettivo codiceIvaCorrispettivoSalvato = null;
		// aggiungo il codice azienda
		codiceIvaCorrispettivo.setCodiceAzienda(getAzienda());

		try {
			codiceIvaCorrispettivoSalvato = panjeaDAO.save(codiceIvaCorrispettivo);
		} catch (Exception ex) {
			logger.error("--> Errore durante il salvataggio del codice iva corrispettivo.", ex);
			throw new RuntimeException("Errore durante il salvataggio del codice iva corrispettivo.", ex);
		}

		logger.debug("--> Exit salvaCodiceIvaCorrispettivo");
		return codiceIvaCorrispettivoSalvato;
	}

	@Override
	public ContabilitaSettings salvaContabilitaSettings(ContabilitaSettings contabilitaSettings) {
		logger.debug("--> Enter salvaContabilitaSettings");

		ContabilitaSettings contabilitaSettingsSalvato;
		try {
			contabilitaSettingsSalvato = panjeaDAO.save(contabilitaSettings);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio di ContabilitaSettings", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit salvaContabilitaSettings");
		return contabilitaSettingsSalvato;
	}
}
