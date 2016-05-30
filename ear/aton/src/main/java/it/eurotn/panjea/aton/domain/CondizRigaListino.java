package it.eurotn.panjea.aton.domain;

import it.eurotn.panjea.magazzino.domain.RigaListino;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class CondizRigaListino extends CondizRiga {

	private RigaListino rigaListino;

	/**
	 * Costruttore.
	 * 
	 * @param rigaListino
	 *            riga del listino da esportare.
	 */
	public CondizRigaListino(final RigaListino rigaListino) {
		super();
		this.rigaListino = rigaListino;
	}

	/**
	 * @return chiave di ricerca per il condiz aton
	 */
	@Override
	public String getChiave() {
		// Il codiceListino DEVE essere di 4 caratteri.
		String codiceListino = rigaListino.getVersioneListino().getListino().getCodice() + "     ";
		return codiceListino.substring(0, 4) + rigaListino.getArticolo().getCodice();
	}

	/**
	 * @return codice di ricerca per il condiz aton
	 */
	@Override
	public String getCodiceRicerca() {
		return DatiEsportazioneContratti.TIPORECORD_LISTINO;
	}

	@Override
	public String getCodiceSconto() {
		return "    ";
	}

	@Override
	public Date getDataFine() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2099, 12, 31, 0, 0);
		return calendar.getTime();
	}

	@Override
	public Date getDataInizio() {
		return rigaListino.getVersioneListino().getDataVigore();
	}

	@Override
	public BigDecimal getPrezzo() {
		return rigaListino.getPrezzo();
	}

	@Override
	public BigDecimal getScontoPercentuale() {
		return BigDecimal.ZERO;
	}

}
