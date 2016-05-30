package it.eurotn.panjea.bi.rich.editors.analisi.commands;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.rich.bd.BusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiEditorController;
import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiForm;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import javax.swing.SwingWorker;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class SalvaAnalisiCommand extends ApplicationWindowAwareCommand {
	public static final String COMMAND_ID = "DWSalvaAnalisiCommand";
	private AnalisiBiEditorController analisiBiEditorController;

	/**
	 * Costruttore.
	 *
	 * @param analisiBiEditorController
	 *            Istanza della controller dell'editor.
	 */
	public SalvaAnalisiCommand(final AnalisiBiEditorController analisiBiEditorController) {
		super(COMMAND_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(COMMAND_ID);
		c.configure(this);
		this.analisiBiEditorController = analisiBiEditorController;
	}

	@Override
	protected void doExecuteCommand() {
		IBusinessIntelligenceBD bd = RcpSupport.getBean(BusinessIntelligenceBD.BEAN_ID);
		final AnalisiBiForm analisiBiForm = new AnalisiBiForm(analisiBiEditorController.getAnalisiBi(), bd);
		PanjeaTitledPageApplicationDialog inputDialog = new PanjeaTitledPageApplicationDialog(analisiBiForm,
				Application.instance().getActiveWindow().getControl()) {

			@Override
			protected boolean onFinish() {
				analisiBiForm.commit();
				final AnalisiBi analisiBiToStore = (AnalisiBi) analisiBiForm.getFormObject();
				new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						analisiBiEditorController.salvaAnalisi(analisiBiToStore);
						return null;
					}
				}.execute();
				return true;
			}
		};
		inputDialog.showDialog();
	}

}
