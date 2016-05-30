package it.eurotn.panjea.magazzino.rich.editors.categoriacommerciale;

import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;

import org.springframework.richclient.settings.Settings;

import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;

public class CategoriaCommercialeArticoloTablePage extends AbstractTablePageEditor<CategoriaCommercialeArticolo> {

	public static final String PAGE_ID = "categoriaCommercialeArticoloTablePage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public CategoriaCommercialeArticoloTablePage() {
		super(PAGE_ID, new String[] { "codice" }, CategoriaCommercialeArticolo.class);
	}

	@Override
	protected EditFrame<CategoriaCommercialeArticolo> createEditFrame() {
		EditFrame<CategoriaCommercialeArticolo> editFrame = super.createEditFrame();
		editFrame.getEditViewTypeCommand().setVisible(false);
		editFrame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
		editFrame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
		editFrame.getContext().setCurrentDockSide(DockContext.DOCK_SIDE_EAST);
		holderPanel.getDockingManager().resetToDefault();
		return editFrame;
	}

	/**
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	@Override
	public Collection<CategoriaCommercialeArticolo> loadTableData() {
		return magazzinoAnagraficaBD.caricaCategorieCommercialeArticolo("codice", null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public Collection<CategoriaCommercialeArticolo> refreshTableData() {
		return null;
	}

	@Override
	public void restoreState(Settings settings) {
		super.restoreState(settings);
		DockableFrame editFrame = holderPanel.getDockingManager().getFrame(EditFrame.EDIT_FRAME_ID);
		editFrame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
		editFrame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
		editFrame.getContext().setCurrentDockSide(DockContext.DOCK_SIDE_EAST);
		holderPanel.getDockingManager().resetToDefault();
	}

	@Override
	public void setFormObject(Object object) {
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
