package it.eurotn.rich.exception.handler;

import it.eurotn.panjea.exception.FileSemaforoPresente;

/**
 * @author Leonardo
 *
 */
public class FileSemaforoPresenteDialogExceptionHandler extends JecMessageDialogExceptionHandler {

    @Override
    public Object createExceptionContent(Throwable throwable) {
        FileSemaforoPresente exception = (FileSemaforoPresente) throwable;
        return "File semaforo presente. Cancellare il file " + exception.getFilePath();

    }
}
