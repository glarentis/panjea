package it.eurotn.panjea.anagrafica.rich.editors.entita.depositi;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.rich.editors.azienda.depositi.DepositiSedeAziendaTablePage;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;

import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

public class DepositiEntitaTablePage extends DepositiSedeAziendaTablePage {

	private AziendaCorrente aziendaCorrente;

	/**
	 * Costruttore.
	 */
	public DepositiEntitaTablePage() {
		super(new String[] { "codice", "descrizione", "datiGeografici.localita.descrizione", "indirizzo",
				"attivo", "sedeEntita" });

		aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
	}

	@Override
	public boolean onPrePageOpen() {
		boolean initializePage = true;
		if (getEntita().isNew()) {
			initializePage = false;
			MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
					.getService(MessageSourceAccessor.class);
			String titolo = messageSourceAccessor.getMessage("entita.null.messageDialog.title", new Object[] {},
					Locale.getDefault());
			String messaggio = messageSourceAccessor.getMessage("entita.null.messageDialog.message",
					new Object[] { messageSourceAccessor.getMessage(getEntita().getDomainClassName(), new Object[] {},
							Locale.getDefault()) }, Locale.getDefault());
			new MessageDialog(titolo, messaggio).showDialog();
		}
		return initializePage;
	}

	@Override
	public void setFormObject(Object object) {
		AziendaLite aziendaLite = aziendaCorrente.getAziendaLite();
		Azienda azienda = new Azienda();
		azienda.setId(aziendaLite.getId());
		super.setFormObject(azienda);

		setEntita(((Entita) object).getEntitaLite());
	}
}
