package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.locking.IDefProperty;
import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.rich.editors.documento.AbstractEliminaDocumentoCommand;
import it.eurotn.panjea.ordini.rich.editors.areaordine.StatiAreaOrdineCommandController;
import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;
import it.eurotn.panjea.preventivi.rich.bd.ICopiaDocumentoBD;
import it.eurotn.panjea.preventivi.rich.bd.interfaces.IAreaDocumentoBD;
import it.eurotn.panjea.preventivi.rich.forms.areapreventivo.AreaPreventivoForm;
import it.eurotn.panjea.preventivi.rich.forms.interfaces.IRequestFocus;
import it.eurotn.panjea.preventivi.util.interfaces.IAreaDTO;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.editors.documento.StampaAreaDocumentoSplitCommand;
import it.eurotn.panjea.rich.editors.documentograph.OpenDocumentoGraphEditorCommand;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.form.Form;

public abstract class AbstractAreaDocumentoPage<E extends IAreaDocumentoTestata, T extends IAreaDTO<E>> extends
FormsBackedTabbedDialogPageEditor implements InitializingBean, IEditorListener {

	private class AreaDocumentoChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			@SuppressWarnings("unchecked")
			IAreaDTO<E> areaDTO = (IAreaDTO<E>) evt.getNewValue();
			E areaDocumento = areaDTO.getAreaDocumento();
			onAreaDocumentoChanged(areaDocumento);
		}
	}

	private class CopiaAreaDocumentoCommandInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {
			IDefProperty formObject = (IDefProperty) getBackingFormPage().getFormObject();
			command.addParameter(CopiaAreaDocumentoCommand.PARAM_AREA_ID, formObject.getId());
			return true;
		}
	}

	private class DocumentoGraphInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {
			command.addParameter(OpenDocumentoGraphEditorCommand.PARAM_ID_DOCUMENTO, getFormObject().getAreaDocumento()
					.getDocumento().getId());
			return true;
		}
	}

	private class EliminaAreaDocumentoCommandInterceptor implements ActionCommandInterceptor {
		@Override
		public void postExecution(ActionCommand arg0) {
		}

		@Override
		public boolean preExecution(ActionCommand arg0) {

			arg0.addParameter(EliminaAreaDocumentoCommand.PARAM_AREA_DA_CANCELLARE, getFormObject().getAreaDocumento());
			return true;
		}
	}

	public class SaveCommandInterceptor implements ActionCommandInterceptor {

		@Override
		public void postExecution(ActionCommand arg0) {
			AbstractAreaDocumentoPage.this.firePropertyChange(VALIDA_AREA_DOCUMENTO, false, true);
		}

		@Override
		public boolean preExecution(ActionCommand arg0) {
			return true;
		}
	}

	/**
	 * Property change che intercetta il cambio di stato eseguito dalla {@link StatiAreaOrdineCommandController} per
	 * aggiornare le aree presenti nell'editor.
	 */
	private class StatoAreaDocumentoChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			@SuppressWarnings("unchecked")
			E areaDocumento = (E) evt.getNewValue();

			T areaDTO = areaBD.caricaAreaFullDTO(areaDocumento);
			areaDTO.setAreaDocumento(areaDocumento);
			preSetFormObject(areaDTO);
			setFormObject(areaDTO);
			postSetFormObject(areaDTO);
			fireObjectChanged(areaDTO);
		}
	}

	public static final String VALIDA_AREA_DOCUMENTO = "validaRigheDocumento";

	private static Logger logger = Logger.getLogger(AbstractAreaDocumentoPage.class);

	private OpenDocumentoGraphEditorCommand openDocumentoGraphEditorCommand;

	private DocumentoGraphInterceptor documentoGraphInterceptor;

	private CopiaAreaDocumentoCommandInterceptor copiaAreaDocumentoCommandInterceptor;

	private CopiaAreaDocumentoCommand copiaAreaDocumentoCommand;

	private EliminaAreaDocumentoCommand<E> eliminaAreaDocumentoCommand;

	private IAreaDocumentoBD<E, T> areaBD;

	private AreaDocumentoChangeListener areaDocumentoChangeListener;

	private AbstractStatiAreaDocumentoCommandController<E, ?> statiAreaDocumentoCommandController;

	private StatoAreaDocumentoChangeListener statoAreaDocumentoChangeListener;

	private static final String INVALID_RIGHE_TITLE = "invalidRighe.title";

	private static final String INVALID_RIGHE_MESSAGE = "invalidRighe.message";

	private StampaAreaDocumentoSplitCommand stampaDocumentoCommand;

	private EliminaAreaDocumentoCommandInterceptor eliminaAreaDocumentoCommandInterceptor;

	private List<JComponent> componentsPerErrorBar;

	private SaveCommandInterceptor saveCommandInterceptor;

	/**
	 *
	 * @param parentPageId
	 *            parentPageId
	 * @param backingFormPage
	 *            backingFormPage
	 */
	public AbstractAreaDocumentoPage(final String parentPageId, final Form backingFormPage) {
		super(parentPageId, backingFormPage);
	}

	/**
	 *
	 * @param areaDocumento
	 *            areaDocumento
	 */
	private void abilitaCommandCopia(E areaDocumento) {
		if (copiaAreaDocumentoCommand != null) {
			copiaAreaDocumentoCommand.setEnabled(isCommandCopiaDaAbilitare(areaDocumento));
		}
	}

	/**
	 *
	 * @param areaDocumento
	 *            areaDocumento
	 */
	private void abilitaCommandStampa(E areaDocumento) {
		if (stampaDocumentoCommand != null) {
			boolean daAbilitare = isCommandStampaDaAbilitare(areaDocumento);
			stampaDocumentoCommand.setEnabled(daAbilitare);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		installPropertyChange(getForm());
	}

	/**
	 *
	 * @param object
	 *            oggetto da convertire nel valore dto
	 * @return dto
	 */
	// protected abstract T convertFromObject(Object object);

	/**
	 * Controlla se ho un cambio stato dell'area.<br/>
	 * Se ho avuto il cambio avviso.
	 *
	 * @param areaPreventivoFullDTOCheck
	 *            area preventivo da controllare
	 */
	protected void avvisaCambioStato(T areaPreventivoFullDTOCheck) {
		E areaDocumento = areaPreventivoFullDTOCheck.getAreaDocumento();
		if (areaDocumento == null) {
			return;
		}

		E areaDocumentoOld = getFormObject().getAreaDocumento();

		if (areaDocumento.isNew()) {
			this.toolbarPageEditor.getNewCommand().execute();

		} else if (areaDocumento.equals(areaDocumentoOld)) {

			// se la version risulta incrementata,verifico lo stato prima e dopo.
			Object statoOld = areaDocumentoOld.getStato();
			Object stato = areaDocumento.getStato();

			// verifica del cambio dello stato
			if (!statoOld.equals(stato) && isRigheDaInvalidareOnCambioStato(areaDocumento)) {
				String titolo = getMessageSource()
						.getMessage(INVALID_RIGHE_TITLE, new Object[] {}, Locale.getDefault());
				String messaggio = getMessageSource().getMessage(INVALID_RIGHE_MESSAGE, new Object[] {},
						Locale.getDefault());

				Message message = new DefaultMessage(titolo + "\n" + messaggio, Severity.INFO);
				MessageAlert messageAlert = new MessageAlert(message);
				messageAlert.showAlert();

			}

		}
	}

	/**
	 *
	 * @return la lista dei componenti da visualizzare nella barra degli errori sulla destra. Di default contiene il
	 *         command per aprire il grafico dei documenti collegati.
	 */
	protected List<JComponent> creaComponentsDaAggiungereAdErrorBar() {
		componentsPerErrorBar = new ArrayList<JComponent>();
		componentsPerErrorBar.add(getOpenDocumentoGraphEditorCommand().createButton());
		return componentsPerErrorBar;
	}

	/**
	 *
	 * @return nuovo Acontroller contenente i command per la visualizzazione degli stati dell'area documento.
	 */
	protected abstract AbstractStatiAreaDocumentoCommandController<E, ?> creaStatiAreaDocumentoCommandController();

	/**
	 *
	 * @return crea la lista dei command da aggiungere alla toolbar. Di default nuovo, modifica, salva, annulla.
	 */
	protected List<AbstractCommand> createCommandsList() {
		List<AbstractCommand> list = new ArrayList<AbstractCommand>();
		list.add(toolbarPageEditor.getNewCommand());
		list.add(toolbarPageEditor.getLockCommand());
		list.add(toolbarPageEditor.getSaveCommand());
		list.add(toolbarPageEditor.getUndoCommand());
		return list;
	}

	@Override
	public JComponent createControl() {
		JComponent superResult = super.createControl();
		List<JComponent> components = creaComponentsDaAggiungereAdErrorBar();
		if (!components.isEmpty()) {
			JPanel panelCommands = getComponentFactory().createPanel(new HorizontalLayout());
			for (JComponent component : components) {
				panelCommands.add(component);
			}
			getErrorBar().add(panelCommands, BorderLayout.LINE_END);
		}
		return superResult;
	}

	@Override
	public void dispose() {
		if (areaDocumentoChangeListener != null) {
			getForm().removeFormObjectChangeListener(areaDocumentoChangeListener);
		}

		if (eliminaAreaDocumentoCommand != null) {
			eliminaAreaDocumentoCommand.removeCommandInterceptor(eliminaAreaDocumentoCommandInterceptor);
		}

		if (copiaAreaDocumentoCommand != null) {
			copiaAreaDocumentoCommand.removeCommandInterceptor(copiaAreaDocumentoCommandInterceptor);
		}

		if (statiAreaDocumentoCommandController != null) {
			statiAreaDocumentoCommandController.removePropertyChangeListener(
					AbstractStatiAreaDocumentoCommandController.PROPERTY_STATO_AREA_PREVENTIVO,
					statoAreaDocumentoChangeListener);
			statiAreaDocumentoCommandController.dispose();
			statiAreaDocumentoCommandController = null;
		}

		toolbarPageEditor.getSaveCommand().removeCommandInterceptor(saveCommandInterceptor);

		super.dispose();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected T doSave() {
		T areaDTO = getFormObject();
		T cloneDTO = (T) PanjeaSwingUtil.cloneObject(areaDTO);
		cloneDTO = save(cloneDTO);

		statiAreaDocumentoCommandController.setAreaDocumento(cloneDTO.getAreaDocumento());
		return cloneDTO;
	}

	/**
	 * Lancia un propertyChange per IPageLifecycleAdvisor.OBJECT_CHANGED con con oggetto areaDTO.
	 *
	 * @param areaDTO
	 *            areaDTO
	 */
	protected void fireObjectChanged(T areaDTO) {
		firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, areaDTO);
	}

	/**
	 *
	 * @return area documento bd
	 */
	protected IAreaDocumentoBD<E, T> getAreaBD() {
		return this.areaBD;
	}

	/**
	 * @return areaDocumentoChangeListener
	 */
	protected AreaDocumentoChangeListener getAreaDocumentoChangeListener() {
		if (areaDocumentoChangeListener == null) {
			areaDocumentoChangeListener = new AreaDocumentoChangeListener();
		}
		return areaDocumentoChangeListener;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		List<AbstractCommand> list = createCommandsList();
		AbstractCommand[] abstractCommands = new AbstractCommand[list.size()];
		list.toArray(abstractCommands);
		saveCommandInterceptor = new SaveCommandInterceptor();
		toolbarPageEditor.getSaveCommand().addCommandInterceptor(saveCommandInterceptor);
		return abstractCommands;
	}

	/**
	 *
	 * @return copiaAreaDocumentoCommand
	 */
	protected CopiaAreaDocumentoCommand getCopiaAreaDocumentoCommand() {
		if (copiaAreaDocumentoCommand == null && areaBD instanceof ICopiaDocumentoBD) {
			copiaAreaDocumentoCommand = new CopiaAreaDocumentoCommand((ICopiaDocumentoBD) areaBD);
			copiaAreaDocumentoCommand.addCommandInterceptor(getCopiaAreaDocumentoCommandInterceptor());
		}
		return copiaAreaDocumentoCommand;
	}

	/**
	 *
	 * @return copiaAreaDocumentoCommandInterceptor
	 */
	protected CopiaAreaDocumentoCommandInterceptor getCopiaAreaDocumentoCommandInterceptor() {
		if (copiaAreaDocumentoCommandInterceptor == null) {
			copiaAreaDocumentoCommandInterceptor = new CopiaAreaDocumentoCommandInterceptor();
		}
		return copiaAreaDocumentoCommandInterceptor;
	}

	/**
	 * @return Returns the documentoGraphInterceptor.
	 */
	protected DocumentoGraphInterceptor getDocumentoGraphInterceptor() {
		if (documentoGraphInterceptor == null) {
			documentoGraphInterceptor = new DocumentoGraphInterceptor();
		}
		return documentoGraphInterceptor;
	}

	/**
	 * @return command per l'eliminazione dell'area magazzino
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractEliminaDocumentoCommand getEliminaCommand() {
		if (eliminaAreaDocumentoCommand == null) {
			eliminaAreaDocumentoCommand = new EliminaAreaDocumentoCommand();
			eliminaAreaDocumentoCommand.setAreaBD(areaBD);
			eliminaAreaDocumentoCommandInterceptor = new EliminaAreaDocumentoCommandInterceptor();
			eliminaAreaDocumentoCommand.addCommandInterceptor(eliminaAreaDocumentoCommandInterceptor);
		}
		return eliminaAreaDocumentoCommand;
	}

	/**
	 *
	 * @return formobject
	 */
	protected abstract T getFormObject();

	/**
	 *
	 * @return stringa con la property da bindare al property change event per l'onNew
	 */
	protected abstract String getOnNewPropertyChangeEvent();

	/**
	 * @return Returns the openDocumentoGraphEditorCommand.
	 */
	protected OpenDocumentoGraphEditorCommand getOpenDocumentoGraphEditorCommand() {
		if (openDocumentoGraphEditorCommand == null) {
			openDocumentoGraphEditorCommand = new OpenDocumentoGraphEditorCommand();
			openDocumentoGraphEditorCommand.addCommandInterceptor(getDocumentoGraphInterceptor());
		}
		return openDocumentoGraphEditorCommand;
	}

	/**
	 *
	 * @return property change listener per cambio tipo area documento.
	 */
	protected abstract FormModelPropertyChangeListeners getTipoAreaDocumentoPropertyChange();

	/**
	 *
	 * @param areaPreventivoForm
	 *            areaPreventivoForm
	 *
	 */
	protected void installPropertyChange(PanjeaAbstractForm areaPreventivoForm) {
		areaPreventivoForm.addFormObjectChangeListener(getAreaDocumentoChangeListener());
		statoAreaDocumentoChangeListener = new StatoAreaDocumentoChangeListener();
		statiAreaDocumentoCommandController = creaStatiAreaDocumentoCommandController();
		statiAreaDocumentoCommandController.addPropertyChangeListener(
				AbstractStatiAreaDocumentoCommandController.PROPERTY_STATO_AREA_DOCUMENTO,
				statoAreaDocumentoChangeListener);
		setExternalCommandLineEnd(statiAreaDocumentoCommandController.getCommands());
	}

	/**
	 *
	 * @param areaDocumento
	 *            areaDocumento
	 * @return true se l'area non può essere modificata
	 */
	protected boolean isAreaDocumentoToBeReadOnly(E areaDocumento) {
		return false;
	}

	/**
	 *
	 * @param areaDocumento
	 *            areaDocumento
	 * @return true se il documento non è vuoto.
	 */
	protected boolean isCommandCopiaDaAbilitare(E areaDocumento) {
		return !areaDocumento.isNew();
	}

	/**
	 *
	 * @param areaDocumento
	 *            areaDocumento
	 * @return true se il documento non è nuovo.
	 */
	protected boolean isCommandStampaDaAbilitare(E areaDocumento) {
		return !areaDocumento.isNew();
	}

	/**
	 *
	 * @param areaDocumento
	 *            area documento
	 * @return true se il pulsante per la copia del documento può essere copiato.
	 */
	protected boolean isCopiaAreaDocumentoToBeEnabled(E areaDocumento) {
		return copiaAreaDocumentoCommand != null && !areaDocumento.isNew();
	}

	/**
	 * chiamata solo se lo stato dell'area è effettivamente cambiata.
	 *
	 * @param areaDocumentoNuovo
	 *            areaDocumento nuovo
	 * @return true se le righe devnono essere invalidate
	 */
	protected abstract boolean isRigheDaInvalidareOnCambioStato(E areaDocumentoNuovo);

	@Override
	public void loadData() {
	}

	/**
	 *
	 * @param areaDocumento
	 *            aeaDocumento cambiata
	 */
	protected void onAreaDocumentoChanged(E areaDocumento) {
		abilitaCommandStampa(areaDocumento);
		abilitaCommandCopia(areaDocumento);
		for (JComponent component : componentsPerErrorBar) {
			if (component instanceof AbstractLabelDocumento<?>) {
				@SuppressWarnings("unchecked")
				AbstractLabelDocumento<E> label = (AbstractLabelDocumento<E>) component;
				label.visualizzaONascondi(areaDocumento);
			}
		}
	}

	@Override
	public ILock onLock() {
		ILock lock = super.onLock();
		((AreaPreventivoForm) getForm()).requestFocusForFormComponent();
		return lock;
	}

	@Override
	public void onNew() {
		super.onNew();
		T areaDTO = getFormObject();
		E areaDocumento = areaDTO.getAreaDocumento();

		FormModelPropertyChangeListeners propertyChange = getTipoAreaDocumentoPropertyChange();
		// RcpSupport.getBean("areaPreventivo.tipoAreaPreventivoPropertyChange");
		propertyChange.setFormModel(getForm().getFormModel());
		PropertyChangeEvent event = new PropertyChangeEvent(this, getOnNewPropertyChangeEvent(), null,
				areaDocumento.getTipoArea());
		propertyChange.propertyChange(event);
		statiAreaDocumentoCommandController.setAreaDocumento(areaDocumento);

		setTabForm(0);
		if (getForm() instanceof IRequestFocus) {
			((IRequestFocus) getForm()).requestFocusForFormComponent();
		}
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void postSetFormObject(Object object) {
		@SuppressWarnings("unchecked")
		T areaDTO = (T) object;
		E areaDocumento = areaDTO.getAreaDocumento();

		getEliminaCommand().addParameter(EliminaAreaDocumentoCommand.PARAM_AREA_DA_CANCELLARE,
				areaDTO.getAreaDocumento());

		statiAreaDocumentoCommandController.setAreaDocumento(areaDocumento);

		setReadOnly(isAreaDocumentoToBeReadOnly(areaDocumento));
		getNewCommand().setEnabled(true);

		if (copiaAreaDocumentoCommand != null) {
			copiaAreaDocumentoCommand.setEnabled(isCopiaAreaDocumentoToBeEnabled(areaDocumento));
		}
	}

	@Override
	public void preSetFormObject(Object object) {
		@SuppressWarnings("unchecked")
		T areaDTO = (T) object;
		avvisaCambioStato(areaDTO);
		E areaDocumento = areaDTO.getAreaDocumento();
		if (areaDocumento.isNew()) {
			this.toolbarPageEditor.getNewCommand().execute();
		}
	}

	@Override
	public void refreshData() {
	}

	/**
	 * Salva un'area ordine full DTO. se viene modificata la data di consegna devo decidere se aggiornare tutte le righe
	 * o solo le righe con data uguale.
	 *
	 * @param areaDTO
	 *            area ordine da salvare
	 * @return area ordine salvata
	 */
	protected T save(T areaDTO) {
		logger.debug("--> Enter save");

		AreaRate areaRate = null;
		if (areaDTO.isAreaRateEnabled()) {
			areaRate = areaDTO.getAreaRate();
		}
		areaDTO = getAreaBD().salvaAreaDocumento(areaDTO.getAreaDocumento(), areaRate);

		logger.debug("--> Exit save");
		return areaDTO;
	}

	/**
	 * @param areaBD
	 *            the preventiviBD to set
	 */
	protected void setAreaBD(IAreaDocumentoBD<E, T> areaBD) {
		this.areaBD = areaBD;
	}
}
