package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.tesoreria.domain.AreaAnticipo;
import it.eurotn.panjea.tesoreria.domain.RigaAnticipo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class SituazioneRigaAnticipo implements Serializable {

	private static final long serialVersionUID = 7081205534194872053L;

	private RigaAnticipo rigaAnticipo;

	private BigDecimal importoGiaAnticipato;

	private final List<SituazioneEffetto> situazioneEffetti;

	/**
	 * Costruttore.
	 */
	public SituazioneRigaAnticipo() {
		super();
		this.situazioneEffetti = new ArrayList<SituazioneEffetto>();
	}

	/**
	 * Costruttore.
	 * 
	 * @param situazioneEffetti
	 *            situazione effetti che compongono la riga anticipo
	 * @param importoGiaAnticipato
	 *            importo già anticipato
	 */
	public SituazioneRigaAnticipo(final List<SituazioneEffetto> situazioneEffetti, final BigDecimal importoGiaAnticipato) {
		super();
		this.situazioneEffetti = situazioneEffetti;

		RigaAnticipo rigaAnticipoNew = new RigaAnticipo();
		rigaAnticipoNew.setDataValuta(this.situazioneEffetti.get(0).getEffetto().getDataValuta());

		Importo impAnticipato = null;
		for (SituazioneEffetto situazioneEffetto : situazioneEffetti) {
			if (impAnticipato == null) {
				impAnticipato = situazioneEffetto.getEffetto().getImporto().clone();
			} else {
				impAnticipato = impAnticipato.add(situazioneEffetto.getEffetto().getImporto(), 2);
			}
		}
		rigaAnticipoNew.setImportoAnticipato(impAnticipato.getImportoInValutaAzienda());

		Documento documento = new Documento();
		documento.setRapportoBancarioAzienda(this.situazioneEffetti.get(0).getRapportoBancario());
		documento.setTotale(impAnticipato);

		AreaAnticipo areaAnticipo = new AreaAnticipo();
		areaAnticipo.setDocumento(documento);

		rigaAnticipoNew.setAreaAnticipo(areaAnticipo);

		this.rigaAnticipo = rigaAnticipoNew;

		this.importoGiaAnticipato = importoGiaAnticipato;

		// setto come importo di anticipo alla riga, l'importo del documento
		// meno l'importo eventualmente già anticipato
		this.rigaAnticipo.setImportoAnticipato(this.rigaAnticipo.getImportoAnticipato().subtract(
				this.importoGiaAnticipato));

		this.rigaAnticipo.setAreaEffetti(situazioneEffetti.get(0).getAreaEffetto());
	}

	/**
	 * @return the impotoGiaAnticipato
	 */
	public BigDecimal getImportoGiaAnticipato() {
		return importoGiaAnticipato;
	}

	/**
	 * @return the rigaAnticipo
	 */
	public RigaAnticipo getRigaAnticipo() {
		return rigaAnticipo;
	}

	/**
	 * @return the situazioneEffetti
	 */
	public List<SituazioneEffetto> getSituazioneEffetti() {
		return situazioneEffetti;
	}

}
