package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;

import java.io.Serializable;

// Wrappo al nazione per aggiungerla alla tabella ed utilizzare il binding standard
public class NazioneUI implements Serializable {
	private static final long serialVersionUID = -2703076972775940929L;
	private Nazione nazione;

	/**
	 * Costruttore.
	 */
	public NazioneUI() {
	}

	/**
	 * @param nazioneWrapped
	 *            nazione da wrappare Costruttore.
	 */
	public NazioneUI(final Nazione nazioneWrapped) {
		super();
		this.nazione = nazioneWrapped;
	}

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
		NazioneUI other = (NazioneUI) obj;
		if (nazione == null) {
			if (other.nazione != null) {
				return false;
			}
		} else if (!nazione.equals(other.nazione)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the nazioneWrapped.
	 */
	public Nazione getNazione() {
		return nazione;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nazione == null) ? 0 : nazione.hashCode());
		return result;
	}

}