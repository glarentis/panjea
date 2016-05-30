package it.eurotn.panjea.rich.backup;

import it.eurotn.rich.command.JECCommandGroup;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.springframework.richclient.progress.ProgressMonitor;

import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;

public class BackupEditor extends AbstractEditor implements PropertyChangeListener {

    private BackupExecuteScriptFtpCommand backupExecuteScriptFtpCommand;
    private BackupExecuteScriptDownloadCommand backupExecuteScriptDownloadCommand;
    private JTextArea txtLog = null;
    private JPanel rootPanel;

    @Override
    public JComponent getControl() {
        if (rootPanel == null) {
            rootPanel = new JPanel(new BorderLayout());
            txtLog = new JTextArea();

            backupExecuteScriptDownloadCommand = new BackupExecuteScriptDownloadCommand();
            backupExecuteScriptDownloadCommand.addPropertyChangeListener(this);

            backupExecuteScriptFtpCommand = new BackupExecuteScriptFtpCommand();
            backupExecuteScriptFtpCommand.addPropertyChangeListener(this);

            JECCommandGroup commandGroup = new JECCommandGroup();

            commandGroup.add(backupExecuteScriptDownloadCommand);
            commandGroup.add(backupExecuteScriptFtpCommand);

            rootPanel.add(commandGroup.createToolBar(), BorderLayout.NORTH);
            rootPanel.add(new JScrollPane(txtLog), BorderLayout.CENTER);
        }
        return rootPanel;
    }

    @Override
    public String getId() {
        return "backupEditor";
    }

    @Override
    public void initialize(Object editorObject) {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (BackupExecuteScriptFtpCommand.PROPERTY_CHANGE_LOG.equals(evt.getPropertyName())) {
            txtLog.append(evt.getNewValue().toString());
        }
    }

    @Override
    public void save(ProgressMonitor arg0) {
    }

}
