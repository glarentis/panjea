package it.eurotn.panjea.ordini.util;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.ordini.domain.RigaArticoloComponente;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;

public class RigaArticoloComponenteDTO extends RigaArticoloDistintaDTO {

	private static final long serialVersionUID = -6528336619203988032L;

	private Integer idAreaOrdine;

	/**
	 * Presupponendo che this sia un articolo padre, aggiunge a this la qta e la descrizione aggiornando di conseguenza
	 * il prezzo totale.
	 * 
	 * @param rigaArticoloDTO
	 *            i dati del figlio da aggiungere al padre (this)
	 */
	public void aggiungiArticoloDTO(RigaArticoloDTO rigaArticoloDTO) {
		setQta(getQta() + rigaArticoloDTO.getQta());
		setQtaChiusa(getQtaChiusa() + rigaArticoloDTO.getQtaChiusa());
		setPrezzoTotale(getPrezzoNetto().multiply(BigDecimal.valueOf(getQta()), getNumeroDecimaliPrezzo()));
	}

	/**
	 * @return the idAreaOrdine
	 */
	public Integer getIdAreaOrdine() {
		return idAreaOrdine;
	}

	@Override
	public RigaOrdine getRigaOrdine() {
		RigaArticoloComponente riga = new RigaArticoloComponente();
		PanjeaEJBUtil.copyProperties(riga, this);

		AreaOrdine areaOrdine = new AreaOrdine();
		areaOrdine.setId(this.idAreaOrdine);
		riga.setAreaOrdine(areaOrdine);

		ArticoloLite articolo = new ArticoloLite();
		articolo.setId(this.getArticolo().getId());
		riga.setArticolo(articolo);

		riga.setPrezzoUnitario(this.getPrezzoUnitario());
		return riga;
	}

	/**
	 * @param idAreaOrdine
	 *            the idAreaOrdine to set
	 */
	public void setIdAreaOrdine(Integer idAreaOrdine) {
		this.idAreaOrdine = idAreaOrdine;
	}
}
