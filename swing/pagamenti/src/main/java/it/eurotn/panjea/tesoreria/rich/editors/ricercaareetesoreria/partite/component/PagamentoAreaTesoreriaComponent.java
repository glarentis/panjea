package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.component;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.anagrafica.rich.converter.ImportoConverter;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.pane.CollapsiblePane;

public class PagamentoAreaTesoreriaComponent extends CollapsiblePane {

	private static final long serialVersionUID = -6541961619159827238L;

	private final Pagamento pagamento;

	private OpenAreeDocumentoCommand openAreeDocumentoCommand;

	/**
	 * Costruttore.
	 * 
	 * @param pagamento
	 *            pagamento da gestire.
	 */
	public PagamentoAreaTesoreriaComponent(final Pagamento pagamento) {
		super();
		this.pagamento = pagamento;
		setStyle(CollapsiblePane.DROPDOWN_STYLE);
		setEmphasized(true);
		setLayout(new BorderLayout());
		collapse(true);

		setBackground(new Color(204, 204, 214));
		setTitleLabelComponent(getTitlePagamentoComponent());
		setTitleComponent(getOpenAreeDocumentoCommand().createButton());
		add(getDetailPagamentoComponent(), BorderLayout.CENTER);
	}

	/**
	 * Crea i componenti che contengono i dettagli del pagamento.
	 * 
	 * @return componenti creati
	 */
	private JComponent getDetailPagamentoComponent() {

		FormLayout layout = new FormLayout("left:130dlu, 5dlu,left:pref:grow,4dlu,right:d:g",
				"default,default,default,default,default,default");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		builder.setOpaque(false);
		CellConstraints cc = new CellConstraints();

		Documento documento = this.pagamento.getRata().getAreaRate().getDocumento();
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

		Rata rata = this.pagamento.getRata();

		// numero rata
		JLabel numeroRataLabel = new JLabel("Numero: " + rata.getNumeroRata().toString());
		numeroRataLabel.setIcon(RcpSupport.getIcon("number"));
		builder.add(numeroRataLabel, cc.xy(1, 4));

		// scadenza rata
		JLabel scadenzaRataLabel = new JLabel("Scadenza: n.d");
		if (rata.getDataScadenza() != null) {
			// anomalia!!! però ci possono essere dati sporchi
			scadenzaRataLabel.setText("Scadenza: " + dateFormat.format(rata.getDataScadenza()));
		}
		scadenzaRataLabel.setIcon(RcpSupport.getIcon(Date.class.getName()));
		builder.add(scadenzaRataLabel, cc.xyw(3, 4, 3));

		// totale rata
		JLabel totaleRataLabel = new JLabel("Importo: " + ObjectConverterManager.toString(rata.getImporto()));
		totaleRataLabel.setToolTipText(ObjectConverterManager.toString(rata.getImporto(), Importo.class,
				ImportoConverter.HTML_CONVERTER_CONTEXT));
		builder.add(totaleRataLabel, cc.xy(1, 5));

		// totale pagato
		JLabel totalePagatoRataLabel = new JLabel("Tot. pagato: "
				+ ObjectConverterManager.toString(rata.getTotalePagato()));
		totalePagatoRataLabel.setToolTipText(ObjectConverterManager.toString(rata.getTotalePagato(), Importo.class,
				ImportoConverter.HTML_CONVERTER_CONTEXT));
		builder.add(totalePagatoRataLabel, cc.xyw(3, 5, 3));

		// Importo forzato
		if (!(this.pagamento.getImportoForzato().getImportoInValutaAzienda().compareTo(BigDecimal.ZERO) == 0)) {
			String impForzato = "Imp. forzato: ";
			if (pagamento.isScontoFinanziario()) {
				impForzato = "Imp. scontato:";
			}
			JLabel impForzatoLabel = new JLabel(impForzato
					+ ObjectConverterManager.toString(this.pagamento.getImportoForzato()));
			impForzatoLabel.setToolTipText(ObjectConverterManager.toString(pagamento.getImportoForzato(),
					Importo.class, ImportoConverter.HTML_CONVERTER_CONTEXT));
			builder.add(impForzatoLabel, cc.xyw(3, 6, 3));
		}

		return builder.getPanel();
	}

	/**
	 * @return the openAreaDocumentoRataCommand
	 */
	public OpenAreeDocumentoCommand getOpenAreeDocumentoCommand() {
		if (openAreeDocumentoCommand == null) {
			openAreeDocumentoCommand = new OpenAreeDocumentoCommand();
			openAreeDocumentoCommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand arg0) {
				}

				@Override
				public boolean preExecution(ActionCommand command) {
					Documento documento = PagamentoAreaTesoreriaComponent.this.pagamento.getRata().getAreaRate()
							.getDocumento();
					command.addParameter(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO, documento.getId());
					return true;
				}
			});
		}

		return openAreeDocumentoCommand;
	}

	/**
	 * Crea i componenti da posizionare nel titolo.
	 * 
	 * @return componenti creati
	 */
	private JComponent getTitlePagamentoComponent() {

		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.setOpaque(false);
		rootPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

		JPanel datiPanel = new JPanel(new BorderLayout());
		datiPanel.setOpaque(false);

		// l'entita' puo' essere cliente,fornitore,banca,azienda, scelte con l'ordine di precedenza come riportato.
		Object entita = this.pagamento.getRata().getAreaRate().getDocumento().getEntita();
		if (entita == null) {
			entita = this.pagamento.getRata().getAreaRate().getDocumento().getRapportoBancarioAzienda();
		}
		if (entita == null) {
			entita = Application.instance().getApplicationContext().getBean("aziendaCorrente");
		}
		String entitaString = ObjectConverterManager.toString(entita);
		if (entitaString.length() > 40) {
			entitaString = entitaString.substring(0, 39) + "...";
		}
		JLabel labelEntita = new JLabel(entitaString, RcpSupport.getIcon(entita.getClass().getName()),
				SwingConstants.LEFT);
		datiPanel.add(labelEntita, BorderLayout.WEST);

		JLabel labelImporto = new JLabel(ObjectConverterManager.toString(pagamento.getImporto().add(
				pagamento.getImportoForzato(), 6)));
		labelImporto.setToolTipText(ObjectConverterManager.toString(
				pagamento.getImporto().add(pagamento.getImportoForzato(), 6), Importo.class,
				ImportoConverter.HTML_CONVERTER_CONTEXT));
		labelImporto.setHorizontalTextPosition(SwingConstants.LEFT);
		datiPanel.add(labelImporto, BorderLayout.EAST);

		rootPanel.add(datiPanel, BorderLayout.CENTER);

		return rootPanel;
	}
}
