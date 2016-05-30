package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta;

import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class DataValutaRow extends EffettoRow {

	/**
	 * Costruttore .
	 *
	 * @param effettiInDataValuta
	 *            effetto descritto dalla riga
	 */
	public DataValutaRow(final Collection<Effetto> effettiInDataValuta) {
		super(PanjeaEJBUtil.cloneObject(effettiInDataValuta.iterator().next()));
		// getEffetto().setDataScadenza(effettiInDataValuta.getDataScadenza());
		// getEffetto().setPagamenti(effettiInDataValuta.getPagamenti());
		// getEffetto().addGiorniBanca(effettiInDataValuta.getGiorniBanca());
		// getEffetto().setDataValuta(effettiInDataValuta.getDataValuta());
		getEffetto().getImporto().setImportoInValuta(BigDecimal.ZERO);
		getEffetto().getImporto().calcolaImportoValutaAzienda(2);
		for (Effetto effetto : effettiInDataValuta) {
			EffettoRow nodeEffetto = new EffettoRow(effetto);
			addChild(nodeEffetto);
		}
	}

	@Override
	public Object addChild(Object obj) {
		Effetto effetto = ((EffettoRow) obj).getEffetto();
		BigDecimal importoTotale = getEffetto().getImporto().getImportoInValutaAzienda();
		getEffetto().getImporto().setImportoInValuta(importoTotale.add(effetto.getImporto().getImportoInValuta()));
		getEffetto().getImporto().calcolaImportoValutaAzienda(2);
		return super.addChild(obj);
	}

	@Override
	public void setValueAt(Object obj, int i) {
		switch (i) {
		case 1:
			getEffetto().setDataValuta((Date) obj);
			for (Object effettoObject : getChildren()) {
				EffettoRow effettoRow = (EffettoRow) effettoObject;
				effettoRow.setValueAt(obj, i);
			}
			break;
		case 2:
			// NPE MAIL: posso cancellare i giorni banca e quindi arriva null
			BigDecimal value = (BigDecimal) obj;
			if (value == null) {
				value = BigDecimal.ZERO;
			}
			Integer giorniBanca = value.intValue();
			getEffetto().addGiorniBanca(giorniBanca);
			for (Object effettoObject : getChildren()) {
				EffettoRow effettoRow = (EffettoRow) effettoObject;
				effettoRow.setValueAt(new BigDecimal(giorniBanca), i);
			}
			break;
		default:
			super.setValueAt(obj, i);
		}
	}
}
