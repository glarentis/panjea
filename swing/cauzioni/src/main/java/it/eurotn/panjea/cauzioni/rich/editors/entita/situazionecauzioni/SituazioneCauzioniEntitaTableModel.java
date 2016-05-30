package it.eurotn.panjea.cauzioni.rich.editors.entita.situazionecauzioni;

import it.eurotn.panjea.cauzioni.rich.bd.ICauzioniBD;
import it.eurotn.panjea.cauzioni.util.parametriricerca.MovimentazioneCauzioneDTO;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniEntitaDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.HierarchicalTableModel;

public class SituazioneCauzioniEntitaTableModel extends DefaultBeanTableModel<SituazioneCauzioniEntitaDTO> implements
		HierarchicalTableModel {

	public static final String MODEL_ID = "situazioneCauzioniEntitaTableModel";

	private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

	private static final long serialVersionUID = 2755540451740444700L;

	private ICauzioniBD cauzioniBD;

	private boolean filtraSediEntita = false;

	/**
	 * Costruttore.
	 */
	public SituazioneCauzioniEntitaTableModel() {
		super(MODEL_ID, new String[] { "sedeEntita", "articolo", "dati", "resi", "saldo", "importoDati", "importoResi",
				"saldoImporto" }, SituazioneCauzioniEntitaDTO.class);
	}

	@Override
	public Object getChildValueAt(int row) {

		SituazioneCauzioniEntitaDTO situazioneCauzioniEntitaDTO = getElementAt(row);

		Set<Integer> idSede = null;
		if (filtraSediEntita) {
			idSede = new TreeSet<Integer>();
			idSede.add(situazioneCauzioniEntitaDTO.getSedeEntita().getId());
		}

		Set<Integer> idEntita = new TreeSet<Integer>();
		idEntita.add(situazioneCauzioniEntitaDTO.getEntitaDocumento().getId());

		Set<Integer> idArticolo = new TreeSet<Integer>();
		idArticolo.add(situazioneCauzioniEntitaDTO.getArticolo().getId());

		List<MovimentazioneCauzioneDTO> movimentazione = cauzioniBD.caricaMovimentazioneCauzioniArticolo(idEntita,
				idSede, idArticolo);

		return movimentazione;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int j) {
		SituazioneCauzioniEntitaDTO situazioneCauzioniEntitaDTO = getElementAt(row);

		switch (j) {
		case 2:
		case 3:
		case 4:
			TOTALE_CONTEXT.setUserObject(situazioneCauzioniEntitaDTO.getNumeroDecimaliQta());
			return TOTALE_CONTEXT;
		case 5:
		case 6:
		case 7:
			TOTALE_CONTEXT.setUserObject(6);
			return TOTALE_CONTEXT;
		default:
			return null;
		}
	}

	@Override
	public boolean hasChild(int arg0) {
		return true;
	}

	@Override
	public boolean isExpandable(int arg0) {
		return true;
	}

	@Override
	public boolean isHierarchical(int arg0) {
		return true;
	}

	/**
	 * @param cauzioniBD
	 *            the cauzioniBD to set
	 */
	public void setCauzioniBD(ICauzioniBD cauzioniBD) {
		this.cauzioniBD = cauzioniBD;
	}

	/**
	 * @param filtraSediEntita
	 *            The filtraSediEntita to set.
	 */
	public void setFiltraSediEntita(boolean filtraSediEntita) {
		this.filtraSediEntita = filtraSediEntita;
	}

}
