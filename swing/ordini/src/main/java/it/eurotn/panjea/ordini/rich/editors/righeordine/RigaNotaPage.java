package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.ordini.domain.RigaNota;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.forms.righeordine.RigaNotaForm;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.RigaNotaDTO;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.util.Assert;

public class RigaNotaPage extends FormBackedDialogPageEditor implements InitializingBean {

	private static final String PAGE_ID = "rigaNotaPage";

	private static Logger logger = Logger.getLogger(RigaNotaPage.class);

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	private AreaOrdineFullDTO areaOrdineFullDTO;

	/**
	 * Costruttore.
	 */
	public RigaNotaPage() {
		super(PAGE_ID, new RigaNotaForm());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(ordiniDocumentoBD);
	}

	@Override
	protected Object doDelete() {
		RigaOrdine rigaOrdine = (RigaOrdine) getBackingFormPage().getFormObject();
		rigaOrdine.setAreaOrdine(areaOrdineFullDTO.getAreaOrdine());
		AreaOrdine areaOrdine = ordiniDocumentoBD.cancellaRigaOrdine(rigaOrdine);

		rigaOrdine.setAreaOrdine(areaOrdine);
		return rigaOrdine;
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		RigaNota rigaNota = (RigaNota) getBackingFormPage().getFormObject();
		rigaNota.setAreaOrdine(areaOrdineFullDTO.getAreaOrdine());
		rigaNota = (RigaNota) ordiniDocumentoBD.salvaRigaOrdine(rigaNota);
		logger.debug("--> Exit doSave");
		return rigaNota;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getNewCommand(),
				toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
				toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
		return abstractCommands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return super.getEditorNewCommand();
	}

	@Override
	protected Object getNewEditorObject() {
		RigaNota rigaNota = (RigaNota) super.getNewEditorObject();
		rigaNota.setAreaOrdine(areaOrdineFullDTO.getAreaOrdine());
		return rigaNota;
	}

	@Override
	public String getPageSecurityEditorId() {
		return "rigaOrdineNotaPage";
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
	 * @param areaOrdineFullDTO
	 *            The areaOrdineFullDTO to set.
	 */
	public void setAreaOrdineFullDTO(AreaOrdineFullDTO areaOrdineFullDTO) {
		this.areaOrdineFullDTO = areaOrdineFullDTO;
	}

	@Override
	public void setFormObject(Object object) {
		logger.debug("--> SetFormObject " + object);
		RigaNota rigaNota = new RigaNota();
		// la setFormObject mi setta rigaNotaDTO se viene dalla table o rigaArticolo se modifico o salvo una
		// nuova riga; se dto devo caricare la rigaNota altrimenti la imposto come formObject
		if (object != null) {
			if (object instanceof RigaNotaDTO) {
				RigaNotaDTO rigaNotaDTO = (RigaNotaDTO) object;
				RigaOrdine ra = new RigaNota();
				if (rigaNotaDTO.getId() != null) {
					ra.setId(rigaNotaDTO.getId());

					rigaNota = (RigaNota) ordiniDocumentoBD.caricaRigaOrdine(ra);
				}
			} else if (object instanceof RigaNota) {
				rigaNota = (RigaNota) object;
			}

			RigaNotaPage.super.setFormObject(rigaNota);
			/*
			 * HACK controlla che il FormModel sia valido. se risulta tale viene forzato il commit perche' la
			 * setFormObject di questo FormModel imposta l'attributo dirty a true
			 */
			if (getBackingFormPage().getFormModel().isValidating()) {
				getBackingFormPage().getFormModel().commit();
			}
		}

	}

	/**
	 * @param ordiniDocumentoBD
	 *            the ordiniDocumentoBD to set
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}
}
