/**
 *
 */
package it.eurotn.panjea.rich.dialogs;

import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.rules.closure.Closure;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;

/**
 * Dialogo di caricamento dove viene eseguita una {@link Closure} in visualizzazione del dialogo nel metodo
 * onAboutToShow() e una volta finito il processo viene chiuso automaticamente il dialogo.
 *
 * @author Leonardo
 *
 */
public class LoadingDialog extends ApplicationDialog {

    /**
     * Funzione da eseguire su un processo separato, usato per processi lunghi che richiedono l'uso intensivo del
     * business layer.
     */
    private Closure runFunction = null;

    /**
     * Parametro della runFunction viene passato al metodo <code>Closure.call(parameterFunction)</code>
     */
    private Object parameterFunction = null;

    /**
     * Loading dialog, dialog di caricamento usato per eseguire processi che occupano pesantemente il business layer per
     * un certo periodo di tempo
     *
     * @param runFunction
     *            la funzione da eseguire dall'apertura del dialogo fino alla sua chiusura automatica a processo
     *            completato, non deve essere null
     * @param parameterFunction
     *            oggetto che passo alla runFunction, puo' essere null.
     */
    public LoadingDialog(final Closure runFunction, final Object parameterFunction) {
        super();
        if (runFunction == null) {
            throw new IllegalArgumentException("The argument runFunction cannot be NULL");
        }
        this.runFunction = runFunction;
        this.parameterFunction = parameterFunction;

        // tolgo i bordi alla finestra per far si che il dialog non venga chiuso, minimizzato o altro
        JDialog.setDefaultLookAndFeelDecorated(true);
        getDialog().getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        getDialog().setResizable(false);
        ((JComponent) getDialog().getContentPane()).setBorder(BorderFactory.createRaisedBevelBorder());
        JDialog.setDefaultLookAndFeelDecorated(false);
    }

    /**
     * Crea i componenti del dialogo con un messaggio di caricamento e una immagine apposta per indicare un processo
     * impegnativo in corso.
     */
    @Override
    protected JComponent createDialogContentPane() {
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        JLabel label = getComponentFactory().createLabel(getMessage("loadingDialog.message", null));
        JLabel imageLabel = new JLabel(getIconSource().getIcon("option"));
        panel.add(imageLabel, BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(300, 100));
        panel.setBorder(BorderFactory.createRaisedBevelBorder());
        return panel;
    }

    /**
     * Non visulaizzo nessun command per questo dialogo
     */
    @Override
    protected Object[] getCommandGroupMembers() {
        return new Object[] {};
    }

    /**
     * Restituisco un title standard
     */
    @Override
    protected String getTitle() {
        return getMessage("loadingDialog.title");
    }

    /**
     * Quando sta per essere visualizzato il dialogo viene chiamata la closure su un processo separato in modo da non
     * bloccare l'interfaccia, ma essendo modale non posso eseguire altre operazioni se non quella di aspettare la
     * conclusione del processo chiamato attraverso la Closure passata sul costruttore che porta alla chiusura
     * automatica del dialogo.
     */
    @Override
    protected void onAboutToShow() {
        AsyncWorker.post(new AsyncTask() {

            @Override
            public void failure(Throwable arg0) {
                dispose();
                PanjeaSwingUtil.checkAndThrowException(arg0);
            }

            @Override
            public Object run() throws Exception {
                // chiamo l'esecuzione della closure ma non passo nessun object alla closure
                runFunction.call(LoadingDialog.this.parameterFunction);
                // oggetto da ritornare al task, non serve ritornare nulla
                return null;
            }

            @Override
            public void success(Object arg0) {
                dispose();
            }
        });
    }

    @Override
    protected void onCancel() {
    }

    /**
     * Posso sempre chiudere il dialogo cosi' facendo quando il processo lanciato separatamente si conclude il dialogo
     * puo' essere chiuso.
     */
    @Override
    protected boolean onFinish() {
        return false;
    }

}
