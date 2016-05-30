package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.report.StampaCommand;

import java.util.HashMap;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

public class TemplateSollecitoPage extends FormBackedDialogPageEditor {

	private class StampaSollecitoTestCommand extends StampaCommand {

		public static final String COMMAND_ID = "stampaSollecitoTestCommand";

		/**
		 * Costruttore.
		 *
		 */
		public StampaSollecitoTestCommand() {
			super(COMMAND_ID, COMMAND_ID);
		}

		@Override
		protected Map<Object, Object> getParametri() {
			HashMap<Object, Object> parametri = new HashMap<Object, Object>();
			parametri = new HashMap<Object, Object>();
			parametri.put("sollecitoTest", Boolean.TRUE);
			TemplateSolleciti templateSolleciti = (TemplateSolleciti) getBackingFormPage().getFormObject();
			parametri.put("idTemplateTest", templateSolleciti.getId());
			parametri.put("azienda", aziendaCorrente.getCodice());
			return parametri;
		}

		@Override
		protected String getReportName() {
			return "Template sollecito test";
		}

		@Override
		protected String getReportPath() {
			return "Tesoreria/Solleciti/Sollecito";
		}
	}

	private ITesoreriaBD tesoreriaBD;

	private AziendaCorrente aziendaCorrente;

	private StampaSollecitoTestCommand stampaSollecitoTestCommand;

	/**
	 * costruttore.
	 *
	 * @param pageID
	 *            .
	 */
	public TemplateSollecitoPage(final String pageID) {
		super(pageID, new TemplateSollecitoForm());
	}

	@Override
	protected Object doDelete() {
		tesoreriaBD.cancellaTemplateSolleciti((TemplateSolleciti) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		TemplateSolleciti templateSollecito = (TemplateSolleciti) this.getForm().getFormObject();
		return tesoreriaBD.salvaTemplateSolleciti(templateSollecito);
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { toolbarPageEditor.getNewCommand(), toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
				toolbarPageEditor.getDeleteCommand(), getStampaSollecitoTestCommand() };
	}

	/**
	 * @return the stampaSollecitoTestCommand
	 */
	public StampaSollecitoTestCommand getStampaSollecitoTestCommand() {
		if (stampaSollecitoTestCommand == null) {
			stampaSollecitoTestCommand = new StampaSollecitoTestCommand();
		}

		return stampaSollecitoTestCommand;
	}

	/**
	 * @return the tesoreriaBD
	 */
	public ITesoreriaBD getTesoreriaBD() {
		return tesoreriaBD;
	}

	@Override
	protected boolean insertControlInScrollPane() {
		return false;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}
}
