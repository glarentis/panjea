package it.eurotn.panjea.magazzino.rich.editors.contratto.agenti;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.magazzino.domain.RigaContrattoAgente;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ExclusiveCommandGroup;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.selection.dialog.FilterListSelectionDialog;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class RigheContrattoAgenteForm extends PanjeaAbstractForm {

	private class AgenteToggleCommand extends JideToggleCommand {

		private AgenteLite agente;

		/**
		 * Costruttore.
		 * 
		 * @param rigaContrattoAgente
		 *            riga contratto agente a cui si riferisce il command
		 */
		public AgenteToggleCommand(final RigaContrattoAgente rigaContrattoAgente) {
			super();
			this.agente = rigaContrattoAgente.getAgente();
			setIcon(RcpSupport.getIcon(Agente.class.getName()));
		}

		/**
		 * Crea il button del comando.
		 * 
		 * @return button
		 */
		public AbstractButton createAgenteButton() {
			AbstractButton button = super.createButton();

			String agenteDesc = null;
			if (agente != null) {
				agenteDesc = agente.getAnagrafica().getDenominazione();
			} else {
				agenteDesc = "TUTTI";
			}

			if (agenteDesc.length() > 20) {
				agenteDesc = agenteDesc.substring(0, 20) + "...";
			}
			button.setText(agenteDesc);
			button.setHorizontalAlignment(SwingConstants.LEFT);
			return button;
		}

		/**
		 * @return the agente
		 */
		public AgenteLite getAgente() {
			return agente;
		}

		@Override
		protected void onSelection() {
			changeRigaContrattoAgente(this.agente);
		}

	}

	private class AggiungiAgenteCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "aggiungiRigaContrattoAgenteCommand";

		/**
		 * Costruttore.
		 * 
		 */
		public AggiungiAgenteCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			ParametriRicercaEntita parametri = new ParametriRicercaEntita();
			parametri.setAbilitato(true);
			parametri.setTipoEntita(TipoEntita.AGENTE);
			List<EntitaLite> agentiForSelection = anagraficaBD.ricercaEntita(parametri);
			boolean aggiungiTuttiCommandVisible = true;

			RigaContratto rigaContratto = (RigaContratto) getFormObject();
			if (rigaContratto.getId() != null && rigaContratto.getRigheContrattoAgente() != null) {
				for (RigaContrattoAgente rigaContrattoAgente : rigaContratto.getRigheContrattoAgente()) {
					agentiForSelection.remove(rigaContrattoAgente.getAgente());

					if (rigaContrattoAgente.getAgente() == null) {
						aggiungiTuttiCommandVisible = false;
					}
				}
			}

			EventList<EntitaLite> agenti = new BasicEventList<EntitaLite>();
			for (EntitaLite entitaLite : agentiForSelection) {
				agenti.add(entitaLite);
			}
			FilterList<EntitaLite> agentiFilterList = new FilterList<EntitaLite>(agenti);
			FilterListSelectionDialog selectionDialog = new AgentiContrattoSelectionDialog(agentiFilterList,
					aggiungiTuttiCommandVisible) {

				@Override
				protected void onSelect(Object selection) {

					EntitaLite entitaLite = (EntitaLite) selection;
					AgenteLite agenteLite = new AgenteLite();
					agenteLite.setId(entitaLite.getId());
					agenteLite.setVersion(entitaLite.getVersion());
					agenteLite.setCodice(entitaLite.getCodice());
					agenteLite.setDenominazione(entitaLite.getAnagrafica().getDenominazione());

					inserisciRigaContrattoAgente(agenteLite);
				}

				@Override
				public void onSelectAll() {
					inserisciRigaContrattoAgente(null);
				}
			};

			selectionDialog.showDialog();
		}
	}

	public static final String FORM_ID = "righeContrattoAgenteForm";

	private JPanel agentiSelectorComponent;
	private AggiungiAgenteCommand aggiungiAgenteCommand;
	private List<AgenteToggleCommand> agentiCommand;

	private JPanel rigaContrattoComponent;
	private RigaContrattoAgenteForm rigaContrattoAgenteForm;

	private IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param parentFormModel
	 *            parentFormModel
	 */
	public RigheContrattoAgenteForm(final FormModel parentFormModel) {
		super(parentFormModel, FORM_ID);

		agentiSelectorComponent = getComponentFactory().createPanel(new BorderLayout());
		// agentiSelectorComponent.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		// agentiSelectorComponent.setPreferredSize(new Dimension(200, 100));
		aggiungiAgenteCommand = new AggiungiAgenteCommand();
		// new PanjeaFormGuard((ValidatingFormModel) parentFormModel, aggiungiAgenteCommand, FormGuard.ON_NOERRORS);

		rigaContrattoComponent = getComponentFactory().createPanel(new BorderLayout());

		anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
	}

	/**
	 * Cambia la riga contratto agente in modifica.
	 * 
	 * @param agente
	 *            nuova riga contratto agente da gestire
	 */
	private void changeRigaContrattoAgente(AgenteLite agente) {

		if (rigaContrattoAgenteForm == null) {
			rigaContrattoAgenteForm = new RigaContrattoAgenteForm(getFormModel());
			rigaContrattoAgenteForm.getRimuoviRigaContrattoAgenteCommand().addCommandInterceptor(
					new ActionCommandInterceptorAdapter() {
						@Override
						public void postExecution(ActionCommand command) {
							updateAgentiSelectorComponent();
							rigaContrattoComponent.setVisible(false);
						}
					});
			rigaContrattoComponent.add(rigaContrattoAgenteForm.getControl(), BorderLayout.CENTER);
		} else {
			// se avevo già una riga in modifica devo aggiornarla nella riga contratto
			if (rigaContrattoAgenteForm.isDirty()) {
				if (rigaContrattoAgenteForm.getFormModel().isCommittable()) {
					rigaContrattoAgenteForm.commit();
				}
				// risetto la lista delle righe contratto agente per tenere il form dirty e permettere il salvataggio
				// delle
				// modifiche
				List<RigaContrattoAgente> righeAgenti = ((RigaContratto) getFormObject()).getRigheContrattoAgente();
				List<RigaContrattoAgente> listNew = new ArrayList<RigaContrattoAgente>();
				listNew.addAll(righeAgenti);
				getFormModel().getValueModel("righeContrattoAgente").setValue(listNew);
			}

			getFormModel().validate();
		}

		rigaContrattoComponent.setVisible(true);

		List<RigaContrattoAgente> righeAgenti = ((RigaContratto) getFormObject()).getRigheContrattoAgente();
		for (RigaContrattoAgente riga : righeAgenti) {
			if (agente == null && riga.getAgente() == null) {
				rigaContrattoAgenteForm.setFormObject(riga);
				break;
			} else if (agente != null && agente.equals(riga.getAgente())) {
				rigaContrattoAgenteForm.setFormObject(riga);
				break;
			}
		}
	}

	@Override
	protected JComponent createFormControl() {

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		updateAgentiSelectorComponent();

		rootPanel.add(agentiSelectorComponent, BorderLayout.NORTH);
		rootPanel.add(rigaContrattoComponent, BorderLayout.CENTER);

		addFormObjectChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				rigaContrattoComponent.setVisible(false);
				updateAgentiSelectorComponent();
			}
		});

		getFormModel().addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				Boolean enableCommand = !getFormModel().isReadOnly() && !getFormModel().getHasErrors();
				aggiungiAgenteCommand.setEnabled(enableCommand);
			}
		});

		return rootPanel;
	}

	/**
	 * Aggiunge una riga contratto agente alla riga contratto. Se l'agente è <code>null</code> verrà inserita la riga
	 * contratto agente che fa riferimento a tutti gli agenti.
	 * 
	 * @param agente
	 *            agente di riferimento
	 */
	private void inserisciRigaContrattoAgente(AgenteLite agente) {

		RigaContratto rigaContratto = (RigaContratto) getFormObject();

		List<RigaContrattoAgente> righeContrattoAgente = new ArrayList<RigaContrattoAgente>();
		if (rigaContratto.getRigheContrattoAgente() != null) {
			righeContrattoAgente.addAll(rigaContratto.getRigheContrattoAgente());
		}

		RigaContrattoAgente rigaContrattoAgente = new RigaContrattoAgente();
		rigaContrattoAgente.setAgente(agente);
		rigaContrattoAgente.setRigaContratto(rigaContratto);
		righeContrattoAgente.add(rigaContrattoAgente);

		getFormModel().getValueModel("righeContrattoAgente").setValue(righeContrattoAgente);

		updateAgentiSelectorComponent();

		// seleziono il command della riga appena creata
		for (AgenteToggleCommand agenteToggleCommand : agentiCommand) {
			if (agente == null && agenteToggleCommand.getAgente() == null) {
				agenteToggleCommand.setSelected(true);
				break;
			} else if (agente != null && agente.equals(agenteToggleCommand.getAgente())) {
				agenteToggleCommand.setSelected(true);
				break;
			}
		}
	}

	@Override
	public void setFormObject(Object formObject) {
		super.setFormObject(formObject);
	}

	/**
	 * Aggiorna il componente di selezione degli agenti in base alla riga contratto contenuta nel form.
	 */
	private void updateAgentiSelectorComponent() {

		RigaContratto rigaContratto = (RigaContratto) getFormObject();

		agentiSelectorComponent.removeAll();
		agentiCommand = new ArrayList<RigheContrattoAgenteForm.AgenteToggleCommand>();

		ExclusiveCommandGroup buttonGroup = new ExclusiveCommandGroup();

		int col = 1;
		int row = 1;
		FormLayout layout = new FormLayout(
				"fill:pref, 2dlu,fill:pref, 2dlu,fill:pref, 2dlu,fill:pref, 2dlu,fill:pref, 2dlu,fill:pref, 2dlu,fill:pref, 2dlu,fill:pref",
				"default,2dlu,default,2dlu,default,2dlu,default,2dlu,default");
		PanelBuilder builder = new PanelBuilder(layout); // , new FormDebugPanel());
		CellConstraints cc = new CellConstraints();

		if (rigaContratto.getRigheContrattoAgente() != null) {
			for (RigaContrattoAgente rigaContrattoAgente : rigaContratto.getRigheContrattoAgente()) {

				AgenteToggleCommand command = new AgenteToggleCommand(rigaContrattoAgente);
				agentiCommand.add(command);
				AbstractButton button = command.createAgenteButton();
				buttonGroup.add(command, true);

				if (col == 15) {
					col = 1;
					row++;
					row++;
				}
				builder.add(button, cc.xy(col, row));
				col++;
				col++;
			}
		}

		agentiSelectorComponent.add(builder.getPanel(), BorderLayout.CENTER);

		// aggiungo il pulsante per aggiungere gli agenti mancanti
		JPanel aggiungiPanel = getComponentFactory().createPanel(new BorderLayout());
		aggiungiPanel.add(aggiungiAgenteCommand.createButton(), BorderLayout.WEST);
		agentiSelectorComponent.add(aggiungiPanel, BorderLayout.SOUTH);

		agentiSelectorComponent.repaint();

		if (!agentiCommand.isEmpty()) {
			rigaContrattoComponent.setVisible(true);
			agentiCommand.get(0).setSelected(true);
		}
	}
}
