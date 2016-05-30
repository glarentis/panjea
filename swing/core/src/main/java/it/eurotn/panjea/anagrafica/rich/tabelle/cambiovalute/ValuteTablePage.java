package it.eurotn.panjea.anagrafica.rich.tabelle.cambiovalute;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.rich.commands.AggiornaTassiCommand;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.bd.IValutaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.util.Calendar;
import java.util.List;
import java.util.Observable;

import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

public class ValuteTablePage extends AbstractTablePageEditor<CambioValuta> {

	private IValutaBD valutaBD;
	private AggiornaTassiCommand aggiornaTassiCommand;

	/**
	 * Costruttore.
	 */
	public ValuteTablePage() {
		super("valuteTablePage", new ValuteTableModel());
		getTable().setDelayForSelection(300);
	}

	/**
	 * @return command per l'aggiornamento dei tassi
	 */
	private AggiornaTassiCommand getAggiornaTassiCommand() {
		if (aggiornaTassiCommand == null) {
			aggiornaTassiCommand = new AggiornaTassiCommand();
			aggiornaTassiCommand.setValutaBD(valutaBD);
			aggiornaTassiCommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand arg0) {
					loadTableData();
				}

				@Override
				public boolean preExecution(ActionCommand arg0) {
					return true;
				}
			});
		}
		return aggiornaTassiCommand;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getAggiornaTassiCommand() };
	}

	@Override
	public List<CambioValuta> loadTableData() {
		return valutaBD.caricaCambiValute(Calendar.getInstance().getTime());
	}

	@Override
	public void onEditorEvent(ApplicationEvent event) {
		// nell'editor delle valute ho due tabelle che gestiscono lo stesso tipo di oggetto
		// devon controllare che la cancellazione provenga da questa tabella.
		PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
		if (panjeaEvent.getSourceContainer() == this) {
			super.onEditorEvent(event);
		} else {
			if (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.DELETED)
					&& panjeaEvent.getObject() instanceof CambioValuta) {
				// Ricarico la valuta perchè potrebbe avermi cancellato il cambio che sto visualizzando
				CambioValuta cambio;
				try {
					cambio = valutaBD.caricaCambioValuta(getTable().getSelectedObject().getValuta().getCodiceValuta(),
							Calendar.getInstance().getTime());
					getTable().replaceOrAddRowObject(getTable().getSelectedObject(), cambio, this);
				} catch (CambioNonPresenteException e) {
					// non ho + la valuta ricarico il tutto
					loadTableData();
				}
			}
		}
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<CambioValuta> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param valutaBD
	 *            The valutaBD to set.
	 */
	public void setValutaBD(IValutaBD valutaBD) {
		this.valutaBD = valutaBD;
	}

	@Override
	public void update(Observable observable, Object obj) {
		firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, getTable().getSelectedObject().getValuta());
		super.update(observable, obj);
	}

}
