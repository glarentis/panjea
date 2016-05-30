package it.eurotn.panjea.magazzino.rich.editors.articolo.marchioce;

import it.eurotn.panjea.magazzino.rich.articoli.marchice.IArticoloMarchiCEDAO;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXImagePanel;
import org.jdesktop.swingx.JXImagePanel.Style;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.components.FileChooser;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;

import com.jidesoft.list.DefaultPreviewImageIcon;
import com.jidesoft.list.ImagePreviewList;
import com.toedter.calendar.JDateChooser;

public class ArticoloMarchiCEDialog extends ApplicationDialog {

	private class UploadMarchioCECommand extends ApplicationWindowAwareCommand {

		public static final String COMMAD_ID = "uploadMarchioCECommand";

		private boolean uploadImage = true;

		/**
		 * Costruttore.
		 */
		public UploadMarchioCECommand() {
			super(COMMAD_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			if (dateChooser.getDate() != null && !imageFileChooser.getText().isEmpty()) {

				boolean existImage = articoloMarchiCEDAO.checkMarchioCE(codiceArticolo, dateChooser.getDate());

				if (existImage) {
					ApplicationDialog dialog = new ConfirmationDialog("ATTENZIONE",
							"Esiste già un marchio con decorrenza " + dateChooser.getDate() + ". Sostituirlo?") {

						@Override
						protected void onCancel() {
							uploadImage = false;
							super.onCancel();
						}

						@Override
						protected void onConfirm() {
							uploadImage = true;
						}
					};
					dialog.showDialog();
				}

				if (uploadImage) {
					articoloMarchiCEDAO.salvaMarchioCE(codiceArticolo, dateChooser.getDate(),
							imageFileChooser.getText());
					updateImageList();
					imageFileChooser.setText("");
					dateChooser.setDate(null);
				}
			}
		}
	}

	public enum ViewType {
		LIST, PREVIEW
	}

	private final String codiceArticolo;

	private final IArticoloMarchiCEDAO articoloMarchiCEDAO;

	private JPanel rootPanel;
	private JXImagePanel previewCard;

	private final ImagePreviewList imagePreviewList = new ImagePreviewList();
	private final FileChooser imageFileChooser = new FileChooser();

	private final JDateChooser dateChooser = new JDateChooser("dd/MM/yy", "00/00/00", '_');
	private UploadMarchioCECommand uploadMarchioCECommand;
	private CancellaMarchioCECommand cancellaMarchioCECommand;

	private AnteprimaMarchioCECommand anteprimaMarchioCECommand;;

	/**
	 * Costruttore.
	 *
	 * @param codiceArticolo
	 *            codice articolo
	 */
	public ArticoloMarchiCEDialog(final String codiceArticolo) {
		super("Gestione marchi CEE", null);
		this.codiceArticolo = codiceArticolo;
		articoloMarchiCEDAO = (IArticoloMarchiCEDAO) Application.instance().getApplicationContext()
				.getBean("articoloMarchiCEDAO");
		setPreferredSize(new Dimension(550, 400));

		imagePreviewList.setShowDetails(ImagePreviewList.SHOW_TITLE | ImagePreviewList.SHOW_DESCRIPTION
				| ImagePreviewList.SHOW_SIZE);
		imagePreviewList.setCellDimension(new Dimension(250, 135));
	}

	@Override
	protected JComponent createDialogContentPane() {
		rootPanel = new JPanel(new CardLayout());

		updateImageList();
		JPanel listImagesCard = new JPanel(new BorderLayout());
		listImagesCard.add(createUploadComponent(), BorderLayout.NORTH);
		listImagesCard.add(new JScrollPane(imagePreviewList), BorderLayout.CENTER);
		rootPanel.add(listImagesCard, "listImages");

		previewCard = new JXImagePanel();
		previewCard.setStyle(Style.SCALED_KEEP_ASPECT_RATIO);
		rootPanel.add(previewCard, "preview");

		return rootPanel;
	}

	/**
	 * Crea il pannello per poter far l'upload di un marchio.
	 *
	 * @return componente creato
	 */
	private JComponent createUploadComponent() {

		dateChooser.setDateFormatString("dd/MM/yy");

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		panel.add(new JLabel("Immagine "), BorderLayout.WEST);
		panel.add(imageFileChooser, BorderLayout.CENTER);

		JPanel dataPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		dataPanel.add(new JLabel("Data "));
		dataPanel.add(dateChooser);
		dataPanel.add(getUploadMarchioCECommand().createButton());
		panel.add(dataPanel, BorderLayout.EAST);

		return panel;
	}

	/**
	 * @return the anteprimaMarchioCECommand
	 */
	public AnteprimaMarchioCECommand getAnteprimaMarchioCECommand() {
		if (anteprimaMarchioCECommand == null) {
			anteprimaMarchioCECommand = new AnteprimaMarchioCECommand(this);
		}

		return anteprimaMarchioCECommand;
	}

	/**
	 * @return the cancellaMarchioCECommand
	 */
	public CancellaMarchioCECommand getCancellaMarchioCECommand() {
		if (cancellaMarchioCECommand == null) {
			cancellaMarchioCECommand = new CancellaMarchioCECommand();
			cancellaMarchioCECommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand arg0) {
					updateImageList();
				}

				@Override
				public boolean preExecution(ActionCommand command) {
					if (imagePreviewList.getSelectedValue() != null) {
						command.addParameter(CancellaMarchioCECommand.PARAM_CODICE_ARTICOLO, codiceArticolo);
						command.addParameter(CancellaMarchioCECommand.PARAM_NOME_IMMAGINE,
								((DefaultPreviewImageIcon) imagePreviewList.getSelectedValue()).getTitle());
					}
					return true;
				}
			});
		}

		return cancellaMarchioCECommand;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return new AbstractCommand[] { getAnteprimaMarchioCECommand(), getCancellaMarchioCECommand(),
				getFinishCommand() };
	}

	/**
	 * @return the uploadMarchioCECommand
	 */
	public UploadMarchioCECommand getUploadMarchioCECommand() {
		if (uploadMarchioCECommand == null) {
			uploadMarchioCECommand = new UploadMarchioCECommand();
		}

		return uploadMarchioCECommand;
	}

	@Override
	protected boolean onFinish() {
		return true;
	}

	/**
	 * Cambia la vista del pannello.
	 *
	 * @param viewType
	 *            tipo di vista
	 */
	public void swicthToView(ViewType viewType) {

		CardLayout cardLayout = (CardLayout) rootPanel.getLayout();

		switch (viewType) {
		case LIST:
			cardLayout.show(rootPanel, "listImages");
			break;
		case PREVIEW:
			if (imagePreviewList.getSelectedIndex() != -1) {
				previewCard.setImage(((DefaultPreviewImageIcon) imagePreviewList.getSelectedValue()).getImageIcon()
						.getImage());
			}
			cardLayout.show(rootPanel, "preview");
			break;
		default:
			break;
		}
	}

	/**
	 * Aggiorna il componente che visualizza le immagini caricando tutte quelle pubblicate per l'articolo.
	 *
	 */
	private void updateImageList() {

		DefaultListModel listModel = new DefaultListModel();
		DateFormat dateFormatToDate = new SimpleDateFormat("yyyyMMdd");
		DateFormat dateFormatToString = new SimpleDateFormat("dd/MM/yyyy");

		Set<ImageIcon> immagini = articoloMarchiCEDAO.caricaMarchiCE(codiceArticolo);
		EventList<ImageIcon> eventList = new BasicEventList<ImageIcon>();
		eventList.addAll(immagini);
		SortedList<ImageIcon> sortedList = new SortedList<ImageIcon>(eventList, new Comparator<ImageIcon>() {

			@Override
			public int compare(ImageIcon o1, ImageIcon o2) {
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});

		for (ImageIcon imageIcon : sortedList) {

			String title = imageIcon.getDescription();

			String description = "";
			Date date = null;
			try {
				date = dateFormatToDate.parse(title);
				description = "Valida dal " + dateFormatToString.format(date);
			} catch (ParseException e) {
				logger.error("--> Errore durante la formattazione della data", e);
			}

			listModel.addElement(new DefaultPreviewImageIcon(imageIcon, title, description));
		}

		imagePreviewList.setModel(listModel);
	}

}
