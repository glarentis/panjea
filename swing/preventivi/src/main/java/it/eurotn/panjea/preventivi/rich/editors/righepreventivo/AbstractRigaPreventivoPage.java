package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.form.Form;
import org.springframework.util.Assert;

public abstract class AbstractRigaPreventivoPage<E extends RigaPreventivo> extends FormBackedDialogPageEditor implements
		InitializingBean {

	private AreaPreventivoFullDTO areaPreventivoFullDTO;
	private IPreventiviBD preventiviBD;
	private String securityEtitorID;

	/**
	 * 
	 * @param parentPageId
	 *            pageId
	 * @param backingFormPage
	 *            backingFormPage
	 * 
	 * @param securityEtitorID
	 *            pageSecurityEditorID
	 */
	public AbstractRigaPreventivoPage(final String parentPageId, final Form backingFormPage,
			final String securityEtitorID) {
		super(parentPageId, backingFormPage);
		this.securityEtitorID = securityEtitorID;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(preventiviBD);
	}

	@Override
	protected RigaPreventivo doDelete() {
		RigaPreventivo rigaPreventivo = getBackingFormObjectWithAreaPreventivoFromDTO();
		AreaPreventivo areaPreventivo = preventiviBD.cancellaRigaPreventivo(rigaPreventivo);
		rigaPreventivo.setAreaPreventivo(areaPreventivo);
		return rigaPreventivo;
	}

	@Override
	protected RigaPreventivo doSave() {
		return preventiviBD.salvaRigaPreventivo(getBackingFormObjectWithAreaPreventivoFromDTO(), false);
	}

	/**
	 * @return the areaPreventivoFullDTO
	 */
	protected final AreaPreventivoFullDTO getAreaPreventivoFullDTO() {
		return areaPreventivoFullDTO;
	}

	/**
	 * 
	 * @return FormObject del BackingFormPage a cui viene impostata l'AreaPreventivo del fullDTO corrente.
	 */
	protected RigaPreventivo getBackingFormObjectWithAreaPreventivoFromDTO() {
		RigaPreventivo rigaPreventivo = (RigaPreventivo) getBackingFormPage().getFormObject();
		setAreaPreventivoFromDTOInto(rigaPreventivo);
		return rigaPreventivo;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getNewCommand(),
				toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
		return abstractCommands;
	}

	/**
	 * 
	 * @return logger
	 */
	protected abstract Logger getLogger();

	@Override
	protected Object getNewEditorObject() {
		RigaPreventivo rigaPreventivo = (RigaPreventivo) super.getNewEditorObject();
		setAreaPreventivoFromDTOInto(rigaPreventivo);
		return rigaPreventivo;
	}

	/**
	 * 
	 * @return Nuovo oggetto del tipo corretto gestito dalla pagina concreta
	 */
	protected abstract E getNewRigaPreventivo();

	@Override
	public final String getPageSecurityEditorId() {
		return securityEtitorID;
	}

	/**
	 * @return the preventivoBD
	 */
	protected final IPreventiviBD getPreventivoBD() {
		return preventiviBD;
	}

	@Override
	protected boolean insertControlInScrollPane() {
		return false;
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
	 * 
	 * @param riga
	 *            riga
	 */
	protected final void setAreaPreventivoFromDTOInto(RigaPreventivo riga) {
		riga.setAreaPreventivo(getAreaPreventivoFullDTO().getAreaPreventivo());
	}

	/**
	 * @param areaPreventivoFullDTO
	 *            the areaPreventivoFullDTO to set
	 */
	public final void setAreaPreventivoFullDTO(AreaPreventivoFullDTO areaPreventivoFullDTO) {
		this.areaPreventivoFullDTO = areaPreventivoFullDTO;
	}

	@Override
	public void setFormObject(Object object) {
		if (object == null) {
			getLogger().debug("--> Enter setFormObject with null. Exit immediately.");
			return;
		}

		getLogger().debug("--> Enter setFormObject " + object);

		RigaPreventivo riga = null;
		if (object instanceof RigaPreventivoDTO) {
			RigaPreventivoDTO rigaDTO = (RigaPreventivoDTO) object;
			riga = getNewRigaPreventivo();
			if (rigaDTO.getId() != null) {
				riga.setId(rigaDTO.getId());
				riga = preventiviBD.caricaRigaPreventivo(riga);
			}
		} else {
			riga = (RigaPreventivo) object;
		}

		super.setFormObject(riga);

		if (getBackingFormPage().getFormModel().isValidating()) {
			getBackingFormPage().getFormModel().commit();
		}

		getLogger().debug("--> Exit setFormObject");
	}

	/**
	 * @param preventivoBD
	 *            the preventivoBD to set
	 */
	public final void setPreventiviBD(IPreventiviBD preventivoBD) {
		this.preventiviBD = preventivoBD;
	}
}
