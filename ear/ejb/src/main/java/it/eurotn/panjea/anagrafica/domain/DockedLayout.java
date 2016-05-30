package it.eurotn.panjea.anagrafica.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue("D")
@NamedQueries( {
		@NamedQuery(name = "DockedLayout.caricaByUtenteEKey", query = " from DockedLayout dl where dl.chiave = :paramKey and dl.utente = :paramUser"),
		@NamedQuery(name = "DockedLayout.caricaByDefaultKey", query = " from DockedLayout dl where dl.chiave = :paramKey and dl.utente is null") })
public class DockedLayout extends AbstractLayout {

	private static final long serialVersionUID = -1402471500495013998L;

	/**
	 * Costruttore.
	 */
	public DockedLayout() {
		super();
		setGlobal(false);
	}

}
