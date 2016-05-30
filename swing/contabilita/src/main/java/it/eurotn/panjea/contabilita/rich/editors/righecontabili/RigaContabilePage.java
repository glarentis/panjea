package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.RigaCentroCosto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class RigaContabilePage extends FormBackedDialogPageEditor implements InitializingBean {

    private static Logger logger = Logger.getLogger(RigaContabilePage.class);
    public static final String PAGE_ID = "rigaContabilePage";
    private IContabilitaBD contabilitaBD;
    private AziendaCorrente aziendaCorrente;
    private AreaContabile areaContabile;

    private RigheContabiliTableModel righeContabiliTableModel;

    /**
     * Costruttore.
     */
    public RigaContabilePage() {
        super(PAGE_ID, new RigaContabileForm());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ((RigaContabileForm) getForm()).setAziendaCorrente(aziendaCorrente);
        ((RigaContabileForm) getForm()).setContabilitaBD(contabilitaBD);
    }

    @Override
    protected Object doDelete() {
        RigaContabile rigaContabile = (RigaContabile) getBackingFormPage().getFormObject();
        AreaContabile areaContabileDelete = contabilitaBD.cancellaRigaContabile(rigaContabile);
        rigaContabile.setAreaContabile(areaContabileDelete);
        return rigaContabile;
    }

    @Override
    protected Object doSave() {
        logger.debug("--> Salvo la riga contabile.");
        RigaContabile rigaContabile = (RigaContabile) getBackingFormPage().getFormObject();
        // reimposto l'area contabile per sicurezza,se ho aperto un documento senza righe iva e non premo nuovo mi
        // rimane la riga senza area
        rigaContabile.setAreaContabile(areaContabile);
        // Devo settare la riga contabile alle righe dei centro di costo
        for (RigaCentroCosto rigaCentroCosto : rigaContabile.getRigheCentroCosto()) {
            rigaCentroCosto.setRigaContabile(rigaContabile);
        }
        for (RigaRateoRisconto rigaRateoRisconto : rigaContabile.getRigheRateoRisconto()) {
            rigaRateoRisconto.setRigaContabile(rigaContabile);
        }
        if (rigaContabile.getAreaContabile() == null) {
            logger.error("-->NV WARNING , Area Contabile null ");
        }
        return contabilitaBD.salvaRigaContabile(rigaContabile);
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] toolbarCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };

        return toolbarCommands;
    }

    /**
     * @return Returns the contabilitaBD.
     */
    public IContabilitaBD getContabilitaBD() {
        return contabilitaBD;
    }

    @Override
    protected boolean insertControlInScrollPane() {
        return true;
    }

    @Override
    public void loadData() {
    }

    @Override
    public ILock onLock() {
        ((RigaContabile) getBackingFormPage().getFormObject()).setAreaContabile(areaContabile);
        return super.onLock();
    }

    @Override
    public void onNew() {
        super.onNew();

        BigDecimal importo = BigDecimal.ZERO;

        if (righeContabiliTableModel.getRowCount() == 0) {
            importo = areaContabile != null ? areaContabile.getDocumento().getTotale().getImportoInValutaAzienda()
                    : BigDecimal.ZERO;
        } else {
            importo = righeContabiliTableModel.getSbilancio();
        }

        if (importo != null && importo.compareTo(BigDecimal.ZERO) != 0) {
            getBackingFormPage().getFormModel().getValueModel("importo").setValue(importo.abs());
        }
        getBackingFormPage().getFormModel().getValueModel("areaContabile").setValue(areaContabile);
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
     * @param areaContabile
     *            The areaContabile to set.
     */
    public void setAreaContabile(AreaContabile areaContabile) {
        this.areaContabile = areaContabile;
        if (areaContabile == null) {
            logger.error("-->WARNING , Area Contabile null ");
            System.err.println("-->WARNING , Area Contabile null ");
        }
        ((RigaContabileForm) getForm()).setAreaContabile(areaContabile);
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param contabilitaBD
     *            The contabilitaBD to set.
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    @Override
    public void setFormObject(Object object) {
        RigaContabile r = (RigaContabile) object;
        if (r.isNew()) {
            return;
        }

        // ricarico valori lazy
        try {
            r = contabilitaBD.caricaRigaContabile(r.getId());
        } catch (Exception ex) {
            System.err.println(ex);
        }
        super.setFormObject(r);
    }

    /**
     * @param righeContabiliTableModel
     *            The righeContabiliTableModel to set.
     */
    public void setRigheContabiliTableModel(RigheContabiliTableModel righeContabiliTableModel) {
        ((RigaContabileForm) getForm()).setRigheContabiliTableModel(righeContabiliTableModel);
        this.righeContabiliTableModel = righeContabiliTableModel;
    }
}