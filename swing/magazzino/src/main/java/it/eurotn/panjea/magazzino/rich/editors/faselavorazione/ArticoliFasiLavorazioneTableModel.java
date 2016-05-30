package it.eurotn.panjea.magazzino.rich.editors.faselavorazione;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class ArticoliFasiLavorazioneTableModel extends DefaultBeanTableModel<FaseLavorazioneArticolo> {
	private static final long serialVersionUID = 6808005900035886878L;

	public ArticoliFasiLavorazioneTableModel() {
		super("articolifasilavorazionetablemodel", new String[] { "articolo", "configurazioneDistinta",
				"componente.articolo" }, FaseLavorazioneArticolo.class);
	}
}
