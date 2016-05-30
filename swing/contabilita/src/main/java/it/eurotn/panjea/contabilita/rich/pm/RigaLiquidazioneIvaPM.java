package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;

public class RigaLiquidazioneIvaPM {

	public enum TipoRigaLuquidazioneIvaPM {
		REGISTRO, VENTILAZIONE
	}

	private RegistroIva registroIva;

	private String tipoRegistro;

	private TotaliCodiceIvaDTO totaliCodiceIvaDTO;

	/**
	 * Costruttore.
	 * 
	 * @param registroIva
	 *            registro iva
	 * @param tipoRegistro
	 *            tipo registro
	 * @param totaliCodiceIvaDTO
	 *            totali codici iva
	 */
	public RigaLiquidazioneIvaPM(final RegistroIva registroIva, final String tipoRegistro,
			final TotaliCodiceIvaDTO totaliCodiceIvaDTO) {
		super();
		this.registroIva = registroIva;
		this.tipoRegistro = tipoRegistro;
		this.totaliCodiceIvaDTO = totaliCodiceIvaDTO;
	}

	/**
	 * @return the registroIva
	 */
	public RegistroIva getRegistroIva() {
		return registroIva;
	}

	/**
	 * @return the tipoRegistro
	 */
	public String getTipoRegistro() {
		return tipoRegistro;
	}

	/**
	 * @return the totaliCodiceIvaDTO
	 */
	public TotaliCodiceIvaDTO getTotaliCodiceIvaDTO() {
		return totaliCodiceIvaDTO;
	}
}
