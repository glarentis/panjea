package it.eurotn.panjea.conai.rich.commands;

import it.eurotn.panjea.conai.rich.dialog.CreaModuloDialog;
import it.eurotn.rich.command.OpenEditorCommand;

import org.springframework.richclient.util.RcpSupport;

public class CreaModuloConaiCommand extends OpenEditorCommand {

	@Override
	protected void doExecuteCommand() {
		CreaModuloDialog dialog = (CreaModuloDialog) RcpSupport.getBean("creaModuloDialogPage");
		dialog.setCallingCommand(this);
		dialog.showDialog();
	}

}
