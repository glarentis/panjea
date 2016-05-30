/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.RapportoBancario;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.List;
import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.dialog.MessageDialog;

/**
 * 
 * @author giangi
 * @version 1.0, 17/nov/06
 * 
 */
public class RapportiAziendaTablePage extends AbstractTablePageEditor<RapportoBancarioAzienda> {

	private static final String PAGE_ID = "rapportiAziendaTablePage";

	private Azienda azienda;
	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 */
	protected RapportiAziendaTablePage() {
		super(PAGE_ID, new String[] { RapportoBancario.PROP_NUMERO, RapportoBancario.PROP_DESCRIZIONE,
				RapportoBancario.PROP_ABILITATO }, RapportoBancarioAzienda.class);
	}

	@Override
	public List<RapportoBancarioAzienda> loadTableData() {
		return anagraficaBD.caricaRapportiBancariAzienda("numero", null);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		boolean initializePage = true;
		if (azienda.isNew()) {
			initializePage = false;
			MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
					.getService(MessageSourceAccessor.class);
			String titolo = messageSourceAccessor.getMessage("entita.null.rapportoBancario.messageDialog.title",
					new Object[] {}, Locale.getDefault());
			String messaggio = messageSourceAccessor.getMessage("entita.null.rapportoBancario.messageDialog.message",
					new Object[] { messageSourceAccessor.getMessage(azienda.getDomainClassName(), new Object[] {},
							Locale.getDefault()) }, Locale.getDefault());
			new MessageDialog(titolo, messaggio).showDialog();
		}
		return initializePage;
	}

	@Override
	public List<RapportoBancarioAzienda> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
		this.azienda = ((AziendaAnagraficaDTO) object).getAzienda();
		((RapportoBancarioAziendaPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setAzienda(azienda);
	}

}
