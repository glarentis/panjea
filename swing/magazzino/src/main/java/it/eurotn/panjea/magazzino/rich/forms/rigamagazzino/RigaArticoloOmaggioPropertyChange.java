package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.manager.omaggio.exception.CodiceIvaPerTipoOmaggioAssenteException;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;

import org.springframework.binding.form.FormModel;

public class RigaArticoloOmaggioPropertyChange implements FormModelPropertyChangeListeners {

	private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;
	private FormModel formModel;

	/**
	 * Legge e riassegna i value model delle variazioni.
	 */
	private void aggiornaValoriVariazioniNelFormModel() {
		formModel.getValueModel("variazione1").setValue(formModel.getValueModel("variazione1").getValue());
		formModel.getValueModel("variazione2").setValue(formModel.getValueModel("variazione2").getValue());
		formModel.getValueModel("variazione3").setValue(formModel.getValueModel("variazione3").getValue());
		formModel.getValueModel("variazione4").setValue(formModel.getValueModel("variazione4").getValue());

	}

	/**
	 * Imposta le variazioni utilizzando gli sconti calcolati dalla politica prezzo. Se la politica prezzo non prevede
	 * sconti le variazioni vengono azzerate.
	 */
	private void impostaVariazioniDaPoliticaPrezzoOAzzerale() {
		PoliticaPrezzo politicaPrezzo = (PoliticaPrezzo) formModel.getValueModel("politicaPrezzo").getValue();

		if (politicaPrezzo != null && politicaPrezzo.getSconti().size() > 0) {
			Double qtaRiga = (Double) formModel.getValueModel("qta").getValue();
			RisultatoPrezzo<Sconto> risultatoSconto = politicaPrezzo.getSconti().getRisultatoPrezzo(qtaRiga);

			if (risultatoSconto != null) {
				setVariazioni(risultatoSconto.getValue());
				formModel.getValueModel("sconto1Bloccato").setValue(politicaPrezzo.isSconto1Bloccato());
				return;
			}
		}

		setVariazioni(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
	}

	/**
	 * 
	 * @param isOmaggio
	 *            se il tipo omaggio impostato è un omaggio.
	 */
	private void impostaVariazioniEPrezzoUnitarioReadOnly(boolean isOmaggio) {
		boolean sconto1Bloccato = (Boolean) formModel.getValueModel("sconto1Bloccato").getValue();
		formModel.getFieldMetadata("prezzoUnitario").setReadOnly(isOmaggio);
		formModel.getFieldMetadata("variazione1").setReadOnly(isOmaggio || sconto1Bloccato);
		formModel.getFieldMetadata("variazione2").setReadOnly(isOmaggio);
		formModel.getFieldMetadata("variazione3").setReadOnly(isOmaggio);
		formModel.getFieldMetadata("variazione4").setReadOnly(isOmaggio);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (formModel == null || evt.getNewValue() == null || evt.getNewValue().equals(evt.getOldValue())) {
			return;
		}

		aggiornaValoriVariazioniNelFormModel();
		TipoOmaggio nuovoTipoOmaggio = (TipoOmaggio) evt.getNewValue();
		boolean nuovoIsOmaggio = (nuovoTipoOmaggio != TipoOmaggio.NESSUNO);

		// se si toglie l'omaggio lascio il prezzo unitario esistente ma risetto tutte le variazioni che vengono dalla
		// politica prezzo
		if (!nuovoIsOmaggio) {
			impostaVariazioniDaPoliticaPrezzoOAzzerale();
		}

		updateCodiceIva();
		impostaVariazioniEPrezzoUnitarioReadOnly(nuovoIsOmaggio);
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            The magazzinoDocumentoBD to set.
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	/**
	 * 
	 * @param variazione1
	 *            variazione1
	 * @param variazione2
	 *            variazione2
	 * @param variazione3
	 *            variazione3
	 * @param variazione4
	 *            varaizione4
	 */
	private void setVariazioni(BigDecimal variazione1, BigDecimal variazione2, BigDecimal variazione3,
			BigDecimal variazione4) {
		formModel.getValueModel("variazione1").setValue(variazione1);
		formModel.getValueModel("variazione2").setValue(variazione2);
		formModel.getValueModel("variazione3").setValue(variazione3);
		formModel.getValueModel("variazione4").setValue(variazione4);
	}

	/**
	 * 
	 * @param sconto
	 *            Sconto
	 */
	private void setVariazioni(Sconto sconto) {
		setVariazioni(sconto.getSconto1(), sconto.getSconto2(), sconto.getSconto3(), sconto.getSconto4());
	}

	/**
	 * Aggiorna il codice iva della riga articolo, se form object è una riga articolo valida ed il formModel non è in
	 * sola lettura.
	 */
	private void updateCodiceIva() {
		if (formModel.isReadOnly() || formModel.getFormObject() == null) {
			return;
		}

		IRigaArticoloDocumento riga = (IRigaArticoloDocumento) formModel.getFormObject();
		try {
			CodiceIva codiceIva = magazzinoDocumentoBD.caricaCodiceIvaPerSostituzione(riga);
			formModel.getValueModel("codiceIva").setValue(codiceIva);
		} catch (CodiceIvaPerTipoOmaggioAssenteException e) {
			// se non ho un codice iva per il tipo omaggio scelto, reimposto nessuno nel form model e lancio una
			// eccezione per avvisare l'utente di legare un codice iva al tipo omaggio
			formModel.getValueModel("tipoOmaggio").setValue(TipoOmaggio.NESSUNO);
			PanjeaSwingUtil.checkAndThrowException(e);
		} catch (RuntimeException e) {
			formModel.getValueModel("tipoOmaggio").setValue(TipoOmaggio.NESSUNO);
			throw e;
		}
	}
}
