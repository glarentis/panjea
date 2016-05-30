package it.eurotn.rich.control.table.command;

import org.springframework.richclient.dialog.MessageDialog;

public class PrintMessageDialog extends MessageDialog {

    /**
     * Costruttore.
     * 
     * @param title
     *            titolo
     * @param message
     *            messaggio
     */
    public PrintMessageDialog(final String title, final String message) {
        super(title, message);
    }

    @Override
    protected Object[] getCommandGroupMembers() {
        return new Object[] {};
    }

    @Override
    protected void onCancel() {
    }

}
