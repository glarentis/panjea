package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype;

import org.springframework.richclient.form.Form;

import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaBodyType;

public abstract class AbstractAreaFatturaElettronicaBodyPage extends FormsBackedTabbedDialogPageEditor {

    /**
     *
     * Costruttore.
     *
     * @param parentPageId
     *            id pagina
     * @param backingFormPage
     *            form
     */
    public AbstractAreaFatturaElettronicaBodyPage(final String parentPageId, final Form backingFormPage) {
        super(parentPageId, backingFormPage);
    }

    protected abstract IFatturaElettronicaBodyType getFatturaElettronicaBodyTypeToSave();

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public void refreshData() {
    }

    /**
     * @param idAreaMagazzino
     *            the idAreaMagazzino to set
     */
    public abstract void setIdAreaMagazzino(Integer idAreaMagazzino);

}
