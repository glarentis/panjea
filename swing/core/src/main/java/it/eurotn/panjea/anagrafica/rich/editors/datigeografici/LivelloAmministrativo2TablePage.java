package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.List;

import javax.swing.JComponent;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.Assert;

public class LivelloAmministrativo2TablePage extends AbstractTablePageEditor<LivelloAmministrativo2> implements
		InitializingBean {

	public static final String PAGE_ID = "livelloAmministrativo2TablePage";
	private IDatiGeograficiBD datiGeograficiBD;
	private DatiGeografici datiGeografici = null;

	/**
	 * Costruttore.
	 */
	public LivelloAmministrativo2TablePage() {
		super(PAGE_ID, new String[] { "nome" }, LivelloAmministrativo2.class);
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
	public List<LivelloAmministrativo2> loadTableData() {
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
	public List<LivelloAmministrativo2> refreshTableData() {
		List<LivelloAmministrativo2> list = datiGeograficiBD.caricaLivelloAmministrativo2(datiGeografici);
		LivelloAmministrativo2 livelloAmministrativo2 = new LivelloAmministrativo2();
		livelloAmministrativo2.setNome("---- Tutti ----");
		list.add(0, livelloAmministrativo2);
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
		((LivelloAmministrativo2Page) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME))
				.setDatiGeografici(datiGeografici);
	}
}
