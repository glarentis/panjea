package it.eurotn.panjea.bi.exceptionhandler;

import javax.swing.JLabel;

import org.springframework.richclient.exceptionhandling.MessagesDialogExceptionHandler;

public class DataWarehouseExceptionHandler extends MessagesDialogExceptionHandler {

	@Override
	public Object createExceptionContent(Throwable throwable) {
		return new JLabel(throwable.getMessage());
	}

}
