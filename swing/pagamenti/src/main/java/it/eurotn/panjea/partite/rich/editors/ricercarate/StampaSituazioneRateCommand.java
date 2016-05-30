package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.anagrafica.domain.Destinatario;
import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.SplitButton;
import it.eurotn.rich.report.ReportManager;
import it.eurotn.rich.report.StampaCommand;

import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.JideButton;

public class StampaSituazioneRateCommand extends StampaCommand {

	public static final String COMMAND_ID = "stampaSituazioneRateCommand";
	private final ReportManager reportManager = RcpSupport.getBean(ReportManager.BEAN_ID);
	private final AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);

	private List<JRadioButton> radioLayouts = new ArrayList<JRadioButton>();

	private SplitButton splitButton;

	private ParametriRicercaRatePage parametriRicercaRatePage;

	/**
	 * Costruttore.
	 *
	 * @param parametriRicercaRatePage
	 *            pagina dei parametri di ricerca
	 */
	public StampaSituazioneRateCommand(final ParametriRicercaRatePage parametriRicercaRatePage) {
		super(COMMAND_ID, COMMAND_ID);
		RcpSupport.configure(this);
		this.parametriRicercaRatePage = parametriRicercaRatePage;
	}

	@Override
	public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer buttonConfigurer) {

		JideButton mainButton = (JideButton) super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
		splitButton = new SplitButton(mainButton, SwingConstants.SOUTH);

		JidePopup popup = new JidePopup();
		popup.getContentPane().setLayout(new VerticalLayout());
		popup.getContentPane().add(createMenuComponent());
		splitButton.setMenu(popup);

		return splitButton;
	}

	/**
	 * Crea i componenti per la scelta del layout da utilizzare.
	 *
	 * @return componenti creati
	 */
	private JComponent createMenuComponent() {
		Set<String> reportLayout = reportManager.listReport("Tesoreria/ScadenzeAperte");

		JPanel radioPanel = getComponentFactory().createPanel(new VerticalLayout(4));

		ButtonGroup buttonGroup = new ButtonGroup();
		for (String report : reportLayout) {
			JRadioButton radioButton = new JRadioButton(report);
			radioButton.setAction(new AbstractAction() {
				private static final long serialVersionUID = -5897052013973164302L;

				@Override
				public void actionPerformed(ActionEvent e) {
					StampaSituazioneRateCommand.this.execute();
				}
			});
			radioButton.setText(report);
			buttonGroup.add(radioButton);
			radioPanel.add(radioButton);
			radioLayouts.add(radioButton);
		}
		if (radioLayouts.size() > 0) {
			radioLayouts.get(0).setSelected(true);
		}

		return radioPanel;
	}

	@Override
	protected Map<Object, Object> getParametri() {
		ParametriRicercaRate parametriRicerca = (ParametriRicercaRate) parametriRicercaRatePage.getBackingFormPage()
				.getFormObject();

		HashMap<Object, Object> parametri = new HashMap<Object, Object>();
		parametri.put("descAzienda", aziendaCorrente.getDenominazione());
		parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
		// stati rata
		parametri.put("statiRata", parametriRicerca.getStatiRataForQuery());
		// tipi pagamento
		parametri.put("tipiPagamento", parametriRicerca.getTipiPagamentoForQuery());
		// tipo partita
		parametri.put("tipoPartita", parametriRicerca.getTipoPartita().ordinal());
		// periodo
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		parametri.put("dataInizio", null);
		parametri.put("dataFine", null);
		if (parametriRicerca.getDataScadenza().getDataIniziale() != null) {
			parametri.put("dataInizio", dateFormat.format(parametriRicerca.getDataScadenza().getDataIniziale()));
		}
		if (parametriRicerca.getDataScadenza().getDataFinale() != null) {
			parametri.put("dataFine", dateFormat.format(parametriRicerca.getDataScadenza().getDataFinale()));
		}
		// entita e sede
		parametri.put("idEntita", null);
		parametri.put("idSedeEntita", null);
		if (parametriRicerca.getEntita() != null) {
			parametri.put("idEntita", parametriRicerca.getEntita().getId());
			if (parametriRicerca.getSedeEntita() != null) {
				parametri.put("idSedeEntita", parametriRicerca.getSedeEntita().getId());
			}
		}
		// codice pagamento
		parametri.put("idCodicePagamento", null);
		if (parametriRicerca.getCodicePagamento() != null) {
			parametri.put("idCodicePagamento", parametriRicerca.getCodicePagamento().getId());
		}

		// importi e valuta
		parametri.put("importoIniziale", null);
		parametri.put("importoFinale", null);
		parametri.put("codiceValuta", null);
		if (parametriRicerca.getImportoIniziale() != null) {
			parametri.put("importoIniziale", parametriRicerca.getImportoIniziale());
		}
		if (parametriRicerca.getImportoFinale() != null) {
			parametri.put("importoFinale", parametriRicerca.getImportoFinale());
		}
		if (parametriRicerca.getCodiceValuta() != null && !parametriRicerca.getCodiceValuta().isEmpty()) {
			parametri.put("codiceValuta", parametriRicerca.getCodiceValuta());
		}

		// rapporto bancario
		parametri.put("idRapportoBancario", null);
		if (parametriRicerca.getRapportoBancarioAzienda() != null) {
			parametri.put("idRapportoBancario", parametriRicerca.getRapportoBancarioAzienda().getId());
		}

		// banca entità
		parametri.put("idBancaEntita", null);
		if (parametriRicerca.getBancaEntita() != null) {
			parametri.put("idBancaEntita", parametriRicerca.getBancaEntita().getId());
		}

		// categoria entità
		parametri.put("idCategoriaEntita", null);
		if (parametriRicerca.getCategoriaEntita() != null) {
			parametri.put("idCategoriaEntita", parametriRicerca.getCategoriaEntita().getId());
		}

		// zona geografica
		parametri.put("idZona", null);
		if (parametriRicerca.getZonaGeografica() != null) {
			parametri.put("idZona", parametriRicerca.getZonaGeografica().getId());
		}

		// agente
		parametri.put("idAgente", null);
		if (parametriRicerca.getAgente() != null) {
			parametri.put("idAgente", parametriRicerca.getAgente().getId());
		}

		parametri.put("stampaDettaglio", true);

		return parametri;
	}

	@Override
	protected ParametriMail getParametriMail() {
		ParametriRicercaRate parametriRicerca = (ParametriRicercaRate) parametriRicercaRatePage.getBackingFormPage()
				.getFormObject();
		ParametriMail parametri = super.getParametriMail();
		if (parametriRicerca.getEntita() != null
				&& parametriRicerca.getEntita().getAnagrafica().getSedeAnagrafica().getIndirizzoMail() != null
				&& !parametriRicerca.getEntita().getAnagrafica().getSedeAnagrafica().getIndirizzoMail().isEmpty()) {

			Destinatario d = new Destinatario();
			d.setEmail(parametriRicerca.getEntita().getAnagrafica().getSedeAnagrafica().getIndirizzoMail());
			parametri.getDestinatari().add(d);
		}
		return parametri;
	}

	@Override
	protected String getReportName() {
		return "Situazione rate";
	}

	@Override
	protected String getReportPath() {
		String reportName = "";
		for (JRadioButton radioButton : radioLayouts) {
			if (radioButton.isSelected()) {
				reportName = radioButton.getText();
				break;
			}
		}
		return "Tesoreria/ScadenzeAperte/" + reportName;
	}

}