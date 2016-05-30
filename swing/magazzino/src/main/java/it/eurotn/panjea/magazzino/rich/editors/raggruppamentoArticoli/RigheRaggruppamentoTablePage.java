package it.eurotn.panjea.magazzino.rich.editors.raggruppamentoArticoli;

import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.Collection;
import java.util.Collections;
import java.util.Observable;
import java.util.Set;

public class RigheRaggruppamentoTablePage extends AbstractTablePageEditor<RigaRaggruppamentoArticoli> {

	public static final String PAGE_ID = "righeRaggruppamentoTablePage";
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private RaggruppamentoArticoli raggruppamentoCorrente;

	/**
	 * Costruttore.
	 */
	public RigheRaggruppamentoTablePage() {
		super(PAGE_ID, new RigheRaggruppamentoArticoliTableModel(PAGE_ID));
		getTable().addSelectionObserver(this);
	}

	/**
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	@Override
	public Collection<RigaRaggruppamentoArticoli> loadTableData() {
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
	protected void onRefresh() {
		refreshTableData();
	}

	@Override
	public Collection<RigaRaggruppamentoArticoli> refreshTableData() {
		Set<RigaRaggruppamentoArticoli> raggruppamenti = Collections.emptySet();

		if (raggruppamentoCorrente.getId() != null) {
			raggruppamenti = magazzinoAnagraficaBD.caricaRigheRaggruppamento(raggruppamentoCorrente);
		}

		return raggruppamenti;
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof RaggruppamentoArticoli) {
			raggruppamentoCorrente = (RaggruppamentoArticoli) object;
			((RigaRaggruppamentoArticoliPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME))
					.setRaggruppamentoCorrente(raggruppamentoCorrente);
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
		RigaRaggruppamentoArticoli rigaRaggruppamento = getTable().getSelectedObject();
		if (rigaRaggruppamento != null) {
			firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, rigaRaggruppamento.getRaggruppamento());
		}
	}
}
