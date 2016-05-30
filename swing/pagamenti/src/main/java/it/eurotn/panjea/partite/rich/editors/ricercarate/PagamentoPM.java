/**
 *
 */
package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.pagamenti.util.PagamentoDTO;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe di presentazione per tenere RataPartitaDTO come attributo di Pagamento per avere accesso al protocollo del
 * documento legato alla rataPartita.
 * 
 * @author Leonardo
 */
public class PagamentoPM implements IDefProperty {

	private SituazioneRata situazioneRata = null;

	private PagamentoDTO pagamentoDTO = null;

	/**
	 * Costruttore di default.
	 */
	public PagamentoPM() {
	}

	/**
	 * Costruttore pagamentoPM.
	 * 
	 * @param pagamentoDTO
	 *            pagamentoDTO
	 * @param rata
	 *            rata
	 */
	public PagamentoPM(final PagamentoDTO pagamentoDTO, final SituazioneRata rata) {
		super();
		this.pagamentoDTO = pagamentoDTO;
		this.situazioneRata = rata;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof PagamentoPM)) {
			return false;
		}

		PagamentoPM pagamento2 = (PagamentoPM) obj;
		return this.getRata().equals(pagamento2.getRata());
	}

	/**
	 * @return pagamento.chiusuraForzata
	 */
	public Boolean getChiusuraForzataRata() {
		return pagamentoDTO.getPagamento().getChiusuraForzataRata();
	}

	/**
	 * @return rata.dataDocumento data del documento della rata
	 */
	public Date getDataDocumento() {
		return situazioneRata.getDocumento().getDataDocumento();
	}

	/**
	 * @return rata.dataScadenza
	 */
	public Date getDataScadenza() {
		return situazioneRata.getRata().getDataScadenza();
	}

	@Override
	public String getDomainClassName() {
		return pagamentoDTO.getDomainClassName();
	}

	/**
	 * @return rata.entita
	 */
	public EntitaDocumento getEntita() {
		return situazioneRata.getEntita();
	}

	@Override
	public Integer getId() {
		return pagamentoDTO.getId();
	}

	/**
	 * @return pagamento.importo
	 */
	public Importo getImporto() {
		return pagamentoDTO.getPagamento().getImporto();
	}

	/**
	 * @return rata.importo
	 */
	public BigDecimal getImportoRata() {
		return situazioneRata.getRata().getImporto().getImportoInValuta();
	}

	/**
	 * @return rata.numeroDocumento
	 */
	public CodiceDocumento getNumeroDocumento() {
		return situazioneRata.getDocumento().getCodice();
	}

	/**
	 * @return rata.numeroRata
	 */
	public Integer getNumeroRata() {
		return situazioneRata.getRata().getNumeroRata();
	}

	/**
	 * @return the pagamento
	 */
	public PagamentoDTO getPagamentoDTO() {
		return pagamentoDTO;
	}

	/**
	 * @return the rataDTO
	 */
	public SituazioneRata getRata() {
		return situazioneRata;
	}

	/**
	 * @return the rata.residuo
	 */
	public BigDecimal getResiduo() {
		return situazioneRata.getRata().getResiduo().getImportoInValuta();
	}

	/**
	 * @return the rata.tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return situazioneRata.getDocumento().getTipoDocumento();
	}

	/**
	 * @return the rata.totalePagato
	 */
	public BigDecimal getTotalePagato() {
		return situazioneRata.getRata().getTotalePagato().getImportoInValuta();
	}

	@Override
	public Integer getVersion() {
		return pagamentoDTO.getVersion();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean isNew() {
		return getId() == null;
	}

	/**
	 * @param value
	 *            imposta pagamento.chiusuraForzataRata
	 */
	public void setChiusuraForzataRata(Boolean value) {
		pagamentoDTO.getPagamento().setChiusuraForzataRata(value);
	}

	/**
	 * @param importo
	 *            imposta pagamento.importo
	 */
	public void setImporto(Importo importo) {
		pagamentoDTO.getPagamento().setImporto(importo);
	}

	/**
	 * @param pagamentoDTO
	 *            the pagamentoDTO to set
	 */
	public void setPagamentoDTO(PagamentoDTO pagamentoDTO) {
		this.pagamentoDTO = pagamentoDTO;
	}

	/**
	 * @param rata
	 *            the rata to set
	 */
	public void setRata(SituazioneRata rata) {
		this.situazioneRata = rata;
	}
}
