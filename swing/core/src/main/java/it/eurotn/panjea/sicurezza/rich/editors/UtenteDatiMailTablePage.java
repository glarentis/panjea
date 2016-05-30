package it.eurotn.panjea.sicurezza.rich.editors;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

import org.springframework.richclient.dialog.MessageDialog;

public class UtenteDatiMailTablePage extends AbstractTablePageEditor<DatiMail> {

    private ISicurezzaBD sicurezzaBD;

    private Utente utente;

    protected UtenteDatiMailTablePage() {
        super("utenteDatiMailTablePage", new String[] { "nomeAccount", "email", "predefinito" }, DatiMail.class);
    }

    @Override
    public Collection<DatiMail> loadTableData() {
        return sicurezzaBD.caricaDatiMail(utente.getId());
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        if (utente == null || utente.isNew()) {
            new MessageDialog("Attenzione", "Salvare l'utente prima di inserire i dati relativi alla mail.")
                    .showDialog();
        }
        return utente != null && !utente.isNew();
    }

    @Override
    public Collection<DatiMail> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        utente = (Utente) object;
        ((UtenteDatiMailPage) getEditFrame().getCurrentEditPage()).setUtente(utente);
    }

    /**
     * @param sicurezzaBD
     *            the sicurezzaBD to set
     */
    public void setSicurezzaBD(ISicurezzaBD sicurezzaBD) {
        this.sicurezzaBD = sicurezzaBD;
    }

}
