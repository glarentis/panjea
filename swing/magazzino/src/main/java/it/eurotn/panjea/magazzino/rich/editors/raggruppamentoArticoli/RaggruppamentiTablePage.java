package it.eurotn.panjea.magazzino.rich.editors.raggruppamentoArticoli;

import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.util.Collection;
import java.util.Observable;

public class RaggruppamentiTablePage extends AbstractTablePageEditor<RaggruppamentoArticoli> {
	public static final String PAGE_ID = "raggruppamentiTablePage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public RaggruppamentiTablePage() {
		super(PAGE_ID, new String[] { "descrizione" }, RaggruppamentoArticoli.class);
		getTable().addSelectionObserver(this);
		getTable().setDelayForSelection(300);
	}

	/**
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	@Override
	public Object getManagedObject(Object pageObject) {
		return super.getManagedObject(pageObject);
	}

	@Override
	public Collection<RaggruppamentoArticoli> loadTableData() {
		return magazzinoAnagraficaBD.caricaRaggruppamenti();
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<RaggruppamentoArticoli> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
		if (object != null) {
			// se esiste (!=-1) reimposto l'oggetto ricevuto (che gestisce l'editor) nella tabella.
			int index = getTable().getRows().indexOf(object);
			if (index != -1) {
				getTable().getRows().set(index, (RaggruppamentoArticoli) object);
			}
		}

	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	@Override
	public void update(Observable observable, Object obj) {
		super.update(observable, obj);
		firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, getTable().getSelectedObject());
	}
}
