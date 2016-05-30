package it.eurotn.panjea.rate.rich.editors.rate;

import it.eurotn.panjea.rate.rich.forms.rate.PagamentoRataForm;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.util.ParametriCreazionePagamento;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.dialog.MessageDialog;

public class PagamentoRataPage extends FormBackedDialogPageEditor {
	private static final String PAGE_ID = "pagamentoRataPage";

	private final ITesoreriaBD tesoreriaBD;

	/**
	 * Costruttore per la pagina.
	 * 
	 * @param tesoreriaBD
	 *            bd tesoreria
	 * 
	 */
	public PagamentoRataPage(final ITesoreriaBD tesoreriaBD) {
		super(PAGE_ID, new PagamentoRataForm(tesoreriaBD));
		this.tesoreriaBD = tesoreriaBD;
	}

	@Override
	public String getDescription() {
		return PAGE_ID + ".description";
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public boolean onSave() {

		ParametriCreazionePagamento parametriCreazionePagamento = (ParametriCreazionePagamento) getForm()
				.getFormObject();

		// controllo se l'importo del pagamento è corretto in base al residuo della rata o dell'acconto
		BigDecimal importoMaxPagamento = parametriCreazionePagamento.getPagamento().getRata().getResiduo()
				.getImportoInValutaAzienda();

		if (parametriCreazionePagamento.getPagamento().getAreaAcconto() != null
				&& parametriCreazionePagamento.getPagamento().getAreaAcconto().getId() != null) {
			BigDecimal residuoAcconto = (parametriCreazionePagamento.getPagamento().getAreaAcconto().getResiduo());

			parametriCreazionePagamento.setDataDocumento(parametriCreazionePagamento.getPagamento().getRata()
					.getAreaRate().getDocumento().getDataDocumento());

			if (residuoAcconto.compareTo(importoMaxPagamento) < 0) {
				importoMaxPagamento = residuoAcconto;
			}
		}

		if (parametriCreazionePagamento.getPagamento().getImporto().getImportoInValuta().compareTo(importoMaxPagamento) > 0) {
			DecimalFormat format = new DecimalFormat("#,##0.00");
			MessageDialog dialog = new MessageDialog("ATTENZIONE", "L'importo massimo del pagamento deve essere di "
					+ format.format(importoMaxPagamento));
			dialog.showDialog();
			return false;
		}

		List<AreaChiusure> areeChiusure = new ArrayList<AreaChiusure>();

		if (parametriCreazionePagamento.getPagamento().getAreaAcconto() != null
				&& parametriCreazionePagamento.getPagamento().getAreaAcconto().getId() != null) {
			List<AreaAcconto> acconti = new ArrayList<AreaAcconto>();
			AreaAcconto areaAcconto = parametriCreazionePagamento.getPagamento().getAreaAcconto();
			acconti.add(areaAcconto);

			List<Pagamento> pagamenti = new ArrayList<Pagamento>();
			Pagamento pagamento = new Pagamento();
			pagamento.setRata(parametriCreazionePagamento.getPagamento().getRata());
			pagamento.setImporto(parametriCreazionePagamento.getPagamento().getImporto().clone());
			pagamenti.add(pagamento);

			areeChiusure = tesoreriaBD.creaPagamentiConAcconto(pagamenti, acconti);
		} else {
			List<Pagamento> pagamenti = new ArrayList<Pagamento>();

			Pagamento pagamento = parametriCreazionePagamento.getPagamento();
			pagamento.setDataCreazione(parametriCreazionePagamento.getDataDocumento());
			pagamenti.add(pagamento);

			areeChiusure = tesoreriaBD.creaAreaChiusurePerPagamenti(parametriCreazionePagamento, pagamenti);
		}

		for (AreaChiusure areaChiusure : areeChiusure) {
			PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
					LifecycleApplicationEvent.CREATED, areaChiusure);
			Application.instance().getApplicationContext().publishEvent(event);
		}

		return true;
	}

	@Override
	public void refreshData() {
	}

}
