/**
 *
 */
package it.eurotn.panjea.contabilita.rich.search;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti.ETipoRicercaSottoConto;
import it.eurotn.rich.search.AbstractSearchObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.factory.MenuFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * SearchObject per la classe SottoConto Presenti.
 * 
 */
public class SottoContoSearchObject extends AbstractSearchObject {

	private class ClientiFiltroCommand extends ActionCommand {

		private JMenuItem menuItem;

		/**
		 * Costruttore.
		 */
		public ClientiFiltroCommand() {
			super(SEARCHOBJECT_ID + "." + "filtroClientiCommand");
		}

		@Override
		public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
				org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
			menuItem = menuFactory.createRadioButtonMenuItem();
			attach(menuItem, faceDescriptorId, buttonConfigurer);
			menuItem.setAccelerator(KeyStroke.getKeyStroke("F10"));
			return menuItem;
		}

		@Override
		protected void doExecuteCommand() {
			parametriRicercaSottoConti.setSottotipoConto(SottotipoConto.CLIENTE);
			menuItem.setSelected(true);
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			groupFiltro.add(button);
		}
	}

	/**
	 * 
	 * @author Adriano
	 */
	private class FornitiFiltroCommand extends ActionCommand {

		private JRadioButtonMenuItem menuItem;

		/**
		 * Costruttore.
		 */
		public FornitiFiltroCommand() {
			super(SEARCHOBJECT_ID + "." + "filtroFornitoriCommand");
		}

		@Override
		public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
				org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
			menuItem = menuFactory.createRadioButtonMenuItem();
			attach(menuItem, faceDescriptorId, buttonConfigurer);
			menuItem.setAccelerator(KeyStroke.getKeyStroke("F11"));
			return menuItem;
		}

		@Override
		protected void doExecuteCommand() {
			parametriRicercaSottoConti.setSottotipoConto(SottotipoConto.FORNITORE);
			menuItem.setSelected(true);
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			groupFiltro.add(button);
		}
	}

	private class OpenEstrattoContoCommand extends ActionCommand {

		private JRadioButtonMenuItem menuItem;

		private AziendaCorrente aziendaCorrente;
		private IDocumentiBD documentiBD;

		/**
		 * Costruttore.
		 */
		public OpenEstrattoContoCommand() {
			super(SottoContoSearchObject.SEARCHOBJECT_ID + ".openEstrattoContoCommand");
			RcpSupport.configure(this);
			this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
			this.documentiBD = RcpSupport.getBean("documentiBD");
		}

		@Override
		public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
				org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
			menuItem = menuFactory.createRadioButtonMenuItem();
			attach(menuItem, faceDescriptorId, buttonConfigurer);
			menuItem.setAccelerator(KeyStroke.getKeyStroke("F12"));
			return menuItem;
		}

		@Override
		protected void doExecuteCommand() {
			SottoConto sottoConto = (SottoConto) SottoContoSearchObject.this.searchPanel.getValueModel().getValue();

			if (sottoConto != null) {
				ParametriRicercaEstrattoConto parametriRicercaEstrattoConto = new ParametriRicercaEstrattoConto();
				parametriRicercaEstrattoConto.setAnnoCompetenza(this.aziendaCorrente.getAnnoContabile());
				parametriRicercaEstrattoConto.getDataRegistrazione().setDataIniziale(
						this.aziendaCorrente.getDataInizioEsercizio());
				parametriRicercaEstrattoConto.getDataRegistrazione().setDataFinale(
						this.aziendaCorrente.getDataFineEsercizio());
				List<TipoDocumento> tipiDocumento = documentiBD.caricaTipiDocumento("codice", null, true);
				parametriRicercaEstrattoConto.getTipiDocumento().addAll(tipiDocumento);
				parametriRicercaEstrattoConto.getStatiAreaContabile().add(AreaContabile.StatoAreaContabile.CONFERMATO);
				parametriRicercaEstrattoConto.getStatiAreaContabile().add(AreaContabile.StatoAreaContabile.VERIFICATO);
				parametriRicercaEstrattoConto.setNumeroTotaleTipiDocumento(tipiDocumento.size());
				parametriRicercaEstrattoConto
						.setSottoConto(contabilitaAnagraficaBD.caricaSottoConto(sottoConto.getId()));
				parametriRicercaEstrattoConto.setEffettuaRicerca(true);
				LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaEstrattoConto);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}

	}

	/**
	 * 
	 * @author adriano
	 * @version 1.0, 25/set/07
	 */
	public class RicercaPerCodiceCommand extends ActionCommand {

		private JRadioButtonMenuItem menuItem;

		/**
		 * Costruttore.
		 */
		public RicercaPerCodiceCommand() {
			super(SottoContoSearchObject.SEARCHOBJECT_ID + "." + SottoContoSearchObject.RICERCA_CODICE_COMMAND_ID);
		}

		@Override
		public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
				org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
			menuItem = menuFactory.createRadioButtonMenuItem();
			attach(menuItem, faceDescriptorId, buttonConfigurer);
			menuItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
			return menuItem;
		}

		@Override
		protected void doExecuteCommand() {
			parametriRicercaSottoConti.setTipoRicercaSottoConto(ETipoRicercaSottoConto.CODICE);
			SottoContoSearchTextField searchText = (SottoContoSearchTextField) searchPanel.getTextFields().get(
					"descrizione");
			searchText.setTipoRicercaSottoConto(ETipoRicercaSottoConto.CODICE, "sottoContoCodice");
			menuItem.setSelected(true);
			if (!getSettings().getString(RICERCA_DEFAULT_SETTINGS_KEY).equals(
					SottoContoSearchObject.RICERCA_CODICE_COMMAND_ID)) {
				getSettings().setString(RICERCA_DEFAULT_SETTINGS_KEY, SottoContoSearchObject.RICERCA_CODICE_COMMAND_ID);
			}
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			groupTipoRicerca.add(button);
		}
	}

	/**
	 * @author Adriano
	 */
	public class RicercaPerDescrizioneCommand extends ActionCommand {

		private JRadioButtonMenuItem menuItem;

		/**
		 * Costruttore.
		 */
		public RicercaPerDescrizioneCommand() {
			super(SottoContoSearchObject.SEARCHOBJECT_ID + "." + SottoContoSearchObject.RICERCA_DESCRIZIONE_COMMAND_ID);
		}

		@Override
		public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
				org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
			menuItem = menuFactory.createRadioButtonMenuItem();
			attach(menuItem, faceDescriptorId, buttonConfigurer);
			menuItem.setAccelerator(KeyStroke.getKeyStroke("F6"));
			return menuItem;
		}

		@Override
		protected void doExecuteCommand() {
			parametriRicercaSottoConti.setTipoRicercaSottoConto(ETipoRicercaSottoConto.DESCRIZIONE);
			SottoContoSearchTextField searchText = (SottoContoSearchTextField) searchPanel.getTextFields().get(
					"descrizione");
			searchText.setTipoRicercaSottoConto(ETipoRicercaSottoConto.DESCRIZIONE, "descrizione");
			menuItem.setSelected(true);
			if (!getSettings().getString(RICERCA_DEFAULT_SETTINGS_KEY).equals(
					SottoContoSearchObject.RICERCA_DESCRIZIONE_COMMAND_ID)) {
				getSettings().setString(RICERCA_DEFAULT_SETTINGS_KEY,
						SottoContoSearchObject.RICERCA_DESCRIZIONE_COMMAND_ID);
				try {
					getSettings().save();
				} catch (IOException e) {
					// non faccio nulla se fa errore a salvare il command di
					// default
					SottoContoSearchObject.logger.warn(
							"Errore nell'impostare il pulsante ricerca per descrizione sottoconto come default", e);
				}
			}
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			groupTipoRicerca.add(button);
		}
	}

	private class VisualizzaTuttiCommand extends ActionCommand {

		private JMenuItem menuItem;

		/**
		 * Costruttore.
		 */
		public VisualizzaTuttiCommand() {
			super(SEARCHOBJECT_ID + "." + "visualizzaTuttiCommand");
		}

		@Override
		public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
				org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
			menuItem = menuFactory.createRadioButtonMenuItem();
			attach(menuItem, faceDescriptorId, buttonConfigurer);
			menuItem.setAccelerator(KeyStroke.getKeyStroke("F9"));
			return menuItem;
		}

		@Override
		protected void doExecuteCommand() {
			parametriRicercaSottoConti.setSottotipoConto(null);
			menuItem.setSelected(true);
		}

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			groupFiltro.add(button);
		}
	}

	public static final String RICERCA_DEFAULT_SETTINGS_KEY = "sottoContoSearchObject.ricercaDefault";
	public static final String FILTRO_SOTTOCONTO_ABILITATO = "filtroSottocontoAbilitato";

	private static Logger logger = Logger.getLogger(SottoContoSearchObject.class);
	private static final String SEARCHOBJECT_ID = "sottoContoSearchObject";
	public static final String RICERCA_CODICE_COMMAND_ID = "ricercaPerCodiceCommand";
	public static final String RICERCA_DESCRIZIONE_COMMAND_ID = "ricercaPerDescrizioneCommand";
	private IContabilitaAnagraficaBD contabilitaAnagraficaBD;
	private ParametriRicercaSottoConti parametriRicercaSottoConti = null;
	private RicercaPerCodiceCommand ricercaPerCodiceCommand = null;
	private ButtonGroup groupFiltro = null;
	private ButtonGroup groupTipoRicerca = null;

	private RicercaPerDescrizioneCommand ricercaPerDescrizioneCommand = null;
	private ClientiFiltroCommand clientiFiltroCommand = null;
	private FornitiFiltroCommand fornitoriFiltroCommand = null;
	private VisualizzaTuttiCommand visualizzaTuttiCommand = null;
	private List<AbstractCommand> customCommands = null;
	private OpenEstrattoContoCommand openEstrattoContoCommand = null;

	/**
	 * Costruttore.
	 */
	public SottoContoSearchObject() {
		super(SEARCHOBJECT_ID);
		parametriRicercaSottoConti = new ParametriRicercaSottoConti();
		groupFiltro = new ButtonGroup();
		groupTipoRicerca = new ButtonGroup();
		this.openEstrattoContoCommand = new OpenEstrattoContoCommand();
	}

	@Override
	public void configureSearchText(it.eurotn.rich.binding.searchtext.SearchTextField searchTextField) {
		super.configureSearchText(searchTextField);

		String commandIdDefault = getSettings().getString(RICERCA_DEFAULT_SETTINGS_KEY);
		if (commandIdDefault.equals(RICERCA_CODICE_COMMAND_ID)) {
			ricercaPerCodiceCommand.execute();
		} else {
			ricercaPerDescrizioneCommand.execute();
		}

		searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F5"), ricercaPerCodiceCommand.getId());
		searchTextField.getTextField().getActionMap()
				.put(ricercaPerCodiceCommand.getId(), ricercaPerCodiceCommand.getActionAdapter());

		searchTextField.getTextField().getInputMap()
				.put(KeyStroke.getKeyStroke("F6"), ricercaPerDescrizioneCommand.getId());
		searchTextField.getTextField().getActionMap()
				.put(ricercaPerDescrizioneCommand.getId(), ricercaPerDescrizioneCommand.getActionAdapter());

		searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F9"), visualizzaTuttiCommand.getId());
		searchTextField.getTextField().getActionMap()
				.put(visualizzaTuttiCommand.getId(), visualizzaTuttiCommand.getActionAdapter());

		searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F10"), clientiFiltroCommand.getId());
		searchTextField.getTextField().getActionMap()
				.put(clientiFiltroCommand.getId(), clientiFiltroCommand.getActionAdapter());

		searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F11"), fornitoriFiltroCommand.getId());
		searchTextField.getTextField().getActionMap()
				.put(fornitoriFiltroCommand.getId(), fornitoriFiltroCommand.getActionAdapter());

		searchTextField.getTextField().getInputMap()
				.put(KeyStroke.getKeyStroke("F12"), openEstrattoContoCommand.getId());
		searchTextField.getTextField().getActionMap()
				.put(openEstrattoContoCommand.getId(), openEstrattoContoCommand.getActionAdapter());

	}

	/**
	 * @return Returns the contabilitaAnagraficaBD.
	 */
	public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
		return contabilitaAnagraficaBD;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		logger.debug("--> Enter getCustomCommands");
		if (customCommands == null) {
			customCommands = new ArrayList<AbstractCommand>();
			// aggiunta action specializzate
			ricercaPerCodiceCommand = new RicercaPerCodiceCommand();
			ricercaPerDescrizioneCommand = new RicercaPerDescrizioneCommand();
			fornitoriFiltroCommand = new FornitiFiltroCommand();
			clientiFiltroCommand = new ClientiFiltroCommand();
			visualizzaTuttiCommand = new VisualizzaTuttiCommand();

			RcpSupport.configure(ricercaPerCodiceCommand);
			RcpSupport.configure(ricercaPerDescrizioneCommand);
			RcpSupport.configure(visualizzaTuttiCommand);
			RcpSupport.configure(clientiFiltroCommand);
			RcpSupport.configure(fornitoriFiltroCommand);

			customCommands.add(ricercaPerCodiceCommand);
			customCommands.add(ricercaPerDescrizioneCommand);
			customCommands.add(visualizzaTuttiCommand);
			customCommands.add(clientiFiltroCommand);
			customCommands.add(fornitoriFiltroCommand);
			customCommands.add(openEstrattoContoCommand);
		}
		logger.debug("--> Exit getCustomCommands");
		return customCommands;
	}

	@Override
	public List<? extends Object> getData(String fieldSearch, String valueSearch) {
		Map<String, Object> parameters = searchPanel.getMapParameters();

		if (parameters.get(FILTRO_SOTTOCONTO_ABILITATO) != null) {
			parametriRicercaSottoConti.setAbilitato(Boolean.TRUE);
		}
		parametriRicercaSottoConti.setValoreDaRicercare(valueSearch);
		List<SottoConto> list = contabilitaAnagraficaBD.ricercaSottoContiSearchObject(parametriRicercaSottoConti);
		return list;
	}

	@Override
	public Object getValueObject(Object object) {
		SottoConto sottoConto = (SottoConto) object;
		sottoConto = contabilitaAnagraficaBD.caricaSottoConto(sottoConto.getId());
		return sottoConto;
	}

	/**
	 * @param contabilitaAnagraficaBD
	 *            The contabilitaAnagraficaBD to set.
	 */
	public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
		this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
	}
}
