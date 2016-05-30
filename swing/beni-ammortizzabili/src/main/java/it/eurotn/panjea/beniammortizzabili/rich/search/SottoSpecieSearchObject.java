/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.search;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.search.AbstractSearchObject;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.factory.MenuFactory;

/**
 * 
 * @author adriano
 * @version 1.0, 06/nov/06
 * 
 */
public class SottoSpecieSearchObject extends AbstractSearchObject {

	/**
	 * 
	 * @author adriano
	 * @version 1.0, 05/dic/07
	 * 
	 */
	public class RicercaPerCodiceCommand extends ActionCommand {

		private JRadioButtonMenuItem menuItem;

		/**
		 * Costruttore.
		 */
		public RicercaPerCodiceCommand() {
			super(SottoSpecieSearchObject.SEARCHOBJECT_ID + "." + SottoSpecieSearchObject.RICERCA_CODICE_COMMAND_ID);
		}

		@Override
		public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
				CommandButtonConfigurer buttonConfigurer) {
			menuItem = menuFactory.createRadioButtonMenuItem();
			attach(menuItem, faceDescriptorId, buttonConfigurer);
			menuItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
			return menuItem;
		}

		@Override
		protected void doExecuteCommand() {
			menuItem.setSelected(true);
			SottoSpecieSearchObject.this.ricercaPerDescrizioneCommand.getMenuItem().setSelected(false);
		}

		/**
		 * @return Returns the menuItem.
		 */
		public JRadioButtonMenuItem getMenuItem() {
			return menuItem;
		}

	}

	/**
	 * @author adriano
	 * @version 1.0, 05/dic/07
	 * 
	 */
	public class RicercaPerDescrizioneCommand extends ActionCommand {

		private JRadioButtonMenuItem menuItem;

		/**
		 * 
		 */
		public RicercaPerDescrizioneCommand() {
			super(SottoSpecieSearchObject.SEARCHOBJECT_ID + "."
					+ SottoSpecieSearchObject.RICERCA_DESCRIZIONE_COMMAND_ID);
		}

		@Override
		public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
				CommandButtonConfigurer buttonConfigurer) {
			menuItem = menuFactory.createRadioButtonMenuItem();
			attach(menuItem, faceDescriptorId, buttonConfigurer);
			menuItem.setAccelerator(KeyStroke.getKeyStroke("F6"));
			return menuItem;
		}

		@Override
		protected void doExecuteCommand() {
			menuItem.setSelected(true);
			SottoSpecieSearchObject.this.ricercaPerCodiceCommand.getMenuItem().setSelected(false);
		}

		/**
		 * @return Returns the menuItem.
		 */
		public JRadioButtonMenuItem getMenuItem() {
			return menuItem;
		}
	}

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;
	private RicercaPerCodiceCommand ricercaPerCodiceCommand;

	private RicercaPerDescrizioneCommand ricercaPerDescrizioneCommand;

	private List<AbstractCommand> customCommands;
	private static final String SEARCHOBJECT_ID = "sottoSpecieSearchObject";

	public static final String RICERCA_DESCRIZIONE_COMMAND_ID = "ricercaPerDescrizioneCommand";

	public static final String RICERCA_CODICE_COMMAND_ID = "ricercaPerCodiceCommand";

	/**
	 * Costruttore.
	 */
	public SottoSpecieSearchObject() {
		super(SEARCHOBJECT_ID);
		ricercaPerCodiceCommand = new RicercaPerCodiceCommand();
		ricercaPerDescrizioneCommand = new RicercaPerDescrizioneCommand();

	}

	@Override
	public void configureSearchText(SearchTextField searchTextField) {

		searchTextField.getTextField().getInputMap().put(KeyStroke.getKeyStroke("F5"), ricercaPerCodiceCommand.getId());
		searchTextField.getTextField().getActionMap()
				.put(ricercaPerCodiceCommand.getId(), ricercaPerCodiceCommand.getActionAdapter());

		searchTextField.getTextField().getInputMap()
				.put(KeyStroke.getKeyStroke("F6"), ricercaPerDescrizioneCommand.getId());
		searchTextField.getTextField().getActionMap()
				.put(ricercaPerDescrizioneCommand.getId(), ricercaPerDescrizioneCommand.getActionAdapter());
	}

	/**
	 * @return Returns the beniAmmortizzabiliBD.
	 */
	public IBeniAmmortizzabiliBD getBeniAmmortizzabiliBD() {
		return beniAmmortizzabiliBD;
	}

	@Override
	public List<AbstractCommand> getCustomCommands() {
		if (customCommands == null) {
			final CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			customCommands = new ArrayList<AbstractCommand>();
			// aggiunta action specializzate
			customCommands.add(c.configure(ricercaPerCodiceCommand));
			customCommands.add(c.configure(ricercaPerDescrizioneCommand));
		}
		return customCommands;
	}

	@Override
	public List<?> getData(String fieldSearch, String valueSearch) {
		List<SottoSpecie> list = beniAmmortizzabiliBD.caricaSottoSpecie(fieldSearch, valueSearch);
		return list;
	}

	/**
	 * @return Returns the ricercaPerCodiceCommand.
	 */
	public RicercaPerCodiceCommand getRicercaPerCodiceCommand() {
		return ricercaPerCodiceCommand;
	}

	/**
	 * @return Returns the ricercaPerDescrizioneCommand.
	 */
	public RicercaPerDescrizioneCommand getRicercaPerDescrizioneCommand() {
		return ricercaPerDescrizioneCommand;
	}

	/**
	 * @param beniAmmortizzabiliBD
	 *            The beniAmmortizzabiliBD to set.
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

}
