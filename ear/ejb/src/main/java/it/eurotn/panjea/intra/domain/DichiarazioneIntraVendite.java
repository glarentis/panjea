package it.eurotn.panjea.intra.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue(value = "V")
public class DichiarazioneIntraVendite extends DichiarazioneIntra {
	private static final long serialVersionUID = -6958140724066845733L;

	@Override
	public String getDescrizioneDichiarazione() {
		if (isNew()) {
			return "Nuova dichiarazione vendite";
		}
		return new StringBuilder("Dichiarazione vendite n.").append(getCodice()).append(" di ").append(getMese())
				.toString();
	}

	@Override
	public TipoDichiarazione getTipoDichiarazione() {
		return TipoDichiarazione.VENDITE;
	}

	@Override
	public String getTipoRiepilogo() {
		return "C";
	}
}
