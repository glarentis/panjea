/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.schedearticolo;

import it.eurotn.panjea.magazzino.manager.schedearticolo.StampaSchedaArticoloBuilder;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoSchedeArticoloBD;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCreazioneSchedeArticoli;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaElaborazioni;
import it.eurotn.panjea.rich.bd.IPreferenceBD;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.form.PanjeaAbstractForm;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author fattazzo
 * 
 */
public class CreazioneSchedeArticoloPage extends FormBackedDialogPageEditor {

	private class CreaSchedeArticoloCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "creaSchedeArticoloCommand";

		/**
		 * Costruttore.
		 */
		public CreaSchedeArticoloCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			getBackingFormPage().commit();
			final ParametriCreazioneSchedeArticoli parametriCreazioneSchedeArticoli = (ParametriCreazioneSchedeArticoli) getBackingFormPage()
					.getFormObject();

			magazzinoSchedeArticoloBD.creaSchedeArticolo(parametriCreazioneSchedeArticoli);

			// Apro l'editor delle elaborazioni con l'elaborazione attuale
			ParametriRicercaElaborazioni parametri = new ParametriRicercaElaborazioni();
			parametri.setAnno(parametriCreazioneSchedeArticoli.getAnno());
			parametri.setMese(parametriCreazioneSchedeArticoli.getMese());
			parametri.setNota(parametriCreazioneSchedeArticoli.getNote());
			parametri.setEffettuaRicerca(true);
			OpenEditorEvent event = new OpenEditorEvent(parametri);
			Application.instance().getApplicationContext().publishEvent(event);

			// resetto l'editor di creazione
			((PanjeaAbstractForm) getBackingFormPage()).getNewFormObjectCommand().execute();
		}
	}

	private class ResetParametriCreazioneCommand extends ActionCommand {

		private static final String COMMAND_ID = "resetCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public ResetParametriCreazioneCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
			RcpSupport.configure(this);

		}

		@Override
		protected void doExecuteCommand() {
			((PanjeaAbstractForm) getBackingFormPage()).getNewFormObjectCommand().execute();
		}

	}

	private IMagazzinoSchedeArticoloBD magazzinoSchedeArticoloBD;
	private IPreferenceBD preferenceBD;

	public static final String PAGE_ID = "creazioneSchedeArticoloPage";

	private CreaSchedeArticoloCommand creaSchedeArticoloCommand;
	private ResetParametriCreazioneCommand resetParametriCreazioneCommand;

	/**
	 * Costruttore.
	 * 
	 * @param backingFormPage
	 */
	public CreazioneSchedeArticoloPage() {
		super(PAGE_ID, new CreazioneSchedeArticoloForm());
		new PanjeaFormGuard(getBackingFormPage().getFormModel(), getCreaSchedeArticoloCommand());
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { getResetParametriCreazioneCommand(), getCreaSchedeArticoloCommand() };
	}

	/**
	 * @return the CreaSchedeArticoloCommand
	 */
	private CreaSchedeArticoloCommand getCreaSchedeArticoloCommand() {
		if (creaSchedeArticoloCommand == null) {
			creaSchedeArticoloCommand = new CreaSchedeArticoloCommand();
			creaSchedeArticoloCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
				@Override
				public boolean preExecution(ActionCommand command) {
					preferenceBD.caricaPreference(StampaSchedaArticoloBuilder.OUTPUT_BASE_PATH_KEY);
					return true;
				}
			});
		}

		return creaSchedeArticoloCommand;
	}

	/**
	 * @return the resetParametriCreazioneCommand
	 */
	public ResetParametriCreazioneCommand getResetParametriCreazioneCommand() {
		if (resetParametriCreazioneCommand == null) {
			resetParametriCreazioneCommand = new ResetParametriCreazioneCommand();
		}

		return resetParametriCreazioneCommand;
	}

	@Override
	public void loadData() {
		ParametriCreazioneSchedeArticoli parametri = (ParametriCreazioneSchedeArticoli) getBackingFormPage()
				.getFormObject();
		if (parametri.getAnno() == null && parametri.getMese() == null) {
			((PanjeaAbstractForm) getBackingFormPage()).getNewFormObjectCommand().execute();
		}
	}

	@Override
	public void onPostPageOpen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshData() {
		loadData();
	}

	/**
	 * @param magazzinoSchedeArticoloBD
	 *            the magazzinoSchedeArticoloBD to set
	 */
	public void setMagazzinoSchedeArticoloBD(IMagazzinoSchedeArticoloBD magazzinoSchedeArticoloBD) {
		this.magazzinoSchedeArticoloBD = magazzinoSchedeArticoloBD;
	}

	/**
	 * @param preferenceBD
	 *            the preferenceBD to set
	 */
	public void setPreferenceBD(IPreferenceBD preferenceBD) {
		this.preferenceBD = preferenceBD;
	}

}
