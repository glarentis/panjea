package it.eurotn.panjea.magazzino.rich.editors.contratto.agenti;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.rich.dialogs.PanjeaFilterListSelectionDialog;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;

public abstract class AgentiContrattoSelectionDialog extends PanjeaFilterListSelectionDialog {

	protected class AgentiRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 8129263663324775797L;

		/**
		 * Costruttore.
		 * 
		 */
		public AgentiRenderer() {
			super();
		}

		@Override
		public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1) {
			JLabel label = (JLabel) super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
			label.setText(((EntitaLite) obj).getAnagrafica().getDenominazione());
			label.setIcon(RcpSupport.getIcon(Agente.class.getName()));
			return label;
		}
	}

	private class AggiungiTuttiCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "aggiungiTuttiAgentiCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public AggiungiTuttiCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			onSelectAll();
			dispose();
		}

	}

	private AggiungiTuttiCommand aggiungiTuttiCommand;
	private boolean aggiungiTuttiCommandVisible;

	/**
	 * Costruttore.
	 * 
	 * @param filterList
	 *            lista di selezione
	 * @param aggiungiTuttiCommandVisible
	 *            indica se il command "Aggiungi tutti" deve essere visibile o no
	 */
	public AgentiContrattoSelectionDialog(final FilterList<EntitaLite> filterList,
			final boolean aggiungiTuttiCommandVisible) {
		super("Seleziona l'agente da inserire", Application.instance().getActiveWindow().getControl(), filterList);
		setFilterator(GlazedLists.textFilterator("anagrafica.denominazione"));
		setRenderer(new AgentiRenderer());
		this.aggiungiTuttiCommandVisible = aggiungiTuttiCommandVisible;
	}

	/**
	 * @return the aggiungiTuttiCommand
	 */
	public AggiungiTuttiCommand getAggiungiTuttiCommand() {
		if (aggiungiTuttiCommand == null) {
			aggiungiTuttiCommand = new AggiungiTuttiCommand();
		}

		return aggiungiTuttiCommand;
	}

	@Override
	protected String getCancelCommandId() {
		return "cancelAgenteCommand";
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		if (aggiungiTuttiCommandVisible) {
			return (new AbstractCommand[] { getAggiungiTuttiCommand(), getFinishCommand(), getCancelCommand() });
		} else {
			return (new AbstractCommand[] { getFinishCommand(), getCancelCommand() });
		}
	}

	@Override
	protected String getFinishCommandId() {
		return "aggiungiAgenteCommand";
	}

	/**
	 * Metodo chiamato alla selezione di tutti gli agenti.
	 */
	public abstract void onSelectAll();
}
