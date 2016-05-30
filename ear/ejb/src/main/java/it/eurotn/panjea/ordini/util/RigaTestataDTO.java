package it.eurotn.panjea.ordini.util;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.RigaTestata;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.util.PanjeaEJBUtil;

public class RigaTestataDTO extends RigaOrdineDTO {
	private static final long serialVersionUID = 649258182055455383L;

	private String codiceTipoDocumentoCollegato;

	private Integer idAreaPreventivoCollegata;

	private IAreaDocumento areaDocumentoCollegata;

	/**
	 * @return restituisce l'area documento collegata
	 */
	public IAreaDocumento getAreaDocumentoCollegata() {
		if (areaDocumentoCollegata == null) {
			if (idAreaPreventivoCollegata != null) {
				AreaPreventivo areaPreventivo = new AreaPreventivo();
				areaPreventivo.setId(idAreaPreventivoCollegata);
				areaDocumentoCollegata = areaPreventivo;
			}
		}

		return areaDocumentoCollegata;
	}

	/**
	 * @return the codiceTipoDocumentoCollegato
	 */
	public String getCodiceTipoDocumentoCollegato() {
		return codiceTipoDocumentoCollegato;
	}

	/**
	 * @return the idAreaPreventivoCollegata
	 */
	public Integer getIdAreaPreventivoCollegata() {
		return idAreaPreventivoCollegata;
	}

	@Override
	public RigaOrdine getRigaOrdine() {
		RigaOrdine riga = new RigaTestata();
		PanjeaEJBUtil.copyProperties(riga, this);
		return riga;
	}

	/**
	 * 
	 * @return true se la riga testata è una riga Testata di un documento di origine
	 */
	public boolean isRigaTestataDocumento() {
		return codiceTipoDocumentoCollegato != null;
	}

	/**
	 * @param codiceTipoDocumentoCollegato
	 *            the codiceTipoDocumentoCollegato to set
	 */
	public void setCodiceTipoDocumentoCollegato(String codiceTipoDocumentoCollegato) {
		this.codiceTipoDocumentoCollegato = codiceTipoDocumentoCollegato;
	}

	/**
	 * @param idAreaPreventivoCollegata
	 *            the idAreaPreventivoCollegata to set
	 */
	public void setIdAreaPreventivoCollegata(Integer idAreaPreventivoCollegata) {
		this.idAreaPreventivoCollegata = idAreaPreventivoCollegata;
	}
}
