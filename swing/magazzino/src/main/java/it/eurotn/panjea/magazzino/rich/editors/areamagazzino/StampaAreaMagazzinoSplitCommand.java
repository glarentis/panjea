package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jdesktop.swingx.VerticalLayout;

import com.jidesoft.popup.JidePopup;

import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseDdt;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.rich.editors.documento.StampaAreaDocumentoSplitCommand;
import it.eurotn.panjea.rich.editors.stampe.StampaAreaDocumentoAction;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.rich.report.JecReportDocumentoMagazzino;

public class StampaAreaMagazzinoSplitCommand extends StampaAreaDocumentoSplitCommand
        implements StampaAreaDocumentoAction {
    private JRadioButton stampaConPrezziRadioButton = new JRadioButton("Stampa con prezzi");
    private JRadioButton stampaSenzaPrezziRadioButton = new JRadioButton("Stampa senza prezzi");

    /**
     * Costruttore.
     */
    public StampaAreaMagazzinoSplitCommand() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param idCommand
     *            id comando
     */
    public StampaAreaMagazzinoSplitCommand(final String idCommand) {
        super(idCommand);
    }

    /**
     * Crea il pannello che contiene tutti i componenti per la scelta delle stampa prezzi.
     *
     * @return controlli creati
     */
    private JComponent getStampaPrezziComponent() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBorder(null);

        if (!areaDocumento.isNew() && areaDocumento.getTipoAreaDocumento().getTipoDocumento()
                .getClasseTipoDocumentoInstance() instanceof ClasseDdt) {
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(stampaConPrezziRadioButton);
            buttonGroup.add(stampaSenzaPrezziRadioButton);

            panel.add(stampaConPrezziRadioButton);
            panel.add(stampaSenzaPrezziRadioButton);

            panel.setBorder(BorderFactory.createTitledBorder("Prezzi"));
        }

        stampaConPrezziRadioButton.setSelected(((AreaMagazzino) areaDocumento).isStampaPrezzi());
        stampaSenzaPrezziRadioButton.setSelected(!((AreaMagazzino) areaDocumento).isStampaPrezzi());

        return panel;
    }

    @Override
    public void stampa(LayoutStampa layout, boolean showPrintDialog) {
        JecReportDocumentoMagazzino documento = new JecReportDocumentoMagazzino((AreaMagazzino) areaDocumento);
        documento.setTipoLinguaReport(getTipoLingua());
        documento.setStampaPrezzi(stampaConPrezziRadioButton.isSelected());
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
    @Override
    public void updateCommand(IAreaDocumento paramAreaDocumento) {

        this.areaDocumento = paramAreaDocumento;
        splitButton.setMenu(null);
        JidePopup popup = new JidePopup();
        popup.getContentPane().setLayout(new VerticalLayout());
        splitButton.setMenu(popup);
        splitButton.getMenu().getContentPane().add(createLinguaReportComponent());
        splitButton.getMenu().getContentPane().add(getStampaPrezziComponent());
        splitButton.getMenu().getContentPane().add(createLayoutsStampaComponent());
        splitButton.getMenu().getContentPane().invalidate();
        splitButton.getMenu().getContentPane().repaint();

        setEnabled(!areaDocumento.getStato().isProvvisorio());
    }
}
