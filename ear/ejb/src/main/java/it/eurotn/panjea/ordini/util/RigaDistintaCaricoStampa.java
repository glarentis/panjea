package it.eurotn.panjea.ordini.util;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.ordini.domain.RigaArticolo;

import java.io.Serializable;

public class RigaDistintaCaricoStampa implements Serializable {

	private static final long serialVersionUID = -2605010143681103170L;

	private Integer idRigaDistintaCarico;

	private RigaArticolo rigaArticolo;

	private Articolo articolo;

	private Double qtaDaEvadere;

	private Double qtaOrdinata;

	private Integer numeroDecimaliQta;
	private EntitaLite entita;

	private String ordinamentoEntita;

	private SedeEntita sedeEntita;

	private VettoreLite vettore;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public RigaDistintaCaricoStampa() {
		articolo = new Articolo();
		entita = new ClienteLite();
		sedeEntita = new SedeEntita();
		vettore = new VettoreLite();
	}

	/**
	 * @return Returns the articolo.
	 */
	public Articolo getArticolo() {
		return articolo;
	}

	/**
	 * @return Returns the entita.
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return Returns the idRigaDistintaCarico.
	 */
	public Integer getIdRigaDistintaCarico() {
		return idRigaDistintaCarico;
	}

	/**
	 * @return Returns the numeroDecimaliQta.
	 */
	public Integer getNumeroDecimaliQta() {
		return numeroDecimaliQta;
	}

	/**
	 * @return Returns the ordinamentoEntita.
	 */
	public String getOrdinamentoEntita() {
		return ordinamentoEntita;
	}

	/**
	 * @return Returns the qtaDaEvadere.
	 */
	public Double getQtaDaEvadere() {
		return qtaDaEvadere;
	}

	/**
	 * @return Returns the qtaOrdinata.
	 */
	public Double getQtaOrdinata() {
		return qtaOrdinata;
	}

	/**
	 * @return Returns the rigaArticolo.
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}

	/**
	 * @return Returns the sedeEntita.
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return Returns the vettore.
	 */
	public VettoreLite getVettore() {
		return vettore;
	}

	/**
	 * @param articolo
	 *            The articolo to set.
	 */
	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param entita
	 *            The entita to set.
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param idRigaDistintaCarico
	 *            The idRigaDistintaCarico to set.
	 */
	public void setIdRigaDistintaCarico(Integer idRigaDistintaCarico) {
		this.idRigaDistintaCarico = idRigaDistintaCarico;
	}

	/**
	 * @param numeroDecimaliQta
	 *            The numeroDecimaliQta to set.
	 */
	public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
		this.numeroDecimaliQta = numeroDecimaliQta;
	}

	/**
	 * @param ordinamentoEntita
	 *            The ordinamentoEntita to set.
	 */
	public void setOrdinamentoEntita(String ordinamentoEntita) {
		this.ordinamentoEntita = ordinamentoEntita;
	}

	/**
	 * @param qtaDaEvadere
	 *            The qtaDaEvadere to set.
	 */
	public void setQtaDaEvadere(Double qtaDaEvadere) {
		this.qtaDaEvadere = qtaDaEvadere;
	}

	/**
	 * @param qtaOrdinata
	 *            The qtaOrdinata to set.
	 */
	public void setQtaOrdinata(Double qtaOrdinata) {
		this.qtaOrdinata = qtaOrdinata;
	}

	/**
	 * @param rigaArticolo
	 *            The rigaArticolo to set.
	 */
	public void setRigaArticolo(RigaArticolo rigaArticolo) {
		this.rigaArticolo = rigaArticolo;
	}

	/**
	 * @param sedeEntita
	 *            The sedeEntita to set.
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	/**
	 * @param vettore
	 *            The vettore to set.
	 */
	public void setVettore(VettoreLite vettore) {
		this.vettore = vettore;
	}

}
