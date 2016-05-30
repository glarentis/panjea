package it.eurotn.rich.exception.handler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

import it.eurotn.rich.report.exception.JecReportErrorsException;

public class JecReportErrorsExceptionHandler extends MessagesDialogExceptionHandler {

    private class ErrorsDialog extends MessageDialog {

        private JecReportErrorsException exception;

        /**
         * Costruttore.
         *
         * @param errorsException
         *            exception
         */
        public ErrorsDialog(final JecReportErrorsException errorsException) {
            super("Errore di stampa", "_");
            this.exception = errorsException;
        }

        @Override
        protected JComponent createDialogContentPane() {

            JPanel rootPanel = new JPanel(new VerticalLayout(15));

            if (!exception.getReportErrors().getReportsNonTrovatiException().isEmpty()) {
                rootPanel.add(createPanel("Non è stato possibile trovare il layout di stampa per i seguenti documenti:",
                        exception.getReportErrors().getReportsNonTrovatiException()));
            }

            if (!exception.getReportErrors().getReportsMaxPageException().isEmpty()) {
                rootPanel.add(createPanel(
                        "<html>E' stato riscontrato un problema di impaginazione per i seguenti documenti.<br>Per risolverlo provare a cambiare la descrizione o nota di una o più righe.</html>",
                        exception.getReportErrors().getReportsMaxPageException()));
            }

            if (!exception.getReportErrors().getReportsGenericException().isEmpty()) {
                rootPanel.add(createPanel("E' stato riscontrato un problema generico per i seguenti documenti:",
                        exception.getReportErrors().getReportsGenericException()));
            }

            return rootPanel;
        }

        private JComponent createPanel(String title, List<String> reports) {
            JPanel panel = new JPanel(new BorderLayout(0, 5));
            panel.setPreferredSize(new Dimension(480, 120));
            panel.setMaximumSize(new Dimension(480, 120));
            panel.add(new JLabel(title), BorderLayout.NORTH);

            DefaultListModel<String> model = new DefaultListModel<String>();
            for (String err : reports) {
                model.addElement(err);
            }

            JList<String> reportsList = new JList<String>(model);
            panel.add(new JScrollPane(reportsList));

            return panel;
        }
    }

    @Override
    public Object createExceptionContent(Throwable throwable) {
        return new JLabel("");
    }

    @Override
    public void notifyUserAboutException(Thread thread, Throwable throwable) {
        ErrorsDialog dialog = new ErrorsDialog((JecReportErrorsException) throwable);
        dialog.showDialog();
    }
}
