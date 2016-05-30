/**
 * 
 */
package it.eurotn.panjea.preventivi.manager;

import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.manager.interfaces.RigheCollegateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.PreventiviRigheCollegateManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PreventiviRigheCollegateManager")
public class RigheCollegateManagerBean implements RigheCollegateManager {

	private static Logger logger = Logger.getLogger(RigheCollegateManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public List<RigaDestinazione> caricaRigheCollegate(RigaPreventivo rigaPreventivo) {
		return caricaRigheOrdineCollegate(rigaPreventivo);
	}

	/**
	 * 
	 * @param rigaPreventivo
	 *            .
	 * @return righe ordine collegate
	 */
	@SuppressWarnings("unchecked")
	private List<RigaDestinazione> caricaRigheOrdineCollegate(RigaPreventivo rigaPreventivo) {

		List<RigaDestinazione> righeDest = new ArrayList<RigaDestinazione>();

		StringBuilder sb = new StringBuilder();
		sb.append("select doc.tipoDocumento.codice as codiceTipoDocumento, ");
		sb.append("doc.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
		sb.append("doc.tipoDocumento.id as idTipoDocumento, ");
		sb.append("doc.codice as numeroDocumento, ");
		sb.append("doc.dataDocumento as dataDocumento, ");
		sb.append("doc.id as idDocumento, ");
		sb.append("ra.id as idRiga, ");
		sb.append("ra.areaOrdine.id as idAreaOrdine, ");
		sb.append("art.codice as codiceArticolo, ");
		sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
		sb.append("ra.qta as quantita, ");
		sb.append("ra.numeroDecimaliQta as numeroDecimaliQta ");
		sb.append("from RigaArticoloOrdine ra inner join ra.articolo art inner join ra.areaOrdine.documento doc ");
		sb.append("where ra.rigaPreventivoCollegata = :paramRiga ");
		sb.append("order by doc.dataDocumento,doc.tipoDocumento.codice,doc.codice.codiceOrder ");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		((QueryImpl) query).getHibernateQuery()
				.setResultTransformer(Transformers.aliasToBean((RigaDestinazione.class)));

		query.setParameter("paramRiga", rigaPreventivo);

		try {
			righeDest = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error(
					"--> errore durante il caricamento delle righe destinazione della riga " + rigaPreventivo.getId(),
					e);
			throw new RuntimeException("errore durante il caricamento delle righe destinazione della riga "
					+ rigaPreventivo.getId(), e);
		}
		return righeDest;
	}

}
