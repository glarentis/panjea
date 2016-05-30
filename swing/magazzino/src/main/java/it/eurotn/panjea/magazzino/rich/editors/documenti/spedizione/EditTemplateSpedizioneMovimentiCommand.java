package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione;

import it.eurotn.panjea.magazzino.domain.TemplateSpedizioneMovimenti;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.rich.components.htmleditor.HTMLEditorPane;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class EditTemplateSpedizioneMovimentiCommand extends ActionCommand {

	private class EditTemplateDialog extends ConfirmationDialog {

		private TemplateSpedizioneMovimenti template;

		private JTextField oggettoTextField = new JTextField();
		private HTMLEditorPane testoEditor = new HTMLEditorPane();

		/**
		 * Costruttore.
		 *
		 * @param templateSpedizioneMovimenti
		 *            template spedizione
		 */
		public EditTemplateDialog(final TemplateSpedizioneMovimenti templateSpedizioneMovimenti) {
			super("Template email di spedizione documenti", "_");
			this.template = templateSpedizioneMovimenti;

			setPreferredSize(new Dimension(850, 300));

			oggettoTextField.setText(template.getOggetto());
			testoEditor.setText(template.getTesto());
		}

		@Override
		protected JComponent createDialogContentPane() {
			FormLayout layout = new FormLayout("right:pref, 4dlu, fill:250dlu, 150dlu", "2dlu, p, 2dlu, fill:pref:grow");
			PanelBuilder builder = new PanelBuilder(layout);
			builder.setDefaultDialogBorder();
			CellConstraints cc = new CellConstraints();

			builder.addLabel("Oggetto", cc.xy(1, 2));
			builder.add(oggettoTextField, cc.xy(3, 2));

			builder.addLabel("Testo", cc.xy(1, 4));
			builder.add(testoEditor, cc.xy(3, 4));

			builder.add(createVariabiliPanel(), cc.xywh(4, 2, 1, 3));

			return builder.getPanel();
		}

		private JComponent createVariabiliPanel() {
			JPanel panel = getComponentFactory().createPanel(new VerticalLayout(4));
			panel.setBorder(BorderFactory.createTitledBorder("Variabili disponibili"));

			panel.add(new JLabel("<html><b>" + TemplateSpedizioneMovimenti.VAR_DATA_DOC
					+ "</b> - Data documento</html>"));
			panel.add(new JLabel("<html><b>" + TemplateSpedizioneMovimenti.VAR_NUMERO_DOC
					+ "</b> - Numero documento</html>"));
			panel.add(new JLabel("<html><b>" + TemplateSpedizioneMovimenti.VAR_COD_TIPO_DOC
					+ "</b> - Codice tipo doc.</html>"));
			panel.add(new JLabel("<html><b>" + TemplateSpedizioneMovimenti.VAR_DESC_TIPO_DOC
					+ "</b> - Desc. tipo doc.</html>"));
			panel.add(new JLabel("<html><b>" + TemplateSpedizioneMovimenti.VAR_DESC_STAMPA_TIPO_AREA
					+ "</b> - Desc. per stampa</html>"));
			panel.add(new JLabel("<html><b>" + TemplateSpedizioneMovimenti.VAR_ENTITA
					+ "</b> - Denominazione entità</html>"));

			return panel;
		}

		@Override
		protected String getCancelCommandId() {
			return "cancelCommand";
		}

		@Override
		protected String getFinishCommandId() {
			return "okCommand";
		}

		@Override
		protected void onConfirm() {

			this.template.setOggetto(oggettoTextField.getText());
			this.template.setTesto(testoEditor.getText());

			magazzinoAnagraficaBD.salvaTemplateSpedizioneMovimenti(template);
		}

		@Override
		protected void onInitialized() {
		}

	}

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 */
	public EditTemplateSpedizioneMovimentiCommand() {
		super("editTemplateSpedizioneMovimentiCommand");
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {

		this.magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);

		EditTemplateDialog dialog = new EditTemplateDialog(
				this.magazzinoAnagraficaBD.caricaTemplateSpedizioneMovimenti());
		dialog.showDialog();

	}

}
