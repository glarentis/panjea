package it.eurotn.panjea.anagrafica.rich.tabelle.cambiovalute;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.rich.bd.IValutaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class ValutaPage extends FormBackedDialogPageEditor {
    private IValutaBD valutaBD;

    /**
     * Costruttore.
     */
    public ValutaPage() {
        super("valutaPage", new ValutaForm());
    }

    @Override
    protected Object doDelete() {
        CambioValuta cambio = (CambioValuta) getForm().getFormObject();
        valutaBD.cancellaValutaAzienda(cambio.getValuta());
        return cambio;
    }

    @Override
    protected Object doSave() {
        CambioValuta cambioValuta = (CambioValuta) getBackingFormPage().getFormObject();

        cambioValuta = valutaBD.salvaCambioValuta(cambioValuta);

        return cambioValuta;
    }

    @Override
    public AbstractCommand[] getCommand() {
        return toolbarPageEditor.getDefaultCommand(true);
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {
    }

    /**
     * @param valitaBd
     *            The valitaBd to set.
     */
    public void setValutaBD(IValutaBD valitaBd) {
        this.valutaBD = valitaBd;
    }
}
