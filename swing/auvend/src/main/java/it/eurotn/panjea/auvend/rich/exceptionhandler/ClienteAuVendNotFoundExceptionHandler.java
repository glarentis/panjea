/**
 * 
 */
package it.eurotn.panjea.auvend.rich.exceptionhandler;

import it.eurotn.panjea.auvend.exception.ClienteAuVendNotFoundException;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

/**
 * ExceptionHandler per l'eccezione {@link ClienteAuVendNotFoundException}.
 * 
 * @author adriano
 * @version 1.0, 04/feb/2009
 * 
 */
public class ClienteAuVendNotFoundExceptionHandler extends MessagesDialogExceptionHandler {

	@Override
	public Object createExceptionContent(Throwable throwable) {
		if (throwable instanceof ClienteAuVendNotFoundException) {
			ClienteAuVendNotFoundException clienteAuVendNotFoundException = (ClienteAuVendNotFoundException) throwable;
			String[] codes = new String[] { clienteAuVendNotFoundException.getClass().getName() + ".description" };
			String[] parameters = new String[] { clienteAuVendNotFoundException.getCodiceCliente() };
			return messageSourceAccessor.getMessage(new DefaultMessageSourceResolvable(codes, parameters, codes[0]));
		} else {
			return super.createExceptionContent(throwable);
		}
	}

}
