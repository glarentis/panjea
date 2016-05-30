package it.eurotn.panjea.partite.rich.editors.tabelle;

import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.panjea.partite.rich.forms.tabelle.TipoDocumentoBasePartitaForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;

public class TipoDocumentoBasePartitaPage extends FormBackedDialogPageEditor implements InitializingBean {

	private static final String PAGE_ID = "tipoDocumentoBasePartitaPage";

	private IPartiteBD partiteBD = null;

	/**
	 * Costruttore.
	 */
	public TipoDocumentoBasePartitaPage() {
		super(PAGE_ID, new TipoDocumentoBasePartitaForm(new TipoDocumentoBasePartite()));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		org.springframework.util.Assert.notNull(partiteBD, "partiteBD cannot be null!");
	}

	@Override
	protected Object doDelete() {
		partiteBD.cancellaTipoDocumentoBase((TipoDocumentoBasePartite) getBackingFormPage().getFormObject());
		return getBackingFormPage().getFormObject();
	}

	@Override
	protected Object doSave() {
		TipoDocumentoBasePartite tipoDocumentoBase = (TipoDocumentoBasePartite) getBackingFormPage().getFormObject();
		TipoDocumentoBasePartite tipoDocumentoBaseSalvato = partiteBD.salvaTipoDocumentoBase(tipoDocumentoBase);
		return tipoDocumentoBaseSalvato;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	/**
	 * @return the partiteBD
	 */
	public IPartiteBD getPartiteBD() {
		return partiteBD;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param partiteBD
	 *            the partiteBD to set
	 */
	public void setPartiteBD(IPartiteBD partiteBD) {
		this.partiteBD = partiteBD;
	}
}
