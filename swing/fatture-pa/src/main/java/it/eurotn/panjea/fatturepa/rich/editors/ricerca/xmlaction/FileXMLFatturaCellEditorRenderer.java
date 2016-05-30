package it.eurotn.panjea.fatturepa.rich.editors.ricerca.xmlaction;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.AbstractTableCellEditorRenderer;
import com.jidesoft.grid.CellRolloverSupport;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.swing.JideButton;

import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.manager.exception.XMLCreationException;
import it.eurotn.panjea.fatturepa.rich.bd.FatturePABD;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePABD;
import it.eurotn.panjea.fatturepa.rich.commands.CreaEFirmaXMLFatturePACommand;
import it.eurotn.panjea.fatturepa.rich.commands.CreaEFirmaXMLFatturePACommand.TipoOperazione;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.AreaFatturaElettronicaType;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.AreaFatturaElettronicaTypeDialog;
import it.eurotn.panjea.fatturepa.rich.editors.ricerca.RisultatiRicercaFatturePATablePage;
import it.eurotn.panjea.fatturepa.util.AreaMagazzinoFatturaPARicerca;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

public class FileXMLFatturaCellEditorRenderer extends AbstractTableCellEditorRenderer implements CellRolloverSupport {

    private class RefreshTableCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public void postExecution(ActionCommand command) {
            tablePage.refreshData();
        }
    }

    private static final long serialVersionUID = -1559557761862573989L;

    private static final Logger LOGGER = Logger.getLogger(FileXMLFatturaCellEditorRenderer.class);

    public static final EditorContext CONTEXT = new EditorContext("fileXMLFatturaCellEditorRenderer");

    private IFatturePABD fatturePABD;

    private CreaEFirmaXMLFatturePACommand creaEFirmaXMLFatturePACommand = new CreaEFirmaXMLFatturePACommand();

    private RisultatiRicercaFatturePATablePage tablePage;

    /**
     * Costruttore.
     *
     * @param tablePage
     *            table page
     */
    public FileXMLFatturaCellEditorRenderer(final RisultatiRicercaFatturePATablePage tablePage) {
        super();
        fatturePABD = RcpSupport.getBean(FatturePABD.BEAN_ID);

        this.tablePage = tablePage;

        RefreshTableCommandInterceptor refreshTableCommandInterceptor = new RefreshTableCommandInterceptor();
        this.creaEFirmaXMLFatturePACommand.addCommandInterceptor(refreshTableCommandInterceptor);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void configureTableCellEditorRendererComponent(final JTable table, Component editorRendererComponent,
            boolean forRenderer, Object value, boolean isSelected, boolean hasFocus, final int row, final int column) {

        JButton newButton = (JButton) (((JPanel) editorRendererComponent).getComponent(0));
        JButton firmaButton = (JButton) (((JPanel) editorRendererComponent).getComponent(1));
        final JButton saveButton = (JButton) (((JPanel) editorRendererComponent).getComponent(2));
        JButton previewButton = (JButton) (((JPanel) editorRendererComponent).getComponent(3));
        JButton deleteButton = (JButton) (((JPanel) editorRendererComponent).getComponent(4));
        JButton editButton = (JButton) (((JPanel) editorRendererComponent).getComponent(5));

        final DefaultBeanTableModel innerModel = (DefaultBeanTableModel) TableModelWrapperUtils
                .getActualTableModel(table.getModel());
        final int roxInner = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);

        newButton.setVisible(roxInner != -1);
        firmaButton.setVisible(roxInner != -1);
        saveButton.setVisible(roxInner != -1);
        previewButton.setVisible(roxInner != -1);
        deleteButton.setVisible(roxInner != -1);
        editButton.setVisible(roxInner != -1);

        if (roxInner != -1) {
            final AreaMagazzinoFatturaPARicerca area = (AreaMagazzinoFatturaPARicerca) innerModel
                    .getElementAt(roxInner);

            newButton.setEnabled(
                    (area.getStato() == StatoAreaMagazzino.CONFERMATO || area.getStato() == StatoAreaMagazzino.FORZATO)
                            && StringUtils.isBlank(area.getFileXmlFattura()));
            firmaButton.setEnabled(area.isSignEnable());
            saveButton.setEnabled(!StringUtils.isBlank(area.getFileXmlFatturaFirmato())
                    || !StringUtils.isBlank(area.getFileXmlFattura()));
            previewButton.setEnabled(!StringUtils.isBlank(area.getFileXmlFattura()));
            deleteButton.setEnabled(area.isDeleteXMLEnable());
            editButton.setEnabled(
                    area.getStato() == StatoAreaMagazzino.CONFERMATO || area.getStato() == StatoAreaMagazzino.FORZATO);

            if (!forRenderer) {
                newButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        tablePage.getTable().selectRowObject(area, null);
                        if (area.getStato() == StatoAreaMagazzino.CONFERMATO
                                || area.getStato() == StatoAreaMagazzino.FORZATO) {
                            Map<Object, Object> param = new HashMap<Object, Object>();
                            param.put(CreaEFirmaXMLFatturePACommand.PARAM_ID_AREE_MAGAZZINO,
                                    Arrays.asList(new Integer[] { area.getIdAreaMagazzino() }));
                            param.put(CreaEFirmaXMLFatturePACommand.PARAM_OPERAZIONE, TipoOperazione.CREA_XML);
                            creaEFirmaXMLFatturePACommand.execute(param);
                        }
                    }
                });
                saveButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent event) {
                        tablePage.getTable().selectRowObject(area, null);
                        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePABD
                                .caricaAreaMagazzinoFatturaPA(area.getIdAreaMagazzino());
                        DownloadXMLAction.download(areaMagazzinoFatturaPA);
                    }
                });
                firmaButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        tablePage.getTable().selectRowObject(area, null);
                        Map<Object, Object> param = new HashMap<Object, Object>();
                        param.put(CreaEFirmaXMLFatturePACommand.PARAM_ID_AREE_MAGAZZINO,
                                Arrays.asList(new Integer[] { area.getIdAreaMagazzino() }));
                        param.put(CreaEFirmaXMLFatturePACommand.PARAM_OPERAZIONE, TipoOperazione.FIRMA_XML);
                        creaEFirmaXMLFatturePACommand.execute(param);
                    }
                });
                previewButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tablePage.getTable().selectRowObject(area, null);
                        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePABD
                                .caricaAreaMagazzinoFatturaPA(area.getIdAreaMagazzino());
                        new PreviewXMLDialog(areaMagazzinoFatturaPA.getXmlFattura()).showDialog();
                    }
                });
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        DeleteXMLFatturaPAAction.cancellaXMLFatturaPA(area.getIdAreaMagazzino());
                        tablePage.refreshData();
                    }
                });
                editButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent event) {
                        tablePage.getTable().selectRowObject(area, null);
                        AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePABD
                                .caricaAreaMagazzinoFatturaPA(area.getIdAreaMagazzino());

                        if (areaMagazzinoFatturaPA != null) {
                            if (!areaMagazzinoFatturaPA.getXmlFattura().isPresent()) {
                                try {
                                    areaMagazzinoFatturaPA = fatturePABD.creaXMLFattura(area.getIdAreaMagazzino());
                                } catch (XMLCreationException e1) {
                                    if (LOGGER.isDebugEnabled()) {
                                        LOGGER.debug("--> Errore durante la creazione dell'xml", e1);
                                    }
                                    MessageDialog dialog = new MessageDialog("ATTENZIONE",
                                            new DefaultMessage(e1.getFormattedMessage(), Severity.ERROR));
                                    dialog.showDialog();
                                    return;
                                }
                            }
                            IFatturaElettronicaType fatturaElettronicaType = fatturePABD.caricaFatturaElettronicaType(
                                    areaMagazzinoFatturaPA.getXmlFattura().getXmlFattura());

                            AreaFatturaElettronicaType areaFatturaElettronicaType = new AreaFatturaElettronicaType(
                                    areaMagazzinoFatturaPA, fatturaElettronicaType);
                            new AreaFatturaElettronicaTypeDialog(areaFatturaElettronicaType) {

                                @Override
                                public void onXMLFatturaCreated(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA) {
                                    tablePage.refreshData();
                                }

                            }.showDialog();
                        }

                    }
                });
            }

            newButton.setToolTipText("Crea file XML");
            firmaButton.setToolTipText("Firma file XML");
            saveButton.setToolTipText("Scarica il file XML");
            previewButton.setToolTipText("Visualizza il file XML");
            deleteButton.setToolTipText("Cancella file XML");
            editButton.setToolTipText("Modifica dati per l'invio");
        }
        editorRendererComponent.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        editorRendererComponent.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
    }

    /**
     * Crea un button standard per il renderer/editor.
     *
     * @param idIcon
     *            id icona
     * @return button
     */
    private JButton createButton(String idIcon) {
        JButton button = new JideButton(RcpSupport.getIcon(idIcon));
        button.setOpaque(false);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setRequestFocusEnabled(false);
        return button;
    }

    @Override
    public Component createTableCellEditorRendererComponent(JTable table, int row, int column) {
        JPanel panel = new JPanel(new GridLayout(1, 3, 2, 0));
        panel.setOpaque(true);
        panel.add(createButton("newCommand.icon"));
        panel.add(createButton("key"));
        panel.add(createButton("download"));
        panel.add(createButton("anteprima"));
        panel.add(createButton("deleteCommand.icon"));
        panel.add(createButton("edit"));
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public boolean isRollover(JTable table, MouseEvent e, int row, int column) {
        return true;
    }
}