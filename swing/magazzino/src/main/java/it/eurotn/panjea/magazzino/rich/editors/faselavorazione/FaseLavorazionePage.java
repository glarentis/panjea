package it.eurotn.panjea.magazzino.rich.editors.faselavorazione;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class FaseLavorazionePage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "faseLavorazionePage";

    private static Logger logger = Logger.getLogger(FaseLavorazionePage.class);

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

    /**
     * Costruttore.
     */
    public FaseLavorazionePage() {
        super(PAGE_ID, new FaseLavorazioneForm());
    }

    @Override
    protected Object doDelete() {
        magazzinoAnagraficaBD.cancellaFaseLavorazione((FaseLavorazione) getBackingFormPage().getFormObject());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        logger.debug("--> Enter doSave");
        FaseLavorazione faseLavorazione = (FaseLavorazione) getForm().getFormObject();
        faseLavorazione = magazzinoAnagraficaBD.salvaFaseLavorazione(faseLavorazione);
        logger.debug("--> Exit doSave");
        return faseLavorazione;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return toolbarPageEditor.getDefaultCommand(true);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void setFormObject(Object object) {
        FaseLavorazione fase = (FaseLavorazione) object;
        if (!fase.isNew()) {
            fase = magazzinoAnagraficaBD.caricaFaseLavorazione(fase.getId());
        }
        super.setFormObject(PanjeaSwingUtil.cloneObject(fase));
    }

    /**
     * @param magazzinoAnagraficaBD
     *            The magazzinoAnagraficaBD to set.
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

}
