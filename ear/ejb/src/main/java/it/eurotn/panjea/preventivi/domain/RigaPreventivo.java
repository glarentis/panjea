package it.eurotn.panjea.preventivi.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.interfaces.IRigaDocumento;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.panjea.preventivi.util.interfaces.IRigaDTO;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "prev_righe_preventivo")
@Audited
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_RIGA", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue("R")
@NamedQueries({
		@NamedQuery(name = "RigaPreventivo.caricaByAreaPreventivo", query = "select r from RigaPreventivo r where r.areaPreventivo.id =:paramAreaPreventivo order by r.ordinamento "),
		@NamedQuery(name = "RigaPreventivo.cancellaByAreaPreventivo", query = "delete from RigaPreventivo r where r.areaPreventivo.id = :paramAreaPreventivo"),
		@NamedQuery(name = "RigaPreventivo.cancellaRigheComponentiByAreaPreventivo", query = "delete from RigaArticoloComponenteOrdine r where r.areaOrdine.id = :paramAreaOrdine"), })
public abstract class RigaPreventivo extends EntityBase implements IRigaDocumento {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	protected AreaPreventivo areaPreventivo;

	private int numeroRiga;

	private double ordinamento;

	/**
	 * Campo utilizzato per salvare le informazioni delle gerarchia dei documenti che si riferiscono alla riga. Il
	 * formato con il quale verranno salvati sarà<br>
	 * [id dell'area]|[id dell'area]|[id dell'area]|[id dell'area]|.....
	 */
	private String chiave;

	/**
	 * Rappresenta il livello della riga all'interno del documento. Le righe del documento saranno di livello 0 mentre
	 * in quelle collegate il livello sarà in base alle righe testata.
	 */
	private int livello;

	/**
	 * Se ho una riga testata la riga è legata ad essa.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private RigaTestata rigaTestataCollegata;

	@Column(length = 4000)
	private String noteRiga;

	@Column(length = 4000)
	private String noteLinguaRiga;

	/**
	 * Indica se le note della riga dovranno essere riportate sul documento di destinazione.
	 */
	private boolean noteSuDestinazione;

	private boolean rigaAutomatica;

	/**
	 * Inizializza i valori della riga magazzino.
	 */
	{
		this.livello = 0;
		this.ordinamento = Calendar.getInstance().getTimeInMillis();
		this.noteSuDestinazione = true;
		this.areaPreventivo = new AreaPreventivo();
		this.rigaAutomatica = Boolean.FALSE;
	}

	/**
	 * @return nuova istanza di una riga magazzino
	 */
	protected abstract RigaPreventivoDTO creaIstanzaRigaPreventivoDTO();

	@Override
	public IRigaDTO creaRigaDTO() {
		return creaRigaPreventivoDTO();
	}

	/**
	 * @return riga ordine DTO
	 */
	public RigaPreventivoDTO creaRigaPreventivoDTO() {
		RigaPreventivoDTO riga = creaIstanzaRigaPreventivoDTO();
		PanjeaEJBUtil.copyProperties(riga, this);
		return riga;
	}

	/**
	 * @return the areaOrdine
	 */
	public AreaPreventivo getAreaPreventivo() {
		return areaPreventivo;
	}

	/**
	 * @return the chiave
	 */
	public String getChiave() {
		return chiave;
	}

	/**
	 * Metodo che restituisce la stringa che rappresenta la rigaMagazzino riportando descrizione,attributi e note.
	 * 
	 * @param stampaAttributi
	 *            indica se stampare gli attributi
	 * @param stampaNote
	 *            indica se stampare le note
	 * @return descrizione delle riga
	 */
	public final String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote) {
		return getDescrizioneRiga(stampaAttributi, stampaNote, null);
	}

	/**
	 * Metodo che restituisce la stringa che rappresenta la rigaMagazzino riportando descrizione,attributi e note.
	 * 
	 * @param stampaAttributi
	 *            indica se stampare gli attributi della riga
	 * @param stampaNote
	 *            indica se stampare le note
	 * @param lingua
	 *            lingua da utilizzare
	 * @return descrizione della riga
	 */
	public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua) {
		String linguaEntita = (areaPreventivo.getDocumento().getSedeEntita() != null) ? areaPreventivo.getDocumento()
				.getSedeEntita().getLingua() : null;

		if (linguaEntita != null && linguaEntita.equals(lingua)) {
			return getNoteLinguaRiga();
		}

		return getNoteRiga();
	}

	/**
	 * @return the livello
	 */
	@Override
	public int getLivello() {
		return livello;
	}

	/**
	 * @return the noteLinguaRiga
	 */
	public String getNoteLinguaRiga() {
		return noteLinguaRiga;
	}

	/**
	 * @return the noteRiga
	 */
	public String getNoteRiga() {
		return noteRiga;
	}

	/**
	 * @return Returns the numeroRiga.
	 */
	@Override
	public int getNumeroRiga() {
		return numeroRiga;
	}

	/**
	 * @return the ordinamento
	 */
	@Override
	public double getOrdinamento() {
		return ordinamento;
	}

	/**
	 * 
	 * @return riga testata al quale è collegata la riga.
	 */
	public RigaTestata getRigaTestataCollegata() {
		return rigaTestataCollegata;
	}

	/**
	 * @return the noteSuDestinazione
	 */
	public boolean isNoteSuDestinazione() {
		return noteSuDestinazione;
	}

	/**
	 * @return the rigaAutomatica
	 */
	public boolean isRigaAutomatica() {
		return rigaAutomatica;
	}

	/**
	 * @param areaPreventivo
	 *            the areaPreventivo to set
	 */
	public void setAreaPreventivo(AreaPreventivo areaPreventivo) {
		this.areaPreventivo = areaPreventivo;
	}

	/**
	 * @param chiave
	 *            the chiave to set
	 */
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	/**
	 * @param livello
	 *            the livello to set
	 */
	public void setLivello(int livello) {
		this.livello = livello;
	}

	/**
	 * @param noteLinguaRiga
	 *            the noteLinguaRiga to set
	 */
	public void setNoteLinguaRiga(String noteLinguaRiga) {
		this.noteLinguaRiga = noteLinguaRiga;
	}

	/**
	 * @param noteRiga
	 *            the noteRiga to set
	 */
	public void setNoteRiga(String noteRiga) {
		if (noteRiga != null && noteRiga.length() > 4000) {
			noteRiga = noteRiga.substring(0, 4000);
		}
		this.noteRiga = noteRiga;
	}

	/**
	 * @param noteSuDestinazione
	 *            the noteSuDestinazione to set
	 */
	public void setNoteSuDestinazione(boolean noteSuDestinazione) {
		this.noteSuDestinazione = noteSuDestinazione;
	}

	/**
	 * @param numeroRiga
	 *            The numeroRiga to set.
	 */
	public void setNumeroRiga(int numeroRiga) {
		this.numeroRiga = numeroRiga;
	}

	/**
	 * @param ordinamento
	 *            the ordinamento to set
	 */
	public void setOrdinamento(double ordinamento) {
		this.ordinamento = ordinamento;
	}

	/**
	 * @param rigaAutomatica
	 *            the rigaAutomatica to set
	 */
	public void setRigaAutomatica(boolean rigaAutomatica) {
		this.rigaAutomatica = rigaAutomatica;
	}

	/**
	 * 
	 * @param rigaTestataCollegata
	 *            setta la riga ordine (nello stesso ordine) alla quale fa riferimento questa riga.
	 */
	public void setRigaTestataCollegata(RigaTestata rigaTestataCollegata) {
		this.rigaTestataCollegata = rigaTestataCollegata;
	}

	/**
	 * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
	 * 
	 * @return a <code>String</code> come risultato di questo oggetto
	 */
	@Override
	public String toString() {

		StringBuffer retValue = new StringBuffer();

		retValue.append("RigaTestata[ ").append(super.toString()).append(" numeroRiga = ").append(this.numeroRiga)
				.append(" ordinamento = ").append(this.ordinamento).append(" ]");

		return retValue.toString();
	}
}
