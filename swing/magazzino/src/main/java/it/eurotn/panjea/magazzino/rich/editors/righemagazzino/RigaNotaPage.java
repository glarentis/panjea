package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.util.Assert;

import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RigaNotaForm;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.RigaNotaDTO;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class RigaNotaPage extends FormBackedDialogPageEditor implements InitializingBean {

    private static final String PAGE_ID = "rigaNotaPage";

    private static Logger logger = Logger.getLogger(RigaNotaPage.class);

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private AreaMagazzinoFullDTO areaMagazzinoFullDTO;

    /**
     * Costruttore.
     */
    public RigaNotaPage() {
        super(PAGE_ID, new RigaNotaForm());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(magazzinoDocumentoBD);
    }

    @Override
    protected Object doSave() {
        logger.debug("--> Enter doSave");
        RigaNota rigaNota = (RigaNota) getBackingFormPage().getFormObject();
        rigaNota.setAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());
        try {
            rigaNota = (RigaNota) magazzinoDocumentoBD.salvaRigaMagazzino(rigaNota);
        } catch (RimanenzaLottiNonValidaException e) {
            // righe lotto non gestite
            throw new IllegalStateException("Righe lotto non gestite sulla riga nota");
        } catch (RigheLottiNonValideException e) {
            // righe lotto non gestite
            throw new IllegalStateException("Righe lotto non gestite sulla riga nota");
        } catch (QtaLottiMaggioreException e) {
            // righe lotto non gestite
            throw new IllegalStateException("Righe lotto non gestite sulla riga nota");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("--> Exit doSave " + rigaNota);
        }
        return rigaNota;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
        return abstractCommands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return super.getEditorNewCommand();
    }

    @Override
    protected Object getNewEditorObject() {
        RigaNota rigaNota = (RigaNota) super.getNewEditorObject();
        rigaNota.setAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());
        return rigaNota;
    }

    @Override
    protected boolean insertControlInScrollPane() {
        return false;
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
     * @param areaMagazzinoFullDTO
     *            The areaMagazzinoFullDTO to set.
     */
    public void setAreaMagazzinoFullDTO(AreaMagazzinoFullDTO areaMagazzinoFullDTO) {
        this.areaMagazzinoFullDTO = areaMagazzinoFullDTO;

        // Potrebbe venire salvata dalla riga se cambia stato
        // quindi "preparo" l'area magazzino per un eventuale salvataggio.
        RigaNota rigaNota = (RigaNota) getBackingFormPage().getFormObject();
        rigaNota.setAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());
    }

    @Override
    public void setFormObject(Object object) {
        logger.debug("--> SetFormObject " + object);
        RigaNota rigaNota = new RigaNota();
        // la setFormObject mi setta rigaNotaDTO se viene dalla table o
        // rigaArticolo se modifico o salvo una
        // nuova riga; se dto devo caricare la rigaNota altrimenti la imposto
        // come formObject
        if (object != null) {
            if (object instanceof RigaNotaDTO) {
                RigaNotaDTO rigaNotaDTO = (RigaNotaDTO) object;
                RigaMagazzino ra = new RigaNota();
                if (rigaNotaDTO.getId() != null) {
                    ra.setId(rigaNotaDTO.getId());

                    rigaNota = (RigaNota) magazzinoDocumentoBD.caricaRigaMagazzino(ra);
                }
            } else if (object instanceof RigaNota) {
                rigaNota = (RigaNota) object;
            }

            RigaNotaPage.super.setFormObject(rigaNota);
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
}
