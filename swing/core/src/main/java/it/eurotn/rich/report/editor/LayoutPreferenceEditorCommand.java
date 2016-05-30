package it.eurotn.rich.report.editor;

import it.eurotn.rich.report.JecReport;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.AbstractButton;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class LayoutPreferenceEditorCommand extends ActionCommand {

    private JecReport jecReport;

    /**
     *
     * @param jecReport
     *            nome del report.
     */
    public LayoutPreferenceEditorCommand(final JecReport jecReport) {
        super();
        this.jecReport = jecReport;
        // visibile solamente sui report generici.
        // i layout dei report ereditati come jecReportDocumento non possono essere customizzati
        // come i report generici.
        setVisible(JecReport.class.getName().equals(jecReport.getClass().getName()));
    }

    @Override
    protected void doExecuteCommand() {
        StringSelection content = new StringSelection("layout-" + jecReport.getReportName());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(content, content);
        Application.instance().getApplicationContext().publishEvent(new OpenEditorEvent("gestioneStampeEditor"));
    }

    @Override
    protected void onButtonAttached(AbstractButton button) {
        button.setText(jecReport.getReportName());
        super.onButtonAttached(button);
    }

}
