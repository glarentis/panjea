package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractEditorDialogPage;

public class DatiGeograficiEditor extends AbstractEditorDialogPage {

	private DatiGeograficiDockingCompositeDialogPage datiGeograficiDockingCompositeDialogPage = null;

	@Override
	protected JecCompositeDialogPage createCompositeDialogPage() {
		if (datiGeograficiDockingCompositeDialogPage == null) {
			datiGeograficiDockingCompositeDialogPage = new DatiGeograficiDockingCompositeDialogPage();
		}
		return datiGeograficiDockingCompositeDialogPage;
	}

}
