package it.eurotn.panjea.mrp.domain;

import java.io.Serializable;
import java.util.Date;

public class OrdiniClienteCalcolo implements Serializable {

	private static final long serialVersionUID = 7525995515445882231L;
	private int idDeposito;
	private int idRigaOrdineFornitore;

	private int idArticolo;
	private double qta;
	private Date dataConsegna;

	/**
	 * @return Returns the dataConsegna.
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return Returns the idArticolo.
	 */
	public int getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return Returns the idDeposito.
	 */
	public int getIdDeposito() {
		return idDeposito;
	}

	/**
	 * @return Returns the idRigaOrdineFornitore.
	 */
	public int getIdRigaOrdineFornitore() {
		return idRigaOrdineFornitore;
	}

	/**
	 * @return Returns the qta.
	 */
	public double getQta() {
		return qta;
	}

	/**
	 * @param dataConsegna
	 *            The dataConsegna to set.
	 */
	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * @param idArticolo
	 *            The idArticolo to set.
	 */
	public void setIdArticolo(int idArticolo) {
		this.idArticolo = idArticolo;
	}

	/**
	 * @param idDeposito
	 *            The idDeposito to set.
	 */
	public void setIdDeposito(int idDeposito) {
		this.idDeposito = idDeposito;
	}

	/**
	 * @param idRigaOrdineFornitore
	 *            The idRigaOrdineFornitore to set.
	 */
	public void setIdRigaOrdineFornitore(int idRigaOrdineFornitore) {
		this.idRigaOrdineFornitore = idRigaOrdineFornitore;
	}

	/**
	 * @param qta
	 *            The qta to set.
	 */
	public void setQta(double qta) {
		this.qta = qta;
	}
}
