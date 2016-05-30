/**
 *
 */
package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.rich.bd.PreventiviBD;
import it.eurotn.panjea.preventivi.rich.editors.evasione.EvasionePreventivoTablePage;
import it.eurotn.panjea.preventivi.util.RigaEvasione;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;
import java.util.List;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

/**
 * @author fattazzo
 *
 */
public class EvadiRighePreventivoCommand extends ActionCommand {

	private class EvasioneDialog extends PanjeaTitledPageApplicationDialog {

		private EvasionePreventivoTablePage evasionePreventivoTablePage;

		/**
		 * Costruttore.
		 */
		public EvasioneDialog() {
			super();
			this.evasionePreventivoTablePage = new EvasionePreventivoTablePage();
			setDialogPage(evasionePreventivoTablePage);
			this.setPreferredSize(new Dimension(1000, 500));
		}

		/**
		 * @return the evasionePreventivoTablePage
		 */
		public EvasionePreventivoTablePage getEvasionePreventivoTablePage() {
			return evasionePreventivoTablePage;
		}

		@Override
		protected String getTitle() {
			return "Evasione preventivo: "
					+ ObjectConverterManager.toString(evasionePreventivoTablePage.getAreaPreventivo().getDocumento(),
							Documento.class, null);
		}

		@Override
		protected boolean isMessagePaneVisible() {
			return false;
		}

		@Override
		protected boolean onFinish() {

			if (!evasionePreventivoTablePage.isDatiEvasioneValid()) {
				MessageDialog dialog = new MessageDialog("ATTENZIONE",
						"Dati inseriti non corretti, impossibile procedere con la creazione dell'ordine.");
				dialog.showDialog();
				return false;
			}

			AreaOrdine areaOrdine = evasionePreventivoTablePage.getAreaOrdineEvasione();
			List<RigaEvasione> righeEvasione = evasionePreventivoTablePage.getTable().getRows();

			preventiviBD.evadiPreventivi(righeEvasione, areaOrdine.getTipoAreaOrdine(),
					areaOrdine.getDepositoOrigine(), areaOrdine.getDocumento().getDataDocumento(),
					areaOrdine.getAgente(), areaOrdine.getDataConsegna());
			return true;
		}

	}

	public static final String PARAM_AREA_PREVENTIVO = "paramAreaPreventivo";

	private EvasioneDialog evasioneDialog;

	private IPreventiviBD preventiviBD;

	{
		evasioneDialog = new EvasioneDialog();
	}

	/**
	 * Costruttore.
	 */
	public EvadiRighePreventivoCommand() {
		super("evadiRighePreventivoCommand");
		RcpSupport.configure(this);
		preventiviBD = RcpSupport.getBean(PreventiviBD.BEAN_ID);
	}

	@Override
	protected void doExecuteCommand() {

		AreaPreventivo areaPreventivo = (AreaPreventivo) getParameter(PARAM_AREA_PREVENTIVO, null);

		if (areaPreventivo != null) {
			evasioneDialog.getEvasionePreventivoTablePage().setFormObject(areaPreventivo);
			evasioneDialog.getEvasionePreventivoTablePage().refreshData();
			evasioneDialog.showDialog();
		}
	}
}
