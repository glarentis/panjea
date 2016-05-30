package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria;

import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.bonifico.GenerazioneBonificoFornitoreDialog;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * Command per la generazione del bonifico fornitore.
 * 
 * @author Leonardo
 */
public class GeneraBonificoFornitoreCommand extends ActionCommand {

	private static final String COMMAND_ID = "generaBonificoFornitoreCommand";

	private AreaPagamenti areaPagamenti;

	private String dialogPageId = null;

	private ITesoreriaBD tesoreriaBD = null;

	/**
	 * Costruttore.
	 * 
	 * @param tesoreriaBD
	 *            bd per la tesoreria
	 */
	public GeneraBonificoFornitoreCommand(final ITesoreriaBD tesoreriaBD) {
		super(COMMAND_ID);
		this.setSecurityControllerId(AreaTesoreriaPage.PAGE_ID + ".controller");
		RcpSupport.configure(this);
		this.tesoreriaBD = tesoreriaBD;
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		org.springframework.util.Assert.notNull(dialogPageId, "dialogPageId cannot be null!");
	}

	@Override
	protected void doExecuteCommand() {
		GenerazioneBonificoFornitoreDialog generazioneDistintaBancariaDialog = new GenerazioneBonificoFornitoreDialog(
				areaPagamenti);
		generazioneDistintaBancariaDialog.setTesoreriaBD(tesoreriaBD);
		generazioneDistintaBancariaDialog.showDialog();
	}

	/**
	 * @return the areaChiusure
	 */
	public AreaChiusure getAreaChiusure() {
		return areaPagamenti;
	}

	/**
	 * @return the dialogPageId
	 */
	public String getDialogPageId() {
		return dialogPageId;
	}

	/**
	 * @param areaPagamenti
	 *            the areaChiusure to set
	 */
	public void setAreaPagamenti(AreaPagamenti areaPagamenti) {
		this.areaPagamenti = areaPagamenti;
	}

	/**
	 * @param dialogPageId
	 *            the dialogPageId to set
	 */
	public void setDialogPageId(String dialogPageId) {
		this.dialogPageId = dialogPageId;
	}

}
