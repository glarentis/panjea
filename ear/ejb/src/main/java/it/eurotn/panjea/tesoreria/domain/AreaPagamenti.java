package it.eurotn.panjea.tesoreria.domain;

import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

/**
 * Classe responsabile dei pagamenti.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 */
@Entity
@Audited
@DiscriminatorValue("PA")
@NamedQueries({ @NamedQuery(name = "AreaPagamenti.carica", query = " select distinct ap from AreaPagamenti ap inner join fetch ap.pagamenti pags inner join fetch pags.rata where ap.id = :paramIdAreaPagamenti ") })
@AuditableProperties(properties = { "documento" })
public class AreaPagamenti extends AreaChiusure {

	private static final long serialVersionUID = 4821314226683882306L;

	@OneToMany(mappedBy = "areaChiusure", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	private Set<Pagamento> pagamenti;

	/**
	 * @return the pagamenti
	 */
	public Set<Pagamento> getPagamenti() {
		return pagamenti;
	}

	/**
	 * Identifica se il documento di pagamento rappresenta un documneto di presentazione di bonifico fornitore.
	 * 
	 * @return true se TipoOperazione.GESTIONE_DISTINTA && TipoPagamento.BONIFICO && TipoPartita.PASSIVA
	 */
	public boolean isPresentazioneBonificoFornitore() {
		if (getTipoAreaPartita() != null
				&& getTipoAreaPartita().getTipoOperazione() != null
				&& getTipoAreaPartita().getTipoOperazione().equals(
						it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione.GESTIONE_DISTINTA)
				&& getTipoAreaPartita().getTipoPagamentoChiusura() != null
				&& getTipoAreaPartita().getTipoPagamentoChiusura().equals(TipoPagamento.BONIFICO)
				&& getTipoAreaPartita().getTipoPartita() != null
				&& getTipoAreaPartita().getTipoPartita().equals(TipoPartita.PASSIVA)) {
			return true;
		}
		return false;
	}

	/**
	 * @param pagamenti
	 *            the pagamenti to set
	 */
	public void setPagamenti(Set<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

}
