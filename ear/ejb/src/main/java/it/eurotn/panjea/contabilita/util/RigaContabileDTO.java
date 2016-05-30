/**
 *
 */
package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile.EContoInsert;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO utile per ricerche.
 * 
 * @author adriano
 * @version 1.0, 14/nov/07
 */
public class RigaContabileDTO extends AreaContabileDTO implements Serializable {

	/**
	 * Indica se la riga contabile DTO rappresenta un riga contabile con la sua area contabile (ROW) oppure se il DTO
	 * contiene solo un'area contabile e la riga vuota (FAKE).
	 * 
	 * @author fattazzo
	 */
	public enum ETipoRigaContabileDTO {
		ROW, FAKE
	}

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String mastroCodice;

	private String contoCodice;

	private String sottoContoCodice;

	private String descrizioneSottoConto;

	private String note;

	private BigDecimal importoDare;

	private BigDecimal importoAvere;

	private AreaContabileDTO areaContabileDTO;

	private ETipoRigaContabileDTO tipoRigaContabileDTO;

	private String registroProtocollo;

	private EContoInsert contoInsert;

	/**
	 * Costruttore.
	 */
	public RigaContabileDTO() {
		this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null);
	}

	/**
	 * Costruttore.
	 * 
	 * @param idAreaContabile
	 *            idAreaContabile
	 * @param statoAreaContabile
	 *            statoAreaContabile
	 * @param annoMovimento
	 *            annoMovimento
	 * @param dataDocumento
	 *            dataDocumento
	 * @param dataRegistrazione
	 *            dataRegistrazione
	 * @param numeroDocumento
	 *            numeroDocumento
	 * @param numeroProtocollo
	 *            numeroProtocollo
	 * @param codiceTipoDocumento
	 *            codiceTipoDocumento
	 * @param descrizioneTipoDocumento
	 *            descrizioneTipoDocumento
	 * @param noteAreaContabile
	 *            noteAreaContabile
	 * @param numeroPaginaGiornale
	 *            numeroPaginaGiornale
	 * @param totale
	 *            totale
	 * @param tipoRigaContabileDTO
	 *            tipoRigaContabileDTO
	 */
	public RigaContabileDTO(final Integer idAreaContabile, final StatoAreaContabile statoAreaContabile,
			final Integer annoMovimento, final Date dataDocumento, final Date dataRegistrazione,
			final CodiceDocumento numeroDocumento, final CodiceDocumento numeroProtocollo,
			final String codiceTipoDocumento, final String descrizioneTipoDocumento, final String noteAreaContabile,
			final Integer numeroPaginaGiornale, final Importo totale, final String tipoRigaContabileDTO) {
		super();
		this.areaContabileDTO = new AreaContabileDTO(idAreaContabile, statoAreaContabile, annoMovimento, dataDocumento,
				dataRegistrazione, numeroDocumento, numeroProtocollo, codiceTipoDocumento, descrizioneTipoDocumento,
				noteAreaContabile, numeroPaginaGiornale, totale, null);
		this.tipoRigaContabileDTO = ETipoRigaContabileDTO.valueOf(tipoRigaContabileDTO);
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param idAreaContabile
	 *            idAreaContabile
	 * @param statoAreaContabile
	 *            statoAreaContabile
	 * @param annoMovimento
	 *            annoMovimento
	 * @param dataDocumento
	 *            dataDocumento
	 * @param dataRegistrazione
	 *            dataRegistrazione
	 * @param numeroDocumento
	 *            numeroDocumento
	 * @param numeroProtocollo
	 *            numeroProtocollo
	 * @param codiceTipoDocumento
	 *            codiceTipoDocumento
	 * @param descrizioneTipoDocumento
	 *            descrizioneTipoDocumento
	 * @param noteAreaContabile
	 *            noteAreaContabile
	 * @param numeroPaginaGiornale
	 *            numeroPaginaGiornale
	 * @param idRigaContabile
	 *            idRigaContabile
	 * @param mastroCodice
	 *            mastroCodice
	 * @param contoCodice
	 *            contoCodice
	 * @param sottoContoCodice
	 *            sottoContoCodice
	 * @param descrizioneSottoConto
	 *            descrizioneSottoConto
	 * @param noteRigaContabile
	 *            noteRigaContabile
	 * @param importoDare
	 *            importoDare
	 * @param importoAvere
	 *            importoAvere
	 * @param contoInsert
	 *            tipo conto
	 * @param totale
	 *            totale
	 * @param registroProtocollo
	 *            numeratore del protocollo dell'area contabile.
	 */
	public RigaContabileDTO(final Integer idAreaContabile, final StatoAreaContabile statoAreaContabile,
			final Integer annoMovimento, final Date dataDocumento, final Date dataRegistrazione,
			final CodiceDocumento numeroDocumento, final CodiceDocumento numeroProtocollo,
			final String codiceTipoDocumento, final String descrizioneTipoDocumento, final String noteAreaContabile,
			final Integer numeroPaginaGiornale, final Integer idRigaContabile, final String mastroCodice,
			final String contoCodice, final String sottoContoCodice, final String descrizioneSottoConto,
			final String noteRigaContabile, final BigDecimal importoDare, final BigDecimal importoAvere,
			final EContoInsert contoInsert, final Importo totale, final String registroProtocollo) {
		super();
		this.registroProtocollo = registroProtocollo;
		this.areaContabileDTO = new AreaContabileDTO(idAreaContabile, statoAreaContabile, annoMovimento, dataDocumento,
				dataRegistrazione, numeroDocumento, numeroProtocollo, codiceTipoDocumento, descrizioneTipoDocumento,
				noteAreaContabile, numeroPaginaGiornale, totale, registroProtocollo);
		this.id = idRigaContabile;
		this.mastroCodice = mastroCodice;
		this.contoCodice = contoCodice;
		this.sottoContoCodice = sottoContoCodice;
		this.descrizioneSottoConto = descrizioneSottoConto;
		this.note = noteRigaContabile;
		this.importoDare = importoDare;
		this.importoAvere = importoAvere;
		this.contoInsert = contoInsert;
		this.tipoRigaContabileDTO = ETipoRigaContabileDTO.ROW;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof RigaContabileDTO)) {
			return false;
		}
		return (this.getId().equals(((RigaContabileDTO) obj).getId()));
	}

	/**
	 * @return Returns the areaContabileDTO.
	 */
	public AreaContabileDTO getAreaContabileDTO() {
		return areaContabileDTO;
	}

	/**
	 * getter method che compone il codice del sottoconto cos� formattato : MMM.CCC.SSSSSSS dove MMM = Codice Mastro CCC
	 * = Codice Conto SSSSSS = Codice Sottoconto.
	 * 
	 * @return codice sottoconto
	 */
	public String getCodiceSottoConto() {

		StringBuffer codiceSottoConto = new StringBuffer();
		if ("".equals(sottoContoCodice)) {
			return sottoContoCodice;
		}
		codiceSottoConto.append(mastroCodice).append(".").append(contoCodice).append(".").append(sottoContoCodice);
		return codiceSottoConto.toString();

	}

	/**
	 * @return the contoInsert
	 */
	public EContoInsert getContoInsert() {
		return contoInsert;
	}

	@Override
	public Date getDataDocumento() {
		return null;
	}

	@Override
	public Date getDataRegistrazione() {
		return null;
	}

	/**
	 * @return Returns the descrizioneSottoConto.
	 */
	public String getDescrizioneSottoConto() {
		return descrizioneSottoConto;
	}

	/**
	 * @return Returns the id.
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @return importo
	 */
	public BigDecimal getImporto() {
		BigDecimal result = BigDecimal.ZERO;

		switch (contoInsert) {
		case DARE:
			if ((this.importoDare != null) && (BigDecimal.ZERO.compareTo(this.importoDare) != 0)) {
				result = this.importoDare;
			}
			break;
		case AVERE:
			if ((this.importoAvere != null) && (BigDecimal.ZERO.compareTo(this.importoAvere) != 0)) {
				result = this.importoAvere;
			}
			break;
		default:
			result = BigDecimal.ZERO;
			break;
		}

		return result;
	}

	/**
	 * @return Returns the importoAvere.
	 */
	@Override
	public BigDecimal getImportoAvere() {
		return importoAvere;
	}

	/**
	 * @return Returns the importoDare.
	 */
	@Override
	public BigDecimal getImportoDare() {
		return importoDare;
	}

	/**
	 * @return Returns the note.
	 */
	@Override
	public String getNote() {
		return note;
	}

	@Override
	public CodiceDocumento getNumeroDocumento() {
		return null;
	}

	/**
	 * @return Returns the numeratoreProtocollo.
	 */
	@Override
	public String getRegistroProtocollo() {
		return registroProtocollo;
	}

	/**
	 * Getter Sbilancio (Dare-Avere).
	 * 
	 * @return sbilanciamento
	 */
	public BigDecimal getSbilanciamento() {
		return this.importoDare.subtract(this.importoAvere);
	}

	@Override
	public BigDecimal getSbilancio() {
		return null;
	}

	/**
	 * @return Returns the sottoContoCodice.
	 */
	public String getSottoContoCodice() {
		return sottoContoCodice;
	}

	@Override
	public StatoAreaContabile getStatoAreaContabile() {
		return null;
	}

	@Override
	public TipoDocumento getTipoDocumento() {
		return null;
	}

	/**
	 * @return tipoRigaContabileDTO
	 */
	public ETipoRigaContabileDTO getTipoRigaContabileDTO() {
		return tipoRigaContabileDTO;
	}

	@Override
	public int hashCode() {
		if (getId() != null) {
			String hashStr = this.getClass().getName() + ":" + getId();
			return hashStr.hashCode();
		}
		return super.hashCode();
	}

	/**
	 * @param areaContabileDTO
	 *            The areaContabileDTO to set.
	 */
	public void setAreaContabileDTO(AreaContabileDTO areaContabileDTO) {
		this.areaContabileDTO = areaContabileDTO;
	}

	/**
	 * @param contoInsert
	 *            the contoInsert to set
	 */
	public void setContoInsert(EContoInsert contoInsert) {
		this.contoInsert = contoInsert;
	}

	/**
	 * @param descrizioneSottoConto
	 *            The descrizioneSottoConto to set.
	 */
	public void setDescrizioneSottoConto(String descrizioneSottoConto) {
		this.descrizioneSottoConto = descrizioneSottoConto;
	}

	/**
	 * @param registroProtocollo
	 *            The registroProtocollo to set.
	 */
	@Override
	public void setRegistroProtocollo(String registroProtocollo) {
		this.registroProtocollo = registroProtocollo;
	}

	/**
	 * @param tipoRigaContabileDTO
	 *            the tipoRigaContabileDTO to set
	 */
	public void setTipoRigaContabileDTO(ETipoRigaContabileDTO tipoRigaContabileDTO) {
		this.tipoRigaContabileDTO = tipoRigaContabileDTO;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("RigaContabileDTO[");
		buffer.append(" areaContabileDTO = ").append(areaContabileDTO != null ? areaContabileDTO.getId() : null);
		buffer.append(" contoCodice = ").append(contoCodice);
		buffer.append(" descrizioneSottoConto = ").append(descrizioneSottoConto);
		buffer.append(" id = ").append(id);
		buffer.append(" importoAvere = ").append(importoAvere);
		buffer.append(" importoDare = ").append(importoDare);
		buffer.append(" mastroCodice = ").append(mastroCodice);
		buffer.append(" note = ").append(note);
		buffer.append(" sottoContoCodice = ").append(sottoContoCodice);
		buffer.append("]");
		return buffer.toString();
	}

}
