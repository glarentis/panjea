/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import java.util.Collection;
import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.MessageDialog;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 *
 * @author Aracno
 * @version 1.0, 02/ott/06
 *
 */
public class FigliBeneAmmortizzabileTablePage extends AbstractTablePageEditor<BeneAmmortizzabile> {

	private static final String PAGE_ID = "figliBeneAmmortizzabileTablePage";

	private BeneAmmortizzabile beneAmmortizzabilePadre;
	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 *
	 */
	public FigliBeneAmmortizzabileTablePage() {
		super(PAGE_ID, new FigliBeneAmmortizzabileTableModel());
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getRefreshCommand() };
	}

	@Override
	public Collection<BeneAmmortizzabile> loadTableData() {
		return beniAmmortizzabiliBD.caricaBeniAmmortizzabiliFigli(beneAmmortizzabilePadre);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		boolean initializePage = true;
		if (beneAmmortizzabilePadre.isNew()) {
			initializePage = false;
			MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
					.getService(MessageSourceAccessor.class);
			String titolo = messageSourceAccessor.getMessage("beneAmmortizzabile.null.beniFigli.messageDialog.title",
					new Object[] {}, Locale.getDefault());
			String messaggio = messageSourceAccessor.getMessage(
					"beneAmmortizzabile.null.beniFigli.messageDialog.message", new Object[] {}, Locale.getDefault());
			new MessageDialog(titolo, messaggio).showDialog();
		}
		return initializePage;
	}

	@Override
	public Collection<BeneAmmortizzabile> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param beniAmmortizzabiliBD
	 *            the beniAmmortizzabiliBD to set
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	public void setFormObject(Object object) {
		beneAmmortizzabilePadre = (BeneAmmortizzabile) object;
	}
}
