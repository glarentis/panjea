/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.manager.interfaces.IFormula;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 * @version 1.0, 29/ago/07
 */
@Entity
@Audited
@Table(name = "cont_strutture_contabili")
@NamedQueries({
		@NamedQuery(name = "StrutturaContabile.caricaByTipoDoc", query = "from StrutturaContabile s where s.codiceAzienda = :paramCodiceAzienda and s.tipoDocumento.id = :paramTipoDocumentoId and s.entita is null order by ordine"),
		@NamedQuery(name = "StrutturaContabile.caricaByTipoDocEEntita", query = "from StrutturaContabile s where s.codiceAzienda = :paramCodiceAzienda and s.tipoDocumento.id = :paramTipoDocumentoId and s.entita.id = :paramEntitaId order by ordine") })
public class StrutturaContabile extends EntityBase implements java.io.Serializable, IFormula, Cloneable {

	private static final long serialVersionUID = 6792907526968268430L;

	/**
	 * @uml.property name="codiceAzienda"
	 */
	@Column(length = 10, nullable = false)
	@Index(name = "index_codiceAzienda")
	private String codiceAzienda;

	/**
	 * @uml.property name="tipoDocumento"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private TipoDocumento tipoDocumento;

	/**
	 * @uml.property name="entita"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private EntitaLite entita;

	/**
	 * @uml.property name="dare"
	 */
	@Column(length = 255)
	private String dare;

	/**
	 * @uml.property name="avere"
	 */
	@Column(length = 255)
	private String avere;

	/**
	 * @uml.property name="formula"
	 */
	@Column(length = 100)
	private String formula;

	/**
	 * @uml.property name="tipologiaConto"
	 * @uml.associationEnd
	 */
	private ETipologiaConto tipologiaConto;

	/**
	 * @uml.property name="ordine"
	 */
	private Integer ordine;

	@Override
	public Object clone() {
		StrutturaContabile strutturaContabile = (StrutturaContabile) PanjeaEJBUtil.cloneObject(this);
		strutturaContabile.setId(null);

		return strutturaContabile;
	}

	/**
	 * @return Returns the avere.
	 * @uml.property name="avere"
	 */
	public String getAvere() {
		if (avere == null) {
			return avere;
		} else {
			switch (tipologiaConto) {
			case CONTO:
				return avere.substring(0, SottoConto.LUNGHEZZACODICESOTTOCONTO);
			default:
				return avere;
			}

		}
	}

	/**
	 * @return Returns the codiceAzienda.
	 * @uml.property name="codiceAzienda"
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return Returns the dare.
	 * @uml.property name="dare"
	 */
	public String getDare() {
		if (dare == null) {
			return dare;
		} else {
			switch (tipologiaConto) {
			case CONTO:
				return dare.substring(0, SottoConto.LUNGHEZZACODICESOTTOCONTO);
			default:
				return dare;
			}

		}
	}

	/**
	 * @return descrizione avere
	 */
	public String getDescrizioneAvere() {
		return avere;
	}

	/**
	 * @return descrizione dare
	 */
	public String getDescrizioneDare() {
		return dare;
	}

	/**
	 * @return Returns the entita.
	 * @uml.property name="entita"
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return Returns the formula.
	 * @uml.property name="formula"
	 */
	public String getFormula() {
		return formula;
	}

	@Override
	public List<String> getFormule() {
		return Arrays.asList(new String[] { formula });
	}

	/**
	 * @return Returns the ordine.
	 * @uml.property name="ordine"
	 */
	public Integer getOrdine() {
		return ordine;
	}

	/**
	 * @return Returns the tipoDocumento.
	 * @uml.property name="tipoDocumento"
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return Returns the tipologiaConto.
	 * @uml.property name="tipologiaConto"
	 */
	public ETipologiaConto getTipologiaConto() {
		return tipologiaConto;
	}

	/**
	 * @param avere
	 *            The avere to set.
	 * @uml.property name="avere"
	 */
	public void setAvere(String avere) {
		this.avere = avere;
	}

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 * @uml.property name="codiceAzienda"
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param dare
	 *            The dare to set.
	 * @uml.property name="dare"
	 */
	public void setDare(String dare) {
		this.dare = dare;
	}

	/**
	 * @param entita
	 *            The entita to set.
	 * @uml.property name="entita"
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param formula
	 *            The formula to set.
	 * @uml.property name="formula"
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @param ordine
	 *            The ordine to set.
	 * @uml.property name="ordine"
	 */
	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}

	/**
	 * @param tipoDocumento
	 *            The tipoDocumento to set.
	 * @uml.property name="tipoDocumento"
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @param tipologiaConto
	 *            The tipologiaConto to set.
	 * @uml.property name="tipologiaConto"
	 */
	public void setTipologiaConto(ETipologiaConto tipologiaConto) {
		this.tipologiaConto = tipologiaConto;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("StrutturaContabile[");
		buffer.append(super.toString());
		buffer.append("avere = ").append(avere);
		buffer.append(" codiceAzienda = ").append(codiceAzienda);
		buffer.append(" dare = ").append(dare);
		buffer.append(" entita = ").append(entita);
		buffer.append(" formula = ").append(formula);
		buffer.append(" ordine = ").append(ordine);
		buffer.append(" tipoDocumento = ").append(tipoDocumento);
		buffer.append(" tipologiaConto = ").append(tipologiaConto);
		buffer.append("]");
		return buffer.toString();
	}
}
