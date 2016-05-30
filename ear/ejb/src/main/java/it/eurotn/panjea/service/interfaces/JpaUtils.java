package it.eurotn.panjea.service.interfaces;

import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface JpaUtils {
	/**
	 *
	 * @return lista dei nomi aziende che corrisponsono al nome del jpaUnit deployati
	 */
	List<String> getAziendeDeployate();

	/**
	 *
	 * @return lista di aziendaLite che corrisponsono al nome del jpaUnit deployati.<NB/> L'unico campo fillato è il
	 *         codice
	 */
	List<AziendaLite> getAziendeLiteDeployate();

	/**
	 *
	 * @return lista delle azienda lite deployate tramite ds .L'oggetto contiene solamente il codice azienda recuperato
	 *         dal DS
	 */
	List<AziendaLite> getAziendeLiteDSDeployate();
}
