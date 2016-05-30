/**
 *
 */
package it.eurotn.panjea.rich.factory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.springframework.richclient.components.PatchedJFormattedTextField;
import org.springframework.richclient.factory.DefaultComponentFactory;
import org.springframework.richclient.factory.TableFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.action.CommandBar;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.PartialLineBorder;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;

import it.eurotn.panjea.rich.components.JecSplitPane;
import it.eurotn.panjea.rich.factory.table.JXTableFactory;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.report.editor.JecJRViewer;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Estende il DefaultComponentFactory sovrascrivendo e/o aggiungendo alcuni componenti Viene sovrascritto questo factory
 * nel context.xml<br>
 * <br>
 * &lt;bean id="componentFactory" class="MyComponentFactory"/&gt;<br>
 *
 * @author Leonardo
 */
public class PanjeaComponentFactory extends DefaultComponentFactory {

    private TableFactory tableFactory;

    private void applyStyle(JDateChooser dateChooser) {
        dateChooser.getCalendarButton().setIcon(RcpSupport.getIcon("java.util.Date"));
        dateChooser.getCalendarButton().setMnemonic(-1);
        dateChooser.getCalendarButton().setBorderPainted(false);
    }

    private Border createBorderForComponent(String labelKey) {
        PartialLineBorder partialLineBorder = new PartialLineBorder(Color.gray, 1, true);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(partialLineBorder, labelKey, TitledBorder.LEFT,
                TitledBorder.CENTER);
        Border emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);
        return BorderFactory.createCompoundBorder(titledBorder, emptyBorder);
    }

    @Override
    public JCheckBox createCheckBox(String labelKey) {
        JCheckBox checkBox = super.createCheckBox(labelKey);
        // setto a true per poter vedere il bordo settato dal focus listener
        checkBox.setBorderPainted(true);
        return checkBox;
    }

    @Override
    public JCheckBox createCheckBox(String[] labelKeys) {
        JCheckBox checkBox = super.createCheckBox(labelKeys);
        // setto a true per poter vedere il bordo settato dal focus listener
        checkBox.setBorderPainted(true);
        return checkBox;
    }

    public JList createCheckBoxList() {
        return new CheckBoxList();
    }

    public CollapsiblePane createCollapsiblePane() {
        return createNewCollapsiblePane();
    }

    public CollapsiblePane createCollapsiblePane(String title) {
        return createNewCollapsiblePane(title);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public JComboBox createComboBox() {
        // JideComboBox comboBox = new JideComboBox();
        // comboBox.setBorder(UIManager.getBorder("TextField.border"));
        return new JComboBox();
    }

    public JDateChooser createDateChooser() {
        JDateChooser dateChooser = new JDateChooser();
        applyStyle(dateChooser);
        return dateChooser;
    }

    public JDateChooser createDateChooser(Date date) {
        JDateChooser dateChooser = new JDateChooser(date);
        applyStyle(dateChooser);
        return dateChooser;
    }

    public JDateChooser createDateChooser(IDateEditor date) {
        JDateChooser dateChooser = new JDateChooser(date);
        applyStyle(dateChooser);
        return dateChooser;
    }

    public JFormattedTextField createFormattedTextField() {
        PatchedJFormattedTextField patchedJFormattedTextField = new PanjeaPatchedJFormattedTextField();
        configureTextField(patchedJFormattedTextField);
        return patchedJFormattedTextField;
    }

    @Override
    public JFormattedTextField createFormattedTextField(AbstractFormatterFactory formatterFactory) {
        PatchedJFormattedTextField patchedJFormattedTextField = new PanjeaPatchedJFormattedTextField(formatterFactory);
        configureTextField(patchedJFormattedTextField);
        return patchedJFormattedTextField;
    }

    public net.sf.jasperreports.view.JRViewer createJRViewer(JasperPrint jasperPrint) {
        return new JecJRViewer(jasperPrint, null);
    }

    protected CollapsiblePane createNewCollapsiblePane() {
        return new CollapsiblePane();
    }

    protected CollapsiblePane createNewCollapsiblePane(String title) {
        return new CollapsiblePane(title);
    }

    @Override
    public JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder());
        return panel;
    }

    @Override
    public JPanel createPanel(LayoutManager layoutManager) {
        JPanel panel = new JPanel(layoutManager);
        panel.setBorder(BorderFactory.createEmptyBorder());
        return panel;
    }

    @Override
    public JScrollPane createScrollPane() {
        JScrollPane panel = super.createScrollPane();
        panel.setBorder(null);
        return panel;
    }

    @Override
    public JScrollPane createScrollPane(Component view) {
        JScrollPane pane = super.createScrollPane(view);
        pane.setBorder(null);
        return pane;
    }

    @Override
    public JScrollPane createScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        JScrollPane pane = super.createScrollPane(view, vsbPolicy, hsbPolicy);
        pane.setBorder(null);
        return pane;
    }

    /**
     *
     * @return {@link SearchTextField}
     */
    public SearchTextField createSearchTextField() {
        return new SearchTextField();
    }

    /**
     *
     * @return {@link JSpinner}
     */
    public JSpinner createSpinner() {
        return new JSpinner();
    }

    public JSplitPane createSplitPane(int orientation) {
        return new JecSplitPane(orientation);
    }

    @Override
    public JTabbedPane createTabbedPane() {
        JideTabbedPane tabbedPane = new JideTabbedPane();
        return tabbedPane;
    }

    @Override
    public JComponent createTitledBorderFor(String labelKey, JComponent component) {
        Border border = createBorderForComponent(labelKey);
        component.setBorder(border);
        component.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        return component;
    }

    @Override
    public JComponent createToolBar() {
        CommandBar commandBar = new CommandBar();
        commandBar.setStretch(true);
        commandBar.setMargin(new Insets(2, 2, 2, 2));
        commandBar.setFloatable(false);
        commandBar.setOpaque(false);
        return commandBar;
    }

    public JTable createTreeTable() {
        return ((JXTableFactory) this.tableFactory).createTreeTable();
    }

    @Override
    public void setTableFactory(TableFactory tableFactory) {
        // sovrascrivo il metodo solo per poter salvare il tablefactory dato che
        // sulla classe DefaultComponentFactory la
        // variabile è privata e non esiste il getter.
        this.tableFactory = tableFactory;
        super.setTableFactory(tableFactory);
    }
}
