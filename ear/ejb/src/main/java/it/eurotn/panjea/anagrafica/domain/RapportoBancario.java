/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;

/**
 * Domain Object di RapportoBancario.
 * 
 * @author adriano
 * @version 1.0, 18/dic/07
 */
@Entity
@Audited
@Table(name = "anag_rapporti_bancari")
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public class RapportoBancario extends EntityBase {

	private static final long serialVersionUID = 8782460485949619472L;

	public static final String REF = "RapportoBancario";

	public static final String PROP_BIC = "bic";
	public static final String PROP_DATA_CHIUSURA = "dataChiusura";
	public static final String PROP_CIN = "cin";
	public static final String PROP_NUMERO = "numero";
	public static final String PROP_BANCA = "banca";
	public static final String PROP_FILIALE = "filiale";
	public static final String PROP_NOTE = "note";
	public static final String PROP_DATA_APERTURA = "dataApertura";
	public static final String PROP_ABILITATO = "abilitato";
	public static final String PROP_ID = "id";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_DEFAULT_PAGAMENTI = "defaultPagamenti";
	private static Logger logger = Logger.getLogger(RapportoBancario.class.getName());

	/**
	 * @uml.property name="numero"
	 */
	@Column(length = 12, nullable = true)
	private String numero;
	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 40, nullable = false)
	private String descrizione;
	/**
	 * @uml.property name="note"
	 */
	@Column(length = 500)
	private String note;
	/**
	 * @uml.property name="cin"
	 */
	@Column(length = 1)
	private String cin;
	/**
	 * @uml.property name="bic"
	 */
	@Column(length = 11)
	private String bic;

	/**
	 * @uml.property name="abilitato"
	 */
	@Column(name = "abilitato")
	private Boolean abilitato;

	/**
	 * @uml.property name="dataApertura"
	 */
	@Column
	@Temporal(TemporalType.DATE)
	private Date dataApertura;
	/**
	 * @uml.property name="dataChiusura"
	 */
	@Column()
	@Temporal(TemporalType.DATE)
	private Date dataChiusura;
	/**
	 * @uml.property name="codicePaese"
	 */
	@Column(length = 2)
	private String codicePaese;
	/**
	 * @uml.property name="checkDigit"
	 */
	@Column(length = 2)
	private String checkDigit;
	/**
	 * @uml.property name="filiale"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private Filiale filiale;
	/**
	 * @uml.property name="banca"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private Banca banca;
	/**
	 * @uml.property name="giorniBanca"
	 */
	private Integer giorniBanca;

	/**
	 * @uml.property name="defaultPagamentiString"
	 */
	@Column(length = 150)
	private String defaultPagamentiString;

	@Transient
	private List<TipoPagamento> defaultPagamenti;

	{
		this.abilitato = true;
		this.defaultPagamenti = new ArrayList<TipoPagamento>();
	}

	/**
	 * Costruttore.
	 * 
	 */
	public RapportoBancario() {
		super();
	}

	/**
	 * @return Returns the abilitato.
	 * @uml.property name="abilitato"
	 */
	public Boolean getAbilitato() {
		return abilitato;
	}

	/**
	 * @return Returns the banca.
	 * @uml.property name="banca"
	 */
	public Banca getBanca() {
		return banca;
	}

	/**
	 * @return Returns the bic.
	 * @uml.property name="bic"
	 */
	public String getBic() {
		return bic;
	}

	/**
	 * @return the checkDigit
	 * @uml.property name="checkDigit"
	 */
	public String getCheckDigit() {
		return checkDigit;
	}

	/**
	 * @return Returns the cin.
	 * @uml.property name="cin"
	 */
	public String getCin() {
		return cin;
	}

	/**
	 * @return the codicePaese
	 * @uml.property name="codicePaese"
	 */
	public String getCodicePaese() {
		return codicePaese;
	}

	/**
	 * @return Returns the dataApertura.
	 * @uml.property name="dataApertura"
	 */
	public Date getDataApertura() {
		return dataApertura;
	}

	/**
	 * @return Returns the dataChiusura.
	 * @uml.property name="dataChiusura"
	 */
	public Date getDataChiusura() {
		return dataChiusura;
	}

	/**
	 * Ritorna le molteplici valorizzazioni trovate sulla string DefaultPagamentiString:<br>
	 * RIBA, BONIFICO, RID, BOLLETTINO_FRECCIA, RIMESSA_DIRETTA, F24.
	 * 
	 * @return valorizzaizoni
	 */
	public List<TipoPagamento> getDefaultPagamenti() {
		// l'istanza deve essere sempre la stessa altrimenti mi ritrovo ad avere
		// il form dirty ogni volta che chiamo la
		// get (e sul client la get viene chiamata spesso !!)
		defaultPagamenti.clear();
		if (getDefaultPagamentiString() == null) {
			return defaultPagamenti;
		}
		String[] tipi = getDefaultPagamentiString().split("#");
		for (String tipo : tipi) {
			TipoPagamento tipoE = Enum.valueOf(TipoPagamento.class, tipo);
			defaultPagamenti.add(tipoE);
		}
		return defaultPagamenti;
	}

	/**
	 * @return defaultPagamentiString
	 * @uml.property name="defaultPagamentiString"
	 */
	public String getDefaultPagamentiString() {
		logger.debug("--> >>>>>>>" + defaultPagamentiString);
		return defaultPagamentiString;
	}

	/**
	 * @return Returns the descrizione.
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return Returns the filiale.
	 * @uml.property name="filiale"
	 */
	public Filiale getFiliale() {
		return filiale;
	}

	/**
	 * @return the giorniBanca
	 * @uml.property name="giorniBanca"
	 */
	public Integer getGiorniBanca() {
		return giorniBanca;
	}

	/**
	 * EUIBAN (composto da : IT89 + stringa ITBBAN (senza spazi) esposto in 6 gruppi da 4 caratteri e 1 gruppo da 3 con
	 * spazio tra gruppo e gruppo. es: EUIBAN = IT89 I081 2001 8010 0000 2344 242
	 * 
	 * @return iban
	 */
	public java.lang.String getIban() {
		StringBuffer buffer = new StringBuffer();
		if (getCodicePaese() != null) {
			buffer.append(getCodicePaese());
		}
		if (getCheckDigit() != null) {
			buffer.append(getCheckDigit());
		}
		if (getCin() != null) {
			buffer.append(getCin());
		}
		if ((getBanca() != null) && (getBanca().getCodice() != null)) {
			buffer.append(getBanca().getCodice());
		}
		if ((getFiliale() != null) && (getFiliale().getCodice() != null)) {
			buffer.append(getFiliale().getCodice());
		}
		if ((getNumero() != null) && (getNumero().length() == 12)) {
			buffer.append(getNumero());
		}
		String euiban = buffer.toString();
		// sono nel caso di un nuovo RapportoBancarioAzienda
		if (euiban.length() == 0) {
			return euiban;
		}
		StringBuffer result = new StringBuffer();
		try {
			result.append(euiban.substring(0, 4)).append(" ").append(euiban.substring(4, 8)).append(" ")
					.append(euiban.substring(8, 12)).append(" ").append(euiban.substring(12, 16)).append(" ")
					.append(euiban.substring(16, 20)).append(" ").append(euiban.substring(20, 24)).append(" ")
					.append(euiban.substring(24, euiban.length()));
		} catch (Exception e) {
			// non faccio nulla nella catch per restituire una stringa vuota
			result = new StringBuffer();
		}
		return result.toString();
	}

	/**
	 * ITBBAN (composto da : CIN (1) + ABI (5) + CAB(5) + Numero rapporto (12) allineati a destra con zero davanti (solo
	 * visualizzazione con spazio tra un campo e l'altro). es: ITBBAN = I 08120 01801 000002344242
	 * 
	 * @return itiban
	 */
	public java.lang.String getItbban() {
		StringBuffer buffer = new StringBuffer();
		if (getCin() != null) {
			buffer.append(getCin() + " ");
		}
		if ((getBanca() != null) && (getBanca().getCodice() != null)) {
			buffer.append(getBanca().getCodice() + " ");
		}
		if ((getFiliale() != null) && (getFiliale().getCodice() != null)) {
			buffer.append(getFiliale().getCodice() + " ");
		}
		if (getNumero() != null) {
			buffer.append(getNumero());
		}
		return buffer.toString();
	}

	/**
	 * @return Returns the note.
	 * @uml.property name="note"
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return Returns the numero.
	 * @uml.property name="numero"
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param abilitato
	 *            The abilitato to set.
	 * @uml.property name="abilitato"
	 */
	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}

	/**
	 * @param banca
	 *            The banca to set.
	 * @uml.property name="banca"
	 */
	public void setBanca(Banca banca) {
		this.banca = banca;
	}

	/**
	 * @param bic
	 *            The bic to set.
	 * @uml.property name="bic"
	 */
	public void setBic(String bic) {
		this.bic = bic;
	}

	/**
	 * @param checkDigit
	 *            the checkDigit to set
	 * @uml.property name="checkDigit"
	 */
	public void setCheckDigit(String checkDigit) {
		this.checkDigit = checkDigit;
	}

	/**
	 * @param cin
	 *            The cin to set.
	 * @uml.property name="cin"
	 */
	public void setCin(String cin) {
		this.cin = cin;
	}

	/**
	 * @param codicePaese
	 *            the codicePaese to set
	 * @uml.property name="codicePaese"
	 */
	public void setCodicePaese(String codicePaese) {
		this.codicePaese = codicePaese;
	}

	/**
	 * @param dataApertura
	 *            The dataApertura to set.
	 * @uml.property name="dataApertura"
	 */
	public void setDataApertura(Date dataApertura) {
		this.dataApertura = dataApertura;
	}

	/**
	 * @param dataChiusura
	 *            The dataChiusura to set.
	 * @uml.property name="dataChiusura"
	 */
	public void setDataChiusura(Date dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	/**
	 * Per trasformare i tipi di pagamento raggruppati in arrayList in una stringa con delimitatori #.
	 * 
	 * @param tips
	 *            tipi pagamento
	 */
	public void setDefaultPagamenti(List<TipoPagamento> tips) {
		if (tips == null || tips.size() == 0) {
			defaultPagamentiString = null;
			return;
		}
		StringBuffer result = new StringBuffer();
		for (TipoPagamento tipoPagamento : tips) {
			result.append(tipoPagamento.name() + "#");
		}
		defaultPagamentiString = result.toString();
		this.defaultPagamenti = new ArrayList<TipoPagamento>();
		this.defaultPagamenti.addAll(tips);
	}

	/**
	 * @param defaultPagamentiString
	 *            the defaultPagamentiString to set
	 * @uml.property name="defaultPagamentiString"
	 */
	public void setDefaultPagamentiString(String defaultPagamentiString) {
		logger.debug("--> >>>>>>>>>>>>" + defaultPagamentiString);
		this.defaultPagamentiString = defaultPagamentiString;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param filiale
	 *            The filiale to set.
	 * @uml.property name="filiale"
	 */
	public void setFiliale(Filiale filiale) {
		this.filiale = filiale;
	}

	/**
	 * @param giorniBanca
	 *            the giorniBanca to set
	 * @uml.property name="giorniBanca"
	 */
	public void setGiorniBanca(Integer giorniBanca) {
		this.giorniBanca = giorniBanca;
	}

	/**
	 * @param note
	 *            The note to set.
	 * @uml.property name="note"
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param numero
	 *            The numero to set.
	 * @uml.property name="numero"
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("RapportoBancario[");
		buffer.append(super.toString());
		buffer.append(" abilitato = ").append(abilitato);
		buffer.append(" banca = ").append(banca);
		buffer.append(" bic = ").append(bic);
		buffer.append(" cin = ").append(cin);
		buffer.append(" dataApertura = ").append(dataApertura);
		buffer.append(" dataChiusura = ").append(dataChiusura);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" note = ").append(note);
		buffer.append(" numero = ").append(numero);
		buffer.append("]");
		return buffer.toString();
	}

}
