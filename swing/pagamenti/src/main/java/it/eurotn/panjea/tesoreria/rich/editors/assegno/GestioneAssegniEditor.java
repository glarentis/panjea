/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.assegno;

import it.eurotn.panjea.rich.editors.RicercaEditor;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.RisultatiRicercaAreaTesoreriaPage;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.PartiteAreaTesoreriaPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.dialog.DialogPage;

/**
 * @author leonardo
 *
 */
public class GestioneAssegniEditor extends RicercaEditor {

	public class AreaTesoreriaListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (RisultatiRicercaAreaTesoreriaPage.AREA_TESORERIA_SELEZIONATA.equals(evt.getPropertyName())) {
				AreaTesoreria areaTesoreria = (AreaTesoreria) evt.getNewValue();

				if (partiteAreaTesoreriaPage.isControlCreated()) {
					partiteAreaTesoreriaPage.setFormObject(areaTesoreria);
					partiteAreaTesoreriaPage.loadData();
				}
			}
		}
	}

	public class GestioneAssegniDockingCompositeDialogPage extends RicercaDockingCompositeDialogPage {

		/**
		 * Costruttore.
		 *
		 * @param idPage
		 *            id
		 */
		public GestioneAssegniDockingCompositeDialogPage(final String idPage) {
			super(idPage);
		}

		@Override
		public void addPage(DialogPage page) {
			super.addPage(page);

			if (RisultatiRicercaAssegniTablePage.PAGE_ID.equals(page.getId())) {
				page.addPropertyChangeListener(RisultatiRicercaAssegniTablePage.AREA_TESORERIA_SELEZIONATA,
						areaTesoreriaListener);
			} else if (PartiteAreaTesoreriaPage.PAGE_ID.equals(page.getId())) {
				partiteAreaTesoreriaPage = (PartiteAreaTesoreriaPage) page;
			}
		}

	}

	private PartiteAreaTesoreriaPage partiteAreaTesoreriaPage;
	private GestioneAssegniDockingCompositeDialogPage compositeDialogPage;
	private final AreaTesoreriaListener areaTesoreriaListener = new AreaTesoreriaListener();

	@Override
	protected JecCompositeDialogPage createCompositeDialogPage() {
		if (compositeDialogPage == null) {
			compositeDialogPage = new GestioneAssegniDockingCompositeDialogPage(getDialogPageId());
		}
		return compositeDialogPage;
	}

}
