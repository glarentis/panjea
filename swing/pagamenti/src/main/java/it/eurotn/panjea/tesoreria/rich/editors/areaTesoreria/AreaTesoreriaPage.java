package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria;

import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.rich.editors.documentograph.OpenDocumentoGraphEditorCommand;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.commands.EliminaAreaTesoreriaCommand;
import it.eurotn.panjea.tesoreria.rich.forms.AreaTesoreriaForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.config.CommandButtonLabelInfo;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Pagina che gestisce un' {@link AreaTesoreria}.
 * 
 * @author fattazzo
 * 
 */
public class AreaTesoreriaPage extends FormBackedDialogPageEditor {

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
			command.addParameter(OpenDocumentoGraphEditorCommand.PARAM_ID_DOCUMENTO, ((AreaTesoreria) getForm()
					.getFormObject()).getDocumento().getId());
			return true;
		}
	}

	private class EliminaAreaTesoreriaCommandInterceptor implements ActionCommandInterceptor {
		@Override
		public void postExecution(ActionCommand arg0) {
		}

		@Override
		public boolean preExecution(ActionCommand command) {
			AreaTesoreria areaTesoreria = (AreaTesoreria) getForm().getFormObject();
			command.addParameter(EliminaAreaTesoreriaCommand.PARAM_AREA_TESORERIA, areaTesoreria);

			return true;
		}
	}

	private EliminaAreaTesoreriaCommandInterceptor eliminaAreaTesoreriaCommandInterceptor;

	public static final String PAGE_ID = "areaTesoreriaPage";

	private AreaContabileCommand areaContabileCommand = null;

	protected ITesoreriaBD tesoreriaBD;

	private EliminaAreaTesoreriaCommand eliminaAreaTesoreriaCommand;

	private OpenDocumentoGraphEditorCommand openDocumentoGraphEditorCommand;
	private DocumentoGraphInterceptor documentoGraphInterceptor;

	/**
	 * Costruttore.
	 */
	public AreaTesoreriaPage() {
		super(PAGE_ID, new AreaTesoreriaForm());
	}

	/**
	 * Costruttore per classi derivate.
	 * 
	 * @param pageId
	 *            l'id della page
	 * @param form
	 *            il form con cui costruire la page
	 */
	protected AreaTesoreriaPage(final String pageId, final Form form) {
		super(pageId, form);
	}

	@Override
	public JComponent createControl() {
		JComponent component = super.createControl();
		ComponentFactory componentFactory = (ComponentFactory) Application.services()
				.getService(ComponentFactory.class);
		JPanel panelAreeColegateCommand = componentFactory.createPanel(new HorizontalLayout());
		// commands di collegamento a aree e documenti collegati
		panelAreeColegateCommand.add(getOpenDocumentoGraphEditorCommand().createButton());
		panelAreeColegateCommand.add(getAreaContabileCommand().createButton());

		getErrorBar().add(panelAreeColegateCommand, BorderLayout.LINE_END);
		return component;
	}

	@Override
	protected Object doDelete() {
		tesoreriaBD.cancellaAreaTesoreria((AreaTesoreria) getForm().getFormObject());
		return getForm().getFormObject();
	}

	@Override
	protected Object doSave() {
		return tesoreriaBD.salvaAreaTesoreria((AreaTesoreria) getForm().getFormObject());
	}

	/**
	 * 
	 * @return command per visualizzare l'area contabile legata al documento di tesoreria.
	 */
	private AreaContabileCommand getAreaContabileCommand() {
		if (areaContabileCommand == null) {
			areaContabileCommand = new AreaContabileCommand();
		}
		return areaContabileCommand;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		AbstractCommand[] abstractCommands = new AbstractCommand[] { getEliminaAreaTesoreriaCommand() };
		return abstractCommands;
	}

	/**
	 * @return Returns the documentoGraphInterceptor.
	 */
	private DocumentoGraphInterceptor getDocumentoGraphInterceptor() {
		if (documentoGraphInterceptor == null) {
			documentoGraphInterceptor = new DocumentoGraphInterceptor();
		}

		return documentoGraphInterceptor;
	}

	@Override
	public AbstractCommand getEditorDeleteCommand() {
		return getEliminaAreaTesoreriaCommand();
	}

	@Override
	public AbstractCommand getEditorNewCommand() {
		return null;
	}

	/**
	 * @return EliminaAreaTesoreriaCommand
	 */
	private EliminaAreaTesoreriaCommand getEliminaAreaTesoreriaCommand() {
		if (eliminaAreaTesoreriaCommand == null) {
			eliminaAreaTesoreriaCommand = new EliminaAreaTesoreriaCommand();
			eliminaAreaTesoreriaCommand.setTesoreriaBD(tesoreriaBD);
			eliminaAreaTesoreriaCommandInterceptor = new EliminaAreaTesoreriaCommandInterceptor();
			eliminaAreaTesoreriaCommand.addCommandInterceptor(eliminaAreaTesoreriaCommandInterceptor);
		}
		return eliminaAreaTesoreriaCommand;
	}

	/**
	 * @return Returns the openDocumentoGraphEditorCommand.
	 */
	private OpenDocumentoGraphEditorCommand getOpenDocumentoGraphEditorCommand() {
		if (openDocumentoGraphEditorCommand == null) {
			openDocumentoGraphEditorCommand = new OpenDocumentoGraphEditorCommand();
			openDocumentoGraphEditorCommand.addCommandInterceptor(getDocumentoGraphInterceptor());
		}

		return openDocumentoGraphEditorCommand;
	}

	/**
	 * @return the tesoreriaBD
	 */
	public ITesoreriaBD getTesoreriaBD() {
		return tesoreriaBD;
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
		super.preSetFormObject(object);
		AreaTesoreria areaTesoreria = (AreaTesoreria) object;
		if (areaTesoreria.getId() != null) {
			getAreaContabileCommand().setVisible(true);
			getAreaContabileCommand().setAreaContabileLite(tesoreriaBD.caricaAreaContabileLite(areaTesoreria));
			getEliminaAreaTesoreriaCommand().setEnabled(true);
		} else {
			getAreaContabileCommand().setVisible(false);
			getEliminaAreaTesoreriaCommand().setEnabled(false);
		}

	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

}
