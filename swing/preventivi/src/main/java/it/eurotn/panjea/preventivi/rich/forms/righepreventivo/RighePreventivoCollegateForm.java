package it.eurotn.panjea.preventivi.rich.forms.righepreventivo;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.rich.bd.PreventiviBD;

import java.util.ArrayList;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.util.RcpSupport;

public class RighePreventivoCollegateForm extends
it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RigheCollegateForm {

	private IPreventiviBD preventiviBD;

	/**
	 * Costruttore.
	 *
	 * @param formModel
	 *            form model
	 */
	public RighePreventivoCollegateForm(final FormModel formModel) {
		super(formModel);
		preventiviBD = RcpSupport.getBean(PreventiviBD.BEAN_ID);
	}

	/**
	 * Azzera le righe nella tabella
	 */
	@Override
	public void refreshData() {
		reloadData();
	}

	/**
	 * Carica le righe collegate
	 */
	@Override
	public void reloadData() {
		if (((IDefProperty) getFormObject()).isNew()) {
			righeCollegateTableWidget
					.setRows(new ArrayList<RigaDestinazione>());
			return;
		}
		try {
			righeCollegateTableWidget.getOverlayTable().startSearch();
			righeCollegateTableWidget.setRows(preventiviBD
					.caricaRigheCollegate((RigaPreventivo) getFormObject()));
		} finally {
			righeCollegateTableWidget.getOverlayTable().stopSearch();
		}
	}
}
