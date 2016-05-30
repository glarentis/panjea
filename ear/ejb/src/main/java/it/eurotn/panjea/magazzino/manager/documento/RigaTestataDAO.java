/**
 *
 */
package it.eurotn.panjea.magazzino.manager.documento;

import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.exception.RigheCollegateException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoDAO;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author leonardo
 */
@Stateless(mappedName = "Panjea.RigaTestataDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaTestataDAO")
public class RigaTestataDAO extends RigaMagazzinoAbstractDAOBean implements RigaMagazzinoDAO {

	/**
	 * Carica il numero di righe articolo collegate. Per caricare le righe collegate prende le righe articolo con la
	 * stessa area magazzino e la stessa area ordine collegata di riga testata.
	 * 
	 * @param rigaTestata
	 *            la riga testata di origine da cui recuperare area magazzino id e area ordine collegata id
	 * @return la count delle righe trovate
	 */
	private int caricaNumeroRigheCollegate(RigaTestata rigaTestata) {
		// devo ricaricarla per avere l'id dell'area ordine collegata
		rigaTestata = panjeaDAO.loadLazy(RigaTestata.class, rigaTestata.getId());
		if (rigaTestata.getAreaCollegata() == null) {
			return 0;
		}

		Integer idAreaMagazzino = rigaTestata.getAreaMagazzino().getId();
		int count = 0;
		if (rigaTestata.getRigaOrdineCollegata() == null) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(" select count(r) from RigaArticolo r ");
			stringBuilder.append(" where r.areaMagazzino.id=:idAreaMaga  ");
			if (rigaTestata.getAreaCollegata() instanceof AreaMagazzino) {
				stringBuilder.append(" and r.areaMagazzinoCollegata.id=:idAreaCollegata ");
			} else {
				stringBuilder.append(" and r.areaOrdineCollegata.id=:idAreaCollegata ");
			}
			stringBuilder.append(" group by r.areaOrdineCollegata ");

			Query query = panjeaDAO.prepareQuery(stringBuilder.toString());
			query.setParameter("idAreaMaga", idAreaMagazzino);
			query.setParameter("idAreaCollegata", rigaTestata.getAreaCollegata().getId());

			// la query non restituisce risultati se non si hanno righe quindi non posso usare la getSingleResult
			@SuppressWarnings("unchecked")
			List<Long> results = query.getResultList();
			if (results.size() > 0) {
				Long primoRisultato = results.get(0);
				count = primoRisultato.intValue();
			}
			logger.debug("trovate " + count + " righe collegate alla riga testata");
		}
		return count;
	}

	@Override
	public AreaMagazzino removeRigaMagazzino(RigaMagazzino rigaMagazzino) {
		RigaTestata rigaTestata = (RigaTestata) caricaRigaMagazzino(rigaMagazzino);
		// se ci sono delle righe collegate alla riga testata devo lanciare una eccezione di righe collegate
		// presenti
		int righeCollegate = caricaNumeroRigheCollegate(rigaTestata);
		if (righeCollegate > 0) {
			RigheCollegateException righeCollegateException = new RigheCollegateException();
			righeCollegateException.setNumeroRigheCollegate(righeCollegate);
			throw righeCollegateException;
		}

		return super.removeRigaMagazzino(rigaTestata);
	}

}
