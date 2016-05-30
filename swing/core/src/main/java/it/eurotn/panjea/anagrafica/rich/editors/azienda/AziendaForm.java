/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.rich.editors.azienda.dao.LogoAziendaDAO;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.DatiGeograficiBinding;
import it.eurotn.rich.components.ScaledImageLabel;
import it.eurotn.rich.control.LinguaListCellRenderer;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.pdfbox.util.ExtensionFileFilter;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form Azienda.
 *
 * @author Aracno, Leonardo
 * @version 1.0, 4-mag-2006
 */
public class AziendaForm extends PanjeaAbstractForm {

	private class LoadLogoRunnable implements Runnable {

		@Override
		public void run() {
			ImageIcon logo = logoAziendaDAO.caricaLogo();
			if (logo != null) {
				logoPanel.setIcon(logo);
			}
		}

	}

	private class UploadLogoCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAD_ID = "uploadLogoCommand";

		/**
		 * Costruttore.
		 */
		public UploadLogoCommand() {
			super(COMMAD_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			ExtensionFileFilter eseguibileFileFilter = new ExtensionFileFilter(new String[] { "png" }, "Immagini png");
			fileChooser.addChoosableFileFilter(eseguibileFileFilter);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(eseguibileFileFilter);

			int returnVal = fileChooser.showDialog(Application.instance().getActiveWindow().getControl(), "Seleziona");

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				logoAziendaDAO.salvaLogo(file.getPath());
				SwingUtilities.invokeLater(new LoadLogoRunnable());
			}
		}
	}

	private JComponent codice = null;

	private LogoAziendaDAO logoAziendaDAO;
	private ScaledImageLabel logoPanel;

	private UploadLogoCommand uploadLogoCommand;

	/**
	 * Costruttore.
	 *
	 * @param formModel
	 *            form model
	 * @param formId
	 *            id del form
	 */
	public AziendaForm(final FormModel formModel, final String formId) {
		super(formModel, formId);

		uploadLogoCommand = new UploadLogoCommand();
		logoPanel = new ScaledImageLabel();
		logoPanel.setHorizontalAlignment(JLabel.CENTER);
		logoPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				uploadLogoCommand.execute();
			}
		});

		logoAziendaDAO = RcpSupport.getBean(LogoAziendaDAO.BEAN_ID);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"70dlu,4dlu,left:default, 10dlu, right:pref,4dlu,left:90dlu, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,fill:default:grow",
				"3dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,fill:150dlu");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addLabel("azienda.codice", 1);
		this.codice = builder.addBinding(bf.createBoundLabel("azienda.codice"), 3);
		codice.setFont(new Font(codice.getFont().getName(), Font.BOLD, codice.getFont().getSize()));

		builder.addPropertyAndLabel("azienda.abilitata", 5);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("azienda.denominazione")[1]).setColumns(40);
		LinguaListCellRenderer linguaListCellRenderer = new LinguaListCellRenderer();
		ComboBoxBinding localeBinding = (ComboBoxBinding) bf.createBoundComboBox("azienda.lingua",
				linguaListCellRenderer.getLingue());
		builder.addLabel("azienda.lingua", 5);
		JComboBox lingueCombo = (JComboBox) builder.addBinding(localeBinding, 7);
		lingueCombo.setRenderer(linguaListCellRenderer);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("azienda." + Azienda.PROP_PARTITA_I_V_A)[1]).setColumns(11);
		builder.addLabel("azienda.codiceFiscale", 5);
		builder.addBinding(bf.createBoundCodiceFiscale("azienda.codiceFiscale", null, null, null, null, null), 7)
		.setPreferredSize(new Dimension(180, 22));
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("sedeAzienda.sede." + SedeAnagrafica.PROP_INDIRIZZO)[1])
		.setColumns(40);
		((JTextField) builder.addPropertyAndLabel("sedeAzienda.sede.numeroCivico", 5)[1]).setColumns(5);
		builder.nextRow();

		DatiGeograficiBinding bindingDatiGeografici = (DatiGeograficiBinding) bf.createDatiGeograficiBinding(
				"sedeAzienda.sede.datiGeografici", "right:70dlu");
		builder.addBinding(bindingDatiGeografici, 1, 10, 11, 1);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("sedeAzienda.sede." + SedeAnagrafica.PROP_TELEFONO, 1)[1])
		.setColumns(13);
		((JTextField) builder.addPropertyAndLabel("sedeAzienda.sede." + SedeAnagrafica.PROP_FAX, 5)[1]).setColumns(13);
		builder.nextRow();

		builder.addPropertyAndLabel("sedeAzienda.sede." + SedeAnagrafica.PROP_INDIRIZZO_MAIL, 1);
		builder.addPropertyAndLabel("azienda.pec", 5);
		builder.nextRow();

		builder.addPropertyAndLabel("sedeAzienda.sede." + SedeAnagrafica.PROP_WEB, 1);
		builder.addLabel("azienda.codiceValuta", 5);
		builder.addBinding(bf.createBoundSearchText("azienda.codiceValuta", null, ValutaAzienda.class), 7);
		builder.nextRow();

		builder.addLabel("logo");
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panel.add(logoPanel, BorderLayout.CENTER);
		builder.addComponent(panel, 3, 18, 5, 1);

		return builder.getPanel();
	}

	@Override
	protected String getCommitCommandFaceDescriptorId() {
		return getFormModel().getId() + ".save";
	}

	@Override
	protected String getNewFormObjectCommandId() {
		return getFormModel().getId() + ".new";
	}

	@Override
	protected String getRevertCommandFaceDescriptorId() {
		return getFormModel().getId() + ".revert";
	}

	/**
	 *
	 * @return focus
	 */
	public boolean requesFocusInWindow() {
		return codice.requestFocusInWindow();
	}

	@Override
	public void setFormObject(Object formObject) {
		super.setFormObject(formObject);

		SwingUtilities.invokeLater(new LoadLogoRunnable());
	}

}
