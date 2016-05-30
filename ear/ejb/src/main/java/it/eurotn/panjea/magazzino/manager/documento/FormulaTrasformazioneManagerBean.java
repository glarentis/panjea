package it.eurotn.panjea.magazzino.manager.documento;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.exception.FormuleLinkateException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.FormulaTrasformazioneManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.FormulaTrasformazioneManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FormulaTrasformazioneManagerBean")
public class FormulaTrasformazioneManagerBean implements FormulaTrasformazioneManager {

	private static Logger logger = Logger.getLogger(FormulaTrasformazioneManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@Override
	public void cancellaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione) {
		try {
			panjeaDAO.delete(formulaTrasformazione);
		} catch (VincoloException e) {
			logger.debug("Errore nel cancellare. Vincolo violato. Id Formula " + formulaTrasformazione.getId());
			throw new RuntimeException(e);
		} catch (DAOException e) {
			logger.error("--> errore nel cancellare la formula " + formulaTrasformazione, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public FormulaTrasformazione caricaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione) {
		FormulaTrasformazione formulaTrasformazioneResult = null;
		try {
			formulaTrasformazioneResult = panjeaDAO.load(FormulaTrasformazione.class, formulaTrasformazione.getId());
		} catch (ObjectNotFoundException e) {
			logger.warn("Non ho trovato la formula" + formulaTrasformazione.getId());
			throw new RuntimeException(e);
		}
		return formulaTrasformazioneResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FormulaTrasformazione> caricaFormuleTrasformazione(String codice) {

		StringBuilder sb = new StringBuilder("from FormulaTrasformazione where codiceAzienda= :azienda ");
		if (codice != null) {
			sb.append(" and codice like ").append(PanjeaEJBUtil.addQuote(codice));
		}
		sb.append(" order by codice");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("azienda", getAzienda());
		return query.getResultList();
	}

	/**
	 * @return codice dell'azienda dell'utente loggato
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	public FormulaTrasformazione salvaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione) {
		if (formulaTrasformazione.getCodiceAzienda() == null) {
			formulaTrasformazione.setCodiceAzienda(getAzienda());
		}
		FormulaTrasformazione formulaTrasformazioneResult = null;
		try {
			formulaTrasformazioneResult = panjeaDAO.save(formulaTrasformazione);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		return formulaTrasformazioneResult;
	}

	@Override
	public void verificaFormuleLinkate(TipoAttributo tipoAttributo, FormulaTrasformazione formulaTrasformazione,
			Map<TipoAttributo, FormulaTrasformazione> formuleTipiAttributo) throws FormuleLinkateException {

		Map<TipoAttributo, FormulaTrasformazione> tipiAttributiConFormulaLinkata = new HashMap<TipoAttributo, FormulaTrasformazione>();

		String codiceTipoAttributo = tipoAttributo.getCodice();

		// carico i codici dei tipi attributo contenuti nella formula
		Set<String> codiciTipiAttributo = formulaTrasformazione.getCodiceTipiAttributi(false);

		for (Entry<TipoAttributo, FormulaTrasformazione> entry : formuleTipiAttributo.entrySet()) {
			if (codiciTipiAttributo.contains(entry.getKey().getCodice())) {
				if (formulaTrasformazione.getCodiceTipiAttributi(false).contains(codiceTipoAttributo)) {
					tipiAttributiConFormulaLinkata.put(entry.getKey(), entry.getValue());
				}
			}
		}

		if (!tipiAttributiConFormulaLinkata.isEmpty()) {
			throw new FormuleLinkateException(tipoAttributo, formulaTrasformazione, tipiAttributiConFormulaLinkata);
		}

	}
}
