package it.eurotn.panjea.rate.domain;

import it.eurotn.panjea.anagrafica.documenti.domain.DatiValidazione;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

/**
 * Area che contiene le {@link Rata} .
 *
 * @author vittorio
 * @version 1.0, 24/nov/2009
 */
@Entity
@Audited
@DiscriminatorValue("RA")
@NamedQueries({ @NamedQuery(name = "AreaRate.ricercaByDocumento", query = "select a from AreaRate a inner join a.documento d where d.id = :paramIdDocumento") })
public class AreaRate extends AreaPartite implements Cloneable {

	private static final long serialVersionUID = 9205732556902989133L;

	@OneToMany(mappedBy = "areaRate", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	@Fetch(FetchMode.JOIN)
	@OrderBy(value = "numeroRata")
	private Set<Rata> rate;

	@Column(precision = 5, scale = 2)
	private BigDecimal percentualeSconto;

	private Integer giorniLimite;

	@Embedded
	private DatiValidazione datiValidazione;

	/**
	 * Costruttore di default.
	 */
	public AreaRate() {
		rate = new TreeSet<Rata>();
		datiValidazione = new DatiValidazione();
	}

	@Override
	public AreaPartite clone() {
		AreaRate areaRateClone = PanjeaEJBUtil.cloneObject(this);
		areaRateClone.setId(null);
		areaRateClone.getDocumento().setId(null);
		return areaRateClone;
	}

	@Override
	public Map<String, Object> fillVariables() {
		return new HashMap<String, Object>();
	}

	/**
	 * @return data finale per poter applicare lo sconto incasso
	 */
	public Date getDataScadenzaScontoFinanziario() {
		Calendar dataScadenzaScontoFinanziario = Calendar.getInstance();
		dataScadenzaScontoFinanziario.setTime(getDocumento().getDataDocumento());
		if (getGiorniLimite() != null) {
			dataScadenzaScontoFinanziario.add(Calendar.DAY_OF_MONTH, getGiorniLimite());
		}
		return dataScadenzaScontoFinanziario.getTime();
	}

	/**
	 * @return the datiValidazione
	 */
	public DatiValidazione getDatiValidazione() {
		return datiValidazione;
	}

	/**
	 * @return the giorniLimite
	 */
	public Integer getGiorniLimite() {
		return giorniLimite;
	}

	/**
	 * @return totale documento con sconto finanziario
	 */
	public BigDecimal getImportoDocumentoConScontoFinanziario() {
		if (getPercentualeSconto() == null || getPercentualeSconto() != null
				&& getPercentualeSconto().compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal percScontoDiv100 = BigDecimal.ONE.subtract(getPercentualeSconto().divide(Importo.HUNDRED, 2,
				RoundingMode.HALF_UP));

		BigDecimal importoScontato = getDocumento().getTotale().getImportoInValuta().multiply(percScontoDiv100);
		return importoScontato;
	}

	/**
	 * @return restituisce la lista di pagamenti dell'area rate.
	 */
	public List<Pagamento> getPagamenti() {
		List<Pagamento> pagamenti = new ArrayList<Pagamento>();
		if (rate != null) {
			for (Rata rata : rate) {
				pagamenti.addAll(rata.getPagamenti());
			}
		}
		return pagamenti;
	}

	/**
	 * @return the percentualeSconto
	 */
	public BigDecimal getPercentualeSconto() {
		return percentualeSconto;
	}

	/**
	 * Restituisce la lista delle {@link Rata} .
	 *
	 * @return List
	 */
	public Set<Rata> getRate() {
		return rate;
	}

	/**
	 * @return totale delle rate contenute nell'area
	 */
	public Importo getTotaleRate() {
		Importo totale = new Importo(getDocumento().getTotale().getCodiceValuta(), getDocumento().getTotale()
				.getTassoDiCambio());
		for (Rata rataCorrente : rate) {
			totale = totale.add(rataCorrente.getImporto(), 2);
		}
		return totale;
	}

	/**
	 * @return definisce se la generazione delle rate e' permessa per questa area rate.
	 */
	public boolean isGenerazioneRateAllowed() {
		return tipoAreaPartita != null && tipoAreaPartita.getTipoOperazione() == TipoOperazione.GENERA;
	}

	/**
	 * @param datiValidazione
	 *            the datiValidazione to set
	 */
	public void setDatiValidazione(DatiValidazione datiValidazione) {
		this.datiValidazione = datiValidazione;
	}

	/**
	 * @param giorniLimite
	 *            the giorniLimite to set
	 */
	public void setGiorniLimite(Integer giorniLimite) {
		this.giorniLimite = giorniLimite;
	}

	/**
	 * @param percentualeSconto
	 *            the percentualeSconto to set
	 */
	public void setPercentualeSconto(BigDecimal percentualeSconto) {
		this.percentualeSconto = percentualeSconto;
	}

	/**
	 * Setta la lista di {@link Rata} .
	 *
	 * @param rate
	 *            la lista ( {@link List} di tipo Rata) to set
	 */
	public void setRate(Set<Rata> rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "AreaRate [datiValidazione=" + datiValidazione + ",  getCodicePagamento()=" + getCodicePagamento()
				+ ", getDocumento()=" + getDocumento() + ", getTipoAreaPartita()=" + getTipoAreaPartita() + "]";
	}

}
