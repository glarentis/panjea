package it.eurotn.panjea.rich;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.CommandManager;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.config.ToolBarCommandButtonConfigurer;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.list.DefaultGroupableListModel;
import com.jidesoft.list.GroupList;
import com.jidesoft.list.ListModelWrapper;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.ListSearchable;
import com.jidesoft.tooltip.ExpandedTipUtils;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.panjea.magazzino.exception.AnalisiPresenteInDashBoardException;
import it.eurotn.rich.command.AbstractDeleteCommand;
import it.eurotn.rich.command.JECCommandGroup;

public class AnalisiCommandPanelMenu extends AbstractMenuPanel {

    /**
     * @author fattazzo
     *
     */
    private final class AnalisiBiTransferHandler extends TransferHandler {
        private static final long serialVersionUID = -2377749352452231954L;

        @Override
        protected Transferable createTransferable(JComponent component) {
            GroupList list = (GroupList) component;

            Object selObject = list.getSelectedValue();

            if (selObject instanceof AnalisiBi) {
                StringBuilder sb = new StringBuilder(30);
                sb.append("AnalisiBi#");
                sb.append(((AnalisiBi) selObject).getCategoria());
                sb.append("#");
                sb.append(((AnalisiBi) selObject).getNome());
                return new StringSelection(sb.toString());
            }

            if (selObject instanceof String) {
                StringBuilder sb = new StringBuilder(30);
                sb.append("AnalisiBi#");
                sb.append(selObject.toString());
                sb.append("ss#aa#");
                return new StringSelection(sb.toString());
            }

            return null;
        }

        @Override
        public int getSourceActions(JComponent component) {
            return COPY_OR_MOVE;
        }
    }

    private static class AnalisiGroupCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = -9040435990579039633L;

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBackground(isSelected ? list.getSelectionBackground() : Color.LIGHT_GRAY);
            label.setForeground(new Color(0, 21, 110));
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            // label.setBorder(BorderFactory.createCompoundBorder(new
            // PartialLineBorder(Color.LIGHT_GRAY, 1,
            // PartialSide.SOUTH), BorderFactory.createEmptyBorder(2, 6, 2, 2)));
            if (value == null || value.toString().isEmpty()) {
                label.setText(" ");
            }
            return label;
        }
    }

    private class CopiaCommand extends ActionCommand {
        public CopiaCommand() {
            super("copiaAnalisiCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            Object selObject = analisiList.getSelectedValue();
            if (selObject instanceof AnalisiBi) {
                businessIntelligenceBD.copiaAnalisi((AnalisiBi) selObject);
                createListModel();
            }

        }
    }

    private class DeleteButton extends AbstractDeleteCommand {

        private boolean deleted = false;

        public DeleteButton() {
            super("deleteAnalisiCommand");
            setId("deleteAnalisiCommand");
            RcpSupport.configure(this);
        }

        private boolean deleteAnalisi(AnalisiBi analisiBi) {
            try {
                AnalisiCommandPanelMenu.this.businessIntelligenceBD.cancellaAnalisi(analisiBi.getId());
                deleted = true;
            } catch (final AnalisiPresenteInDashBoardException e) {
                StringBuilder sb = new StringBuilder(
                        "L'analisi che si stà cercando di cancellare è presente nelle seguenti dashboard:<b><br><ul>");
                for (DashBoard dashBoard : e.getDashBoards()) {
                    sb.append("<li>");
                    sb.append(dashBoard.getNome());
                    sb.append("</li>");
                }
                sb.append("</ul></b><br>Rimuoverla dalle dashboard elencate?");
                ConfirmationDialog dialog = new ConfirmationDialog("ATTENZIONE", sb.toString()) {

                    @Override
                    protected void onConfirm() {
                        try {
                            AnalisiCommandPanelMenu.this.businessIntelligenceBD.cancellaAnalisi(e.getIdAnalisi(), true);
                            deleted = true;
                        } catch (AnalisiPresenteInDashBoardException e) {
                            // in questo caso me ne frego, non verrà mai lanciata visto che forzo la
                            // rimozione delle
                            // dashboardanalisi che la contengono.
                            deleted = false;
                        }
                    }
                };

                dialog.setPreferredSize(new Dimension(450, 150));
                dialog.showDialog();
            }

            return deleted;
        }

        private boolean deleteDashBoard(DashBoard dashBoard) {
            DashBoard dashboard = (DashBoard) analisiList.getSelectedValue();
            AnalisiCommandPanelMenu.this.businessIntelligenceBD.cancellaDashBoard(dashboard.getNome());

            return true;
        }

        @Override
        public Object onDelete() {
            deleted = false;

            Object selValue = analisiList.getSelectedValue();
            if (selValue instanceof AnalisiBi) {
                deleteAnalisi((AnalisiBi) selValue);
            } else if (selValue instanceof DashBoard) {
                deleteDashBoard((DashBoard) selValue);
            }

            if (deleted) {
                int selectedIndex = ((ListModelWrapper) analisiList.getModel())
                        .getActualIndexAt(analisiList.getSelectedIndex());
                listModel.remove(selectedIndex);
            }
            return null;
        }

    }

    private static class ListRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = -9061532317641507759L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof AnalisiBi) {
                AnalisiBi analisi = (AnalisiBi) value;
                label.setText(analisi.getNome());
                label.setIcon(RcpSupport.getIcon("openDatawareHouseCommand.icon"));
                label.setToolTipText(analisi.getDescrizione());
            } else if (value instanceof DashBoard) {
                DashBoard dashboard = (DashBoard) value;
                label.setText(dashboard.getNome());
                label.setIcon(RcpSupport.getIcon("newDashBoardEditorCommand.icon"));
            }
            return label;
        }
    }

    private class RefreshButton extends ActionCommand {
        public RefreshButton() {
            super("refreshBiCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            createListModel();
        }
    }

    private IBusinessIntelligenceBD businessIntelligenceBD;
    private String idNewDashBoardEditorCommand;
    private String idNewAnalisiEditorCommand;
    private GroupList analisiList;
    private DefaultGroupableListModel listModel;

    /**
     *
     * Costruttore.
     */
    public AnalisiCommandPanelMenu() {
        super();
        setSingleton(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent createControl() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        analisiList = new GroupList();
        ExpandedTipUtils.install(analisiList);
        analisiList.setDragEnabled(true);
        analisiList.setTransferHandler(new AnalisiBiTransferHandler());

        analisiList.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent paramKeyEvent) {
                if (paramKeyEvent.getKeyChar() == KeyEvent.VK_ENTER) {
                    Object selectObject = analisiList.getSelectedValue();
                    LifecycleApplicationEvent event = new OpenEditorEvent(selectObject);
                    Application.instance().getApplicationContext().publishEvent(event);
                }
            }
        });
        analisiList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2) {
                    Object selectObject = analisiList.getSelectedValue();
                    LifecycleApplicationEvent event = new OpenEditorEvent(selectObject);
                    Application.instance().getApplicationContext().publishEvent(event);
                }
            }
        });

        new ListSearchable(analisiList);
        analisiList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        analisiList.setCellRenderer(new ListRenderer());
        analisiList.setGroupCellRenderer(new AnalisiGroupCellRenderer());
        listModel = new DefaultGroupableListModel();
        createListModel();
        CommandManager commandManager = Application.instance().getActiveWindow().getCommandManager();
        CommandGroup toolbar = new CommandGroup();
        toolbar.add(((AbstractCommand) commandManager.getCommand(idNewAnalisiEditorCommand)));
        toolbar.add(((AbstractCommand) commandManager.getCommand(idNewDashBoardEditorCommand)));
        menuPanel.add(toolbar.createToolBar(), BorderLayout.NORTH);
        menuPanel.add(new JScrollPane(analisiList), BorderLayout.CENTER);

        CommandGroup toolbarBottom = new JECCommandGroup() {
            @Override
            protected CommandButtonConfigurer getToolBarButtonConfigurer() {
                // uso quello di default perchè non voglio l'ombra sulle icone dei pulsanti
                return new ToolBarCommandButtonConfigurer();
            }
        };
        toolbarBottom.add(new RefreshButton());
        toolbarBottom.add(new DeleteButton());
        toolbarBottom.add(new CopiaCommand());
        menuPanel.add(toolbarBottom.createToolBar(), BorderLayout.SOUTH);
        return new JScrollPane(menuPanel);
    }

    @SuppressWarnings("unchecked")
    private void createListModel() {
        listModel.clear();
        List<AnalisiBi> analisi = businessIntelligenceBD.caricaListaAnalisi();
        for (AnalisiBi analisiBi : analisi) {
            listModel.addElement(analisiBi);
            listModel.setGroupAt(analisiBi.getCategoria(), listModel.getSize() - 1);
        }
        List<DashBoard> dashboards = businessIntelligenceBD.caricaListaDashBoard();
        for (DashBoard dashBoard : dashboards) {
            listModel.addElement(dashBoard);
            listModel.setGroupAt(dashBoard.getCategoria(), listModel.getSize() - 1);
        }

        // ReportManager reportManager = RcpSupport.getBean("reportManager");
        // Set<String> resports = reportManager.listReport("Analisi/Fatturato");
        // for (String report : resports) {
        // listModel.addElement(report);
        // }
        analisiList.setModel(listModel);
    }

    /**
     * @return Returns the businessIntelligenceBD.
     */
    public IBusinessIntelligenceBD getBusinessIntelligenceBD() {
        return businessIntelligenceBD;
    }

    /**
     * @return Returns the idNewAnalisiEditorCommand.
     */
    public String getIdNewAnalisiEditorCommand() {
        return idNewAnalisiEditorCommand;
    }

    /**
     * @return Returns the idNewDashBoardEditorCommand.
     */
    public String getIdNewDashBoardEditorCommand() {
        return idNewDashBoardEditorCommand;
    }

    @Override
    public boolean hasElements() {
        return true;
    }

    /**
     * @param businessIntelligenceBD
     *            The businessIntelligenceBD to set.
     */
    public void setBusinessIntelligenceBD(IBusinessIntelligenceBD businessIntelligenceBD) {
        this.businessIntelligenceBD = businessIntelligenceBD;
    }

    /**
     * @param idNewAnalisiEditorCommand
     *            The idNewAnalisiEditorCommand to set.
     */
    public void setIdNewAnalisiEditorCommand(String idNewAnalisiEditorCommand) {
        this.idNewAnalisiEditorCommand = idNewAnalisiEditorCommand;
    }

    /**
     * @param idNewDashBoardEditorCommand
     *            The idNewDashBoardEditorCommand to set.
     */
    public void setIdNewDashBoardEditorCommand(String idNewDashBoardEditorCommand) {
        this.idNewDashBoardEditorCommand = idNewDashBoardEditorCommand;
    }
}
