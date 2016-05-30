package it.eurotn.rich.components.htmleditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.UndoManager;

import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.action.CommandBar;

import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.StandardDialog;
import net.atlanticbb.tantlinger.ui.UIUtils;
import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import net.atlanticbb.tantlinger.ui.text.Entities;
import net.atlanticbb.tantlinger.ui.text.HTMLUtils;
import net.atlanticbb.tantlinger.ui.text.IndentationFilter;
import net.atlanticbb.tantlinger.ui.text.SourceCodeEditor;
import net.atlanticbb.tantlinger.ui.text.WysiwygHTMLEditorKit;
import net.atlanticbb.tantlinger.ui.text.actions.BasicEditAction;
import net.atlanticbb.tantlinger.ui.text.actions.ClearStylesAction;
import net.atlanticbb.tantlinger.ui.text.actions.FindReplaceAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLEditorActionFactory;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLElementPropertiesAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLFontColorAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLHorizontalRuleAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLInlineAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLLineBreakAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLLinkAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLTableAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction;
import net.atlanticbb.tantlinger.ui.text.actions.SpecialCharAction;
import novaworx.syntax.SyntaxFactory;
import novaworx.textpane.SyntaxDocument;
import novaworx.textpane.SyntaxGutter;
import novaworx.textpane.SyntaxGutterBase;

/**
 * Componente HtmlEditor.
 *
 * @author Bob Tantlinger
 * @author Leonardo
 */
public class HTMLEditorPane extends JPanel {

    private class CaretHandler implements CaretListener {

        @Override
        public void caretUpdate(CaretEvent event) {
            updateState();
        }
    }

    private class FocusHandler implements FocusListener {

        @Override
        public void focusGained(FocusEvent event) {
            if (event.getComponent() instanceof JEditorPane) {
                JEditorPane ed = (JEditorPane) event.getComponent();
                CompoundUndoManager.updateUndo(ed.getDocument());
                if (ed.isEnabled()) {
                    focusedEditor = ed;
                } else {
                    focusedEditor = null;
                }
                updateState();

            }
        }

        @Override
        public void focusLost(FocusEvent event) {
            // non faccio nulla altrimenti se clicco sulla toolbar del componente
            // disattiva la toolbar
        }
    }

    /**
     * Dialog standard che permette l'edit del sorgente per l'editor html.
     *
     * @author leonardo
     */
    private class HTMLSourceCodeDialog extends StandardDialog {

        private static final long serialVersionUID = 1269649651324216274L;

        /**
         * Costruttore.
         */
        public HTMLSourceCodeDialog() {
            super();
            JScrollPane scrollPane = new JScrollPane(srcEditor);
            SyntaxGutter gutter = new SyntaxGutter(srcEditor);
            SyntaxGutterBase gutterBase = new SyntaxGutterBase(gutter);
            scrollPane.setRowHeaderView(gutter);
            scrollPane.setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, gutterBase);
            setContentPane(scrollPane);
            setSize(500, 300);
            setLocationRelativeTo(null);
            setMinimumSize(new Dimension(500, 300));
            setPreferredSize(new Dimension(500, 300));
        }

    }

    /**
     * Action per editare il sorgente html per il componente.<br>
     * Le modifiche eseguite vengono riportate nell'editor standard alla sola conferma del dialog.
     *
     * @author leonardo
     */
    private class OpenSourceDialogAction extends BasicEditAction {

        private static final long serialVersionUID = 6806419677895832393L;

        /**
         * Costruttore.
         */
        public OpenSourceDialogAction() {
            super(I18N.str("open_source"));
            putValue(MNEMONIC_KEY, new Integer(I18N.mnem("open_html_source")));
            putValue(SMALL_ICON, RcpSupport.getIcon("open_html_source"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_MASK));
            putValue(Action.SHORT_DESCRIPTION, getValue(Action.NAME));
        }

        @Override
        protected void doEdit(ActionEvent arg0, JEditorPane arg1) {

            String txt = deIndent(removeInvalidTags(wysEditor.getText()));
            txt = Entities.HTML40.unescapeUnknownEntities(txt);
            srcEditor.setText(txt);
            CompoundUndoManager.discardAllEdits(srcEditor.getDocument());

            StandardDialog dialog = new HTMLSourceCodeDialog();
            dialog.setVisible(true);
            if (!dialog.hasUserCancelled()) {
                String topText = removeInvalidTags(srcEditor.getText());
                topText = deIndent(removeInvalidTags(topText));
                topText = Entities.HTML40.unescapeUnknownEntities(topText);
                setText(topText);
            }
        }
    }

    /**
     * Editor kit per scrivere nel document di una textField il testo passato senza alcuna formattazione.<br>
     * il default editor kit inserisce un line_break prima.
     *
     * @author leonardo
     */
    private class PlainTextEditorKit extends DefaultEditorKit {

        private static final long serialVersionUID = 871516335213403378L;

        @Override
        public void write(Writer out, Document doc, int pos, int len) throws IOException, BadLocationException {
            doc.putProperty(DefaultEditorKit.EndOfLineStringProperty, "");
            super.write(out, doc, pos, len);
        }

    }

    private class PopupHandler extends MouseAdapter {

        /**
         * Mostra il popup al click del mouse.
         *
         * @param e
         *            mouse event
         */
        private void checkForPopupTrigger(MouseEvent event) {
            if (event.isPopupTrigger()) {
                JPopupMenu popup = null;
                if (event.getSource() == wysEditor) {
                    popup = wysPopupMenu;
                } else if (event.getSource() == srcEditor) {
                    popup = srcPopupMenu;
                } else {
                    return;
                }
                popup.show(event.getComponent(), event.getX(), event.getY());
            }
        }

        @Override
        public void mousePressed(MouseEvent event) {
            checkForPopupTrigger(event);
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            checkForPopupTrigger(event);
        }
    }

    private static final long serialVersionUID = 1L;

    private static final I18n I18N = I18n.getInstance("net.atlanticbb.tantlinger.shef");
    private static final String[] INVALID_TAGS = { "html", "head", "body", "title" };
    private JEditorPane wysEditor;

    private SourceCodeEditor srcEditor;
    private JEditorPane focusedEditor;
    private CommandBar formatToolBar;
    private JMenu editMenu;

    private JMenu formatMenu;
    private JMenu insertMenu;

    private JPopupMenu wysPopupMenu;
    private JPopupMenu srcPopupMenu;

    private ActionList actionList;

    private final FocusListener focusHandler = new FocusHandler();

    private final CaretListener caretHandler = new CaretHandler();
    private final MouseListener popupHandler = new PopupHandler();
    private JMenuBar menuBar;

    /**
     * Costruttore di default.
     */
    public HTMLEditorPane() {
        initUI();
    }

    /**
     * Aggiunge alla toolbar il button legato alla action passata.
     *
     * @param toolbar
     *            la toolbar
     * @param act
     *            la action da aggiungere alla toolbar
     */
    private void addToToolBar(CommandBar toolbar, Action act) {
        AbstractButton button = ActionUIFactory.getInstance().createButton(act);
        configToolbarButton(button);
        toolbar.add(button);
    }

    /**
     * Configura l'aspetto del button.
     *
     * @param button
     *            il button da configurare
     */
    private void configToolbarButton(AbstractButton button) {
        button.setText(null);
        button.setRolloverEnabled(true);
        button.setMnemonic(0);
        button.setMaximumSize(new Dimension(22, 22));
        button.setMinimumSize(new Dimension(22, 22));
        button.setPreferredSize(new Dimension(22, 22));
        button.setFocusable(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        Action act = button.getAction();
        if (act != null) {
            button.setToolTipText(act.getValue(Action.NAME).toString());
        }
    }

    /**
     * Crea le action per il component.
     */
    private void createEditorActions() {
        actionList = new ActionList("editor-actions");

        ActionList editActions = HTMLEditorActionFactory.createEditActionList();
        Action objectPropertiesAction = new HTMLElementPropertiesAction();

        // create editor popupmenus
        wysPopupMenu = ActionUIFactory.getInstance().createPopupMenu(editActions);
        wysPopupMenu.addSeparator();
        wysPopupMenu.add(objectPropertiesAction);
        srcPopupMenu = ActionUIFactory.getInstance().createPopupMenu(editActions);

        // create edit menu
        ActionList lst = new ActionList("edits");
        lst.addAll(editActions);
        lst.add(null);
        lst.add(new FindReplaceAction(false));
        actionList.addAll(lst);
        editMenu = ActionUIFactory.getInstance().createMenu(lst);
        editMenu.setText(I18N.str("edit"));

        // create format menu
        formatMenu = new JMenu(I18N.str("format"));
        lst = HTMLEditorActionFactory.createFontSizeActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, I18N.str("size")));
        ActionList fontSizeActions = new ActionList("fontSizeActions");
        fontSizeActions.addAll(lst);

        lst = HTMLEditorActionFactory.createInlineActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, I18N.str("style")));

        Action act = new HTMLFontColorAction();
        actionList.add(act);
        formatMenu.add(act);

        act = new ClearStylesAction();
        actionList.add(act);
        formatMenu.add(act);
        formatMenu.addSeparator();

        lst = HTMLEditorActionFactory.createBlockElementActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, I18N.str("paragraph")));
        ActionList paraActions = new ActionList("paraActions");
        paraActions.addAll(lst);

        lst = HTMLEditorActionFactory.createListElementActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, I18N.str("list")));
        formatMenu.addSeparator();
        paraActions.addAll(lst);

        actionList.add(objectPropertiesAction);
        formatMenu.add(objectPropertiesAction);

        // create insert menu
        insertMenu = new JMenu(I18N.str("insert"));

        act = new HTMLLineBreakAction();
        actionList.add(act);
        insertMenu.add(act);

        act = new HTMLHorizontalRuleAction();
        actionList.add(act);
        insertMenu.add(act);

        act = new SpecialCharAction();
        actionList.add(act);
        insertMenu.add(act);

        createFormatToolBar(fontSizeActions);
    }

    /**
     *
     */
    private void createEditors() {
        wysEditor = createWysiwygEditor();
        srcEditor = createSourceEditor();
    }

    /**
     * Crea la toolbar per il component.
     *
     * @param fontSizeActs
     *            fontSizeActs
     */
    @SuppressWarnings("rawtypes")
    private void createFormatToolBar(ActionList fontSizeActs) {
        ComponentFactory componentFactory = (ComponentFactory) Application.services()
                .getService(ComponentFactory.class);
        formatToolBar = (CommandBar) componentFactory.createToolBar();
        formatToolBar.setFloatable(false);
        formatToolBar.setFocusable(false);

        final JButton fontSizeButton = new JButton(UIUtils.getIcon(UIUtils.X16, "fontsize.png"));
        final JPopupMenu sizePopup = ActionUIFactory.getInstance().createPopupMenu(fontSizeActs);
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                sizePopup.show(fontSizeButton, 0, fontSizeButton.getHeight());
            }
        };
        fontSizeButton.addActionListener(al);
        configToolbarButton(fontSizeButton);
        formatToolBar.add(fontSizeButton);

        Action act = new HTMLFontColorAction();
        actionList.add(act);
        addToToolBar(formatToolBar, act);
        formatToolBar.addSeparator();

        act = new HTMLLinkAction();
        this.actionList.add(act);
        addToToolBar(this.formatToolBar, act);

        act = new HTMLImageAction();
        actionList.add(act);
        addToToolBar(formatToolBar, act);
        formatToolBar.addSeparator();

        act = new HTMLTableAction();
        this.actionList.add(act);
        addToToolBar(this.formatToolBar, act);
        formatToolBar.addSeparator();

        act = new HTMLInlineAction(HTMLInlineAction.BOLD);
        act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
        actionList.add(act);
        addToToolBar(formatToolBar, act);

        act = new HTMLInlineAction(HTMLInlineAction.ITALIC);
        act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
        actionList.add(act);
        addToToolBar(formatToolBar, act);

        act = new HTMLInlineAction(HTMLInlineAction.UNDERLINE);
        act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
        actionList.add(act);
        addToToolBar(formatToolBar, act);
        formatToolBar.addSeparator();

        List alst = HTMLEditorActionFactory.createListElementActionList();
        for (Iterator it = alst.iterator(); it.hasNext();) {
            act = (Action) it.next();
            act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
            actionList.add(act);
            addToToolBar(formatToolBar, act);
        }
        formatToolBar.addSeparator();

        act = new OpenSourceDialogAction();
        actionList.add(act);
        addToToolBar(formatToolBar, act);
    }

    /**
     * Crea il menu con una lista di actions.
     *
     * @param lst
     *            la list di ActionList
     * @param menuName
     *            il nome da associare al menu
     * @return JMenu
     */
    private JMenu createMenu(ActionList lst, String menuName) {
        JMenu menu = ActionUIFactory.getInstance().createMenu(lst);
        menu.setText(menuName);
        return menu;
    }

    /**
     * Crea il component per visualizzare il sorgente html.
     *
     * @return SourceCodeEditor
     */
    private SourceCodeEditor createSourceEditor() {

        SyntaxDocument doc = new SyntaxDocument();
        doc.setSyntax(SyntaxFactory.getSyntax("html"));
        CompoundUndoManager cuh = new CompoundUndoManager(doc, new UndoManager());

        doc.addUndoableEditListener(cuh);
        doc.setDocumentFilter(new IndentationFilter());

        SourceCodeEditor ed = new SourceCodeEditor();
        ed.setDocument(doc);
        ed.addFocusListener(focusHandler);
        ed.addCaretListener(caretHandler);
        ed.addMouseListener(popupHandler);

        return ed;
    }

    /**
     * Crea l'editor principale per visualizzare/editare l'html.
     *
     * @return JEditorPane
     */
    private JEditorPane createWysiwygEditor() {
        final JEditorPane ed = new JEditorPane();

        ed.setEditorKitForContentType("text/html", new WysiwygHTMLEditorKit());

        ed.setContentType("text/html");
        insertHTML(ed, "<div></div>", 0);

        ed.addCaretListener(caretHandler);
        ed.addFocusListener(focusHandler);
        ed.addMouseListener(popupHandler);

        // ed.setMaximumSize(new Dimension(400, 300));

        HTMLDocument document = (HTMLDocument) ed.getDocument();
        CompoundUndoManager cuh = new CompoundUndoManager(document, new UndoManager());
        document.addUndoableEditListener(cuh);
        document.setDocumentFilter(new TabToSpaceFilter(4));

        return ed;
    }

    /**
     * Methods for dealing with HTML between wysiwyg and source editors.
     *
     * @param html
     *            html
     * @return String
     */
    private String deIndent(String html) {
        String ws = "\n     ";
        StringBuilder sb = new StringBuilder(html);

        while (sb.indexOf(ws) != -1) {
            int sidx = sb.indexOf(ws);
            int eidx = sidx + ws.length();
            sb.delete(sidx, eidx);
            sb.insert(sidx, "\n");
        }

        return sb.toString();
    }

    /**
     * Cancella dal testo la stringa passata.
     *
     * @param text
     *            il testo completo
     * @param word
     *            la stringa da rimuovere
     * @return il testo senza la stringa scelta
     */
    private String deleteOccurance(String text, String word) {
        if (text == null) {
            return text;
        }
        StringBuilder sb = new StringBuilder(text);
        int pidx;
        while ((pidx = sb.toString().toLowerCase().indexOf(word.toLowerCase())) != -1) {
            sb.delete(pidx, pidx + word.length());
        }
        return sb.toString();
    }

    /**
     * @return editMenu
     */
    public JMenu getEditMenu() {
        return editMenu;
    }

    /**
     * @return formatMenu
     */
    public JMenu getFormatMenu() {
        return formatMenu;
    }

    /**
     * @return Returns the formatToolBar.
     */
    public CommandBar getFormatToolBar() {
        return formatToolBar;
    }

    /**
     * @return insertMenu
     */
    public JMenu getInsertMenu() {
        return insertMenu;
    }

    /**
     * @return Returns the menuBar.
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * @return restituisce il testo dell'editor
     */
    public String getText() {
        String topText;
        topText = removeInvalidTags(wysEditor.getText());
        return topText;
    }

    /**
     * @return JEditorPane
     */
    public JEditorPane getWysEditor() {
        return wysEditor;
    }

    /**
     * Crea i controlli e le action per l'editor HTML.
     */
    private void initUI() {
        createEditors();
        createEditorActions();

        setLayout(new BorderLayout());

        menuBar = new JMenuBar();
        JMenu edit = getEditMenu();
        menuBar.add(edit);
        JMenu format = getFormatMenu();
        menuBar.add(format);
        JMenu insert = getInsertMenu();
        menuBar.add(insert);

        setPreferredSize(new Dimension(200, 100));

        ComponentFactory componentFactory = (ComponentFactory) Application.services()
                .getService(ComponentFactory.class);
        JPanel panelMenu = componentFactory.createPanel(new BorderLayout());
        panelMenu.add(menuBar, BorderLayout.WEST);
        panelMenu.add(formatToolBar, BorderLayout.EAST);
        add(panelMenu, BorderLayout.NORTH);
        add(new JScrollPane(wysEditor), BorderLayout.CENTER);
    }

    /**
     * Aggiunge all'editor una stringa utilizzando l'html editor kit.
     *
     * @param editor
     *            editor in cui scrivere
     * @param html
     *            stringa html da scrivere
     * @param location
     *            posizione del cursore
     */
    public void insertHTML(JEditorPane editor, String html, int location) {
        try {
            if (html == null) {
                return;
            }
            HTMLEditorKit kit = (HTMLEditorKit) editor.getEditorKit();
            Document doc = editor.getDocument();
            StringReader reader = new StringReader(HTMLUtils.jEditorPaneizeHTML(html));
            kit.read(reader, doc, location);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Inserisce del semplice testo nell'editor scelto utilizzando un kit che non considera alcuna formattazione ne
     * carattere.
     *
     * @param editor
     *            editor
     * @param text
     *            text
     * @param location
     *            location
     */
    public void insertText(JEditorPane editor, String text, int location) {
        try {
            DefaultEditorKit kit = new PlainTextEditorKit();
            Document doc = editor.getDocument();
            StringReader reader = new StringReader(text);
            kit.read(reader, doc, location);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Rimuove i tags html non validi.
     *
     * @param html
     *            l'html su cui agire
     * @return l'html senza tag non validi
     */
    private String removeInvalidTags(String html) {
        for (String element : INVALID_TAGS) {
            html = deleteOccurance(html, '<' + element + '>');
            html = deleteOccurance(html, "</" + element + '>');
        }
        if (html != null) {
            html = html.trim();
        }
        return html;
    }

    /**
     * Imposta il cursore ad una data posizione nell'editor.
     *
     * @param pos
     *            la posizione da impostare al cursore
     */
    public void setCaretPosition(int pos) {
        wysEditor.setCaretPosition(pos);
        wysEditor.requestFocusInWindow();
    }

    /**
     * Aggiorna lo stato readOnly del componente che abilita/disabilita lo stesso.
     *
     * @param readOnly
     *            true o false
     */
    public void setReadOnly(boolean readOnly) {
        wysEditor.setEditable(!readOnly);
        for (int i = 0; i < formatToolBar.getComponents().length; i++) {
            formatToolBar.getComponents()[i].setEnabled(!readOnly);
        }
        getEditMenu().setEnabled(!readOnly);
        getFormatMenu().setEnabled(!readOnly);
        getInsertMenu().setEnabled(!readOnly);
        super.setEnabled(!readOnly);
    }

    /**
     * Imposta all'editor il testo passato sovrascrivendo il contenuto.
     *
     * @param text
     *            il testo da settare
     */
    public void setText(String text) {
        String topText = removeInvalidTags(text);
        wysEditor.setText("");
        insertHTML(wysEditor, topText, 0);
        CompoundUndoManager.discardAllEdits(wysEditor.getDocument());
    }

    /**
     * Attiva le azioni per l'editor che possiede il focus.
     */
    private void updateState() {
        actionList.putContextValueForAll(HTMLTextEditAction.EDITOR, focusedEditor);
        actionList.updateEnabledForAll();
    }
}
