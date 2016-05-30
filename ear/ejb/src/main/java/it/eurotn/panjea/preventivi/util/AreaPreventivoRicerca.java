package it.eurotn.panjea.preventivi.util;

import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;

import java.io.Serializable;

public class AreaPreventivoRicerca extends AbstractAreaDocumentoRicerca<TipoAreaPreventivo, AreaPreventivo> implements
Serializable {
	private static final long serialVersionUID = 1L;
	private StatoAreaPreventivo statoAreaPreventivo;
	private TipoAreaPreventivo tipoAreaDocumento;
	private boolean processato;

	/**
	 * Costruttore.
	 */
	public AreaPreventivoRicerca() {
		tipoAreaDocumento = new TipoAreaPreventivo();
	}

	/**
	 *
	 * Costruttore.
	 *
	 * @param areaPreventivo
	 *            area preventivo
	 */
	public AreaPreventivoRicerca(final AreaPreventivo areaPreventivo) {
		super(areaPreventivo);
		setStatoAreaPreventivo(areaPreventivo.getStatoAreaPreventivo());
	}

	@Override
	protected AreaPreventivo creaAreaDocumento() {
		return new AreaPreventivo();
	}

	/**
	 * @return the statoAreaOrdine
	 */
	public StatoAreaPreventivo getStatoAreaPreventivo() {
		return statoAreaPreventivo;
	}

	@Override
	public TipoAreaPreventivo getTipoAreaDocumento() {
		return tipoAreaDocumento;
	}

	/**
	 * @return the processato
	 */
	public boolean isProcessato() {
		return processato;
	}

	@Override
	public void setIdTipoAreaDocumento(int idTipoAreaDocumento) {
		tipoAreaDocumento.setId(idTipoAreaDocumento);
	}

	/**
	 * @param processato
	 *            the processato to set
	 */
	public void setProcessato(boolean processato) {
		this.processato = processato;
	}

	/**
	 * @param statoAreaPreventivo
	 *            the statoAreaPreventivo to set
	 */
	public void setStatoAreaPreventivo(StatoAreaPreventivo statoAreaPreventivo) {
		this.statoAreaPreventivo = statoAreaPreventivo;
	}

}
