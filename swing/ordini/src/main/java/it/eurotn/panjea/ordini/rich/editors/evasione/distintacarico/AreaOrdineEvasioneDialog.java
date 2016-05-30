package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;
import java.util.List;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;

public class AreaOrdineEvasioneDialog extends PanjeaTitledPageApplicationDialog {

	private List<AreaOrdinePM> righeOrdinePM;

	/**
	 * Costruttore.
	 *
	 * @param ordiniPM
	 *            ordini da visualizzare.
	 *
	 */
	public AreaOrdineEvasioneDialog(final List<AreaOrdinePM> ordiniPM) {
		super(new AreaOrdineEvasioneTablePage());
		setPreferredSize(new Dimension(800, 600));
		IPageLifecycleAdvisor page = (IPageLifecycleAdvisor) getDialogPage();
		page.setFormObject(ordiniPM);
		page.loadData();
	}

	/**
	 * @return Returns the righeOrdinePM.
	 */
	public List<AreaOrdinePM> getRigheOrdinePM() {
		return righeOrdinePM;
	}

	@Override
	protected boolean isMessagePaneVisible() {
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected boolean onFinish() {
		righeOrdinePM = ((AbstractTablePageEditor) getDialogPage()).getTable().getRows();

		boolean areeValide = true;
		for (AreaOrdinePM areaOrdinePM : righeOrdinePM) {
			if (areaOrdinePM.getTipoAreaEvasione().getTipoMovimento() == TipoMovimento.TRASFERIMENTO
					&& (areaOrdinePM.getDepositoDestinazione() == null || areaOrdinePM.getDepositoDestinazione()
							.isNew())) {
				areeValide = false;
				break;
			}
		}

		if (!areeValide) {
			new MessageDialog(
					"ATTENZIONE",
					new DefaultMessage(
							"Avvalorare il deposito di destinazione per tutti i documenti di evasione di tipo trasferimento prima di proseguire.",
							Severity.WARNING)).showDialog();
		}

		return areeValide;
	}

}
