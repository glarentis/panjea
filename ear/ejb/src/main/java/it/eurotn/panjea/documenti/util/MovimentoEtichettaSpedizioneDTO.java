package it.eurotn.panjea.documenti.util;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;

public class MovimentoEtichettaSpedizioneDTO extends MovimentoSpedizioneDTO {

	private static final long serialVersionUID = 4153253466798304264L;

	private Documento documento;

	/**
	 * @return the documento
	 */
	@Override
	public Documento getDocumento() {
		return documento;
	}

	@Override
	public TipoDocumento getTipoDocumento() {
		return documento.getTipoDocumento();
	}

	/**
	 * @param documento
	 *            the documento to set
	 */
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	/**
	 * @param tipoSedeEntita
	 *            the tipoSedeEntita to set
	 */
	public void setTipoSedeSedeSpedizione(TipoSedeEntita tipoSedeEntita) {
		getSedeSpedizione().setTipoSede(tipoSedeEntita);
	}

}
