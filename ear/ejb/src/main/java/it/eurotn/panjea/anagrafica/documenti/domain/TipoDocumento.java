package it.eurotn.panjea.anagrafica.documenti.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.classedocumento.ClasseTipoDocumentoFactory;
import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClientePotenzialeLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.util.PanjeaEJBUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "docu_tipi_documento", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda", "codice" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "tipoDocumento")
public class TipoDocumento extends EntityBase implements java.io.Serializable, Cloneable {

	public enum TipoEntita {
		CLIENTE(ClienteLite.class), FORNITORE(FornitoreLite.class), AZIENDA(AziendaLite.class), BANCA(null), VETTORE(
				VettoreLite.class), AGENTE(AgenteLite.class), CLIENTE_POTENZIALE(ClientePotenzialeLite.class);

		private Class<?> classTipoEntitaLite;

		/**
		 * Costruttore.
		 * 
		 * @param classTipoEntitaLite
		 *            classe lite <B>NB</B>passo Class<?> perchè AZIENDA e BANCA non derivano da entitaLite
		 * 
		 *            .
		 */
		private TipoEntita(final Class<?> classTipoEntitaLite) {
			this.classTipoEntitaLite = classTipoEntitaLite;
		}

		/**
		 * 
		 * @return crea un istanza per il tipo di entità
		 */
		public Object createInstanceLite() {
			try {
				return classTipoEntitaLite.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Errore nel creare l'istanza per la classe " + classTipoEntitaLite.getName());
			}
		}

		/**
		 * @return the classTipoEntitaLite
		 */
		public Class<?> getClassTipoEntitaLite() {
			return classTipoEntitaLite;
		}

	}

	private static final long serialVersionUID = -2712533148079047650L;

	/**
	 * @return Returns the serialversionuid.
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(length = 18)
	@Index(name = "codice")
	private String codice;

	@Column(length = 60)
	private String descrizione;

	@Index(name = "azienda")
	private java.lang.String codiceAzienda;

	private boolean gestioneIntra;

	@Column(length = 10)
	private String registroProtocollo;

	@Column(length = 50)
	private String patternNumeroDocumento;

	@Enumerated
	private TipoEntita tipoEntita;

	private boolean righeIvaEnable;

	private boolean notaCreditoEnable;
	private boolean notaAddebitoEnable;

	@Column(length = 100)
	private String classeTipoDocumento;

	private boolean abilitato;

	private boolean gestioneContratto;

	private String colore;

	private boolean gestioneLotti;

	@Transient
	private IClasseTipoDocumento classeTipoDocumentoInstance = null;

	private boolean richiediDocumentoCollegato;

	/**
	 * Costruttore di default.
	 */
	public TipoDocumento() {
		super();
		initialize();
	}

	@Override
	public Object clone() {
		TipoDocumento tipoDocumentoClone = (TipoDocumento) PanjeaEJBUtil.cloneObject(this);
		tipoDocumentoClone.setId(null);

		return tipoDocumentoClone;
	}

	/**
	 * @return classeTipoDocumento
	 */
	public String getClasseTipoDocumento() {
		return classeTipoDocumento;
	}

	/**
	 * @return classeTipoDocumentoInstance
	 */
	public IClasseTipoDocumento getClasseTipoDocumentoInstance() {
		if (this.classeTipoDocumentoInstance == null && this.classeTipoDocumento != null) {
			this.classeTipoDocumentoInstance = ClasseTipoDocumentoFactory
					.getClasseTipoDocumento(this.classeTipoDocumento);
		}
		return this.classeTipoDocumentoInstance;
	}

	/**
	 * @return codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return codiceAzienda
	 */
	public java.lang.String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return Returns the colore.
	 */
	public String getColore() {
		return colore;
	}

	/**
	 * @return descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the patternNumeroDocumento
	 */
	public String getPatternNumeroDocumento() {
		return patternNumeroDocumento;
	}

	/**
	 * @return registroProtocollo
	 */
	public String getRegistroProtocollo() {
		return registroProtocollo;
	}

	/**
	 * @return tipoEntita
	 */
	public TipoEntita getTipoEntita() {
		return tipoEntita;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		abilitato = true;
		colore = "#FFFFFF";
		richiediDocumentoCollegato = false;
		gestioneLotti = false;
	}

	/**
	 * @return isAbilitato
	 */
	public boolean isAbilitato() {
		return abilitato;
	}

	/**
	 * @return the gestioneContratto
	 */
	public boolean isGestioneContratto() {
		return gestioneContratto;
	}

	/**
	 * @return Returns the gestioneIntra.
	 */
	public boolean isGestioneIntra() {
		return gestioneIntra;
	}

	/**
	 * @return Returns the gestioneLotti.
	 */
	public boolean isGestioneLotti() {
		return gestioneLotti;
	}

	/**
	 * @return Returns the notaAddebitoEnable.
	 */
	public boolean isNotaAddebitoEnable() {
		return notaAddebitoEnable;
	}

	/**
	 * @return isNotaCreditoEnable
	 */
	public boolean isNotaCreditoEnable() {
		return notaCreditoEnable;
	}

	/**
	 * @return Returns the richiediDocumentoCollegato.
	 */
	public boolean isRichiediDocumentoCollegato() {
		return richiediDocumentoCollegato;
	}

	/**
	 * @return isRigheIvaEnable
	 */
	public boolean isRigheIvaEnable() {
		return righeIvaEnable;
	}

	/**
	 * @param abilitato
	 *            the abilitato to set
	 */
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	/**
	 * @param classeTipoDocumento
	 *            the classeTipoDocumento to set
	 */
	public void setClasseTipoDocumento(String classeTipoDocumento) {
		this.classeTipoDocumento = classeTipoDocumento;
	}

	/**
	 * @param classeTipoDocumentoInstance
	 *            the classeTipoDocumentoInstance to set
	 */
	public void setClasseTipoDocumentoInstance(IClasseTipoDocumento classeTipoDocumentoInstance) {
		this.classeTipoDocumentoInstance = classeTipoDocumentoInstance;
		this.classeTipoDocumento = classeTipoDocumentoInstance != null ? classeTipoDocumentoInstance.getClass()
				.getName() : null;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(java.lang.String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param colore
	 *            The colore to set.
	 */
	public void setColore(String colore) {
		this.colore = colore;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param gestioneContratto
	 *            the gestioneContratto to set
	 */
	public void setGestioneContratto(boolean gestioneContratto) {
		this.gestioneContratto = gestioneContratto;
	}

	/**
	 * @param gestioneIntra
	 *            The gestioneIntra to set.
	 */
	public void setGestioneIntra(boolean gestioneIntra) {
		this.gestioneIntra = gestioneIntra;
	}

	/**
	 * @param gestioneLotti
	 *            The gestioneLotti to set.
	 */
	public void setGestioneLotti(boolean gestioneLotti) {
		this.gestioneLotti = gestioneLotti;
	}

	/**
	 * @param notaAddebitoEnable
	 *            The notaAddebitoEnable to set.
	 */
	public void setNotaAddebitoEnable(boolean notaAddebitoEnable) {
		this.notaAddebitoEnable = notaAddebitoEnable;
		if (notaAddebitoEnable) {
			notaCreditoEnable = !notaAddebitoEnable;
		}
	}

	/**
	 * @param notaCreditoEnable
	 *            the notaCreditoEnable to set
	 */
	public void setNotaCreditoEnable(boolean notaCreditoEnable) {
		this.notaCreditoEnable = notaCreditoEnable;
		if (notaCreditoEnable) {
			this.notaAddebitoEnable = !notaCreditoEnable;
		}
	}

	/**
	 * @param patternNumeroDocumento
	 *            the patternNumeroDocumento to set
	 */
	public void setPatternNumeroDocumento(String patternNumeroDocumento) {
		this.patternNumeroDocumento = patternNumeroDocumento;
	}

	/**
	 * @param registroProtocollo
	 *            the registroProtocollo to set
	 */
	public void setRegistroProtocollo(String registroProtocollo) {
		this.registroProtocollo = registroProtocollo;
	}

	/**
	 * @param richiediDocumentoCollegato
	 *            The richiediDocumentoCollegato to set.
	 */
	public void setRichiediDocumentoCollegato(boolean richiediDocumentoCollegato) {
		this.richiediDocumentoCollegato = richiediDocumentoCollegato;
	}

	/**
	 * @param righeIvaEnable
	 *            the righeIvaEnable to set
	 */
	public void setRigheIvaEnable(boolean righeIvaEnable) {
		this.righeIvaEnable = righeIvaEnable;
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(TipoEntita tipoEntita) {
		this.tipoEntita = tipoEntita;
	}

	@Override
	public String toString() {
		return "TipoDocumento [codice=" + codice + "]";
	}

}
