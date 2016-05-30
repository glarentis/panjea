package it.eurotn.panjea.anagrafica.rich.editors.entita;

import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.dialog.MessageDialog;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.dms.manager.allegati.AllegatoDMS;
import it.eurotn.panjea.dms.manager.allegati.AllegatoEntita;
import it.eurotn.panjea.rich.editors.dms.allegati.AbstractAllegatiPage;

public class EntitaAllegatiPage extends AbstractAllegatiPage {

    @Override
    protected AllegatoDMS createAttributoFromFormObject(Object object) {
        Entita entita = (Entita) object;
        return new AllegatoEntita(entita.getEntitaLite().creaEntitaDocumento(), getCodiceAzienda());
    }

    @Override
    protected String getFolder(Object object) {
        Entita entita = (Entita) object;
        return dmsBD.caricaDmsSettings().getFolder(entita);
    }

    @Override
    public boolean onPrePageOpen() {
        boolean initializePage = true;
        if (currentFormObject.isNew()) {
            initializePage = false;
            MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                    .getService(MessageSourceAccessor.class);
            String titolo = messageSourceAccessor.getMessage("entita.null.messageDialog.title", new Object[] {},
                    Locale.getDefault());
            String messaggio = messageSourceAccessor.getMessage(
                    "entita.null.messageDialog.message", new Object[] { messageSourceAccessor
                            .getMessage(currentFormObject.getDomainClassName(), new Object[] {}, Locale.getDefault()) },
                    Locale.getDefault());
            new MessageDialog(titolo, messaggio).showDialog();
        }
        return initializePage;
    }

}
