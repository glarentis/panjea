package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.springframework.richclient.command.AbstractCommand;

public class RigheListinoTablePage extends AbstractTablePageEditor<RigaListinoDTO> {

	private class VersioniListinoTableObserver implements Observer {

		@Override
		public void update(Observable observable, Object obj) {
			versioneListino = (VersioneListino) obj;
			((RigaListinoPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME))
					.setVersioneCorrente(versioneListino);
			refreshData();

			setReadOnly(versioneListino == null);
		}
	}

	public static final String PAGE_ID = "righeListinoTablePage";
	private VersioneListino versioneListino = null;
	private VersioniListinoTableObserver versioniListinoTableObserver = null;
	protected IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
	private Listino listino = null;

	/**
	 * Costruttore.
	 */
	public RigheListinoTablePage() {
		this(PAGE_ID, new RigheListinoTableModel());
		getTable().setDelayForSelection(300);
	}

	/**
	 * Costruttore.
	 * 
	 * @param idPagina
	 *            id della pagina
	 * @param tableModel
	 *            tableModel personalizzato.
	 */
	public RigheListinoTablePage(final String idPagina, final DefaultBeanTableModel<RigaListinoDTO> tableModel) {
		super(idPagina, tableModel);
	}

	@Override
	protected EditFrame<RigaListinoDTO> createEditFrame() {
		return new RigheListinoEditFrame(EEditPageMode.DETAIL, this, EditFrame.QUICK_ACTION_DEFAULT);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getRefreshCommand(), getEditorDeleteCommand() };
	}

	/**
	 * @return the magazzinoAnagraficaBD
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	@Override
	public Object getManagedObject(Object pageObject) {
		if (pageObject instanceof RigaListinoDTO) {
			return magazzinoAnagraficaBD.caricaRigaListino(((RigaListinoDTO) pageObject).getId());
		}
		return super.getManagedObject(pageObject);
	}

	/**
	 * @return VersioniListinoTableObserver
	 */
	private VersioniListinoTableObserver getVersioniListinoTableObserver() {
		if (versioniListinoTableObserver == null) {
			versioniListinoTableObserver = new VersioniListinoTableObserver();
		}
		return versioniListinoTableObserver;
	}

	@Override
	public Collection<RigaListinoDTO> loadTableData() {
		if (listino != null && listino.getId() != null) {
			((RigaListinoPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setListinoCorrente(listino);
		}

		return null;
	}

	@Override
	public void onPostPageOpen() {
		setReadOnly(versioneListino == null);
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	protected void onRefresh() {
		refreshData();
	}

	@Override
	public Collection<RigaListinoDTO> refreshTableData() {
		List<RigaListinoDTO> righe = Collections.emptyList();

		if (versioneListino != null && versioneListino.getId() != null) {
			righe = magazzinoAnagraficaBD.caricaRigheListinoByVersione(versioneListino.getId());
		}

		return righe;
	}

	@Override
	public void setFormObject(Object object) {
		listino = (Listino) object;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	/**
	 * @param versioniListinoTablePage
	 *            pagina delle versioni del listino.
	 */
	public void setVersioniListinoTablePage(VersioniListinoTablePage versioniListinoTablePage) {
		versioniListinoTablePage.getTable().addSelectionObserver(getVersioniListinoTableObserver());
	}
}
