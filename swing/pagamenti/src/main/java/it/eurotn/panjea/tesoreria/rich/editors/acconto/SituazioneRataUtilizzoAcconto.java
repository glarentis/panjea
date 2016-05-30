package it.eurotn.panjea.tesoreria.rich.editors.acconto;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.math.BigDecimal;
import java.util.Date;

public class SituazioneRataUtilizzoAcconto {

	private final SituazioneRata situazioneRata;

	private BigDecimal importoAcconto;

	private final BigDecimal importoResiduo;

	/**
	 * Costruttore.
	 */
	public SituazioneRataUtilizzoAcconto() {
		super();
		this.situazioneRata = null;
		this.importoAcconto = BigDecimal.ZERO;
		this.importoResiduo = BigDecimal.ZERO;
	}

	/**
	 * Costruttore.
	 * 
	 * @param situazioneRata
	 *            situazioneRata
	 */
	public SituazioneRataUtilizzoAcconto(final SituazioneRata situazioneRata) {
		super();
		this.situazioneRata = situazioneRata;
		this.importoAcconto = BigDecimal.ZERO;
		this.importoResiduo = situazioneRata.getRata().getResiduo().getImportoInValuta();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		SituazioneRataUtilizzoAcconto other = (SituazioneRataUtilizzoAcconto) obj;
		if (situazioneRata == null) {
			if (other.situazioneRata != null) {
				return false;
			}
		} else {
			if (!situazioneRata.equals(other.situazioneRata)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return situazioneRata.getRata().getDataScadenza();
	}

	/**
	 * @return the entita
	 */
	public EntitaDocumento getEntita() {
		return situazioneRata.getEntita();
	}

	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return situazioneRata.getRata().getImporto().getImportoInValuta();
	}

	/**
	 * @return the importoAcconto
	 */
	public BigDecimal getImportoAcconto() {
		return importoAcconto;
	}

	/**
	 * @return the importoResiduo
	 */
	public BigDecimal getImportoResiduo() {
		if (importoAcconto == null) {
			importoAcconto = BigDecimal.ZERO;
		}
		return this.importoResiduo.subtract(this.importoAcconto);
	}

	/**
	 * @return the numeroDocumento
	 */
	public CodiceDocumento getNumeroDocumento() {
		return situazioneRata.getDocumento().getCodice();
	}

	/**
	 * @return the situazioneRata
	 */
	public SituazioneRata getSituazioneRata() {
		return situazioneRata;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return situazioneRata.getDocumento().getTipoDocumento();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((situazioneRata == null) ? 0 : situazioneRata.hashCode());
		return result;
	}

	/**
	 * @param importoAcconto
	 *            the importoAcconto to set
	 */
	public void setImportoAcconto(BigDecimal importoAcconto) {
		this.importoAcconto = importoAcconto;
	}
}
