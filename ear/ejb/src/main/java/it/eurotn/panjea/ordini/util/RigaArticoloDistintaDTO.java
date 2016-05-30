package it.eurotn.panjea.ordini.util;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.ordini.domain.RigaArticoloDistinta;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.util.PanjeaEJBUtil;

public class RigaArticoloDistintaDTO extends RigaArticoloDTO {
	private static final long serialVersionUID = 1L;

	private final ArticoloLite articoloPadre;

	@SuppressWarnings("unused")
	private String codiceEntitaPadre;

	/**
	 * Costruttore.
	 */
	public RigaArticoloDistintaDTO() {
		articoloPadre = new ArticoloLite();
	}

	@Override
	public RigaOrdine getRigaOrdine() {
		RigaOrdine riga = new RigaArticoloDistinta();
		PanjeaEJBUtil.copyProperties(riga, this);
		return riga;
	}

	/**
	 * @param codiceEntitaPadre
	 *            the codiceEntitaPadre to set
	 */
	public void setCodiceEntitaPadre(String codiceEntitaPadre) {
		this.codiceEntitaPadre = codiceEntitaPadre;
	}

	/**
	 * @param codicePadre
	 *            the codicePadre to set
	 */
	public void setCodicePadre(String codicePadre) {
		this.articoloPadre.setCodice(codicePadre);
	}

	/**
	 * @param descrizioneArticoloPadre
	 *            descrizioneArticoloPadre dell'articoloPadre
	 */
	public void setDescrizioneArticoloPadre(String descrizioneArticoloPadre) {
		this.articoloPadre.setDescrizione(descrizioneArticoloPadre);
	}

	/**
	 * @param idArticoloPadre
	 *            the idArticoloPadre to set
	 */
	public void setIdArticoloPadre(Integer idArticoloPadre) {
		this.articoloPadre.setId(idArticoloPadre);
	}

}
