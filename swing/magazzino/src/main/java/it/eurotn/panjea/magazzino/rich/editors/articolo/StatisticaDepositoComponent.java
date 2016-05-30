package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.articolo.statistiche.DisponibilitaPivotDataModel;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.StatisticaArticolo;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.pivot.PivotDataModel;
import com.jidesoft.pivot.PivotTablePane;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class StatisticaDepositoComponent extends JPanel {
	private class OpenAreaMagazzinoEditorCommand extends ApplicationWindowAwareCommand implements MouseListener {

		private static final String COMMAND_ID = "openAreaMagazzinoCommand";

		private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;

		private final AreaMagazzino areaMagazzino;

		/**
		 * Costruisce il comando per aprire l'area di magazzino selezionata.
		 * 
		 * @param magazzinoDocumentoBD
		 *            BD per caricare i dati dal service
		 * @param idAreaMagazzino
		 *            id dell'area magazzino da aprire
		 */
		public OpenAreaMagazzinoEditorCommand(final IMagazzinoDocumentoBD magazzinoDocumentoBD,
				final int idAreaMagazzino) {
			super(COMMAND_ID);
			RcpSupport.configure(this);
			setLabel("");
			this.magazzinoDocumentoBD = magazzinoDocumentoBD;
			areaMagazzino = new AreaMagazzino();
			areaMagazzino.setId(idAreaMagazzino);
		}

		@Override
		protected void doExecuteCommand() {
			AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD.caricaAreaMagazzinoFullDTO(areaMagazzino);
			LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
			Application.instance().getApplicationContext().publishEvent(event);
		}

		@Override
		public void mouseClicked(MouseEvent mouseevent) {
			doExecuteCommand();
		}

		@Override
		public void mouseEntered(MouseEvent mouseevent) {
			JLabel label = (JLabel) mouseevent.getSource();
			label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseExited(MouseEvent mouseevent) {
			JLabel label = (JLabel) mouseevent.getSource();
			label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void mousePressed(MouseEvent mouseevent) {

		}

		@Override
		public void mouseReleased(MouseEvent mouseevent) {

		}
	}

	// private final class OpenChartsCommand extends ActionCommand {
	// private final ChartModel pieChartQtaModel;
	// private final ChartModel pieChartImportoModel;
	//
	// private StatisticaChartDeposito statisticaChartDeposito = new StatisticaChartDeposito();
	//
	// public static final String COMMAND_ID = "openChartsCommand";
	//
	// /**
	// *
	// * Costruttore.
	// *
	// * @param pieChartQtaModel
	// * qta model
	// * @param pieChartImportoModel
	// * importi model
	// */
	// private OpenChartsCommand(final ChartModel pieChartQtaModel, final ChartModel pieChartImportoModel) {
	// super(COMMAND_ID);
	// RcpSupport.configure(this);
	// this.pieChartQtaModel = pieChartQtaModel;
	// this.pieChartImportoModel = pieChartImportoModel;
	// }
	//
	// @Override
	// protected void doExecuteCommand() {
	//
	// ApplicationDialog dialog = new ApplicationDialog() {
	//
	// @Override
	// protected JComponent createDialogContentPane() {
	//
	// Chart pieChart = statisticaChartDeposito.getChart();
	// pieChart.getPieSegmentRenderer().setPointLabeler(new DefaultPointLabeler());
	// pieChart.setModel(pieChartQtaModel, statisticaChartDeposito.getChartStyle());
	// JPanel qtaPanel = new JPanel(new BorderLayout());
	// qtaPanel.add(new JLabel("QUANTITA'"), BorderLayout.NORTH);
	// qtaPanel.add(pieChart, BorderLayout.CENTER);
	//
	// Chart pieChartImporto = statisticaChartDeposito.getChart();
	// pieChartImporto.getPieSegmentRenderer().setPointLabeler(new DefaultPointLabeler());
	// pieChartImporto.setModel(pieChartImportoModel, statisticaChartDeposito.getChartStyle());
	// JPanel importoPanel = new JPanel(new BorderLayout());
	// importoPanel.add(new JLabel("IMPORTI"), BorderLayout.NORTH);
	// importoPanel.add(pieChartImporto, BorderLayout.CENTER);
	//
	// JPanel rootPanel = new JPanel(new GridLayout(1, 2));
	// rootPanel.add(qtaPanel);
	// rootPanel.add(importoPanel);
	//
	// return rootPanel;
	// }
	//
	// @Override
	// protected Object[] getCommandGroupMembers() {
	// return (new AbstractCommand[] { getFinishCommand() });
	// }
	//
	// @Override
	// protected boolean onFinish() {
	// return true;
	// }
	// };
	// dialog.setPreferredSize(new Dimension(800, 600));
	// dialog.showDialog();
	// }
	// }

	private static final long serialVersionUID = -1259344237244674755L;
	public static final String ACQUISTATO = "Acquistato";
	public static final String VENDUTO = "Venduto";
	public static final String RESO_ACQ = "Reso Acquistato";

	public static final String RESO_VEN = "Reso Venduto";
	public static final String CARICO_ALTRO = "Carico altro";
	public static final String SCARICO_ALTRO = "Scarico altro";
	public static final String CARICO_TRASF = "Carico trasf.";
	public static final String SCARICO_TRASF = "Scarico trasf.";
	private final StatisticaArticolo statisticaArticolo;
	private final ComponentFactory componentFactory;
	private final DecimalFormat qtaFormat;

	private final DecimalFormat importoFormat;
	private final IMagazzinoDocumentoBD magazzinoDocumentoBD;

	/**
	 * 
	 * @param statisticaArticolo
	 *            statisticaArticolo statistica da visualizzare
	 * @param decimaliImporto
	 *            decimali per l'importo
	 * @param decimaliQta
	 *            decimali per la qta
	 * @param magazzinoDocumentoBD
	 *            bd per la gestione del documento
	 */
	public StatisticaDepositoComponent(final StatisticaArticolo statisticaArticolo, final int decimaliImporto,
			final int decimaliQta, final IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		super();
		this.statisticaArticolo = statisticaArticolo;
		setOpaque(false);
		// setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.DARK_GRAY));
		componentFactory = (ComponentFactory) Application.services().getService(ComponentFactory.class);
		String decimalPatternQta = decimaliQta == 0 ? "#,##0" : "#,##0.";
		String decimalPatternPrezzo = decimaliImporto == 0 ? "#,##0" : "#,##0.";

		qtaFormat = new DecimalFormat(decimalPatternQta + StringUtils.rightPad("", decimaliQta, "0"));
		importoFormat = new DecimalFormat(decimalPatternPrezzo + StringUtils.rightPad("", decimaliImporto, "0"));
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
		fill();
	}

	/**
	 * 
	 * @param importo
	 *            importo da formattare. Può essere null
	 * @return label con la importo formattata
	 */
	private JLabel creaImportoLabel(BigDecimal importo) {
		if (importo == null) {
			importo = BigDecimal.ZERO;
		}
		return componentFactory.createLabel(importoFormat.format(importo));
	}

	/**
	 * 
	 * @param importo
	 *            importo
	 * @param qta
	 *            qta
	 * @return label con l'importo medio formattato
	 */
	private JLabel creaImportoMedioLabel(BigDecimal importo, Double qta) {
		JLabel importoMedio = componentFactory.createLabel("");
		if (qta == null || qta == 0) {
			qta = 1.0;
		}
		if (importo == null) {
			importo = BigDecimal.ZERO;
		}
		importoMedio
				.setText(importoFormat.format(importo.divide(BigDecimal.valueOf(qta), 6, BigDecimal.ROUND_HALF_UP)));
		return importoMedio;
	}

	/**
	 * 
	 * @param qta
	 *            qta da formattare. Può essere null
	 * @return label con la qta formattata
	 */
	private JLabel creaQtaLabel(Double qta) {
		if (qta == null) {
			qta = 0.0;
		}
		return componentFactory.createLabel(qtaFormat.format(qta));
	}

	/**
	 * 
	 * @param color
	 *            colore del background del pannello
	 * @return pannello con i lcolore di sfondo impostato
	 */
	private JComponent createColorPanel(Color color) {
		JPanel pannello = componentFactory.createPanel();
		pannello.setOpaque(true);
		pannello.setBackground(color);
		return pannello;
	}

	/**
	 * Visualizza i dati della StatisticaArticolo.
	 */
	private void fill() {
		FormLayout formlayout = new FormLayout(
				"RIGHT:DEFAULT:NONE,RIGHT:5DLU:NONE,RIGHT:45DLU:NONE,FILL:4DLU:NONE,RIGHT:65DLU:NONE,FILL:8DLU:NONE,RIGHT:DEFAULT:NONE,FILL:4DLU:NONE,LEFT:DEFAULT:NONE,LEFT:4DLU:GROW,LEFT:DEFAULT:NONE,LEFT:DEFAULT:NONE",
				"CENTER:3DLU:NONE,   CENTER:DEFAULT:NONE,   CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:10DLU:NONE,CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:10DLU:NONE,CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:10DLU:NONE,CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:10DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
		CellConstraints cc = new CellConstraints();
		setLayout(formlayout);
		setOpaque(true);

		setBorder(BorderFactory.createLineBorder(Color.black));

		JLabel lblQta = componentFactory.createLabel("Qta");
		JLabel lblImporto = componentFactory.createLabel("Importo");
		JLabel lblData = componentFactory.createLabel("Data");
		JLabel lblPrezzoMedio = componentFactory.createLabel("Prz. medio");

		JLabel lblAcquistato = componentFactory.createLabel("Acquistato");
		JLabel lblVenduto = componentFactory.createLabel("Venduto");
		JLabel lblResoAcquistato = componentFactory.createLabel("ResoAcquistato");
		JLabel lblResoVenduto = componentFactory.createLabel("ResoVenduto");
		JLabel lblCaricoAltro = componentFactory.createLabel("CaricoAltro");
		JLabel lblScaricoAltro = componentFactory.createLabel("ScaricoAltro");
		JLabel lblCaricoTrasferimento = componentFactory.createLabel("CaricoTrasferimento");
		JLabel lblScaricoTrasferimento = componentFactory.createLabel("ScaricoTrasferimento");
		JLabel lblCaricoProduzione = componentFactory.createLabel("CaricoProduzione");
		JLabel lblScaricoProduzione = componentFactory.createLabel("ScaricoProduzione");
		JLabel lblInventario = componentFactory.createLabel("Inventario");
		JLabel lblGiacenza = componentFactory.createLabel("Giacenza");
		JLabel lblRotazione = componentFactory.createLabel("Indice rot.");
		JLabel lblGiacenzaMedia = componentFactory.createLabel("Giac. media");
		JLabel lblUltimoCosto = componentFactory.createLabel("UltimoCosto");

		JLabel lblAcquistatoQta = creaQtaLabel(statisticaArticolo.getQtaAcquistato());
		JLabel lblVendutoQta = creaQtaLabel(statisticaArticolo.getQtaVenduto());
		JLabel lblResoAcquistatoQta = creaQtaLabel(statisticaArticolo.getQtaResoAcquistato());
		JLabel lblResoVendutoQta = creaQtaLabel(statisticaArticolo.getQtaResoVenduto());
		JLabel lblCaricoAltroQta = creaQtaLabel(statisticaArticolo.getQtaCaricoAltro());
		JLabel lblScaricoAltroQta = creaQtaLabel(statisticaArticolo.getQtaScaricoAltro());
		JLabel lblCaricoTrasferimentoQta = creaQtaLabel(statisticaArticolo.getQtaCaricoTrasferimento());
		JLabel lblScaricoTrasferimentoQta = creaQtaLabel(statisticaArticolo.getQtaScaricoTrasferimento());
		JLabel lblCaricoProduzioneQta = creaQtaLabel(statisticaArticolo.getQtaCaricoProduzione());
		JLabel lblScaricoProduzioneQta = creaQtaLabel(statisticaArticolo.getQtaScaricoProduzione());
		JLabel lblInventarioQta = creaQtaLabel(statisticaArticolo.getQtaInventario());
		JLabel lblGiacenzaQta = creaQtaLabel(statisticaArticolo.getGiacenza());
		JLabel lblRotazioneQta = creaQtaLabel(statisticaArticolo.getIndiceRotazione().getRotazione());
		JLabel lblGiacenzaMediaQta = creaQtaLabel(statisticaArticolo.getIndiceRotazione().getGiacenzaMedia());

		JLabel lblAcquistatoImporto = creaImportoLabel(statisticaArticolo.getImportoAcquistato());
		JLabel lblVendutoImporto = creaImportoLabel(statisticaArticolo.getImportoVenduto());
		JLabel lblResoAcquistatoImporto = creaImportoLabel(statisticaArticolo.getImportoResoAcquistato());
		JLabel lblResoVendutoImporto = creaImportoLabel(statisticaArticolo.getImportoResoVenduto());
		JLabel lblCaricoAltroImporto = creaImportoLabel(statisticaArticolo.getImportoCaricoAltro());
		JLabel lblScaricoAltroImporto = creaImportoLabel(statisticaArticolo.getImportoScaricoAltro());
		JLabel lblCaricoTrasferimentoImporto = creaImportoLabel(statisticaArticolo.getImportoCaricoTrasferimento());
		JLabel lblScaricoTrasferimentoImporto = creaImportoLabel(statisticaArticolo.getImportoScaricoTrasferimento());

		JLabel lblAcquistatoMedio = creaImportoMedioLabel(statisticaArticolo.getImportoAcquistato(),
				statisticaArticolo.getQtaAcquistato());
		JLabel lblVendutoIMedio = creaImportoMedioLabel(statisticaArticolo.getImportoVenduto(),
				statisticaArticolo.getQtaVenduto());
		JLabel lblResoAcquistatoMedio = creaImportoMedioLabel(statisticaArticolo.getImportoResoAcquistato(),
				statisticaArticolo.getQtaResoAcquistato());
		JLabel lblResoVendutoMedio = creaImportoMedioLabel(statisticaArticolo.getImportoResoVenduto(),
				statisticaArticolo.getQtaResoVenduto());
		JLabel lblCaricoAltroMedio = creaImportoMedioLabel(statisticaArticolo.getImportoCaricoAltro(),
				statisticaArticolo.getQtaCaricoAltro());
		JLabel lblScaricoAltroMedio = creaImportoMedioLabel(statisticaArticolo.getImportoScaricoAltro(),
				statisticaArticolo.getQtaScaricoAltro());
		JLabel lblCaricoTrasferimentoMedio = creaImportoMedioLabel(statisticaArticolo.getImportoCaricoTrasferimento(),
				statisticaArticolo.getQtaCaricoTrasferimento());
		JLabel lblScaricoTrasferimentoMedio = creaImportoMedioLabel(
				statisticaArticolo.getImportoScaricoTrasferimento(), statisticaArticolo.getQtaScaricoTrasferimento());

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		if (statisticaArticolo.getGiacenza() != null && statisticaArticolo.getGiacenza() < 0) {
			lblGiacenzaQta.setForeground(Color.RED);
		}

		JLabel lblDataUltimaVendita = componentFactory.createLabel("");
		if (statisticaArticolo.getDataUltimaVendita() != null) {
			lblDataUltimaVendita.setText(dateFormat.format(statisticaArticolo.getDataUltimaVendita()));
		}

		JLabel lblDataUltimoAcquisto = componentFactory.createLabel("");
		if (statisticaArticolo.getDataUltimoAcquisto() != null) {
			lblDataUltimoAcquisto.setText(dateFormat.format(statisticaArticolo.getDataUltimoAcquisto()));
		}

		add(lblQta, cc.xy(3, 2));
		add(lblImporto, cc.xy(5, 2));
		add(lblPrezzoMedio, cc.xy(7, 2));
		add(lblData, cc.xy(9, 2));

		add(lblInventario, cc.xy(1, 4));
		add(lblInventarioQta, cc.xy(3, 4));

		if (statisticaArticolo.getDataInventario() != null) {
			JLabel lblDataInventario = componentFactory.createLabel("");
			lblDataInventario.addMouseListener(new OpenAreaMagazzinoEditorCommand(magazzinoDocumentoBD,
					statisticaArticolo.getIdDocumentoInventario()));
			lblDataInventario.setText(dateFormat.format(statisticaArticolo.getDataInventario()));
			lblDataInventario.setIcon(RcpSupport.getIcon("openAreaMagazzinoCommand.icon"));
			add(lblDataInventario, cc.xy(9, 4));
		}

		add(lblAcquistato, cc.xy(1, 6));
		add(createColorPanel(Color.YELLOW), cc.xy(2, 6));
		add(lblAcquistatoQta, cc.xy(3, 6));
		add(lblAcquistatoImporto, cc.xy(5, 6));
		add(lblAcquistatoMedio, cc.xy(7, 6));

		add(lblResoAcquistato, cc.xy(1, 8));
		add(createColorPanel(Color.GREEN), cc.xy(2, 8));
		add(lblResoAcquistatoQta, cc.xy(3, 8));
		add(lblResoAcquistatoImporto, cc.xy(5, 8));
		add(lblResoAcquistatoMedio, cc.xy(7, 8));

		add(lblCaricoAltro, cc.xy(1, 10));
		add(createColorPanel(Color.ORANGE), cc.xy(2, 10));
		add(lblCaricoAltroQta, cc.xy(3, 10));
		add(lblCaricoAltroImporto, cc.xy(5, 10));
		add(lblCaricoAltroMedio, cc.xy(7, 10));

		add(lblCaricoTrasferimento, cc.xy(1, 12));
		add(createColorPanel(Color.PINK), cc.xy(2, 12));
		add(lblCaricoTrasferimentoQta, cc.xy(3, 12));
		add(lblCaricoTrasferimentoImporto, cc.xy(5, 12));
		add(lblCaricoTrasferimentoMedio, cc.xy(7, 12));

		add(lblCaricoProduzione, cc.xy(1, 14));
		add(createColorPanel(new Color(20, 200, 145)), cc.xy(2, 14));
		add(lblCaricoProduzioneQta, cc.xy(3, 14));

		add(lblVenduto, cc.xy(1, 16));
		add(createColorPanel(Color.RED), cc.xy(2, 16));
		add(lblVendutoQta, cc.xy(3, 16));
		add(lblVendutoImporto, cc.xy(5, 16));
		add(lblVendutoIMedio, cc.xy(7, 16));

		add(lblResoVenduto, cc.xy(1, 18));
		add(createColorPanel(Color.BLUE), cc.xy(2, 18));
		add(lblResoVendutoQta, cc.xy(3, 18));
		add(lblResoVendutoImporto, cc.xy(5, 18));
		add(lblResoVendutoMedio, cc.xy(7, 18));

		add(lblScaricoAltro, cc.xy(1, 20));
		add(createColorPanel(Color.BLACK), cc.xy(2, 20));
		add(lblScaricoAltroQta, cc.xy(3, 20));
		add(lblScaricoAltroImporto, cc.xy(5, 20));
		add(lblScaricoAltroMedio, cc.xy(7, 20));

		add(lblScaricoTrasferimento, cc.xy(1, 22));
		add(createColorPanel(Color.MAGENTA), cc.xy(2, 22));
		add(lblScaricoTrasferimentoQta, cc.xy(3, 22));
		add(lblScaricoTrasferimentoImporto, cc.xy(5, 22));
		add(lblScaricoTrasferimentoMedio, cc.xy(7, 22));

		add(lblScaricoProduzione, cc.xy(1, 24));
		add(createColorPanel(new Color(200, 18, 50)), cc.xy(2, 24));
		add(lblScaricoProduzioneQta, cc.xy(3, 24));

		add(lblGiacenza, cc.xy(1, 26));
		add(lblGiacenzaQta, cc.xy(3, 26));

		add(lblRotazione, cc.xy(5, 26));
		add(lblRotazioneQta, cc.xy(7, 26));

		add(lblUltimoCosto, cc.xy(1, 28));
		add(lblGiacenzaMedia, cc.xy(5, 28));
		add(lblGiacenzaMediaQta, cc.xy(7, 28));

		if (statisticaArticolo.getUltimoCosto() != null
				&& statisticaArticolo.getUltimoCosto().compareTo(BigDecimal.ZERO) != 0) {
			JLabel lblUltimoCostoImporto = creaImportoLabel(statisticaArticolo.getUltimoCosto());
			add(lblUltimoCostoImporto, cc.xy(3, 28));
		}
		if (statisticaArticolo.getIdDocumentoUltimoCosto() != null) {
			if (statisticaArticolo.getDataUltimoCosto() != null) {
				JLabel lblUltimoCostoData = componentFactory.createLabel("");
				lblUltimoCostoData.setText(dateFormat.format(statisticaArticolo.getDataUltimoCosto()));
				lblUltimoCostoData.addMouseListener(new OpenAreaMagazzinoEditorCommand(magazzinoDocumentoBD,
						statisticaArticolo.getIdDocumentoUltimoCosto()));
				lblUltimoCostoData.setIcon(RcpSupport.getIcon("openAreaMagazzinoCommand.icon"));
				add(lblUltimoCostoData, cc.xy(9, 28));
			}
		}

		if (statisticaArticolo.getIdUltimoDocumentoAcquisto() != null) {
			lblDataUltimoAcquisto.addMouseListener(new OpenAreaMagazzinoEditorCommand(magazzinoDocumentoBD,
					statisticaArticolo.getIdUltimoDocumentoAcquisto()));
			lblDataUltimoAcquisto.setIcon(RcpSupport.getIcon("openAreaMagazzinoCommand.icon"));
			add(lblDataUltimoAcquisto, cc.xy(9, 6));
		}

		if (statisticaArticolo.getIdUltimoDocumentoVendita() != null) {
			lblDataUltimaVendita.addMouseListener(new OpenAreaMagazzinoEditorCommand(magazzinoDocumentoBD,
					statisticaArticolo.getIdUltimoDocumentoVendita()));
			lblDataUltimaVendita.setIcon(RcpSupport.getIcon("openAreaMagazzinoCommand.icon"));
			add(lblDataUltimaVendita, cc.xy(9, 16));
		}

		StatisticaChartDeposito chartQtaDeposito = new StatisticaChartDeposito();
		Map<String, Double> valori = new HashMap<String, Double>();

		if (statisticaArticolo.getQtaAcquistato().compareTo(0.0) != 0) {
			valori.put(ACQUISTATO, statisticaArticolo.getQtaAcquistato());
		}
		if (statisticaArticolo.getQtaVenduto().compareTo(0.0) != 0) {
			valori.put(VENDUTO, statisticaArticolo.getQtaVenduto());
		}
		if (statisticaArticolo.getQtaResoAcquistato().compareTo(0.0) != 0) {
			valori.put(RESO_ACQ, statisticaArticolo.getQtaResoAcquistato());
		}
		if (statisticaArticolo.getQtaResoVenduto().compareTo(0.0) != 0) {
			valori.put(RESO_VEN, statisticaArticolo.getQtaResoVenduto());
		}
		if (statisticaArticolo.getQtaCaricoAltro().compareTo(0.0) != 0) {
			valori.put(CARICO_ALTRO, statisticaArticolo.getQtaCaricoAltro());
		}
		if (statisticaArticolo.getQtaScaricoAltro().compareTo(0.0) != 0) {
			valori.put(SCARICO_ALTRO, statisticaArticolo.getQtaScaricoAltro());
		}
		if (statisticaArticolo.getQtaCaricoTrasferimento().compareTo(0.0) != 0) {
			valori.put(CARICO_TRASF, statisticaArticolo.getQtaCaricoTrasferimento());
		}
		if (statisticaArticolo.getQtaScaricoTrasferimento().compareTo(0.0) != 0) {
			valori.put(SCARICO_TRASF, statisticaArticolo.getQtaScaricoTrasferimento());
		}

		add(chartQtaDeposito.getChart(valori), cc.xywh(11, 2, 1, 13));

		StatisticaChartDeposito chartImportoDeposito = new StatisticaChartDeposito();
		valori = new HashMap<String, Double>();
		if (statisticaArticolo.getImportoAcquistato().compareTo(BigDecimal.ZERO) != 0) {
			valori.put(ACQUISTATO, statisticaArticolo.getImportoAcquistato().doubleValue());
		}
		if (statisticaArticolo.getImportoVenduto().compareTo(BigDecimal.ZERO) != 0) {
			valori.put(VENDUTO, statisticaArticolo.getImportoVenduto().doubleValue());
		}
		if (statisticaArticolo.getImportoResoAcquistato().compareTo(BigDecimal.ZERO) != 0) {
			valori.put(RESO_ACQ, statisticaArticolo.getImportoResoAcquistato().doubleValue());
		}
		if (statisticaArticolo.getImportoResoVenduto().compareTo(BigDecimal.ZERO) != 0) {
			valori.put(RESO_VEN, statisticaArticolo.getImportoResoVenduto().doubleValue());
		}
		if (statisticaArticolo.getImportoCaricoAltro().compareTo(BigDecimal.ZERO) != 0) {
			valori.put(CARICO_ALTRO, statisticaArticolo.getImportoCaricoAltro().doubleValue());
		}
		if (statisticaArticolo.getImportoScaricoAltro().compareTo(BigDecimal.ZERO) != 0) {
			valori.put(SCARICO_ALTRO, statisticaArticolo.getImportoScaricoAltro().doubleValue());
		}
		if (statisticaArticolo.getImportoCaricoTrasferimento().compareTo(BigDecimal.ZERO) != 0) {
			valori.put(CARICO_TRASF, statisticaArticolo.getImportoCaricoTrasferimento().doubleValue());
		}
		if (statisticaArticolo.getImportoScaricoTrasferimento().compareTo(BigDecimal.ZERO) != 0) {
			valori.put(SCARICO_TRASF, statisticaArticolo.getImportoScaricoTrasferimento().doubleValue());
		}
		add(chartImportoDeposito.getChart(valori), cc.xywh(11, 15, 1, 13));

		if (statisticaArticolo.getDisponibilita() != null && !statisticaArticolo.getDisponibilita().isEmpty()) {
			PivotDataModel pivotDataModel = new DisponibilitaPivotDataModel(statisticaArticolo);
			PivotTablePane pane = new PivotTablePane(pivotDataModel);
			pane.setFlatLayout(true);
			pane.setFieldChooserVisible(false);
			pane.setFilterFieldAreaVisible(false);
			pane.setRowFieldAreaVisible(false);
			pane.setColumnFieldAreaVisible(false);
			pane.setColumnAutoResizable(true);
			pane.setDataFieldAreaVisible(false);
			pane.autoResizeAllColumns();
			pane.setOpaque(false);
			pane.getScrollPane().setOpaque(false);
			pane.getScrollPane().setBackground(Color.red);
			pane.autoResizeAllColumns();
			pane.setMinimumSize(new Dimension(12, 215));
			pane.setPreferredSize(new Dimension(12, 215));
			add(pane, cc.xyw(1, 30, 12));
		}
	}
}
