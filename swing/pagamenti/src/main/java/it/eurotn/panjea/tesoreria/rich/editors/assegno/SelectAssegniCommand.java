package it.eurotn.panjea.tesoreria.rich.editors.assegno;

import it.eurotn.panjea.tesoreria.util.AssegnoDTO;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.pivot.AggregateTable;

public class SelectAssegniCommand extends ApplicationWindowAwareCommand {

	public static final String SELECT_COMMAND_ID = "selezionaTuttoCommand";
	public static final String UNSELECT_COMMAND_ID = "deselezionaTuttoCommand";

	private boolean select = true;
	private final RisultatiRicercaAssegniTablePage page;

	/**
	 * Costruttore.
	 * 
	 * @param commandId
	 *            id del comando
	 * @param select
	 *            indica se il command deve selezionare o deselezionare tutte gli assegni
	 * @param page
	 *            pagina che contiene le rate trovate
	 */
	public SelectAssegniCommand(final String commandId, final boolean select,
			final RisultatiRicercaAssegniTablePage page) {
		super(commandId);
		this.select = select;
		this.page = page;
		RcpSupport.configure(this);

	}

	/**
	 * deseleziona le rate selezionate.
	 */
	private void deseleziona() {
		for (AssegnoDTO assegnoDTO : page.getTable().getRows()) {
			if (assegnoDTO.getStatoCarrello() == it.eurotn.panjea.tesoreria.util.AssegnoDTO.StatoCarrello.DA_AGGIUNGERE) {
				assegnoDTO.setStatoCarrello(it.eurotn.panjea.tesoreria.util.AssegnoDTO.StatoCarrello.SELEZIONABILE);
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
		for (AssegnoDTO assegnoDTO : page.getTable().getRows()) {
			if (assegnoDTO.getStatoCarrello() == it.eurotn.panjea.tesoreria.util.AssegnoDTO.StatoCarrello.SELEZIONABILE) {
				assegnoDTO.setStatoCarrello(it.eurotn.panjea.tesoreria.util.AssegnoDTO.StatoCarrello.DA_AGGIUNGERE);
			}
		}
	}

}