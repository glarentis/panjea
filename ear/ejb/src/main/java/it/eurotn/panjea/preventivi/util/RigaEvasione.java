/**
 * 
 */
package it.eurotn.panjea.preventivi.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @author fattazzo
 * 
 */
public class RigaEvasione implements Serializable {

	private static final long serialVersionUID = 4683602086921946293L;

	// identificativo della riga evasione sul quale viene basato il metodo equals(). Non è possibile utilizzare l'idi
	// della riga movimentazione in quanto una riga articolo può essere evasa su più righe ( es: cambio data consegna
	// per una certa quantità)
	private Long id;

	private RigaMovimentazione rigaMovimentazione;

	private Double quantitaRiga;

	private Double quantitaEvasione;

	private Date dataConsegna;

	private boolean selezionata;

	// se avvalorata significa che la riga in questione è stata creata per una nuova data di consegna dalla riga padre
	private RigaEvasione rigaPadre;

	{
		selezionata = Boolean.FALSE;
		quantitaEvasione = 0.0;
		id = Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * Costruttore.
	 */
	public RigaEvasione() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param rigaMovimentazione
	 *            riga movimentazione
	 */
	public RigaEvasione(final RigaMovimentazione rigaMovimentazione) {
		super();
		this.rigaMovimentazione = rigaMovimentazione;
		this.dataConsegna = rigaMovimentazione.getDataConsegna();
		this.quantitaRiga = rigaMovimentazione.getQuantita() - rigaMovimentazione.getQuantitaEvasa();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RigaEvasione other = (RigaEvasione) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the dataConsegna
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the quantitaEvasione
	 */
	public Double getQuantitaEvasione() {
		return quantitaEvasione;
	}

	/**
	 * @return the quantitaRiga
	 */
	public Double getQuantitaRiga() {
		return quantitaRiga;
	}

	/**
	 * @return the rigaMovimentazione
	 */
	public RigaMovimentazione getRigaMovimentazione() {
		return rigaMovimentazione;
	}

	/**
	 * @return the rigaPadre
	 */
	public RigaEvasione getRigaPadre() {
		return rigaPadre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * @return the selezionata
	 */
	public boolean isSelezionata() {
		return selezionata;
	}

	/**
	 * @param dataConsegna
	 *            the dataConsegna to set
	 */
	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * @param quantitaEvasione
	 *            the quantitaEvasione to set
	 */
	public void setQuantitaEvasione(Double quantitaEvasione) {
		if (quantitaEvasione == null) {
			quantitaEvasione = 0.0;
		}
		this.quantitaEvasione = quantitaEvasione;
		selezionata = quantitaEvasione.compareTo(0.0) != 0;
	}

	/**
	 * @param quantitaRiga
	 *            the quantitaRiga to set
	 */
	public void setQuantitaRiga(Double quantitaRiga) {
		this.quantitaRiga = quantitaRiga;
	}

	/**
	 * @param rigaMovimentazione
	 *            the rigaMovimentazione to set
	 */
	public void setRigaMovimentazione(RigaMovimentazione rigaMovimentazione) {
		this.rigaMovimentazione = rigaMovimentazione;
	}

	/**
	 * @param rigaPadre
	 *            the rigaPadre to set
	 */
	public void setRigaPadre(RigaEvasione rigaPadre) {
		this.rigaPadre = rigaPadre;
	}

	/**
	 * @param selezionata
	 *            the selezionata to set
	 */
	public void setSelezionata(boolean selezionata) {
		this.selezionata = selezionata;
		if (selezionata) {
			if (quantitaEvasione.compareTo(0.0) == 0) {
				quantitaEvasione = quantitaRiga;
			}
		} else {
			quantitaEvasione = 0.0;
		}
	}
}
