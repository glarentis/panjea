package it.eurotn.panjea.bi.rich.editors.analisi.commands;

import it.eurotn.rich.command.JideToggleCommand;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.dialog.AbstractDialogPage;

public class VisualizzaPageToggleCommand extends JideToggleCommand implements PropertyChangeListener {
	public static final String COMMAND_ID = "DWVisualizzaDettaglioCommand";

	private final AbstractDialogPage page;

	/**
	 * Costruttore.
	 *
	 * @param page
	 *            pagina di cui gestire la visualizzazione
	 * @param commandId
	 *            id del comando
	 */
	public VisualizzaPageToggleCommand(final AbstractDialogPage page, final String commandId) {
		super(commandId);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(commandId);
		c.configure(this);
		this.page = page;
		page.addPropertyChangeListener(this);
		this.setSelected(this.page.isVisible());
	}

	@Override
	protected void onDeselection() {
		page.setVisible(false);
	}

	@Override
	protected void onSelection() {
		page.setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("visible")) {
			this.setSelected(this.page.isVisible());
		}
	}

	@Override
	protected boolean requestSetSelection(boolean selected) {
		return super.requestSetSelection(selected);
	}

}
