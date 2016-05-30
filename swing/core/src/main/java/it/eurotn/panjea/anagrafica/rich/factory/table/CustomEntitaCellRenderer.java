package it.eurotn.panjea.anagrafica.rich.factory.table;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.rich.factory.table.AbstractCustomTableCellRenderer;

import javax.swing.SwingConstants;

public class CustomEntitaCellRenderer extends AbstractCustomTableCellRenderer {

	private static final long serialVersionUID = -6279343292063921287L;

	/**
	 * Costruttore.
	 * 
	 */
	public CustomEntitaCellRenderer() {
		super(SwingConstants.LEFT);
	}

	@Override
	public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
		if (value != null) {
			return value.getClass().getName();
		} else {
			return null;
		}
	}

	@Override
	public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {
		String denominazioneEntita = "";
		if (value != null) {
			if (value instanceof Entita) {
				Entita entita = (Entita) value;
				denominazioneEntita = entita.getAnagrafica().getDenominazione();
			} else if (value instanceof EntitaLite) {
				EntitaLite entita = (EntitaLite) value;
				denominazioneEntita = entita.getAnagrafica().getDenominazione();
			} else if (value instanceof RapportoBancarioAzienda) {
				RapportoBancarioAzienda rapportoBancarioAzienda = (RapportoBancarioAzienda) value;
				denominazioneEntita = rapportoBancarioAzienda.getDescrizione();
			} else if (value instanceof AziendaCorrente) {
				AziendaCorrente azienda = (AziendaCorrente) value;
				denominazioneEntita = azienda.getDenominazione();
			}
		}

		return denominazioneEntita;
	}
}
