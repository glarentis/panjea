package it.eurotn.panjea.magazzino.rich.editors.sottocontocontabilizzazione;

import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;

import org.springframework.richclient.command.AbstractCommand;

public class SottoContoContabilizzazioneRicaviPage extends SottoContoContabilizzazionePage {

	/**
	 * Costruttore.
	 */
	public SottoContoContabilizzazioneRicaviPage() {
		super(ETipoEconomico.RICAVO);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

}
