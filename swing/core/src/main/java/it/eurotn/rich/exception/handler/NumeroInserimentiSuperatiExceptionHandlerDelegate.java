package it.eurotn.rich.exception.handler;

import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.sicurezza.license.exception.NumeroInserimentiSuperatiException;

public class NumeroInserimentiSuperatiExceptionHandlerDelegate extends JecMessageDialogExceptionHandler {

    @Override
    public Object createExceptionContent(Throwable throwable) {

        NumeroInserimentiSuperatiException ex = (NumeroInserimentiSuperatiException) throwable;

        return RcpSupport.getMessage("", NumeroInserimentiSuperatiException.class.getName(), "description",
                new Object[] { String.valueOf(ex.getMaxInserimenti()) });
    }
}
