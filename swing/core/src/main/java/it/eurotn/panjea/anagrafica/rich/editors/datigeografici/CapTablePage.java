package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.command.AbstractCommand;

public class CapTablePage extends AbstractTablePageEditor<Cap> {

	public static final String PAGE_ID = "capTablePage";
	private IDatiGeograficiBD datiGeograficiBD = null;
	private DatiGeografici datiGeografici = null;

	/**
	 * Costruttore.
	 */
	public CapTablePage() {
		super(PAGE_ID, new String[] { "descrizione" }, Cap.class);
		getTable().setDelayForSelection(200);
	}

	@Override
	public JComponent createToolbar() {
		toolbarCommandGroup = new JECCommandGroup();
		AbstractCommand newCommand = getNewCommands().values().iterator().next();
		toolbarCommandGroup.add(newCommand);
		toolbarCommandGroup.add(getEditorLockCommand());
		toolbarCommandGroup.add(getEditorDeleteCommand());
		JComponent component = toolbarCommandGroup.createToolBar();
		return component;
	}

	@Override
	protected boolean enableChartPanel() {
		return false;
	}

	@Override
	public AbstractCommand[] getCommands() {
		return null;
	}

	/**
	 * @return the datiGeografici
	 */
	public DatiGeografici getDatiGeografici() {
		return datiGeografici;
	}

	/**
	 * @return the datiGeograficiBD
	 */
	public IDatiGeograficiBD getDatiGeograficiBD() {
		return datiGeograficiBD;
	}

	@Override
	public List<Cap> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<Cap> refreshTableData() {
		List<Cap> cap = datiGeograficiBD.caricaCap(datiGeografici);
		return cap;
	}

	/**
	 * @param datiGeografici
	 *            the datiGeografici to set
	 */
	private void setDatiGeografici(DatiGeografici datiGeografici) {
		this.datiGeografici = datiGeografici;
	}

	/**
	 * @param datiGeograficiBD
	 *            the datiGeograficiBD to set
	 */
	public void setDatiGeograficiBD(IDatiGeograficiBD datiGeograficiBD) {
		this.datiGeograficiBD = datiGeograficiBD;
	}

	@Override
	public void setFormObject(Object object) {
		DatiGeografici nuoviDatiGeografici = (DatiGeografici) object;
		setDatiGeografici(nuoviDatiGeografici);
		((CapPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setDatiGeografici(datiGeografici);
	}

}
