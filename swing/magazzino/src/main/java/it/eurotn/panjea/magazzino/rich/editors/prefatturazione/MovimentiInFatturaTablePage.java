package it.eurotn.panjea.magazzino.rich.editors.prefatturazione;

import it.eurotn.panjea.magazzino.rich.editors.areamagazzino.RisultatiRicercaAreaMagazzinoTablePage;

import org.springframework.richclient.command.AbstractCommand;

public class MovimentiInFatturaTablePage extends RisultatiRicercaAreaMagazzinoTablePage {

	@Override
	public AbstractCommand[] getCommands() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getOpenAreaMagazzinoEditor() };
		return abstractCommands;
	}
}
