package it.eurotn.panjea.contabilita.exceptionhandler;

import it.eurotn.panjea.contabilita.service.exception.TipoAreaContabileAssenteException;
import it.eurotn.rich.exception.handler.JecMessageDialogExceptionHandler;

import org.springframework.richclient.util.RcpSupport;

public class TipoAreaContabileAssenteExceptionHandler extends JecMessageDialogExceptionHandler {

	@Override
	public Object createExceptionContent(Throwable throwable) {
		TipoAreaContabileAssenteException ex = (TipoAreaContabileAssenteException) throwable;
		String messaggio = RcpSupport.getMessage("", TipoAreaContabileAssenteException.class.getName(), "description",
				new Object[] { RcpSupport.getMessage(ex.getTipoDocumento().getDescrizione()) });
		return messaggio;
	}
}
