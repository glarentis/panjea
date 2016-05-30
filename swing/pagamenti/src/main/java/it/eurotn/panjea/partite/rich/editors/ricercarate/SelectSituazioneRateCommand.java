package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.panjea.tesoreria.util.SituazioneRata.StatoCarrello;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.pivot.AggregateTable;

public class SelectSituazioneRateCommand extends ApplicationWindowAwareCommand {

	public static final String SELECT_COMMAND_ID = "selezionaTuttoCommand";
	public static final String UNSELECT_COMMAND_ID = "deselezionaTuttoCommand";

	private boolean select = true;
	private final RisultatiRicercaRatePage page;

	/**
	 * Costruttore.
	 * 
	 * @param commandId
	 *            id del comando
	 * @param select
	 *            indica se il command deve selezionare o deselezionare tutte le rate
	 * @param page
	 *            pagina che contiene le rate trovate
	 */
	public SelectSituazioneRateCommand(final String commandId, final boolean select, final RisultatiRicercaRatePage page) {
		super(commandId);
		this.select = select;
		this.page = page;
		RcpSupport.configure(this);

	}

	/**
	 * deseleziona le rate selezionate.
	 */
	private void deseleziona() {
		for (SituazioneRata situazioneRata : page.getTable().getRows()) {
			if (situazioneRata.getStatoCarrello() == StatoCarrello.DA_AGGIUNGERE) {
				situazioneRata.setStatoCarrello(StatoCarrello.SELEZIONABILE);
			}
		}

	}

	@Override
	protected void doExecuteCommand() {
		if (select) {
			seleziona();
		} else {
			deseleziona();
		}
		((AggregateTable) page.getTable().getTable()).getAggregateTableModel().fireTableDataChanged();
	}

	/**
	 * seleziona le rate.
	 */
	private void seleziona() {
		for (SituazioneRata situazioneRata : page.getTable().getRows()) {
			if (situazioneRata.getStatoCarrello() == StatoCarrello.SELEZIONABILE) {
				situazioneRata.setStatoCarrello(StatoCarrello.DA_AGGIUNGERE);
			}
		}
	}

}