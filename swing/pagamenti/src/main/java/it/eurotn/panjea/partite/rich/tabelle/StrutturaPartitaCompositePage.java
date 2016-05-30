package it.eurotn.panjea.partite.rich.tabelle;

import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartitaLite;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.rich.dialog.TabbedCompositeDialogPage;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.springframework.richclient.dialog.DialogPage;

public class StrutturaPartitaCompositePage extends TabbedCompositeDialogPage implements Observer,
		PropertyChangeListener {

	private static final String PAGE_ID = "strutturaPartitaCompositePage";
	private IPartiteBD partiteBD = null;

	/**
	 * Costruttore.
	 */
	public StrutturaPartitaCompositePage() {
		super(PAGE_ID);
	}

	@Override
	public void addPage(DialogPage page) {
		if (StrutturaPartitaPage.PAGE_ID.equals(page.getId())) {
			page.addPropertyChangeListener(FormBackedDialogPageEditor.OBJECT_CHANGED, this);
		}
		super.addPage(page);
	}

	/**
	 * @return the partiteBD
	 */
	public IPartiteBD getPartiteBD() {
		return partiteBD;
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertychangeevent) {
		update(null, propertychangeevent.getNewValue());
	}

	@Override
	public void setIdPages(List<String> idPages) {
		for (String pagId : idPages) {
			addPage(pagId);
		}
	}

	/**
	 * @param partiteBD
	 *            the partiteBD to set
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}

	@Override
	public void update(Observable o, Object object) {
		if (object instanceof StrutturaPartitaLite) {
			StrutturaPartitaLite strutturaPartitaLite = (StrutturaPartitaLite) object;
			object = partiteBD.caricaStrutturaPartita(strutturaPartitaLite.getId());
		}

		this.setCurrentObject(object);
		this.objectChange(object, this);

		StrutturaPartita strutturaPartita = (StrutturaPartita) object;

		disableAllTabs();
		changeEnableTab(StrutturaPartitaPage.PAGE_ID, true);

		if (strutturaPartita != null && strutturaPartita.getId() != null) {
			enableAllTabs();
		}

	}

}
