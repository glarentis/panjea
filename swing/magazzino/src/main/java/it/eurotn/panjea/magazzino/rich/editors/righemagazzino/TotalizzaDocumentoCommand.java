package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.JideSplitButton;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;

public class TotalizzaDocumentoCommand extends ApplicationWindowAwareCommand {

    private static Logger logger = Logger.getLogger(TotalizzaDocumentoCommand.class);

    public static final String COMMAND_ID = "totalizzaDocumentoCommand";

    public static final String KEY_TOTALIZZAZIONE_AUTOMATICA = "keyTotalizzazioneAutomaticaMagazzino";

    private AreaMagazzinoFullDTO areaMagazzinoFullDTO;

    private final IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private JRadioButtonMenuItem manualeMenuItem;

    private JRadioButtonMenuItem automaticoMenuItem;

    private JideSplitButton button;

    /**
     * Costruttore.
     * 
     * @param magazzinoDocumentoBD
     *            magazzinoDocumentoBD
     */
    public TotalizzaDocumentoCommand(final IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        super(COMMAND_ID);
        RcpSupport.configure(this);
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    @Override
    public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
            CommandButtonConfigurer buttonConfigurer) {

        button = new JideSplitButton();
        button.setName(COMMAND_ID);
        manualeMenuItem = new JRadioButtonMenuItem("Manuale", true);
        manualeMenuItem.setName("totalizzazioneManualeCommand");
        automaticoMenuItem = new JRadioButtonMenuItem("Automatico");
        automaticoMenuItem.setName("totalizzazioneAutomaticaCommand");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(manualeMenuItem);
        buttonGroup.add(automaticoMenuItem);

        button.add(manualeMenuItem);
        button.add(automaticoMenuItem);

        button.setAction(new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                TotalizzaDocumentoCommand.this.execute();
            }
        });
        RcpSupport.configure(this);
        // button.setText(this.getText());
        button.setIcon(this.getIcon());
        button.setToolTipText(this.getText());

        return button;
    }

    @Override
    protected void doExecuteCommand() {
        try {
            AreaMagazzino areaMagazzino = magazzinoDocumentoBD.totalizzaDocumento(
                    areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino()
                            .getStrategiaTotalizzazioneDocumento(),
                    areaMagazzinoFullDTO.getAreaMagazzino(), areaMagazzinoFullDTO.getAreaRate());
            areaMagazzinoFullDTO.setAreaMagazzino(areaMagazzino);
        } catch (Exception e) {
            // EXCEPTION MAIL verifico i dati passati alla conferma righe magazzino
            logger.error("--> Errore nella conferma delle righe magazzino, areaMagazzino "
                    + areaMagazzinoFullDTO.getAreaMagazzino() + ", areaRate " + areaMagazzinoFullDTO.getAreaRate()
                    + ", areaContabile " + areaMagazzinoFullDTO.getAreaContabileLite(), e);
            throw new PanjeaRuntimeException(e);
        }
    }

    /**
     * @return the areaMagazzinoFullDTO
     */
    public AreaMagazzinoFullDTO getAreaMagazzinoFullDTO() {
        return areaMagazzinoFullDTO;
    }

    /**
     * @return the totalizzazioneAutomatica
     */
    public boolean isTotalizzazioneAutomatica() {
        return automaticoMenuItem.isSelected();
    }

    /**
     * @param areaMagazzinoFullDTO
     *            the areaMagazzinoFullDTO to set
     */
    public void setAreaMagazzinoFullDTO(AreaMagazzinoFullDTO areaMagazzinoFullDTO) {
        this.areaMagazzinoFullDTO = areaMagazzinoFullDTO;
    }

    @Override
    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    /**
     * @param totalizzazioneAutomatica
     *            the totalizzazioneAutomatica to set
     */
    public void setTotalizzazioneAutomatica(boolean totalizzazioneAutomatica) {
        manualeMenuItem.setSelected(!totalizzazioneAutomatica);
        automaticoMenuItem.setSelected(totalizzazioneAutomatica);
    }

    @Override
    public void setVisible(boolean value) {
        button.setVisible(value);
        super.setVisible(value);
    }

}
