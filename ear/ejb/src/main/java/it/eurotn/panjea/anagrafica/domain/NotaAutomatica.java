package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.classedocumento.ClasseTipoDocumentoFactory;
import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "anag_note_automatiche_documenti")
public class NotaAutomatica extends EntityBase {

	private static final long serialVersionUID = 2910250472371513776L;

	@Lob
	private String nota;

	@ManyToOne(optional = true)
	private EntitaLite entita;

	@ManyToOne(optional = true)
	private SedeEntita sedeEntita;

	private Date dataInizio;

	private Date dataFine;

	private boolean ripetiAnnualmente;

	@Column(length = 100)
	private String classeTipoDocumento;

	@Transient
	private IClasseTipoDocumento classeTipoDocumentoInstance = null;

	@ManyToOne(optional = true)
	private TipoDocumento tipoDocumento;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	private Set<ArticoloLite> articoli;

	{
		articoli = new HashSet<ArticoloLite>();
	}

	/**
	 * Costruttore.
	 */
	public NotaAutomatica() {
		super();
	}

	/**
	 * @return the articoli
	 */
	public Set<ArticoloLite> getArticoli() {
		return articoli;
	}

	/**
	 * @return the dataFine
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
	 * @return the dataFine
	 */
	public Date getDataFine() {
		return dataFine;
	}

	/**
	 * @return the dataInizio
	 */
	public Date getDataInizio() {
		return dataInizio;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the nota
	 */
	public String getNota() {
		return nota;
	}

	/**
	 * Sostituisce a tutti i segnaposto nel template delle note, le nota anagrafiche, restituendo la nota elaborata.
	 * 
	 * @param noteAnagrafiche
	 *            note anagrafiche
	 * @return nota
	 */
	public String getNotaElaborata(List<NotaAnagrafica> noteAnagrafiche) {
		// NPE mail all'interno del ciclo for alla riga 66
		String tmp = getNota();
		if (tmp == null) {
			tmp = "";
		}
		for (NotaAnagrafica notaAnagrafica : noteAnagrafiche) {
			if (notaAnagrafica != null) {
				tmp = tmp.replace("$" + notaAnagrafica.getCodice() + "$", notaAnagrafica.getDescrizione());
			}
		}
		return tmp;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @return the ripetiAnnualmente
	 */
	public boolean isRipetiAnnualmente() {
		return ripetiAnnualmente;
	}

	/**
	 * @param articoli
	 *            the articoli to set
	 */
	public void setArticoli(Set<ArticoloLite> articoli) {
		this.articoli = articoli;
	}

	/**
	 * @param classeTipoDocumento
	 *            The classeTipoDocumento to set.
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
	 * @param codiceEntita
	 *            The codiceEntita to set.
	 */
	public void setCodiceEntita(Integer codiceEntita) {
		if (getEntita() != null) {
			getEntita().setCodice(codiceEntita);
		}
	}

	/**
	 * @param codiceTipoDocumento
	 *            The codiceTipoDocumento to set.
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		if (tipoDocumento != null) {
			this.tipoDocumento.setCodice(codiceTipoDocumento);
		}
	}

	/**
	 * @param dataFine
	 *            the dataFine to set
	 */
	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	/**
	 * @param dataInizio
	 *            the dataInizio to set
	 */
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
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
	 * @param descrizioneTipoDocumento
	 *            The descrizioneTipoDocumento to set.
	 */
	public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
		if (tipoDocumento != null) {
			this.getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
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
	 * @param idTipoDocumento
	 *            The idTipoDocumento to set.
	 */
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		if (idTipoDocumento == null) {
			this.tipoDocumento = null;
		} else {
			this.tipoDocumento = new TipoDocumento();
			this.tipoDocumento.setId(idTipoDocumento);
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
	 * @param nota
	 *            the nota to set
	 */
	public void setNota(String nota) {
		this.nota = nota;
	}

	/**
	 * @param ripetiAnnualmente
	 *            the ripetiAnnualmente to set
	 */
	public void setRipetiAnnualmente(boolean ripetiAnnualmente) {
		this.ripetiAnnualmente = ripetiAnnualmente;
	}

	/**
	 * @param sedeEntita
	 *            the sedeEntita to set
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @param tipoEntita
	 *            The tipoEntita to set.
	 */
	public void setTipoEntita(String tipoEntita) {
		if (tipoEntita == null) {
			setEntita(null);
		} else if ("F".equals(tipoEntita)) {
			setEntita(new FornitoreLite());
		} else if ("C".equals(tipoEntita)) {
			setEntita(new ClienteLite());
		} else if ("V".equals(tipoEntita)) {
			setEntita(new VettoreLite());
		} else {
			setEntita(new EntitaLite());
		}
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

	/**
	 * @param versionTipoDocumento
	 *            The versionTipoDocumento to set.
	 */
	public void setVersionTipoDocumento(Integer versionTipoDocumento) {
		if (tipoDocumento != null) {
			this.tipoDocumento.setVersion(versionTipoDocumento);
		}
	}
}
