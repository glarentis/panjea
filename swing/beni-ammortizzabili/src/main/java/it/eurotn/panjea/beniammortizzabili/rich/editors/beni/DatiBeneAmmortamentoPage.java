/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.commands.StampaEtichettaBeniCommand;
import it.eurotn.panjea.beniammortizzabili.rich.forms.bene.FigliBeneAmmortizzabileForm;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

/**
 * Pagina contenente i dati del bene ammortamento.
 *
 * @author Aracno,Leonardo
 * @version 1.0, 02/ott/06
 *
 */
public class DatiBeneAmmortamentoPage extends FormsBackedTabbedDialogPageEditor {

	private class CloneBeneCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "cloneBeneCommand";

		/**
		 * Costruttore.
		 */
		public CloneBeneCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			BeneAmmortizzabile beneAmmortizzabile = (BeneAmmortizzabile) getBackingFormPage().getFormObject();
			beneAmmortizzabile.setCodice(null);
			beneAmmortizzabile.setCodiceBeneEsterno(null);
			beneAmmortizzabile.setId(null);
			beneAmmortizzabile.setUserInsert(null);
			beneAmmortizzabile.setMatricolaAziendale(null);
			beneAmmortizzabile.setMatricolaFornitore(null);

			setFormObject(beneAmmortizzabile);
		}

	}

	private static final String PAGE_ID = "datiBeneAmmortamentoPage";
	private static final String DATI_BENE_FORM_MODEL = "datiBeneAmmortizzabileForm";
	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;
	private String idNuovoBeneCommand;
	private CloneBeneCommand cloneBeneCommand;
	private JLabel beneTestataLabel;

	private StampaEtichettaBeniCommand stampaEtichettaBeniCommand;

	/**
	 * Costruttore di default.
	 */
	public DatiBeneAmmortamentoPage() {
		super(PAGE_ID, new DatiBeneAmmortizzabileForm(PanjeaFormModelHelper.createFormModel(new BeneAmmortizzabile(),
				false, DATI_BENE_FORM_MODEL)));
	}

	@Override
	public void addForms() {
		// Aggiungo il form dei dati civilistici/fiscali del bene ammortizzabile
		ParametriCalcoloBeneAmmortamentoForm parametriCalcoloBeneAmmortamentoForm = new ParametriCalcoloBeneAmmortamentoForm(
				getBackingFormPage().getFormModel());
		addForm(parametriCalcoloBeneAmmortamentoForm);

		// Aggiungo il form dei sotto conti del bene ammortizzabile
		SottoContiBeneForm sottoContiBeneForm = new SottoContiBeneForm(getBackingFormPage().getFormModel());
		addForm(sottoContiBeneForm);

		// Agiiungo il form dei figli del bene
		FigliBeneAmmortizzabileForm figliBeneAmmortizzabileForm = new FigliBeneAmmortizzabileForm(getBackingFormPage()
				.getFormModel());
		addForm(figliBeneAmmortizzabileForm);
	}

	@Override
	protected Object doDelete() {
		BeneAmmortizzabile bene = (BeneAmmortizzabile) getBackingFormPage().getFormObject();
		beniAmmortizzabiliBD.cancellaBeneAmmortizzabile(bene);
		return bene;
	}

	@Override
	protected Object doSave() {
		BeneAmmortizzabile beneDaSalvare = (BeneAmmortizzabile) getBackingFormPage().getFormObject();
		BeneAmmortizzabile beneAmmortizzabileResult = getBeniAmmortizzabiliBD().salvaBeneAmmortizzabile(beneDaSalvare);
		beneTestataLabel.setText(ObjectConverterManager.toString(beneAmmortizzabileResult));
		return beneAmmortizzabileResult;
	}

	/**
	 *
	 * @return beniAmmortizzabiliBD
	 */
	public IBeniAmmortizzabiliBD getBeniAmmortizzabiliBD() {
		return beniAmmortizzabiliBD;
	}

	/**
	 *
	 * @return command per clonare il beneAmmortizzabile
	 */
	public CloneBeneCommand getCloneBeneCommand() {
		if (cloneBeneCommand == null) {
			cloneBeneCommand = new CloneBeneCommand();
		}

		return cloneBeneCommand;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
				toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand(), getCloneBeneCommand(),
				getStampaEtichettaBeniCommand() };
		return abstractCommands;
	}

	@Override
	public JComponent getHeaderTabbedComponent() {
		JPanel topPanel = getComponentFactory().createPanel(new BorderLayout());
		topPanel.setPreferredSize(new Dimension(200, 25));
		beneTestataLabel = getComponentFactory().createLabel("");
		topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		topPanel.add(beneTestataLabel, BorderLayout.CENTER);
		return topPanel;
	}

	/**
	 * @return idNuovoBeneCommand
	 */
	public String getIdNuovoBeneCommand() {
		return idNuovoBeneCommand;
	}

	/**
	 * @return the stampaEtichettaBeniCommand
	 */
	public StampaEtichettaBeniCommand getStampaEtichettaBeniCommand() {
		if (stampaEtichettaBeniCommand == null) {
			stampaEtichettaBeniCommand = new StampaEtichettaBeniCommand();
			stampaEtichettaBeniCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {

				@Override
				public boolean preExecution(ActionCommand command) {
					BeneAmmortizzabile beneAmmortizzabile = (BeneAmmortizzabile) getBackingFormPage().getFormObject();
					BeneAmmortizzabileLite beneLite = new BeneAmmortizzabileLite();
					beneLite.setId(beneAmmortizzabile.getId());

					List<BeneAmmortizzabileLite> listBeni = new ArrayList<BeneAmmortizzabileLite>();
					listBeni.add(beneLite);
					command.addParameter(StampaEtichettaBeniCommand.PARAM_BENI, listBeni);
					return !beneAmmortizzabile.isNew();
				}
			});
		}

		return stampaEtichettaBeniCommand;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onNew() {
		super.onNew();
		beneTestataLabel.setVisible(false);
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void postSetFormObject(Object object) {
		super.postSetFormObject(object);
		beneTestataLabel.setVisible(true);
		beneTestataLabel.setText(ObjectConverterManager.toString(object));
		beneTestataLabel.setIcon(RcpSupport.getIcon(((BeneAmmortizzabile) object).getClass().getName()));
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param beniAmmortizzabiliBD
	 *            the beniAmmortizzabiliBD to set
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	@Override
	public void setFormObject(Object object) {
		BeneAmmortizzabile beneAmmortizzabile = (BeneAmmortizzabile) object;
		super.setFormObject(beneAmmortizzabile);
		toolbarPageEditor.getLockCommand().addEnabledListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				getCloneBeneCommand().setEnabled((Boolean) arg0.getNewValue());
				getStampaEtichettaBeniCommand().setEnabled((Boolean) arg0.getNewValue());
			}
		});
	}

	/**
	 * @param idNuovoBeneCommand
	 *            the idNuovoBeneCommand to set
	 */
	public void setIdNuovoBeneCommand(String idNuovoBeneCommand) {
		this.idNuovoBeneCommand = idNuovoBeneCommand;
	}
}
