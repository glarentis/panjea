package it.eurotn.panjea.ordini.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdineNote;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.RiferimentiOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.RiferimentiOrdine.ModalitaRicezione;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.rate.domain.AreaRate;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * Mappa gli ordini che sono stati importati da una procedura esterna.<br/>
 * Devono essere confermati per farli diventare normali ordini di Panjea.
 *
 * @author giangi
 * @version 1.0, 06/dic/2010
 *
 */
@Entity
@Table(name = "ordi_ordini_importati", uniqueConstraints = @UniqueConstraint(columnNames = { "numero", "codiceAgente",
"data" }))
@NamedQueries({
	@NamedQuery(name = "OrdineImportato.caricaAll", query = "select o from OrdineImportato o"),
	@NamedQuery(name = "OrdineImportato.cancellaByCodiceAgente", query = "delete from OrdineImportato o where o.codiceAgente=:codiceAgente"),
	@NamedQuery(name = "OrdineImportato.cancellaAll", query = "delete from OrdineImportato") })
public class OrdineImportato extends EntityBase {
	public enum EProvenienza {
		TUTTI, ATON
	}

	private static final long serialVersionUID = -4293431534619076783L;

	@ManyToOne
	private AgenteLite agente;

	private Boolean sedeEredita;

	/**
	 * Se true blocca l'ordine alla sua creazione.
	 */
	@Transient
	private boolean bloccaOrdine;

	@Column(length = 12)
	private String codiceAgente;

	@ManyToOne
	private EntitaLite entita;

	@Column(length = 12)
	private String codiceEntita;

	@ManyToOne
	private SedeEntita sedeEntita;

	@ManyToOne
	private Listino listino;
	@ManyToOne
	private Listino listinoAlternativo;
	@ManyToOne
	private TipoDocumento tipoDocumento;
	@Column(length = 12)
	private String codiceTipoDocumento;
	private Integer idSedeImportata;
	private String numero;

	private Date data;

	@ManyToOne
	private CodicePagamento pagamento;

	@ManyToOne
	private CodicePagamento pagamentoDeterminato;

	@Column(length = 12)
	private String codicePagamento;

	private boolean sedeBloccata;

	@Column(length = 300)
	private String noteBlocco;

	@Transient
	private DepositoLite deposito;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ordine", fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@javax.persistence.MapKey(name = "numeroRiga")
	@OrderBy("numeroRiga")
	private Map<Integer, RigaOrdineImportata> righe;

	@Column(length = 255)
	private String note;

	private EProvenienza provenienza;

	@Column(precision = 19, scale = 6, nullable = true)
	private BigDecimal importoFido;

	@Column(precision = 19, scale = 6, nullable = true)
	private BigDecimal importoRateAperte;

	@Transient
	private ModalitaRicezione modalitaRicezione;

	@Transient
	private TipoAreaOrdine tipoAreaOrdine;

	/**
	 * Se impostata è da considerare questa invece che crearne una con i dati di this.
	 */
	@Transient
	private AreaOrdine areaOrdine;

	/**
	 * Costruttore.
	 */
	public OrdineImportato() {
		righe = new HashMap<Integer, RigaOrdineImportata>();
		bloccaOrdine = false;
		modalitaRicezione = ModalitaRicezione.ATON;
	}

	/**
	 * @param notaOrdine
	 *            The note to set.
	 */
	public void addNotaOrdine(NotaOrdineImportata notaOrdine) {
		if (notaOrdine.isNotaTestata()) {
			aggiungiNotaTestata(notaOrdine);
		} else {
			aggiungiNotaRiga(notaOrdine);
		}
	}

	/**
	 * Aggiunge una riga all'ordine.
	 *
	 * @param riga
	 *            riga da aggiungere
	 */
	public void addRiga(RigaOrdineImportata riga) {
		riga.setOrdine(this);
		righe.put(riga.getNumeroRiga(), riga);
	}

	/**
	 * @param notaOrdine
	 *            nota ordine da aggiungere alla riga
	 */
	private void aggiungiNotaRiga(NotaOrdineImportata notaOrdine) {
		try {
			RigaOrdineImportata riga = righe.get(new Integer(notaOrdine.getNumeroRiga()));
			riga.addNota(notaOrdine);
		} catch (Exception e) {
			System.out.println("--> Errore " + notaOrdine);
		}
	}

	/**
	 * @param notaOrdine
	 *            nota ordine da aggiungere alla testata
	 */
	private void aggiungiNotaTestata(NotaOrdineImportata notaOrdine) {
		if (note != null) {
			note += " ";
		} else {
			note = "";
		}
		this.note += notaOrdine.getNota();
	}

	/**
	 * Crea un'arae Ordine dall'ordine importato.
	 *
	 * @param codiceValuta
	 *            codcie valuta del documento
	 * @param anno
	 *            anno del documento
	 * @param sedeAreaMagazzino
	 *            sedeAreaMagazzino dell'entità sul documento
	 * @return nuova areaOrdine
	 */
	public AreaOrdine creaAreaOrdine(String codiceValuta, Integer anno, SedeAreaMagazzinoDTO sedeAreaMagazzino) {
		Importo importo = new Importo(codiceValuta, BigDecimal.ONE);
		importo.calcolaImportoValutaAzienda(2);

		Documento documento = new Documento();
		documento.setDataDocumento(getData());
		if (!tipoAreaOrdine.isOrdineProduzione()) {
			documento.setEntita(getEntita());
			documento.setSedeEntita(getSedeEntita());
		}
		documento.setTipoDocumento(getTipoDocumento());
		documento.setTotale(importo);

		// Creo l'area ordine
		AreaOrdine nuovaAreaOrdine = new AreaOrdine();

		nuovaAreaOrdine.setDocumento(documento);
		nuovaAreaOrdine.setTipoAreaOrdine(tipoAreaOrdine);
		nuovaAreaOrdine.setAgente(getAgente());
		nuovaAreaOrdine.setDataRegistrazione(getData());
		nuovaAreaOrdine.setDataConsegna(getData());
		nuovaAreaOrdine.setAnnoMovimento(anno);
		nuovaAreaOrdine.setDepositoOrigine(deposito);
		nuovaAreaOrdine.setNoteImportazione(getNoteImportazione());

		nuovaAreaOrdine.setRiferimentiOrdine(new RiferimentiOrdine());
		nuovaAreaOrdine.getRiferimentiOrdine().setNumeroOrdine(StringUtils.stripStart(getNumero(), "0"));
		nuovaAreaOrdine.getRiferimentiOrdine().setDataOrdine(getData());
		nuovaAreaOrdine.getRiferimentiOrdine().setModalitaRicezione(modalitaRicezione);
		if (getNote() != null) {
			AreaOrdineNote areaOrdineNote = new AreaOrdineNote();
			areaOrdineNote.setNoteTestata(getNote());
			nuovaAreaOrdine.setAreaOrdineNote(areaOrdineNote);
		}

		if (sedeAreaMagazzino != null) {
			nuovaAreaOrdine.setListino(sedeAreaMagazzino.getListino());
			nuovaAreaOrdine.setListinoAlternativo(sedeAreaMagazzino.getListinoAlternativo());
			nuovaAreaOrdine.setVettore(sedeAreaMagazzino.getVettore());
			nuovaAreaOrdine.setCausaleTrasporto(sedeAreaMagazzino.getCausaleTrasporto());
			nuovaAreaOrdine.setTrasportoCura(sedeAreaMagazzino.getTrasportoCura());
			nuovaAreaOrdine.setTipoPorto(sedeAreaMagazzino.getTipoPorto());
			nuovaAreaOrdine.setAddebitoSpeseIncasso(sedeAreaMagazzino.isCalcoloSpese());
			nuovaAreaOrdine.setRaggruppamentoBolle(sedeAreaMagazzino.isRaggruppamentoBolle());
			nuovaAreaOrdine.setTipologiaCodiceIvaAlternativo(sedeAreaMagazzino.getTipologiaCodiceIvaAlternativo());
			nuovaAreaOrdine.setCodiceIvaAlternativo(sedeAreaMagazzino.getCodiceIvaAlternativo());
			nuovaAreaOrdine.setSedeVettore(sedeAreaMagazzino.getSedeVettore());
		}
		return nuovaAreaOrdine;
	}

	/**
	 *
	 * @param areaOrdineParam
	 *            areaOrdine per l'area rate
	 * @param tap
	 *            tipoAreaPArtite
	 * @return areaRate con i dati impostati
	 */
	public AreaRate creaAreaRate(AreaOrdine areaOrdineParam, TipoAreaPartita tap) {
		AreaRate areaRate = new AreaRate();
		areaRate.setTipoAreaPartita(tap);
		areaRate.setDocumento(areaOrdineParam.getDocumento());
		areaRate.setCodicePagamento(pagamento);
		if (pagamento != null) {
			areaRate.setSpeseIncasso(pagamento.getImportoSpese());
		}
		return areaRate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}
		OrdineImportato other = (OrdineImportato) obj;
		if (codiceAgente == null) {
			if (other.codiceAgente != null) {
				return false;
			}
		} else if (!codiceAgente.equals(other.codiceAgente)) {
			return false;
		}
		if (codiceEntita == null) {
			if (other.codiceEntita != null) {
				return false;
			}
		} else if (!codiceEntita.equals(other.codiceEntita)) {
			return false;
		}
		if (codiceTipoDocumento == null) {
			if (other.codiceTipoDocumento != null) {
				return false;
			}
		} else if (!codiceTipoDocumento.equals(other.codiceTipoDocumento)) {
			return false;
		}
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		if (!numero.equals(other.numero)) {
			return false;
		}
		if (sedeEntita == null) {
			if (other.sedeEntita != null) {
				return false;
			}
		} else if (!sedeEntita.equals(other.sedeEntita)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the agente.
	 */
	public AgenteLite getAgente() {
		return agente;
	}

	/**
	 * @return the areaOrdine
	 */
	public AreaOrdine getAreaOrdine() {
		return areaOrdine;
	}

	/**
	 * @return Returns the codiceAgente.
	 */
	public String getCodiceAgente() {
		return codiceAgente;
	}

	/**
	 * @return Returns the codiceCliente.
	 */
	public String getCodiceEntita() {
		return codiceEntita;
	}

	/**
	 * @return Returns the codicePagamento.
	 */
	public String getCodicePagamento() {
		return codicePagamento;
	}

	/**
	 * @return Returns the codiceTipoDocumento.
	 */
	public String getCodiceTipoDocumento() {
		return codiceTipoDocumento;
	}

	/**
	 * @return Returns the data.
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @return Returns the deposito.
	 */
	public DepositoLite getDeposito() {
		return deposito;
	}

	/**
	 * @return Returns the entita.
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return Returns the idSedeImportata.
	 */
	public Integer getIdSedeImportata() {
		return idSedeImportata;
	}

	/**
	 * @return the importoFido
	 */
	public BigDecimal getImportoFido() {
		return importoFido;
	}

	/**
	 * @return Importo del fido residuo.
	 */
	public BigDecimal getImportoFidoResiduo() {
		BigDecimal importo = BigDecimal.ZERO;

		if (importoFido != null && importoRateAperte != null) {
			importo = importoFido.subtract(importoRateAperte);
		}

		return importo;
	}

	/**
	 * @return the importoRateAperte
	 */
	public BigDecimal getImportoRateAperte() {
		return importoRateAperte;
	}

	/**
	 * @return the listino
	 */
	public Listino getListino() {
		return listino;
	}

	/**
	 * @return the listinoAlternativo
	 */
	public Listino getListinoAlternativo() {
		return listinoAlternativo;
	}

	/**
	 * @return Returns the modalitaRicezione.
	 */
	public ModalitaRicezione getModalitaRicezione() {
		return modalitaRicezione;
	}

	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return Returns the noteBlocco.
	 */
	public String getNoteBlocco() {
		return noteBlocco;
	}

	/**
	 * @return note dell'importazione contenente i valori determinati rispetto a quelli definiti dall'agente.
	 */
	public String getNoteImportazione() {
		StringBuilder sb = new StringBuilder();
		if (entita != null && entita.getBloccoSede().isBlocco() && entita.getBloccoSede().getNoteBlocco() != null) {
			sb.append("<b>NOTE BLOCCO ENTITA': </b>");
			sb.append(sedeEntita.getEntita().getBloccoSede().getNoteBlocco());
			sb.append("<BR>");
		} else if (sedeEntita != null && sedeEntita.getBloccoSede().isBlocco()
				&& sedeEntita.getBloccoSede().getNoteBlocco() != null) {
			sb.append("<b>NOTE BLOCCO SEDE: </b>");
			sb.append(sedeEntita.getBloccoSede().getNoteBlocco());
			sb.append("<BR>");
		}

		if (!isPagamentoStandard()) {
			sb.append("<b>PAGAMENTO PREVISTO: </b>");
			sb.append(pagamentoDeterminato.getCodicePagamento() + "-" + pagamentoDeterminato.getDescrizione());
			sb.append("<BR>");
		}

		if (importoFido != null && getImportoFidoResiduo().compareTo(BigDecimal.ZERO) < 0) {
			DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
			sb.append("<b>FIDO: </b>");
			sb.append("Accordato: " + decimalFormat.format(importoFido));
			sb.append(" Utilizzato: " + decimalFormat.format(importoRateAperte));
			sb.append("<BR>");
		}
		return sb.toString();
	}

	/**
	 * @return Returns the numero.
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @return Returns the pagamento.
	 */
	public CodicePagamento getPagamento() {
		return pagamento;
	}

	/**
	 * @return Returns the pagamentoDeterminato.
	 */
	public CodicePagamento getPagamentoDeterminato() {
		return pagamentoDeterminato;
	}

	/**
	 * @return the provenienza
	 */
	public EProvenienza getProvenienza() {
		return provenienza;
	}

	/**
	 * @return righe dell'ordine
	 */
	public Map<Integer, RigaOrdineImportata> getRighe() {
		return Collections.unmodifiableMap(righe);
	}

	/**
	 * @return righe componente
	 */
	public Map<Integer, RigaOrdineImportata> getRigheComponente() {
		Map<Integer, RigaOrdineImportata> righeComponente = new HashMap<Integer, RigaOrdineImportata>();
		for (Entry<Integer, RigaOrdineImportata> entry : righe.entrySet()) {
			if (entry.getValue().getIdRigaDistinta() != null) {
				righeComponente.put(entry.getKey(), entry.getValue());
			}
		}
		return Collections.unmodifiableMap(righeComponente);
	}

	/**
	 * @return Returns the sedeEntita.
	 */
	public SedeEntita getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return Returns the sedeEredita.
	 */
	public Boolean getSedeEredita() {
		return sedeEredita;
	}

	/**
	 * @return Returns the tipoAreaOrdine.
	 */
	public TipoAreaOrdine getTipoAreaOrdine() {
		return tipoAreaOrdine;
	}

	/**
	 * @return Returns the tipoDocumento.
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codiceAgente == null) ? 0 : codiceAgente.hashCode());
		result = prime * result + ((codiceEntita == null) ? 0 : codiceEntita.hashCode());
		result = prime * result + ((codicePagamento == null) ? 0 : codicePagamento.hashCode());
		result = prime * result + ((codiceTipoDocumento == null) ? 0 : codiceTipoDocumento.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((sedeEntita == null) ? 0 : sedeEntita.hashCode());
		return result;
	}

	/**
	 * @return Returns the bloccaOrdine.
	 */
	public boolean isBloccaOrdine() {
		return bloccaOrdine;
	}

	/**
	 * @return true se il pagamento importato è quello standard, false se è stato cambiato
	 */
	public boolean isPagamentoStandard() {
		if (getPagamento() != null && getPagamentoDeterminato() != null) {
			return getPagamento().getCodicePagamento().equals(getPagamentoDeterminato().getCodicePagamento());
		}
		return true;
	}

	/**
	 * @return Returns the sedeBloccata.
	 */
	public boolean isSedeBloccata() {
		return sedeBloccata;
	}

	/**
	 * @param agente
	 *            The agente to set.
	 */
	public void setAgente(AgenteLite agente) {
		this.agente = agente;
	}

	/**
	 * @param areaOrdine
	 *            the areaOrdine to set
	 */
	public void setAreaOrdine(AreaOrdine areaOrdine) {
		this.areaOrdine = areaOrdine;
	}

	/**
	 * @param bloccaOrdine
	 *            The bloccaOrdine to set.
	 */
	public void setBloccaOrdine(boolean bloccaOrdine) {
		this.bloccaOrdine = bloccaOrdine;
	}

	/**
	 * @param codiceAgente
	 *            The codiceAgente to set.
	 */
	public void setCodiceAgente(String codiceAgente) {
		this.codiceAgente = codiceAgente;
	}

	/**
	 * @param codiceEntita
	 *            The codiceEntita to set.
	 */
	public void setCodiceEntita(String codiceEntita) {
		this.codiceEntita = codiceEntita;
	}

	/**
	 * @param codicePagamento
	 *            The codicePagamento to set.
	 */
	public void setCodicePagamento(String codicePagamento) {
		this.codicePagamento = codicePagamento;
	}

	/**
	 * @param codiceTipoDocumento
	 *            The codiceTipoDocumento to set.
	 */
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.codiceTipoDocumento = codiceTipoDocumento;
	}

	/**
	 * @param data
	 *            The data to set.
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @param deposito
	 *            The deposito to set.
	 */
	public void setDeposito(DepositoLite deposito) {
		this.deposito = deposito;
	}

	/**
	 * @param entita
	 *            The entita to set.
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param idSedeImportata
	 *            The idSedeImportata to set.
	 */
	public void setIdSedeImportata(Integer idSedeImportata) {
		this.idSedeImportata = idSedeImportata;
	}

	/**
	 * @param importoFido
	 *            the importoFido to set
	 */
	public void setImportoFido(BigDecimal importoFido) {
		this.importoFido = importoFido;
	}

	/**
	 * @param importoRateAperte
	 *            the importoRateAperte to set
	 */
	public void setImportoRateAperte(BigDecimal importoRateAperte) {
		this.importoRateAperte = importoRateAperte;
	}

	/**
	 * @param listino
	 *            the listino to set
	 */
	public void setListino(Listino listino) {
		this.listino = listino;
	}

	/**
	 * @param listinoAlternativo
	 *            the listinoAlternativo to set
	 */
	public void setListinoAlternativo(Listino listinoAlternativo) {
		this.listinoAlternativo = listinoAlternativo;
	}

	/**
	 * @param modalitaRicezione
	 *            The modalitaRicezione to set.
	 */
	public void setModalitaRicezione(ModalitaRicezione modalitaRicezione) {
		this.modalitaRicezione = modalitaRicezione;
	}

	/**
	 * @param noteBlocco
	 *            The noteBlocco to set.
	 */
	public void setNoteBlocco(String noteBlocco) {
		this.noteBlocco = noteBlocco;
	}

	/**
	 * @param numero
	 *            The numero to set.
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @param pagamento
	 *            The pagamento to set.
	 */
	public void setPagamento(CodicePagamento pagamento) {
		this.pagamento = pagamento;
	}

	/**
	 * @param pagamentoDeterminato
	 *            The pagamentoDeterminato to set.
	 */
	public void setPagamentoDeterminato(CodicePagamento pagamentoDeterminato) {
		this.pagamentoDeterminato = pagamentoDeterminato;
	}

	/**
	 * @param provenienza
	 *            the provenienza to set
	 */
	public void setProvenienza(EProvenienza provenienza) {
		this.provenienza = provenienza;
	}

	/**
	 * Aggiunge le righe all'ordine.
	 *
	 * @param righeAdd
	 *            righeAdd da aggiungere
	 */
	public void setRighe(Map<Integer, RigaOrdineImportata> righeAdd) {
		righe = new HashMap<Integer, RigaOrdineImportata>();
		righe.putAll(righeAdd);
	}

	/**
	 * @param sedeBloccata
	 *            The sedeBloccata to set.
	 */
	public void setSedeBloccata(boolean sedeBloccata) {
		this.sedeBloccata = sedeBloccata;
	}

	/**
	 * @param sedeEntita
	 *            The sedeEntita to set.
	 */
	public void setSedeEntita(SedeEntita sedeEntita) {
		this.sedeEntita = sedeEntita;
	}

	/**
	 * @param sedeEredita
	 *            The sedeEredita to set.
	 */
	public void setSedeEredita(Boolean sedeEredita) {
		this.sedeEredita = sedeEredita;
	}

	/**
	 * @param tipoAreaOrdine
	 *            The tipoAreaOrdine to set.
	 */
	public void setTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine) {
		this.tipoAreaOrdine = tipoAreaOrdine;
	}

	/**
	 * @param tipoDocumento
	 *            The tipoDocumento to set.
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@Override
	public String toString() {
		return "OrdineImportato [codiceAgente=" + codiceAgente + ", codiceEntita=" + codiceEntita + ", numero="
				+ numero + ", data=" + data + "]";
	}
}
