package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.util.Assert;

import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RigaTestataForm;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.RigaTestataDTO;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class RigaTestataPage extends FormBackedDialogPageEditor implements InitializingBean {

    public static final String PAGE_ID = "rigaTestataPage";

    private static Logger logger = Logger.getLogger(RigaTestataPage.class);

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private AreaMagazzinoFullDTO areaMagazzinoFullDTO;

    /**
     * Costruttore.
     */
    public RigaTestataPage() {
        super(PAGE_ID, new RigaTestataForm());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(magazzinoDocumentoBD);
    }

    @Override
    protected Object doSave() {
        logger.debug("--> Enter doSave");
        RigaTestata rigaTestata = (RigaTestata) getBackingFormPage().getFormObject();
        rigaTestata.setAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());
        try {
            rigaTestata = (RigaTestata) magazzinoDocumentoBD.salvaRigaMagazzino(rigaTestata);
        } catch (RimanenzaLottiNonValidaException e) {
            // righe lotto non gestite
            throw new IllegalStateException("Righe lotto non gestite sulla riga testata.");
        } catch (RigheLottiNonValideException e) {
            // righe lotto non gestite
            throw new IllegalStateException("Righe lotto non gestite sulla riga testata.");
        } catch (QtaLottiMaggioreException e) {
            // righe lotto non gestite
            throw new IllegalStateException("Righe lotto non gestite sulla riga nota");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("--> Exit doSave " + rigaTestata);
        }
        return rigaTestata;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
                toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
                toolbarPageEditor.getDeleteCommand() };

        return abstractCommands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return super.getEditorNewCommand();
    }

    @Override
    protected Object getNewEditorObject() {
        RigaTestata rigaTestata = (RigaTestata) super.getNewEditorObject();
        rigaTestata.setAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());
        return rigaTestata;
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
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
    }

    /**
     * @param areaMagazzinoFullDTO
     *            areaMagazzinoFullDTO to set
     */
    public void setAreaMagazzinoFullDTO(AreaMagazzinoFullDTO areaMagazzinoFullDTO) {
        this.areaMagazzinoFullDTO = areaMagazzinoFullDTO;
    }

    @Override
    public void setFormObject(Object object) {
        logger.debug("--> SetFormObject " + object);
        RigaTestata rigaTestata = new RigaTestata();

        if (object != null) {
            if (object instanceof RigaTestataDTO) {
                RigaTestataDTO rigaTestataDTO = (RigaTestataDTO) object;
                RigaMagazzino ra = new RigaTestata();
                if (rigaTestataDTO.getId() != null) {
                    ra.setId(rigaTestataDTO.getId());

                    rigaTestata = (RigaTestata) magazzinoDocumentoBD.caricaRigaMagazzino(ra);
                }
            } else if (object instanceof RigaTestata) {
                rigaTestata = (RigaTestata) object;
            }

            RigaTestataPage.super.setFormObject(rigaTestata);
            /*
             * HACK controlla che il FormModel sia valido. se risulta tale viene forzato il commit perche' la
             * setFormObject di questo FormModel imposta l'attributo dirty a true
             */
            if (getBackingFormPage().getFormModel().isValidating()) {
                getBackingFormPage().getFormModel().commit();
            }
        }
    }

    /**
     * @param magazzinoDocumentoBD
     *            magazzinoDocumentoBD to set
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    @Override
    public void updateCommands() {
        super.updateCommands();
        boolean readOnly = getForm().getFormModel().isReadOnly();
        if (readOnly) {
            toolbarPageEditor.getLockCommand().setEnabled(false);
            toolbarPageEditor.getDeleteCommand().setEnabled(false);
        }
    }

}
