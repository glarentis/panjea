package it.eurotn.panjea.intra.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue(value = "A")
public class DichiarazioneIntraAcquisti extends DichiarazioneIntra {
	private static final long serialVersionUID = -6958140724066845733L;

	@Override
	public String getDescrizioneDichiarazione() {
		if (isNew()) {
			return "Nuova dichiarazione acquisti";
		}
		StringBuilder sb = new StringBuilder("Dic. acquisti n.").append(getCodice());
		if (getTipoPeriodo() == TipoPeriodo.M) {
			sb.append(" mese ");
			sb.append(getMese());
		} else {
			sb.append(" trimestre ");
			sb.append(getTrimestre());
		}
		return sb.toString();
	}

	@Override
	public TipoDichiarazione getTipoDichiarazione() {
		return TipoDichiarazione.ACQUISTI;
	}

	@Override
	public String getTipoRiepilogo() {
		return "A";
	}
}
