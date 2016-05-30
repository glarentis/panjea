/**
 * 
 */
package it.eurotn.panjea.offerte.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Leonardo
 * 
 */
@Entity
@Table(name = "offe_tipi_area_offerte")
@NamedQueries({
		@NamedQuery(name = "TipoAreaOfferta.caricaAll", query = "select tao from TipoAreaOfferta tao inner join tao.tipoDocumento td where td.codiceAzienda = :paramCodiceAzienda and (td.abilitato = true or :paramTuttiTipi = 1) "),
		@NamedQuery(name = "TipoAreaOfferta.caricaByTipoDocumento", query = "select tao from TipoAreaOfferta tao inner join tao.tipoDocumento td where td.id = :paramId ") })
public class TipoAreaOfferta extends EntityBase implements java.io.Serializable {

	private static final long serialVersionUID = 3385916812395627373L;

	@OneToOne
	@Fetch(FetchMode.SELECT)
	private TipoDocumento tipoDocumento;

	@Lob
	private String templateOfferta;

	/**
	 * 
	 */
	public TipoAreaOfferta() {
		super();
	}

	/**
	 * @return the templateOfferta
	 */
	public String getTemplateOfferta() {
		return templateOfferta;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param templateOfferta
	 *            the templateOfferta to set
	 */
	public void setTemplateOfferta(String templateOfferta) {
		this.templateOfferta = templateOfferta;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

}
