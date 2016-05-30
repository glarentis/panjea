package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria;

import it.eurotn.panjea.rich.editors.DockedEditor;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.RisultatiRicercaAreaTesoreriaPage;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.PartiteAreaTesoreriaPage;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.dialog.DialogPage;

public class AreaTesoreriaEditor extends DockedEditor {

	public class AreaTesoreriaDockingCompositeDialogPage extends DockingCompositeDialogPage {

		/**
		 * Costruttore.
		 * 
		 * @param idPage
		 *            id
		 */
		public AreaTesoreriaDockingCompositeDialogPage(final String idPage) {
			super(idPage);
		}

		@Override
		public void addPage(DialogPage page) {
			super.addPage(page);

			if (RisultatiRicercaAreaTesoreriaPage.PAGE_ID.equals(page.getId())) {
				page.addPropertyChangeListener(RisultatiRicercaAreaTesoreriaPage.AREA_TESORERIA_SELEZIONATA,
						areaTesoreriaListener);
			} else if (PartiteAreaTesoreriaPage.PAGE_ID.equals(page.getId())) {
				partiteAreaTesoreriaPage = (PartiteAreaTesoreriaPage) page;
			}
		}

	}

	public class AreaTesoreriaListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (RisultatiRicercaAreaTesoreriaPage.AREA_TESORERIA_SELEZIONATA.equals(evt.getPropertyName())) {
				AreaTesoreria areaTesoreria = (AreaTesoreria) evt.getNewValue();

				if (partiteAreaTesoreriaPage.isControlCreated()) {
					partiteAreaTesoreriaPage.setFormObject(areaTesoreria);
					partiteAreaTesoreriaPage.refreshData();
				}
			}
		}
	}

	private PartiteAreaTesoreriaPage partiteAreaTesoreriaPage;

	private AreaTesoreriaDockingCompositeDialogPage compositeDialogPage;
	private final AreaTesoreriaListener areaTesoreriaListener = new AreaTesoreriaListener();

	@Override
	protected JecCompositeDialogPage createCompositeDialogPage() {
		if (compositeDialogPage == null) {
			compositeDialogPage = new AreaTesoreriaDockingCompositeDialogPage(getDialogPageId());
		}
		return compositeDialogPage;
	}
}
