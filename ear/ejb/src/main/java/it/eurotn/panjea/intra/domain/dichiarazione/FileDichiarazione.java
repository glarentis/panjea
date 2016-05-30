package it.eurotn.panjea.intra.domain.dichiarazione;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * 
 * Contiene i dati del file generato da presentare in dogana.
 * 
 * @author giangi
 * @version 1.0, 10/apr/2013
 * 
 */
@Entity
@Audited
@Table(name = "intr_file_intra")
@NamedQueries({ @NamedQuery(name = "FileDichiarazione.caricaAll", query = "select distinct f from FileDichiarazione f join fetch f.dichiarazioni order by f.timeStamp") })
public class FileDichiarazione extends EntityBase {
	public enum StatoFileDichiarazione {
		DA_SPEDIRE, IN_ATTESA_VERIFICA, IN_ELABORAZIONE, ELABORAZIONE_OK, ELABORAZIONE_KO
	}

	private static final long serialVersionUID = 2073062986943619050L;
	@Lob
	private byte[] content;
	private String nome;
	@OneToMany(mappedBy = "fileDichiarazione")
	private Set<DichiarazioneIntra> dichiarazioni;

	private StatoFileDichiarazione stato;

	{
		stato = StatoFileDichiarazione.DA_SPEDIRE;
	}

	/**
	 * aggiunge una dichiarazione al file.
	 * 
	 * @param dichiarazioneIntra
	 *            dic. da aggiungere
	 */
	public void addDichiarazione(DichiarazioneIntra dichiarazioneIntra) {
		if (dichiarazioni == null) {
			dichiarazioni = new HashSet<DichiarazioneIntra>();
		}
		dichiarazioneIntra.setFileDichiarazione(this);
		dichiarazioni.add(dichiarazioneIntra);
	}

	/**
	 * @return Returns the content.
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * 
	 * @return data di creazione del file
	 */
	public Date getDataCreazione() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(getTimeStamp());
		return calendar.getTime();
	}

	/**
	 * @return Returns the dichiarazioni.
	 */
	public Set<DichiarazioneIntra> getDichiarazioni() {
		return dichiarazioni;
	}

	/**
	 * 
	 * @return descrizione delle dichiarazioni contenute
	 */
	public String getDichiarazioniDescrizione() {
		StringBuilder sb = new StringBuilder();
		for (DichiarazioneIntra dichiarazione : dichiarazioni) {
			sb.append(dichiarazione.getDescrizioneDichiarazione());
			sb.append(" - ");
		}
		return sb.toString();
	}

	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return Returns the stato.
	 */
	public StatoFileDichiarazione getStato() {
		return stato;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @param dichiarazioni
	 *            The dichiarazioni to set.
	 */
	public void setDichiarazioni(Set<DichiarazioneIntra> dichiarazioni) {
		this.dichiarazioni = dichiarazioni;
	}

	/**
	 * @param nome
	 *            The nome to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param stato
	 *            The stato to set.
	 */
	public void setStato(StatoFileDichiarazione stato) {
		this.stato = stato;
	};

}
