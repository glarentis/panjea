/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * @author fattazzo
 *
 */
@Entity
@Audited
@Table(name = "bamm_settings")
@NamedQueries({ @NamedQuery(name = "BeniSettings.caricaAll", query = "from BeniSettings bs  where bs.codiceAzienda = :codiceAzienda") })
public class BeniSettings extends EntityBase {

	public enum RaggruppamentoACSimulazione {
		UNICA, UNA_PER_TIPO_RAGGRUPPAMENTO
	}

	public enum TipoRaggruppamentoACSimulazione {
		SPECIE, SOTTOSPECIE, BENE
	}

	private static final long serialVersionUID = -1378450137715617548L;

	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	private TipoRaggruppamentoACSimulazione tipoRaggruppamentoACSimulazione;

	private RaggruppamentoACSimulazione raggruppamentoACSimulazione;

	{
		tipoRaggruppamentoACSimulazione = TipoRaggruppamentoACSimulazione.SPECIE;
		raggruppamentoACSimulazione = RaggruppamentoACSimulazione.UNICA;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the raggruppamentoACSimulazione
	 */
	public RaggruppamentoACSimulazione getRaggruppamentoACSimulazione() {
		return raggruppamentoACSimulazione;
	}

	/**
	 * @return the tipoRaggruppamentoACSimulazione
	 */
	public TipoRaggruppamentoACSimulazione getTipoRaggruppamentoACSimulazione() {
		return tipoRaggruppamentoACSimulazione;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param raggruppamentoACSimulazione
	 *            the raggruppamentoACSimulazione to set
	 */
	public void setRaggruppamentoACSimulazione(RaggruppamentoACSimulazione raggruppamentoACSimulazione) {
		this.raggruppamentoACSimulazione = raggruppamentoACSimulazione;
	}

	/**
	 * @param tipoRaggruppamentoACSimulazione
	 *            the tipoRaggruppamentoACSimulazione to set
	 */
	public void setTipoRaggruppamentoACSimulazione(TipoRaggruppamentoACSimulazione tipoRaggruppamentoACSimulazione) {
		this.tipoRaggruppamentoACSimulazione = tipoRaggruppamentoACSimulazione;
	}

}
