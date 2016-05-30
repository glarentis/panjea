/**
 * 
 */
package it.eurotn.panjea.iva.rich.commads.righeiva;

import it.eurotn.panjea.iva.rich.editors.righeiva.RigheIvaTablePage;
import it.eurotn.panjea.iva.rich.editors.righeiva.ValidaRigheIvaConfimationDialog;
import it.eurotn.panjea.iva.rich.forms.righeiva.AbstractAreaIvaModel;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * Command per chiudere la parte iva di un documento; questa operazione risulta essere possibile se i totali delle righe
 * sono quadrati rispetto al totale documento o se viene forzata la chiusura da parte dell'utente tramite un dialogo di
 * conferma.
 * 
 * @author Leonardo
 */
public class CloseRigheIvaCommand extends ActionCommand {

	private static final String ID = ".closeRigheIvaCommand";
	private static final String NON_QUADRATE_RIGHE_IVA_TITLE = ".nonQuadrateRigheIva.title";
	private static final String NON_QUADRATE_RIGHE_IVA_MESSAGE = ".nonQuadrateRigheIva.message";
	private AbstractAreaIvaModel areaIvaModel = null;

	/**
	 * Costruttore.
	 */
	public CloseRigheIvaCommand() {
		super(RigheIvaTablePage.PAGE_ID + ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(RigheIvaTablePage.PAGE_ID + ".controller");
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		org.springframework.util.Assert.notNull(areaIvaModel,
				"Errore, area iva model null, chiamare la setAreaIvaModel");

		if (this.areaIvaModel.isAreaIvaQuadrata()) {
			// se il controllo di quadratura ? positivo valido l'area contabile
			validaRigheIva();
		} else {
			MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);
			// se le righe risultano essere squadrate chiedo se comunque si
			// vuole forzare la validazione dell'area iva
			String title = messageSource.getMessage(RigheIvaTablePage.PAGE_ID + NON_QUADRATE_RIGHE_IVA_TITLE, null,
					Locale.getDefault());
			String message = messageSource.getMessage(RigheIvaTablePage.PAGE_ID + NON_QUADRATE_RIGHE_IVA_MESSAGE, null,
					Locale.getDefault());
			ValidaRigheIvaConfimationDialog dialog = new ValidaRigheIvaConfimationDialog(title, message);
			dialog.showDialog();

			if (dialog.isForce()) {
				// se forza la conferma salvo l'area contabile come validata ma
				// squadrata
				validaRigheIva();
			}
			dialog = null;
		}
	}

	/**
	 * @return AreaIvaModel
	 */
	public AbstractAreaIvaModel getAreaIvaModel() {
		return areaIvaModel;
	}

	/**
	 * @param areaIvaModel
	 *            The areaIvaModel to set.
	 */
	public void setAreaIvaModel(AbstractAreaIvaModel areaIvaModel) {
		this.areaIvaModel = areaIvaModel;
	}

	@Override
	public void setEnabled(boolean enabled) {
		// Se l'area iva è validata sono sempre disabilitato
		if (areaIvaModel.isRigheIvaValide()) {
			enabled = false;
		}
		super.setEnabled(enabled);
	}

	/**
	 * Setta come validata l'area IVA e salva l'area contabile.
	 */
	private void validaRigheIva() {
		this.areaIvaModel.validaAreaIva();
		this.setEnabled(false);
	}
}