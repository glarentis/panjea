package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria;

import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta.GenerazioneDistintaBancariaDialog;
import it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta.GenerazioneDistintaBancariaPage;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * Command per la generazione della distinta bancaria da una {@link AreaPartiteFullDTO}, se l'area non prevede la
 * gestione degli effetti il command diventa invisibile; all'esecuzione di questo command viene mostrato un dialogo
 * {@link GenerazioneDistintaBancariaDialog} che contiene la {@link GenerazioneDistintaBancariaPage}.
 * 
 * @author Leonardo
 */
public class GeneraDistintaCommand extends ActionCommand {

	private static final String COMMAND_ID = "generaDistintaCommand";

	private AreaEffetti areaEffetti;

	private String dialogPageId = null;

	private ITesoreriaBD tesoreriaBD = null;

	/**
	 * Costruttore.
	 * 
	 * @param tesoreriaBD
	 *            bd per la tesoreria
	 */
	public GeneraDistintaCommand(final ITesoreriaBD tesoreriaBD) {
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
		// nel dialog chiamo la refreshData per aggiornare il treemodel
		GenerazioneDistintaBancariaDialog generazioneDistintaBancariaDialog = new GenerazioneDistintaBancariaDialog(
				areaEffetti);
		generazioneDistintaBancariaDialog.setTesoreriaBD(tesoreriaBD);
		generazioneDistintaBancariaDialog.showDialog();
	}

	/**
	 * @return the areaEffetti
	 */
	public AreaEffetti getAreaEffetti() {
		return areaEffetti;
	}

	/**
	 * @return the dialogPageId
	 */
	public String getDialogPageId() {
		return dialogPageId;
	}

	/**
	 * @param areaEffetti
	 *            the areaEffetti to set
	 */
	public void setAreaEffetti(AreaEffetti areaEffetti) {
		this.areaEffetti = areaEffetti;
	}

	/**
	 * @param dialogPageId
	 *            the dialogPageId to set
	 */
	public void setDialogPageId(String dialogPageId) {
		this.dialogPageId = dialogPageId;
	}

}
