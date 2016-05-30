package it.eurotn.panjea.magazzino.rich.editors.situazionearticoli;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;

public class SostituzioneCodiceIvaPM {

	{
		codiceIvaDaSostituire = null;
		nuovoCodiceIva = null;
	}

	private CodiceIva codiceIvaDaSostituire;

	private CodiceIva nuovoCodiceIva;

	/**
	 * Costruttore.
	 */
	public SostituzioneCodiceIvaPM() {
		super();
	}

	/**
	 * @return the codiceIvaDaSostituire
	 */
	public CodiceIva getCodiceIvaDaSostituire() {
		return codiceIvaDaSostituire;
	}

	/**
	 * @return the nuovoCodiceIva
	 */
	public CodiceIva getNuovoCodiceIva() {
		return nuovoCodiceIva;
	}

	/**
	 * @return true se i codici iva sono presenti e diversi
	 */
	public boolean isValid() {
		return codiceIvaDaSostituire != null && nuovoCodiceIva != null && !codiceIvaDaSostituire.equals(nuovoCodiceIva);
	}

	/**
	 * @param codiceIvaDaSostituire
	 *            the codiceIvaDaSostituire to set
	 */
	public void setCodiceIvaDaSostituire(CodiceIva codiceIvaDaSostituire) {
		this.codiceIvaDaSostituire = codiceIvaDaSostituire;
	}

	/**
	 * @param nuovoCodiceIva
	 *            the nuovoCodiceIva to set
	 */
	public void setNuovoCodiceIva(CodiceIva nuovoCodiceIva) {
		this.nuovoCodiceIva = nuovoCodiceIva;
	}

}