package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.component;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.rich.command.AbstractDeleteCommand;

import java.awt.BorderLayout;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.pane.CollapsiblePane;

public class EffettoAreaTesoreriaComponent extends CollapsiblePane {

	private class DeleteEffettoCommand extends AbstractDeleteCommand {

		/**
		 * cancella l'effetto descritto dal pannello.
		 */
		public DeleteEffettoCommand() {
			super(null);
			this.setLabel("");
		}

		@Override
		public Object onDelete() {
			((ITesoreriaBD) RcpSupport.getBean("tesoreriaBD")).cancellaEffetto(effetto);
			return effetto;
		}

	}

	private static final long serialVersionUID = 5591137492744084639L;

	private final Effetto effetto;

	/**
	 * Costruttore.
	 * 
	 * @param effetto
	 *            effetto da gestire
	 */
	public EffettoAreaTesoreriaComponent(final Effetto effetto) {
		super();
		this.effetto = effetto;
		setStyle(CollapsiblePane.DROPDOWN_STYLE);
		setEmphasized(true);
		setLayout(new BorderLayout());
		collapse(true);
		setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));

		setBackground(new Color(204, 204, 214));
		setTitleLabelComponent(getTitleEffettoComponent());
		add(getDetailEffettoComponent(), BorderLayout.CENTER);
	}

	/**
	 * Crea i controlli per tutti i pagamenti dell'effetto.
	 * 
	 * @return componenti creati
	 */
	private JComponent createPagamentiComponent() {

		JPanel rootPanel = new JPanel(new VerticalLayout(5));
		rootPanel.setOpaque(false);

		for (Pagamento pagamento : this.effetto.getPagamenti()) {
			rootPanel.add(createPagamentoEffettoComponent(pagamento));
		}

		return rootPanel;
	}

	/**
	 * Crea i componenti per il pagamento.
	 * 
	 * @param pagamento
	 *            pagamento
	 * @return componenti creati
	 */
	private JComponent createPagamentoEffettoComponent(Pagamento pagamento) {

		FormLayout layout = new FormLayout("left:pref:grow, 5dlu,left:pref:grow,4dlu,right:d:g",
				"default,default,default,default,default,default");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		builder.setOpaque(true);
		builder.setBackground(new Color(230, 230, 250));
		CellConstraints cc = new CellConstraints();

		final Documento documento = pagamento.getRata().getAreaRate().getDocumento();
		// tipo documento
		JLabel tipoDocLabel = new JLabel(ObjectConverterManager.toString(documento.getTipoDocumento()));
		tipoDocLabel.setIcon(RcpSupport.getIcon(TipoDocumento.class.getName()));
		builder.add(tipoDocLabel, cc.xy(1, 1));

		// numero documento
		JLabel numeroDocLabel = new JLabel("Nr." + ObjectConverterManager.toString(documento.getCodice()));
		numeroDocLabel.setIcon(RcpSupport.getIcon("number"));
		builder.add(numeroDocLabel, cc.xy(3, 1));

		// data documento
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
		JLabel dataDocLabel = new JLabel(dateFormat.format(documento.getDataDocumento()));
		dataDocLabel.setIcon(RcpSupport.getIcon(Date.class.getName()));
		builder.add(dataDocLabel, cc.xy(5, 1));

		builder.add(new JLabel(" "), cc.xy(1, 2));
		builder.addSeparator("Rata", cc.xyw(1, 3, 5));

		Rata rata = pagamento.getRata();

		// numero rata
		JLabel numeroRataLabel = new JLabel("Numero: " + rata.getNumeroRata().toString());
		numeroRataLabel.setIcon(RcpSupport.getIcon("number"));
		builder.add(numeroRataLabel, cc.xy(1, 4));

		// scadenza rata
		JLabel scadenzaRataLabel = new JLabel("Scadenza: "
				+ ((rata.getDataScadenza() != null) ? dateFormat.format(rata.getDataScadenza()) : ""));
		scadenzaRataLabel.setIcon(RcpSupport.getIcon(Date.class.getName()));
		builder.add(scadenzaRataLabel, cc.xy(3, 4));

		OpenAreeDocumentoCommand openAreeDocumentoCommand = new OpenAreeDocumentoCommand();
		openAreeDocumentoCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
			@Override
			public boolean preExecution(ActionCommand command) {
				command.addParameter(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO, documento.getId());
				return documento.getId() != null;
			}
		});
		builder.add(openAreeDocumentoCommand.createButton(), cc.xy(5, 4));

		// totale rata
		JLabel totaleRataLabel = new JLabel("Importo: " + ObjectConverterManager.toString(rata.getImporto()));
		totaleRataLabel.setIcon(RcpSupport.getIcon(rata.getImporto().getCodiceValuta()));
		builder.add(totaleRataLabel, cc.xy(1, 5));

		// totale pagato
		JLabel totalePagatoRataLabel = new JLabel("Tot. pagamento: "
				+ ObjectConverterManager.toString(rata.getTotalePagato()));
		totalePagatoRataLabel.setIcon(RcpSupport.getIcon(rata.getTotalePagato().getCodiceValuta()));
		builder.add(totalePagatoRataLabel, cc.xyw(3, 5, 3));

		// Importo forzato
		if (!(pagamento.getImportoForzato().getImportoInValutaAzienda().compareTo(BigDecimal.ZERO) == 0)) {
			JLabel impForzatoLabel = new JLabel("Imp. forzato: "
					+ ObjectConverterManager.toString(pagamento.getImportoForzato()));
			impForzatoLabel.setIcon(RcpSupport.getIcon(pagamento.getImportoForzato().getCodiceValuta()));
			builder.add(impForzatoLabel, cc.xyw(3, 6, 3));
		}

		return builder.getPanel();
	}

	/**
	 * Crea i componenti che contengono i dettagli del pagamento.
	 * 
	 * @return componenti creati
	 */
	private JComponent getDetailEffettoComponent() {

		FormLayout layout = new FormLayout("left:pref:grow, 5dlu,left:pref:grow,4dlu,right:d:g",
				"default,default,default,default,default,default");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		builder.setOpaque(false);
		CellConstraints cc = new CellConstraints();
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

		// stato effetto
		JLabel statoEffettoLabel = new JLabel(RcpSupport.getMessage(this.effetto.getStatoEffetto().getClass().getName()
				+ "." + this.effetto.getStatoEffetto().name()));
		statoEffettoLabel.setIcon(RcpSupport
				.getIcon((this.effetto.getStatoEffetto().getClass().getName() + "." + this.effetto.getStatoEffetto()
						.name())));
		builder.add(statoEffettoLabel, cc.xy(1, 1));

		// giorni banca
		if (this.effetto.getDataValuta() != null) {
			JLabel giorniBancaLabel = new JLabel("Giorni banca: " + this.effetto.getGiorniBanca().toString());
			giorniBancaLabel.setIcon(RcpSupport.getIcon(Date.class.getName()));
			builder.add(giorniBancaLabel, cc.xyw(3, 1, 3));
		}

		// spese presentazione
		BigDecimal spesePres = BigDecimal.ZERO;
		if (this.effetto.getSpesePresentazione() != null) {
			spesePres = this.effetto.getSpesePresentazione();
		}
		JLabel spesePresLabel = new JLabel("Spese pres. " + decimalFormat.format(spesePres));
		spesePresLabel.setName("spesePresentazioneLabel");
		spesePresLabel.setIcon(RcpSupport.getIcon("EUR"));
		builder.add(spesePresLabel, cc.xy(1, 2));

		// data scadenza
		if (this.effetto.getDataValuta() != null) {
			JLabel dataScadLabel = new JLabel("Scadenza: "
					+ ObjectConverterManager.toString(this.effetto.getDataScadenza()));
			dataScadLabel.setIcon(RcpSupport.getIcon(Date.class.getName()));
			builder.add(dataScadLabel, cc.xy(3, 2));
		}

		// spese insoluto
		BigDecimal speseIns = BigDecimal.ZERO;
		if (this.effetto.getSpeseInsoluto() != null) {
			speseIns = this.effetto.getSpeseInsoluto();
		}
		JLabel speseInsLabel = new JLabel("Spese ins.: " + decimalFormat.format(speseIns));
		speseInsLabel.setIcon(RcpSupport.getIcon("EUR"));
		builder.add(speseInsLabel, cc.xy(5, 2));

		// pagamenti dell'effetto
		builder.add(createPagamentiComponent(), cc.xyw(1, 3, 5));

		return builder.getPanel();
	}

	/**
	 * Crea i componenti da posizionare nel titolo.
	 * 
	 * @return componenti creati
	 */
	private JComponent getTitleEffettoComponent() {

		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.setOpaque(false);
		rootPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

		JPanel datiPanel = new JPanel(new BorderLayout());
		datiPanel.setOpaque(false);

		EntitaLite entita = this.effetto.getPagamenti().iterator().next().getRata().getAreaRate().getDocumento()
				.getEntita();
		String entitaString = ObjectConverterManager.toString(entita);
		if (entitaString.length() > 40) {
			entitaString = entitaString.substring(0, 39) + "...";
		}
		JLabel labelEntita = new JLabel(entitaString, RcpSupport.getIcon(entita.getClass().getName()),
				SwingConstants.LEFT);
		datiPanel.add(labelEntita, BorderLayout.CENTER);

		DecimalFormat format = new DecimalFormat("#,##0.00");
		JLabel labelImporto = new JLabel(format.format(this.effetto.getImporto().getImportoInValutaAzienda()),
				RcpSupport.getIcon(this.effetto.getImporto().getCodiceValuta()), SwingConstants.RIGHT);
		labelImporto.setHorizontalTextPosition(SwingConstants.LEFT);
		datiPanel.add(labelImporto, BorderLayout.EAST);

		if (effetto.getDataValuta() == null) {
			rootPanel.add(new DeleteEffettoCommand().createButton(), BorderLayout.WEST);
		}
		rootPanel.add(datiPanel, BorderLayout.CENTER);

		return rootPanel;
	}

}
