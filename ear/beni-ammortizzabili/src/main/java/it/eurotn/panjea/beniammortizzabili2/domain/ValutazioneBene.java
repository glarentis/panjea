/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Leonardo
 * 
 */
@Entity
@Table(name = "bamm_valutazioni_bene")
@NamedQueries({
		@NamedQuery(name = "ValutazioneBene.caricaAll", query = "from ValutazioneBene"),
		@NamedQuery(name = "ValutazioneBene.caricaByBene", query = "from ValutazioneBene v where v.bene.id = :paramIdBene "),
		@NamedQuery(name = "ValutazioneBene.caricaImportiValutazioni", query = " select new it.eurotn.panjea.beniammortizzabili.util.ValutazioneBeneImporto(v.bene.id, sum(v.importoValutazioneBene), sum(v.importoValutazioneFondo) ) from ValutazioneBene v "
				+ " where v.bene.codiceAzienda = :paramCodiceAzienda and (v.bene.datiCivilistici.ammortamentoInCorso = true or v.bene.datiFiscali.ammortamentoInCorso = true) and "
				+ " v.bene.beneDiProprieta = true and v.bene.indAmmortamento = true and v.bene.benePadre is null "
				+ " group by v.bene.id  "),
		@NamedQuery(name = "ValutazioneBene.caricaImportiValutazioniPerBene", query = " select new it.eurotn.panjea.beniammortizzabili.util.ValutazioneBeneImporto(v.bene.id, sum(v.importoValutazioneBene), sum(v.importoValutazioneFondo) ) from ValutazioneBene v "
				+ " where v.bene.id  = :paramIdBeneAmmortizzabile and (v.bene.datiCivilistici.ammortamentoInCorso = true or v.bene.datiFiscali.ammortamentoInCorso = true) and "
				+ " v.bene.beneDiProprieta = true and v.bene.indAmmortamento = true and v.bene.benePadre is null "
				+ " group by v.bene.id  ")

})
public class ValutazioneBene extends EntityBase {

	private static final long serialVersionUID = -5673697963467827275L;

	public static final String REF = "ValutazioneBene";
	public static final String PROP_ANNO = "anno";
	public static final String PROP_IMPORTO_VALUTAZIONE_BENE = "importoValutazioneBene";
	public static final String PROP_IMPORTO_VALUTAZIONE_FONDO = "importoValutazioneFondo";
	public static final String PROP_NOTE = "note";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_DATE_UPDATE = "dateUpdate";

	private Integer anno;

	private BigDecimal importoValutazioneBene;
	private BigDecimal importoValutazioneFondo;

	private String note;

	@ManyToOne
	private BeneAmmortizzabile bene;

	/**
	 * Costruttore di default.
	 */
	public ValutazioneBene() {
		initialize();
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return the bene
	 */
	public BeneAmmortizzabile getBene() {
		return bene;
	}

	/**
	 * @return the importoValutazioneBene
	 */
	public BigDecimal getImportoValutazioneBene() {
		return importoValutazioneBene;
	}

	/**
	 * @return the importoValutazioneFondo
	 */
	public BigDecimal getImportoValutazioneFondo() {
		return importoValutazioneFondo;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		importoValutazioneBene = BigDecimal.ZERO;
		importoValutazioneFondo = BigDecimal.ZERO;

		bene = new BeneAmmortizzabile();
	}

	/**
	 * @param anno
	 *            the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param bene
	 *            the bene to set
	 */
	public void setBene(BeneAmmortizzabile bene) {
		this.bene = bene;
	}

	/**
	 * @param importoValutazioneBene
	 *            the importoValutazioneBene to set
	 */
	public void setImportoValutazioneBene(BigDecimal importoValutazioneBene) {
		this.importoValutazioneBene = importoValutazioneBene;
	}

	/**
	 * @param importoValutazioneFondo
	 *            the importoValutazioneFondo to set
	 */
	public void setImportoValutazioneFondo(BigDecimal importoValutazioneFondo) {
		this.importoValutazioneFondo = importoValutazioneFondo;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ValutazioneBene[");
		buffer.append("anno = ").append(anno);
		buffer.append(" bene = ").append(bene != null ? bene.getId() : null);
		buffer.append(" importoValutazioneBene = ").append(importoValutazioneBene);
		buffer.append(" importoValutazioneFondo = ").append(importoValutazioneFondo);
		buffer.append(" note = ").append(note);
		buffer.append("]");
		return buffer.toString();
	}

}
