/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.service.exception.RapportoBancarioRegolamentazioneAssenteException;
import it.eurotn.panjea.contabilita.domain.SottoConto;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * @author Leonardo
 */
@Entity
@Audited
@Table(name = "anag_rapporti_bancari")
@DiscriminatorValue("A")
@NamedQueries({ @NamedQuery(name = "RapportoBancarioAzienda.caricaSottoContoByRapporto", query = "select sottoConto from RapportoBancarioAzienda rb where rb.id = :paramIdRapportoBancario ") })
public class RapportoBancarioAzienda extends RapportoBancario {

	private static final long serialVersionUID = 8782460485949619472L;
	public static final String REF = "RapportoBancarioAzienda";
	public static final String PROP_AZIENDA = "Azienda";

	@Column(name = "speseInsoluto", precision = 19, scale = 6)
	private BigDecimal speseInsoluto;

	@Column(name = "spesePresentazione", precision = 19, scale = 6)
	private BigDecimal spesePresentazione;

	@Column(name = "speseDistinta", precision = 19, scale = 6)
	private BigDecimal speseDistinta;

	@Column(name = "percAnticippoFatture", precision = 5, scale = 2)
	private BigDecimal percAnticippoFatture;

	@Column(name = "percIvaAnticipoFatture", precision = 5, scale = 2)
	private BigDecimal percIvaAnticipoFatture;

	@ManyToOne(fetch = FetchType.LAZY)
	private AziendaLite azienda;

	@ManyToOne(fetch = FetchType.LAZY)
	private RapportoBancarioAzienda rapportoBancarioRegolamentazione;

	@ManyToOne(fetch = FetchType.LAZY)
	private SottoConto sottoConto;

	@ManyToOne(fetch = FetchType.LAZY)
	private SottoConto sottoContoEffettiAttivi;

	@ManyToOne(fetch = FetchType.LAZY)
	private SottoConto sottoContoAnticipoFatture;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE }, mappedBy = "rapportoBancarioAzienda")
	private Set<SedeEntita> sediEntita;

	{
		sediEntita = new HashSet<SedeEntita>();
	}

	/**
	 * Costruttore.
	 */
	public RapportoBancarioAzienda() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param rapportoBancarioRegolamentazione
	 *            {@link RapportoBancarioRegolamentazioneAssenteException}
	 */
	public RapportoBancarioAzienda(final RapportoBancarioAzienda rapportoBancarioRegolamentazione) {
		this.rapportoBancarioRegolamentazione = rapportoBancarioRegolamentazione;
	}

	/**
	 * @return the azienda
	 */
	public AziendaLite getAzienda() {
		return azienda;
	}

	/**
	 * @return the percAnticippoFatture
	 */
	public BigDecimal getPercAnticippoFatture() {
		return percAnticippoFatture;
	}

	/**
	 * @return the percIvaAnticipoFatture
	 */
	public BigDecimal getPercIvaAnticipoFatture() {
		return percIvaAnticipoFatture;
	}

	/**
	 * @return Returns the rapportoBancarioRegolamentazione.
	 */
	public RapportoBancarioAzienda getRapportoBancarioRegolamentazione() {
		return rapportoBancarioRegolamentazione;
	}

	/**
	 * @return the sediEntita
	 */
	public Set<SedeEntita> getSediEntita() {
		return sediEntita;
	}

	/**
	 * @return the sottoConto
	 */
	public SottoConto getSottoConto() {
		return sottoConto;
	}

	/**
	 * @return the sottoContoAnticipoFatture
	 */
	public SottoConto getSottoContoAnticipoFatture() {
		return sottoContoAnticipoFatture;
	}

	/**
	 * @return sottoContoEffettiAttivi
	 */
	public SottoConto getSottoContoEffettiAttivi() {
		return sottoContoEffettiAttivi;
	}

	/**
	 * @return the speseDistinta
	 */
	public BigDecimal getSpeseDistinta() {
		return speseDistinta;
	}

	/**
	 * @return Returns the speseInsoluto.
	 */
	public BigDecimal getSpeseInsoluto() {
		return speseInsoluto;
	}

	/**
	 * @return Returns the spesePresentazione.
	 */
	public BigDecimal getSpesePresentazione() {
		return spesePresentazione;
	}

	/**
	 * removeNullValueRapportoBancarioAzienda.
	 */
	public void removeNullValueRapportoBancarioAzienda() {
		if (rapportoBancarioRegolamentazione == null) {
			rapportoBancarioRegolamentazione = new RapportoBancarioAzienda(null);
		}
		if (sediEntita == null) {
			sediEntita = new HashSet<SedeEntita>();
		}
	}

	/**
	 * @param azienda
	 *            the azienda to set
	 */
	public void setAzienda(AziendaLite azienda) {
		this.azienda = azienda;
	}

	/**
	 * @param percAnticippoFatture
	 *            the percAnticippoFatture to set
	 */
	public void setPercAnticippoFatture(BigDecimal percAnticippoFatture) {
		this.percAnticippoFatture = percAnticippoFatture;
	}

	/**
	 * @param percIvaAnticipoFatture
	 *            the percIvaAnticipoFatture to set
	 */
	public void setPercIvaAnticipoFatture(BigDecimal percIvaAnticipoFatture) {
		this.percIvaAnticipoFatture = percIvaAnticipoFatture;
	}

	/**
	 * @param rapportoBancarioRegolamentazione
	 *            The rapportoBancarioRegolamentazione to set.
	 */
	public void setRapportoBancarioRegolamentazione(RapportoBancarioAzienda rapportoBancarioRegolamentazione) {
		this.rapportoBancarioRegolamentazione = rapportoBancarioRegolamentazione;
	}

	/**
	 * @param sediEntita
	 *            the sediEntita to set
	 */
	public void setSediEntita(Set<SedeEntita> sediEntita) {
		this.sediEntita = sediEntita;
	}

	/**
	 * @param sottoConto
	 *            the sottoConto to set
	 */
	public void setSottoConto(SottoConto sottoConto) {
		this.sottoConto = sottoConto;
	}

	/**
	 * @param sottoContoAnticipoFatture
	 *            the sottoContoAnticipoFatture to set
	 */
	public void setSottoContoAnticipoFatture(SottoConto sottoContoAnticipoFatture) {
		this.sottoContoAnticipoFatture = sottoContoAnticipoFatture;
	}

	/**
	 * @param sottoContoEffettiAttivi
	 *            the sottoContoEffettiAttivi to set
	 */
	public void setSottoContoEffettiAttivi(SottoConto sottoContoEffettiAttivi) {
		this.sottoContoEffettiAttivi = sottoContoEffettiAttivi;
	}

	/**
	 * @param speseDistinta
	 *            the speseDistinta to set
	 */
	public void setSpeseDistinta(BigDecimal speseDistinta) {
		this.speseDistinta = speseDistinta;
	}

	/**
	 * @param speseInsoluto
	 *            The speseInsoluto to set.
	 */
	public void setSpeseInsoluto(BigDecimal speseInsoluto) {
		this.speseInsoluto = speseInsoluto;
	}

	/**
	 * @param spesePresentazione
	 *            The spesePresentazione to set.
	 */
	public void setSpesePresentazione(BigDecimal spesePresentazione) {
		this.spesePresentazione = spesePresentazione;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("RapportoBancarioAzienda[");
		buffer.append(super.toString());
		buffer.append("]");
		return buffer.toString();
	}
}
