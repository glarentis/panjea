package it.eurotn.panjea.partite.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.contabilita.manager.interfaces.IFormula;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Entity
@Audited
@Table(name = "part_struttura_partita")
@NamedQueries({ @NamedQuery(name = "StrutturaPartita.caricaAll", query = "select a from StrutturaPartita a where a.codiceAzienda = :paramCodiceAzienda") })
public class StrutturaPartita extends EntityBase implements IFormula {

	/**
	 * Classe enum che stabilisce i tipi di strategie per la generazione della data scadenza della rata Il tipo dovra'
	 * essere usato come input della factory per ottenere il giusto calcolo.<br>
	 * DATA_DOC_GG_SOLARI = dalla data del doc si aggiungono i gg di intervallo esistenti sulla riga struttura
	 * (Eventualmente mod da gg fisso)<br>
	 * DATA_DOC_GG_COMM = dalla data documento si aggiugono i gg di intervallo commerciali(30)<br>
	 * DATA_DOC_GG_COMM_FINE_MESE = dalla data documento si aggiugono i gg di intervallo commerciali(30) con data
	 * iniziali a fine mese<br>
	 * DATA_DOC_TABELLA date scadenza generate da una tabella aggiuntiva sulla struttura partita<br>
	 * SENZA_DATA genera rate senza data scadenza (es.versamento da ritenuta d'acconto)
	 */
	public enum TipoStrategiaDataScadenza {
		DATA_DOC_GG_SOLARI, DATA_DOC_GG_COMM, DATA_DOC_GG_COMM_FINE_MESE
	}

	private static final long serialVersionUID = 3847123402823835703L;

	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	@Column(length = 60)
	private String descrizione;

	@Column
	private Integer giornoFisso;

	@Enumerated
	private TipoStrategiaDataScadenza tipoStrategiaDataScadenza;

	@ManyToOne
	private CategoriaRata categoriaRata;

	@Enumerated
	private TipoPagamento tipoPagamento;

	@Column
	private Integer ggPostScadenza;

	/**
	 * Indica se dovraà essere generata la stampa per la richiesta di versamento.
	 * 
	 */
	private boolean stampaRV;

	@OneToMany(mappedBy = "strutturaPartita", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<RigaStrutturaPartite> righeStrutturaPartita;

	@OneToOne(cascade = CascadeType.ALL)
	private StrategiaCalcoloPartita primaStrategiaCalcoloPartita;

	@OneToOne(cascade = CascadeType.ALL)
	private StrategiaCalcoloPartita secondaStrategiaCalcoloPartita;;

	/**
	 * Costruttore.
	 */
	public StrutturaPartita() {
		categoriaRata = new CategoriaRata();
	};

	/**
	 * @return the categoriaRata
	 */
	public CategoriaRata getCategoriaRata() {
		return categoriaRata;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	@Override
	public List<String> getFormule() {
		ArrayList<String> formule = new ArrayList<String>();
		if (getPrimaStrategiaCalcoloPartita() instanceof StrategiaCalcoloPartitaFormula) {
			formule.add(((StrategiaCalcoloPartitaFormula) getPrimaStrategiaCalcoloPartita()).getFormula());
		}
		if (getSecondaStrategiaCalcoloPartita() instanceof StrategiaCalcoloPartitaFormula) {
			formule.add(((StrategiaCalcoloPartitaFormula) getSecondaStrategiaCalcoloPartita()).getFormula());
		}
		return formule;
	}

	/**
	 * @return the ggPostScadenza
	 */
	public Integer getGgPostScadenza() {
		return ggPostScadenza;
	}

	/**
	 * @return the giornoFisso
	 */
	public Integer getGiornoFisso() {
		return giornoFisso;
	}

	/**
	 * Metodo di utilita per ottenere la formula della prima strategia
	 * 
	 * @return null / string con formula
	 */
	public String getPrimaFormula() {
		if (getPrimaStrategiaCalcoloPartita() instanceof StrategiaCalcoloPartitaFormula) {
			return ((StrategiaCalcoloPartitaFormula) getPrimaStrategiaCalcoloPartita()).getFormula();
		}
		return null;
	}

	/**
	 * @return the primaStrategiaCalcoloPartita
	 */
	public StrategiaCalcoloPartita getPrimaStrategiaCalcoloPartita() {
		return primaStrategiaCalcoloPartita;
	}

	/**
	 * @return the righeStrutturaPartita
	 */
	public List<RigaStrutturaPartite> getRigheStrutturaPartita() {
		return righeStrutturaPartita;
	}

	/**
	 * Metodo di utilita per ottenere la formula della seconda strategia
	 * 
	 * @return null / string con formula
	 */
	public String getSecondaFormula() {
		if (getSecondaStrategiaCalcoloPartita() instanceof StrategiaCalcoloPartitaFormula) {
			return ((StrategiaCalcoloPartitaFormula) getSecondaStrategiaCalcoloPartita()).getFormula();
		}
		return null;
	}

	/**
	 * @return the secondaStrategiaCalcoloPartita
	 */
	public StrategiaCalcoloPartita getSecondaStrategiaCalcoloPartita() {
		return secondaStrategiaCalcoloPartita;
	}

	/**
	 * @return strutturaPartitaLite
	 */
	public StrutturaPartitaLite getStrutturaPartitaLite() {
		StrutturaPartitaLite strutturaPartitaLite = new StrutturaPartitaLite();
		strutturaPartitaLite.setCategoriaRata(getCategoriaRata());
		strutturaPartitaLite.setCodiceAzienda(getCodiceAzienda());
		strutturaPartitaLite.setDescrizione(getDescrizione());
		strutturaPartitaLite.setGgPostScadenza(getGgPostScadenza());
		strutturaPartitaLite.setGiornoFisso(getGiornoFisso());
		strutturaPartitaLite.setId(getId());
		strutturaPartitaLite.setTipoPagamento(getTipoPagamento());
		strutturaPartitaLite.setTipoStrategiaDataScadenza(getTipoStrategiaDataScadenza());
		strutturaPartitaLite.setVersion(getVersion());
		return strutturaPartitaLite;
	}

	/**
	 * @return the tipoPagamento
	 */
	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	/**
	 * @return the tipoStrategiaDataScadenza
	 */
	public TipoStrategiaDataScadenza getTipoStrategiaDataScadenza() {
		return tipoStrategiaDataScadenza;
	}

	/**
	 * @return the stampaRV
	 */
	public boolean isStampaRV() {
		return stampaRV;
	}

	/**
	 * @param categoriaRata
	 *            the categoriaRata to set
	 */
	public void setCategoriaRata(CategoriaRata categoriaRata) {
		this.categoriaRata = categoriaRata;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param ggPostScadenza
	 *            the ggPostScadenza to set
	 */
	public void setGgPostScadenza(Integer ggPostScadenza) {
		this.ggPostScadenza = ggPostScadenza;
	}

	/**
	 * @param giornoFisso
	 *            the giornoFisso to set
	 */
	public void setGiornoFisso(Integer giornoFisso) {
		this.giornoFisso = giornoFisso;
	}

	/**
	 * @param primaStrategiaCalcoloPartita
	 *            the primaStrategiaCalcoloPartita to set
	 */
	public void setPrimaStrategiaCalcoloPartita(StrategiaCalcoloPartita primaStrategiaCalcoloPartita) {
		this.primaStrategiaCalcoloPartita = primaStrategiaCalcoloPartita;
	}

	/**
	 * @param righeStrutturaPartita
	 *            the righeStrutturaPartita to set
	 */
	public void setRigheStrutturaPartita(List<RigaStrutturaPartite> righeStrutturaPartita) {
		this.righeStrutturaPartita = righeStrutturaPartita;
	}

	/**
	 * @param secondaStrategiaCalcoloPartita
	 *            the secondaStrategiaCalcoloPartita to set
	 */
	public void setSecondaStrategiaCalcoloPartita(StrategiaCalcoloPartita secondaStrategiaCalcoloPartita) {
		this.secondaStrategiaCalcoloPartita = secondaStrategiaCalcoloPartita;
	}

	/**
	 * @param stampaRV
	 *            the stampaRV to set
	 */
	public void setStampaRV(boolean stampaRV) {
		this.stampaRV = stampaRV;
	}

	/**
	 * @param tipoPagamento
	 *            the tipoPagamento to set
	 */
	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	/**
	 * @param tipoStrategiaDataScadenza
	 *            the tipoStrategiaDataScadenza to set
	 */
	public void setTipoStrategiaDataScadenza(TipoStrategiaDataScadenza tipoStrategiaDataScadenza) {
		this.tipoStrategiaDataScadenza = tipoStrategiaDataScadenza;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("StrutturaPartita[");
		buffer.append("codiceAzienda = ").append(codiceAzienda);
		buffer.append("categoriaRata = ").append(categoriaRata);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append(" ggPostScadenza = ").append(ggPostScadenza);
		buffer.append(" giornoFisso = ").append(giornoFisso);
		buffer.append(" primaStrategiaCalcoloPartita = ").append(primaStrategiaCalcoloPartita);
		buffer.append(" secondaStrategiaCalcoloPartita = ").append(secondaStrategiaCalcoloPartita);
		buffer.append(" tipoPagamento = ").append(tipoPagamento);
		buffer.append("]");
		return buffer.toString();
	}

}
