package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.dialog.MessageDialog;

public class SediAziendaTablePage extends AbstractTablePageEditor<SedeAzienda> {

	private static final String PAGE_ID = "sediAziendaTablePage";

	private Azienda azienda;
	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 * 
	 */
	public SediAziendaTablePage() {
		super(PAGE_ID, new String[] { "abilitato", "sede.descrizione", "sede.datiGeografici.localita.descrizione",
				"sede.indirizzo", "tipoSede.descrizione" }, SedeAzienda.class);
	}

	@Override
	public List<SedeAzienda> loadTableData() {
		return anagraficaBD.caricaSediSecondarieAzienda(azienda);
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
			String titolo = messageSourceAccessor.getMessage("entita.null.sedeEntita.messageDialog.title",
					new Object[] {}, Locale.getDefault());
			String messaggio = messageSourceAccessor.getMessage("entita.null.sedeEntita.messageDialog.message",
					new Object[] { messageSourceAccessor.getMessage(azienda.getDomainClassName(), new Object[] {},
							Locale.getDefault()) }, Locale.getDefault());
			new MessageDialog(titolo, messaggio).showDialog();
		}
		return initializePage;
	}

	@Override
	public void onRefresh() {
		loadData();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (evt.getNewValue() instanceof SedeAzienda) {
			SedeAzienda sedeAzienda = (SedeAzienda) evt.getNewValue();
			if (sedeAzienda.getTipoSede() != null && sedeAzienda.getTipoSede().isSedePrincipale()) {
				AziendaAnagraficaDTO aziendaAnagraficaAggiornata = new AziendaAnagraficaDTO();
				aziendaAnagraficaAggiornata.setAzienda(azienda);
				aziendaAnagraficaAggiornata.setSedeAzienda(sedeAzienda);
				firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, aziendaAnagraficaAggiornata);
			}
		}
		// // loadData();
	}

	@Override
	public List<SedeAzienda> refreshTableData() {
		return loadTableData();
	}

	/**
	 * 
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
		this.azienda = ((AziendaAnagraficaDTO) object).getAzienda();
		SedeAziendaPage sedeAziendaPage = (SedeAziendaPage) getEditPages().get("defaultObjectClassName");
		sedeAziendaPage.setAzienda(azienda);
	}

}
