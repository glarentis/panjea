package it.eurotn.panjea.anagrafica.rich.pm;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;

import java.util.ArrayList;
import java.util.List;

public class SediEntitaPM {

	private Entita entita;

	private List<SedeEntita> listSedeEntita = null;

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param entita
	 *            entita
	 * @param anagraficaBD
	 *            anagraficaBD
	 */
	public SediEntitaPM(final Entita entita, final IAnagraficaBD anagraficaBD) {
		this.entita = entita;
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @return domainClassName delle sedi entità
	 */
	public String getDomainClassName() {
		if (listSedeEntita.size() > 0) {
			return listSedeEntita.get(0).getDomainClassName();
		} else {
			return null;
		}
	}

	/**
	 * @return the entita
	 */
	public Entita getEntita() {
		return entita;
	}

	/**
	 * @return the listSedeEntita
	 */
	public List<SedeEntita> getListSedeEntita() {
		if (listSedeEntita == null) {
			if (this.entita.isNew()) {
				listSedeEntita = new ArrayList<SedeEntita>();
			} else {
				listSedeEntita = anagraficaBD.caricaSediSecondarieEntita(this.entita);
			}
		}
		return listSedeEntita;
	}

	/**
	 * @param listSedeEntita
	 *            the listSedeEntita to set
	 */
	public void setListSedeEntita(List<SedeEntita> listSedeEntita) {
		this.listSedeEntita = listSedeEntita;
	}

}
