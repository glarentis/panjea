package it.eurotn.panjea.anagrafica.domain.lite;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "anag_sedi_entita")
@NamedQueries({ @NamedQuery(name = "SedeEntitaLite.caricaByEntita", query = " from SedeEntitaLite s where s.entita.id = :paramIdEntita and s.abilitato = true") })
public class SedeEntitaLite extends EntityBase {

	private static final long serialVersionUID = 8841613448946689211L;

	@Column(length = 20)
	private String codice;

	@ManyToOne
	@JoinColumn(name = "entita_id")
	private EntitaLite entita;

	/**
	 * @uml.property name="sede"
	 * @uml.associationEnd
	 */
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "sede_anagrafica_id")
	private SedeAnagraficaLite sede;

	/**
	 * @uml.property name="tipoSede"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private TipoSedeEntita tipoSede;

	/**
	 * @uml.property name="abilitato"
	 */
	@Column(name = "abilitato")
	private boolean abilitato;

	@ManyToOne
	private RapportoBancarioAzienda rapportoBancarioAzienda;

	/**
	 * Costruttore.
	 *
	 */
	public SedeEntitaLite() {
		initialize();
	}

	/**
	 * Crea una {@link SedeEntita} dalla {@link SedeEntitaLite}.
	 *
	 * @return {@link SedeEntitaLite}
	 */
	public SedeEntita createProxySedeEntita() {
		SedeEntita sedeEntita = new SedeEntita();
		sedeEntita.setId(getId());
		return sedeEntita;
	}

	/**
	 * @return Returns the codice.
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return entita
	 * @uml.property name="entita"
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the rapportoBancarioAzienda
	 */
	public RapportoBancarioAzienda getRapportoBancarioAzienda() {
		return rapportoBancarioAzienda;
	}

	/**
	 * @return sede
	 * @uml.property name="sede"
	 */
	public SedeAnagraficaLite getSede() {
		return sede;
	}

	/**
	 * @return Returns the tipoSede.
	 */
	public TipoSedeEntita getTipoSede() {
		return tipoSede;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		abilitato = true;
	}

	/**
	 * @return the abilitato
	 * @uml.property name="abilitato"
	 */
	public boolean isAbilitato() {
		return abilitato;
	}

	/**
	 * @param abilitato
	 *            the abilitato to set
	 * @uml.property name="abilitato"
	 */
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	/**
	 * @param codice
	 *            The codice to set.
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceEntita
	 *            The codiceEntita to set.
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		this.entita.setCodice(codiceEntita);
	}

	/**
	 * @param denominazioneEntita
	 *            The denominazioneEntita to set.
	 */
	public void setDenominazioneEntita(String denominazioneEntita) {
		this.entita.getAnagrafica().setDenominazione(denominazioneEntita);
	}

	/**
	 * @param descrizioneSede
	 *            The descrizioneSede to set.
	 */
	public void setDescrizioneSede(String descrizioneSede) {
		this.sede.setDescrizione(descrizioneSede);
	}

	/**
	 * @param entita
	 *            the entita to set
	 * @uml.property name="entita"
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param idEntita
	 *            The idEntita to set.
	 */
	public void setIdEntita(Integer idEntita) {
		this.entita.setId(idEntita);
	}

	/**
	 * @param rapportoBancarioAzienda
	 *            the rapportoBancarioAzienda to set
	 */
	public void setRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
		this.rapportoBancarioAzienda = rapportoBancarioAzienda;
	}

	/**
	 * @param sede
	 *            the sede to set
	 * @uml.property name="sede"
	 */
	public void setSede(SedeAnagraficaLite sede) {
		this.sede = sede;
	}

	/**
	 * @param tipoEntita
	 *            The tipoEntita to set.
	 */
	public void setTipoEntita(String tipoEntita) {
		this.entita.setTipo(tipoEntita);
	}

	/**
	 * @param tipoSede
	 *            The tipoSede to set.
	 */
	public void setTipoSede(TipoSedeEntita tipoSede) {
		this.tipoSede = tipoSede;
	}
}
