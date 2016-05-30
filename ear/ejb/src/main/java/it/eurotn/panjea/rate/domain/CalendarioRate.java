package it.eurotn.panjea.rate.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.partite.domain.CategoriaRata;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "part_calendari_rate")
@NamedQueries({
		@NamedQuery(name = "CalendarioRate.caricaByCliente", query = "select distinct c from CalendarioRate c left join fetch c.categorieRate cat inner join c.clienti cl where cl in (:paramCliente) order by c.descrizione"),
		@NamedQuery(name = "CalendarioRate.caricaAziendali", query = "select distinct c from CalendarioRate c inner join fetch c.categorieRate cat where c.calendarioAziendale=true order by c.descrizione"),
		@NamedQuery(name = "CalendarioRate.caricaByClienteECategoriaRata", query = "select distinct c from CalendarioRate c inner join fetch c.categorieRate cat inner join c.clienti cl where cat in (:parameCategoriaRata) and cl in (:paramCliente)"),
		@NamedQuery(name = "CalendarioRate.caricaByAziendaECategoriaRata", query = "select distinct c from CalendarioRate c inner join fetch c.categorieRate cat where c.calendarioAziendale=true and cat in (:parameCategoriaRata) order by c.descrizione") })
public class CalendarioRate extends EntityBase {

	private static final long serialVersionUID = -6518508104569611862L;

	private String descrizione;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private List<ClienteLite> clienti;

	@ManyToMany(fetch = FetchType.LAZY)
	private List<CategoriaRata> categorieRate;

	private boolean calendarioAziendale;

	/**
	 * Costruttore.
	 * 
	 */
	public CalendarioRate() {
		super();
		categorieRate = new ArrayList<CategoriaRata>();
		clienti = new ArrayList<ClienteLite>();
		calendarioAziendale = Boolean.FALSE;
	}

	/**
	 * @return the categorieRate
	 */
	public List<CategoriaRata> getCategorieRate() {
		return categorieRate;
	}

	/**
	 * @return categorie rate associate al calendario in formato string.
	 */
	public String getCategorieRateToString() {
		StringBuilder result = new StringBuilder();

		for (CategoriaRata categoriaRata : getCategorieRate()) {
			if (result.length() != 0) {
				result.append(", ");
			}
			result.append(categoriaRata.getDescrizione());
		}

		return result.toString();
	}

	/**
	 * @return the clienti
	 */
	public List<ClienteLite> getClienti() {
		return clienti;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the calendarioAziendale
	 */
	public boolean isCalendarioAziendale() {
		return calendarioAziendale;
	}

	/**
	 * @param calendarioAziendale
	 *            the calendarioAziendale to set
	 */
	public void setCalendarioAziendale(boolean calendarioAziendale) {
		this.calendarioAziendale = calendarioAziendale;
	}

	/**
	 * @param categorieRate
	 *            the categorieRate to set
	 */
	public void setCategorieRate(List<CategoriaRata> categorieRate) {
		this.categorieRate = categorieRate;
	}

	/**
	 * @param clienti
	 *            the clienti to set
	 */
	public void setClienti(List<ClienteLite> clienti) {
		this.clienti = clienti;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
