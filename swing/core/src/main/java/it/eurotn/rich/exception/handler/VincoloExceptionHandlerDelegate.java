package it.eurotn.rich.exception.handler;

import org.apache.log4j.Logger;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.dao.exception.VincoloException;

public class VincoloExceptionHandlerDelegate extends JecMessageDialogExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(VincoloExceptionHandlerDelegate.class);

    @Override
    public Object createExceptionContent(Throwable throwable) {
        // Cerco la classe che ha la foreign con "fk" + className.
        // Se non trovo il messaggio per l'errore di foreign key cerco la classe internazionalizzata e lancio un errore

        VincoloException ex = (VincoloException) throwable;
        if ((ex.getVincoloClass() == null || ex.getVincoloClass().isEmpty()) && ex.getMessage() == null) {
            LOGGER.error("-->errore nella vincolo. Non riesco a fare il parser della seguente eccezione", ex);
            return "Cancellazione non eseguita. I dati sono legati ad altri dati.";
        }

        if (ex.getMessage() != null) {
            return ex.getMessage();
        }
        String riga1 = RcpSupport.getMessage("", "fk", "standard");
        String riga2 = RcpSupport.getMessage("", "fk", ex.getVincoloClass());
        if ("fk".equals(riga2)) {
            LOGGER.error(
                    "-->errore messaggio fk mancante. Inserire la seguente chiave\n fk." + ex.getVincoloClass() + "=");
            // Provo con la classe
            riga2 = RcpSupport.getMessage(ex.getVincoloClass());
            if (riga2.isEmpty()) {
                // rimuovo il package
                String[] messaggioSplittato = ex.getVincoloClass().split("\\.");
                riga2 = messaggioSplittato[messaggioSplittato.length - 1];
            }
            riga2 = RcpSupport.getMessage("", "fk", "tipoDato", new Object[] { riga2 });
        }
        return riga1 + riga2;
    }
}
