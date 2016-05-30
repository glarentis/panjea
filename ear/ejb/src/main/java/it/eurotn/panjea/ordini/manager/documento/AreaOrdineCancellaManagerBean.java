package it.eurotn.panjea.ordini.manager.documento;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineCancellaManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineManager;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 */
@Stateless(mappedName = "Panjea.AreaOrdineCancellaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaOrdineCancellaManager")
public class AreaOrdineCancellaManagerBean implements AreaOrdineCancellaManager {

	private static Logger logger = Logger.getLogger(AreaOrdineCancellaManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected AreaRateManager areaRateManager;

	@EJB
	protected DocumentiManager documentiManager;

	@EJB
	protected AreaOrdineManager areaOrdineManager;

	@IgnoreDependency
	@EJB
	private RigaOrdineManager rigaOrdineManager;

	@Override
	public void cancellaAreaOrdine(AreaOrdine areaOrdine) {
		logger.debug("--> Enter cancellaAreaOrdine");
		Documento documento = documentiManager.caricaDocumento(areaOrdine.getDocumento().getId());
		areaRateManager.cancellaAreaRate(documento);
		try {

			// Devo mettere a null tutte le righe che puntano a righeTestate collegate
			Query queryRigheTestateCollegate = panjeaDAO
					.prepareQuery("update RigaOrdine ro set ro.rigaTestataCollegata=null where ro.areaOrdine.id=:paramIdAreaOrdine");
			queryRigheTestateCollegate.setParameter("paramIdAreaOrdine", areaOrdine.getId());
			queryRigheTestateCollegate.executeUpdate();

			// rimuovo tutti i link con le righe padri per non incorrere in una vincolo exception al momento della
			// cancellazione
			Query removeLinks = panjeaDAO
					.prepareQuery("update RigaArticoloComponenteOrdine ra set ra.rigaPadre=null where ra.areaOrdine.id = :paramIdArea ");
			removeLinks.setParameter("paramIdArea", areaOrdine.getId());
			removeLinks.executeUpdate();

			SQLQuery removeRigheOrdiniCollegati = panjeaDAO
					.prepareNativeQuery("delete rocoll from ordi_righe_ordine_ordi_righe_ordine rocoll inner join ordi_righe_ordine ro on ro.id=rocoll.ordi_righe_ordine_id where ro.areaOrdine_id = :paramIdArea ");
			removeRigheOrdiniCollegati.setParameter("paramIdArea", areaOrdine.getId());
			removeRigheOrdiniCollegati.executeUpdate();

			// cancella prima i componenti e poi le righe per evitare di incorrere nel vincolo
			// RigaComponente->RigaDistinta
			Query queryComponenti = panjeaDAO.prepareNamedQuery("RigaOrdine.cancellaRigheComponentiByAreaOrdine");
			queryComponenti.setParameter("paramAreaOrdine", areaOrdine.getId());
			panjeaDAO.executeQuery(queryComponenti);

			// cancello le righe dell'area
			Query query = panjeaDAO.prepareNamedQuery("RigaOrdine.cancellaByAreaOrdine");
			query.setParameter("paramAreaOrdine", areaOrdine.getId());
			panjeaDAO.executeQuery(query);

			panjeaDAO.delete(areaOrdine);
		} catch (Exception e) {
			logger.error("-->errore nel cancellare l'area ordine per il documento con codice " + documento.getCodice(),
					e);
			throw new RuntimeException(e);
		}
		documentiManager.cancellaDocumento(documento);
		logger.debug("--> Exit cancellaAreaOrdine");
	}

	@Override
	public AreaOrdine cancellaRigaOrdine(RigaOrdine rigaOrdine) {
		return rigaOrdineManager.getDao(rigaOrdine).cancellaRigaOrdine(rigaOrdine);
	}

}
