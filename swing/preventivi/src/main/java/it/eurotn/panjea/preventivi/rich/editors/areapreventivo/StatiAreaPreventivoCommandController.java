package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.rich.editors.righepreventivo.EvadiRighePreventivoCommand;
import it.eurotn.panjea.preventivi.service.exception.ClientePotenzialePresenteException;
import it.eurotn.panjea.preventivi.util.RegoleCambioStatoAreaPreventivo;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.steema.teechart.misc.StringBuilder;

public class StatiAreaPreventivoCommandController extends
		AbstractStatiAreaDocumentoCommandController<AreaPreventivo, StatoAreaPreventivo> implements
		PropertyChangeListener {

	/**
	 * @author fattazzo
	 *
	 */
	private final class ConfermaClientePotenzialeDialog extends ConfirmationDialog {

		private StatoAreaPreventivo nuovoStato;

		private ClientePotenzialePresenteException exception;

		private AreaPreventivo areaPreventivo;

		/**
		 * Costruttore.
		 */
		private ConfermaClientePotenzialeDialog(final StatoAreaPreventivo nuovoStato,
				final ClientePotenzialePresenteException exception) {
			// setto temporaneamente il messaggio a "message" perchè il ConfirmationDialog non ne accetta uno vuoto. Lo
			// costruisco e setto successivamente con l'eccezione.
			super("ATTENZIONE", "message");
			this.nuovoStato = nuovoStato;
			this.exception = exception;

			StringBuilder sb = new StringBuilder(200);
			sb.append("Il preventivo non può passare allo stato ACCETTATO perchè l'entità<b><br><ul><li>");
			sb.append(ObjectConverterManager.toString(exception.getClientePotenzialeLite()));
			sb.append("</li></ul><br></b>risulta essere un cliente potenziale.<br>Renderlo effettivo per proseguire?</b>");
			setConfirmationMessage(sb.toString());

			setPreferredSize(new Dimension(550, 150));
			setCloseAction(CloseAction.HIDE);
		}

		public void disposeDialog() {
			if (getDialog() != null) {
				onWindowClosing();
				disposeDialogContentPane();
				getDialog().dispose();
			}
		}

		/**
		 * @return the areaPreventivo
		 */
		public AreaPreventivo getAreaPreventivo() {
			return areaPreventivo;
		}

		@Override
		protected void onCancel() {
			areaPreventivo = getAreaDocumento();
			super.onCancel();
		}

		@Override
		protected void onConfirm() {
			try {
				anagraficaBD.confermaClientePotenziale(getAreaDocumento().getDocumento().getEntita().getId());
				// ricarico l'area
				AreaPreventivo newArea = preventiviBD.caricaAreaPreventivo(getAreaDocumento());
				areaPreventivo = preventiviBD.cambiaStatoSeApplicabile(newArea, nuovoStato);
			} catch (ClientePotenzialePresenteException e) {
				// non può succedere perchè ho forzato la conferma del cliente potenziale
				logger.error("--> Cliente potenziale sul documento, impossibile cambiare lo stato", e);
			}
		}
	}

	private final IPreventiviBD preventiviBD;
	private IAnagraficaBD anagraficaBD;

	private EvadiRighePreventivoCommand evadiRighePreventivoCommand;

	private ConfermaClientePotenzialeDialog clienteProvvisorioDialog;

	/**
	 * costruttore.
	 *
	 * @param preventiviBD
	 *            preventiviBD
	 */
	StatiAreaPreventivoCommandController(final IPreventiviBD preventiviBD) {
		super(StatoAreaPreventivo.class.getEnumConstants(), StatoAreaPreventivo.class,
				new RegoleCambioStatoAreaPreventivo());
		this.preventiviBD = preventiviBD;
		this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);

		this.addPropertyChangeListener(StatiAreaPreventivoCommandController.PROPERTY_STATO_AREA_DOCUMENTO, this);
	}

	@Override
	protected AreaPreventivo cambiaStatoAreaDocumento(final StatoAreaPreventivo stato) {

		AreaPreventivo areaPreventivo = getAreaDocumento();
		try {
			areaPreventivo = preventiviBD.cambiaStatoSeApplicabile(getAreaDocumento(), stato);
		} catch (ClientePotenzialePresenteException e) {
			this.clienteProvvisorioDialog = new ConfermaClientePotenzialeDialog(stato, e);
			clienteProvvisorioDialog.showDialog();
			areaPreventivo = clienteProvvisorioDialog.getAreaPreventivo();
			clienteProvvisorioDialog.disposeDialog();
			clienteProvvisorioDialog = null;
		}

		return areaPreventivo;
	}

	/**
	 * @return the evadiRighePreventivoCommand
	 */
	private EvadiRighePreventivoCommand getEvadiRighePreventivoCommand() {
		if (evadiRighePreventivoCommand == null) {
			evadiRighePreventivoCommand = new EvadiRighePreventivoCommand();
		}

		return evadiRighePreventivoCommand;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		AreaPreventivo areaPreventivo = (AreaPreventivo) evt.getNewValue();

		if (areaPreventivo != null && areaPreventivo.getTipoAreaPreventivo().isProcessaSuAccettazione()
				&& areaPreventivo.getStatoAreaPreventivo() == StatoAreaPreventivo.ACCETTATO) {
			getEvadiRighePreventivoCommand().addParameter(EvadiRighePreventivoCommand.PARAM_AREA_PREVENTIVO,
					areaPreventivo);
			getEvadiRighePreventivoCommand().execute();
		}
	}

}
