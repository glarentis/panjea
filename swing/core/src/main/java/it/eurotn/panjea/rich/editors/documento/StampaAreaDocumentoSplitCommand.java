package it.eurotn.panjea.rich.editors.documento;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.JideButton;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.rich.editors.stampe.StampaAreaDocumentoAction;
import it.eurotn.panjea.rich.editors.stampe.TipoAreaStampeComponent;
import it.eurotn.panjea.rich.stampe.ILayoutStampeManager;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;
import it.eurotn.rich.control.SplitButton;
import it.eurotn.rich.report.JecReportDocumento;
import it.eurotn.rich.report.JecReportDocumento.TipoLingua;

public class StampaAreaDocumentoSplitCommand extends ActionCommand implements StampaAreaDocumentoAction {

    private JRadioButton linguaAziendaleRadioButton = new JRadioButton();
    private JRadioButton linguaEntitaRadioButton = new JRadioButton();

    private JLabel linguaAziendaleLabel;
    private JLabel linguaEntitaLabel;

    private AziendaCorrente aziendaCorrente;

    protected SplitButton splitButton;

    private ILayoutStampeManager layoutStampeManager;

    protected IAreaDocumento areaDocumento;
    private LayoutStampa layoutStampaPredefinita;

    /**
     * Costruttore.
     */
    public StampaAreaDocumentoSplitCommand() {
        this("printCommand");
    }

    /**
     * Costruttore.
     *
     * @param idCommand
     *            id comando
     */
    public StampaAreaDocumentoSplitCommand(final String idCommand) {
        super(idCommand);
        RcpSupport.configure(this);
        this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
        this.layoutStampeManager = RcpSupport.getBean(LayoutStampeManager.BEAN_ID);
    }

    @Override
    public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
            CommandButtonConfigurer buttonConfigurer) {

        JideButton mainButton = (JideButton) super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
        splitButton = new SplitButton(mainButton, SwingConstants.SOUTH);

        JidePopup popup = new JidePopup();
        popup.getContentPane().setLayout(new VerticalLayout());
        splitButton.setMenu(popup);

        return splitButton;
    }

    /**
     * Crea il pannello che contiene tutti i layout da poter utilizzare.
     *
     * @return controlli creati
     */
    protected JComponent createLayoutsStampaComponent() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

        if (areaDocumento.getTipoAreaDocumento() != null) {

            List<LayoutStampaDocumento> layouts = layoutStampeManager.caricaLayoutStampe(
                    areaDocumento.getTipoAreaDocumento(), areaDocumento.getDocumento().getEntita(),
                    areaDocumento.getDocumento().getSedeEntita());

            TipoAreaStampeComponent tipoAreaStampeComponent = new TipoAreaStampeComponent(
                    areaDocumento.getTipoAreaDocumento(), layouts, true, this);
            layoutStampaPredefinita = tipoAreaStampeComponent.getLayoutStampaPredefinito();
            rootPanel.add(tipoAreaStampeComponent, BorderLayout.CENTER);
        }

        return rootPanel;
    }

    /**
     * Crea il pannello che contiene tutti i componenti per la scelta della lingua da utilizzare.
     *
     * @return controlli creati
     */
    protected JComponent createLinguaReportComponent() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));

        linguaAziendaleRadioButton.setEnabled(false);
        linguaEntitaRadioButton.setEnabled(false);

        panel.setBorder(null);

        // se ho la possibilità di stampare il report in più lingue inserisco i
        // controlli per selezionarle
        String linguaEntita = (areaDocumento.getDocumento().getSedeEntita() != null)
                ? areaDocumento.getDocumento().getSedeEntita().getLingua() : null;
        if (linguaEntita != null && !linguaEntita.isEmpty() && !linguaEntita.equals(aziendaCorrente.getLingua())) {
            ButtonGroup buttonGroup = new ButtonGroup();

            buttonGroup.add(getLinguaAziendaleRadioButton());
            JPanel linguaAziendalePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            linguaAziendalePanel.add(linguaAziendaleRadioButton);
            linguaAziendalePanel.add(linguaAziendaleLabel);
            panel.add(linguaAziendalePanel);

            JPanel linguaEntitaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            buttonGroup.add(getLinguaEntitaRadioButton());
            linguaEntitaPanel.add(linguaEntitaRadioButton);
            linguaEntitaPanel.add(linguaEntitaLabel);
            panel.add(linguaEntitaPanel);

            linguaAziendaleRadioButton.setEnabled(true);
            linguaEntitaRadioButton.setEnabled(true);

            // preferisco sempre la lingua entita come lingua predefinita
            linguaEntitaRadioButton.setSelected(true);

            panel.setBorder(BorderFactory.createTitledBorder("Lingua"));
        }

        return panel;
    }

    @Override
    protected void doExecuteCommand() {
        if (layoutStampaPredefinita == null) {
            splitButton.showPopup();
        } else {
            int key = (int) getParameter(MODIFIERS_PARAMETER_KEY);
            stampa(layoutStampaPredefinita, ((key & InputEvent.CTRL_MASK) != 0));
        }
    }

    /**
     * @return the linguaAziendaleRadioButton
     */
    private JRadioButton getLinguaAziendaleRadioButton() {

        Locale locale = new Locale(aziendaCorrente.getLingua());
        linguaAziendaleRadioButton.setText(StringUtils.capitalize(locale.getDisplayLanguage()));
        linguaAziendaleLabel = new JLabel(RcpSupport.getIcon(aziendaCorrente.getLingua()));
        linguaAziendaleRadioButton.setSelected(true);
        return linguaAziendaleRadioButton;
    }

    /**
     * @return the linguaEntitaRadioButton
     */
    private JRadioButton getLinguaEntitaRadioButton() {

        String linguaEntita = areaDocumento.getDocumento().getSedeEntita().getLingua();
        Locale locale = new Locale(linguaEntita);
        linguaEntitaRadioButton.setText(StringUtils.capitalize(locale.getDisplayLanguage()));
        linguaEntitaLabel = new JLabel(RcpSupport.getIcon(linguaEntita));

        return linguaEntitaRadioButton;
    }

    /**
     * @return tipo di lingua scelta
     */
    public TipoLingua getTipoLingua() {
        TipoLingua lingua = TipoLingua.AZIENDA;
        if (linguaEntitaRadioButton.isEnabled() && linguaEntitaRadioButton.isSelected()) {
            lingua = TipoLingua.ENTITA;
        }

        return lingua;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        splitButton.setEnabled(enabled);
    }

    @Override
    public void stampa(LayoutStampa layout, boolean showPrintDialog) {
        JecReportDocumento documento = new JecReportDocumento(areaDocumento);
        documento.setTipoLinguaReport(getTipoLingua());
        documento.setLayoutStampa(layout);
        documento.setShowPrintDialog(showPrintDialog);
        documento.execute();
    }

    /**
     * Aggiorna lo stato del comando.
     *
     * @param paramAreaDocumento
     *            paramAreaDocumento da aggiornare
     */
    public void updateCommand(IAreaDocumento paramAreaDocumento) {

        this.areaDocumento = paramAreaDocumento;
        splitButton.setMenu(null);
        JidePopup popup = new JidePopup();
        popup.getContentPane().setLayout(new VerticalLayout());
        splitButton.setMenu(popup);
        splitButton.getMenu().getContentPane().add(createLinguaReportComponent());
        splitButton.getMenu().getContentPane().add(createLayoutsStampaComponent());
        splitButton.getMenu().getContentPane().repaint();

        setEnabled(!areaDocumento.getStato().isProvvisorio());
    }

}
