package it.eurotn.panjea.magazzino.rich.editors.contrattisede;

import it.eurotn.panjea.magazzino.util.ContrattoProspettoDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class ContrattiSedeTableModel extends DefaultBeanTableModel<ContrattoProspettoDTO> {
	private static final long serialVersionUID = -3274477264390860685L;

	/**
	 * Costruttore.
	 */
	public ContrattiSedeTableModel() {
		super(ContrattiSedeTablePage.PAGE_ID, new String[] { "tipoLinkEntita", "contratto", "contratto.dataInizio",
				"contratto.dataFine" }, ContrattoProspettoDTO.class);
	}

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 *            id model
	 * @param columnPropertyNames
	 *            nome proprietà
	 * @param classe
	 *            classe gestita
	 */
	public ContrattiSedeTableModel(final String modelId, final String[] columnPropertyNames,
			final Class<ContrattoProspettoDTO> classe) {
		super(modelId, columnPropertyNames, classe);
	}

}
