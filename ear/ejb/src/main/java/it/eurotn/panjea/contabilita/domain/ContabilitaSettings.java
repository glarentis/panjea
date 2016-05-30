/**
 *
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

@NamedQueries({ @NamedQuery(name = "ContabilitaSettings.caricaAll", query = "from ContabilitaSettings cs left join fetch cs.codiceIvaPerVentilazione left join fetch cs.codiceIvaPerVentilazione.codiceIvaSostituzioneVentilazione  where cs.codiceAzienda = :codiceAzienda", hints = {
		@QueryHint(name = "org.hibernate.cacheable", value = "true"),
		@QueryHint(name = "org.hibernate.cacheRegion", value = "contabilitaSettings") }) })
@Entity
@Audited
@Table(name = "cont_settings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "contabilitaSettings")
public class ContabilitaSettings extends EntityBase implements java.io.Serializable {

	/**
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum ETipoPeriodicita {
		MENSILE, TRIMESTRALE, ANNUALE
	}

	private static final long serialVersionUID = 1L;

	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	private boolean usaContoEffettiAttivi;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "contabilitaSettings", cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@OrderBy("anno")
	private List<ProRataSetting> proRataSettings;

	@Enumerated
	private ETipoPeriodicita tipoPeriodicita;

	private BigDecimal percTrimestrale;

	private BigDecimal minimaleIVA;

	private boolean calcoloCorrispettivi;

	@ManyToOne
	private CodiceIva codiceIvaPerVentilazione;

	/**
	 * Anno di competenza della contabilita'.
	 */
	private Integer annoCompetenza;

	/**
	 * Se l'anno di competenza richiesto per il calcolo del saldo <br/>
	 * è uguale ad annoInizioCalcoloSaldo non devo cercare lil movimento di apertura e mettere <br/>
	 * il saldo a zero.<b>Vedi bug. 1105</b>
	 */
	private Integer annoInizioCalcoloSaldo;

	// Utilizzata per verificare la possibilità di inserimento/modifica del documento contabile/iva
	private Date dataBloccoContabilita;
	private Date dataBloccoIva;

	/**
	 * Data utilizzata per il controllo dei documenti che sono sprovvisti di centri di costo configurati
	 */
	private Date dataControlloCentriDiCosto;

	{
		this.calcoloCorrispettivi = false;
		this.tipoPeriodicita = ETipoPeriodicita.MENSILE;
		this.percTrimestrale = BigDecimal.ZERO;
		this.proRataSettings = new ArrayList<ProRataSetting>();
	}

	/**
	 * Costruttore di default dove viene preparato un nuovo oggetto contabilitaSettings.
	 */
	public ContabilitaSettings() {
		super();
	}

	/**
	 * @return the annoCompetenza
	 */
	public Integer getAnnoCompetenza() {
		return annoCompetenza;
	}

	/**
	 * @return the annoInizioCalcoloSaldo
	 */
	public Integer getAnnoInizioCalcoloSaldo() {
		return annoInizioCalcoloSaldo;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the codiceIvaPerVentilazione
	 */
	public CodiceIva getCodiceIvaPerVentilazione() {
		return codiceIvaPerVentilazione;
	}

	/**
	 * @return the dataBloccoContabilita
	 */
	public Date getDataBloccoContabilita() {
		return dataBloccoContabilita;
	}

	/**
	 * @return the dataBloccoIva
	 */
	public Date getDataBloccoIva() {
		return dataBloccoIva;
	}

	/**
	 * @return the dataControlloCentriDiCosto
	 */
	public Date getDataControlloCentriDiCosto() {
		return dataControlloCentriDiCosto;
	}

	/**
	 * @return the minimaleIVA
	 */
	public BigDecimal getMinimaleIVA() {
		return minimaleIVA;
	}

	/**
	 * @return the percTrimestrale
	 */
	public BigDecimal getPercTrimestrale() {
		return percTrimestrale;
	}

	/**
	 * @param anno
	 *            anno interessato
	 * @param registroIva
	 *            registro interessato
	 * @return setting del prorata per l'anno e il registro richiesto
	 */
	public ProRataSetting getProRataSetting(int anno, RegistroIva registroIva) {
		ProRataSetting proRataSettingResult = null;

		Map<RegistroIva, ProRataSetting> mapRegistri = new HashMap<RegistroIva, ProRataSetting>();
		for (ProRataSetting proRataSetting : proRataSettings) {
			if (proRataSetting.getAnno().equals(anno)) {
				mapRegistri.put(proRataSetting.getRegistroIva(), proRataSetting);
			}
		}

		proRataSettingResult = mapRegistri.get(registroIva);

		// se non ho trovato il prorata per il registro iva prendo quello valido per tutti i registri
		proRataSettingResult = proRataSettingResult == null ? mapRegistri.get(null) : proRataSettingResult;

		return proRataSettingResult;
	}

	/**
	 * @return Returns the proRataSettings.
	 */
	public List<ProRataSetting> getProRataSettings() {
		return proRataSettings;
	}

	/**
	 * @return the tipoPeriodicita
	 */
	public ETipoPeriodicita getTipoPeriodicita() {
		return tipoPeriodicita;
	}

	/**
	 * @return the calcoloCorrispettivi
	 */
	public boolean isCalcoloCorrispettivi() {
		return calcoloCorrispettivi;
	}

	/**
	 * @return Returns the usaContoEffettiAttivi.
	 */
	public boolean isUsaContoEffettiAttivi() {
		return usaContoEffettiAttivi;
	}

	/**
	 * @param annoCompetenza
	 *            the annoCompetenza to set
	 */
	public void setAnnoCompetenza(Integer annoCompetenza) {
		this.annoCompetenza = annoCompetenza;
	}

	/**
	 * @param annoInizioCalcoloSaldo
	 *            the annoInizioCalcoloSaldo to set
	 */
	public void setAnnoInizioCalcoloSaldo(Integer annoInizioCalcoloSaldo) {
		this.annoInizioCalcoloSaldo = annoInizioCalcoloSaldo;
	}

	/**
	 * @param calcoloCorrispettivi
	 *            the calcoloCorrispettivi to set
	 */
	public void setCalcoloCorrispettivi(boolean calcoloCorrispettivi) {
		this.calcoloCorrispettivi = calcoloCorrispettivi;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param codiceIvaPerVentilazione
	 *            the codiceIvaPerVentilazione to set
	 */
	public void setCodiceIvaPerVentilazione(CodiceIva codiceIvaPerVentilazione) {
		this.codiceIvaPerVentilazione = codiceIvaPerVentilazione;
	}

	/**
	 * @param dataBloccoContabilita
	 *            the dataBloccoContabilita to set
	 */
	public void setDataBloccoContabilita(Date dataBloccoContabilita) {
		this.dataBloccoContabilita = dataBloccoContabilita;
	}

	/**
	 * @param dataBloccoIva
	 *            the dataBloccoIva to set
	 */
	public void setDataBloccoIva(Date dataBloccoIva) {
		this.dataBloccoIva = dataBloccoIva;
	}

	/**
	 * @param dataControlloCentriDiCosto
	 *            the dataControlloCentriDiCosto to set
	 */
	public void setDataControlloCentriDiCosto(Date dataControlloCentriDiCosto) {
		this.dataControlloCentriDiCosto = dataControlloCentriDiCosto;
	}

	/**
	 * @param minimaleIVA
	 *            the minimaleIVA to set
	 */
	public void setMinimaleIVA(BigDecimal minimaleIVA) {
		this.minimaleIVA = minimaleIVA;
	}

	/**
	 * @param percTrimestrale
	 *            the percTrimestrale to set
	 */
	public void setPercTrimestrale(BigDecimal percTrimestrale) {
		this.percTrimestrale = percTrimestrale;
	}

	/**
	 * @param proRataSettings
	 *            The proRataSettings to set.
	 */
	public void setProRataSettings(List<ProRataSetting> proRataSettings) {
		this.proRataSettings = proRataSettings;
	}

	/**
	 * @param tipoPeriodicita
	 *            the tipoPeriodicita to set
	 */
	public void setTipoPeriodicita(ETipoPeriodicita tipoPeriodicita) {
		this.tipoPeriodicita = tipoPeriodicita;
	}

	/**
	 * @param usaContoEffettiAttivi
	 *            The usaContoEffettiAttivi to set.
	 */
	public void setUsaContoEffettiAttivi(boolean usaContoEffettiAttivi) {
		this.usaContoEffettiAttivi = usaContoEffettiAttivi;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ContabilitaSettings[");
		buffer.append("calcoloCorrispettivi = ").append(calcoloCorrispettivi);
		buffer.append(" codiceAzienda = ").append(codiceAzienda);
		buffer.append(" codiceIvaPerVentilazione = ").append(codiceIvaPerVentilazione);
		buffer.append(" minimaleIVA = ").append(minimaleIVA);
		buffer.append(" tipoPeriodicita = ").append(tipoPeriodicita);
		buffer.append("]");
		return buffer.toString();
	}
}
