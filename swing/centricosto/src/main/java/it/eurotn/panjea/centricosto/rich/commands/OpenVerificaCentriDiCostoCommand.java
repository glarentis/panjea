package it.eurotn.panjea.centricosto.rich.commands;

import it.eurotn.panjea.contabilita.rich.bd.ContabilitaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.rich.command.OpenEditorCommand;

import org.springframework.richclient.util.RcpSupport;

public class OpenVerificaCentriDiCostoCommand extends OpenEditorCommand {

	@Override
	protected void doExecuteCommand() {
		if (getParameter("check") != null) {
			IContabilitaBD contabilitaBD = RcpSupport.getBean(ContabilitaBD.BEAN_ID);
			if (contabilitaBD.verificaRigheSenzaCentriDiCosto().size() == 0) {
				return;
			}
		}
		super.doExecuteCommand();
	}

}
