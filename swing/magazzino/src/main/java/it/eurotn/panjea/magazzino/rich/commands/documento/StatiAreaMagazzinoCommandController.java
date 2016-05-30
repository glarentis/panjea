package it.eurotn.panjea.magazzino.rich.commands.documento;

import javax.swing.AbstractButton;

import org.apache.log4j.Logger;
import org.springframework.binding.value.support.AbstractPropertyChangePublisher;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ExclusiveCommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Controller/Container dei Command incaricati di eseguire la variazione dello stato di {@link AreaMagazzino}.<br>
 * Da questa classe la Page preleva i command per inserirli nella sua toolbar. <br>
 * La classe dovrà essere registrata al {@link IPageLifecycleAdvisor} .OBJECT_CHANGED per intercettare le variazioni
 * {@link AreaMagazzinoFullDTO} <br>
 * e variare di conseguenza lo stato enabled degli {@link ActionCommand} al suo interno
 *
 * @author adriano
 * @version 1.0, 09/ott/2008
 */
public class StatiAreaMagazzinoCommandController extends AbstractPropertyChangePublisher {

	private abstract class StatoAreaMagazzinoCommand extends JideToggleCommand {

		/**
		 * Costruttore.
		 *
		 * @param commandId
		 *            id del comando
		 */
		public StatoAreaMagazzinoCommand(final String commandId) {
			super(commandId);
			final CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
					.getService(CommandConfigurer.class);
			setSecurityControllerId(commandId);
			c.configure(this);
		}

		/**
		 * get dello stato attuale del command.
		 *
		 * @return stato
		 */
		public abstract StatoAreaMagazzino getStatoAreaMagazzino();

		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName(this.getId());
		}

		/**
		 * seleziona lo stato del pulsante in base allo stato dell'area.
		 *
		 * @param statoAreaMagazzino
		 *            stato
		 */
		public void setSelected(StatoAreaMagazzino statoAreaMagazzino) {
			setSelected(getStatoAreaMagazzino().equals(statoAreaMagazzino));
			setEnabled(getStatoAreaMagazzino().equals(statoAreaMagazzino));
		}
	}

	/**
	 * Command per la variazione dello stato di {@link AreaMagazzino} da PROVVISORIO in CONFERMATO.
	 *
	 * @author adriano
	 * @version 1.0, 10/ott/2008
	 */
	public class StatoConfermatoCommand extends StatoAreaMagazzinoCommand {

		public static final String COMMAND_ID = "statoConfermatoCommand";

		/**
		 * Costruttore.
		 *
		 */
		public StatoConfermatoCommand() {
			super(COMMAND_ID);
		}

		@Override
		public StatoAreaMagazzino getStatoAreaMagazzino() {
			return StatoAreaMagazzino.CONFERMATO;
		}
	}

	public class StatoForzatoCommand extends StatoAreaMagazzinoCommand {

		public static final String COMMAND_ID = "statoForzatoCommand";

		/**
		 * Costruttore.
		 *
		 */
		public StatoForzatoCommand() {
			super(COMMAND_ID);
		}

		@Override
		public StatoAreaMagazzino getStatoAreaMagazzino() {
			return StatoAreaMagazzino.FORZATO;
		}

	}

	/**
	 * Command per gestire lo stati INFATTURAZIONE.
	 */
	public class StatoInFatturazioneCommand extends StatoAreaMagazzinoCommand {

		public static final String COMMAND_ID = "statoInFatturazioneCommand";

		/**
		 * Costruttore.
		 *
		 */
		public StatoInFatturazioneCommand() {
			super(COMMAND_ID);
		}

		@Override
		public StatoAreaMagazzino getStatoAreaMagazzino() {
			return StatoAreaMagazzino.INFATTURAZIONE;
		}

	}

	/**
	 * Command per la variazione dello stato di {@link AreaMagazzino} da CONFERMATO in PROVVISORIO.
	 *
	 * @author adriano
	 * @version 1.0, 10/ott/2008
	 */
	public class StatoProvvisorioCommand extends StatoAreaMagazzinoCommand {

		public static final String COMMAND_ID = "statoProvvisorioCommand";

		/**
		 * Costruttore.
		 *
		 */
		public StatoProvvisorioCommand() {
			super(COMMAND_ID);
		}

		@Override
		public StatoAreaMagazzino getStatoAreaMagazzino() {
			return StatoAreaMagazzino.PROVVISORIO;
		}

	}

	public static final String PROPERTY_STATO_AREA_MAGAZZINO = "propertyStatoAreaMagazzinoChange";

	private Logger logger = Logger.getLogger(StatiAreaMagazzinoCommandController.class);

	private ExclusiveCommandGroup statiCommandGroup;
	private StatoProvvisorioCommand statoProvvisorioCommand;
	private StatoConfermatoCommand statoConfermatoCommand;
	private StatoForzatoCommand statoForzatoCommand;
	private StatoInFatturazioneCommand statoInFatturazioneCommand;

	private AreaMagazzino areaMagazzino;

	/**
	 * Costruttore.
	 */
	public StatiAreaMagazzinoCommandController() {
		super();
		initialize();
	}

	/**
	 * @return command del controller
	 */
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getStatoInFatturazioneCommand(), getStatoProvvisorioCommand(),
				getStatoForzatoCommand(), getStatoConfermatoCommand() };
	}

	/**
	 * getter lazy di {@link StatoConfermatoCommand}.
	 *
	 * @return command
	 */
	public StatoConfermatoCommand getStatoConfermatoCommand() {
		if (statoConfermatoCommand == null) {
			statoConfermatoCommand = new StatoConfermatoCommand();
		}
		return statoConfermatoCommand;
	}

	/**
	 * getter lazy di {@link StatoForzatoCommand}.
	 *
	 * @return command
	 */
	public StatoForzatoCommand getStatoForzatoCommand() {
		if (statoForzatoCommand == null) {
			statoForzatoCommand = new StatoForzatoCommand();
		}
		return statoForzatoCommand;
	}

	/**
	 * getter lazy di {@link StatoInFatturazioneCommand}.
	 *
	 * @return command
	 */
	public StatoInFatturazioneCommand getStatoInFatturazioneCommand() {
		if (statoInFatturazioneCommand == null) {
			statoInFatturazioneCommand = new StatoInFatturazioneCommand();
		}
		return statoInFatturazioneCommand;
	}

	/**
	 * getter lazy di {@link StatoProvvisorioCommand}.
	 *
	 * @return command
	 */
	public StatoProvvisorioCommand getStatoProvvisorioCommand() {
		if (statoProvvisorioCommand == null) {
			statoProvvisorioCommand = new StatoProvvisorioCommand();
		}
		return statoProvvisorioCommand;
	}

	/**
	 * Inizializza il controller.
	 */
	private void initialize() {
		statiCommandGroup = new ExclusiveCommandGroup();

		statoInFatturazioneCommand = getStatoInFatturazioneCommand();
		statiCommandGroup.add(statoInFatturazioneCommand);

		statoProvvisorioCommand = getStatoProvvisorioCommand();
		statiCommandGroup.add(statoProvvisorioCommand);

		statoConfermatoCommand = getStatoConfermatoCommand();
		statiCommandGroup.add(statoConfermatoCommand);

		statoForzatoCommand = getStatoForzatoCommand();
		statiCommandGroup.add(statoForzatoCommand);
	}

	/**
	 * selected dei commands all'interno di questa classe in base al valore statoAreaMagazzino dell' AreaMagazzino
	 * corrente se l'area di magazzino è in stato di inserimento disabilita i commands.
	 *
	 */
	protected void setSelected() {
		logger.debug("--> Enter setSelected");
		if (areaMagazzino.isNew()) {
			getStatoConfermatoCommand().setEnabled(false);
			getStatoProvvisorioCommand().setEnabled(false);
			getStatoForzatoCommand().setEnabled(false);
			getStatoInFatturazioneCommand().setEnabled(false);
		} else {
			getStatoConfermatoCommand().setEnabled(true);
			getStatoProvvisorioCommand().setEnabled(true);
			getStatoForzatoCommand().setEnabled(true);
			getStatoInFatturazioneCommand().setEnabled(true);
			getStatoConfermatoCommand().setSelected(areaMagazzino.getStatoAreaMagazzino());
			getStatoProvvisorioCommand().setSelected(areaMagazzino.getStatoAreaMagazzino());
			getStatoForzatoCommand().setSelected(areaMagazzino.getStatoAreaMagazzino());
			getStatoInFatturazioneCommand().setSelected(areaMagazzino.getStatoAreaMagazzino());
		}
		logger.debug("--> Exit setSelected");
	}

	/**
	 *
	 * @param areaMagazzinoUpdated
	 *            area magazzino aggiornata
	 */
	public void updateAreaMagazzino(AreaMagazzino areaMagazzinoUpdated) {
		logger.debug("--> Enter updateAreaMagazzino");
		this.areaMagazzino = areaMagazzinoUpdated;
		setSelected();
		logger.debug("--> Exit StatiAreaMagazzinoController.propertyChange");
	}

}
