/**
 *
 */
package it.eurotn.panjea.sicurezza.domain;

import it.eurotn.entity.EntityBase;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Domain Object Utente.
 *
 * @author adriano
 * @version 1.0, 20/dic/07
 */
@Entity
@Table(name = "sicu_utenti", uniqueConstraints = @UniqueConstraint(columnNames = { "userName" }))
@NamedQueries({
	@NamedQuery(name = "Utente.caricaByUserName", query = " from Utente u where u.userName = :paramUserName ", hints = {
			@QueryHint(name = "org.hibernate.cacheable", value = "true"),
			@QueryHint(name = "org.hibernate.cacheRegion", value = "utente") }),
			@NamedQuery(name = "Utente.caricaUtentiPos", query = " from Utente u where u.passwordPos != null and u.passwordPos != '' ") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "utente")
public class Utente extends EntityBase {

	private static final long serialVersionUID = 6795854361680570148L;

	public static final String REF = "Utente";

	public static final String PROP_PASSWORD = "password";
	public static final String PROP_ABILITATO = "abilitato";
	public static final String PROP_USER_NAME = "userName";
	public static final String PROP_ID = "id";
	public static final String PROP_DESCRIZIONE = "descrizione";

	@Column(length = 20, nullable = false)
	private String userName;

	@Column(length = 60, nullable = false)
	private String password;

	@Column()
	private Integer passwordPos;

	@Column(length = 50)
	private String descrizione;

	@Column(length = 100)
	private String nome;

	@Column(length = 100)
	private String cognome;

	@Column(length = 30)
	private String cellulare;

	@Column
	private Boolean abilitato;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "utente")
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<DatiMail> datiMail;

	@Embedded
	private DatiBugTracker datiBugTracker;

	@Embedded
	private DatiJasperServer datiJasperServer;

	@ManyToMany
	private Set<Ruolo> ruoli;

	@CollectionOfElements(targetElement = String.class, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "sicu_utenti_menudisablecommands", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "menuDisableCommands")
	private Set<String> menuDisableCommands;

	/**
	 * Costruttore.
	 */
	public Utente() {
		initialize();
	}

	/**
	 * @return Returns the abilitato.
	 */
	public Boolean getAbilitato() {
		return abilitato;
	}

	/**
	 * @return the cellulare
	 */
	public String getCellulare() {
		return cellulare;
	}

	/**
	 * @return the cognome
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * @return the datiBugTracker
	 */
	@Embedded
	public DatiBugTracker getDatiBugTracker() {
		if (datiBugTracker == null) {
			datiBugTracker = new DatiBugTracker();
		}
		return datiBugTracker;
	}

	/**
	 * @return Returns the datiJasperServer.
	 */
	public DatiJasperServer getDatiJasperServer() {
		if (datiJasperServer == null) {
			datiJasperServer = new DatiJasperServer();
		}
		return datiJasperServer;
	}

	/**
	 * @return the datiMail
	 */
	public List<DatiMail> getDatiMail() {
		return datiMail;
	}

	/**
	 * @return the datiMail
	 */
	public DatiMail getDatiMailPredefiniti() {
		DatiMail datiPredefiniti = new DatiMail();

		if (datiMail != null && !datiMail.isEmpty()) {
			for (DatiMail dati : datiMail) {
				if (dati.isPredefinito()) {
					datiPredefiniti = dati;
					break;
				}
			}
			// se ho dati mail ma nessuno è predefinito prendo il primo
			if (datiPredefiniti.isNew()) {
				datiPredefiniti = datiMail.get(0);
			}
		}

		return datiPredefiniti;
	}

	/**
	 * @return the datiMail
	 */
	public DatiMail getDatiMailPredefinitiPec() {
		DatiMail datiPredefiniti = new DatiMail();

		if (datiMail != null && !datiMail.isEmpty()) {
			for (DatiMail dati : datiMail) {
				if (dati.isPredefinito() && dati.isPec()) {
					datiPredefiniti = dati;
					break;
				}
			}
			// se ho dati mail ma nessuno è predefinito prendo il primo pec
			if (datiPredefiniti.isNew()) {
				for (DatiMail dati : datiMail) {
					if (dati.isPec()) {
						datiPredefiniti = dati;
						break;
					}
				}
			}
		}

		return datiPredefiniti;
	}

	/**
	 * @return Returns the descrizione.
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return Returns the menuDisableCommands.
	 */
	public Set<String> getMenuDisableCommands() {
		return menuDisableCommands;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the passwordPos
	 */
	public Integer getPasswordPos() {
		return passwordPos;
	}

	/**
	 * @return Returns the ruoli.
	 */
	public Set<Ruolo> getRuoli() {
		return ruoli;
	}

	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Inizializza i valori di default.
	 */
	@PostLoad
	public void initialize() {
		if (ruoli == null) {
			ruoli = new TreeSet<Ruolo>();
		}
	}

	/**
	 * @param abilitato
	 *            The abilitato to set.
	 */
	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}

	/**
	 * @param cellulare
	 *            the cellulare to set
	 */
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}

	/**
	 * @param cognome
	 *            the cognome to set
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * @param datiBugTracker
	 *            the datiBugTracker to set
	 */
	public void setDatiBugTracker(DatiBugTracker datiBugTracker) {
		if (datiBugTracker == null) {
			datiBugTracker = new DatiBugTracker();
		}
		this.datiBugTracker = datiBugTracker;
	}

	/**
	 * @param datiJasperServer
	 *            The datiJasperServer to set.
	 */
	public void setDatiJasperServer(DatiJasperServer datiJasperServer) {
		this.datiJasperServer = datiJasperServer;
	}

	/**
	 * @param datiMail
	 *            the datiMail to set
	 */
	public void setDatiMail(List<DatiMail> datiMail) {
		this.datiMail = datiMail;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param menuDisableCommands
	 *            The menuDisableCommands to set.
	 */
	public void setMenuDisableCommands(Set<String> menuDisableCommands) {
		this.menuDisableCommands = menuDisableCommands;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param passwordPos
	 *            the passwordPos to set
	 */
	public void setPasswordPos(Integer passwordPos) {
		this.passwordPos = passwordPos;
	}

	/**
	 * @param ruoli
	 *            The ruoli to set.
	 */
	public void setRuoli(Set<Ruolo> ruoli) {
		this.ruoli = ruoli;
	}

	/**
	 * @param userName
	 *            The userName to set.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Utente[");
		buffer.append(super.toString());
		buffer.append(" abilitato = ").append(abilitato);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" password = ").append(password);
		buffer.append(" userName = ").append(userName);
		buffer.append("]");
		return buffer.toString();
	}

}
