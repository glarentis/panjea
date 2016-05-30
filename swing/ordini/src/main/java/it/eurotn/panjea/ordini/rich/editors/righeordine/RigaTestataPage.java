package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.RigaTestata;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.RigaTestataDTO;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.Assert;

public class RigaTestataPage extends FormBackedDialogPageEditor implements InitializingBean {

	public static final String PAGE_ID = "rigaTestataPage";

	private IOrdiniDocumentoBD ordiniDocumentoBD;

	private AreaOrdineFullDTO areaOrdineFullDTO;

	private static Logger logger = Logger.getLogger(RigaTestataPage.class);

	/**
	 * Costruttore.
	 */
	public RigaTestataPage() {
		super(PAGE_ID, new RigaTestataForm());
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(ordiniDocumentoBD);
	}

	@Override
	protected Object doSave() {
		logger.debug("--> Enter doSave");
		RigaTestata rigaTestata = (RigaTestata) getBackingFormPage().getFormObject();
		rigaTestata.setAreaOrdine(areaOrdineFullDTO.getAreaOrdine());
		rigaTestata = (RigaTestata) ordiniDocumentoBD.salvaRigaOrdine(rigaTestata);
		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit doSave " + rigaTestata);
		}
		return rigaTestata;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
				toolbarPageEditor.getDeleteCommand() };

		return abstractCommands;
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return super.getEditorNewCommand();
	}

	@Override
	protected Object getNewEditorObject() {
		RigaTestata rigaTestata = (RigaTestata) super.getNewEditorObject();
		rigaTestata.setAreaOrdine(areaOrdineFullDTO.getAreaOrdine());
		return rigaTestata;
	}

	/**
	 * 
	 * @return bd ordini documento
	 */
	public IOrdiniDocumentoBD getOrdiniDocumentoBD() {
		return ordiniDocumentoBD;
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
	public void preSetFormObject(Object object) {
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
		RigaTestata rigaTestata = new RigaTestata();

		if (object != null) {
			if (object instanceof RigaTestataDTO) {
				RigaTestataDTO rigaTestataDTO = (RigaTestataDTO) object;
				RigaOrdine ra = new RigaTestata();
				if (rigaTestataDTO.getId() != null) {
					ra.setId(rigaTestataDTO.getId());
					rigaTestata = (RigaTestata) ordiniDocumentoBD.caricaRigaOrdine(ra);
				}
			} else if (object instanceof RigaTestata) {
				rigaTestata = (RigaTestata) object;
			}

			RigaTestataPage.super.setFormObject(rigaTestata);
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
	 * 
	 * @param ordiniDocumentoBD
	 *            bd documenti
	 */
	public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

}
