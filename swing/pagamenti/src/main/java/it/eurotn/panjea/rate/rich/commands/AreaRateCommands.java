/**
 * 
 */
package it.eurotn.panjea.rate.rich.commands;

import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.rich.forms.AbstractAreaRateModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.command.AbstractCommand;

/**
 * Classe container di commands per la customizzazione del modulo di {@link AreaRate} per le diverse aree che la
 * utilizzano.
 * 
 * @author adriano
 * @version 1.0, 17/ott/2008
 * 
 */
public class AreaRateCommands {

	private List<AbstractCommand> commands = new ArrayList<AbstractCommand>();
	private CloseRateCommand confermaRateCommand = null;
	private AbstractAreaRateModel areaRateModel = null;

	/**
	 * Costruttore di default.
	 */
	public AreaRateCommands() {
		super();
		// add(getCloseRateCommand());
	}

	/**
	 * 
	 * @param command
	 *            command da aggiungere
	 */
	public void add(AbstractCommand command) {
		commands.add(command);
	}

	/**
	 * @return Returns the areaPartiteModel.
	 */
	protected AbstractAreaRateModel getAreaRatteModel() {
		return areaRateModel;
	}

	/**
	 * 
	 * @return CloseRatePartiteCommand
	 */
	public CloseRateCommand getCloseRateCommand() {
		if (confermaRateCommand == null) {
			confermaRateCommand = new CloseRateCommand();
		}
		return confermaRateCommand;
	}

	/**
	 * 
	 * @return commands per l'area partite
	 */
	public AbstractCommand[] getCommands() {
		return commands.toArray(new AbstractCommand[0]);
	}

	/**
	 * @param areaRateModel
	 *            The areaRateModel to set.
	 */
	public void setAreaRateModel(AbstractAreaRateModel areaRateModel) {
		this.areaRateModel = areaRateModel;
		getCloseRateCommand().setAreaRateModel(areaRateModel);
	}

	/**
	 * Abilita o disabilita tutti i pulsanti gestiti.
	 * 
	 * @param enabled
	 *            abilita o disabilita i pulsanti
	 */
	public void setEnabled(boolean enabled) {
		for (AbstractCommand command : commands) {
			command.setEnabled(enabled);
		}
		getCloseRateCommand().setEnabled(!areaRateModel.isAreaRateValida());
	}

}
