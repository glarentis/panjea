/**
 *
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author adriano
 * @version 1.0, 28/giu/07
 */
public class RigaContabileEstrattoConto implements Serializable {

	private static final long serialVersionUID = -7084151804451104550L;
	private Integer idRigaContabile;
	private int ordinamento;
	private Date dataRegistrazione;
	private String sottoContoDescrizione;
	private BigDecimal importoDare;
	private BigDecimal importoAvere;
	private BigDecimal progressivoImporto;
	private Date dataDocumento;
	private CodiceDocumento numeroDocumento;
	private CodiceDocumento protocollo;
	private TipoDocumento tipoDocumento;
	private String note;
	private String noteAreaContabile;
	private AreaContabile.StatoAreaContabile statoAreaContabile;
	private Integer idAreaContabile;
	private EntitaLite entita;
	private Integer riga;
	private Integer pagina;
	private String codiceMastro;
	private String codiceConto;
	private String codiceSottoConto;
	private Integer numeroRata;
	private CodiceDocumento numeroDocumentoCollegato;
	private Date dataDocumentoCollegato;
	private String codiceTipoDocumentoCollegato;
	private String descrizioneTipoDocumentoCollegato;
	private String documentiCollegati;
	private String entCollegato;

	/**
	 * Costruttore.
	 */
	public RigaContabileEstrattoConto() {
		super();
		documentiCollegati = "";
	}

	/**
	 * @param paramDocumentiCollegati
	 *            the documentiCollegati to add
	 * @param entCollegata
	 *            entita collegata al doc. collegato.
	 */
	public void addDocumentiCollegati(String paramDocumentiCollegati, String entCollegata) {
		StringBuilder sb = new StringBuilder(1000).append(this.documentiCollegati);
		if (!documentiCollegati.isEmpty()) {
			if (sb.length() > 0) {
				sb.append(",");
			}
		}
		if (entCollegata != null) {
			sb.append(entCollegata).append("-");
		}
		sb.append(paramDocumentiCollegati);
		this.documentiCollegati = sb.toString();
	}

	/**
	 * @return the codiceConto
	 */
	public String getCodiceConto() {
		return codiceConto;
	}

	/**
	 * @return the codiceMastro
	 */
	public String getCodiceMastro() {
		return codiceMastro;
	}

	/**
	 * @return the codiceSottoConto
	 */
	public String getCodiceSottoConto() {
		return codiceSottoConto;
	}

	/**
	 * @return the codiceTipoDocumentoCollegato
	 */
	public String getCodiceTipoDocumentoCollegato() {
		return codiceTipoDocumentoCollegato;
	}

	/**
	 * @return dataDocumento
	 * @uml.property name="dataDocumento"
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the dataDocumentoCollegato
	 */
	public Date getDataDocumentoCollegato() {
		return dataDocumentoCollegato;
	}

	/**
	 * @return Returns the dataRegistrazione.
	 * @uml.property name="dataRegistrazione"
	 */
	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return the descrizioneTipoDocumentoCollegato
	 */
	public String getDescrizioneTipoDocumentoCollegato() {
		return descrizioneTipoDocumentoCollegato;
	}

	/**
	 * @return the documentiCollegati
	 */
	public String getDocumentiCollegati() {
		return documentiCollegati;
	}

	/**
	 * @return la stringa formattata con le informazioni base per identificare il documento collegato
	 */
	public String getDocumentoCollegato() {
		StringBuffer docCollegato = new StringBuffer();
		if (numeroDocumentoCollegato != null) {
			docCollegato = docCollegato.append(codiceTipoDocumentoCollegato);
			docCollegato = docCollegato.append(" nr." + numeroDocumentoCollegato.getCodice());
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			docCollegato = docCollegato.append(" del " + dateFormat.format(dataDocumentoCollegato));
			docCollegato = docCollegato.append(" rata n." + numeroRata);
		}
		return docCollegato.toString();
	}

	/**
	 * @return Returns the entCollegato.
	 */
	public String getEntCollegato() {
		return entCollegato;
	}

	/**
	 * @return entita
	 * @uml.property name="entita"
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return idAreaContabile
	 * @uml.property name="idAreaContabile"
	 */
	public Integer getIdAreaContabile() {
		return idAreaContabile;
	}

	/**
	 * @return the idRigaContabile
	 */
	public Integer getIdRigaContabile() {
		return idRigaContabile;
	}

	/**
	 * @return Returns the importoAvere.
	 * @uml.property name="importoAvere"
	 */
	public BigDecimal getImportoAvere() {
		return (BigDecimal.ZERO.compareTo(importoAvere) != 0) ? importoAvere : null;
	}

	/**
	 * @return Returns the importoDare.
	 * @uml.property name="importoDare"
	 */
	public BigDecimal getImportoDare() {
		return (BigDecimal.ZERO.compareTo(importoDare) != 0) ? importoDare : null;
	}

	/**
	 * @return note
	 * @uml.property name="note"
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return noteAreaContabile
	 * @uml.property name="noteAreaContabile"
	 */
	public String getNoteAreaContabile() {
		return noteAreaContabile;
	}

	/**
	 * @return note da visualizzare in stampa dell'estratto conto
	 */
	public String getNotePerStampa() {
		StringBuffer sb = new StringBuffer();
		String lf = "";
		if (noteAreaContabile != null && !noteAreaContabile.isEmpty()) {
			sb.append(noteAreaContabile);
			lf = "\n";
		}
		if (entita != null) {
			sb.append(lf);
			sb.append(entita.getCodice());
			sb.append(" - ");
			sb.append(entita.getAnagrafica().getDenominazione());
			lf = "\n";
		}
		if (getDocumentiCollegati() != null && !getDocumentiCollegati().isEmpty()) {
			sb.append(lf);
			sb.append(getDocumentiCollegati().replaceAll(",", "\n"));
			lf = "\n";
		}
		if (note != null && !note.isEmpty()) {
			sb.append(lf);
			sb.append(note);
			lf = "\n";
		}
		return sb.toString();
	}

	/**
	 * @return numeroDocumento
	 * @uml.property name="numeroDocumento"
	 */
	public CodiceDocumento getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @return the numeroDocumentoCollegato
	 */
	public CodiceDocumento getNumeroDocumentoCollegato() {
		return numeroDocumentoCollegato;
	}

	/**
	 * @return the numeroRata
	 */
	public Integer getNumeroRata() {
		return numeroRata;
	}

	/**
	 * @return Returns the ordinamento.
	 * @uml.property name="ordinamento"
	 */
	public int getOrdinamento() {
		return ordinamento;
	}

	/**
	 * @return Returns the pagina.
	 */
	public Integer getPagina() {
		if (pagina.compareTo(0) == 0) {
			return null;
		}
		return pagina;
	}

	/**
	 * @return Returns the progressivoImporto.
	 * @uml.property name="progressivoImporto"
	 */
	public BigDecimal getProgressivoImporto() {
		return progressivoImporto;
	}

	/**
	 * @return protocollo
	 * @uml.property name="protocollo"
	 */
	public CodiceDocumento getProtocollo() {
		return protocollo;
	}

	/**
	 * @return Returns the riga.
	 */
	public Integer getRiga() {
		return riga;
	}

	/**
	 * @return Returns the sottoContoCodice.
	 * @uml.property name="sottoContoCodice"
	 */
	public String getSottoContoCodice() {
		return codiceMastro + "." + codiceConto + "." + codiceSottoConto;
	}

	/**
	 * @return Returns the sottoContoDescrizione.
	 * @uml.property name="sottoContoDescrizione"
	 */
	public String getSottoContoDescrizione() {
		return sottoContoDescrizione;
	}

	/**
	 * @return statoAreaContabile
	 * @uml.property name="statoAreaContabile"
	 */
	public StatoAreaContabile getStatoAreaContabile() {
		return statoAreaContabile;
	}

	/**
	 * @return tipoDocumento
	 * @uml.property name="tipoDocumento"
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param codiceConto
	 *            the codiceConto to set
	 */
	public void setCodiceConto(String codiceConto) {
		this.codiceConto = codiceConto;
	}

	/**
	 * @param codiceMastro
	 *            the codiceMastro to set
	 */
	public void setCodiceMastro(String codiceMastro) {
		this.codiceMastro = codiceMastro;
	}

	/**
	 * @param codiceSottoConto
	 *            the codiceSottoConto to set
	 */
	public void setCodiceSottoConto(String codiceSottoConto) {
		this.codiceSottoConto = codiceSottoConto;
	}

	/**
	 * @param codiceTipoDocumentoCollegato
	 *            the codiceTipoDocumentoCollegato to set
	 */
	public void setCodiceTipoDocumentoCollegato(String codiceTipoDocumentoCollegato) {
		this.codiceTipoDocumentoCollegato = codiceTipoDocumentoCollegato;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param dataDocumentoCollegato
	 *            the dataDocumentoCollegato to set
	 */
	public void setDataDocumentoCollegato(Date dataDocumentoCollegato) {
		this.dataDocumentoCollegato = dataDocumentoCollegato;
	}

	/**
	 * @param dataRegistrazione
	 *            the dataRegistrazione to set
	 */
	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param descrizioneTipoDocumentoCollegato
	 *            the descrizioneTipoDocumentoCollegato to set
	 */
	public void setDescrizioneTipoDocumentoCollegato(String descrizioneTipoDocumentoCollegato) {
		this.descrizioneTipoDocumentoCollegato = descrizioneTipoDocumentoCollegato;
	}

	/**
	 * @param entCollegato
	 *            The entCollegato to set.
	 */
	public void setEntCollegato(String entCollegato) {
		this.entCollegato = entCollegato;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param idAreaContabile
	 *            the idAreaContabile to set
	 */
	public void setIdAreaContabile(Integer idAreaContabile) {
		this.idAreaContabile = idAreaContabile;
	}

	/**
	 * @param idRigaContabile
	 *            the idRigaContabile to set
	 */
	public void setIdRigaContabile(Integer idRigaContabile) {
		this.idRigaContabile = idRigaContabile;
	}

	/**
	 * @param importoAvere
	 *            the importoAvere to set
	 */
	public void setImportoAvere(BigDecimal importoAvere) {
		this.importoAvere = importoAvere;
	}

	/**
	 * @param importoDare
	 *            the importoDare to set
	 */
	public void setImportoDare(BigDecimal importoDare) {
		this.importoDare = importoDare;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param noteAreaContabile
	 *            the noteAreaContabile to set
	 */
	public void setNoteAreaContabile(String noteAreaContabile) {
		this.noteAreaContabile = noteAreaContabile;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(CodiceDocumento numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @param numeroDocumentoCollegato
	 *            the numeroDocumentoCollegato to set
	 */
	public void setNumeroDocumentoCollegato(CodiceDocumento numeroDocumentoCollegato) {
		this.numeroDocumentoCollegato = numeroDocumentoCollegato;
	}

	/**
	 * @param numeroRata
	 *            the numeroRata to set
	 */
	public void setNumeroRata(Integer numeroRata) {
		this.numeroRata = numeroRata;
	}

	/**
	 * @param ordinamento
	 *            The ordinamento to set.
	 * @uml.property name="ordinamento"
	 */
	public void setOrdinamento(int ordinamento) {
		this.ordinamento = ordinamento;
	}

	/**
	 * @param pagina
	 *            The pagina to set.
	 */
	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

	/**
	 * @param progressivoImporto
	 *            The progressivoImporto to set.
	 * @uml.property name="progressivoImporto"
	 */
	public void setProgressivoImporto(BigDecimal progressivoImporto) {
		this.progressivoImporto = progressivoImporto;
	}

	/**
	 * @param protocollo
	 *            the protocollo to set
	 */
	public void setProtocollo(CodiceDocumento protocollo) {
		this.protocollo = protocollo;
	}

	/**
	 * @param riga
	 *            The riga to set.
	 */
	public void setRiga(Integer riga) {
		this.riga = riga;
	}

	/**
	 * @param sottoContoDescrizione
	 *            the sottoContoDescrizione to set
	 */
	public void setSottoContoDescrizione(String sottoContoDescrizione) {
		this.sottoContoDescrizione = sottoContoDescrizione;
	}

	/**
	 * @param statoAreaContabile
	 *            the statoAreaContabile to set
	 */
	public void setStatoAreaContabile(AreaContabile.StatoAreaContabile statoAreaContabile) {
		this.statoAreaContabile = statoAreaContabile;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RigaContabileEstrattoConto [dataDocumento=");
		builder.append(dataDocumento);
		builder.append(", importoAvere=");
		builder.append(importoAvere);
		builder.append(", importoDare=");
		builder.append(importoDare);
		builder.append(", numeroDocumento=");
		builder.append(numeroDocumento);
		builder.append(", progressivoImporto=");
		builder.append(progressivoImporto);
		builder.append(", sottoContoCodice=");
		builder.append(getSottoContoCodice());
		builder.append("]");
		return builder.toString();
	}
}
