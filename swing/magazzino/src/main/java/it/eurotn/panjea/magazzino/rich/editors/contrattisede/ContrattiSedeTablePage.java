package it.eurotn.panjea.magazzino.rich.editors.contrattisede;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.rich.bd.IContrattoBD;
import it.eurotn.panjea.magazzino.util.ContrattoProspettoDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Visualizza i contatti per la sede.
 * 
 * @author giangi
 * @version 1.0, 15/nov/2011
 * 
 */
public class ContrattiSedeTablePage extends AbstractTablePageEditor<ContrattoProspettoDTO> {

	public class OpenEditorContrattoCommand extends ActionCommand {

		@Override
		protected void doExecuteCommand() {
			if (getTable().getSelectedObject() != null) {
				Contratto contratto = contrattoBD.caricaContratto(getTable().getSelectedObject().getContratto(), true);
				LifecycleApplicationEvent event = new OpenEditorEvent(contratto);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}
	}

	public static final String PAGE_ID = "contrattiSedeTablePage";

	private IContrattoBD contrattoBD;

	private SedeEntita sedeEntita;

	/**
	 * Costruttore.
	 */
	public ContrattiSedeTablePage() {
		super(PAGE_ID, new ContrattiSedeTableModel());
		getTable().setTableType(TableType.AGGREGATE);
		getTable().setAggregatedColumns(new String[] { "entita" });
		getTable().setPropertyCommandExecutor(new OpenEditorContrattoCommand());
	}

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            page id
	 * @param tableModel
	 *            model
	 */
	public ContrattiSedeTablePage(final String pageId, final DefaultBeanTableModel<ContrattoProspettoDTO> tableModel) {
		super(pageId, tableModel);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getRefreshCommand() };
	}

	/**
	 * @return Returns the contrattoBD.
	 */
	public IContrattoBD getContrattoBD() {
		return contrattoBD;
	}

	@Override
	public Collection<ContrattoProspettoDTO> loadTableData() {
		Integer idSedeEntita = null;
		if (sedeEntita != null) {
			idSedeEntita = sedeEntita.getId();
		}
		return contrattoBD.caricaContrattoProspetto(idSedeEntita, Calendar.getInstance().getTime());
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<ContrattoProspettoDTO> refreshTableData() {
		return null;
	}

	@Override
	public void restoreState(Settings settings) {
		super.restoreState(settings);
	}

	/**
	 * @param contrattoBD
	 *            The contrattoBD to set.
	 */
	public void setContrattoBD(IContrattoBD contrattoBD) {
		this.contrattoBD = contrattoBD;
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof SedeEntita) {
			sedeEntita = (SedeEntita) object;
		} else {
			sedeEntita = null;
		}
	}

}
