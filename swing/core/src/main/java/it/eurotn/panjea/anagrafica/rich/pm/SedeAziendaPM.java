package it.eurotn.panjea.anagrafica.rich.pm;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;

import java.util.ArrayList;
import java.util.List;

public class SedeAziendaPM {

	private Azienda azienda;

	private List<SedeAzienda> listSedeAzienda = null;

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param azienda
	 *            azienda
	 * @param anagraficaBD
	 *            anagraficaBD
	 */
	public SedeAziendaPM(final Azienda azienda, final IAnagraficaBD anagraficaBD) {
		this.azienda = azienda;
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @return the azienda
	 */
	public Azienda getAzienda() {
		return azienda;
	}

	/**
	 * @return domainClassName delle sedi azienda
	 */
	public String getDomainClassName() {
		if (listSedeAzienda.size() > 0) {
			return listSedeAzienda.get(0).getDomainClassName();
		} else {
			return null;
		}
	}

	/**
	 * @return the listSedeAzienda
	 */
	public List<SedeAzienda> getListSedeEntita() {
		if (listSedeAzienda == null) {
			if (this.azienda.isNew()) {
				listSedeAzienda = new ArrayList<SedeAzienda>();
			} else {
				listSedeAzienda = anagraficaBD.caricaSediAzienda(this.azienda);
			}
		}
		return listSedeAzienda;
	}

	/**
	 * @param listSedeAzienda
	 *            the listSedeAzienda to set
	 */
	public void setListSedeAzienda(List<SedeAzienda> listSedeAzienda) {
		this.listSedeAzienda = listSedeAzienda;
	}
}
