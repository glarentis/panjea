package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.FormaGiuridica;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * Pagina contenente i dati dell'azienda.
 *
 * @author Aracno
 * @version 1.0, 17-mag-2006
 *
 */
public class DatiAziendaPage extends FormsBackedTabbedDialogPageEditor {

    private static final String PAGE_ID = "datiAziendaPage";
    private IAnagraficaBD anagraficaBD;

    /**
     * Costruttore.
     * 
     */
    public DatiAziendaPage() {
        super(PAGE_ID,
                new AziendaForm(
                        PanjeaFormModelHelper.createFormModel(new AziendaAnagraficaDTO(), false, "aziendaFormModel"),
                        "aziendaForm"));
    }

    @Override
    public void addForms() {
        PersonaFisicaGiuridicaForm personaFisicaGiuridicaForm = new PersonaFisicaGiuridicaForm(
                getBackingFormPage().getFormModel(), "personaFisicaGiuridicaForm");
        addForm(personaFisicaGiuridicaForm);

        // Aggiungo il form del legale rappresentante dell'azienda
        LegaleRappresentanteForm legaleRappresentanteForm = new LegaleRappresentanteForm(
                getBackingFormPage().getFormModel(), "legaleRappresentanteForm");
        addForm(legaleRappresentanteForm);

        // Aggiungo il form dei dati attività dell'azienda
        DatiAttivitaAziendaForm datiAttivitaAziendaForm = new DatiAttivitaAziendaForm(
                getBackingFormPage().getFormModel(), "datiAttivitaAziendaForm");
        addForm(datiAttivitaAziendaForm);
    }

    @Override
    protected Object doSave() {
        AziendaAnagraficaDTO aziendaAnagraficaDTO = (AziendaAnagraficaDTO) getBackingFormPage().getFormObject();
        // FIXME rimuovere l'inizializzazione dei valori a null e spostarli all'interno del metodo restoreNullValue e
        // removeNullValue
        String[] descLingua = aziendaAnagraficaDTO.getAzienda().getLingua().split("-");
        aziendaAnagraficaDTO.getAzienda().setLingua(descLingua[0].trim());

        AziendaAnagraficaDTO aziendaAnagraficaResult = getAnagraficaBD()
                .salvaAziendaAnagrafica(((AziendaAnagraficaDTO) getBackingFormPage().getFormObject()));
        // FIXME rimuovere l'inizializzazione dei valori a null e spostarli all'interno del metodo restoreNullValue e
        // removeNullValue
        getBackingFormPage().setFormObject(aziendaAnagraficaResult);

        PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.MODIFIED,
                aziendaAnagraficaResult);
        Application.instance().getApplicationContext().publishEvent(event);
        return aziendaAnagraficaResult;
    }

    /**
     * @return the anagraficaBD
     */
    public IAnagraficaBD getAnagraficaBD() {
        return anagraficaBD;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
                toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand() };
        return abstractCommands;
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
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
        Azienda azienda = ((AziendaAnagraficaDTO) object).getAzienda();
        if (azienda.getFormaGiuridica() == null) {
            azienda.setFormaGiuridica(new FormaGiuridica());
        }
        super.setFormObject(object);
    }

}