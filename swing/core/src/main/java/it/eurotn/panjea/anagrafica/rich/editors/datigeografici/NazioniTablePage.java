package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.command.AbstractCommand;

public class NazioniTablePage extends AbstractTablePageEditor<NazioneUI> {

	public static final String PAGE_ID = "nazioniTablePage";
	private IDatiGeograficiBD datiGeograficiBD = null;

	private DatiGeografici datiGeografici = null;

	/**
	 * Costruttore.
	 */
	public NazioniTablePage() {
		super(PAGE_ID, new String[] { "nazione" }, NazioneUI.class);
		getTable().setDelayForSelection(200);
	}

	@Override
	protected JComponent createControl() {
		JComponent control = super.createControl();
		control.setMinimumSize(new Dimension(250, 100));
		control.setMaximumSize(new Dimension(250, 100));
		control.setPreferredSize(new Dimension(250, 100));
		return control;
	}

	@Override
	protected EditFrame<NazioneUI> createEditFrame() {
		return new EditFrame<NazioneUI>(editPageMode, this, EditFrame.QUICK_ACTION_DEFAULT) {
			private static final long serialVersionUID = 6861324909231778873L;

			@Override
			public NazioneUI getTableManagedObject(Object object) {
				return new NazioneUI((Nazione) object);
			}
		};
	}

	@Override
	public JComponent createToolbar() {
		toolbarCommandGroup = new JECCommandGroup();
		toolbarCommandGroup.add(getNewCommands().values().iterator().next());
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
	public List<NazioneUI> loadTableData() {
		List<Nazione> nazioni = datiGeograficiBD.caricaNazioni(null);
		List<NazioneUI> nazioniUI = new ArrayList<NazioneUI>();
		for (Nazione nazione : nazioni) {
			nazioniUI.add(new NazioneUI(nazione));
		}
		return nazioniUI;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public List<NazioneUI> refreshTableData() {
		return null;
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
	}
}
