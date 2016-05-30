package it.eurotn.panjea.anagrafica.util;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Contatto;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.lite.ClientePotenzialeLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Contiene i dati per visualizzare i dati delle anagrafiche come una rubrica.<br/>
 * I dati possono provenire da Entita-Sedi-Contatti-Utenti.
 *
 * @author giangi
 * @version 1.0, 06/apr/2011
 *
 */
public class RubricaDTO implements Serializable {
	public enum ChildrenType {
		SEDE, CONTATTO
	}

	private static final long serialVersionUID = -6374329441083913788L;;

	private static Logger logger = Logger.getLogger(RubricaDTO.class);

	private String piva;
	private String codiceFiscale;
	private String denominazione;
	private String codice;
	private String indirizzo;

	private String nazione;

	private String cap;
	private String localita;
	private String livelloAmministrativo1;
	private String livelloAmministrativo2;
	private String livelloAmministrativo3;
	private String livelloAmministrativo4;
	private String telefono;
	private String email;
	private String indirizzoPEC;
	private String indirizzoMailSpedizione;
	private String fax;
	private String cell;
	private Class<?> rowClass;
	private int idEntita;
	private int id;
	private int version;
	private List<RubricaDTO> rubricaChlidren;

	private Integer idRif;
	private boolean sedePrincipale;

	private EntitaLite entita;
	private EntitaLite entitaRiferimento;

	/**
	 * Costruttore.
	 *
	 */
	public RubricaDTO() {
		super();
		rubricaChlidren = new ArrayList<RubricaDTO>();
	}

	/**
	 * @param rubricaDTOFiglio
	 *            una riga di rubrica da aggiungere all'elemento.
	 */
	public void addRubrica(RubricaDTO rubricaDTOFiglio) {
		rubricaChlidren.add(rubricaDTOFiglio);
	}

	/**
	 * @return Returns the cap.
	 */
	public String getCap() {
		return cap;
	}

	/**
	 * @return Returns the cell.
	 */
	public String getCell() {
		return cell;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return Returns the codiceFiscale.
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	/**
	 * @return Returns the denominazione.
	 */
	public String getDenominazione() {
		return denominazione;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	/**
	 * Entita a cui fa riferimento il DTO.
	 *
	 * @return entità
	 */
	public EntitaLite getEntita() {
		if (Entita.class.isAssignableFrom(rowClass)) {
			if (entita == null) {
				try {
					entita = ((Entita) rowClass.newInstance()).getEntitaLite();
					entita.setId(id);
					entita.setVersion(version);
					entita.setDenominazione(denominazione);
					entita.setCodice(Integer.parseInt(codice));
				} catch (Exception e) {
					logger.error("--> errore durante la creazione dell'entità per la riga di rubrica DTO", e);
					throw new RuntimeException("errore durante la creazione dell'entità per la riga di rubrica DTO", e);
				}
			}
			return entita;
		}

		return entitaRiferimento;
	}

	/**
	 * @return Returns the fax.
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the idEntita
	 */
	public int getIdEntita() {
		return idEntita;
	}

	/**
	 * @return the idRif
	 */
	public Integer getIdRif() {
		return idRif;
	}

	/**
	 * @return Returns the indirizzo.
	 */
	public String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * @return the indirizzoMailSpedizione
	 */
	public String getIndirizzoMailSpedizione() {
		return indirizzoMailSpedizione;
	}

	/**
	 * @return the indirizzoPEC
	 */
	public String getIndirizzoPEC() {
		if (indirizzoPEC == null) {
			indirizzoPEC = "";
		}
		return indirizzoPEC;
	}

	/**
	 * @return the livelloAmministrativo1
	 */
	public String getLivelloAmministrativo1() {
		return livelloAmministrativo1;
	}

	/**
	 * @return the livelloAmministrativo2
	 */
	public String getLivelloAmministrativo2() {
		return livelloAmministrativo2;
	}

	/**
	 * @return the livelloAmministrativo3
	 */
	public String getLivelloAmministrativo3() {
		return livelloAmministrativo3;
	}

	/**
	 * @return the livelloAmministrativo4
	 */
	public String getLivelloAmministrativo4() {
		return livelloAmministrativo4;
	}

	/**
	 * @return Returns the localita.
	 */
	public String getLocalita() {
		return localita;
	}

	/**
	 * @return Returns the nazione.
	 */
	public String getNazione() {
		return nazione;
	}

	/**
	 * @return Returns the piva.
	 */
	public String getPiva() {
		return piva;
	}

	/**
	 * @return Returns the rowClass.
	 */
	public Class<?> getRowClass() {
		return rowClass;
	}

	/**
	 * @return Returns the rubricaDTO.
	 */
	public List<RubricaDTO> getRubricaDTO() {
		return rubricaChlidren;
	}

	/**
	 * @return Returns the telefono.
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @return Returns the email.
	 */
	public boolean isIndirizzoEmailEPECPresenti() {
		return !getEmail().isEmpty() && !getIndirizzoPEC().isEmpty();
	}

	/**
	 * @return <code>true</code> se esiste almeno il telefono o email o cell o fax
	 */
	public boolean isRecapitiPresenti() {
		return (telefono != null && !telefono.isEmpty()) || (email != null && !email.isEmpty())
				|| (cell != null && !cell.isEmpty()) || (fax != null && !fax.isEmpty());
	}

	/**
	 * @return the sedePrincipale
	 */
	public boolean isSedePrincipale() {
		return sedePrincipale;
	}

	/**
	 * @param cap
	 *            The cap to set.
	 */
	public void setCap(String cap) {
		this.cap = cap;
	}

	/**
	 * @param cell
	 *            The cell to set.
	 */
	public void setCell(String cell) {
		this.cell = cell;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceFiscale
	 *            The codiceFiscale to set.
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	/**
	 * @param denominazione
	 *            The denominazione to set.
	 */
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	/**
	 * @param email
	 *            The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param entitaRiferimento
	 *            the entitaRiferimento to set
	 */
	public void setEntitaRiferimento(EntitaLite entitaRiferimento) {
		this.entitaRiferimento = entitaRiferimento;
	}

	/**
	 * @param fax
	 *            The fax to set.
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param idEntita
	 *            the idEntita to set
	 */
	public void setIdEntita(int idEntita) {
		this.idEntita = idEntita;
	}

	/**
	 * @param idRif
	 *            the idRif to set
	 */
	public void setIdRif(Integer idRif) {
		this.idRif = idRif;
	}

	/**
	 * @param indirizzo
	 *            The indirizzo to set.
	 */
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	/**
	 * @param indirizzoMailSpedizione
	 *            the indirizzoMailSpedizione to set
	 */
	public void setIndirizzoMailSpedizione(String indirizzoMailSpedizione) {
		this.indirizzoMailSpedizione = indirizzoMailSpedizione;
	}

	/**
	 * @param indirizzoPEC
	 *            the indirizzoPEC to set
	 */
	public void setIndirizzoPEC(String indirizzoPEC) {
		this.indirizzoPEC = indirizzoPEC;
	}

	/**
	 * @param livelloAmministrativo1
	 *            the livelloAmministrativo1 to set
	 */
	public void setLivelloAmministrativo1(String livelloAmministrativo1) {
		this.livelloAmministrativo1 = livelloAmministrativo1;
	}

	/**
	 * @param livelloAmministrativo2
	 *            the livelloAmministrativo2 to set
	 */
	public void setLivelloAmministrativo2(String livelloAmministrativo2) {
		this.livelloAmministrativo2 = livelloAmministrativo2;
	}

	/**
	 * @param livelloAmministrativo3
	 *            the livelloAmministrativo3 to set
	 */
	public void setLivelloAmministrativo3(String livelloAmministrativo3) {
		this.livelloAmministrativo3 = livelloAmministrativo3;
	}

	/**
	 * @param livelloAmministrativo4
	 *            the livelloAmministrativo4 to set
	 */
	public void setLivelloAmministrativo4(String livelloAmministrativo4) {
		this.livelloAmministrativo4 = livelloAmministrativo4;
	}

	/**
	 * @param localita
	 *            The localita to set.
	 */
	public void setLocalita(String localita) {
		this.localita = localita;
	}

	/**
	 * @param nazione
	 *            The nazione to set.
	 */
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}

	/**
	 * @param piva
	 *            The piva to set.
	 */
	public void setPiva(String piva) {
		this.piva = piva;
	}

	/**
	 * @param rowClass
	 *            The rowClass to set.
	 */
	public void setRowClass(Class<?> rowClass) {
		this.rowClass = rowClass;
	}

	/**
	 * @param sedePrincipale
	 *            the sedePrincipale to set
	 */
	public void setSedePrincipale(boolean sedePrincipale) {
		this.sedePrincipale = sedePrincipale;
	}

	/**
	 * @param telefono
	 *            The telefono to set.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(String tipo) {
		if ("C".equals(tipo)) {
			rowClass = Cliente.class;
		} else if ("CP".equals(tipo)) {
			rowClass = ClientePotenzialeLite.class;
		} else if ("F".equals(tipo)) {
			rowClass = Fornitore.class;
		} else if ("V".equals(tipo)) {
			rowClass = Vettore.class;
		} else if ("A".equals(tipo)) {
			rowClass = Agente.class;
		}
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * Aggiorna i dati della rubricaDTO in base al tipo specificato. (Sede o Contatto). Nel caso la rubrica da
	 * aggiungere sia una sede principale, verranno aggiornati i dati della rubrica "padre" e riestituita.
	 *
	 * @param rubricaDTO
	 *            rubrica da aggiungere
	 * @param type
	 *            tipo della rubrica da aggiungere
	 * @return rubrica aggiunta
	 */
	public RubricaDTO updateData(RubricaDTO rubricaDTO, ChildrenType type) {

		RubricaDTO rubricaResult = null;

		switch (type) {
		case SEDE:
			if (rubricaDTO.isSedePrincipale()) {
				this.setCap(rubricaDTO.getCap());
				this.setEmail(rubricaDTO.getEmail());
				this.setIndirizzoPEC(rubricaDTO.getIndirizzoPEC());
				this.setIndirizzoMailSpedizione(rubricaDTO.getIndirizzoMailSpedizione());
				this.setFax(rubricaDTO.getFax());
				this.setIndirizzo(rubricaDTO.getIndirizzo());
				this.setLivelloAmministrativo1(rubricaDTO.getLivelloAmministrativo1());
				this.setLivelloAmministrativo2(rubricaDTO.getLivelloAmministrativo2());
				this.setLivelloAmministrativo3(rubricaDTO.getLivelloAmministrativo3());
				this.setLivelloAmministrativo4(rubricaDTO.getLivelloAmministrativo4());
				this.setLocalita(rubricaDTO.getLocalita());
				this.setNazione(rubricaDTO.getNazione());
				this.setTelefono(rubricaDTO.getTelefono());

				rubricaResult = this;
			} else {
				rubricaDTO.setRowClass(SedeEntita.class);
				rubricaDTO.setEntitaRiferimento(getEntita());
				this.rubricaChlidren.add(rubricaDTO);

				rubricaResult = rubricaDTO;
			}
			break;

		case CONTATTO:
			rubricaDTO.setRowClass(Contatto.class);
			rubricaDTO.setEntitaRiferimento(getEntita());
			this.rubricaChlidren.add(0, rubricaDTO);

			rubricaResult = rubricaDTO;
			break;
		default:
			break;
		}

		return rubricaResult;
	}
}
