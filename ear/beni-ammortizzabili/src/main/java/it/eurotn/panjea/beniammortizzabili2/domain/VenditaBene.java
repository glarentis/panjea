/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Leonardo
 * 
 */
@Entity
@Table(name = "bamm_vendite_bene")
@NamedQueries({ @NamedQuery(name = "VenditaBene.caricaAll", query = " from VenditaBene "),
		@NamedQuery(name = "VenditaBene.caricaByBene", query = " from VenditaBene v where v.bene.id = :paramIdBene ")

})
public class VenditaBene extends EntityBase {

	private static final long serialVersionUID = 5367958308672635849L;

	public static final String REF = "VenditaBene";
	public static final String PROP_DATA_VENDITA = "dataVendita";
	public static final String PROP_DATA_FATTURA_VENDITA = "dataFatturaVendita";
	public static final String PROP_VALORE_FONDO = "valoreFondo";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_NUMERO_PROTOCOLLO_VENDITA = "numeroProtocolloVendita";
	public static final String PROP_VENDITA_TOTALE = "venditaTotale";
	public static final String PROP_VALORE_VENDITA = "valoreVendita";
	public static final String PROP_BENE = "bene";
	public static final String PROP_IMPORTO_FATTURA_VENDITA = "importoFatturaVendita";
	public static final String PROP_TIPOLOGIA_ELIMINAZIONE = "tipologiaEliminazione";
	public static final String PROP_IMPORTO_STORNO_FONDO_AMMORTAMENTO = "importoStornoFondoAmmortamento";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_VALORE_BENE = "valoreBene";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_NOTE = "note";
	public static final String PROP_IMPORTO_STORNO_VALORE_BENE = "importoStornoValoreBene";
	public static final String PROP_PLUS_MINUS_VALORE = "plusMinusValore";
	public static final String PROP_DATE_UPDATE = "dateUpdate";
	public static final String PROP_ID = "id";
	public static final String PROP_CLIENTE = "cliente";
	public static final String PROP_IMPORTO_VENDITA = "importoVendita";

	private String numeroProtocolloVendita;
	private String note;

	private BigDecimal importoFatturaVendita;
	private BigDecimal valoreBene;
	private BigDecimal valoreFondo;
	private BigDecimal importoStornoValoreBene;
	private BigDecimal valoreVendita;
	private BigDecimal importoVendita;
	private BigDecimal plusMinusValore;
	private BigDecimal importoStornoFondoAmmortamento;

	@Temporal(TemporalType.DATE)
	private Date dataVendita;

	@Temporal(TemporalType.DATE)
	private Date dataFatturaVendita;

	private boolean venditaTotale = false;

	@ManyToOne
	private BeneAmmortizzabile bene;
	@ManyToOne
	private EntitaLite cliente;
	@ManyToOne
	private TipologiaEliminazione tipologiaEliminazione;

	{
		importoFatturaVendita = BigDecimal.ZERO;
		valoreBene = BigDecimal.ZERO;
		valoreFondo = BigDecimal.ZERO;
		importoStornoValoreBene = BigDecimal.ZERO;
		valoreVendita = BigDecimal.ZERO;
		importoVendita = BigDecimal.ZERO;
		plusMinusValore = BigDecimal.ZERO;
		importoStornoFondoAmmortamento = BigDecimal.ZERO;
	}

	/**
	 * Costruttore di default.
	 */
	public VenditaBene() {
	}

	/**
	 * @return the bene
	 */
	public BeneAmmortizzabile getBene() {
		return bene;
	}

	/**
	 * @return the cliente
	 */
	public EntitaLite getCliente() {
		return cliente;
	}

	/**
	 * @return the dataFatturaVendita
	 */
	public Date getDataFatturaVendita() {
		return dataFatturaVendita;
	}

	/**
	 * @return the dataVendita
	 */
	public Date getDataVendita() {
		return dataVendita;
	}

	/**
	 * @return the importoFatturaVendita
	 */
	public BigDecimal getImportoFatturaVendita() {
		return importoFatturaVendita;
	}

	/**
	 * @return the importoStornoFondoAmmortamento
	 */
	public BigDecimal getImportoStornoFondoAmmortamento() {
		return importoStornoFondoAmmortamento;
	}

	/**
	 * @return the importoStornoValoreBene
	 */
	public BigDecimal getImportoStornoValoreBene() {
		return importoStornoValoreBene;
	}

	/**
	 * @return the importoVendita
	 */
	public BigDecimal getImportoVendita() {
		return importoVendita;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the numeroProtocolloVendita
	 */
	public String getNumeroProtocolloVendita() {
		return numeroProtocolloVendita;
	}

	/**
	 * @return the plusMinusValore
	 */
	public BigDecimal getPlusMinusValore() {
		return plusMinusValore;
	}

	/**
	 * @return the tipologiaEliminazione
	 */
	public TipologiaEliminazione getTipologiaEliminazione() {
		return tipologiaEliminazione;
	}

	/**
	 * @return the valoreBene
	 */
	public BigDecimal getValoreBene() {
		return valoreBene;
	}

	/**
	 * @return the valoreFondo
	 */
	public BigDecimal getValoreFondo() {
		return valoreFondo;
	}

	/**
	 * @return the valoreVendita
	 */
	public BigDecimal getValoreVendita() {
		return valoreVendita;
	}

	/**
	 * @return the venditaTotale
	 */
	public boolean isVenditaTotale() {
		return venditaTotale;
	}

	/**
	 * @param bene
	 *            the bene to set
	 */
	public void setBene(BeneAmmortizzabile bene) {
		this.bene = bene;
	}

	/**
	 * @param cliente
	 *            the cliente to set
	 */
	public void setCliente(EntitaLite cliente) {
		this.cliente = cliente;
	}

	/**
	 * @param dataFatturaVendita
	 *            the dataFatturaVendita to set
	 */
	public void setDataFatturaVendita(Date dataFatturaVendita) {
		this.dataFatturaVendita = dataFatturaVendita;
	}

	/**
	 * @param dataVendita
	 *            the dataVendita to set
	 */
	public void setDataVendita(Date dataVendita) {
		this.dataVendita = dataVendita;
	}

	/**
	 * @param importoFatturaVendita
	 *            the importoFatturaVendita to set
	 */
	public void setImportoFatturaVendita(BigDecimal importoFatturaVendita) {
		this.importoFatturaVendita = importoFatturaVendita;
	}

	/**
	 * @param importoStornoFondoAmmortamento
	 *            the importoStornoFondoAmmortamento to set
	 */
	public void setImportoStornoFondoAmmortamento(BigDecimal importoStornoFondoAmmortamento) {
		this.importoStornoFondoAmmortamento = importoStornoFondoAmmortamento;
	}

	/**
	 * @param importoStornoValoreBene
	 *            the importoStornoValoreBene to set
	 */
	public void setImportoStornoValoreBene(BigDecimal importoStornoValoreBene) {
		this.importoStornoValoreBene = importoStornoValoreBene;
	}

	/**
	 * @param importoVendita
	 *            the importoVendita to set
	 */
	public void setImportoVendita(BigDecimal importoVendita) {
		this.importoVendita = importoVendita;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param numeroProtocolloVendita
	 *            the numeroProtocolloVendita to set
	 */
	public void setNumeroProtocolloVendita(String numeroProtocolloVendita) {
		this.numeroProtocolloVendita = numeroProtocolloVendita;
	}

	/**
	 * @param plusMinusValore
	 *            the plusMinusValore to set
	 */
	public void setPlusMinusValore(BigDecimal plusMinusValore) {
		this.plusMinusValore = plusMinusValore;
	}

	/**
	 * @param tipologiaEliminazione
	 *            the tipologiaEliminazione to set
	 */
	public void setTipologiaEliminazione(TipologiaEliminazione tipologiaEliminazione) {
		this.tipologiaEliminazione = tipologiaEliminazione;
	}

	/**
	 * @param valoreBene
	 *            the valoreBene to set
	 */
	public void setValoreBene(BigDecimal valoreBene) {
		this.valoreBene = valoreBene;
	}

	/**
	 * @param valoreFondo
	 *            the valoreFondo to set
	 */
	public void setValoreFondo(BigDecimal valoreFondo) {
		this.valoreFondo = valoreFondo;
	}

	/**
	 * @param valoreVendita
	 *            the valoreVendita to set
	 */
	public void setValoreVendita(BigDecimal valoreVendita) {
		this.valoreVendita = valoreVendita;
	}

	/**
	 * @param venditaTotale
	 *            the venditaTotale to set
	 */
	public void setVenditaTotale(boolean venditaTotale) {
		this.venditaTotale = venditaTotale;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("VenditaBene[");
		buffer.append("bene = ").append(bene);
		buffer.append(" cliente = ").append(cliente != null ? cliente.getId() : null);
		buffer.append(" dataFatturaVendita = ").append(dataFatturaVendita);
		buffer.append(" dataVendita = ").append(dataVendita);
		buffer.append(" importoFatturaVendita = ").append(importoFatturaVendita);
		buffer.append(" importoStornoFondoAmmortamento = ").append(importoStornoFondoAmmortamento);
		buffer.append(" importoStornoValoreBene = ").append(importoStornoValoreBene);
		buffer.append(" importoVendita = ").append(importoVendita);
		buffer.append(" note = ").append(note);
		buffer.append(" numeroProtocolloVendita = ").append(numeroProtocolloVendita);
		buffer.append(" plusMinusValore = ").append(plusMinusValore);
		buffer.append(" tipologiaEliminazione = ").append(
				tipologiaEliminazione != null ? tipologiaEliminazione.getId() : null);
		buffer.append(" valoreBene = ").append(valoreBene);
		buffer.append(" valoreFondo = ").append(valoreFondo);
		buffer.append(" valoreVendita = ").append(valoreVendita);
		buffer.append(" venditaTotale = ").append(venditaTotale);
		buffer.append("]");
		return buffer.toString();
	}

}
