package it.eurotn.panjea.auvend.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Contiene i dati statistici per l'importazione legati ad un deposito/caricatore. <br>
 * Contiene i movimenti da inserire/modificare e gli articoli mancanti in panjea. <br/>
 * La severity indica:<br/>
 * INFO:l'importazione ha i dati senza anomalie<br/>
 * WARNING:ho dei movimenti da sovrascrivere. Non dovrebbe accadere ma non è considerato un errore <br/>
 * ERROR:non posso eseguire l'importazione. Solitamente perchè non ho gli articoli in panjea
 * 
 * @author giangi
 * 
 */
public class StatisticaImportazione implements Serializable {
	public enum ESEVERITY {
		Info, Warning, Error
	}

	private static final long serialVersionUID = -5714787250178285372L;

	private String deposito;
	private int numeroMovimentiDaInserire;
	private List<Documento> documentiDaAggiornare;
	private List<Documento> documentiDaInserire;
	private List<Articolo> articoliMancanti;
	private List<Cliente> clientiDaVerificare;

	/**
	 * @return articoliMancanti
	 */
	public List<Articolo> getArticoliMancanti() {
		return articoliMancanti;
	}

	/**
	 * @return clientiDaVerificare
	 */
	public List<Cliente> getClientiDaVerificare() {
		return clientiDaVerificare;
	}

	/**
	 * @return deposito
	 */
	public String getDeposito() {
		return deposito;
	}

	/**
	 * @return documentiDaAggiornare
	 */
	public List<Documento> getDocumentiDaAggiornare() {
		return documentiDaAggiornare;
	}

	/**
	 * @return documentiDaInserire
	 */
	public List<Documento> getDocumentiDaInserire() {
		return documentiDaInserire;
	}

	/**
	 * @return numeroMovimentiDaInserire
	 */
	public int getNumeroMovimentiDaInserire() {
		if (documentiDaInserire != null) {
			return documentiDaInserire.size();
		}
		return numeroMovimentiDaInserire;
	}

	/**
	 * @return severity
	 */
	public ESEVERITY getSeverity() {
		if (articoliMancanti != null && articoliMancanti.size() > 0) {
			return ESEVERITY.Error;
		}
		if (getDocumentiDaAggiornare() != null && getDocumentiDaAggiornare().size() > 0) {
			return ESEVERITY.Warning;
		}
		return ESEVERITY.Info;
	}

	/**
	 * @param articoliMancanti
	 *            the articoliMancanti to set
	 */
	public void setArticoliMancanti(List<Articolo> articoliMancanti) {
		this.articoliMancanti = articoliMancanti;
	}

	/**
	 * @param clientiDaVerificare
	 *            the clientiDaVerificare to set
	 */
	public void setClientiDaVerificare(List<Cliente> clientiDaVerificare) {
		this.clientiDaVerificare = clientiDaVerificare;
	}

	/**
	 * @param deposito
	 *            the deposito to set
	 */
	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}

	/**
	 * @param documentiDaAggiornare
	 *            the documentiDaAggiornare to set
	 */
	public void setDocumentiDaAggiornare(List<Documento> documentiDaAggiornare) {
		this.documentiDaAggiornare = documentiDaAggiornare;
	}

	/**
	 * @param documentiDaInserire
	 *            the documentiDaInserire to set
	 */
	public void setDocumentiDaInserire(List<Documento> documentiDaInserire) {
		this.documentiDaInserire = documentiDaInserire;
	}

	/**
	 * @param numeroMovimentiDaInserire
	 *            the numeroMovimentiDaInserire to set
	 */
	public void setNumeroMovimentiDaInserire(int numeroMovimentiDaInserire) {
		this.numeroMovimentiDaInserire = numeroMovimentiDaInserire;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString() {
		final String tab = "    ";

		String retValue = "";

		retValue = "StatisticaImportazione ( " + super.toString() + tab + "deposito = " + this.deposito + tab
				+ "numeroMovimentiDaInserire = " + this.numeroMovimentiDaInserire + tab + "documentiDaAggiornare = "
				+ this.documentiDaAggiornare + tab + "documentiDaInserire = " + this.documentiDaInserire + tab
				+ "articoliMancanti = " + this.articoliMancanti + tab + "clientiDaVerificare = "
				+ this.clientiDaVerificare + tab + " )";

		return retValue;
	}

}
