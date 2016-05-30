/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Index;
import org.hibernate.envers.NotAudited;

/**
 * @author Leonardo
 *
 */
@Entity
@Table(name = "bamm_simulazione")
@NamedQueries({
	@NamedQuery(name = "Simulazione.caricaAll", query = " from Simulazione s where s.codiceAzienda = :paramCodiceAzienda "),
	@NamedQuery(name = "Simulazione.caricaSimulazioniCollegate", query = " from Simulazione s where s.simulazioneRiferimento.id = :paramIdSimulazioneRiferimento and s.codiceAzienda = :paramCodiceAzienda "),
	@NamedQuery(name = "Simulazione.caricaAnnoUltimaSimulazioneConsolidata", query = " select max(s.data) as maxData from Simulazione s where (s.consolidata = true) and (s.codiceAzienda = :paramCodiceAzienda) "),
	@NamedQuery(name = "Simulazione.caricaByAnno", query = " from Simulazione s where s.codiceAzienda = :paramCodiceAzienda and s.anno = :paramAnno "),
	@NamedQuery(name = "Simulazione.caricaAnnoUltimaSimulazione", query = " select max(s.data) as maxData from Simulazione s where (s.consolidata = false) and (s.codiceAzienda = :paramCodiceAzienda) ") })
public class Simulazione extends EntityBase {

	private static final long serialVersionUID = 2635684906167645645L;

	public static final String REF = "Simulazione";
	public static final String PROP_DATA = "data";
	public static final String PROP_USER_UPDATE = "userUpdate";
	public static final String PROP_DATE_INSERT = "dateInsert";
	public static final String PROP_USER_INSERT = "userInsert";
	public static final String PROP_DATE_UPDATE = "dateUpdate";
	public static final String PROP_ID = "id";
	public static final String PROP_DESCRIZIONE = "descrizione";
	public static final String PROP_CODICE_AZIENDA = "codiceAzienda";
	public static final String PROP_ANNO = "anno";
	public static final String PROP_CONSOLIDATA = "consolidata";

	@Column(length = 10, nullable = false)
	@Index(name = "index_codiceAzienda")
	private String codiceAzienda = null;

	@Column(nullable = false)
	private String descrizione = null;

	@Temporal(TemporalType.DATE)
	private Date data = null;

	private boolean consolidata = false;

	@ManyToOne
	private Simulazione simulazioneRiferimento;

	@Formula("YEAR(data)")
	private Integer anno;

	@Column
	private boolean simulazioneInCorso;

	@Transient
	private List<PoliticaCalcolo> politicheCalcolo;

	@Transient
	private boolean allowConsolida;

	@ManyToOne
	private AreaContabileLite areaContabile;

	@Formula("( select if(count(ac.id)>0,0,1) from cont_area_contabile ac left join bamm_simulazione sim on sim.areaContabile_id = ac.id left join bamm_politica_calcolo p on p.areaContabile_id = ac.id where sim.id = id or p.simulazione_id = id)")
	@NotAudited
	private boolean areeContabiliDaCreare;

	@Formula("( select if(count(ac.id)>0,1,0) from cont_area_contabile ac left join bamm_simulazione sim on sim.areaContabile_id = ac.id left join bamm_politica_calcolo p on p.areaContabile_id = ac.id where sim.id = id or p.simulazione_id = id and ac.statoAreaContabile > 1)")
	@NotAudited
	private boolean areeContabiliDaConfermare;

	/**
	 * Costruttore di default.
	 */
	public Simulazione() {
		initialize();
	}

	/**
	 * Aggiunge una politica di calcolo alla simulazione.
	 *
	 * @param politicaCalcolo
	 *            politica da aggiungere
	 */
	public void addTopoliticheCalcolo(PoliticaCalcolo politicaCalcolo) {
		if (getPoliticheCalcolo() == null) {
			setPoliticheCalcolo(new ArrayList<PoliticaCalcolo>());
		}
		getPoliticheCalcolo().add(politicaCalcolo);
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return the areaContabile
	 */
	public AreaContabileLite getAreaContabile() {
		return areaContabile;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the politicheCalcolo
	 */
	public List<PoliticaCalcolo> getPoliticheCalcolo() {
		return politicheCalcolo;
	}

	/**
	 * @return the simulazioneRiferimento
	 */
	public Simulazione getSimulazioneRiferimento() {
		return simulazioneRiferimento;
	}

	/**
	 * Inizializza i valori delle variabili dell'istanza.
	 */
	private void initialize() {
		politicheCalcolo = new ArrayList<PoliticaCalcolo>();
		simulazioneInCorso = false;
	}

	/**
	 * @return the allowConsolida
	 */
	public boolean isAllowConsolida() {
		return allowConsolida;
	}

	/**
	 * @return the areeContabiliDaConfermare
	 */
	public boolean isAreeContabiliDaConfermare() {
		return areeContabiliDaConfermare;
	}

	/**
	 * @return the areeContabiliDaCreare
	 */
	public boolean isAreeContabiliDaCreare() {
		return areeContabiliDaCreare;
	}

	/**
	 * @return the consolidata
	 */
	public boolean isConsolidata() {
		return consolidata;
	}

	/**
	 * @return Returns the simulazioneInCorso.
	 */
	public boolean isSimulazioneInCorso() {
		return simulazioneInCorso;
	}

	/**
	 * @param allowConsolida
	 *            the allowConsolida to set
	 */
	public void setAllowConsolida(boolean allowConsolida) {
		this.allowConsolida = allowConsolida;
	}

	/**
	 * @param anno
	 *            the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param areaContabile
	 *            the areaContabile to set
	 */
	public void setAreaContabile(AreaContabileLite areaContabile) {
		this.areaContabile = areaContabile;
	}

	/**
	 * @param areeContabiliDaConfermare
	 *            the areeContabiliDaConfermare to set
	 */
	public void setAreeContabiliDaConfermare(boolean areeContabiliDaConfermare) {
		this.areeContabiliDaConfermare = areeContabiliDaConfermare;
	}

	/**
	 * @param areeContabiliDaCreare
	 *            the areeContabiliDaCreare to set
	 */
	public void setAreeContabiliDaCreare(boolean areeContabiliDaCreare) {
		this.areeContabiliDaCreare = areeContabiliDaCreare;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param consolidata
	 *            the consolidata to set
	 */
	public void setConsolidata(boolean consolidata) {
		this.consolidata = consolidata;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param politicheCalcolo
	 *            the politicheCalcolo to set
	 */
	public void setPoliticheCalcolo(List<PoliticaCalcolo> politicheCalcolo) {
		this.politicheCalcolo = politicheCalcolo;
	}

	/**
	 * @param simulazioneInCorso
	 *            The simulazioneInCorso to set.
	 */
	public void setSimulazioneInCorso(boolean simulazioneInCorso) {
		this.simulazioneInCorso = simulazioneInCorso;
	}

	/**
	 * @param simulazioneRiferimento
	 *            the simulazioneRiferimento to set
	 */
	public void setSimulazioneRiferimento(Simulazione simulazioneRiferimento) {
		this.simulazioneRiferimento = simulazioneRiferimento;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Simulazione[");
		buffer.append("codiceAzienda = ").append(codiceAzienda);
		buffer.append(" consolidata = ").append(consolidata);
		buffer.append(" data = ").append(data);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" simulazioneRiferimento = ").append(
				simulazioneRiferimento != null ? simulazioneRiferimento.getId() : null);
		buffer.append(" simulazioneInCorso = ").append(simulazioneInCorso);
		buffer.append("]");
		return buffer.toString();
	}

}
