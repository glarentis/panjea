package it.eurotn.panjea.lotti.exceptionhandler;

import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

import it.eurotn.panjea.lotti.exception.LottiException;

public class LottiExceptionHandler extends MessagesDialogExceptionHandler {

    @Override
    public void notifyUserAboutException(Thread thread, Throwable throwable) {

        final MessageDialog dialog = new MessageDialog("ATTENZIONE", ((LottiException) throwable).getHTMLMessage());
        dialog.showDialog();
    }

}
