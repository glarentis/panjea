package it.eurotn.panjea.anagrafica.rich.editors.entita;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.dialog.MessageDialog;

import it.eurotn.panjea.anagrafica.domain.ContattoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EditFrame;

/**
 * @author Leonardo
 */
public class ContattiSedeEntitaTablePage extends AbstractTablePageEditor<ContattoSedeEntita>
        implements InitializingBean {

    private static final String PAGE_ID = "contattiSedeEntitaTablePage";

    private IAnagraficaBD anagraficaBD;
    private Entita entita;

    /**
     * Costruttore.
     *
     * @param anagraficaBD
     *            anagraficaBD
     */
    public ContattiSedeEntitaTablePage(final IAnagraficaBD anagraficaBD) {
        super(PAGE_ID, new String[] { "contatto", "contatto.email", "contatto.interno", "contatto.cellulare",
                "mansione.descrizione", "sedeEntita", "contatto.fax" }, ContattoSedeEntita.class);
        this.anagraficaBD = anagraficaBD;
        getTable().setAggregatedColumns(new String[] { "sedeEntita" });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public List<ContattoSedeEntita> loadTableData() {

        List<ContattoSedeEntita> contatti = null;

        if (entita != null) {
            contatti = anagraficaBD.caricaContattiSedeEntitaPerEntita(entita);
        }

        return contatti;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        boolean initializePage = true;
        if (entita.isNew()) {
            initializePage = false;
            MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                    .getService(MessageSourceAccessor.class);
            String titolo = messageSourceAccessor.getMessage("entita.null.messageDialog.title", new Object[] {},
                    Locale.getDefault());
            String messaggio = messageSourceAccessor.getMessage(
                    "entita.null.messageDialog.message", new Object[] { messageSourceAccessor
                            .getMessage(entita.getDomainClassName(), new Object[] {}, Locale.getDefault()) },
                    Locale.getDefault());
            new MessageDialog(titolo, messaggio).showDialog();
        }
        return initializePage;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        super.propertyChange(event);
        if (JecCompositeDialogPage.CURRENT_OBJECT_CHANGED_PROPERTY.equals(event.getPropertyName())) {
            if (event.getNewValue() instanceof ContattoSedeEntita) {
                if (((ContattoSedeEntita) event.getNewValue()).getId() != null) {
                    getTable().replaceOrAddRowObject((ContattoSedeEntita) event.getNewValue(),
                            (ContattoSedeEntita) event.getNewValue(), this);
                }
            }
        }
    }

    @Override
    public List<ContattoSedeEntita> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param anagraficaBD
     *            anagraficaBD
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
        this.entita = (Entita) object;
        ((ContattoSedeEntitaPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setEntita(entita);
    }

}
