package it.eurotn.panjea.tesoreria.rich.editors.acconto;

import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class UtilizzaAccontoCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "utilizzaAccontoCommand";
	public static final String PARAM_ACCONTO = "paramAcconto";

	private final ITesoreriaBD tesoreriaBD;

	/**
	 * Costruttore.
	 * 
	 * @param tesoreriaBD
	 *            tesoreriaBD
	 */
	public UtilizzaAccontoCommand(final ITesoreriaBD tesoreriaBD) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.tesoreriaBD = tesoreriaBD;
	}

	@Override
	protected void doExecuteCommand() {

		AreaAcconto areaAcconto = (AreaAcconto) this.getParameter(PARAM_ACCONTO, null);

		if (areaAcconto != null && areaAcconto.getId() != null) {

			UtilizzoAccontoDialog dialog = new UtilizzoAccontoDialog(tesoreriaBD, areaAcconto);
			dialog.showDialog();
		}
	}

}
