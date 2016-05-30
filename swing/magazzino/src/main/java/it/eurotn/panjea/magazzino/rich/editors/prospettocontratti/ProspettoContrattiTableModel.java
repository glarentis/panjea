package it.eurotn.panjea.magazzino.rich.editors.prospettocontratti;

import it.eurotn.panjea.magazzino.rich.editors.contrattisede.ContrattiSedeTableModel;
import it.eurotn.panjea.magazzino.util.ContrattoProspettoDTO;

public class ProspettoContrattiTableModel extends ContrattiSedeTableModel {

	private static final long serialVersionUID = 1143685465808859430L;

	/**
	 * Costruttore.
	 */
	public ProspettoContrattiTableModel() {
		super(ProspettoContrattiTablePage.PAGE_ID, new String[] { "entita", "sedeEntita", "tipoLinkEntita",
				"contratto", "contratto.dataInizio", "contratto.dataFine" }, ContrattoProspettoDTO.class);
	}
}
