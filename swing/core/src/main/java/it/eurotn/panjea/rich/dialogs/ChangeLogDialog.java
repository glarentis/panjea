/**
 * 
 */
package it.eurotn.panjea.rich.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.core.io.Resource;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.TitledApplicationDialog;
import org.springframework.richclient.text.HtmlPane;
import org.springframework.util.FileCopyUtils;

/**
 * Dialogo per presentare in un HtmlPane scrollable le modifiche eseguite nelle diverse versioni
 * 
 * @author Leonardo
 */
public class ChangeLogDialog extends TitledApplicationDialog {

    private Resource changeLogTextPath;

    /**
     * Creo il dialogo settando titolo, sottotitolo e descrizione della titledDialog
     */
    public ChangeLogDialog() {
        super();
        setTitle(getMessage("changeLogDialog.title"));
        setTitlePaneTitle(getMessage("changeLogDialog.titlePane.title"));
        setDescription(getMessage("changeLogDialog.description"));
    }

    /*
     * @see org.springframework.richclient.dialog.ApplicationDialog#getCommandGroupMembers()
     */
    @Override
    protected Object[] getCommandGroupMembers() {
        // sovrascrivo il metodo per tornare solo il finish command dato che questo dialogo non
        // deve eseguire nessuna operazione se non quella di visualizzare i change logs
        return new AbstractCommand[] { getFinishCommand() };
    }

    /*
     * @see org.springframework.richclient.dialog.TitledApplicationDialog#createTitledDialogContentPane()
     */
    @Override
    protected JComponent createTitledDialogContentPane() {
        // pannello generale
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(600, 300));

        // scrollpane da aggiungere al pannello principale
        JScrollPane scrollPane = getComponentFactory().createScrollPane();
        // a cui aggiungo le scrollbar horiz. e vertic.
        scrollPane.setVerticalScrollBar(scrollPane.createVerticalScrollBar());
        scrollPane.setHorizontalScrollBar(scrollPane.createHorizontalScrollBar());
        scrollPane.setPreferredSize(new Dimension(600, 300));

        // html pane a cui settare il change logs (file di testo scritto in pseudo html (semplificato))
        HtmlPane htmlPane = new HtmlPane();
        htmlPane.setText(getChangeLog());
        htmlPane.setPreferredSize(new Dimension(600, 300));

        // aggiungo l'html pane come view dello scrollpane
        scrollPane.setViewportView(htmlPane);
        // aggiungo al pannello principale lo scroll pane
        panel.add(scrollPane);
        return panel;
    }

    /**
     * Recupera la risorsa Resource definita da changeLogTextPath e ne ritorna la String
     * 
     * @return String
     */
    private String getChangeLog() {
        String changeLog = null;
        try {
            changeLog = FileCopyUtils
                    .copyToString(new BufferedReader(new InputStreamReader(changeLogTextPath.getInputStream())));
        } catch (IOException e) {
            final IllegalStateException exp = new IllegalStateException(
                    "change log text not accessible: " + e.getMessage());
            exp.setStackTrace(e.getStackTrace());
            throw exp;
        }
        return changeLog;
    }

    /**
     * @return Resource
     */
    public Resource getChangeLogTextPath() {
        return changeLogTextPath;
    }

    /**
     * set Resource da cui estrarre i change logs
     * 
     * @param changeLogTextPath
     *            la Resource dei change logs
     */
    public void setChangeLogTextPath(Resource changeLogTextPath) {
        this.changeLogTextPath = changeLogTextPath;
    }

    @Override
    protected boolean onFinish() {
        // non devo fare nulla, e' solo una finestra per mostrare il changelog dell'applicazione
        return true;
    }

}
