/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.PostLoad;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Classe embedded PersonaFisica.
 *
 * @author adriano
 * @version 1.0, 13/dic/07
 */
@Embeddable
public class PersonaFisica implements Serializable {

	private static final long serialVersionUID = 7233286196747552604L;

	public static final String REF = "PersonaFisica";
	public static final String PROP_SESSO = "sesso";
	public static final String PROP_DATA_NASCITA = "dataNascita";
	public static final String PROP_NOME = "nome";
	public static final String PROP_VIA_RESIDENZA = "viaResidenza";
	public static final String PROP_COGNOME = "cognome";

	@Column(name = "personaFisica_cognome", length = 40)
	private String cognome;

	@Column(name = "personaFisica_nome", length = 40)
	private String nome;

	@Column(name = "personaFisica_dataNascita")
	@Temporal(TemporalType.DATE)
	private Date dataNascita;

	@Column(name = "personaFisica_sesso")
	private Sesso sesso;

	@Embedded
	@AssociationOverrides({
		@AssociationOverride(name = "nazione", joinColumns = @JoinColumn(name = "nazioneNascita_pf_id")),
		@AssociationOverride(name = "livelloAmministrativo1", joinColumns = @JoinColumn(name = "lvlNascita1_pf_id")),
		@AssociationOverride(name = "livelloAmministrativo2", joinColumns = @JoinColumn(name = "lvlNascita2_pf_id")),
		@AssociationOverride(name = "livelloAmministrativo3", joinColumns = @JoinColumn(name = "lvlNascita3_pf_id")),
		@AssociationOverride(name = "livelloAmministrativo4", joinColumns = @JoinColumn(name = "lvlNascita4_pf_id")),
		@AssociationOverride(name = "localita", joinColumns = @JoinColumn(name = "localitaNascita_pf_id")),
		@AssociationOverride(name = "cap", joinColumns = @JoinColumn(name = "capNascita_pf_id")) })
	private DatiGeografici datiGeograficiNascita;

	@Embedded
	@AssociationOverrides({
		@AssociationOverride(name = "nazione", joinColumns = @JoinColumn(name = "nazioneResidenza_pf_id")),
		@AssociationOverride(name = "livelloAmministrativo1", joinColumns = @JoinColumn(name = "lvlResidenza1_pf_id")),
		@AssociationOverride(name = "livelloAmministrativo2", joinColumns = @JoinColumn(name = "lvlResidenza2_pf_id")),
		@AssociationOverride(name = "livelloAmministrativo3", joinColumns = @JoinColumn(name = "lvlResidenza3_pf_id")),
		@AssociationOverride(name = "livelloAmministrativo4", joinColumns = @JoinColumn(name = "lvlResidenza4_pf_id")),
		@AssociationOverride(name = "localita", joinColumns = @JoinColumn(name = "localitaResidenza_pf_id")),
		@AssociationOverride(name = "cap", joinColumns = @JoinColumn(name = "capResidenza_pf_id")) })
	private DatiGeografici datiGeograficiResidenza;

	@Column(name = "personaFisica_viaResidenza", length = 50)
	private String viaResidenza;

	@Column(name = "personaFisica_titolo", length = 10)
	private String titolo;

	@Column(name = "personaFisica_codiceEORI", length = 17)
	private String codiceEori;

	/**
	 * Costruttore.
	 */
	public PersonaFisica() {
		initialize();
	}

	/**
	 * @return the codiceEori
	 */
	public String getCodiceEori() {
		return codiceEori;
	}

	/**
	 * @return Returns the cognome.
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * @return Returns the dataNascita.
	 */
	public Date getDataNascita() {
		return dataNascita;
	}

	/**
	 * @return the datiGeograficiNascita
	 */
	public DatiGeografici getDatiGeograficiNascita() {
		if (datiGeograficiNascita == null) {
			datiGeograficiNascita = new DatiGeografici();
		}
		return datiGeograficiNascita;
	}

	/**
	 * @return the datiGeograficiResidenza
	 */
	public DatiGeografici getDatiGeograficiResidenza() {
		if (datiGeograficiResidenza == null) {
			datiGeograficiResidenza = new DatiGeografici();
		}
		return datiGeograficiResidenza;
	}

	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return Returns the sesso.
	 */
	public Sesso getSesso() {
		return sesso;
	}

	/**
	 *
	 * @return sesso in formato stringa
	 */
	public String getSessoStringFormat() {
		if (sesso == null) {
			return "";
		}
		return sesso.name();
	}

	/**
	 * @return the titolo
	 */
	public String getTitolo() {
		return titolo;
	}

	/**
	 * @return Returns the viaResidenza.
	 */
	public String getViaResidenza() {
		return viaResidenza;
	}

	/**
	 * Init degli embedded datiGeografici.
	 */
	@PostLoad
	public void initialize() {
	}

	/**
	 * @param codiceEori the codiceEori to set
	 */
	public void setCodiceEori(String codiceEori) {
		this.codiceEori = codiceEori;
	}

	/**
	 * @param cognome
	 *            The cognome to set.
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * @param dataNascita
	 *            The dataNascita to set.
	 */
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	/**
	 * @param datiGeograficiNascita
	 *            the datiGeograficiNascita to set
	 */
	public void setDatiGeograficiNascita(DatiGeografici datiGeograficiNascita) {
		this.datiGeograficiNascita = datiGeograficiNascita;
	}

	/**
	 * @param datiGeograficiResidenza
	 *            the datiGeograficiResidenza to set
	 */
	public void setDatiGeograficiResidenza(DatiGeografici datiGeograficiResidenza) {
		this.datiGeograficiResidenza = datiGeograficiResidenza;
	}

	/**
	 * @param nome
	 *            The nome to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param sesso
	 *            The sesso to set.
	 */
	public void setSesso(Sesso sesso) {
		this.sesso = sesso;
	}

	/**
	 *
	 * @param sessoParam
	 *            .
	 */
	public void setSessoStringFormat(String sessoParam) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param titolo the titolo to set
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	/**
	 * @param viaResidenza
	 *            The viaResidenza to set.
	 */
	public void setViaResidenza(String viaResidenza) {
		this.viaResidenza = viaResidenza;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("PersonaFisica[");
		buffer.append(" datiGeograficiNascita = ").append(datiGeograficiNascita);
		buffer.append(" cognome = ").append(cognome);
		buffer.append(" dataNascita = ").append(dataNascita);
		buffer.append(" nome = ").append(nome);
		buffer.append(" datiGeograficiResidenza = ").append(datiGeograficiResidenza);
		buffer.append(" sesso = ").append(sesso);
		buffer.append(" viaResidenza = ").append(viaResidenza);
		buffer.append("]");
		return buffer.toString();
	}

}
