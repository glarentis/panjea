package it.eurotn.panjea.aton.importer.service;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.aton.importer.service.interfaces.DolcelitAtonImporterService;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.DolcelitAtonImporterServiceBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DolcelitAtonImporterServiceBean")
public class DolcelitAtonImporterServiceBean implements DolcelitAtonImporterService {
	private static Logger logger = Logger.getLogger(DolcelitAtonImporterServiceBean.class);
	@EJB
	protected PanjeaDAO panjeaDAO;

	/**
	 * 
	 * @param ordineImportato
	 *            ordine da sistemare per dolcelit
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Override
	public void importa(OrdineImportato ordineImportato) {
		// Per gli articoli a quantità variabile sposto qta in pezzi.
		// Ricarico l'ordine
		try {
			ordineImportato = panjeaDAO.load(OrdineImportato.class, ordineImportato.getId());
		} catch (ObjectNotFoundException e1) {
			e1.printStackTrace();
		}
		for (RigaOrdineImportata rigaOrdineImportata : ordineImportato.getRighe().values()) {
			// Se articolo a peso variabile sposto la qta in pezzi
			if (rigaOrdineImportata.getArticolo() != null) {
				panjeaDAO.getEntityManager().merge(rigaOrdineImportata.getArticolo());
				if (rigaOrdineImportata.getArticolo().getAttributo("pesovar") != null
						&& "X".equals(rigaOrdineImportata.getArticolo().getAttributo("pesovar").getValore())) {
					String pezzi = NumberFormat.getNumberInstance().format(rigaOrdineImportata.getQta())
							.replace("'.", ",");
					rigaOrdineImportata.setAttributi(new StringBuilder("colli=").append(pezzi).toString());
					rigaOrdineImportata.setQta(0.0);
				}
				if ("7000".equals(rigaOrdineImportata.getArticolo().getCodice())) {
					rigaOrdineImportata.setPrezzoUnitarioDeterminato(rigaOrdineImportata.getPrezzoUnitario().negate());
				}
			}
			if (rigaOrdineImportata.getSconto1().compareTo(BigDecimal.ZERO) != 0) {
				Sconto sconto = rigaOrdineImportata.getScontoDeterminato();
				sconto.aggiungiInCoda(rigaOrdineImportata.getSconto1(), false);
				rigaOrdineImportata.setScontoDeterminato(sconto);
			}
		}
		try {
			panjeaDAO.save(ordineImportato);
		} catch (DAOException e) {
			logger.error("-->errore nel salvare l'ordine importato per la customizzazione di Dolcelit", e);
			throw new RuntimeException(e);
		}
	}
}