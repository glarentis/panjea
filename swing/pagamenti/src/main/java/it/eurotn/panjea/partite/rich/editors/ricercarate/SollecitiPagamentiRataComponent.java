package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.panjea.tesoreria.solleciti.editors.StampaSollecitiDialog;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.image.EmptyIcon;
import org.springframework.richclient.layout.TableLayoutBuilder;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;

public class SollecitiPagamentiRataComponent extends AbstractControlFactory {

	private class DeleteSollecitoCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "deleteCommand";

		private Sollecito sollecito = null;

		/**
		 * Costruttore.
		 * 
		 * @param sollecito
		 *            sollecito
		 * 
		 */
		public DeleteSollecitoCommand(final Sollecito sollecito) {
			super(COMMAND_ID);
			RcpSupport.configure(this);

			this.sollecito = sollecito;
		}

		@Override
		protected void doExecuteCommand() {

			ConfirmationDialog dialog = new ConfirmationDialog("ATTENZIONE", "Cancellare il sollecito "
					+ ObjectConverterManager.toString(sollecito) + " ?") {

				@Override
				protected void onConfirm() {
					tesoreriaBD.cancellaSollecito(sollecito);
					updateControl(situazioneRata);
				}
			};
			dialog.showDialog();

		}

	}

	private class StampaInviaSollecitoCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "stampaInviaSollecitoCommand";

		private Sollecito sollecito;

		/**
		 * Costruttore.
		 * 
		 * @param sollecito
		 *            sollecito da gestire
		 * 
		 */
		public StampaInviaSollecitoCommand(final Sollecito sollecito) {
			super(COMMAND_ID);
			RcpSupport.configure(this);

			this.sollecito = sollecito;
		}

		@Override
		protected void doExecuteCommand() {

			List<Sollecito> sollecitiStampa = new ArrayList<Sollecito>();
			sollecitiStampa.add(sollecito);

			StampaSollecitiDialog dialog = RcpSupport.getBean(StampaSollecitiDialog.PAGE_ID);
			dialog.setSolleciti(sollecitiStampa);
			dialog.showDialog();
		}

	}

	private ITesoreriaBD tesoreriaBD = null;
	private int numeroColonne = 2;

	private JPanel rootJPanel;
	private SituazioneRata situazioneRata;

	public static final String PAGAMENTO_ICON_KEY = "pagamentoSituazioneRata.icon";

	public static final String PAGAMENTO_INSOLUTO_KEY = "pagamentoSituazioneRata.insoluto";

	public static final String OPEN_AREA_CHIUSURA_KEY = "openAreaChiusuraButton";

	/**
	 * Costruttore.
	 */
	public SollecitiPagamentiRataComponent() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param numeroColonne
	 *            numero di colonne di pagamenti
	 */
	public SollecitiPagamentiRataComponent(final int numeroColonne) {
		super();
		this.numeroColonne = numeroColonne;
	}

	@Override
	protected JComponent createControl() {
		rootJPanel = getComponentFactory().createPanel(new BorderLayout());
		return rootJPanel;
	}

	/**
	 * Crea il component per i solleciti del pagamento.
	 * 
	 * @param sollecito
	 *            il sollecito da cui recuperare le informazioni da mostrare
	 * @return JComponent
	 */
	private JComponent createSollecitoComponent(Sollecito sollecito) {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(createTopComponent(sollecito), BorderLayout.NORTH);
		// JComponent pagComponent = createCenterPanel(sollecito);
		// GuiStandardUtils.attachBorder(pagComponent, BorderFactory.createEmptyBorder(0, 10, 10, 10));
		// panel.add(pagComponent, BorderLayout.CENTER);

		// metto un padding al pannello
		GuiStandardUtils.attachBorder(panel, BorderFactory.createEtchedBorder(0));
		panel.setPreferredSize(new Dimension(40, 110));
		panel.setMaximumSize(new Dimension(40, 110));
		return panel;
	}

	/**
	 * Crea il component per i solleciti del pagamento.
	 * 
	 * @param sollecito
	 *            il sollecito da cui recuperare le informazioni da mostrare
	 * @return JComponent
	 */
	private JComponent createTopComponent(final Sollecito sollecito) {

		Format format = new DecimalFormat("###,###,###,##0.00");

		FormLayout layout = new FormLayout(
				"5dlu,left:pref,4dlu,50dlu,20dlu,left:pref,4dlu,left:pref,4dlu,10dlu,4dlu,10dlu",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default");
		PanelBuilder builder = new PanelBuilder(layout); // , new FormDebugPanel());

		CellConstraints cc = new CellConstraints();

		// separator
		builder.addSeparator("Sollecito", cc.xyw(2, 2, 11));

		// importo
		JLabel labelImporto = getComponentFactory().createLabel("importo");
		labelImporto.setIcon(RcpSupport.getIcon("EUR"));
		builder.add(labelImporto, cc.xy(2, 4));
		JLabel labelImportoValore = getComponentFactory().createLabel(
				format.format(sollecito.getImporto().getImportoInValutaAzienda()));
		builder.add(labelImportoValore, cc.xy(4, 4));

		// data
		Date date = new Date(sollecito.getTimeStamp());
		JLabel labelData = getComponentFactory().createLabel("data");
		labelData.setIcon(RcpSupport.getIcon(Date.class.getName()));
		builder.add(labelData, cc.xy(6, 4));
		JLabel labelDataValore = getComponentFactory().createLabel(ObjectConverterManager.toString(date));
		builder.add(labelDataValore, cc.xy(8, 4));

		// cancellazione
		DeleteSollecitoCommand deleteSollecitoCommand = new DeleteSollecitoCommand(sollecito);
		AbstractButton deleteSollecitoButton = deleteSollecitoCommand.createButton();
		deleteSollecitoButton.setToolTipText(deleteSollecitoButton.getText());
		deleteSollecitoButton.setText(null);
		builder.add(deleteSollecitoButton, cc.xy(10, 4));

		// stampa
		StampaInviaSollecitoCommand stampaInviaSollecitoCommand = new StampaInviaSollecitoCommand(sollecito);
		AbstractButton stampaSollecitoButton = stampaInviaSollecitoCommand.createButton();
		stampaSollecitoButton.setToolTipText(stampaSollecitoButton.getText());
		stampaSollecitoButton.setText(null);
		builder.add(stampaSollecitoButton, cc.xy(12, 4));

		// note
		String nota1 = sollecito.getNota() == null ? "" : sollecito.getNota();
		if (nota1.length() > 45) {
			nota1 = nota1.substring(0, 45);
		}
		JLabel noteLabel = getComponentFactory().createLabel(nota1);
		noteLabel.setIcon(RcpSupport.getIcon("note.icon"));
		builder.add(noteLabel, cc.xywh(2, 6, 11, 1));

		String nota2 = sollecito.getNota() == null ? "" : sollecito.getNota();
		if (nota2.length() > 45) {
			nota2 = nota2.substring(45, nota2.length() > 90 ? 90 : nota2.length());
			JLabel noteLabel2 = getComponentFactory().createLabel(nota2);
			noteLabel2.setIcon(EmptyIcon.SMALL);
			builder.add(noteLabel2, cc.xywh(2, 8, 11, 1));
		}

		String nota3 = sollecito.getNota() == null ? "" : sollecito.getNota();
		if (nota3.length() > 90) {
			nota3 = nota3.substring(90, nota3.length());
			JLabel noteLabel3 = getComponentFactory().createLabel(nota3);
			noteLabel3.setIcon(EmptyIcon.SMALL);
			builder.add(noteLabel3, cc.xywh(2, 10, 11, 1));
		}

		return builder.getPanel();
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

	/**
	 * Aggiorna i controlli con una nuova lista di solleciti da cui recuperare le informazioni da visualizzare.
	 * 
	 * @param solleciti
	 *            i solleciti da cui trarre le informazioni da aggiornare nei controlli
	 */
	public void updateControl(List<Sollecito> solleciti) {

		rootJPanel.removeAll();

		if (solleciti != null && !solleciti.isEmpty()) {
			TableLayoutBuilder builder = new TableLayoutBuilder();

			int colIdx = 0;
			for (Sollecito sollecito : solleciti) {
				builder.cell(createSollecitoComponent(sollecito));

				if (colIdx == numeroColonne - 1) {
					builder.row();
					colIdx = 0;
				} else {
					colIdx++;
				}
			}

			// se i pagamentoi sono dispari ne creo uno fittizio altrimenti il builder dispone l'ultimo su tutta
			// l'ultima riga
			if (colIdx == 1) {
				builder.cell();
			}

			rootJPanel.add(getComponentFactory().createScrollPane(builder.getPanel()), BorderLayout.CENTER);
		}
	}

	/**
	 * Aggiorna i controlli per la situazione rata passata.
	 * 
	 * @param situazioneRataParam
	 *            la situazione rata da cui trarre le informazioni da visualizzare
	 */
	public void updateControl(SituazioneRata situazioneRataParam) {

		if (situazioneRataParam == null) {
			return;
		}
		this.situazioneRata = situazioneRataParam;

		rootJPanel.removeAll();

		List<Sollecito> solleciti = tesoreriaBD.caricaSollecitiByRata(situazioneRata.getRata().getId());

		if (solleciti != null) {
			if (solleciti.isEmpty()) {
				rootJPanel.repaint();
			}
			updateControl(solleciti);
		}

	}

}
