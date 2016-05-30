package it.eurotn.panjea.contabilita.rich.editors.areacontabile;

import javax.swing.Icon;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonLabelInfo;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.rich.bd.ContabilitaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.editors.areacontabile.datiritenutaacconto.DatiRitenutaAccontoForm;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

/**
 * @author fattazzo
 *
 */
public class DatiRitenutaAccontoCommand extends ActionCommand {

    public static final String COMMAND_ID = "DatiRitenutaAccontoCommand";

    private AreaContabile areaContabile;

    private DatiRitenutaAccontoForm datiRitenutaAccontoForm;

    private IContabilitaBD contabilitaBD;

    /**
     * Costruttore.
     */
    public DatiRitenutaAccontoCommand() {
        super(COMMAND_ID);
        RcpSupport.configure(this);

        contabilitaBD = RcpSupport.getBean(ContabilitaBD.BEAN_ID);

        datiRitenutaAccontoForm = new DatiRitenutaAccontoForm();
    }

    @Override
    protected void doExecuteCommand() {
        datiRitenutaAccontoForm.revert();
        datiRitenutaAccontoForm.setFormObject(areaContabile);
        PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(datiRitenutaAccontoForm,
                null) {

            @Override
            protected boolean isMessagePaneVisible() {
                return true;
            }

            @Override
            protected boolean onFinish() {
                if (datiRitenutaAccontoForm.isDirty() && datiRitenutaAccontoForm.getFormModel().isCommittable()) {
                    datiRitenutaAccontoForm.commit();

                    try {
                        areaContabile = contabilitaBD
                                .salvaAreaContabile((AreaContabile) datiRitenutaAccontoForm.getFormObject());
                    } catch (Exception e) {
                        logger.error("--> errore durante il salvataggio dell'area contabile", e);
                        PanjeaSwingUtil.checkAndThrowException(e);
                    }
                }
                return true;
            }
        };
        dialog.showDialog();
    }

    /**
     * @return the areaContabile
     */
    public AreaContabile getAreaContabile() {
        return areaContabile;
    }

    /**
     * @param areaContabile
     *            the areaContabile to set
     */
    public void setAreaContabile(AreaContabile areaContabile) {
        this.areaContabile = areaContabile;
        if (areaContabile == null || areaContabile.isNew() || areaContabile.getTipoAreaContabile() == null
                || !areaContabile.getTipoAreaContabile().isRitenutaAcconto()) {
            getFaceDescriptor().setCaption("");
            getFaceDescriptor().setIcon(null);
            setVisible(false);
        } else {
            CommandButtonLabelInfo labelInfo = new CommandButtonLabelInfo("");
            getFaceDescriptor().setLabelInfo(labelInfo);
            Icon icon = RcpSupport.getIcon("ritenutaAcconto");
            getFaceDescriptor().setIcon(icon);
            setVisible(true);
        }
    }

}
