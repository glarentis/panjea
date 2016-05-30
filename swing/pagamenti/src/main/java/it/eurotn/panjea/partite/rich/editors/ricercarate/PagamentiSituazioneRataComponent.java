package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.rich.converter.ImportoConverter;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.rich.bd.IRateBD;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAreeTesoreria;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.layout.TableLayoutBuilder;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.SimpleScrollPane;

public class PagamentiSituazioneRataComponent extends AbstractControlFactory {

	private ITesoreriaBD tesoreriaBD = null;
	private IRateBD rateBD = null;

	private AziendaCorrente aziendaCorrente;

	private int numeroColonne = 2;

	private JPanel rootJPanel;

	public static final String PAGAMENTO_ICON_KEY = "pagamentoSituazioneRata.icon";

	public static final String PAGAMENTO_INSOLUTO_KEY = "pagamentoSituazioneRata.insoluto";

	public static final String OPEN_AREA_CHIUSURA_KEY = "openAreaChiusuraButton";

	/**
	 * Costruttore.
	 */
	public PagamentiSituazioneRataComponent() {
		super();
		this.rateBD = RcpSupport.getBean("rateBD");
	}

	/**
	 * Costruttore.
	 * 
	 * @param numeroColonne
	 *            numero di colonne di pagamenti
	 */
	public PagamentiSituazioneRataComponent(final int numeroColonne) {
		super();
		this.numeroColonne = numeroColonne;
	}

	/**
	 * Crea il pannello per visualizzare il pagamento e le informazioni collegate.
	 * 
	 * @param pagamento
	 *            il pagamento da cui recuperare le informazioni da visualizzare
	 * @return JComponent
	 */
	@SuppressWarnings("deprecation")
	private JComponent createCenterPanel(final Pagamento pagamento) {

		TableLayoutBuilder builder = new TableLayoutBuilder();

		// effetto
		if (pagamento.getEffetto() != null && pagamento.getEffetto().getId() != null) {
			builder.separator("Effetto");
			builder.row();
			// data doc
			JLabel labelDataScadenzaEffetto = getComponentFactory().createLabel(
					"Data scadenza: " + ObjectConverterManager.toString(pagamento.getEffetto().getDataScadenza()));
			labelDataScadenzaEffetto.setIcon(RcpSupport.getIcon(Date.class.getName()));
			builder.cell(labelDataScadenzaEffetto);
			JLabel labelDataEffetto = getComponentFactory().createLabel(
					"Data effetto: " + ObjectConverterManager.toString(pagamento.getEffetto().getDataScadenza()));
			labelDataEffetto.setIcon(RcpSupport.getIcon(Date.class.getName()));
			builder.cell(labelDataEffetto);
			builder.row();
		}
		// area
		builder.separator("Documento");
		builder.row();
		Documento documento = pagamento.getAreaChiusure().getDocumento();
		// entita
		EntitaDocumento entitaDocumento = new EntitaDocumento();
		entitaDocumento.setTipoEntita(documento.getTipoDocumento().getTipoEntita());
		String entitaIconKey = "";
		switch (documento.getTipoDocumento().getTipoEntita()) {
		case AZIENDA:
			entitaDocumento.setId(aziendaCorrente.getId());
			entitaDocumento.setDescrizione(aziendaCorrente.getDenominazione());
			entitaIconKey = Azienda.class.getName();
			break;
		case BANCA:
			entitaDocumento.setId(documento.getRapportoBancarioAzienda().getId());
			entitaDocumento.setDescrizione(documento.getRapportoBancarioAzienda().getDescrizione());
			entitaIconKey = Banca.class.getName();
			break;
		case FORNITORE:
			entitaIconKey = Fornitore.class.getName();
			entitaDocumento.setId(documento.getEntita().getId());
			entitaDocumento.setCodice(documento.getEntita().getCodice());
			entitaDocumento.setDescrizione(documento.getEntita().getAnagrafica().getDenominazione());
			break;
		case CLIENTE:
			entitaIconKey = Cliente.class.getName();
			entitaDocumento.setId(documento.getEntita().getId());
			entitaDocumento.setCodice(documento.getEntita().getCodice());
			entitaDocumento.setDescrizione(documento.getEntita().getAnagrafica().getDenominazione());
			break;
		case VETTORE:
			entitaIconKey = Vettore.class.getName();
			entitaDocumento.setId(documento.getEntita().getId());
			entitaDocumento.setCodice(documento.getEntita().getCodice());
			entitaDocumento.setDescrizione(documento.getEntita().getAnagrafica().getDenominazione());
			break;
		default:
			break;
		}
		JLabel labelEntita = new JLabel(ObjectConverterManager.toString(entitaDocumento));
		labelEntita.setIcon(RcpSupport.getIcon(entitaIconKey));
		labelEntita.setName("entitaLabel");
		builder.cell(labelEntita);
		builder.row();
		// data doc
		JLabel labelDataDoc = new JLabel("Data: "
				+ ObjectConverterManager.toString(pagamento.getAreaChiusure().getDocumento().getDataDocumento()));
		labelDataDoc.setIcon(RcpSupport.getIcon(Date.class.getName()));
		labelDataDoc.setName("dataDocLabel");
		builder.cell(labelDataDoc);
		// num doc
		JLabel labelNumDoc = new JLabel("Numero:"
				+ ObjectConverterManager.toString(pagamento.getAreaChiusure().getDocumento().getCodice()));
		labelNumDoc.setIcon(RcpSupport.getIcon("number"));
		builder.cell(labelNumDoc);
		builder.row();
		// tipo doc
		JLabel labelTipoDoc = new JLabel("Tipo doc.: "
				+ ObjectConverterManager.toString(pagamento.getAreaChiusure().getDocumento().getTipoDocumento()));
		labelTipoDoc.setIcon(RcpSupport.getIcon(TipoDocumento.class.getName()));
		labelTipoDoc.setName("tipoDocLabel");
		builder.cell(labelTipoDoc);
		// importo doc
		JLabel labelImportoDoc = new JLabel("Importo: "
				+ ObjectConverterManager.toString(pagamento.getAreaChiusure().getDocumento().getTotale()));
		labelImportoDoc.setToolTipText(ObjectConverterManager.toString(pagamento.getAreaChiusure().getDocumento()
				.getTotale(), Importo.class, ImportoConverter.HTML_CONVERTER_CONTEXT));
		labelImportoDoc.setName("importoLabel");
		builder.cell(labelImportoDoc);
		// apertura area chiusura
		JButton openAreaButton = getComponentFactory().createButton("Apri");
		openAreaButton.setAction(new AbstractAction() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1185740804315038636L;

			@Override
			public void actionPerformed(ActionEvent e) {
				AreaTesoreria areaTesoreria = tesoreriaBD.caricaAreaTesoreria(pagamento.getAreaChiusure());
				LifecycleApplicationEvent event = new OpenEditorEvent(ParametriRicercaAreeTesoreria
						.creaParametriRicercaAreeTesoreria(areaTesoreria));
				Application.instance().getApplicationContext().publishEvent(event);
			}
		});
		openAreaButton.setText(RcpSupport.getMessage(OPEN_AREA_CHIUSURA_KEY));
		openAreaButton.setIcon(RcpSupport.getIcon(OPEN_AREA_CHIUSURA_KEY));
		openAreaButton.setName("ApriPagamento");
		builder.cell(openAreaButton, "align=left");
		builder.row();
		return builder.getPanel();

	}

	@Override
	protected JComponent createControl() {
		rootJPanel = getComponentFactory().createPanel(new BorderLayout());
		return rootJPanel;
	}

	/**
	 * Crea il pannello per visualizzare il pagamento e le informazioni collegate.
	 * 
	 * @param pagamento
	 *            il pagamento da cui recuperare le informazioni da visualizzare
	 * @return JComponent
	 */
	private JComponent createPagamentoComponent(Pagamento pagamento) {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(createTopComponent(pagamento), BorderLayout.NORTH);
		JComponent pagComponent = createCenterPanel(pagamento);
		GuiStandardUtils.attachBorder(pagComponent, BorderFactory.createEmptyBorder(0, 10, 10, 10));
		panel.add(pagComponent, BorderLayout.CENTER);

		// metto un padding al pannello
		GuiStandardUtils.attachBorder(panel, BorderFactory.createEtchedBorder(0));
		panel.setPreferredSize(new Dimension(80, 180));
		panel.setMaximumSize(new Dimension(80, 180));
		return panel;
	}

	/**
	 * Crea i controlli per la visualizzazione delle rate collegate a quella di riferimento.
	 * 
	 * @param rata
	 *            rata di riferimento
	 * @return controlli creati, <code>null</code> se la rata non ha rate collegate
	 */
	private JComponent createRateCollegateComponent(Rata rata) {

		JPanel rootPanel = null;

		List<Rata> rateCollegate = rateBD.caricaRateCollegate(rata);
		if (rateCollegate != null && !rateCollegate.isEmpty()) {
			rootPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
			rootPanel.add(new JLabel("Rate collegate " + rateCollegate.size() + ": "));
			for (Rata rataCollegata : rateCollegate) {
				JLabel labelRata = new JLabel();
				labelRata.setText(ObjectConverterManager.toString(rataCollegata));
				labelRata.setIcon(RcpSupport.getIcon(Rata.class.getName()));
				rootPanel.add(labelRata);
			}
		}

		return new SimpleScrollPane(rootPanel);
	}

	/**
	 * Crea il pannello per visualizzare il pagamento e le informazioni collegate.
	 * 
	 * @param pagamento
	 *            il pagamento da cui recuperare le informazioni da visualizzare
	 * @return JComponent
	 */
	@SuppressWarnings("deprecation")
	private JComponent createTopComponent(final Pagamento pagamento) {

		JPanel panel = new JPanel(new BorderLayout());

		TableLayoutBuilder builder = new TableLayoutBuilder(panel);

		builder.separator("Pagamento");
		builder.row();
		// importo
		JLabel labelImporto = new JLabel("Importo: " + ObjectConverterManager.toString(pagamento.getImporto()));
		labelImporto.setIcon(RcpSupport.getIcon(Pagamento.class.getName()));
		labelImporto.setToolTipText(ObjectConverterManager.toString(pagamento.getImporto(), Importo.class,
				ImportoConverter.HTML_CONVERTER_CONTEXT));

		// Aggiungo i dati relativi all'importo forzato
		if (pagamento.getImportoForzato().getImportoInValuta().compareTo(BigDecimal.ZERO) != 0) {
			String labelForzato = " (Forzato:";
			if (pagamento.isScontoFinanziario()) {
				labelForzato = " (Scontato:";
			}
			labelImporto.setText(labelImporto.getText() + labelForzato
					+ ObjectConverterManager.toString(pagamento.getImportoForzato()) + ")");
			labelImporto.setToolTipText(labelImporto.getToolTipText() + "<br/><b>Forzato:</b>"
					+ pagamento.getImportoForzato().getImportoInValutaAzienda());
		}

		builder.cell(labelImporto, "colSpec=198dlu");
		// data pagamento
		String dataPag = "In lavorazione";
		if (pagamento.getDataPagamento() != null) {
			dataPag = ObjectConverterManager.toString(pagamento.getDataPagamento());
		}
		JLabel labelDataPag = new JLabel("Data pagamento: " + dataPag);
		labelDataPag.setIcon(RcpSupport.getIcon(Date.class.getName()));
		builder.cell(labelDataPag, "colSpec=120dlu");
		// stato insoluto
		JLabel insolutoLabel = new JLabel(" ");
		if (pagamento.isInsoluto()) {
			insolutoLabel = new JLabel(RcpSupport.getMessage(PAGAMENTO_INSOLUTO_KEY));
			Font font = insolutoLabel.getFont();
			// bold
			insolutoLabel.setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));
			insolutoLabel.setForeground(Color.RED);
			builder.cell(insolutoLabel);
		}
		JPanel topPanel = builder.getPanel();
		topPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));
		return topPanel;
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param tesoreriaBD
	 *            the tesoreriaBD to set
	 */
	public void setTesoreriaBD(ITesoreriaBD tesoreriaBD) {
		this.tesoreriaBD = tesoreriaBD;
	}

	/**
	 * Aggiorna i controlli con una nuova lista di pagamenti da cui recuperare le informazioni da visualizzare.
	 * 
	 * @param pagamenti
	 *            i pagamenti da cui trarre le informazioni da aggiornare nei controlli
	 */
	public void updateControl(List<Pagamento> pagamenti) {

		rootJPanel.removeAll();

		if (pagamenti != null && !pagamenti.isEmpty()) {
			TableLayoutBuilder builder = new TableLayoutBuilder();

			int colIdx = 0;
			for (Pagamento pagamento : pagamenti) {
				builder.cell(createPagamentoComponent(pagamento));

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
	 * @param situazioneRata
	 *            la situazione rata da cui trarre le informazioni da visualizzare
	 */
	public void updateControl(SituazioneRata situazioneRata) {

		rootJPanel.removeAll();

		if (situazioneRata != null) {
			List<Pagamento> pagamenti = tesoreriaBD.caricaPagamenti(situazioneRata.getRata().getId());
			if (pagamenti.isEmpty()) {
				rootJPanel.repaint();
			}
			updateControl(pagamenti);

			JComponent rateCollegateComponent = createRateCollegateComponent(situazioneRata.getRata());
			if (rateCollegateComponent != null) {
				rootJPanel.add(rateCollegateComponent, BorderLayout.NORTH);
			}
		}
	}

}
