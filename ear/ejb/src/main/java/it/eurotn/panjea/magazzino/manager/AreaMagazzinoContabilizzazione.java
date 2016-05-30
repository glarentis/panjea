package it.eurotn.panjea.magazzino.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;

import java.util.Date;

public class AreaMagazzinoContabilizzazione {

	private Integer idAreaMagazzino;

	private Integer idTipoAreaContabile;

	private Integer idDocumento;
	private Date dataDocumento;
	private TipoEntita tipoEntita;

	private Integer idDepositoOrigine;

	private Integer idSedeEntita;

	private boolean notaCreditoEnable;

	private boolean generaAreaContabile;

	/**
	 * Costruttore.
	 * 
	 * @param idAreaMagazzino
	 *            id dell'area magazzino
	 * @param idTipoAreaContabile
	 *            id del tipo area contabile
	 * @param idDocumento
	 *            id del documento
	 * @param dataDocumento
	 *            data del documento
	 * @param tipoEntita
	 *            tipo entità del tipo documento
	 * @param idDepositoOrigine
	 *            id del deposito di origine
	 * @param idSedeEntita
	 *            id della sede entità
	 * @param notaCreditoEnable
	 *            flag nota credito
	 * @param generaAreaContabile
	 *            generaAreaContabile
	 */
	public AreaMagazzinoContabilizzazione(final Integer idAreaMagazzino, final Integer idTipoAreaContabile,
			final Integer idDocumento, final Date dataDocumento, final TipoEntita tipoEntita,
			final Integer idDepositoOrigine, final Integer idSedeEntita, final boolean notaCreditoEnable,
			final boolean generaAreaContabile) {
		super();
		this.idAreaMagazzino = idAreaMagazzino;
		this.idTipoAreaContabile = idTipoAreaContabile;
		this.idDocumento = idDocumento;
		this.dataDocumento = dataDocumento;
		this.tipoEntita = tipoEntita;
		this.idDepositoOrigine = idDepositoOrigine;
		this.idSedeEntita = idSedeEntita;
		this.notaCreditoEnable = notaCreditoEnable;
		this.generaAreaContabile = generaAreaContabile;
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the idAreaMagazzino
	 */
	public Integer getIdAreaMagazzino() {
		return idAreaMagazzino;
	}

	/**
	 * @return the idDepositoOrdigine
	 */
	public Integer getIdDepositoOrigine() {
		return idDepositoOrigine;
	}

	/**
	 * @return the idDocumento
	 */
	public Integer getIdDocumento() {
		return idDocumento;
	}

	/**
	 * @return the idSedeEntita
	 */
	public Integer getIdSedeEntita() {
		return idSedeEntita;
	}

	/**
	 * @return the idTipoAreaContabile
	 */
	public Integer getIdTipoAreaContabile() {
		return idTipoAreaContabile;
	}

	/**
	 * @return the tipoEntita
	 */
	public TipoEntita getTipoEntita() {
		return tipoEntita;
	}

	/**
	 * @return the generaAreaContabile
	 */
	public boolean isGeneraAreaContabile() {
		return generaAreaContabile;
	}

	/**
	 * @return the notaCreditoEnable
	 */
	public boolean isNotaCreditoEnable() {
		return notaCreditoEnable;
	}
}
