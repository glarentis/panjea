package it.eurotn.panjea.anagrafica.rich.editors.entita.sedicollegate;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.CaricamentoSediEntita;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Crea tutti i componenti per gestire l'inserimento, la visualizzazione e la cancellazione delle sedi magazzino del
 * contratto.
 * 
 * @author fattazzo
 */
public class SediEntitaAssociateHeaderComponent extends AbstractControlFactory {

	/**
	 * Command per aggiungere tutte le sedi dell'entità all'agente.
	 * 
	 */
	public class AddAllSediEntitaCommand extends ApplicationWindowAwareCommand {

		/**
		 * Default constructor.
		 */
		public AddAllSediEntitaCommand() {
			super(ADD_SEDI_ENTITA_COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			EntitaLite entitaLite = (EntitaLite) sedeFormModel.getValueModel("entita").getValue();

			if (entitaLite != null && entitaLite.getId() != null) {
				Entita entita = new Cliente();
				entita.setId(entitaLite.getId());
				List<SedeEntita> sedi = anagraficaBD.caricaSediEntita(null, entita.getId(),
						CaricamentoSediEntita.ESCLUSE_SEDI_SPEDIZIONE_SERVIZI, false);

				for (SedeEntita sedeEntita : sedi) {
					saveClosure.call(sedeEntita);
				}
			}
			sedeFormModel.setFormObject(new EntitaWrapper());
		}
	}

	/**
	 * Command per aggiungere al {@link Contratto} una sede magazzino.
	 * 
	 * @author Leonardo
	 */
	public class AddSedeCommand extends ApplicationWindowAwareCommand {

		/**
		 * Default constructor.
		 */
		public AddSedeCommand() {
			super(ADD_SEDE_COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			SedeEntita sedeEntita = (SedeEntita) sedeFormModel.getValueModel("sedeEntita").getValue();

			saveClosure.call(sedeEntita);

			sedeFormModel.setFormObject(new EntitaWrapper());
		}
	}

	public class EntitaWrapper {
		private EntitaLite entita;
		private SedeEntita sedeEntita;

		/**
		 * 
		 * Costruttore.
		 */
		public EntitaWrapper() {
			entita = new EntitaLite();
			sedeEntita = new SedeEntita();
		}

		/**
		 * @return Returns the entita.
		 */
		public EntitaLite getEntita() {
			return entita;
		}

		/**
		 * @return Returns the sedeEntita.
		 */
		public SedeEntita getSedeEntita() {
			return sedeEntita;
		}

		/**
		 * 
		 * @return tipo enttità cliente
		 */
		public TipoEntita getTipoEntita() {
			return TipoEntita.CLIENTE;
		}

		/**
		 * @param entita
		 *            The entita to set.
		 */
		public void setEntita(EntitaLite entita) {
			this.entita = entita;
		}

		/**
		 * @param sedeEntita
		 *            The sedeEntita to set.
		 */
		public void setSedeEntita(SedeEntita sedeEntita) {
			this.sedeEntita = sedeEntita;
		}

	}

	private static final String ADD_SEDE_COMMAND_ID = "addSedeCommand";
	private static final String ADD_SEDI_ENTITA_COMMAND_ID = "addSediEntitaCommand";

	private AddSedeCommand addSedeCommand = null;
	private AddAllSediEntitaCommand addAllSediEntitaCommand = null;

	protected Agente agente;

	private ValidatingFormModel sedeFormModel;

	private RefreshableValueHolder sediEntitaValueHolder;
	private final IAnagraficaBD anagraficaBD;

	private Closure saveClosure;

	protected JComboBox sediComboBox;

	/**
	 * Costruttore di default.
	 * 
	 * @param anagraficaBD
	 *            anagraficaBD contrattoBD
	 * @param saveClosure
	 *            closure da eseguire sul salvataggio dell'associazione sulla sede entità
	 */
	public SediEntitaAssociateHeaderComponent(final IAnagraficaBD anagraficaBD, final Closure saveClosure) {
		super();
		this.anagraficaBD = anagraficaBD;
		this.saveClosure = saveClosure;
	}

	/**
	 * @return button per aggiungere la sede
	 */
	private JComponent createAddSedeButton() {
		return getAddSedeCommand().createButton();
	}

	/**
	 * Crea tutti i controlli necessari per aggiungere una sede.
	 * 
	 * @return ritorna i controlli del form della sede
	 */
	private JComponent createAddSedeControl() {
		sedeFormModel = (ValidatingFormModel) PanjeaFormModelHelper.createFormModel(new EntitaWrapper(), false,
				"sedeFormModel");
		sedeFormModel.getValueModel("entita").addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				sediEntitaValueHolder.refresh();
				if (sediComboBox.getModel().getSize() > 0) {
					sediComboBox.setSelectedIndex(0);
				}
			}
		});

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) ((BindingFactoryProvider) Application
				.services().getService(BindingFactoryProvider.class)).getBindingFactory(sedeFormModel);
		FormLayout layout = new FormLayout("left:pref,4dlu,fill:pref,4dlu,fill:80dlu", "5dlu,default,2dlu,default,5dlu");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);
		builder.addLabel("entita", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(bf.createBoundSearchText("entita", new String[] {
				"codice", "anagrafica.denominazione" }, new String[] { "tipoEntita" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY }), 3);
		searchPanel.getTextFields().get("codice").setColumns(8);
		searchPanel.getTextFields().get("anagrafica.denominazione").setColumns(25);

		builder.addComponent(getAddAllSediEntitaCommand().createButton(), 5);
		builder.nextRow();

		sediEntitaValueHolder = new RefreshableValueHolder(new Closure() {
			@Override
			public Object call(Object argument) {
				EntitaLite entitaLite = (EntitaLite) sedeFormModel.getValueModel("entita").getValue();

				List<SedeEntita> list = new ArrayList<SedeEntita>();
				// se l'entita' a' stata selezionata carico le sue sedi
				// magazzino
				if (entitaLite != null && entitaLite.getId() != null) {
					Entita entita = new Cliente();
					entita.setId(entitaLite.getId());
					list = anagraficaBD.caricaSediEntita(null, entita.getId(),
							CaricamentoSediEntita.ESCLUSE_SEDI_SPEDIZIONE_SERVIZI, false);
				}
				return list;
			}
		});
		sediEntitaValueHolder.refresh();
		builder.addLabel("sedeEntita", 1);
		sediComboBox = (JComboBox) builder.addBinding(
				bf.createBoundComboBox("sedeEntita", sediEntitaValueHolder, "sede.descrizione"), 3);
		builder.addComponent(createAddSedeButton(), 5);

		return builder.getPanel();
	}

	@Override
	protected JComponent createControl() {
		return createAddSedeControl();
	}

	/**
	 * @return the addAllSediEntitaCommand
	 */
	public AddAllSediEntitaCommand getAddAllSediEntitaCommand() {
		if (addAllSediEntitaCommand == null) {
			addAllSediEntitaCommand = new AddAllSediEntitaCommand();
		}

		return addAllSediEntitaCommand;
	}

	/**
	 * @return AddSedeCommand
	 */
	public AddSedeCommand getAddSedeCommand() {
		if (addSedeCommand == null) {
			addSedeCommand = new AddSedeCommand();
		}
		return addSedeCommand;
	}

	/**
	 * @param agente
	 *            the agente to set
	 */
	public void setAgente(Agente agente) {
		this.agente = agente;
	}

}
