package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.pane.CollapsiblePane;

import ca.odell.glazedlists.GroupingList;
import it.eurotn.panjea.pagamenti.service.interfaces.FlussoCBIDownload;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti.RaggruppamentoEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.GeneraDistintaCommand;
import it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.GeneraFlussoCommand;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.component.PartitaAreaTesoreriaComponentBuilder;
import it.eurotn.rich.command.JECCommandGroup;

public class EffettiComponentBuilder extends AbstractControlFactory {

    private class ExpandCommand extends ActionCommand {

        private static final String COLLAPSE_STATE = "collapse";
        private static final String ESPANDI_COMMAND = ".expandCommand";
        private static final String EXPAND_STATE = "expand";
        private boolean collapse;
        private final CommandFaceDescriptor collapseDescriptor;

        private final CommandFaceDescriptor expandDescriptor;
        private final JPanel panel;

        /**
         * Costruttore.
         * 
         * @param collapse
         *            stato inizale del comando
         * @param panel
         *            pannello da gestire
         */
        public ExpandCommand(final boolean collapse, final JPanel panel) {
            super(ESPANDI_COMMAND);
            RcpSupport.configure(this);
            this.collapse = collapse;
            this.panel = panel;

            final Icon toExpandIcon = RcpSupport.getIcon(EXPAND_STATE + ".icon");
            final Icon toCollapseIcon = RcpSupport.getIcon(COLLAPSE_STATE + ".icon");
            collapseDescriptor = new CommandFaceDescriptor(null, toExpandIcon, null);
            expandDescriptor = new CommandFaceDescriptor(null, toCollapseIcon, null);
            if (collapse) {
                setFaceDescriptor(collapseDescriptor);
            } else {
                setFaceDescriptor(expandDescriptor);

            }
        }

        @Override
        protected void doExecuteCommand() {
            collapse = !collapse;
            for (final Component component : panel.getComponents()) {
                if (component instanceof CollapsiblePane) {
                    ((CollapsiblePane) component).collapse(collapse);
                }
            }
            if (getFaceDescriptor().equals(collapseDescriptor)) {
                setFaceDescriptor(expandDescriptor);
            } else {
                setFaceDescriptor(collapseDescriptor);
            }
        }
    }

    private final AreaEffetti areaEffetti;

    private final PartitaAreaTesoreriaComponentBuilder componentBuilder = new PartitaAreaTesoreriaComponentBuilder();

    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    private final FlussoCBIDownload flussoCBIDownload;
    private GeneraDistintaCommand generaDistintaCommand;
    private GeneraFlussoCommand generaFlussoCommand;

    private JPanel rootPanel;

    private final ITesoreriaBD tesoreriaBD;

    /**
     * Costruttore.
     * 
     * @param areaTesoreria
     *            area tesoreria da gestire
     * @param tesoreriaBD
     *            tesoreriaBD
     */
    public EffettiComponentBuilder(final AreaTesoreria areaTesoreria, final ITesoreriaBD tesoreriaBD) {
        super();
        this.areaEffetti = (AreaEffetti) areaTesoreria;
        this.tesoreriaBD = tesoreriaBD;
        this.flussoCBIDownload = RcpSupport.getBean("flussoCBIDownload");
    }

    @Override
    protected JComponent createControl() {

        rootPanel = getComponentFactory().createPanel(new VerticalLayout(5));

        if (areaEffetti.getEffetti().size() > 0) {
            RaggruppamentoEffetti raggruppamentoEffetti = RaggruppamentoEffetti.DATA_SCADENZA;
            if (areaEffetti.getEffetti().iterator().next().getDataValuta() != null) {
                raggruppamentoEffetti = RaggruppamentoEffetti.DATA_VALUTA;
            }

            final GroupingList<Effetto> effetti = areaEffetti.getEffettiRaggrupati(raggruppamentoEffetti);
            final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // aggiungo il pannello riassuntivo con totale pagamenti e importo documento
            rootPanel.add(createEffettoHeaderPanel(effetti, areaEffetti));

            for (final List<Effetto> list : effetti) {

                final List<JComponent> effettiGroupComponents = new ArrayList<>();
                BigDecimal totEffettiGroup = BigDecimal.ZERO;
                String dataGroupString = "";
                switch (raggruppamentoEffetti) {
                case DATA_SCADENZA:
                    dataGroupString = "Scadenza: " + ObjectConverterManager.toString(list.get(0).getDataScadenza());
                    break;
                case DATA_VALUTA:
                    dataGroupString = "Valuta: " + dateFormat.format(list.get(0).getDataValuta());
                    break;
                default:
                    throw new UnsupportedOperationException("Tipo raggruppamento non gestito");
                }

                for (final Effetto effetto : list) {
                    effettiGroupComponents.add(componentBuilder.getPartitaAreaTesoreriaComponent(effetto));
                    totEffettiGroup = totEffettiGroup.add(effetto.getImporto().getImportoInValutaAzienda());
                }

                // aggiungo il pannello riassuntivo con data, numero e totali effetti
                final CollapsiblePane groupCollapsiblePane = new CollapsiblePane();
                groupCollapsiblePane.collapse(false);

                groupCollapsiblePane.setLayout(new BorderLayout());
                groupCollapsiblePane.setTitleIcon(RcpSupport.getIcon(Date.class.getName()));
                final JPanel effettiPanel = getComponentFactory().createPanel(new VerticalLayout(5));
                for (final JComponent component : effettiGroupComponents) {
                    effettiPanel.add(component);
                }
                groupCollapsiblePane.add(effettiPanel, BorderLayout.CENTER);

                final JPanel groupPanel = getComponentFactory().createPanel(new BorderLayout());
                groupPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
                groupPanel.add(new ExpandCommand(true, effettiPanel).createButton(), BorderLayout.LINE_START);
                final JLabel dataGrouplabel = new JLabel(dataGroupString + " (" + list.size() + ")");
                Font f = dataGrouplabel.getFont();
                dataGrouplabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
                groupPanel.add(dataGrouplabel, BorderLayout.CENTER);

                final JLabel totaleLabel = new JLabel(decimalFormat.format(totEffettiGroup));
                f = totaleLabel.getFont();
                totaleLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
                totaleLabel.setIcon(RcpSupport.getIcon("EUR"));
                totaleLabel.setHorizontalTextPosition(SwingConstants.LEFT);
                groupPanel.add(totaleLabel, BorderLayout.EAST);
                groupCollapsiblePane.setTitleLabelComponent(groupPanel);

                rootPanel.add(groupCollapsiblePane);
            }
        }

        return rootPanel;
    }

    /**
     * Crea l'header panel degli effetti.
     * 
     * @param effetti
     *            effetti
     * @param paramAreaEffetti
     *            area effetti
     * @return pannello creato
     */
    private JComponent createEffettoHeaderPanel(GroupingList<Effetto> effetti, AreaEffetti paramAreaEffetti) {
        final JPanel headerPanel = getComponentFactory().createPanel(new BorderLayout());

        final JPanel commandPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        commandPanel.setMinimumSize(new Dimension(100, 30));
        commandPanel.setPreferredSize(new Dimension(100, 30));

        final JECCommandGroup commandGroup = new JECCommandGroup();
        commandGroup.add(getGeneraDistintaCommand());
        commandGroup.add(getGeneraFlussoCommand());
        commandPanel.add(commandGroup.createToolBar());

        // se ho la data valuta del primo effetto a null devo ancora creare una distinta e assegnare una data valuta.
        // visualizzo il pulsante per creare la distinta.
        final boolean generaDistintaVisible = effetti.get(0).get(0).getDataValuta() == null;
        getGeneraDistintaCommand().setAreaEffetti(paramAreaEffetti);
        getGeneraFlussoCommand().setAreaChiusure(paramAreaEffetti);
        getGeneraDistintaCommand().setVisible(generaDistintaVisible);
        getGeneraFlussoCommand().setVisible(generaDistintaVisible);

        headerPanel.add(commandPanel, BorderLayout.NORTH);

        headerPanel.add(new ExpandCommand(false, rootPanel).createButton(), BorderLayout.LINE_START);
        headerPanel.add(new JLabel("Numero valute: " + effetti.size()), BorderLayout.CENTER);

        headerPanel.add(
                new JLabel("Totale: " + decimalFormat
                        .format(paramAreaEffetti.getDocumento().getTotale().getImportoInValutaAzienda())),
                BorderLayout.EAST);
        GuiStandardUtils.attachBorder(headerPanel);
        return headerPanel;
    }

    /**
     * @return the generaDistintaCommand
     */
    public GeneraDistintaCommand getGeneraDistintaCommand() {
        if (generaDistintaCommand == null) {
            generaDistintaCommand = new GeneraDistintaCommand(tesoreriaBD);
            generaDistintaCommand.setVisible(false);
        }

        return generaDistintaCommand;
    }

    /**
     * @return the generaFlussoCommand
     */
    public GeneraFlussoCommand getGeneraFlussoCommand() {
        if (generaFlussoCommand == null) {
            generaFlussoCommand = new GeneraFlussoCommand(flussoCBIDownload, tesoreriaBD);
            generaFlussoCommand.setVisible(false);
        }

        return generaFlussoCommand;
    }
}
