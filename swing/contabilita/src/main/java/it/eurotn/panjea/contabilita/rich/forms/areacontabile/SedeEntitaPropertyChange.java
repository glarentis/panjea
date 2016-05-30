package it.eurotn.panjea.contabilita.rich.forms.areacontabile;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.NoteAreaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.rich.bd.IAnagraficaPagamentiBD;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.util.RcpSupport;

public class SedeEntitaPropertyChange implements FormModelPropertyChangeListeners {
	private IAnagraficaPagamentiBD anagraficaPagamentiBD;
	private IContabilitaBD contabilitaBD;

	private FormModel formModel;

	private static Logger logger = Logger.getLogger(SedeEntitaPropertyChange.class);

	/**
	 * @return Returns the anagraficaPagamentiBD.
	 */
	public IAnagraficaPagamentiBD getAnagraficaPagamentiBD() {
		return anagraficaPagamentiBD;
	}

	/**
	 * Imposta il codice pagamento per l'area Rate.
	 *
	 * @param sedePagamento
	 *            nuova sede selezionata
	 */
	private void impostaPagamento(SedePagamento sedePagamento) {
		// imposto il codice pagamento sempre, anche se non c'è un modo da
		// svuotare il campo
		formModel.getValueModel("areaRate.codicePagamento").setValue(sedePagamento.getCodicePagamento());
	}

	/**
	 * Imposta la valuta per il totale documento.
	 *
	 * @param sedePagamento
	 *            nuova sede selezionata
	 */
	private void impostaValuta(SedePagamento sedePagamento) {
		// Imposto la valuta
		Importo totaleDocumento = (Importo) formModel.getValueModel("areaContabile.documento.totale").getValue();

		Importo nuovoImporto = totaleDocumento.clone();
		String codiceValuta = sedePagamento.getSedeEntita().getCodiceValuta();
		// Se non ho la valuta sul cliente uso quella aziendale
		if (codiceValuta == null) {
			AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
			codiceValuta = aziendaCorrente.getCodiceValuta();
		}
		nuovoImporto.setCodiceValuta(codiceValuta);
		formModel.getValueModel("areaContabile.documento.totale").setValue(nuovoImporto);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (formModel.isReadOnly()) {
			logger.debug("--> Exit propertyChange. FormModel in sola lettura");
			return;
		}

		final SedeEntita sedeEntitaCorrente = (SedeEntita) evt.getNewValue();
		SedeEntita sedeEntitaPrecedente = (SedeEntita) evt.getOldValue();

		if (sedeEntitaCorrente == null) {
			logger.debug("--> Exit propertyChange");
			return;
		}
		if (sedeEntitaCorrente.equals(sedeEntitaPrecedente)) {
			logger.debug("--> Exit propertyChange");
			return;
		}

		// Se il tipo entita è azienda non carico nulla dalla sede entita
		final Documento documento = (Documento) formModel.getValueModel("areaContabile.documento").getValue();
		if (documento.getTipoDocumento().getTipoEntita() == TipoEntita.AZIENDA) {
			logger.debug("--> Exit propertyChange perchè l'entità su documento è azienda");
			return;
		}
		SedePagamento sedePagamento = anagraficaPagamentiBD.caricaSedePagamentoBySedeEntita(sedeEntitaCorrente.getId());
		impostaPagamento(sedePagamento);
		impostaValuta(sedePagamento);

		if (sedeEntitaCorrente != null) {
			NoteAreaContabile note = contabilitaBD.caricaNoteSede(sedeEntitaCorrente);

			// se arriva note[]!=null allora visualizzo il dialogo
			if (note != null && !note.isEmpty()) {
				NoteContabilitaEntitaDialog d = new NoteContabilitaEntitaDialog(note);
				d.setPreferredSize(new Dimension(400, 400));
				d.showDialog();
			}
		}
	}

	/**
	 * @param anagraficaPagamentiBD
	 *            The anagraficaPagamentiBD to set.
	 */
	public void setAnagraficaPagamentiBD(IAnagraficaPagamentiBD anagraficaPagamentiBD) {
		this.anagraficaPagamentiBD = anagraficaPagamentiBD;
	}

	/**
	 * @param contabilitaBD
	 *            the contabilitaBD to set
	 */
	public void setContabilitaBD(IContabilitaBD contabilitaBD) {
		this.contabilitaBD = contabilitaBD;
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
