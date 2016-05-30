package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.InserisciRaggruppamentoArticoliCommand;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.dialogs.PanjeaFilterListSelectionDialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.selection.dialog.FilterListSelectionDialog;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class InserisciRaggruppamentoArticoliRigheOrdineCommand extends InserisciRaggruppamentoArticoliCommand {

	private Date dataConsegnaArticoli;

	@Override
	protected void doExecuteCommand() {
		FilterList<RaggruppamentoArticoli> raggruppamentiFilterList = new FilterList<RaggruppamentoArticoli>(
				GlazedLists.eventList(getMagazzinoAnagraficaBD().caricaRaggruppamenti()));
		FilterListSelectionDialog selectionDialog = new PanjeaFilterListSelectionDialog(
				"Seleziona il raggruppamento da inserire", Application.instance().getActiveWindow().getControl(),
				raggruppamentiFilterList) {

			private JDateChooser dataConsegna;

			@Override
			protected JComponent createDialogContentPane() {
				JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 5));
				rootPanel.add(super.createDialogContentPane(), BorderLayout.CENTER);

				JPanel datePanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
				datePanel.add(new JLabel(RcpSupport.getMessage("dataConsegna")));

				IDateEditor textFieldDateEditor = new JTextFieldDateEditor("dd/MM/yy", "##/##/##", '_');
				dataConsegna = new JDateChooser(textFieldDateEditor);
				dataConsegna.setEnabled(true);
				dataConsegna.getDateEditor().getUiComponent().setName("dataConsegna");
				datePanel.add(dataConsegna);

				rootPanel.add(datePanel, BorderLayout.SOUTH);
				return rootPanel;
			}

			@Override
			protected void onSelect(Object selection) {
				setDataConsegnaArticoli(dataConsegna.getDate());
				inserisciRighe((RaggruppamentoArticoli) selection);
			}

		};
		selectionDialog.setRenderer(new RaggruppamentoArticoliRender());
		selectionDialog.setFilterator(GlazedLists.textFilterator("descrizione"));
		selectionDialog.showDialog();
	}

	/**
	 * Inserisce le righe per il raggruppamento.
	 * 
	 * @param raggruppamento
	 *            raggruppamento con le righe da inserire.
	 */
	@Override
	protected void inserisciRighe(RaggruppamentoArticoli raggruppamento) {
		IOrdiniDocumentoBD ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
		AreaOrdine areaOrdine = (AreaOrdine) getParameter(AREA_DOCUMENTO_KEY);
		if (areaOrdine == null) {
			throw new IllegalArgumentException("impostare l'area ordine");
		}
		AreaRate areaRate = (AreaRate) getParameter(AREA_RATE_KEY, null);
		if (areaRate == null) {
			throw new IllegalArgumentException("Impostare l'area rate");
		}
		Integer idListinoAlternativo = null;
		Integer idListino = null;
		Integer idSedeEntita = null;
		Integer idTipoMezzo = null;
		Integer idZonaGeografica = null;
		String codiceLingua = null;
		Integer idAgente = null;
		BigDecimal percentualeScontoCommerciale = null;

		if (areaOrdine.getListinoAlternativo() != null) {
			idListinoAlternativo = areaOrdine.getListinoAlternativo().getId();
		}
		if (areaOrdine.getListino() != null) {
			idListino = areaOrdine.getListino().getId();
		}
		if (areaOrdine.getDocumento().getSedeEntita() != null) {
			idSedeEntita = areaOrdine.getDocumento().getSedeEntita().getId();
			codiceLingua = areaOrdine.getDocumento().getSedeEntita().getLingua();
		}
		if (areaOrdine.getAgente() != null) {
			idAgente = areaOrdine.getAgente().getId();
		}
		if (areaRate.getCodicePagamento() != null) {
			percentualeScontoCommerciale = areaRate.getCodicePagamento().getPercentualeScontoCommerciale();
		}

		ordiniDocumentoBD.inserisciRaggruppamentoArticoli(areaOrdine.getId(), ProvenienzaPrezzo.LISTINO, raggruppamento
				.getId(), areaOrdine.getDocumento().getDataDocumento(), idSedeEntita, idListinoAlternativo, idListino,
				areaOrdine.getDocumento().getTotale(), areaOrdine.getCodiceIvaAlternativo(), idTipoMezzo,
				idZonaGeografica, areaOrdine.getTipoAreaOrdine().isNoteSuDestinazione(), areaOrdine.getDocumento()
						.getTotale().getCodiceValuta(), codiceLingua, dataConsegnaArticoli, idAgente, areaOrdine
						.getTipologiaCodiceIvaAlternativo(), percentualeScontoCommerciale);
	}

	@Override
	protected void onButtonAttached(AbstractButton button) {
		super.onButtonAttached(button);
		button.setName("RaggruppamentoArticoliOrdineCommand");
	}

	/**
	 * @param dataConsegnaArticoli
	 *            the dataConsegnaArticoli to set
	 */
	public void setDataConsegnaArticoli(Date dataConsegnaArticoli) {
		this.dataConsegnaArticoli = dataConsegnaArticoli;
	}

}
