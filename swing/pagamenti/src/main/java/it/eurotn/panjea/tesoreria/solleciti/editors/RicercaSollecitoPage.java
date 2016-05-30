package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.tesoreria.solleciti.RigaSollecito;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.rich.control.table.JecGroupTable;
import it.eurotn.rich.control.table.JideTableWidget.TableType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ListSelectionModel;

import org.springframework.richclient.command.AbstractCommand;

public class RicercaSollecitoPage extends RicercaSollecitiByEntitaPage {

	public static final String PAGE_ID = "ricercaSollecitoPage";

	/**
	 * Costruttore.
	 * 
	 */
	protected RicercaSollecitoPage() {
		super(PAGE_ID, new RicercaSollecitiTableModel());
		getTable().setTableType(TableType.GROUP);
		getTable().setAggregatedColumns(new String[] { "entita", "sollecito" });
		getTable().getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getStampaInviaSollecitoCommand(), getRefreshCommand() };
	}

	@Override
	public Collection<RigaSollecito> loadTableData() {
		return null;
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	protected void onRefresh() {
		refreshData();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void processTableData(Collection<RigaSollecito> results) {
		super.processTableData(results);
		((JecGroupTable<RigaSollecito>) getTable().getTable()).expandAll();
	}

	@Override
	public Collection<RigaSollecito> refreshTableData() {
		List<RigaSollecito> righeSolleciti = new ArrayList<RigaSollecito>();

		List<Sollecito> solleciti = tesoreriaBD.caricaSolleciti();
		List<Sollecito> sollecitiFilterList = controlfilter(solleciti);

		for (Sollecito sollecito : sollecitiFilterList) {
			righeSolleciti.addAll(sollecito.getRigheSollecito());
		}

		return righeSolleciti;
	}

	@Override
	public void setFormObject(Object object) {
	}
}
