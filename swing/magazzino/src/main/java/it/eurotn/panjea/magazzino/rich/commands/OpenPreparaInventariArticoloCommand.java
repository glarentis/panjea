package it.eurotn.panjea.magazzino.rich.commands;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.inventarioarticolo.PreparaInventariArticoloDialog;
import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.rich.command.OpenEditorCommand;

import java.util.Date;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.util.RcpSupport;

public class OpenPreparaInventariArticoloCommand extends OpenEditorCommand {

	@Override
	protected void doExecuteCommand() {

		PreparaInventariArticoloDialog dialog = new PreparaInventariArticoloDialog();
		dialog.setCloseAction(CloseAction.HIDE);
		dialog.showDialog();

		if (dialog.isPreparaInventariConfirmed()) {
			Date data = dialog.getDataPreparazione();
			List<DepositoLite> depositi = dialog.getDepositiSelezionati();

			if (data != null && !depositi.isEmpty()) {
				IMagazzinoDocumentoBD magazzinoDocumentoBD = RcpSupport
						.getBean(MagazzinoDocumentoBD.BEAN_ID);
				magazzinoDocumentoBD.creaInventariArticolo(data, depositi);

				ApplicationPage applicationPage = Application.instance()
						.getActiveWindow().getPage();
				((PanjeaDockingApplicationPage) applicationPage)
						.openResultView("it.eurotn.panjea.magazzino.util.InventarioArticoloDTO");
			}
		}

		dialog = null;
	}

}
