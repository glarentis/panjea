package it.eurotn.rich.exception.handler;

import org.springframework.richclient.util.RcpSupport;

import it.eurotn.dao.exception.DataTooLongException;

public class DataTooLongExceptionHandlerDelegate extends JecMessageDialogExceptionHandler {

    @Override
    public Object createExceptionContent(Throwable throwable) {
        DataTooLongException ex = (DataTooLongException) throwable;
        return RcpSupport.getMessage("", DataTooLongException.class.getName(), "description",
                new Object[] { RcpSupport.getMessage(ex.getColumn()) });
    }
}
