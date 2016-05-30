package it.eurotn.panjea.magazzino.rich.editors.sottocontocontabilizzazione;

import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;

import org.springframework.richclient.command.AbstractCommand;

public class SottoContoContabilizzazioneCostiPage extends SottoContoContabilizzazionePage {

	/**
	 * Costruttore.
	 */
	public SottoContoContabilizzazioneCostiPage() {
		super(ETipoEconomico.COSTO);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}
}
