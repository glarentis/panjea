package it.eurotn.panjea.stampe.domain;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.stampe.manager.FormulaNumeroCopieCalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.MetaValue;

/**
 * Gestisce i layout di stampa per i vari TipiDocumento.
 *
 * @author giangi
 * @version 1.0, 26/mar/2012
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("TD")
@NamedQuery(name = "LayoutStampaDocumento.caricaAll", query = "select l from LayoutStampaDocumento l ", hints = {
		@QueryHint(name = "org.hibernate.cacheable", value = "true"),
		@QueryHint(name = "org.hibernate.cacheRegion", value = "layoutStampa") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "layoutStampa")
public class LayoutStampaDocumento extends LayoutStampa {

	private static final long serialVersionUID = 1705760595437069415L;

	private static Map<String, String[]> possibleVariablesForTipoDocumento;

	static {
		possibleVariablesForTipoDocumento = new HashMap<String, String[]>();
		possibleVariablesForTipoDocumento.put(TipoAreaMagazzino.class.getName(),
				new String[] { AreaMagazzino.FORMULA_VAR_N_COPIE_TIPO_DOCUMENTO });
		possibleVariablesForTipoDocumento.put(TipoAreaOrdine.class.getName(), new String[] {});
		possibleVariablesForTipoDocumento.put(TipoAreaPreventivo.class.getName(), new String[] {});
		possibleVariablesForTipoDocumento = java.util.Collections.unmodifiableMap(possibleVariablesForTipoDocumento);
	}

	@Any(metaColumn = @Column(name = "tipoDocumentoType"), optional = true)
	@AnyMetaDef(idType = "int", metaType = "string", metaValues = {
			@MetaValue(targetEntity = TipoAreaMagazzino.class, value = "TAM"),
			@MetaValue(targetEntity = TipoAreaOrdine.class, value = "TAO"),
			@MetaValue(targetEntity = TipoAreaPreventivo.class, value = "TAP") })
	@JoinColumn(name = "tipoDocumentoId")
	private ITipoAreaDocumento tipoAreaDocumento;

	@ManyToOne(optional = true)
	@JoinColumn(name = "entita_id")
	private EntitaLite entita;

	@ManyToOne(optional = true)
	@JoinColumn(name = "sedeEntita_id")
	private SedeEntita sedeEntita;

	/**
	 * Costruttore.
	 */
	public LayoutStampaDocumento() {
		super();
	}

	/**
	 * Costruttore.
	 *
	 * @param tipoAreaDocumento
	 *            tipoAreaDocumento
	 * @param entita
	 *            entita
	 * @param sedeEntita
	 *            sedeEntita
	 */
	public LayoutStampaDocumento(final ITipoAreaDocumento tipoAreaDocumento, final EntitaLite entita,
			final SedeEntita sedeEntita) {
		super();
		setTipoAreaDocumento(tipoAreaDocumento);
		setEntita(entita);
		setSedeEntita(sedeEntita);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		LayoutStampaDocumento other = (LayoutStampaDocumento) obj;
		if (reportName == null) {
			if (other.reportName != null) {
				return false;
			}
		} else if (!reportName.equals(other.reportName)) {
			return false;
		}
		if (!Objects.equals(other.getSedeEntita(), sedeEntita)) {
			return false;
		}
		if (!Objects.equals(other.getEntita(), entita)) {
			return false;
		}
		return true;
	}

	/**
	 * Restituisce la chiave di identificazione del layout di stampa.
	 *
	 * @return chiave
	 */
	@Override
	public String getChiave() {
		StringBuilder sb = new StringBuilder(100);
		if (tipoAreaDocumento != null) {
			sb.append(tipoAreaDocumento.getTipoDocumento().getCodice());
			sb.append(tipoAreaDocumento.getClass().getSimpleName());
		}
		if (getEntita() != null && !getEntita().isNew()) {
			sb.append(getEntita().getId());
		}
		if (getSedeEntita() != null && !getSedeEntita().isNew()) {
			sb.append(getSedeEntita().getId());
		}
		if (getReportName() != null) {
			sb.append(getReportName());
		}
		return sb.toString();
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * Restituisce il numero copie per la stampa.
	 *
	 * @param areaDocumento
	 *            area di riferimento
	 * @return numero copie
	 */
	public int getNumeroCopie(IAreaDocumento areaDocumento) {
		return FormulaNumeroCopieCalculator.calcolaNumeroCopie(getFormulaNumeroCopie(), areaDocumento.fillVariables());
	}

	/**
	 * @return restituisce le variabili che possono essere utilizzate per comporre la formula per il calcolo del numero
	 *         copie
	 */
	@Override
	public String[] getPossibleVariables() {
		if (tipoAreaDocumento == null) {
			return new String[] {};
		}
		return possibleVariablesForTipoDocumento.get(tipoAreaDocumento.getClass().getName());
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return Returns the tipoAreaDocumento.
	 */
	public ITipoAreaDocumento getTipoAreaDocumento() {
		return tipoAreaDocumento;
	}

	/**
	 * @return tipo documento associato
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoAreaDocumento != null ? tipoAreaDocumento.getTipoDocumento() : null;
	}

	/**
	 * @param codiceEntita
	 *            The codiceEntita to set.
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		if (getEntita() != null) {
			getEntita().setCodice(codiceEntita);
		}
	}

	/**
	 * @param denominazioneEntita
	 *            The denominazioneEntita to set.
	 */
	public void setDenominazioneEntita(String denominazioneEntita) {
		if (getEntita() != null) {
			getEntita().getAnagrafica().setDenominazione(denominazioneEntita);
		}
	}

	/**
	 * @param descrizioneSede
	 *            The descrizioneSede to set.
	 */
	public void setDescrizioneSede(String descrizioneSede) {
		if (getSedeEntita() != null) {
			getSedeEntita().getSede().setDescrizione(descrizioneSede);
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
	 * @param idEntita
	 *            The idEntita to set.
	 */
	public void setIdEntita(Integer idEntita) {
		if (getEntita() != null) {
			getEntita().setId(idEntita);
		}
	}

	/**
	 * @param idSede
	 *            The idSede to set.
	 */
	public void setIdSede(Integer idSede) {
		if (idSede == null) {
			setSedeEntita(null);
		} else {
			setSedeEntita(new SedeEntita());
			getSedeEntita().setId(idSede);
		}
	}

	/**
	 * @param indirizzoSede
	 *            The indirizzoSede to set.
	 */
	public void setIndirizzoSede(String indirizzoSede) {
		if (getSedeEntita() != null) {
			getSedeEntita().getSede().setIndirizzo(indirizzoSede);
		}
	}

	/**
	 * @param sedeEntita
	 *            the sedeEntita to set
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	/**
	 * @param tipoAreaDocumento
	 *            The tipoAreaDocumento to set.
	 */
	public void setTipoAreaDocumento(ITipoAreaDocumento tipoAreaDocumento) {
		this.tipoAreaDocumento = tipoAreaDocumento;
	}

	/**
	 * @param versionEntita
	 *            The versionEntita to set.
	 */
	public void setVersionEntita(Integer versionEntita) {
		if (getEntita() != null) {
			getEntita().setVersion(versionEntita);
		}
	}

	/**
	 * @param versionSede
	 *            The versionSede to set.
	 */
	public void setVersionSede(Integer versionSede) {
		if (getSedeEntita() != null) {
			getSedeEntita().setVersion(versionSede);
		}
	}

}
