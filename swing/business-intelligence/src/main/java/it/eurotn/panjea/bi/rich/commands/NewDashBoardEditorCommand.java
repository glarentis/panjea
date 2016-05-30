package it.eurotn.panjea.bi.rich.commands;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiForm;
import it.eurotn.rich.command.OpenEditorCommand;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class NewDashBoardEditorCommand extends OpenEditorCommand {

	@Override
	protected void doExecuteCommand() {
		final AnalisiBiForm analisiBiForm = new AnalisiBiForm(new AnalisiBi());
		final DashBoard dashBoard = new DashBoard();
		PanjeaTitledPageApplicationDialog inputDialog = new PanjeaTitledPageApplicationDialog(analisiBiForm, null) {

			@Override
			protected boolean onFinish() {
				AnalisiBi analisiBiToStore = (AnalisiBi) analisiBiForm.getFormObject();
				dashBoard.setNome(analisiBiToStore.getNome());
				dashBoard.setCategoria(analisiBiToStore.getCategoria());
				dashBoard.setDescrizione(analisiBiToStore.getDescrizione());
				LifecycleApplicationEvent event = new OpenEditorEvent(dashBoard);
				Application.instance().getApplicationContext().publishEvent(event);
				return true;
			}
		};
		inputDialog.showDialog();
	}
}
