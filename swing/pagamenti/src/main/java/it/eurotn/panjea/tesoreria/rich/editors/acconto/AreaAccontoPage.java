package it.eurotn.panjea.tesoreria.rich.editors.acconto;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.rich.editors.documentograph.OpenDocumentoGraphEditorCommand;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.forms.acconto.AreaAccontoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonLabelInfo;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class AreaAccontoPage extends FormBackedDialogPageEditor {

	public class AreaContabileCommand extends ActionCommand {

		private static final String COMMAND_ID = "areaContabileCommand";
		private AreaContabileLite areaContabileLite;

		/**
		 * Costruttore.
		 */
		public AreaContabileCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			LifecycleApplicationEvent event = new OpenEditorEvent(areaContabileLite);
			Application.instance().getApplicationContext().publishEvent(event);
		}

		/**
		 * 
		 * @return araeContabileLite
		 */
		public AreaContabileLite getAreaContabileLite() {
			return areaContabileLite;
		}

		/**
		 * setter for areacontabilelite.
		 * 
		 * @param areaContabileLite
		 *            areaContabileLite
		 */
		public void setAreaContabileLite(AreaContabileLite areaContabileLite) {
			this.areaContabileLite = areaContabileLite;
			if (areaContabileLite == null) {
				getFaceDescriptor().setCaption("");
				getFaceDescriptor().setIcon(null);
				setVisible(false);
			} else {
				CommandButtonLabelInfo labelInfo = new CommandButtonLabelInfo("");
				getFaceDescriptor().setLabelInfo(labelInfo);
				Icon icon = getIconSource().getIcon(areaContabileLite.getDomainClassName());
				getFaceDescriptor().setIcon(icon);
				setVisible(true);
			}
		}
	}

	private class DocumentoGraphInterceptor extends ActionCommandInterceptorAdapter {

		@Override
		public boolean preExecution(ActionCommand command) {
			command.addParameter(OpenDocumentoGraphEditorCommand.PARAM_ID_DOCUMENTO, ((AreaAcconto) getForm()
					.getFormObject()).getDocumento().getId());
			return true;
		}
	}

	public static final String PAGE_ID = "areaAccontoPage";

	private AreaAcconto areaAcconto;

	private ITesoreriaBD tesoreriaBD;

	private OpenDocumentoGraphEditorCommand openDocumentoGraphEditorCommand = null;

	private AreaContabileCommand areaContabileCommand = null;

	/**
	 * Costruttore.
	 * 
	 */
	public AreaAccontoPage() {
		super(PAGE_ID, new AreaAccontoForm());
	}

	@Override
	public JComponent createControl() {
		JComponent component = super.createControl();
		ComponentFactory componentFactory = (ComponentFactory) Application.services()
				.getService(ComponentFactory.class);
		JPanel panelAreeCollegateCommand = componentFactory.createPanel(new HorizontalLayout());
		// commands di collegamento a aree e documenti collegati
		panelAreeCollegateCommand.add(getOpenDocumentoGraphEditorCommand().createButton());
		panelAreeCollegateCommand.add(getAreaContabileCommand().createButton());

		getErrorBar().add(panelAreeCollegateCommand, BorderLayout.LINE_END);
		return component;
	}

	@Override
	protected Object doDelete() {
		AreaAcconto areaAccontoSave = (AreaAcconto) getBackingFormPage().getFormObject();
		tesoreriaBD.cancellaAcconto(areaAccontoSave);
		return areaAccontoSave;
	}

	@Override
	protected Object doSave() {
		AreaAcconto areaAccontoSave = (AreaAcconto) getBackingFormPage().getFormObject();
		areaAccontoSave.getDocumento().setContrattoSpesometro(null);
		areaAccontoSave = tesoreriaBD.salvaAreaAcconto(areaAccontoSave);
		getAreaContabileCommand().setAreaContabileLite(tesoreriaBD.caricaAreaContabileLite(areaAccontoSave));
		return areaAccontoSave;
	}

	/**
	 * 
	 * @return command per visualizzare l'area contabile legata al documento di acconto.
	 */
	private AreaContabileCommand getAreaContabileCommand() {
		if (areaContabileCommand == null) {
			areaContabileCommand = new AreaContabileCommand();
		}
		return areaContabileCommand;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { toolbarPageEditor.getNewCommand(), toolbarPageEditor.getLockCommand(),
				toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
				toolbarPageEditor.getDeleteCommand() };
	}

	/**
	 * @return Returns the openDocumentoGraphEditorCommand.
	 */
	private OpenDocumentoGraphEditorCommand getOpenDocumentoGraphEditorCommand() {
		if (openDocumentoGraphEditorCommand == null) {
			openDocumentoGraphEditorCommand = new OpenDocumentoGraphEditorCommand();
			openDocumentoGraphEditorCommand.addCommandInterceptor(new DocumentoGraphInterceptor());
		}
		return openDocumentoGraphEditorCommand;
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onNew() {
		super.onNew();

		getAreaContabileCommand().setAreaContabileLite(null);
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
		super.postSetFormObject(object);
	}

	@Override
	public void preSetFormObject(Object object) {
		super.preSetFormObject(object);
		getAreaContabileCommand().setAreaContabileLite(tesoreriaBD.caricaAreaContabileLite((AreaAcconto) object));
	}

	@Override
	public void refreshData() {
	}

	@Override
	public void setFormObject(Object object) {
		this.areaAcconto = (AreaAcconto) object;
		super.setFormObject(object);

		boolean deleteEnabled = this.areaAcconto.getImportoUtilizzato() == null
				|| this.areaAcconto.getImportoUtilizzato().compareTo(BigDecimal.ZERO) == 0;
		// se non l'importo utilizzato è 0 o null
		toolbarPageEditor.getDeleteCommand().setEnabled(deleteEnabled);
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

	/**
	 * 
	 * @param tipoEntita
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(TipoEntita tipoEntita) {
		((AreaAccontoForm) getBackingFormPage()).setTipoEntita(tipoEntita);
	}

}
