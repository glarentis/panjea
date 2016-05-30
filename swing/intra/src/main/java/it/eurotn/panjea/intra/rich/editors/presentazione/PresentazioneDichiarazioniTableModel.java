package it.eurotn.panjea.intra.rich.editors.presentazione;

import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

public class PresentazioneDichiarazioniTableModel extends DefaultBeanEditableTableModel<DichiarazioniIntraPM> {

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public PresentazioneDichiarazioniTableModel() {
		super("presentazioneDichiarazioniTableModel", new String[] { "selezionata",
				"dichiarazioneIntra.tipoDichiarazione", "dichiarazioneIntra.anno", "dichiarazioneIntra.data",
				"dichiarazioneIntra.mese", "dichiarazioneIntra.trimestre", "dichiarazioneIntra.codice" },
				DichiarazioniIntraPM.class);
	}

	@Override
	protected boolean isAllowInsert() {
		return false;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 0;
	}
}
