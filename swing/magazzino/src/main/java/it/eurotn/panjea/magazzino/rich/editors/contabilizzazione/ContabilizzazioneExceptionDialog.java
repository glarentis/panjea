package it.eurotn.panjea.magazzino.rich.editors.contabilizzazione;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiDuplicatiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.AreeContabiliDuplicateException;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class ContabilizzazioneExceptionDialog extends ApplicationDialog {

	public static final String PAGE_ID = "contabilizzazioneExceptionDialog";

	private final ContabilizzazioneException exception;

	/**
	 * Costruttore.
	 * 
	 * @param exception
	 *            eccezione da gestire
	 */
	public ContabilizzazioneExceptionDialog(final ContabilizzazioneException exception) {
		super();
		this.exception = exception;
		this.setPreferredSize(new Dimension(850, 400));
	}

	/**
	 * Crea i controlli per visualizzare le informazioni contenute nell'eccezione
	 * {@link AreeContabiliDuplicateException}.
	 * 
	 * @param areeException
	 *            {@link AreeContabiliDuplicateException}
	 * @return controlli creati
	 */
	private JComponent createAreeContabiliDuplicateExceptionComponent(AreeContabiliDuplicateException areeException) {

		final JideTableWidget<AreaContabileDuplicateException> exceptionTable = new JideTableWidget<AreaContabileDuplicateException>(
				"areaContabileDuplicateTable", new String[] { "areaContabileEsistente.codice",
						"areaContabileEsistente.documento.entita", "areaContabileEsistente.documento.dataDocumento" },
				AreaContabileDuplicateException.class);
		exceptionTable.setRows(areeException.getAreeContabiliDuplicateException());

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		rootPanel.add(getComponentFactory().createLabel("areaContabileDuplicate.title"), BorderLayout.NORTH);
		rootPanel.add(exceptionTable.getComponent(), BorderLayout.CENTER);
		rootPanel.setPreferredSize(new Dimension(800, 150));

		exceptionTable.setPropertyCommandExecutor(new ActionCommandExecutor() {

			@Override
			public void execute() {
				if (exceptionTable.getSelectedObject() != null) {
					LifecycleApplicationEvent event = new OpenEditorEvent(exceptionTable.getSelectedObject()
							.getAreaContabileEsistente().getAreaContabileLite());
					Application.instance().getApplicationContext().publishEvent(event);
				}
			}
		});

		return rootPanel;
	}

	@Override
	protected JComponent createDialogContentPane() {
		JPanel rootPanel = getComponentFactory().createPanel(new VerticalLayout(10));

		if (exception.getSottoContiContabiliAssentiException() != null) {
			SottoContiContabiliAssentiExceptionDialog dialog = new SottoContiContabiliAssentiExceptionDialog(
					exception.getSottoContiContabiliAssentiException());
			JLabel titleLabel = getComponentFactory().createLabel(dialog.getTitle());
			JPanel exceptionPanel = dialog.getRootPanel();
			exceptionPanel.setPreferredSize(new Dimension(800, 150));

			JPanel sottoContiPanel = getComponentFactory().createPanel(new BorderLayout());
			sottoContiPanel.add(titleLabel, BorderLayout.NORTH);
			sottoContiPanel.add(exceptionPanel, BorderLayout.CENTER);
			rootPanel.add(sottoContiPanel);
		}

		if (exception.getContiEntitaAssentiException() != null) {
			ContiEntitaAssentiExceptionDialog dialog = new ContiEntitaAssentiExceptionDialog(
					exception.getContiEntitaAssentiException());
			JLabel titleLabel = getComponentFactory().createLabel(ContiEntitaAssentiExceptionDialog.PAGE_ID);
			JPanel exceptionPanel = dialog.getRootPanel();
			exceptionPanel.setPreferredSize(new Dimension(800, 200));

			JPanel contiPanel = getComponentFactory().createPanel(new BorderLayout());
			contiPanel.add(titleLabel, BorderLayout.NORTH);
			contiPanel.add(exceptionPanel, BorderLayout.CENTER);
			rootPanel.add(contiPanel);
		}

		if (exception.getAreeContabiliDuplicateException() != null) {
			JComponent areeComponent = createAreeContabiliDuplicateExceptionComponent(exception
					.getAreeContabiliDuplicateException());
			rootPanel.add(areeComponent);
		}

		if (exception.getDocumentiDuplicatiException() != null) {
			JComponent documentiComponent = createDocumentiDuplicatiExceptionComponent(exception
					.getDocumentiDuplicatiException());
			rootPanel.add(documentiComponent);
		}

		JScrollPane scroll = getComponentFactory().createScrollPane(rootPanel);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return scroll;
	}

	/**
	 * Crea i controlli per visualizzare le informazioni contenute nell'eccezione. {@link DocumentiDuplicatiException}.
	 * 
	 * @param documentiDuplicatiException
	 *            l'eccezione contenente i documenti duplicati
	 * @return il component con la lista di documenti duplicati
	 */
	private JComponent createDocumentiDuplicatiExceptionComponent(
			DocumentiDuplicatiException documentiDuplicatiException) {
		final JideTableWidget<DocumentoDuplicateException> exceptionTable = new JideTableWidget<DocumentoDuplicateException>(
				"documentiDuplicatiTable", new String[] { "documento.codice", "documento.entita",
						"documento.dataDocumento", "documento.tipoDocumento" }, DocumentoDuplicateException.class);
		exceptionTable.setRows(documentiDuplicatiException.getDocumentiDuplicatiException());

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		rootPanel.add(getComponentFactory().createLabel("documentoDuplicate.title"), BorderLayout.NORTH);
		rootPanel.add(exceptionTable.getComponent(), BorderLayout.CENTER);
		rootPanel.setPreferredSize(new Dimension(800, 150));

		return rootPanel;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return (new AbstractCommand[] { getFinishCommand() });
	}

	@Override
	protected String getTitle() {
		return RcpSupport.getMessage(PAGE_ID + ".title");
	}

	@Override
	protected boolean onFinish() {
		return true;
	}

}
