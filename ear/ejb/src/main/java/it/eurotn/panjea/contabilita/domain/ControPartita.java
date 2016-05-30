/**
 *
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

/**
 * Classe che gestisce una contropartita. La gestione della descrizione dare o avere viene gestita internamente
 * dall'oggetto di dominio sui vari metodi di set di sottoconto, contobase e conto.
 * 
 * @author fattazzo
 * @version 1.0, 31/ago/07
 */
@NamedQueries({
		@NamedQuery(name = "ControPartita.caricaByTipoDoc", query = "from ControPartita c where c.tipoDocumento.id = :paramTipoDocumentoId and c.entita is null order by ordine"),
		@NamedQuery(name = "ControPartita.caricaByTipoDocEEntita", query = "from ControPartita c where c.tipoDocumento.id = :paramTipoDocumentoId and c.entita.id = :paramEntitaId order by ordine") })
@Entity
@Audited
@Table(name = "cont_contro_partite")
public class ControPartita extends EntityBase implements java.io.Serializable, Cloneable {

	/**
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	private enum ETipoDescrizione {
		DARE, AVERE
	}

	private static final long serialVersionUID = -1757385366176278971L;

	@ManyToOne
	private TipoDocumento tipoDocumento;

	@ManyToOne
	private EntitaLite entita;

	@ManyToOne
	private SottoConto dare;

	@ManyToOne
	private SottoConto avere;

	@ManyToOne
	private Conto contoDare;

	@ManyToOne
	private Conto contoAvere;

	@ManyToOne
	private ContoBase contoBaseDare;

	@ManyToOne
	private ContoBase contoBaseAvere;

	@Column(length = 100)
	private String formula;

	@Column(length = 40)
	private String descrizione;

	@ManyToOne
	private CodiceIva codiceIva;

	private Integer ordine;

	private ETipologiaContoControPartita tipologiaContoControPartita;

	private String descrizioneDare;
	private String descrizioneAvere;

	@Transient
	private SottoConto sottoContoNull;

	@Transient
	private Conto contoNull;

	@Transient
	private ContoBase contoBaseNull;

	@Transient
	private BigDecimal importo;

	@Transient
	private String note;

	{
		this.sottoContoNull = null;
		this.contoNull = null;
		this.contoBaseNull = null;
		this.importo = BigDecimal.ZERO;
		this.codiceIva = null;
		this.note = "";
	}

	/**
	 * Costruttore.
	 */
	public ControPartita() {

	}

	@Override
	public Object clone() {
		ControPartita controPartita = (ControPartita) PanjeaEJBUtil.cloneObject(this);
		controPartita.setId(null);
		return controPartita;
	}

	/**
	 * @return avere
	 */
	public SottoConto getAvere() {
		if (avere == null) {
			return sottoContoNull;
		} else {
			return avere;
		}
	}

	/**
	 * @return codiceIva
	 */
	public CodiceIva getCodiceIva() {
		return codiceIva;
	}

	/**
	 * @return contoAvere
	 */
	public Conto getContoAvere() {
		if (contoAvere == null) {
			return contoNull;
		} else {
			return contoAvere;
		}
	}

	/**
	 * @return contoBaseAvere
	 */
	public ContoBase getContoBaseAvere() {
		if (contoBaseAvere == null) {
			return contoBaseNull;
		} else {
			return contoBaseAvere;
		}
	}

	/**
	 * @return contoBaseDare
	 */
	public ContoBase getContoBaseDare() {
		if (contoBaseDare == null) {
			return contoBaseNull;
		} else {
			return contoBaseDare;
		}
	}

	/**
	 * @return contoDare
	 */
	public Conto getContoDare() {
		if (contoDare == null) {
			return contoNull;
		} else {
			return contoDare;
		}
	}

	/**
	 * @return dare
	 */
	public SottoConto getDare() {
		if (dare == null) {
			return sottoContoNull;
		} else {
			return dare;
		}
	}

	/**
	 * @return descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return descrizioneAvere
	 */
	public String getDescrizioneAvere() {
		return descrizioneAvere;
	}

	/**
	 * @return descrizioneDare
	 */
	public String getDescrizioneDare() {
		return descrizioneDare;
	}

	/**
	 * @return entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @return importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return ordine
	 */
	public Integer getOrdine() {
		return ordine;
	}

	/**
	 * @return tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return tipologiaContoControPartita
	 */
	public ETipologiaContoControPartita getTipologiaContoControPartita() {
		return tipologiaContoControPartita;
	}

	/**
	 * @param avere
	 *            the avere to set
	 */
	public void setAvere(SottoConto avere) {
		this.avere = avere;
		if (avere != null) {
			this.dare = null;
			setDescrizione(avere.getSottoContoCodice() + " " + avere.getDescrizione(), ETipoDescrizione.AVERE);
			this.contoAvere = null;
			this.contoDare = null;
			this.contoBaseAvere = null;
			this.contoBaseDare = null;
		}
	}

	/**
	 * @param codiceIva
	 *            the codiceIva to set
	 */
	public void setCodiceIva(CodiceIva codiceIva) {
		this.codiceIva = codiceIva;
	}

	/**
	 * @param contoAvere
	 *            the contoAvere to set
	 */
	public void setContoAvere(Conto contoAvere) {
		this.contoAvere = contoAvere;
		if (contoAvere != null) {
			this.contoDare = null;
			setDescrizione(contoAvere.getContoCodice() + " " + contoAvere.getDescrizione(), ETipoDescrizione.AVERE);
			this.avere = null;
			this.dare = null;
			this.contoBaseAvere = null;
			this.contoBaseDare = null;
		}
	}

	/**
	 * @param contoBaseAvere
	 *            the contoBaseAvere to set
	 */
	public void setContoBaseAvere(ContoBase contoBaseAvere) {
		this.contoBaseAvere = contoBaseAvere;
		if (contoBaseAvere != null) {
			this.contoBaseDare = null;
			setDescrizione(contoBaseAvere.getDescrizione(), ETipoDescrizione.AVERE);
			this.contoAvere = null;
			this.contoDare = null;
			this.contoAvere = null;
			this.contoDare = null;
		}
	}

	/**
	 * @param contoBaseDare
	 *            the contoBaseDare to set
	 */
	public void setContoBaseDare(ContoBase contoBaseDare) {
		this.contoBaseDare = contoBaseDare;
		if (contoBaseDare != null) {
			this.contoBaseAvere = null;
			setDescrizione(contoBaseDare.getDescrizione(), ETipoDescrizione.DARE);
			this.contoAvere = null;
			this.contoDare = null;
			this.contoAvere = null;
			this.contoDare = null;
		}
	}

	/**
	 * @param contoDare
	 *            the contoDare to set
	 */
	public void setContoDare(Conto contoDare) {
		this.contoDare = contoDare;
		if (contoDare != null) {
			this.contoAvere = null;
			setDescrizione(contoDare.getContoCodice() + " " + contoDare.getDescrizione(), ETipoDescrizione.DARE);
			this.avere = null;
			this.dare = null;
			this.contoBaseAvere = null;
			this.contoBaseDare = null;
		}
	}

	/**
	 * @param dare
	 *            the dare to set
	 */
	public void setDare(SottoConto dare) {
		this.dare = dare;
		if (dare != null) {
			this.avere = null;
			setDescrizione(dare.getSottoContoCodice() + " " + dare.getDescrizione(), ETipoDescrizione.DARE);
			this.contoAvere = null;
			this.contoDare = null;
			this.contoBaseAvere = null;
			this.contoBaseDare = null;
		}
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * Metodo di servizio usato per settare la descrizione dare o avere e cancellare l'altra.
	 * 
	 * @param descrizioneSet
	 *            descrizione
	 * @param tipoDescrizione
	 *            tipo descrizione
	 */
	private void setDescrizione(String descrizioneSet, ETipoDescrizione tipoDescrizione) {
		switch (tipoDescrizione) {
		case AVERE:
			this.descrizioneAvere = descrizioneSet;
			this.descrizioneDare = "";
			break;
		case DARE:
			this.descrizioneAvere = "";
			this.descrizioneDare = descrizioneSet;
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param formula
	 *            the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @param importo
	 *            the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param ordine
	 *            the ordine to set
	 */
	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @param tipologiaContoControPartita
	 *            the tipologiaContoControPartita to set
	 */
	public void setTipologiaContoControPartita(ETipologiaContoControPartita tipologiaContoControPartita) {
		this.tipologiaContoControPartita = tipologiaContoControPartita;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ControPartita[");
		buffer.append(super.toString());
		buffer.append("avere = ").append(avere);
		buffer.append(" dare = ").append(dare);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" formula = ").append(formula);
		buffer.append(" entita = ").append(entita);
		buffer.append(" tipo documento = ").append(tipoDocumento);
		buffer.append("]");
		return buffer.toString();
	}
}
