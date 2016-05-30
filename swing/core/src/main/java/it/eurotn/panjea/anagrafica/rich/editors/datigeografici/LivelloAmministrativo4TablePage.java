package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.List;

import javax.swing.JComponent;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.Assert;

public class LivelloAmministrativo4TablePage extends AbstractTablePageEditor<LivelloAmministrativo4> implements
		InitializingBean {

	public static final String PAGE_ID = "livelloAmministrativo4TablePage";
	private IDatiGeograficiBD datiGeograficiBD;
	private DatiGeografici datiGeografici = null;

	/**
	 * Costruttore.
	 */
	public LivelloAmministrativo4TablePage() {
		super(PAGE_ID, new String[] { "nome" }, LivelloAmministrativo4.class);
		getTable().setDelayForSelection(200);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(datiGeograficiBD, "datiGeograficiBD cannot be null!");
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
	public List<LivelloAmministrativo4> loadTableData() {
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
	public List<LivelloAmministrativo4> refreshTableData() {
		List<LivelloAmministrativo4> list = datiGeograficiBD.caricaLivelloAmministrativo4(datiGeografici);
		LivelloAmministrativo4 livelloAmministrativo4 = new LivelloAmministrativo4();
		livelloAmministrativo4.setNome("---- Tutti ----");
		list.add(0, livelloAmministrativo4);
		return list;
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
		((LivelloAmministrativo4Page) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME))
				.setDatiGeografici(datiGeografici);
	}

}
