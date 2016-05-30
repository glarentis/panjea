package it.eurotn.panjea.preventivi.util;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.rate.domain.AreaRate;

import java.util.List;

public class AreaPreventivoFullDTOStampa extends AreaPreventivoFullDTO {

	private static final long serialVersionUID = 1L;

	private RapportoBancarioSedeEntita rapportoBancarioSedeEntita;

	private SedeAzienda sedeAzienda;

	/**
	 *
	 * @param areaPreventivo
	 *            areaPreventivo
	 * @param areaRate
	 *            areaRate
	 * @param righePreventivo
	 *            righe
	 */
	public AreaPreventivoFullDTOStampa(final AreaPreventivo areaPreventivo, final AreaRate areaRate,
			final List<RigaPreventivo> righePreventivo, final SedeAzienda sedeAzienda) {
		super();
		setSedeAzienda(sedeAzienda);
		setAreaPreventivo(areaPreventivo);
		setAreaRateEnabled(areaRate.getId() != null);
		setAreaRate(areaRate);
		setRighePreventivo(righePreventivo);
	}

	/**
	 * @return the rapportoBancarioSedeEntita
	 */
	public RapportoBancarioSedeEntita getRapportoBancarioSedeEntita() {
		return rapportoBancarioSedeEntita;
	}

	/**
	 * @return the sedeAzienda
	 */
	public SedeAzienda getSedeAzienda() {
		return sedeAzienda;
	}

	/**
	 * @param rapportoBancarioSedeEntita
	 *            the rapportoBancarioSedeEntita to set
	 */
	public void setRapportoBancarioSedeEntita(RapportoBancarioSedeEntita rapportoBancarioSedeEntita) {
		this.rapportoBancarioSedeEntita = rapportoBancarioSedeEntita;
	}

	/**
	 * @param sedeAzienda
	 *            the sedeAzienda to set
	 */
	public void setSedeAzienda(SedeAzienda sedeAzienda) {
		this.sedeAzienda = sedeAzienda;
	}
}
